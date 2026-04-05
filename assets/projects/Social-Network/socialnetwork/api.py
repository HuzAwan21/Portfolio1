from django.contrib.auth import logout
from django.db.models import Q, Exists, OuterRef, When, IntegerField, FloatField, Count, ExpressionWrapper, Case, Value, F, Prefetch
from django.db.models.fields import return_None

from fame.models import Fame, FameLevels, FameUsers, ExpertiseAreas
from socialnetwork.models import Posts, SocialNetworkUsers, UserRatings
from collections import defaultdict


# general methods independent of HTML and REST views
# should be used by REST and HTML views

def _get_social_network_user(user) -> SocialNetworkUsers:
    """Given a FameUser, gets the social network user from the request. Assumes that the user is authenticated."""
    try:
        user = SocialNetworkUsers.objects.get(id=user.id)
    except SocialNetworkUsers.DoesNotExist:
        raise PermissionError("User does not exist")
    return user


def timeline(user: SocialNetworkUsers, start: int = 0, end: int = None, published=True, community_mode=False):
    """Get the timeline of the user. Assumes that the user is authenticated."""

    if community_mode:
        # T4
        # in community mode, posts of communities are displayed if ALL of the following criteria are met:
        # 1. the author of the post is a member of the community
        # 2. the user is a member of the community
        # 3. the post contains the community’s expertise area
        # 4. the post is published or the user is the author

        # Get all communities the user is a member of
        user_communities = user.communities.all()
        if not user_communities.exists():
            return Posts.objects.none()  # No communities, so no posts should be shown

        community_posts_q = Q()
        for community in user_communities:
            community_posts_q |= (
                    Q(author__communities=community) &
                    Q(expertise_area_and_truth_ratings=community)
            )

        posts = Posts.objects.filter(
            community_posts_q &
            (Q(published=published) | Q(author=user))
        ).distinct().order_by("-submitted")

    #already given:
    else:
        # in standard mode, posts of followed users are displayed
        _follows = user.follows.all()
        posts = Posts.objects.filter(
            (Q(author__in=_follows) & Q(published=published)) | Q(author=user)
        ).order_by("-submitted")
    if end is None:
        return posts[start:]
    else:
        return posts[start:end + 1]


def search(keyword: str, start: int = 0, end: int = None, published=True):
    """Search for all posts in the system containing the keyword. Assumes that all posts are public"""
    posts = Posts.objects.filter(
        Q(content__icontains=keyword)
        | Q(author__email__icontains=keyword)
        | Q(author__first_name__icontains=keyword)
        | Q(author__last_name__icontains=keyword),
        published=published,
    ).order_by("-submitted")
    if end is None:
        return posts[start:]
    else:
        return posts[start:end + 1]


def follows(user: SocialNetworkUsers, start: int = 0, end: int = None):
    """Get the users followed by this user. Assumes that the user is authenticated."""
    _follows = user.follows.all()
    if end is None:
        return _follows[start:]
    else:
        return _follows[start:end + 1]


def followers(user: SocialNetworkUsers, start: int = 0, end: int = None):
    """Get the followers of this user. Assumes that the user is authenticated."""
    _followers = user.followed_by.all()
    if end is None:
        return _followers[start:]
    else:
        return _followers[start:end + 1]


def follow(user: SocialNetworkUsers, user_to_follow: SocialNetworkUsers):
    """Follow a user. Assumes that the user is authenticated. If user already follows the user, signal that."""
    if user_to_follow in user.follows.all():
        return {"followed": False}
    user.follows.add(user_to_follow)
    user.save()
    return {"followed": True}


def unfollow(user: SocialNetworkUsers, user_to_unfollow: SocialNetworkUsers):
    """Unfollow a user. Assumes that the user is authenticated. If user does not follow the user anyway, signal that."""
    if user_to_unfollow not in user.follows.all():
        return {"unfollowed": False}
    user.follows.remove(user_to_unfollow)
    user.save()
    return {"unfollowed": True}


def submit_post(
        user: SocialNetworkUsers,
        content: str,
        cites: Posts = None,
        replies_to: Posts = None,
):
    """Submit a post for publication. Assumes that the user is authenticated.
    returns a tuple of three elements:
    1. a dictionary with the keys "published" and "id" (the id of the post)
    2. a list of dictionaries containing the expertise areas and their truth ratings
    3. a boolean indicating whether the user was banned and logged out and should be redirected to the login page
    """

    # create post instance:
    post = Posts.objects.create(
        content=content,
        author=user,
        cites=cites,
        replies_to=replies_to,
    )
    # post ist ein Objekt mit einem Feld "published", das Standardmäßig auf "False" gesetzt ist

    # Klassifiziere den Inhalt in Fachbereiche und prüfe auf Bullshit:
    # Die Methode liefert:
    # - ein bool: ob mindestens ein Bereich Bullshit enthält
    # - eine Liste der erkannten Fachbereiche
    _at_least_one_expertise_area_contains_bullshit, _expertise_areas = (
        post.determine_expertise_areas_and_truth_ratings()
    )

    # Publishe nur, wenn keiner der Bereiche Bullshit enthält
    post.published = not _at_least_one_expertise_area_contains_bullshit

    redirect_to_logout = False

    # AUFGABE 1: Nur wenn die AI "veröffentlichen erlaubt", führen wir unsere zusätzliche Prüfung durch:
    if post.published:
        expertise_area_list = [
            single_expertise_element['expertise_area'] for single_expertise_element in _expertise_areas
        ]

        # Prüfe, ob der User in einem dieser Bereiche negative Reputation hat
        user_negative_fame = Fame.objects.filter(
            user=user,
            expertise_area__in=expertise_area_list,
            # __in --> verhält sich wie ein SQL-Query mit Iteration: WHERE expertise_area IN (...)
            fame_level__numeric_value__lt=0
        )

        # Falls negative Reputation -> Veröffentlichung verhindern
        if user_negative_fame.exists():
            post.published = False
    else:
        negative_areas = [
            expertise_element['expertise_area']
            for expertise_element in _expertise_areas
            if (expertise_element['truth_rating'] is not None and
                expertise_element['truth_rating'].numeric_value < 0)
        ]
        redirect_to_logout = adjust_fame_rating_of_user(user, negative_areas)

    # Remove user from communities where fame level dropped below Super Pro
    super_pro_level = FameLevels.objects.get(name="Super Pro").numeric_value
    for community in user.communities.all():
        fame_entry = Fame.objects.filter(user=user, expertise_area=community).first()
        if not fame_entry or fame_entry.fame_level.numeric_value < super_pro_level:
            leave_community(user, community)

    # Ab hier keine Veränderungen mehr
    post.save()

    return (
        {"published": post.published, "id": post.id},
        _expertise_areas,
        redirect_to_logout
    )


# Teil von AUFGABE 2
def adjust_fame_rating_of_user(user: SocialNetworkUsers, negative_expertise_areas):
    """Adjust the fame rating of a user based on the negative expertise areas."""

    for area in negative_expertise_areas:
        try:
            # T2a
            # User hat Eintrag für Fame Level in diesem Bereich → setze runter
            fame_entry = Fame.objects.get(user=user, expertise_area=area)       # wenn user noch nicht diese expertise_area hat, dann wird eine DoesNotExist Exception geworfen
            current_level_of_fame = fame_entry.fame_level

            one_level_lower = FameLevels.get_next_lower_fame_level(current_level_of_fame)       # wirft ValueError, wenn es kein Level mehr gibt, das niedriger ist
            if one_level_lower:
                fame_entry.fame_level = one_level_lower
                fame_entry.save()

        except Fame.DoesNotExist:
            # T2b
            # Kein Eintrag vorhande -> neuen mit "Confuser"
            confuser_level = FameLevels.objects.get(name="Confuser")
            Fame.objects.create(user=user, expertise_area=area, fame_level=confuser_level)

        except ValueError:
            # T2c
            # User ist bereits auf dem niedrigsten Fame-Level, also logout und User bannen - extension: auch aus Communities entfernen
            user.is_banned = True
            user.is_active = False
            user.communities.clear()
            user.save()
            Posts.objects.filter(author=user).update(published=False)
            return True
    return False  # User wurde nicht gebannt, also kann er online bleiben / kein redirect to login notwendig


def rate_post(
        user: SocialNetworkUsers, post: Posts, rating_type: str, rating_score: int
):
    """Rate a post. Assumes that the user is authenticated. If user already rated the post with the given rating_type,
    update that rating score."""
    user_rating = None
    try:
        user_rating = user.userratings_set.get(post=post, rating_type=rating_type)
    except user.userratings_set.model.DoesNotExist:
        pass

    if user == post.author:
        raise PermissionError(
            "User is the author of the post. You cannot rate your own post."
        )

    if user_rating is not None:
        # update the existing rating:
        user_rating.rating_score = rating_score
        user_rating.save()
        return {"rated": True, "type": "update"}
    else:
        # create a new rating:
        user.userratings_set.add(
            post,
            through_defaults={"rating_type": rating_type, "rating_score": rating_score},
        )
        user.save()
        return {"rated": True, "type": "new"}


def fame(user: SocialNetworkUsers):
    """Get the fame of a user. Assumes that the user is authenticated."""
    try:
        user = SocialNetworkUsers.objects.get(id=user.id)
    except SocialNetworkUsers.DoesNotExist:
        raise ValueError("User does not exist")

    return user, Fame.objects.filter(user=user)


def bullshitters():
    """Return a Python dictionary mapping each existing expertise area in the fame profiles to a list of the users
    having negative fame for that expertise area. Each list should contain Python dictionaries as entries with keys
    ``user'' (for the user) and ``fame_level_numeric'' (for the corresponding fame value), and should be ranked, i.e.,
    users with the lowest fame are shown first, in case there is a tie, within that tie sort by date_joined
    (most recent first). Note that expertise areas with no expert may be omitted.
    Project Description: Note that expertise areas with no bullshitters may be omitted.
    """
    # all Fame entries with negative fame, joined with corresponding user, expertise area and fame level
    negative_fame = Fame.objects.filter(fame_level__numeric_value__lt=0).select_related('user', 'expertise_area',
                                                                                        'fame_level')

    result = defaultdict(list)
    for fame_entry in negative_fame:  # user, expertise_area, und fame_level attributes
        area_obj = fame_entry.expertise_area
        result[area_obj].append({
            "user": fame_entry.user,
            "fame_level_numeric": fame_entry.fame_level.numeric_value
        })

    # Sort: fame_level (ascending), then date_joined
    for area in result:
        result[area].sort(key=lambda f_entry: (f_entry["fame_level_numeric"], -f_entry["user"].date_joined.timestamp()))

    return dict(result) # ohne dict() -> Fehler

def experts():
    """Return a Python dictionary mapping each existing expertise area in the fame profiles to a list of the users
    having more than superPro fame for that expertise area. Each list should contain Python dictionaries as entries with keys
    ``user'' (for the user) and ``fame_level_numeric'' (for the corresponding fame value), and should be ranked, i.e.,
    users with the highest fame are shown first, in case there is a tie, within that tie sort by date_joined
    (most recent first). Note that expertise areas with no expert may be omitted.
    """
    super_pro_level = FameLevels.objects.get(name="Super Pro").numeric_value
    # all Fame entries with fame >= SuperPro, joined with corresponding user, expertise area and fame level
    experts = Fame.objects.filter(fame_level__numeric_value__gte=super_pro_level).select_related('user', 'expertise_area',
                                                                                        'fame_level')
    result = defaultdict(list)
    for fame_entry in experts:  # user, expertise_area, und fame_level attributes
        area_obj = fame_entry.expertise_area
        result[area_obj].append({
            "user": fame_entry.user,
            "fame_level_numeric": fame_entry.fame_level.numeric_value
        })

    # Sortiere erst nach fame_level (absteigen (höchstes zuerst), dann nach date_joined
    for area in result:
        result[area].sort(key=lambda f_entry: (-f_entry["fame_level_numeric"], -f_entry["user"].date_joined.timestamp()))

    return dict(result) # ohne dict() -> Fehler


def join_community(user: SocialNetworkUsers, community: ExpertiseAreas):
    """Join a specified community. Note that this method does not check whether the user is eligible for joining the
    community.
    """
    if community in user.communities.all():
        return {"joined": False, "message": "You are already a member of this community"}
    else:
        # Allow joining if user has fame_level numeric_value >= super_pro_level
        super_pro_level = FameLevels.objects.get(name="Super Pro").numeric_value
        if Fame.objects.filter(
                user=user,
                expertise_area=community,
                fame_level__numeric_value__gte=super_pro_level
        ).exists():
            user.communities.add(community)
            user.save()
            return {"joined": True, "message": "Welcome to the community!"}
        else:
            return {"joined": False, "message": "You don't have sufficient fame to join this community."}


def leave_community(user: SocialNetworkUsers, community: ExpertiseAreas):
    """Leave a specified community."""
    if community not in user.communities.all():
        return {"left": False, "massage:": "You are not a member of this community."}
    else:
        user.communities.remove(community)
        user.save()
        return {"left": True, "message": "You have left this community."}
    

def similar_users(user: SocialNetworkUsers):
    """Compute the similarity of user with all other users. The method returns a QuerySet of FameUsers annotated
    with an additional field 'similarity'. Sort the result in descending order according to 'similarity', in case
    there is a tie, within that tie sort by date_joined (most recent first)"""

    def get_userfame(to_be_checked_user, to_be_checked_exp_area_id):
        """Helper function to get the fame value of a user in a specific expertise area.
        Does not throw an exception if the user has no fame value in that area, but returns infinity instead."""
        try:
            to_be_checked_user_fame = Fame.objects.get(user=to_be_checked_user, expertise_area_id=to_be_checked_exp_area_id)
            return to_be_checked_user_fame.fame_level.numeric_value
        except Fame.DoesNotExist:
            return float('inf')         # f(ui,e) returns infinity if no fame value

    # all expertise areas of the given user
    user_expertise_areas = Fame.objects.filter(user=user).values_list('expertise_area', flat=True)

    # For each other user, calculate similarity score
    similarity_data = []
    other_users = FameUsers.objects.exclude(id=user.id)

    total_areas = len(user_expertise_areas)  # represents the E_i in the formula
    for other_user in other_users:
        similar_areas_count = 0
        for expertise_area_id in user_expertise_areas:      # check for each expertise area both of our current users

            user_fame_value = get_userfame(user, expertise_area_id)
            other_user_fame_value = get_userfame(other_user, expertise_area_id)

            if user_fame_value == float('inf') and other_user_fame_value == float('inf'):   # Both have NO fame values, consider as similar
                similar_areas_count += 1
            elif user_fame_value != float('inf') and other_user_fame_value != float('inf'): # Both HAVE fame values, check difference
                if abs(user_fame_value - other_user_fame_value) <= 100:
                    similar_areas_count += 1
            # elif: If one has inf and other doesn't, they are not similar (difference > 100)

        # Calculate similarity score: s(u_i,u_j) = (1/|E_i|) * sum of sigma values
        similarity_score = similar_areas_count / total_areas if total_areas > 0 else 0

        if similarity_score > 0:  # Only include users with non-zero similarity
            similarity_data.append((other_user, similarity_score))

    # Sort by similarity score (descending), then by date_joined (most recent first)
    similarity_data.sort(key=lambda pair_user_score: (-pair_user_score[1], -pair_user_score[0].date_joined.timestamp()))

    ordered_user_ids = [user.id for user, _ in similarity_data]

    # if no similar users found
    if not ordered_user_ids:
        return FameUsers.objects.none()

    similarity_dict = {user.id: similarity_score for (user, similarity_score) in similarity_data}

    # Use Case/When to annotate similarity scores for the query, used as "lookup", to determine similarity score for each user
    when_conditions = [
        When(id=user_id, then=Value(similarity_dict[user_id]))
        for user_id in ordered_user_ids
    ]

    result_queryset = FameUsers.objects.filter(
        id__in=ordered_user_ids
    ).annotate(
        similarity=Case(
            *when_conditions,
            default=Value(0.0),
            output_field=FloatField()
        )
    ).order_by('-similarity', '-date_joined')

    return result_queryset

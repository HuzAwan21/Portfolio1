from django.contrib.auth.decorators import login_required
from django.shortcuts import render, redirect
from django.urls import reverse
from django.views.decorators.http import require_http_methods

from socialnetwork import api
from socialnetwork.api import _get_social_network_user
from socialnetwork.models import SocialNetworkUsers, ExpertiseAreas, Posts
from fame.models import FameLevels
from socialnetwork.serializers import PostsSerializer




@require_http_methods(["GET"])
@login_required
def timeline(request):
    # using the serializer to get the data, then use JSON in the template!
    # avoids having to do the same thing twice

    # initialize community mode to False the first time in the session
    if 'community_mode' not in request.session:
        request.session['community_mode'] = False

    keyword = request.GET.get("search", "")
    published = request.GET.get("published", True)
    error = request.GET.get("error", None)

    # get the user and communities:
    user = _get_social_network_user(request.user)
    community_mode = request.session.get('community_mode', False)

    # if keyword is not empty, use search method of API:
    if keyword and keyword != "":
        # Get the posts from the API based on the search keyword
        # Note: The 'published' parameter is used to filter the posts based on their publication status
        # and the 'filter_posts' function is used to filter the posts based on community mode
        posts = PostsSerializer(
            api.search(
                keyword,
                published=published
            ),
            many=True
        ).data

    else:  # otherwise, use timeline method of API:
        # Get the posts from the API
        posts = PostsSerializer(
            api.timeline(
                user,
                published=published,
                community_mode=community_mode
            ),
            many=True,
        ).data

        # Prepare the context for rendering the template
        # Note: The 'followers' field is populated with the IDs of the users that the current user follows
    context = {
        "posts": posts,
        "searchkeyword": keyword if keyword else "",
        "error": error,
        "followers": list(api.follows(user).values_list('id', flat=True)),
        "community_mode": community_mode,
    }

    return render(request, "timeline.html", context=context)


@require_http_methods(["POST"])
@login_required
def follow(request):
    user = _get_social_network_user(request.user)
    user_to_follow = SocialNetworkUsers.objects.get(id=request.POST.get("user_id"))
    api.follow(user, user_to_follow)
    return redirect(reverse("sn:timeline"))


@require_http_methods(["POST"])
@login_required
def unfollow(request):
    user = _get_social_network_user(request.user)
    user_to_unfollow = SocialNetworkUsers.objects.get(id=request.POST.get("user_id"))
    api.unfollow(user, user_to_unfollow)
    return redirect(reverse("sn:timeline"))

@require_http_methods(["GET"])
@login_required
def followers_view(request):
    user = _get_social_network_user(request.user)
    followers = api.followers(user)
    context = {
        "followers": followers
    }
    return render(request, "followers.html", context)

@require_http_methods(["GET"])
@login_required
def follows_view(request):
    user = _get_social_network_user(request.user)
    followed = api.follows(user)
    context = {
        "follows": followed
    }
    return render(request, "follows.html", context)

@require_http_methods(["GET"])
@login_required
def bullshitters(request):
    # Call the API to get the list of bullshitters
    bullshit_users = api.bullshitters()
    context = {
        "bullshitters": bullshit_users,
    }
    return render(request, "bullshitters.html", context)

@require_http_methods(["GET"])
@login_required
def experts(request):
    expert_users = api.experts()
    context = {
        "experts": expert_users,
    }
    return render(request, "experts.html", context)

@require_http_methods(["POST"])
@login_required
def toggle_community_mode(request):
    # Toggle the community mode in the session
    current_mode = request.session.get("community_mode", False)
    request.session["community_mode"] = not current_mode
    return redirect(reverse("sn:timeline"))

@require_http_methods(["POST"])
@login_required
def join_community(request):
    user = _get_social_network_user(request.user)
    community_id = request.POST.get("community_id")
    try:
        community = ExpertiseAreas.objects.get(id=community_id)
    except ExpertiseAreas.DoesNotExist:
        return redirect(reverse("sn:communities"))
    api.join_community(user, community)
    return redirect(reverse("sn:communities"))

@require_http_methods(["POST"])
@login_required
def leave_community(request):
    # Get the user and community ID from the request
    user = _get_social_network_user(request.user)
    community_id = request.POST.get("community_id")
    if not community_id:
        return redirect(reverse("sn:communities"))
    try:
        community = ExpertiseAreas.objects.get(id=community_id)
    except ExpertiseAreas.DoesNotExist:
        return redirect(reverse("sn:communities"))
    api.leave_community(user, community)
    return redirect(reverse("sn:communities"))

@require_http_methods(["GET"])
@login_required
def communities(request):
    user = _get_social_network_user(request.user)
    all_communities = ExpertiseAreas.objects.all()
    user_communities = user.communities.all()
    super_pro_level = FameLevels.objects.get(name="Super Pro").numeric_value
    eligible_communities = ExpertiseAreas.objects.filter(
        fame__user=user,
        fame__fame_level__numeric_value__gte=super_pro_level
    ).exclude(id__in=user_communities.values_list('id', flat=True)).distinct()
    context = {
        "all_communities": all_communities,
        "user_communities": user_communities,
        "eligible_communities": eligible_communities,
    }
    return render(request, "communities.html", context)

@require_http_methods(["POST"])
@login_required
def rate_post(request):
    user = _get_social_network_user(request.user)
    post_id = request.POST.get("post_id")
    rating_type = request.POST.get("rating_type")

    if not post_id or not rating_type:
        return redirect(reverse("sn:timeline"))

    try:
        post = Posts.objects.get(id=post_id)
    except Posts.DoesNotExist:
        return redirect(reverse("sn:timeline"))

    api.rate_post(user, post, rating_type)
    return redirect(reverse("sn:timeline"))

@require_http_methods(["GET"])
@login_required
def similar_users(request):
    user = _get_social_network_user(request.user)
    similar = api.similar_users(user)
    for u in similar:
        u.similarity_percent = u.similarity * 100
    context = {
        "similar_users": similar,
    }
    return render(request, "similar_users.html", context)
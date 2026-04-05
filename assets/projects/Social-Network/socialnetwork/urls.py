from django.urls import path

from socialnetwork.views.html import timeline
from socialnetwork.views.html import follow
from socialnetwork.views.html import unfollow
from socialnetwork.views.rest import PostsListApiView
from socialnetwork.views.html import bullshitters, experts, join_community, leave_community, toggle_community_mode, communities, rate_post, similar_users, followers_view, follows_view

app_name = "socialnetwork"

urlpatterns = [
    path("api/posts", PostsListApiView.as_view(), name="posts_fulllist"),
    path("html/timeline", timeline, name="timeline"),
    path("api/follow", follow, name="follow"),
    path("api/unfollow", unfollow, name="unfollow"),

    # Added:
    path('html/bullshitters/', bullshitters, name='bullshitters'), #Bullshitters view URL
    path('html/experts/', experts, name='experts'),
    path("html/join_community/", join_community, name="join_community"), # Join a community
    path("html/leave_community/", leave_community, name="leave_community"),# Leave a community
    path("html/toggle_community_mode/", toggle_community_mode, name="toggle_community_mode"),# Toggle community mode
    path("html/communities/", communities, name="communities"), # View communities
    path('html/rate_post/', rate_post, name='rate_post'), # Rate a post
    path("html/similar_users/", similar_users, name="similar_users"), # View similar users
    path('html/followers/', followers_view, name='followers'), # Followers view
    path('html/follows/', follows_view, name='follows'), # Followed users view
]

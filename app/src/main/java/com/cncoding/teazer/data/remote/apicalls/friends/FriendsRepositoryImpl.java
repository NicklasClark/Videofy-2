package com.cncoding.teazer.data.remote.apicalls.friends;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.circleListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.followersListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.followingsListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.resultObjectCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.usersListCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_ACCEPT_JOIN_REQUEST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_BLOCK_UNBLOCK_USER;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_CANCEL_REQUEST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_DELETE_JOIN_REQUEST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_FOLLOW_USER;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_BLOCKED_USERS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_FRIENDS_FOLLOWERS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_FRIENDS_FOLLOWERS_WITH_SEARCH_TERM;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_FRIENDS_FOLLOWINGS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_FRIENDS_FOLLOWINGS_WITH_SEARCH_TERM;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_LIKED_USERS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_CIRCLE;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_CIRCLE_WITH_SEARCH_TERM;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_FOLLOWERS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_FOLLOWERS_WITH_SEARCH_TERM;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_FOLLOWING;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_MY_FOLLOWINGS_WITH_SEARCH_TERM;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_OTHERS_PROFILE_INFO;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_USERS_LIST_TO_FOLLOW;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_SEND_JOIN_REQUEST_BY_USERNAME;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_SEND_JOIN_REQUEST_BY_USER_ID;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UNFOLLOW_USER;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class FriendsRepositoryImpl implements FriendsRepository {

    private FriendsService friendsService;

    public FriendsRepositoryImpl(String token) {
        friendsService = getRetrofitWithAuthToken(token).create(FriendsService.class);
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUserId(int userId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.sendJoinRequestByUserId(userId).enqueue(resultObjectCallback(liveData, CALL_SEND_JOIN_REQUEST_BY_USER_ID));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUsername(String username) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.sendJoinRequestByUsername(username).enqueue(resultObjectCallback(liveData, CALL_SEND_JOIN_REQUEST_BY_USERNAME));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> acceptJoinRequest(int notificationId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.acceptJoinRequest(notificationId).enqueue(resultObjectCallback(liveData, CALL_ACCEPT_JOIN_REQUEST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deleteJoinRequest(int notificationId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.deleteJoinRequest(notificationId).enqueue(resultObjectCallback(liveData, CALL_DELETE_JOIN_REQUEST));
        return liveData;
    }

    @Override
    public LiveData<CircleList> getMyCircle(int page) {
        MutableLiveData<CircleList> liveData = new MutableLiveData<>();
        friendsService.getMyCircle(page).enqueue(circleListCallback(liveData, CALL_GET_MY_CIRCLE));
        return liveData;
    }

    @Override
    public LiveData<CircleList> getMyCircleWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<CircleList> liveData = new MutableLiveData<>();
        friendsService.getMyCircleWithSearchTerm(page, searchTerm)
                .enqueue(circleListCallback(liveData, CALL_GET_MY_CIRCLE_WITH_SEARCH_TERM));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getMyFollowing(int page) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowing(page).enqueue(followingsListCallback(liveData, CALL_GET_MY_FOLLOWING));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getMyFollowingsWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowingsWithSearchTerm(page, searchTerm)
                .enqueue(followingsListCallback(liveData, CALL_GET_MY_FOLLOWINGS_WITH_SEARCH_TERM));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getFriendsFollowings(int page, int userId) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowings(page, userId).enqueue(followingsListCallback(liveData, CALL_GET_FRIENDS_FOLLOWINGS));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowingsWithSearchTerm(userId, page, searchTerm)
                .enqueue(followingsListCallback(liveData, CALL_GET_FRIENDS_FOLLOWINGS_WITH_SEARCH_TERM));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getMyFollowers(int page) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowers(page).enqueue(followersListCallback(liveData, CALL_GET_MY_FOLLOWERS));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getMyFollowersWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowersWithSearchTerm(page, searchTerm)
                .enqueue(followersListCallback(liveData, CALL_GET_MY_FOLLOWERS_WITH_SEARCH_TERM));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getFriendsFollowers(int page, int userId) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowers(page, userId).enqueue(followersListCallback(liveData, CALL_GET_FRIENDS_FOLLOWERS));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowersWithSearchTerm(userId, page, searchTerm)
                .enqueue(followersListCallback(liveData, CALL_GET_FRIENDS_FOLLOWERS_WITH_SEARCH_TERM));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> unfollowUser(int userId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.unfollowUser(userId).enqueue(resultObjectCallback(liveData, CALL_UNFOLLOW_USER));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> cancelRequest(int userId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.cancelRequest(userId).enqueue(resultObjectCallback(liveData, CALL_CANCEL_REQUEST));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> followUser(int userId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.followUser(userId).enqueue(resultObjectCallback(liveData, CALL_FOLLOW_USER));
        return liveData;
    }

    @Override
    public LiveData<ProfileInfo> getOthersProfileInfo(int userId) {
        final MutableLiveData<ProfileInfo> liveData = new MutableLiveData<>();
        friendsService.getOthersProfileInfo(userId).enqueue(new Callback<ProfileInfo>() {
            @Override
            public void onResponse(Call<ProfileInfo> call, Response<ProfileInfo> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_OTHERS_PROFILE_INFO) :
                        new ProfileInfo(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_OTHERS_PROFILE_INFO));
            }

            @Override
            public void onFailure(Call<ProfileInfo> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ProfileInfo(new Throwable(FAILED)).setCallType(CALL_GET_OTHERS_PROFILE_INFO));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ResultObject> blockUnblockUser(int userId, int status) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.blockUnblockUser(userId, status).enqueue(resultObjectCallback(liveData, CALL_BLOCK_UNBLOCK_USER));
        return liveData;
    }

    @Override
    public LiveData<BlockedUsersList> getBlockedUsers(int page) {
        final MutableLiveData<BlockedUsersList> liveData = new MutableLiveData<>();
        friendsService.getBlockedUsers(page).enqueue(new Callback<BlockedUsersList>() {
            @Override
            public void onResponse(Call<BlockedUsersList> call, Response<BlockedUsersList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_BLOCKED_USERS) :
                        new BlockedUsersList(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_BLOCKED_USERS));
            }

            @Override
            public void onFailure(Call<BlockedUsersList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new BlockedUsersList(new Throwable(FAILED)).setCallType(CALL_GET_BLOCKED_USERS));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollow(int page) {
        MutableLiveData<UsersList> liveData = new MutableLiveData<>();
        friendsService.getUsersListToFollow(page).enqueue(usersListCallback(liveData, CALL_GET_USERS_LIST_TO_FOLLOW));
        return liveData;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<UsersList> liveData = new MutableLiveData<>();
        friendsService.getUsersListToFollowWithSearchTerm(page, searchTerm)
                .enqueue(usersListCallback(liveData, Annotations.CALL_GET_USERS_LIST_TO_FOLLOW_WITH_SEARCH_TERM));
        return liveData;
    }

    @Override
    public LiveData<LikedUserList> getLikedUsers(int postId, int page) {
        final MutableLiveData<LikedUserList> liveData = new MutableLiveData<>();
        friendsService.getLikedUsers(postId, page).enqueue(new Callback<LikedUserList>() {
            @Override
            public void onResponse(Call<LikedUserList> call, Response<LikedUserList> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_LIKED_USERS) :
                        new LikedUserList(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_LIKED_USERS));
            }

            @Override
            public void onFailure(Call<LikedUserList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new LikedUserList(new Throwable(FAILED)).setCallType(CALL_GET_LIKED_USERS));
            }
        });
        return liveData;
    }
}
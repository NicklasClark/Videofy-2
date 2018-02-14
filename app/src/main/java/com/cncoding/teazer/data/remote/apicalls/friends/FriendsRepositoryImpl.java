package com.cncoding.teazer.data.remote.apicalls.friends;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.friends.CircleList;
import com.cncoding.teazer.model.friends.FollowersList;
import com.cncoding.teazer.model.friends.FollowingsList;
import com.cncoding.teazer.model.friends.ProfileInfo;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.LikedUserPost;
import com.cncoding.teazer.model.user.BlockedUsersList;

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
        friendsService.sendJoinRequestByUserId(userId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUsername(String username) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.sendJoinRequestByUsername(username).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> acceptJoinRequest(int notificationId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.acceptJoinRequest(notificationId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deleteJoinRequest(int notificationId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.deleteJoinRequest(notificationId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<CircleList> getMyCircle(int page) {
        MutableLiveData<CircleList> liveData = new MutableLiveData<>();
        friendsService.getMyCircle(page).enqueue(circleListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<CircleList> getMyCircleWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<CircleList> liveData = new MutableLiveData<>();
        friendsService.getMyCircleWithSearchTerm(page, searchTerm).enqueue(circleListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getMyFollowing(int page) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowing(page).enqueue(followingsListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getMyFollowingsWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowingsWithSearchTerm(page, searchTerm).enqueue(followingsListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getFriendsFollowings(int page, int userId) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowings(page, userId).enqueue(followingsListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowingsList> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm) {
        MutableLiveData<FollowingsList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowingsWithSearchTerm(userId, page, searchTerm).enqueue(followingsListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getMyFollowers(int page) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowers(page).enqueue(followersListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getMyFollowersWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getMyFollowersWithSearchTerm(page, searchTerm).enqueue(followersListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getFriendsFollowers(int page, int userId) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowers(page, userId).enqueue(followersListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<FollowersList> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm) {
        MutableLiveData<FollowersList> liveData = new MutableLiveData<>();
        friendsService.getFriendsFollowersWithSearchTerm(userId, page, searchTerm).enqueue(followersListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> unfollowUser(int userId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.unfollowUser(userId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> cancelRequest(int userId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.cancelRequest(userId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> followUser(int userId) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.followUser(userId).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ProfileInfo> getOthersProfileInfo(int userId) {
        final MutableLiveData<ProfileInfo> liveData = new MutableLiveData<>();
        friendsService.getOthersProfileInfo(userId).enqueue(new Callback<ProfileInfo>() {
            @Override
            public void onResponse(Call<ProfileInfo> call, Response<ProfileInfo> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new ProfileInfo(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<ProfileInfo> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new ProfileInfo(new Throwable(FAILED)));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ResultObject> blockUnblockUser(int userId, int status) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        friendsService.blockUnblockUser(userId, status).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<BlockedUsersList> getBlockedUsers(int page) {
        final MutableLiveData<BlockedUsersList> liveData = new MutableLiveData<>();
        friendsService.getBlockedUsers(page).enqueue(new Callback<BlockedUsersList>() {
            @Override
            public void onResponse(Call<BlockedUsersList> call, Response<BlockedUsersList> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new BlockedUsersList(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<BlockedUsersList> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new BlockedUsersList(new Throwable(FAILED)));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollow(int page) {
        MutableLiveData<UsersList> liveData = new MutableLiveData<>();
        friendsService.getUsersListToFollow(page).enqueue(usersListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        MutableLiveData<UsersList> liveData = new MutableLiveData<>();
        friendsService.getUsersListToFollowWithSearchTerm(page, searchTerm).enqueue(usersListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<LikedUserPost> getLikedUsers(int postId, int page) {
        final MutableLiveData<LikedUserPost> liveData = new MutableLiveData<>();
        friendsService.getLikedUsers(postId, page).enqueue(new Callback<LikedUserPost>() {
            @Override
            public void onResponse(Call<LikedUserPost> call, Response<LikedUserPost> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new LikedUserPost(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<LikedUserPost> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new LikedUserPost(new Throwable(FAILED)));
            }
        });
        return liveData;
    }
}

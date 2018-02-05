package com.cncoding.teazer.data.repository.remote.friends;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.model.friends.CircleList;
import com.cncoding.teazer.model.friends.FollowersList;
import com.cncoding.teazer.model.friends.FollowingsList;
import com.cncoding.teazer.model.friends.ProfileInfo;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.LikedUserPost;
import com.cncoding.teazer.model.user.BlockedUsersList;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class FriendsRepo implements FriendsRepository {
    @Override
    public LiveData<ResultObject> getMyCircleWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<FollowingsList> getMyFollowing(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUserId(int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUsername(String username) {
        return null;
    }

    @Override
    public LiveData<ResultObject> acceptJoinRequest(int notificationId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deleteJoinRequest(int notificationId) {
        return null;
    }

    @Override
    public LiveData<CircleList> getMyCircle(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getMyFollowingsWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<FollowingsList> getFriendsFollowings(int page, int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<FollowersList> getMyFollowers(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getMyFollowersWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<FollowersList> getFriendsFollowers(int page, int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<ResultObject> unfollowUser(int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> cancelRequest(int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> followUser(int userId) {
        return null;
    }

    @Override
    public LiveData<ProfileInfo> getOthersProfileInfo(int userId) {
        return null;
    }

    @Override
    public LiveData<ResultObject> blockUnblockUser(int userId, int status) {
        return null;
    }

    @Override
    public LiveData<BlockedUsersList> getBlockedUsers(int page) {
        return null;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollow(int page) {
        return null;
    }

    @Override
    public LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        return null;
    }

    @Override
    public LiveData<LikedUserPost> getLikedUsers(int postId, int page) {
        return null;
    }
}

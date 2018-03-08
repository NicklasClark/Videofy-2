package com.cncoding.teazer.data.remote.apicalls.friends;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations.BlockUnblock;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface FriendsRepository {

    LiveData<ResultObject> sendJoinRequestByUserId(int userId);

    LiveData<ResultObject> sendJoinRequestByUsername(String username);

    LiveData<ResultObject> acceptJoinRequest(int notificationId);

    LiveData<ResultObject> deleteJoinRequest(int notificationId);

    LiveData<CircleList> getMyCircle(int page);

    LiveData<CircleList> getMyCircleWithSearchTerm(int page, String searchTerm);

    LiveData<FollowingsList> getMyFollowing(int page);

    LiveData<FollowingsList> getMyFollowingsWithSearchTerm(int page, String searchTerm);

    LiveData<FollowingsList> getFriendsFollowings(int page, int userId);

    LiveData<FollowingsList> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm);

    LiveData<FollowersList> getMyFollowers(int page);

    LiveData<FollowersList> getMyFollowersWithSearchTerm(int page, String searchTerm);

    LiveData<FollowersList> getFriendsFollowers(int page, int userId);

    LiveData<FollowersList> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm);

    LiveData<ResultObject> unfollowUser(int userId);

    LiveData<ResultObject> cancelRequest(int userId);

    LiveData<ResultObject>followUser(int userId);

    LiveData<ProfileInfo> getOthersProfileInfo(int userId);

    LiveData<ResultObject> blockUnblockUser(int userId, @BlockUnblock int status);

    LiveData<BlockedUsersList> getBlockedUsers(int page);

    LiveData<UsersList> getUsersListToFollow(int page);

    LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm);

    LiveData<LikedUserList>getLikedUsers(int postId, int page);
}
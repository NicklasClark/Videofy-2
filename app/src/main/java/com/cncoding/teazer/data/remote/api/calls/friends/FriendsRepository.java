package com.cncoding.teazer.data.remote.api.calls.friends;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.friends.CircleList;
import com.cncoding.teazer.model.friends.FollowersList;
import com.cncoding.teazer.model.friends.FollowingsList;
import com.cncoding.teazer.model.friends.ProfileInfo;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.LikedUserPost;
import com.cncoding.teazer.model.user.BlockedUsersList;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface FriendsRepository {

    LiveData<ResultObject> getMyCircleWithSearchTerm(int page, String searchTerm);

    LiveData<FollowingsList> getMyFollowing(int page);

    LiveData<ResultObject> sendJoinRequestByUserId(int userId);

    LiveData<ResultObject> sendJoinRequestByUsername(String username);

    LiveData<ResultObject> acceptJoinRequest(int notificationId);

    LiveData<ResultObject> deleteJoinRequest(int notificationId);

    LiveData<CircleList> getMyCircle(int page);

    LiveData<ResultObject> getMyFollowingsWithSearchTerm(int page, String searchTerm);

    LiveData<FollowingsList> getFriendsFollowings(int page, int userId);

    LiveData<ResultObject> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm);

    LiveData<FollowersList> getMyFollowers(int page);

    LiveData<ResultObject> getMyFollowersWithSearchTerm(int page, String searchTerm);

    LiveData<FollowersList> getFriendsFollowers(int page, int userId);

    LiveData<ResultObject> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm);

    LiveData<ResultObject> unfollowUser(int userId);

    LiveData<ResultObject> cancelRequest(int userId);

    LiveData<ResultObject>followUser(int userId);

    LiveData<ProfileInfo> getOthersProfileInfo(int userId);

    LiveData<ResultObject> blockUnblockUser(int userId, int status);

    LiveData<BlockedUsersList> getBlockedUsers(int page);

    LiveData<UsersList> getUsersListToFollow(int page);

    LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm);

    LiveData<LikedUserPost>getLikedUsers(int postId, int page);
}

package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsRepository;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsRepositoryImpl;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 2/22/2018.
 */

public class FriendsViewModel extends ViewModel {

    private MediatorLiveData<ResultObject> resultObjectLiveData;
    private MediatorLiveData<CircleList> circleListLiveData;
    private MediatorLiveData<FollowingsList> followingsListLiveData;
    private MediatorLiveData<FollowersList> followersListLiveData;
    private MediatorLiveData<ProfileInfo> profileInfoLiveData;
    private MediatorLiveData<BlockedUsersList> blockedUsersListLiveData;
    private MediatorLiveData<UsersList> usersListLiveData;
    private MediatorLiveData<LikedUserList> likedUserPostLiveData;
    private FriendsRepository friendsRepository;

    @Inject public FriendsViewModel(MediatorLiveData<ResultObject> resultObjectLiveData,
                                    MediatorLiveData<CircleList> circleListLiveData, MediatorLiveData<FollowingsList> followingsListLiveData,
                                    MediatorLiveData<FollowersList> followersListLiveData, MediatorLiveData<ProfileInfo> profileInfoLiveData,
                                    MediatorLiveData<BlockedUsersList> blockedUsersListLiveData, MediatorLiveData<UsersList> usersListLiveData,
                                    MediatorLiveData<LikedUserList> likedUserPostLiveData, FriendsRepository friendsRepository) {
        this.resultObjectLiveData = resultObjectLiveData;
        this.circleListLiveData = circleListLiveData;
        this.followingsListLiveData = followingsListLiveData;
        this.followersListLiveData = followersListLiveData;
        this.profileInfoLiveData = profileInfoLiveData;
        this.blockedUsersListLiveData = blockedUsersListLiveData;
        this.usersListLiveData = usersListLiveData;
        this.likedUserPostLiveData = likedUserPostLiveData;
        this.friendsRepository = friendsRepository;
    }

    public FriendsViewModel(String token, boolean isViewHolder) {
        if (isViewHolder) {
            resultObjectLiveData = new MediatorLiveData<>();
        } else {
            this.resultObjectLiveData = new MediatorLiveData<>();
            this.circleListLiveData = new MediatorLiveData<>();
            this.followingsListLiveData = new MediatorLiveData<>();
            this.followersListLiveData = new MediatorLiveData<>();
            this.profileInfoLiveData = new MediatorLiveData<>();
            this.blockedUsersListLiveData = new MediatorLiveData<>();
            this.usersListLiveData = new MediatorLiveData<>();
            this.likedUserPostLiveData = new MediatorLiveData<>();
        }
        friendsRepository = new FriendsRepositoryImpl(token);
    }

    //region Getters
    public MediatorLiveData<ResultObject> getResultObject() {
        return resultObjectLiveData;
    }

    public MediatorLiveData<CircleList> getCircleList() {
        return circleListLiveData;
    }

    public MediatorLiveData<FollowingsList> getFollowingsList() {
        return followingsListLiveData;
    }

    public MediatorLiveData<FollowersList> getFollowersList() {
        return followersListLiveData;
    }

    public MediatorLiveData<ProfileInfo> getProfileInfo() {
        return profileInfoLiveData;
    }

    public MediatorLiveData<BlockedUsersList> getBlockedUsersList() {
        return blockedUsersListLiveData;
    }

    public MediatorLiveData<UsersList> getUsersList() {
        return usersListLiveData;
    }

    public MediatorLiveData<LikedUserList> getLikedUserPost() {
        return likedUserPostLiveData;
    }
    //endregion

    //region API Calls
    public void sendJoinRequestByUserId(int userId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.sendJoinRequestByUserId(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void sendJoinRequestByUserId(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.sendJoinRequestByUserId(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
                }
        );
    }

    public void sendJoinRequestByUsername(String username) {
        resultObjectLiveData.addSource(
                friendsRepository.sendJoinRequestByUsername(username),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void acceptJoinRequest(int notificationId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.acceptJoinRequest(notificationId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void acceptJoinRequest(int notificationId) {
        resultObjectLiveData.addSource(
                friendsRepository.acceptJoinRequest(notificationId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void deleteJoinRequest(int notificationId) {
        resultObjectLiveData.addSource(
                friendsRepository.deleteJoinRequest(notificationId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getMyCircle(int page) {
        if (circleListLiveData != null) {
            circleListLiveData.addSource(
                    friendsRepository.getMyCircle(page),
                    new Observer<CircleList>() {
                        @Override
                        public void onChanged(@Nullable CircleList circleList) {
                            circleListLiveData.setValue(circleList);
                        }
                    }
            );
        }
    }

    public void getMyCircleWithSearchTerm(int page, String searchTerm) {
        if (circleListLiveData != null) {
            circleListLiveData.addSource(
                    friendsRepository.getMyCircleWithSearchTerm(page, searchTerm),
                    new Observer<CircleList>() {
                        @Override
                        public void onChanged(@Nullable CircleList circleList) {
                            circleListLiveData.setValue(circleList);
                        }
                    }
            );
        }
    }

    public void getMyFollowing(int page) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getMyFollowing(page),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getMyFollowingsWithSearchTerm(int page, String searchTerm) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getMyFollowingsWithSearchTerm(page, searchTerm),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowings(int page, int userId) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getFriendsFollowings(page, userId),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm) {
        if (followingsListLiveData != null) {
            followingsListLiveData.addSource(
                    friendsRepository.getFriendsFollowingsWithSearchTerm(page, userId, searchTerm),
                    new Observer<FollowingsList>() {
                        @Override
                        public void onChanged(@Nullable FollowingsList followingsList) {
                            followingsListLiveData.setValue(followingsList);
                        }
                    }
            );
        }
    }

    public void getMyFollowers(int page) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getMyFollowers(page),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void getMyFollowersWithSearchTerm(int page, String searchTerm) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getMyFollowersWithSearchTerm(page, searchTerm),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowers(int page, int userId) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getFriendsFollowers(page, userId),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm) {
        if (followersListLiveData != null) {
            followersListLiveData.addSource(
                    friendsRepository.getFriendsFollowersWithSearchTerm(page, userId, searchTerm),
                    new Observer<FollowersList>() {
                        @Override
                        public void onChanged(@Nullable FollowersList followersList) {
                            followersListLiveData.setValue(followersList);
                        }
                    }
            );
        }
    }

    public void unfollowUser(int userId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.unfollowUser(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void unfollowUser(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.unfollowUser(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void cancelRequest(int userId, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.cancelRequest(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void cancelRequest(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.cancelRequest(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void followUser(int userId) {
        resultObjectLiveData.addSource(
                friendsRepository.followUser(userId),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getOthersProfileInfo(int userId) {
        if (profileInfoLiveData != null) {
            profileInfoLiveData.addSource(
                    friendsRepository.getOthersProfileInfo(userId),
                    new Observer<ProfileInfo>() {
                        @Override
                        public void onChanged(@Nullable ProfileInfo profileInfo) {
                            profileInfoLiveData.setValue(profileInfo);
                        }
                    }
            );
        }
    }

    public void blockUnblockUser(int userId, int status, final int position) {
        resultObjectLiveData.addSource(
                friendsRepository.blockUnblockUser(userId, status),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        if (resultObject != null) {
                            resultObjectLiveData.setValue(position  > -1 ? resultObject.setAdapterPosition(position) : resultObject);
                        }
                    }
                }
        );
    }

    public void blockUnblockUser(int userId, int status) {
        resultObjectLiveData.addSource(
                friendsRepository.blockUnblockUser(userId, status),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getBlockedUsers(int page) {
        if (blockedUsersListLiveData != null) {
            blockedUsersListLiveData.addSource(
                    friendsRepository.getBlockedUsers(page),
                    new Observer<BlockedUsersList>() {
                        @Override
                        public void onChanged(@Nullable BlockedUsersList blockedUsersList) {
                            blockedUsersListLiveData.setValue(blockedUsersList);
                        }
                    }
            );
        }
    }

    public void getUsersListToFollow(int page) {
        if (usersListLiveData != null) {
            usersListLiveData.addSource(
                    friendsRepository.getUsersListToFollow(page),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersListLiveData.setValue(usersList);
                        }
                    }
            );
        }
    }

    public void getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        if (usersListLiveData != null) {
            usersListLiveData.addSource(
                    friendsRepository.getUsersListToFollowWithSearchTerm(page, searchTerm),
                    new Observer<UsersList>() {
                        @Override
                        public void onChanged(@Nullable UsersList usersList) {
                            usersListLiveData.setValue(usersList);
                        }
                    }
            );
        }
    }

    public void getLikedUsers(int postId, int page) {
        if (likedUserPostLiveData != null) {
            likedUserPostLiveData.addSource(
                    friendsRepository.getLikedUsers(postId, page),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList likedUserList) {
                            likedUserPostLiveData.setValue(likedUserList);
                        }
                    }
            );
        }
    }
    //endregion
}
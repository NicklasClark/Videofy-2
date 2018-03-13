package com.cncoding.teazer.data.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.BrokerLiveData;
import com.cncoding.teazer.data.local.database.DataAccessTasks;
import com.cncoding.teazer.data.local.database.TeazerDB;
import com.cncoding.teazer.data.model.application.ConfigBody;
import com.cncoding.teazer.data.model.application.ConfigDetails;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.PushNotificationBody;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.data.model.base.TaggedUser;
import com.cncoding.teazer.data.model.discover.LandingPostsV2;
import com.cncoding.teazer.data.model.discover.UserInterestsAndTrendingCategories;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.ReportPost;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.post.UpdatePostRequest;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.model.react.GiphyReactionRequest;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;
import com.cncoding.teazer.data.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.data.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.data.model.user.ReportUser;
import com.cncoding.teazer.data.model.user.SetPasswordRequest;
import com.cncoding.teazer.data.model.user.UpdateCategories;
import com.cncoding.teazer.data.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.application.ApplicationRepository;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepository;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsRepository;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepository;
import com.cncoding.teazer.data.remote.apicalls.react.ReactRepository;
import com.cncoding.teazer.data.remote.apicalls.user.UserRepository;
import com.cncoding.teazer.injection.component.DaggerBaseComponent;
import com.cncoding.teazer.utilities.common.Annotations.AccountType;
import com.cncoding.teazer.utilities.common.Annotations.BlockUnblock;
import com.cncoding.teazer.utilities.common.Annotations.HideOrShow;
import com.cncoding.teazer.utilities.common.Annotations.LikeDislike;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import okhttp3.MultipartBody;

import static com.cncoding.teazer.utilities.common.Annotations.MOST_POPULAR;
import static com.cncoding.teazer.utilities.common.Annotations.POST_LIST;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@SuppressWarnings({"WeakerAccess", "deprecation"})
public class BaseViewModel extends AndroidViewModel {

    @Inject BrokerLiveData<ResultObject> resultObjectLiveData;
    @Inject BrokerLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData;
    @Inject BrokerLiveData<ConfigDetails> configDetailsLiveData;
    @Inject BrokerLiveData<List<ReportTypes>> reportTypesListLiveData;
    @Inject BrokerLiveData<List<DeactivateTypes>> deactivateTypesListLiveData;
    @Inject BrokerLiveData<List<Category>> categoriesListLiveData;
    @Inject BrokerLiveData<LandingPostsV2> landingPostsLiveData;
    @Inject @Named(POST_LIST) BrokerLiveData<PostList> postListLiveData;
    @Inject @Named(MOST_POPULAR) BrokerLiveData<PostList> mostPopularLiveData;
    @Inject BrokerLiveData<UsersList> usersListLiveData;
    @Inject BrokerLiveData<VideosList> videosListLiveData;
    @Inject BrokerLiveData<UserInterestsAndTrendingCategories> userInterestsAndTrendingCategoriesLiveData;
    @Inject BrokerLiveData<CircleList> circleListLiveData;
    @Inject BrokerLiveData<FollowingsList> followingsListLiveData;
    @Inject BrokerLiveData<FollowersList> followersListLiveData;
    @Inject BrokerLiveData<ProfileInfo> profileInfoLiveData;
    @Inject BrokerLiveData<BlockedUsersList> blockedUsersListLiveData;
    @Inject BrokerLiveData<LikedUserList> likedUserListLiveData;
    @Inject BrokerLiveData<PostDetails> postDetailsLiveData;
    @Inject BrokerLiveData<PostUploadResult> postUploadResultLiveData;
    @Inject BrokerLiveData<TaggedUsersList> taggedUsersListLiveData;
    @Inject BrokerLiveData<ReactionResponse> reactionResponseLiveData;
    @Inject BrokerLiveData<ReactionsList> reactionsListLiveData;
    @Inject BrokerLiveData<UserProfile> userProfileLiveData;
    @Inject BrokerLiveData<NotificationsList> notificationsLiveData;

    @Inject ApplicationRepository applicationRepository;
    @Inject DiscoverRepository discoverRepository;
    @Inject FriendsRepository friendsRepository;
    @Inject PostsRepository postsRepository;
    @Inject ReactRepository reactRepository;
    @Inject UserRepository userRepository;

    @Inject TeazerDB database;

    public BaseViewModel(Application application) {
        super(application);
        DaggerBaseComponent.builder()
                .appComponent(((TeazerApplication) application).getAppComponent())
                .build()
                .inject(this);
    }

    //region Getters
    @NonNull public BrokerLiveData<PostList> getPostList() {
        return postListLiveData;
    }

    @NonNull public BrokerLiveData<ResultObject> getResultObjectLiveData() {
        return resultObjectLiveData;
    }

    @NonNull public BrokerLiveData<PostDetails> getPostDetailsLiveData() {
        return postDetailsLiveData;
    }

    @NonNull public BrokerLiveData<PostUploadResult> getPostUploadResultLiveData() {
        return postUploadResultLiveData;
    }

    @NonNull public BrokerLiveData<TaggedUsersList> getTaggedUsersListLiveData() {
        return taggedUsersListLiveData;
    }

    @NonNull public BrokerLiveData<ReactionsList> getReactionsListLiveData() {
        return reactionsListLiveData;
    }

    @NonNull public BrokerLiveData<ReactionResponse> getReactionResponseLiveData() {
        return reactionResponseLiveData;
    }

    @NonNull public BrokerLiveData<DefaultCoverImageResponse> getDefaultCoverImageLiveData() {
        return defaultCoverImageLiveData;
    }

    @NonNull public BrokerLiveData<ConfigDetails> getConfigDetailsLiveData() {
        return configDetailsLiveData;
    }

    @NonNull public BrokerLiveData<List<ReportTypes>> getReportTypesListLiveData() {
        return reportTypesListLiveData;
    }

    @NonNull public BrokerLiveData<List<DeactivateTypes>> getDeactivateTypesListLiveData() {
        return deactivateTypesListLiveData;
    }

    @NonNull public BrokerLiveData<List<Category>> getCategoriesListLiveData() {
        return categoriesListLiveData;
    }

    @NonNull public BrokerLiveData<LandingPostsV2> getLandingPosts() {
        return landingPostsLiveData;
    }

    @NonNull public BrokerLiveData<PostList> getPostListLiveData() {
        return postListLiveData;
    }

    @NonNull public BrokerLiveData<PostList> getMostPopularPosts() {
        return mostPopularLiveData;
    }

    @NonNull public BrokerLiveData<UsersList> getUsersList() {
        return usersListLiveData;
    }

    @NonNull public BrokerLiveData<VideosList> getVideosList() {
        return videosListLiveData;
    }

    public BrokerLiveData<UserInterestsAndTrendingCategories> getUserInterestsAndTrendingCategoriesLiveData() {
        return userInterestsAndTrendingCategoriesLiveData;
    }

    @NonNull public BrokerLiveData<ResultObject> getResultObject() {
        return resultObjectLiveData;
    }

    @NonNull public BrokerLiveData<CircleList> getCircleList() {
        return circleListLiveData;
    }

    @NonNull public BrokerLiveData<FollowingsList> getFollowingsList() {
        return followingsListLiveData;
    }

    @NonNull public BrokerLiveData<FollowersList> getFollowersList() {
        return followersListLiveData;
    }

    @NonNull public BrokerLiveData<ProfileInfo> getProfileInfo() {
        return profileInfoLiveData;
    }

    @NonNull public BrokerLiveData<BlockedUsersList> getBlockedUsersList() {
        return blockedUsersListLiveData;
    }

    @NonNull public BrokerLiveData<LikedUserList> getLikedUserLiveData() {
        return likedUserListLiveData;
    }

    @NonNull public BrokerLiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }

    @NonNull public BrokerLiveData<NotificationsList> getNotificationsLiveData() {
        return notificationsLiveData;
    }
    //endregion

    //region Application API calls
    public void sendPushNotificationToAllApiCall(PushNotificationBody pushNotificationBody){
        resultObjectLiveData.observeOn(applicationRepository.sendPushNotificationToAll(pushNotificationBody));
    }

    public void getDefaultCoverImagesApiCall(int page){
        defaultCoverImageLiveData.observeOn(applicationRepository.getDefaultCoverImages(page));
    }

//    public void addDefaultCoverMediaApiCall(DefaultCoverMedia defaultCoverMedia){}

    public void getUpdateAndConfigDetailsApiCall(ConfigBody configBody){
        configDetailsLiveData.observeOn(applicationRepository.getUpdateAndConfigDetails(configBody));
    }

    public void getPostReportTypesApiCall(){
        reportTypesListLiveData.observeOn(applicationRepository.getPostReportTypes());
    }

    public void getReactionReportTypesApiCall(){
        reportTypesListLiveData.observeOn(applicationRepository.getReactionReportTypes());
    }

    public void getProfileReportTypesApiCall(){
        reportTypesListLiveData.observeOn(applicationRepository.getReactionReportTypes());
    }

    public void getDeactivationTypesListApiCall(){
        deactivateTypesListLiveData.observeOn(applicationRepository.getDeactivationTypesList());
    }

    public void getCategoriesApiCall(){
        categoriesListLiveData.observeOn(applicationRepository.getCategories());
    }
    //endregion

    //region Discover API Calls
    public void loadLandingPostsApiCall() {
        landingPostsLiveData.observeOn(discoverRepository.getNewDiscoverLandingPosts());
    }

    public void loadMostPopularPostsApiCall(final int page) {
        try {
            final LiveData<PostList> liveData = discoverRepository.getAllMostPopularVideos(page);
            mostPopularLiveData.addSource(
                    liveData,
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            mostPopularLiveData.removeSource(liveData);
                            if (page > 1 && postList != null && mostPopularLiveData.getValue() != null) {
                                List<PostDetails> postDetailsList = mostPopularLiveData.getValue().getPosts();
                                postDetailsList.addAll(postList.getPosts());
                                postList.setPosts(postDetailsList);
                            }
                            mostPopularLiveData.setValue(postList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFeaturedPostsApiCall(int page) {
        postListLiveData.observeOn(discoverRepository.getFeaturedPosts(page));
    }

    public void loadAllInterestedCategoriesPostsApiCall(int page, int categoryId) {
        postListLiveData.observeOn(discoverRepository.getAllInterestedCategoriesVideos(page, categoryId));
    }

    public void loadTrendingPostsByCategoryApiCall(int page, int categoryId) {
        postListLiveData.observeOn(discoverRepository.getTrendingVideosByCategory(page, categoryId));
    }

    public void loadUsersListApiCall(int page) {
    }

    public void loadUsersListWithSearchTermApiCall(int page, String searchTerm) {
        usersListLiveData.observeOn(discoverRepository.getUsersListToFollowWithSearchTerm(page, searchTerm));
    }

    public void loadTrendingVideosApiCall(int page) {
        videosListLiveData.observeOn(discoverRepository.getTrendingVideos(page));
    }

    public void loadUserInterestsAndTrendingCategoriesApiCall() {
        userInterestsAndTrendingCategoriesLiveData.observeOn(discoverRepository.getUserInterestsAndTrendingCategories());
    }

    public void loadVideosWithSearchTermApiCall(int page, String searchTerm) {
        videosListLiveData.observeOn(discoverRepository.getVideosWithSearchTerm(page, searchTerm));
    }
    //endregion

    //region Friends API Calls
    public void sendJoinRequestByUserIdApiCall(int userId, final int position) {
        resultObjectLiveData.observeOn(friendsRepository.sendJoinRequestByUserId(userId), position);
    }

    public void sendJoinRequestByUserIdApiCall(int userId) {
        resultObjectLiveData.observeOn(friendsRepository.sendJoinRequestByUserId(userId));
    }

    public void sendJoinRequestByUsernameApiCall(String username) {
        resultObjectLiveData.observeOn(friendsRepository.sendJoinRequestByUsername(username));
    }

    public void acceptJoinRequestApiCall(int notificationId, final int position) {
        resultObjectLiveData.observeOn(friendsRepository.acceptJoinRequest(notificationId));
    }

    public void acceptJoinRequestApiCall(int notificationId) {
        resultObjectLiveData.observeOn(friendsRepository.acceptJoinRequest(notificationId));
    }

    public void deleteJoinRequestApiCall(int notificationId) {
        resultObjectLiveData.observeOn(friendsRepository.deleteJoinRequest(notificationId));
    }

    public void getMyCircleApiCall(int page) {
        circleListLiveData.observeOn(friendsRepository.getMyCircle(page));
    }

    public void getMyCircleWithSearchTermApiCall(int page, String searchTerm) {
        circleListLiveData.observeOn(friendsRepository.getMyCircleWithSearchTerm(page, searchTerm));
    }

    public void getMyFollowingApiCall(int page) {
        followingsListLiveData.observeOn(friendsRepository.getMyFollowing(page));
    }

    public void getMyFollowingsWithSearchTermApiCall(int page, String searchTerm) {
        followingsListLiveData.observeOn(friendsRepository.getMyFollowingsWithSearchTerm(page, searchTerm));
    }

    public void getFriendsFollowingsApiCall(int page, int userId) {
        followingsListLiveData.observeOn(friendsRepository.getFriendsFollowings(page, userId));
    }

    public void getFriendsFollowingsWithSearchTermApiCall(int userId, int page, String searchTerm) {
        followingsListLiveData.observeOn(friendsRepository.getFriendsFollowingsWithSearchTerm(page, userId, searchTerm));
    }

    public void getMyFollowersApiCall(int page) {
        followersListLiveData.observeOn(friendsRepository.getMyFollowers(page));
    }

    public void getMyFollowersWithSearchTermApiCall(int page, String searchTerm) {
        followersListLiveData.observeOn(friendsRepository.getMyFollowersWithSearchTerm(page, searchTerm));
    }

    public void getFriendsFollowersApiCall(int page, int userId) {
        followersListLiveData.observeOn(friendsRepository.getFriendsFollowers(page, userId));
    }

    public void getFriendsFollowersWithSearchTermApiCall(int userId, int page, String searchTerm) {
        followersListLiveData.observeOn(friendsRepository.getFriendsFollowersWithSearchTerm(page, userId, searchTerm));
    }

    public void unfollowUserApiCall(int userId, final int position) {
        resultObjectLiveData.observeOn(friendsRepository.unfollowUser(userId), position);
    }

    public void unfollowUserApiCall(int userId) {
        resultObjectLiveData.observeOn(friendsRepository.unfollowUser(userId));
    }

    public void cancelRequestApiCall(int userId, final int position) {
        resultObjectLiveData.observeOn(friendsRepository.cancelRequest(userId), position);
    }

    public void cancelRequestApiCall(int userId) {
        resultObjectLiveData.observeOn(friendsRepository.cancelRequest(userId));
    }

    public void followUserApiCall(int userId) {
        resultObjectLiveData.observeOn(friendsRepository.followUser(userId));
    }

    public void getOthersProfileInfoApiCall(int userId) {
        profileInfoLiveData.observeOn(friendsRepository.getOthersProfileInfo(userId));
    }

    public void blockUnblockUserApiCall(int userId, @BlockUnblock int status, final int position) {
        resultObjectLiveData.observeOn(friendsRepository.blockUnblockUser(userId, status), position);
    }

    public void blockUnblockUserApiCall(int userId, @BlockUnblock int status) {
        resultObjectLiveData.observeOn(friendsRepository.blockUnblockUser(userId, status));
    }

    public void getBlockedUsersApiCall(int page) {
        blockedUsersListLiveData.observeOn(friendsRepository.getBlockedUsers(page));
    }

    public void getUsersListToFollowApiCall(int page) {
        usersListLiveData.observeOn(friendsRepository.getUsersListToFollow(page));
    }

    public void getUsersListToFollowWithSearchTermApiCall(int page, String searchTerm) {
        usersListLiveData.observeOn(friendsRepository.getUsersListToFollowWithSearchTerm(page, searchTerm));
    }

    public void getLikedUsersApiCall(int postId, int page) {
        likedUserListLiveData.observeOn(friendsRepository.getLikedUsers(postId, page));
    }
    //endregion

    //region HomePage related stuff (Local and Remote).
    /**
     * API call to fetch post list.
     */
    public void loadHomePagePostsApiCall(final int page) {
        postListLiveData.observeOn(postsRepository.getHomePagePosts(page));
    }

    public TeazerDB getDatabase() {
        return database;
    }

    public boolean getAllLocalPosts() {
        try {
            List<PostDetails> postDetailsList = database.dao().getAllPosts();
            boolean result = postDetailsList != null && !postDetailsList.isEmpty();
            if (result) postListLiveData.setValue(new PostList(postDetailsList));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PostDetails getLocalPost(int postId) {
        try {
            return database.dao().getPost(postId).get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insertAllPostsToDB(List<PostDetails> postDetails) {
        new DataAccessTasks.InsertAllTask(database, postDetails).execute();
    }

    public void updateLocalPost(PostDetails postDetails) {
        new DataAccessTasks.UpdatePostTask(database, postDetails).execute();
    }

    public void likeLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(TeazerDB.LIKE, database).execute(postId);
    }

    public void dislikeLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(TeazerDB.DISLIKE, database).execute(postId);
    }

    public void deleteLocalPost(int postId) {
        new DataAccessTasks.PostUpdateTaskWithId(TeazerDB.DELETE, database).execute(postId);
    }

    public void deleteLocalPost(PostDetails postDetails) {
        new DataAccessTasks.PostUpdateTaskWithObject(TeazerDB.DELETE, database).execute(postDetails);
    }

    public void deleteAllLocalPosts() {
        new DataAccessTasks.DeleteAllTask(database).execute();
    }

    public void updateLocalTitle(String title, int postId) {
        new DataAccessTasks.UpdateTitleTask(database, title, postId).execute();
    }

    public void updateLocalTags(ArrayList<TaggedUser> taggedUsers, int postId) {
        new DataAccessTasks.UpdateTagsReactionsTask(database, taggedUsers, null, false, TeazerDB.TAGS, postId).execute();
    }

    public void updateLocalReactions(ArrayList<PostReaction> reactions, boolean canReact, int postId) {
        new DataAccessTasks.UpdateTagsReactionsTask(database, null, reactions, true, TeazerDB.TAGS, postId).execute();
    }

    public void incrementLocalViews(ArrayList<Medias> medias, int postId) {
        new DataAccessTasks.IncrementViewsTask(database, postId, medias).execute();
    }
    //endregion

    //region Other "Post" API calls
    public void uploadVideoApiCall(MultipartBody.Part video, String title, String location,
                            double latitude, double longitude, String tags, String categories) {
        postUploadResultLiveData.observeOn(postsRepository.uploadVideo(video, title, location, latitude, longitude, tags, categories));
    }

    public void createReactionByGiphyApiCall(GiphyReactionRequest giphyReactionRequest) {
        reactionResponseLiveData.observeOn(reactRepository.createReactionByGiphy(giphyReactionRequest));
    }

    public void updatePostApiCall(UpdatePostRequest updatePostRequest) {
        postUploadResultLiveData.observeOn(postsRepository.updatePost(updatePostRequest));
    }

    public void likeDislikePostApiCall(int postId, @LikeDislike int status) {
        resultObjectLiveData.observeOn(postsRepository.likeDislikePost(postId, status));
    }

    public void incrementViewCountApiCall(int mediaId) {
        resultObjectLiveData.observeOn(postsRepository.incrementViewCount(mediaId));
    }

    public void deletePostApiCall(int postId) {
        resultObjectLiveData.observeOn(postsRepository.deletePost(postId));
    }

    public void reportPostApiCall(ReportPost reportPostDetails) {
        resultObjectLiveData.observeOn(postsRepository.reportPost(reportPostDetails));
    }

    public void hideOrShowPostApiCall(int postId, @HideOrShow int status) {
        resultObjectLiveData.observeOn(postsRepository.hideOrShowPost(postId, status));
    }

    public void loadPostDetailsApiCall(int postId) {
        postDetailsLiveData.observeOn(postsRepository.getPostDetails(postId));
    }

    public void loadTaggedUsersApiCall(int postId, int page) {
        taggedUsersListLiveData.observeOn(postsRepository.getTaggedUsers(postId, page));
    }

    public void loadHiddenPostsApiCall(int page) {
        postListLiveData.observeOn(postsRepository.getHiddenPosts(page));
    }

    public void loadMyPostedVideosApiCall(int page) {
        postListLiveData.observeOn(postsRepository.getMyPostedVideos(page));
    }

    public void loadVideosPostedByFriendApiCall(int page, int friendId) {
        postListLiveData.observeOn(postsRepository.getVideosPostedByFriend(page, friendId));
    }

    public void loadReactionsOfPostApiCall(int postId, int page) {
        reactionsListLiveData.observeOn(postsRepository.getReactionsOfPost(postId, page));
    }

    public void loadHiddenVideosListApiCall(int page) {
        postListLiveData.observeOn(postsRepository.getHiddenVideosList(page));
    }

    public void loadAllHiddenVideosListApiCall(int postId) {
        resultObjectLiveData.observeOn(postsRepository.getAllHiddenVideosList(postId));
    }
    //endregion

    //region React API Calls
    public void uploadReactionApiCall(MultipartBody.Part video, int postId, String title){
        reactionResponseLiveData.observeOn(reactRepository.uploadReaction(video, postId, title));
    }

    public void getReactionDetailApiCall(int reactId){
        reactionResponseLiveData.observeOn(reactRepository.getReactionDetail(reactId));
    }

    public void likeDislikeReactionApiCall(int reactId, @LikeDislike int status){
        resultObjectLiveData.observeOn(reactRepository.likeDislikeReaction(reactId, status));
    }

    public void incrementReactionViewCountApiCall(int mediaId){
        resultObjectLiveData.observeOn(reactRepository.incrementReactionViewCount(mediaId));
    }

    public void deleteReactionApiCall(int reactId){
        resultObjectLiveData.observeOn(reactRepository.deleteReaction(reactId));
    }

    public void reportReactionApiCall(ReportReaction reportReaction){
        resultObjectLiveData.observeOn(reactRepository.reportReaction(reportReaction));
    }

    public void hideOrShowReactionApiCall(int reactId, @HideOrShow int status){
        resultObjectLiveData.observeOn(reactRepository.hideOrShowReaction(reactId, status));
    }

    public void getMyReactionsApiCall(int page){
        reactionsListLiveData.observeOn(reactRepository.getMyReactions(page));
    }

    public void getFriendsReactionsApiCall(int page, int friend_id){
        reactionsListLiveData.observeOn(reactRepository.getFriendsReactions(page, friend_id));
    }

    public void getHiddenReactionsApiCall(int page){
        reactionsListLiveData.observeOn(reactRepository.getHiddenReactions(page));
    }

    @Deprecated public void getOldLikedUsersOfReactionApiCall(int reactId, int page){
        likedUserListLiveData.observeOn(reactRepository.getOldLikedUsersOfReaction(reactId, page));
    }

    @Deprecated
    public void getOldLikedUsersOfReactionWithSearchTermApiCall(int reactId, int page, String searchTerm){
        likedUserListLiveData.observeOn(reactRepository.getOldLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm));
    }

    public void getLikedUsersOfReactionApiCall(int reactId, int page){
        likedUserListLiveData.observeOn(reactRepository.getLikedUsersOfReaction(reactId, page));
    }

    public void getLikedUsersOfReactionWithSearchTermApiCall(int reactId, int page, String searchTerm){
        likedUserListLiveData.observeOn(reactRepository.getLikedUsersOfReactionWithSearchTerm(reactId, page, searchTerm));
    }
    //endregion

    //region User API Calls
    public void updateUserProfileMediaApiCall(MultipartBody.Part media){
        resultObjectLiveData.observeOn(userRepository.updateUserProfileMedia(media));
    }

    public void resetFcmTokenApiCall(String header, String token){
        resultObjectLiveData.observeOn(userRepository.resetFcmToken(header, token));
    }

    public void setAccountVisibilityApiCall(@AccountType int accountType){
        resultObjectLiveData.observeOn(userRepository.setAccountVisibility(accountType));
    }

    public void getUserProfileApiCall(){
        userProfileLiveData.observeOn(userRepository.getUserProfile());
    }

    public void changeMobileNumberApiCall(ChangeMobileNumber changeMobileNumber){
        resultObjectLiveData.observeOn(userRepository.changeMobileNumber(changeMobileNumber));
    }

    public void updateMobileNumberApiCall(UpdateMobileNumber updateMobileNumber){
        resultObjectLiveData.observeOn(userRepository.updateMobileNumber(updateMobileNumber));
    }

    public void updateUserProfileApiCall(ProfileUpdateRequest updateProfileDetails){
        resultObjectLiveData.observeOn(userRepository.updateUserProfile(updateProfileDetails));
    }

    public void updatePasswordApiCall(UpdatePasswordRequest updatePasswordDetails){
        resultObjectLiveData.observeOn(userRepository.updatePassword(updatePasswordDetails));
    }

    public void setPasswordApiCall(SetPasswordRequest setPasswordDetails){
        resultObjectLiveData.observeOn(userRepository.setPassword(setPasswordDetails));
    }

    public void getFollowingNotificationsApiCall(int page){
        notificationsLiveData.observeOn(userRepository.getFollowingNotifications(page));
    }

    public void getRequestNotificationsApiCall(int page){
        notificationsLiveData.observeOn(userRepository.getRequestNotifications(page));
    }

    public void updateCategoriesApiCall(UpdateCategories categories){
        resultObjectLiveData.observeOn(userRepository.updateCategories(categories));
    }

    public void logoutApiCall(String header){
        resultObjectLiveData.observeOn(userRepository.logout(header));
    }

    public void removeProfilePicApiCall(){
        resultObjectLiveData.observeOn(userRepository.removeProfilePic());
    }

    public void reportUserApiCall(ReportUser reportuser){
        resultObjectLiveData.observeOn(userRepository.reportUser(reportuser));
    }

    public void deactivateAccountApiCall(DeactivateAccountRequest deactivateAccountRequest){
        resultObjectLiveData.observeOn(userRepository.deactivateAccount(deactivateAccountRequest));
    }

    public void resetUnreadNotificationApiCall(int notificationType){
        resultObjectLiveData.observeOn(userRepository.resetUnreadNotification(notificationType));
    }

    public void likeDislikeProfileApiCall(int userId, @LikeDislike int status){
        resultObjectLiveData.observeOn(userRepository.likeDislikeProfile(userId, status));
    }

    public void getLikedUsersListApiCall(int userId){
        likedUserListLiveData.observeOn(userRepository.getLikedUsersList(userId));
    }
    //endregion
}
package com.cncoding.teazer.data;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.data.model.base.Authorize;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LandingPosts;
import com.cncoding.teazer.data.model.post.LikedUserPost;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostReactionsList;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.ReportPost;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.post.UpdatePostRequest;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionUploadResult;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.react.ReportReaction;
import com.cncoding.teazer.data.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.data.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.Profile;
import com.cncoding.teazer.data.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.data.model.user.ReportUser;
import com.cncoding.teazer.data.model.user.SetPasswordRequest;
import com.cncoding.teazer.data.model.user.UpdateCategories;
import com.cncoding.teazer.data.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.repository.ApiRepository;
import com.cncoding.teazer.data.room.daos.PostDetailsDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

@Singleton
public class Repo extends ApiRepository {

    private PostDetailsDao postDetailsDao;
    private Executor executor;

    @Inject public Repo(PostDetailsDao postDetailsDao, Executor executor) {
        this.postDetailsDao = postDetailsDao;
        this.executor = executor;
    }

    @Inject public Repo() {
    }

    @Override
    public LiveData<ResultObject> signUp(Authorize signUpBody) {
        return super.signUp(signUpBody);
    }

    @Override
    public LiveData<ResultObject> verifySignUp(Authorize verifySignUp) {
        return super.verifySignUp(verifySignUp);
    }

    @Override
    public LiveData<ResultObject> socialSignUp(Authorize socialSignUpDetails) {
        return super.socialSignUp(socialSignUpDetails);
    }

    @Override
    public LiveData<PostList> getFeaturedPosts(int page) {
        return super.getFeaturedPosts(page);
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getPostReportTypes() {
        return super.getPostReportTypes();
    }

    @Override
    public LiveData<PostList> getAllInterestedCategoriesVideos(int page, int categoryId) {
        return super.getAllInterestedCategoriesVideos(page, categoryId);
    }

    @Override
    public LiveData<ResultObject> loginWithPassword(Authorize loginWithPassword) {
        return super.loginWithPassword(loginWithPassword);
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getProfileReportTypes() {
        return super.getProfileReportTypes();
    }

    @Override
    public LiveData<ReactionUploadResult> uploadReaction(MultipartBody.Part video, int postId, String title) {
        return super.uploadReaction(video, postId, title);
    }

    @Override
    public LiveData<PostList> getAllMostPopularVideos(int page) {
        return super.getAllMostPopularVideos(page);
    }

    @Override
    public LiveData<ResultObject> loginWithOtp(Authorize phoneNumberDetails) {
        return super.loginWithOtp(phoneNumberDetails);
    }

    @Override
    public LiveData<List<DeactivateTypes>> getDeactivationTypesList() {
        return super.getDeactivationTypesList();
    }

    @Override
    public LiveData<PostList> getTrendingVideos(int page, int categoryId) {
        return super.getTrendingVideos(page, categoryId);
    }

    @Override
    public LiveData<ResultObject> likeDislikeReaction(int reactId, int status) {
        return super.likeDislikeReaction(reactId, status);
    }

    @Override
    public LiveData<ResultObject> verifyLoginWithOtp(Authorize verifyLoginWithOtp) {
        return super.verifyLoginWithOtp(verifyLoginWithOtp);
    }

    @Override
    public LiveData<ArrayList<Category>> getCategories() {
        return super.getCategories();
    }

    @Override
    public LiveData<LandingPosts> getDiscoverPagePosts() {
        return super.getDiscoverPagePosts();
    }

    @Override
    public LiveData<ResultObject> getMyCircleWithSearchTerm(int page, String searchTerm) {
        return super.getMyCircleWithSearchTerm(page, searchTerm);
    }

    @Override
    public LiveData<ResultObject> incrementReactionViewCount(int mediaId) {
        return super.incrementReactionViewCount(mediaId);
    }

    @Override
    public LiveData<ResultObject> checkUsernameAvailability(String username) {
        return super.checkUsernameAvailability(username);
    }

    @Override
    public LiveData<PostUploadResult> uploadVideo(MultipartBody.Part video, String title, String location, double latitude, double longitude, String tags, String categories) {
        return super.uploadVideo(video, title, location, latitude, longitude, tags, categories);
    }

    @Override
    public LiveData<UsersList> getUsersListToFollow(int page) {
        return super.getUsersListToFollow(page);
    }

    @Override
    public LiveData<ResultObject> deleteReaction(int reactId) {
        return super.deleteReaction(reactId);
    }

    @Override
    public LiveData<FollowingsList> getMyFollowing(int page) {
        return super.getMyFollowing(page);
    }

    @Override
    public LiveData<ResultObject> checkEmailAvailability(String email) {
        return super.checkEmailAvailability(email);
    }

    @Override
    public LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm) {
        return super.getUsersListToFollowWithSearchTerm(page, searchTerm);
    }

    @Override
    public LiveData<ResultObject> reportReaction(ReportReaction reportReaction) {
        return super.reportReaction(reportReaction);
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUserId(int userId) {
        return super.sendJoinRequestByUserId(userId);
    }

    @Override
    public LiveData<ResultObject> checkPhoneNumberAvailability(int countryCode, long phoneNumber) {
        return super.checkPhoneNumberAvailability(countryCode, phoneNumber);
    }

    @Override
    public LiveData<VideosList> getVideosWithSearchTerm(int page, String searchTerm) {
        return super.getVideosWithSearchTerm(page, searchTerm);
    }

    @Override
    public LiveData<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest) {
        return super.updatePost(updatePostRequest);
    }

    @Override
    public LiveData<ResultObject> sendJoinRequestByUsername(String username) {
        return super.sendJoinRequestByUsername(username);
    }

    @Override
    public LiveData<ResultObject> hideOrShowReaction(int reactId, int status) {
        return super.hideOrShowReaction(reactId, status);
    }

    @Override
    public LiveData<ResultObject> verifyForgotPasswordOtp(int otp) {
        return super.verifyForgotPasswordOtp(otp);
    }

    @Override
    public LiveData<ResultObject> likeDislikePost(int postId, int status) {
        return super.likeDislikePost(postId, status);
    }

    @Override
    public LiveData<ResultObject> acceptJoinRequest(int notificationId) {
        return super.acceptJoinRequest(notificationId);
    }

    @Override
    public LiveData<ReactionsList> getMyReactions(int page) {
        return super.getMyReactions(page);
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByEmail(String email) {
        return super.requestResetPasswordByEmail(email);
    }

    @Override
    public LiveData<ResultObject> incrementViewCount(int mediaId) {
        return super.incrementViewCount(mediaId);
    }

    @Override
    public LiveData<ResultObject> getHiddenReactions(int page) {
        return super.getHiddenReactions(page);
    }

    @Override
    public LiveData<ResultObject> deleteJoinRequest(int notificationId) {
        return super.deleteJoinRequest(notificationId);
    }

    @Override
    public LiveData<ResultObject> updateUserProfileMedia(MultipartBody.Part media) {
        return super.updateUserProfileMedia(media);
    }

    @Override
    public LiveData<ResultObject> requestResetPasswordByPhone(Authorize phoneNumberDetails) {
        return super.requestResetPasswordByPhone(phoneNumberDetails);
    }

    @Override
    public LiveData<ResultObject> deletePost(int postId) {
        return super.deletePost(postId);
    }

    @Override
    public LiveData<ReactionResponse> getReactionDetail(int reactId) {
        return super.getReactionDetail(reactId);
    }

    @Override
    public LiveData<CircleList> getMyCircle(int page) {
        return super.getMyCircle(page);
    }

    @Override
    public LiveData<ResultObject> resetFcmToken(String header, String token) {
        return super.resetFcmToken(header, token);
    }

    @Override
    public LiveData<ResultObject> deletePostVideo(int postId) {
        return super.deletePostVideo(postId);
    }

    @Override
    public LiveData<ResultObject> getMyFollowingsWithSearchTerm(int page, String searchTerm) {
        return super.getMyFollowingsWithSearchTerm(page, searchTerm);
    }

    @Override
    public LiveData<ResultObject> resetPasswordByOtp(Authorize resetPasswordDetails) {
        return super.resetPasswordByOtp(resetPasswordDetails);
    }

    @Override
    public LiveData<ResultObject> setAccountVisibility(int accountType) {
        return super.setAccountVisibility(accountType);
    }

    @Override
    public LiveData<ResultObject> reportPost(ReportPost reportPostDetails) {
        return super.reportPost(reportPostDetails);
    }

    @Override
    public LiveData<FollowingsList> getFriendsFollowings(int page, int userId) {
        return super.getFriendsFollowings(page, userId);
    }

    @Override
    public LiveData<UserProfile> getUserProfile() {
        return super.getUserProfile();
    }

    @Override
    public LiveData<ResultObject> hideOrShowPost(int postId, int status) {
        return super.hideOrShowPost(postId, status);
    }

    @Override
    public LiveData<ResultObject> changeMobileNumber(ChangeMobileNumber changeMobileNumber) {
        return super.changeMobileNumber(changeMobileNumber);
    }

    @Override
    public LiveData<ResultObject> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm) {
        return super.getFriendsFollowingsWithSearchTerm(userId, page, searchTerm);
    }

    @Override
    public LiveData<PostDetails> getPostDetails(int postId) {
        return super.getPostDetails(postId);
    }

    @Override
    public LiveData<ResultObject> updateMobileNumber(UpdateMobileNumber updateMobileNumber) {
        return super.updateMobileNumber(updateMobileNumber);
    }

    @Override
    public LiveData<TaggedUsersList> getTaggedUsers(int postId, int page) {
        return super.getTaggedUsers(postId, page);
    }

    @Override
    public LiveData<FollowersList> getMyFollowers(int page) {
        return super.getMyFollowers(page);
    }

    @Override
    public LiveData<PostList> getHiddenPosts(int page) {
        return super.getHiddenPosts(page);
    }

    @Override
    public LiveData<Profile> getUserProfileDetail() {
        return super.getUserProfileDetail();
    }

    @Override
    public LiveData<ResultObject> getMyFollowersWithSearchTerm(int page, String searchTerm) {
        return super.getMyFollowersWithSearchTerm(page, searchTerm);
    }

    @Override
    public LiveData<PostList> getHomePagePosts(int page, String authToken) {
        return super.getHomePagePosts(page, authToken);
    }

    @Override
    public LiveData<ResultObject> updateUserProfile(ProfileUpdateRequest updateProfileDetails) {
        return super.updateUserProfile(updateProfileDetails);
    }

    @Override
    public LiveData<FollowersList> getFriendsFollowers(int page, int userId) {
        return super.getFriendsFollowers(page, userId);
    }

    @Override
    public LiveData<PostList> getPostedVideos(int page) {
        return super.getPostedVideos(page);
    }

    @Override
    public LiveData<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails) {
        return super.updatePassword(updatePasswordDetails);
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriends(int page, int friendId) {
        return super.getVideosPostedByFriends(page, friendId);
    }

    @Override
    public LiveData<ResultObject> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm) {
        return super.getFriendsFollowersWithSearchTerm(userId, page, searchTerm);
    }

    @Override
    public LiveData<PostList> getVideosPostedByFriend(int page, int friendId) {
        return super.getVideosPostedByFriend(page, friendId);
    }

    @Override
    public LiveData<ResultObject> setPassword(SetPasswordRequest setPasswordDetails) {
        return super.setPassword(setPasswordDetails);
    }

    @Override
    public LiveData<ResultObject> unfollowUser(int userId) {
        return super.unfollowUser(userId);
    }

    @Override
    public LiveData<PostReactionsList> getReactionsOfPost(int postId, int page) {
        return super.getReactionsOfPost(postId, page);
    }

    @Override
    public LiveData<NotificationsList> getFollowingNotifications(int page) {
        return super.getFollowingNotifications(page);
    }

    @Override
    public LiveData<ResultObject> cancelRequest(int userId) {
        return super.cancelRequest(userId);
    }

    @Override
    public LiveData<PostList> getHiddenVideosList(int page) {
        return super.getHiddenVideosList(page);
    }

    @Override
    public LiveData<ResultObject> followUser(int userId) {
        return super.followUser(userId);
    }

    @Override
    public LiveData<NotificationsList> getRequestNotifications(int page) {
        return super.getRequestNotifications(page);
    }

    @Override
    public LiveData<ProfileInfo> getOthersProfileInfo(int userId) {
        return super.getOthersProfileInfo(userId);
    }

    @Override
    public LiveData<ResultObject> getAllHiddenVideosList(int postId) {
        return super.getAllHiddenVideosList(postId);
    }

    @Override
    public LiveData<ResultObject> updateCategories(UpdateCategories categories) {
        return super.updateCategories(categories);
    }

    @Override
    public LiveData<ResultObject> blockUnblockUser(int userId, int status) {
        return super.blockUnblockUser(userId, status);
    }

    @Override
    public LiveData<ResultObject> logout(String header) {
        return super.logout(header);
    }

    @Override
    public LiveData<BlockedUsersList> getBlockedUsers(int page) {
        return super.getBlockedUsers(page);
    }

    @Override
    public LiveData<ResultObject> removeProfilePic() {
        return super.removeProfilePic();
    }

    @Override
    public LiveData<ResultObject> reportUser(ReportUser reportuser) {
        return super.reportUser(reportuser);
    }

    @Override
    public LiveData<ResultObject> deactivateAccount(DeactivateAccountRequest deactivateAccountRequest) {
        return super.deactivateAccount(deactivateAccountRequest);
    }

    @Override
    public LiveData<LikedUserPost> getLikedUsers(int postId, int page) {
        return super.getLikedUsers(postId, page);
    }

    @Override
    public LiveData<ResultObject> resetUnreadNotification(int notificationType) {
        return super.resetUnreadNotification(notificationType);
    }
}

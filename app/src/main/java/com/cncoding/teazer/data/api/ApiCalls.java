package com.cncoding.teazer.data.api;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.model.application.DeactivateTypes;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.model.base.Authorize;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.CircleList;
import com.cncoding.teazer.model.friends.FollowersList;
import com.cncoding.teazer.model.friends.FollowingsList;
import com.cncoding.teazer.model.friends.ProfileInfo;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.LandingPosts;
import com.cncoding.teazer.model.post.LikedUserPost;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.post.ReportPost;
import com.cncoding.teazer.model.post.TaggedUsersList;
import com.cncoding.teazer.model.post.UpdatePostRequest;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.react.ReactionUploadResult;
import com.cncoding.teazer.model.react.ReactionsList;
import com.cncoding.teazer.model.react.ReportReaction;
import com.cncoding.teazer.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.model.user.BlockedUsersList;
import com.cncoding.teazer.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.model.user.NotificationsList;
import com.cncoding.teazer.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.model.user.ReportUser;
import com.cncoding.teazer.model.user.SetPasswordRequest;
import com.cncoding.teazer.model.user.UpdateCategories;
import com.cncoding.teazer.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.model.user.UserProfile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 *
 * Created by Prem $ on 10/10/2017.
 */

public class ApiCalls {

//    static final String BASE_URL = "http://dev.teazer.online/";
//    static final String BASE_URL = "https://api.teazer.online/";

    static final int SUCCESS_OK_TRUE = 1;
    static final int SUCCESS_OK_FALSE = 2;
    //    public static final int BACK_PRESSED_ACTION = 6;
    //    public static final int RESUME_WELCOME_VIDEO_ACTION = 8;
    static final int FAIL = 3;
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static class Application {

        public static Call<List<ReportPostTitlesResponse>> getPostReportTypes() {
            return getApplicationService().getPostReportTypes();
        }

        public static Call<List<ReportPostTitlesResponse>> getProfileReportTypes() {
            return getApplicationService().getProfileReportTypes();
        }

        public static Call<ArrayList<Category>> getCategories() {
            return getApplicationService().getCategories();

        }
        public static Call<List<DeactivateTypes>> getDeactivationTypesList() {
            return getApplicationService().getDeactivationTypesList();
        }
        private static ApiService.ApplicationCalls getApplicationService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Resources.getSystem().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
            return retrofit.create(ApiService.ApplicationCalls.class);
        }
    }

    public static class Auth {

        public static boolean checkUsername(ProximaNovaRegularAutoCompleteTextView usernameView) {
            return getAvailabilityServiceCallback(
                    getAuthenticationService().checkUsernameAvailability(usernameView.getText().toString())
            );
        }

        public static boolean checkEmail(ProximaNovaRegularAutoCompleteTextView emailView) {
            return getAvailabilityServiceCallback(
                    getAuthenticationService().checkEmailAvailability(emailView.getText().toString())
            );
        }

        public static boolean checkPhoneNumber(int countryCode, String phoneNumberString) {
            return !phoneNumberString.isEmpty() && getAvailabilityServiceCallback(
                    getAuthenticationService().checkPhoneNumberAvailability(countryCode, Long.parseLong(phoneNumberString))
            );
        }

        public static Call<ResultObject> performSignUp(Authorize signUpBody) {
            return getAuthenticationService().signUp(signUpBody);
        }

        public static Call<ResultObject> verifySignUp(Authorize signUpVerificationBody) {
            return getAuthenticationService().verifySignUp(signUpVerificationBody);
        }

        public static Call<ResultObject> socialSignUp(Authorize socialSignUpDetails) {
            return getAuthenticationService().socialSignUp(socialSignUpDetails);
        }

        public static Call<ResultObject> requestResetPasswordByEmail(String email) {
            return getAuthenticationService().requestResetPasswordByEmail(email);
        }

        public static Call<ResultObject> requestResetPasswordByPhone(long phoneNumber, int countryCode) {
            return getAuthenticationService().requestResetPasswordByPhone(new Authorize(phoneNumber, countryCode));
        }

        public static Call<ResultObject> changePassword(Authorize resetPasswordDetails) {
            return getAuthenticationService().resetPasswordByOtp(resetPasswordDetails);
        }

        public static Call<ResultObject> loginWithPassword(Authorize loginWithPassword) {
            return getAuthenticationService().loginWithPassword(loginWithPassword);
        }

        public static Call<ResultObject> loginWithOtp(Authorize phoneNumberDetails) {
            return getAuthenticationService().loginWithOtp(phoneNumberDetails);
        }

        public static Call<ResultObject> verifyLoginWithOtp(Authorize verifyLoginWithOtp) {
            return getAuthenticationService().verifyLoginWithOtp(verifyLoginWithOtp);
        }

        private static ApiService.AuthenticationCalls getAuthenticationService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Resources.getSystem().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .client(getOkHttpClientWithAuthToken(context))
                    .client(getOkHttpClient())
                    .build();
            return retrofit.create(ApiService.AuthenticationCalls.class);
        }
    }

    public static class Discover {

        /**
         * Call this service to get the discover page featured videos lists.
         */
        public static Call<PostList> getFeaturedPosts(int page, String authToken){
            return getDiscoverService(authToken).getFeaturedPosts(page);
        }

        /**
         * Call this service to get the discover page interested category videos when user clicks "View all".
         */
        public static Call<PostList> getAllInterestedCategoriesVideos(int page, int categoryId, String authToken){
            return getDiscoverService(authToken).getAllInterestedCategoriesVideos(page, categoryId);
        }

        /**
         * Call this service to get the most popular videos. Call this service when user taps "View All".
         */
        public static Call<PostList> getAllMostPopularVideos(int page, String authToken){
            return getDiscoverService(authToken).getAllMostPopularVideos(page);
        }

        /**
         * Call this service to get the discover page trending category videos of the respected category.
         */
        public static Call<PostList> getTrendingVideos(int page, int categoryId, String authToken){
            return getDiscoverService(authToken).getTrendingVideos(page, categoryId);
        }


        /**
         * Call this service to get discover page landing posts.
         */
        public static Call<LandingPosts> getDiscoverPagePosts(String authToken){
            return getDiscoverService(authToken).getDiscoverPagePosts();
        }

        /**
         * Call this service to get users list to send follow request.
         */
        public static Call<UsersList> getUsersListToFollow(int page, String authToken){
            return getDiscoverService(authToken).getUsersListToFollow(page);
        }

        /**
         * Call this service to get users list to send follow request with search term.
         */
        public static Call<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm, String authToken){
            return getDiscoverService(authToken).getUsersListToFollowWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get users list to send follow request with search term.
         */
        public static Call<VideosList> getVideosWithSearchTerm(int page, String searchTerm, String authToken){
            return getDiscoverService(authToken).getVideosWithSearchTerm(page, searchTerm);
        }

        private static ApiService.DiscoverCalls getDiscoverService(String authToken) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Resources.getSystem().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(authToken))
                    .build();
            return retrofit.create(ApiService.DiscoverCalls.class);
        }
    }

    public static class Friends {
        /**
         * Get the "my circle" with search term
         * */
        public static Call<ResultObject> getMyCircleWithSearchTerm(int page, String searchTerm, String authToken) {
            return getFriendsService(authToken).getMyCircleWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the my followings list
         * */
        public static Call<CircleList> getMyFollowings(int page, String authToken) {
            return getFriendsService(authToken).getMyFollowings(page);
        }

        public static Call<FollowingsList> getMyFollowing(int page, String authToken) {
            return getFriendsService(authToken).getMyFollowing(page);
        }

        /**
         * Call this service to send a join request by user ID
         * */
        public static Call<ResultObject> sendJoinRequestByUserId(int userId, String authToken) {
            return getFriendsService(authToken).sendJoinRequestByUserId(userId);
        }

        /**
         * Call this service to send a join request by user name
         * */
        public static Call<ResultObject> sendJoinRequestByUsername(String username, String authToken) {
            return getFriendsService(authToken).sendJoinRequestByUsername(username);
        }

        /**
         * Call this service to accept the join request
         * */
        public static Call<ResultObject> acceptJoinRequest(int notificationId, String authToken) {
            return getFriendsService(authToken).acceptJoinRequest(notificationId);
        }

        /**
         * Call this service to delete the join request
         * */
        public static Call<ResultObject> deleteJoinRequest(int notificationId, String authToken) {
            return getFriendsService(authToken).deleteJoinRequest(notificationId);
        }

         public static Call<ResultObject> cancelRequest(int userId, String authToken) {
            return getFriendsService(authToken).cancelRequest(userId);
        }

        /**
         * Call this service to get the my circle list
         * @return If “nextPage” is true some more records present. So, you can call again with increase the page count by 1.
         * If “next_page” is false no more records present.
         * */
        public static Call<CircleList> getMyCircle(int page, String authToken) {
            return getFriendsService(authToken).getMyCircle(page);
        }

        /**
         * Call this service to get the my followings list with search term
         * */
        public static Call<ResultObject> getMyFollowingsWithSearchTerm(int page, String searchTerm, String authToken) {
            return getFriendsService(authToken).getMyFollowingsWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the friends followings list
         * */
        public static Call<FollowingsList> getFriendsFollowings(int page, int userId, String authToken) {
            return getFriendsService(authToken).getFriendsFollowings(page, userId);
        }

        /**
         * Call this service to get the friends followings list with search term
         * */
        public static Call<ResultObject> getFriendsFollowingsWithSearchTerm(int userId, int page, String searchTerm, String authToken) {
            return getFriendsService(authToken).getFriendsFollowingsWithSearchTerm(userId, page, searchTerm);
        }

        /**
         * Call this service to get the my followers list
         * */
        public static Call<FollowersList> getMyFollowers(int page, String authToken) {
            return getFriendsService(authToken).getMyFollowers(page);
        }

        /**
         * Call this service to get the my followers list with search term
         * */
        public static Call<ResultObject> getMyFollowersWithSearchTerm(int page, String searchTerm, String authToken) {
            return getFriendsService(authToken).getMyFollowersWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the friends followers list
         * */
        public static Call<FollowersList> getFriendsFollowers(int page, int userId, String authToken) {
            return getFriendsService(authToken).getFriendsFollowers(page, userId);
        }

        /**
         * Call this service to get the friends followers list with search term
         * */
        public static Call<ResultObject> getFriendsFollowersWithSearchTerm(int userId, int page, String searchTerm, String authToken) {
            return getFriendsService(authToken).getFriendsFollowersWithSearchTerm(userId, page, searchTerm);
        }

        /**
         * Call this service to unfollow a user
         * */
        public static Call<ResultObject> unfollowUser(int userId, String authToken) {
            return getFriendsService(authToken).unfollowUser(userId);
        }

        public static Call<ResultObject> followUser(int userId, String authToken) {
            return getFriendsService(authToken).followUser(userId);
        }

        /**
         * Call this service to get other's profile information
         * @return “account_type” 1 is a Private account, 2 is a Public account.
         *          “can_join” tell whether you peoples are already friends.
         *          Based on “account_type” you can read either private or public profile.
         * */
        public static Call<ProfileInfo> getOthersProfileInfo(int userId, String authToken) {
            return getFriendsService(authToken).getOthersProfileInfo(userId);
        }
//            public static Call<Profile> getOthersProfileInfoNoti(int userId, String authToken) {
//            return getFriendsService(authToken).getOthersProfileInfoNoti(userId);
//        }

        /**
         * Call this service to Block/Unblock a user
         * @param status should be 1 for block and 2 for unblock.
         */
        public static Call<ResultObject> blockUnblockUser(int userId, int status, String authToken){
            return getFriendsService(authToken).blockUnblockUser(userId, status);

        }
        public static Call<LikedUserPost> getLikedUsers(int postId, int page, String authToken){
            return getFriendsService(authToken).getLikedUsers(postId, page);
        }

        /**
         * Call this service to get blocked users list by you.
         */
        public static Call<BlockedUsersList> getBlockedUsers(int page, String authToken){
            return getFriendsService(authToken).getBlockedUsers(page);
        }

        public static int isResponseOk(Response<CircleList> response) {
            switch (response.code()) {
                case 200:
                    if (response.body().isNextPage())
                        return SUCCESS_OK_TRUE;
                    else return SUCCESS_OK_FALSE;
                default:
                    return FAIL;
            }
        }

        private static ApiService.FriendsCalls getFriendsService(String authToken) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Resources.getSystem().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(authToken))
                    .build();
            return retrofit.create(ApiService.FriendsCalls.class);
        }
    }

    public static class React {

        /**
         * Call this service to react for a video
         *
         * @return 200 : Reacted to video has failed. Due to incorrect post id, not having access to react, already done a reaction.
         * On all above cases the “status” will be false.
         * 201 : Reacted to video Successfully.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ReactionUploadResult> uploadReaction(MultipartBody.Part video, int postId, String authToken, String title) {
            return getReactService(authToken).uploadReaction(video, postId, title);
        }

        /**
         * Call this service to like/dislike a reacted video.
         * ‘Status’ should be 1 for like and 2 for dislike
         *
         * @return 200 : Pre validation failed.
         * 201 : Liked Successfully.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> likeDislikeReaction(int reactId, int status, String authToken) {
            return getReactService(authToken).likeDislikeReaction(reactId, status);
        }

        /**
         * Call this service to increase the reacted video view count
         *
         * @return 200 : If “status” is true reacted video view count increased successfully.
         * If “status” is false reacted video view count increased failed.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> incrementReactionViewCount(int mediaId, String authToken) {
            return getReactService(authToken).incrementReactionViewCount(mediaId);
        }

        /**
         * Call this service to delete a reaction.
         * Only the Video Owner or Reacted Owner has the delete access.
         *
         * @return 200 : If “status” is true post deleted successfully.
         * If “status” is false post deleted failed.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> deleteReaction(int reactId, String authToken) {
            return getReactService(authToken).deleteReaction(reactId);
        }

        /**
         * Call this service to report the reaction.
         *
         * @return 200 : If “status” is true reported successfully.
         * If “status” is false reported failed or you have already reported.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> reportReaction(ReportReaction reportReaction, String authToken) {
            return getReactService(authToken).reportReaction(reportReaction);
        }

        /**
         * Call this service to hide or show a reaction.
         * ‘Status’ should be 1 to hide and 2 to show the reaction.
         *
         * @return 200 : If status is true Hided/Un Hided Successfully.
         * If status is false Hide/Un Hide failed.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> hideOrShowReaction(int reactId, int status, String authToken) {
            return getReactService(authToken).hideOrShowReaction(reactId, status);
        }

        /**
         * Call this service to get the reactions of 
         *
         * @return 200 : If “nextPage” is true some more records present,
         * so you can call again after incrementing the page count by 1,
         * If “next_page” is false no more records present.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ReactionsList> getMyReactions(int page, String authToken) {
            return getReactService(authToken).getMyReactions(page);
        }

        /**
         * Call this service to get the reactions of friends.
         *
         * @return 200 : If “nextPage” is true some more records present,
         * so you can call again after incrementing the page count by 1,
         * If “next_page” is false no more records present.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> getFriendsReactions(int page, int friend_id, String authToken) {
            return getReactService(authToken).getFriendsReactions(page, friend_id);
        }

        /**
         * Call this service to get the reactions hidden by 
         *
         * @return 200 : If “nextPage” is true some more records present,
         * so you can call again after incrementing the page count by 1,
         * If “next_page” is false no more records present.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> likeDislikeReaction(int page, String authToken) {
            return getReactService(authToken).getHiddenReactions(page);
        }

        public static Call<ReactionResponse> getReactionDetail(int reactId, String authToken) {
            return getReactService(authToken).getReactionDetail(reactId);
        }

        private static ApiService.ReactCalls getReactService(String authToken) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Resources.getSystem().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(authToken))
                    .build();
            return retrofit.create(ApiService.ReactCalls.class);
        }
    }

    public static class Posts {

        public static Call<PostUploadResult> uploadVideo(MultipartBody.Part videoPartFile, String title, @NonNull String location,
                                                         double latitude, double longitude,
                                                         String tags, String categories, String authToken) {
            return getPostalService(authToken).uploadVideo(videoPartFile, title, location, latitude, longitude, tags, categories);
        }

        public static Call<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest, String authToken) {
            return getPostalService(authToken).updatePost(updatePostRequest);
        }

        public static Call<ResultObject> likeDislikePost(int postId, int status, String authToken) {
            return getPostalService(authToken).likeDislikePost(postId, status);
        }

        public static Call<ResultObject> incrementViewCount(int mediaId, String authToken) {
            return getPostalService(authToken).incrementViewCount(mediaId);
        }

        public static Call<ResultObject> deletePost(int postId, String authToken) {
            return getPostalService(authToken).deletePost(postId);
        }

        public static Call<ResultObject> deletePosts(int postId, String authToken) {
            return getPostalService(authToken).deletePostVideo(postId);
        }

        public static Call<ResultObject> reportPost(ReportPost reportPostDetails, String authToken) {
            return getPostalService(authToken).reportPost(reportPostDetails);
        }

        public static Call<ResultObject> hideOrShowPost(int postId, int status, String authToken) {
            return getPostalService(authToken).hideOrShowPost(postId, status);
        }

        public static Call<PostDetails> getPostDetails(int postId, String authToken) {
            return getPostalService(authToken).getPostDetails(postId);
        }

        public static Call<TaggedUsersList> getTaggedUsers(int postId, int page, String authToken) {
            return getPostalService(authToken).getTaggedUsers(postId, page);
        }

        public static Call<PostList> getHiddenPosts(int page, String authToken) {
            return getPostalService(authToken).getHiddenPosts(page);
        }

        public static Call<PostList> getHomePagePosts(int page, String authToken) {
            return getPostalService(authToken).getHomePagePosts(page);
        }

        public static Call<PostList> getPostedVideos(int page, String authToken) {
            return getPostalService(authToken).getPostedVideos(page);
        }

        public static Call<PostList> getVideosPostedByFriends(int page, int friendId, String authToken) {
            return getPostalService(authToken).getVideosPostedByFriends(page, friendId);

        }

        public static Call<PostList> getVideosPostedByFriend(int page, int friendId, String authToken) {
            return getPostalService(authToken).getVideosPostedByFriend(page, friendId);
        }

        public static Call<PostReactionsList> getReactionsOfPost(int postId, int page, String authToken) {
            return getPostalService(authToken).getReactionsOfPost(postId, page);
        }

        public static Call<PostList>getPostedVideos(String authToken, int page) {
            return getPostalService(authToken).getPostedVideos(page);

        }
        public static Call<PostList> getHiddenVideosList(int page, String authToken){
            return getPostalService(authToken).getHiddenVideosList(page);
        }

        public static Call<ResultObject> getAllHiddenVideosList(int userID, String authToken){
            return getPostalService(authToken).getAllHiddenVideosList(userID);
        }
        private static ApiService.Posts getPostalService(String authToken) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Resources.getSystem().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(authToken))
                    .build();
            return retrofit.create(ApiService.Posts.class);
        }
    }

    public static class User {
        public static Call<ResultObject> updateUserProfileMedia(MultipartBody.Part file, String authToken) {
            return getUserService(authToken).updateUserProfileMedia(file);
        }

        public static  Call<ResultObject> resetFcmToken(String header, String token, String authToken) {
            return getUserService(authToken).resetFcmToken(header, token);
        }

        public static Call<ResultObject> setAccountVisibility(int accountType, String authToken) {
            return getUserService(authToken).setAccountVisibility(accountType);
        }

        public static Call<UserProfile> getUserProfile(String authToken) {
            return getUserService(authToken).getUserProfile();

        }

        public static Call<ResultObject> changeMobileNumber(String authToken, ChangeMobileNumber changeMobileNumber) {
            return getUserService(authToken).changeMobileNumber(changeMobileNumber);

        }

          public static Call<ResultObject> updateMobileNumber(String authToken, UpdateMobileNumber updateMobileNumber) {
            return getUserService(authToken).updateMobileNumber(updateMobileNumber);

        }

        public static Call<ResultObject> updateUserProfiles(ProfileUpdateRequest updateProfileDetails, String authToken) {
            return getUserService(authToken).updateUserProfile(updateProfileDetails);
        }

        public static Call<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails, String authToken) {
            return getUserService(authToken).updatePassword(updatePasswordDetails);
        }
        public static Call<ResultObject> setPassword(SetPasswordRequest setPasswordDetails, String authToken) {
            return getUserService(authToken).setPassword(setPasswordDetails);
        }

        public static Call<NotificationsList> getFollowingNotifications(int page, String authToken){
            return getUserService(authToken).getFollowingNotifications(page);
        }

        public static Call<NotificationsList> getRequestNotifications(int page, String authToken){
            return getUserService(authToken).getRequestNotifications(page);
        }

        public static Call<ResultObject> updateCategories(UpdateCategories categories, String authToken) {
            return getUserService(authToken).updateCategories(categories);
        }

        public static Call<ResultObject> logout(String header, String authToken) {
            return getUserService(authToken).logout(header);
        }

        public static Call<UserProfile>getUserProfileDetail(String authToken) {
            return getUserService(authToken).getUserProfile();
        }

        public static Call<ResultObject> reportUsers(ReportUser reportuser, String authToken){
            return getUserService(authToken).reportUser(reportuser);
        }

        public static Call<ResultObject> deactivateAccount(String authToken, DeactivateAccountRequest deactivateAccountRequest) {
            return getUserService(authToken).deactivateAccount(deactivateAccountRequest);
        }

        public static Call<ResultObject> resetUnreadNotification(String authToken, int type) {
            return getUserService(authToken).resetUnreadNotification(type);
        }
        public static Call<ResultObject> removeProfilePic(String authToken) {
            return getUserService(authToken).removeProfilePic();
        }

        private static ApiService.UserCalls getUserService(String authToken) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Resources.getSystem().getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(authToken))
                    .build();
            return retrofit.create(ApiService.UserCalls.class);
        }
    }

    private static boolean getAvailabilityServiceCallback(Call<ResultObject> service) {
        try {
            Response<ResultObject> callback = service.execute();
            if (callback != null && callback.isSuccessful())
                return callback.body().getStatus();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static OkHttpClient getOkHttpClientWithAuthToken(final String authToken) {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", authToken)
                        .method(original.method(), original.body())
                        .build();
//                Log.d("AuthToken Fresh",SharedPrefs.getAuthToken(context));
                return chain.proceed(request);
            }
        })
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
//                .addInterceptor(logging).build();
    }

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(logging).build();
    }
}
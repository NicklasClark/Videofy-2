package com.cncoding.teazer.apiCalls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
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
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.post.ReportPost;
import com.cncoding.teazer.model.post.TaggedUsersList;
import com.cncoding.teazer.model.post.UpdatePostRequest;
import com.cncoding.teazer.model.react.ReactVideoDetailsResponse;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.react.ReactionUploadResult;
import com.cncoding.teazer.model.react.ReactionsList;
import com.cncoding.teazer.model.react.ReportReaction;
import com.cncoding.teazer.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.model.user.BlockedUsersList;
import com.cncoding.teazer.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.model.user.NotificationsList;
import com.cncoding.teazer.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.model.user.ReportUser;
import com.cncoding.teazer.model.user.SetPasswordRequest;
import com.cncoding.teazer.model.user.UpdateCategories;
import com.cncoding.teazer.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.model.user.UserProfile;
import com.cncoding.teazer.ui.fragment.activity.UpdateMobileNumber;
import com.cncoding.teazer.utilities.SharedPrefs;

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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;

/**
 *
 * Created by Prem $ on 10/10/2017.
 */

public class ApiCallingService {

//    static final String BASE_URL = "http://dev.teazer.online/";
//    static final String BASE_URL = "https://api.teazer.online/";

    public static final int SUCCESS_OK_TRUE = 1;
    public static final int SUCCESS_OK_FALSE = 2;
    //    public static final int BACK_PRESSED_ACTION = 6;
    //    public static final int RESUME_WELCOME_VIDEO_ACTION = 8;
    static final int FAIL = 3;
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static class Application {

        public static Call<List<ReportPostTitlesResponse>> getPostReportTypes(Context context) {
            return getApplicationService(context).getPostReportTypes();
        }

        public static Call<List<ReportPostTitlesResponse>> getProfileReportTypes(Context context) {
            return getApplicationService(context).getProfileReportTypes();
        }

        public static Call<ArrayList<Category>> getCategories(Context context) {
            return getApplicationService(context).getCategories();

        }
        public static Call<List<DeactivateTypes>> getDeactivationTypesList(Context context) {
            return getApplicationService(context).getDeactivationTypesList();
        }
        private static TeazerApiCall.ApplicationCalls getApplicationService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClient())
                    .build();
            return retrofit.create(TeazerApiCall.ApplicationCalls.class);
        }
    }

    public static class Auth {

        public static void checkUsername(Context context, ProximaNovaRegularAutoCompleteTextView usernameView, boolean isSignUp) {
            getAvailabilityServiceCallback(
                    getAuthenticationService(context).checkUsernameAvailability(usernameView.getText().toString()),
                    usernameView, isSignUp);
        }

        public static void checkEmail(Context context, ProximaNovaRegularAutoCompleteTextView emailView, boolean isSignUp) {
            getAvailabilityServiceCallback(
                    getAuthenticationService(context).checkEmailAvailability(emailView.getText().toString()),
                    emailView, isSignUp);
        }

        public static void checkPhoneNumber(Context context, int countryCode, ProximaNovaRegularAutoCompleteTextView phoneNumberView, boolean isSignUp) {
            String phoneString = phoneNumberView.getText().toString();
            if (!phoneString.isEmpty()) {
                getAvailabilityServiceCallback(
                        getAuthenticationService(context).checkPhoneNumberAvailability(
                                countryCode,
                                Long.parseLong(phoneString)),
                        phoneNumberView, isSignUp);
            }
        }

        public static Call<ResultObject> performSignUp(Context context, Authorize signUpBody) {
            return getAuthenticationService(context).signUp(signUpBody);
        }

        public static Call<ResultObject> verifySignUp(Context context, Authorize signUpVerificationBody) {
            return getAuthenticationService(context).verifySignUp(signUpVerificationBody);
        }

        public static Call<ResultObject> socialSignUp(Context context, Authorize socialSignUpDetails) {
            return getAuthenticationService(context).socialSignUp(socialSignUpDetails);
        }

        public static Call<ResultObject> requestResetPasswordByEmail(Context context, String email) {
            return getAuthenticationService(context).requestResetPasswordByEmail(email);
        }

        public static Call<ResultObject> requestResetPasswordByPhone(Context context, long phoneNumber, int countryCode) {
            return getAuthenticationService(context).requestResetPasswordByPhone(new Authorize(phoneNumber, countryCode));
        }

        public static Call<ResultObject> changePassword(Context context, Authorize resetPasswordDetails) {
            return getAuthenticationService(context).resetPasswordByOtp(resetPasswordDetails);
        }

        public static Call<ResultObject> loginWithPassword(Context context, Authorize loginWithPassword) {
            return getAuthenticationService(context).loginWithPassword(loginWithPassword);
        }

        public static Call<ResultObject> loginWithOtp(Context context, Authorize phoneNumberDetails) {
            return getAuthenticationService(context).loginWithOtp(phoneNumberDetails);
        }

        public static Call<ResultObject> verifyLoginWithOtp(Context context, Authorize verifyLoginWithOtp) {
            return getAuthenticationService(context).verifyLoginWithOtp(verifyLoginWithOtp);
        }

        private static TeazerApiCall.AuthenticationCalls getAuthenticationService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                    .client(getOkHttpClientWithAuthToken(context))
                    .client(getOkHttpClient())
                    .build();
            return retrofit.create(TeazerApiCall.AuthenticationCalls.class);
        }
    }

    public static class Discover {

        /**
         * Call this service to get the discover page featured videos lists.
         */
        public static Call<PostList> getFeaturedPosts(int page, Context context){
            return getDiscoverService(context).getFeaturedPosts(page);
        }

        /**
         * Call this service to get the discover page interested category videos when user clicks "View all".
         */
        public static Call<PostList> getAllInterestedCategoriesVideos(int page, int categoryId, Context context){
            return getDiscoverService(context).getAllInterestedCategoriesVideos(page, categoryId);
        }

        /**
         * Call this service to get the most popular videos. Call this service when user taps "View All".
         */
        public static Call<PostList> getAllMostPopularVideos(int page, Context context){
            return getDiscoverService(context).getAllMostPopularVideos(page);
        }

        /**
         * Call this service to get the discover page trending category videos of the respected category.
         */
        public static Call<PostList> getTrendingVideos(int page, int categoryId, Context context){
            return getDiscoverService(context).getTrendingVideos(page, categoryId);
        }


        /**
         * Call this service to get discover page landing posts.
         */
        public static Call<LandingPosts> getDiscoverPagePosts(Context context){
            return getDiscoverService(context).getDiscoverPagePosts();
        }

        /**
         * Call this service to get users list to send follow request.
         */
        public static Call<UsersList> getUsersListToFollow(int page, Context context){
            return getDiscoverService(context).getUsersListToFollow(page);
        }

        /**
         * Call this service to get users list to send follow request with search term.
         */
        public static Call<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm, Context context){
            return getDiscoverService(context).getUsersListToFollowWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get users list to send follow request with search term.
         */
        public static Call<VideosList> getVideosWithSearchTerm(int page, String searchTerm, Context context){
            return getDiscoverService(context).getVideosWithSearchTerm(page, searchTerm);
        }

        private static TeazerApiCall.DiscoverCalls getDiscoverService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(context))
                    .build();
            return retrofit.create(TeazerApiCall.DiscoverCalls.class);
        }
    }

    public static class Friends {
        /**
         * Get the "my circle" with search term
         * */
        public static Call<ResultObject> getMyCircleWithSearchTerm(int page, String searchTerm, Context context) {
            return getFriendsService(context).getMyCircleWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the my followings list
         * */
        public static Call<CircleList> getMyFollowings(int page, Context context) {
            return getFriendsService(context).getMyFollowings(page);
        }

        public static Call<FollowingsList> getMyFollowing(int page, Context context) {
            return getFriendsService(context).getMyFollowing(page);
        }

        /**
         * Call this service to send a join request by user ID
         * */
        public static Call<ResultObject> sendJoinRequestByUserId(int userId, Context context) {
            return getFriendsService(context).sendJoinRequestByUserId(userId);
        }

        /**
         * Call this service to send a join request by user name
         * */
        public static Call<ResultObject> sendJoinRequestByUsername(String username, Context context) {
            return getFriendsService(context).sendJoinRequestByUsername(username);
        }

        /**
         * Call this service to accept the join request
         * */
        public static Call<ResultObject> acceptJoinRequest(int notificationId, Context context) {
            return getFriendsService(context).acceptJoinRequest(notificationId);
        }

        /**
         * Call this service to delete the join request
         * */
        public static Call<ResultObject> deleteJoinRequest(int notificationId, Context context) {
            return getFriendsService(context).deleteJoinRequest(notificationId);
        }

         public static Call<ResultObject> cancelRequest(int userId, Context context) {
            return getFriendsService(context).cancelRequest(userId);
        }

        /**
         * Call this service to get the my circle list
         * @return If “nextPage” is true some more records present. So, you can call again with increase the page count by 1.
         * If “next_page” is false no more records present.
         * */
        public static Call<CircleList> getMyCircle(int page, Context context) {
            return getFriendsService(context).getMyCircle(page);
        }

        /**
         * Call this service to get the my followings list with search term
         * */
        public static Call<ResultObject> getMyFollowingsWithSearchTerm(int page, String searchTerm, Context context) {
            return getFriendsService(context).getMyFollowingsWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the friends followings list
         * */
        public static Call<FollowingsList> getFriendsFollowings(int page, int userId, Context context) {
            return getFriendsService(context).getFriendsFollowings(page, userId);
        }

        /**
         * Call this service to get the friends followings list with search term
         * */
        public static Call<ResultObject> getFriendsFollowingsWithSearchTerm(
                int userId,
                int page,
                String searchTerm, Context context) {
            return getFriendsService(context).getFriendsFollowingsWithSearchTerm(userId, page, searchTerm);
        }

        /**
         * Call this service to get the my followers list
         * */
        public static Call<FollowersList> getMyFollowers(int page, Context context) {
            return getFriendsService(context).getMyFollowers(page);
        }

        /**
         * Call this service to get the my followers list with search term
         * */
        public static Call<ResultObject> getMyFollowersWithSearchTerm(int page, String searchTerm, Context context) {
            return getFriendsService(context).getMyFollowersWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the friends followers list
         * */
        public static Call<FollowersList> getFriendsFollowers(int page, int userId, Context context) {
            return getFriendsService(context).getFriendsFollowers(page, userId);
        }

        /**
         * Call this service to get the friends followers list with search term
         * */
        public static Call<ResultObject> getFriendsFollowersWithSearchTerm(
                int userId,
                int page,
                String searchTerm, Context context) {
            return getFriendsService(context).getFriendsFollowersWithSearchTerm(userId, page, searchTerm);
        }

        /**
         * Call this service to unfollow a user
         * */
        public static Call<ResultObject> unfollowUser(int userId, Context context) {
            return getFriendsService(context).unfollowUser(userId);
        }

        public static Call<ResultObject> followUser(int userId, Context context) {
            return getFriendsService(context).followUser(userId);
        }

        /**
         * Call this service to get other's profile information
         * @return “account_type” 1 is a Private account, 2 is a Public account.
         *          “can_join” tell whether you peoples are already friends.
         *          Based on “account_type” you can read either private or public profile.
         * */
        public static Call<ProfileInfo> getOthersProfileInfo(int userId, Context context) {
            return getFriendsService(context).getOthersProfileInfo(userId);
        }
//            public static Call<Profile> getOthersProfileInfoNoti(int userId, Context context) {
//            return getFriendsService(context).getOthersProfileInfoNoti(userId);
//        }

        /**
         * Call this service to Block/Unblock a user
         * @param status should be 1 for block and 2 for unblock.
         */
        public static Call<ResultObject> blockUnblockUser(int userId, int status, Context context){
            return getFriendsService(context).blockUnblockUser(userId, status);

        }
        public static Call<LikedUserPost> getLikedUsers(int postId, int page, Context context){
            return getFriendsService(context).getLikedUsers(postId, page);
        }

        /**
         * Call this service to get blocked users list by you.
         */
        public static Call<BlockedUsersList> getBlockedUsers(int page, Context context){
            return getFriendsService(context).getBlockedUsers(page);
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

        private static TeazerApiCall.FriendsCalls getFriendsService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(context))
                    .build();
            return retrofit.create(TeazerApiCall.FriendsCalls.class);
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
        public static Call<ReactionUploadResult> uploadReaction(MultipartBody.Part video, int postId, Context context, String title) {
            return getReactService(context).uploadReaction(video, postId, title);
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
        public static Call<ResultObject> likeDislikeReaction(int reactId, int status, Context context) {
            return getReactService(context).likeDislikeReaction(reactId, status);
        }

        /**
         * Call this service to increase the reacted video view count
         *
         * @return 200 : If “status” is true reacted video view count increased successfully.
         * If “status” is false reacted video view count increased failed.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> incrementReactionViewCount(int mediaId, Context context) {
            return getReactService(context).incrementReactionViewCount(mediaId);
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
        public static Call<ResultObject> deleteReaction(int reactId, Context context) {
            return getReactService(context).deleteReaction(reactId);
        }

        /**
         * Call this service to report the reaction.
         *
         * @return 200 : If “status” is true reported successfully.
         * If “status” is false reported failed or you have already reported.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> reportReaction(ReportReaction reportReaction, Context context) {
            return getReactService(context).reportReaction(reportReaction);
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
        public static Call<ResultObject> hideOrShowReaction(int reactId, int status, Context context) {
            return getReactService(context).hideOrShowReaction(reactId, status);
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
        public static Call<ReactionsList> getMyReactions(int page, Context context) {
            return getReactService(context).getMyReactions(page);
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
        public static Call<ResultObject> getFriendsReactions(int page, int friend_id, Context context) {
            return getReactService(context).getFriendsReactions(page, friend_id);
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
        public static Call<ResultObject> likeDislikeReaction(int page, Context context) {
            return getReactService(context).getHiddenReactions(page);
        }

        public static Call<ReactionResponse> getReactionDetail(int reactId, Context context) {
            return getReactService(context).getReactionDetail(reactId);
        }
        public static Call<ReactVideoDetailsResponse> getReactionDetail2(int reactId, Context context) {
            return getReactService(context).getReactionDetail2(reactId);
        }

        private static TeazerApiCall.ReactCalls getReactService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(context))
                    .build();
            return retrofit.create(TeazerApiCall.ReactCalls.class);
        }
    }

    public static class Posts {

        public static Call<PostUploadResult> uploadVideo(MultipartBody.Part videoPartFile, String title, @NonNull String location,
                                                         double latitude, double longitude,
                                                         String tags, String categories, Context context) {
            return getPostalService(context).uploadVideo(videoPartFile, title, location, latitude, longitude, tags, categories);
        }

        public static Call<PostUploadResult> updatePost(UpdatePostRequest updatePostRequest, Context context) {
            return getPostalService(context).updatePost(updatePostRequest);
        }

        public static Call<ResultObject> likeDislikePost(int postId, int status, Context context) {
            return getPostalService(context).likeDislikePost(postId, status);
        }

        public static Call<ResultObject> incrementViewCount(int mediaId, Context context) {
            return getPostalService(context).incrementViewCount(mediaId);
        }

        public static Call<ResultObject> deletePost(int postId, Context context) {
            return getPostalService(context).deletePost(postId);
        }

        public static Call<ResultObject> deletePosts(int postId, Context context) {
            return getPostalService(context).deletePostVideo(postId);
        }

        public static Call<ResultObject> reportPost(ReportPost reportPostDetails, Context context) {
            return getPostalService(context).reportPost(reportPostDetails);
        }

        public static Call<ResultObject> hideOrShowPost(int postId, int status, Context context) {
            return getPostalService(context).hideOrShowPost(postId, status);
        }

        public static Call<PostDetails> getPostDetails(int postId, Context context) {
            return getPostalService(context).getPostDetails(postId);
        }

        public static Call<TaggedUsersList> getTaggedUsers(int postId, int page, Context context) {
            return getPostalService(context).getTaggedUsers(postId, page);
        }

        public static Call<PostList> getHiddenPosts(int page, Context context) {
            return getPostalService(context).getHiddenPosts(page);
        }

        public static Call<PostList> getHomePagePosts(int page, Context context) {
            return getPostalService(context).getHomePagePosts(page);
        }

        public static Call<PostList> getPostedVideos(int page, Context context) {
            return getPostalService(context).getPostedVideos(page);
        }

        public static Call<PostList> getVideosPostedByFriends(int page, int friendId, Context context) {
            return getPostalService(context).getVideosPostedByFriends(page, friendId);

        }

        public static Call<PostList> getVideosPostedByFriend(int page, int friendId, Context context) {
            return getPostalService(context).getVideosPostedByFriend(page, friendId);
        }

        public static Call<PostReactionsList> getReactionsOfPost(int postId, int page, Context context) {
            return getPostalService(context).getReactionsOfPost(postId, page);
        }

        public static Call<PostList>getPostedVideos(Context context, int page) {
            return getPostalService(context).getPostedVideos(page);

        }
        public static Call<PostList> getHiddenVideosList(int page, Context context){
            return getPostalService(context).getHiddenVideosList(page);
        }

        public static Call<ResultObject> getAllHiddenVideosList(int userID, Context context){
            return getPostalService(context).getAllHiddenVideosList(userID);
        }
        private static TeazerApiCall.Posts getPostalService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(context))
                    .build();
            return retrofit.create(TeazerApiCall.Posts.class);
        }
    }

    public static class User {
        public static Call<ResultObject> updateUserProfileMedia(MultipartBody.Part file, Context context) {
            return getUserService(context).updateUserProfileMedia(file);
        }

        public static  Call<ResultObject> resetFcmToken(String header, String token, Context context) {
            return getUserService(context).resetFcmToken(header, token);
        }

        public static Call<ResultObject> setAccountVisibility(int accountType, Context context) {
            return getUserService(context).setAccountVisibility(accountType);
        }

        public static Call<UserProfile> getUserProfile(Context context) {
            return getUserService(context).getUserProfile();

        }

        public static Call<ResultObject> changeMobileNumber(Context context, ChangeMobileNumber changeMobileNumber) {
            return getUserService(context).changeMobileNumber(changeMobileNumber);

        }

          public static Call<ResultObject> updateMobileNumber(Context context, UpdateMobileNumber updateMobileNumber) {
            return getUserService(context).updateMobileNumber(updateMobileNumber);

        }

        public static Call<ResultObject> updateUserProfiles(ProfileUpdateRequest updateProfileDetails, Context context) {
            return getUserService(context).updateUserProfile(updateProfileDetails);
        }

        public static Call<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails, Context context) {
            return getUserService(context).updatePassword(updatePasswordDetails);
        }
        public static Call<ResultObject> setPassword(SetPasswordRequest setPasswordDetails, Context context) {
            return getUserService(context).setPassword(setPasswordDetails);
        }

        public static Call<NotificationsList> getFollowingNotifications(int page, Context context){
            return getUserService(context).getFollowingNotifications(page);
        }

        public static Call<NotificationsList> getRequestNotifications(int page, Context context){
            return getUserService(context).getRequestNotifications(page);
        }

        public static Call<ResultObject> updateCategories(UpdateCategories categories, Context context) {
            return getUserService(context).updateCategories(categories);
        }

        public static Call<ResultObject> logout(String header, Context context) {
            return getUserService(context).logout(header);
        }

        public static Call<UserProfile>getUserProfileDetail(Context context) {
            return getUserService(context).getUserProfile();
        }

        public static Call<ResultObject> reportUsers(ReportUser reportuser, Context context){
            return getUserService(context).reportUser(reportuser);
        }

        public static Call<ResultObject> deactivateAccount(Context context, DeactivateAccountRequest deactivateAccountRequest) {
            return getUserService(context).deactivateAccount(deactivateAccountRequest);
        }

        public static Call<ResultObject> resetUnreadNotification(Context context, int type) {
            return getUserService(context).resetUnreadNotification(type);
        }
        public static Call<ResultObject> removeProfilepic(Context context) {
            return getUserService(context).removeProfilePic();
        }

        private static TeazerApiCall.UserCalls getUserService(Context context) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(context.getString(R.string.base_url))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(getOkHttpClientWithAuthToken(context))
                    .build();
            return retrofit.create(TeazerApiCall.UserCalls.class);
        }
    }

    private static void getAvailabilityServiceCallback(Call<ResultObject> service,
                                                       final ProximaNovaRegularAutoCompleteTextView view,
                                                       final boolean isSignUp) {
        service.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
//                    Toast.makeText(context, response.code() + ":" + response.body().getStatus() + ", " + isSignUp, Toast.LENGTH_SHORT).show();
                    if (isSignUp) {
                        if (!response.body().getStatus()) {
                            setEditTextDrawableEnd(view, R.drawable.ic_tick_circle);
                        } else {
                            setEditTextDrawableEnd(view, R.drawable.ic_cross);
                        }
                    } else {
                        if (!response.body().getStatus()) {
                            setEditTextDrawableEnd(view, R.drawable.ic_cross);
                        } else {
                            setEditTextDrawableEnd(view, R.drawable.ic_tick_circle);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Snackbar.make(view, "Failed to get availability", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private static OkHttpClient getOkHttpClientWithAuthToken(final Context context) {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", "Bearer " + SharedPrefs.getAuthToken(context))
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
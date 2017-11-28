package com.cncoding.teazer.apiCalls;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.model.profile.blockuser.BlockUnBlockUser;
import com.cncoding.teazer.model.profile.blockuser.BlockUsers;
import com.cncoding.teazer.model.profile.delete.DeleteMyVideos;
import com.cncoding.teazer.model.profile.followerprofile.FollowersProfile;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.FollowersProfileCreations;
import com.cncoding.teazer.model.profile.followers.ProfileMyFollowers;
import com.cncoding.teazer.model.profile.following.ProfileMyFollowing;
import com.cncoding.teazer.model.profile.otherfollower.FreindFollower;
import com.cncoding.teazer.model.profile.othersfollowing.OthersFollowing;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdate;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdateRequest;
import com.cncoding.teazer.model.profile.reaction.ProfileReaction;
import com.cncoding.teazer.model.profile.reportuser.ReportUser;
import com.cncoding.teazer.model.profile.userProfile.SetPasswordRequest;
import com.cncoding.teazer.model.profile.userProfile.UpdatePasswordRequest;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Authorize;
import com.cncoding.teazer.utilities.Pojos.Friends.CircleList;
import com.cncoding.teazer.utilities.Pojos.Post.LandingPosts;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostList;
import com.cncoding.teazer.utilities.Pojos.Post.PostReactionsList;
import com.cncoding.teazer.utilities.Pojos.Post.TaggedUsersList;
import com.cncoding.teazer.utilities.Pojos.React.UserReactionsList;
import com.cncoding.teazer.utilities.Pojos.User.Profile;
import com.cncoding.teazer.utilities.SharedPrefs;

import java.io.IOException;
import java.util.ArrayList;

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

import static com.cncoding.teazer.MainActivity.BASE_URL;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;
//5346800017115465
//136

/**
 *
 * Created by Prem $ on 10/10/2017.
 */

public class ApiCallingService {

//    private static String AUTH_TOKEN = "Bearer 8c2400ccd8c32d572cc8181ccadc70c08f5df408b14e0c77b60e2277825ef2ad";
    public static final int SUCCESS_OK_TRUE = 1;
    public static final int SUCCESS_OK_FALSE = 2;
    static final int FAIL = 3;
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static class Application {

        public static Call<ArrayList<Pojos.Application.ReportType>> getPostReportTypes() {
            return getApplicationService().getPostReportTypes();
        }

        public static Call<ArrayList<Pojos.Application.ReportType>> getProfileReportTypes() {
            return getApplicationService().getProfileReportTypes();
        }

        public static Call<ArrayList<Pojos.Category>> getCategories() {
            return getApplicationService().getCategories();
        }

        private static TeazerApiCall.ApplicationCalls getApplicationService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();
            return retrofit.create(TeazerApiCall.ApplicationCalls.class);
        }
    }

    public static class Auth {

        public static void checkUsername(ProximaNovaRegularAutoCompleteTextView usernameView, boolean isSignUp) {
            getAvailabilityServiceCallback(
                    getAuthenticationService().checkUsernameAvailability(usernameView.getText().toString()),
                    usernameView, isSignUp);
        }

        public static void checkEmail(ProximaNovaRegularAutoCompleteTextView emailView, boolean isSignUp) {
            getAvailabilityServiceCallback(
                    getAuthenticationService().checkEmailAvailability(emailView.getText().toString()),
                    emailView, isSignUp);
        }

        public static void checkPhoneNumber(int countryCode, ProximaNovaRegularAutoCompleteTextView phoneNumberView, boolean isSignUp) {
            String phoneString = phoneNumberView.getText().toString();
            if (!phoneString.isEmpty()) {
                getAvailabilityServiceCallback(
                        getAuthenticationService().checkPhoneNumberAvailability(
                                countryCode,
                                Long.parseLong(phoneString)),
                        phoneNumberView, isSignUp);
            }
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

        private static TeazerApiCall.AuthenticationCalls getAuthenticationService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
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

        private static TeazerApiCall.DiscoverCalls getDiscoverService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
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

        public static Call<ProfileMyFollowing> getMyFollowing(int page, Context context) {
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
        public static Call<OthersFollowing> getFriendsFollowings(int page, int userId, Context context) {
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
        public static Call<ProfileMyFollowers> getMyFollowers(int page, Context context) {
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
        public static Call<FreindFollower> getFriendsFollowers(int page, int userId, Context context) {
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
        public static Call<FollowersProfile> getOthersProfileInfo(int userId, Context context) {
            return getFriendsService(context).getOthersProfileInfo(userId);
        }
            public static Call<Profile> getOthersProfileInfoNoti(int userId, Context context) {
            return getFriendsService(context).getOthersProfileInfoNoti(userId);
        }

        /**
         * Call this service to Block/Unblock a user
         * @param status should be 1 for block and 2 for unblock.
         */
        public static Call<BlockUnBlockUser> blockUnblockUser(int userId, int status, Context context){
            return getFriendsService(context).blockUnblockUser(userId, status);
        }



        /**
         * Call this service to get blocked users list by you.
         */
        public static Call<BlockUsers> getBlockedUsers(int page, Context context){
            return getFriendsService(context).getBlockedUsers(page);
        }

        /**
         * Call this service to get users list to send follow request.
         */
        public static Call<Pojos.Friends.UsersList> getUsersListToFollow(int page, Context context){
            return getFriendsService(context).getUsersListToFollow(page);
        }

        /**
         * Call this service to get users list to send follow request with search term.
         */
        public static Call<Pojos.Friends.UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm, Context context){
            return getFriendsService(context).getUsersListToFollowWithSearchTerm(page, searchTerm);
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
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
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
        public static Call<ResultObject> uploadReaction(MultipartBody.Part video, int postId, Context context, String title) {
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
        public static Call<ResultObject> reportReaction(Pojos.React.ReportReaction reportReaction, Context context) {
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
         * Call this service to get the reactions of user.
         *
         * @return 200 : If “nextPage” is true some more records present,
         * so you can call again after incrementing the page count by 1,
         * If “next_page” is false no more records present.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<UserReactionsList> getMyReactions(int page, Context context) {
            return getReactService(context).getMyReactions(page);
        }


        public static Call<ProfileReaction> getMyReaction(int page, Context context) {
            return getReactService(context).getMyReaction(page);
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
         * Call this service to get the reactions hidden by user.
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

        private static TeazerApiCall.ReactCalls getReactService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientWithAuthToken(context))
                    .build();
            return retrofit.create(TeazerApiCall.ReactCalls.class);
        }
    }

    public static class Posts {

        public static Call<ResultObject> uploadVideo(MultipartBody.Part videoPartFile, String title, @NonNull String location,
                                                     double latitude, double longitude,
                                                     String tags, String categories, Context context) {
            return getPostalService(context).uploadVideoToServer(videoPartFile, title, location, latitude, longitude, tags, categories);
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

        public static Call<DeleteMyVideos> deletePosts(int postId, Context context) {
            return getPostalService(context).deletePostVideo(postId);
        }

        public static Call<ResultObject> reportPost(Pojos.Post.ReportPost reportPostDetails, Context context) {
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

        public static Call<FollowersProfileCreations> getVideosPostedByFriend(int page, int friendId, Context context) {
            return getPostalService(context).getVideosPostedByFriend(page, friendId);
        }

        public static Call<PostReactionsList> getReactionsOfPost(int postId, int page, Context context) {
            return getPostalService(context).getReactionsOfPost(postId, page);
        }

        public static Call<PostList>getPostedVideos(Context context, int page) {
            return getPostalService(context).getPostedVideos(page);

        }

        private static TeazerApiCall.Posts getPostalService(Context context) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
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

        public static Call<Pojos.User.UserProfile> getUserProfile(Context context) {
            return getUserService(context).getUserProfile();

        }


        public static Call<ProfileUpdate> updateUserProfiles(ProfileUpdateRequest updateProfileDetails, Context context) {
            return getUserService(context).updateUserProfile(updateProfileDetails);
        }

        public static Call<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails, Context context) {
            return getUserService(context).updatePassword(updatePasswordDetails);
        }
        public static Call<ResultObject> setPassword(SetPasswordRequest setPasswordDetails, Context context) {
            return getUserService(context).setPassword(setPasswordDetails);
        }

        public static Call<Pojos.User.NotificationsList> getFollowingNotifications(int page, Context context){
            return getUserService(context).getFollowingNotifications(page);
        }

        public static Call<Pojos.User.NotificationsList> getRequestNotifications(int page, Context context){
            return getUserService(context).getRequestNotifications(page);
        }

        public static Call<ResultObject> updateCategories(Pojos.User.UpdateCategories categories, Context context) {
            return getUserService(context).updateCategories(categories);
        }

        public static Call<ResultObject> logout(String header, Context context) {
            return getUserService(context).logout(header);
        }

        public static Call<Pojos.User.UserProfile>getUserProfileDetail(Context context) {
            return getUserService(context).getUserProfile();
        }

        public static Call<ResultObject> reportUsers(ReportUser reportuser, Context context){
            return getUserService(context).reportUser(reportuser);
        }

        private static TeazerApiCall.UserCalls getUserService(Context context) {






            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
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
        }).addInterceptor(logging).build();
    }
    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder().addInterceptor(logging).build();
    }
}
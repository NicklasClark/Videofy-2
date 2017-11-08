package com.cncoding.teazer.apiCalls;

import android.support.design.widget.Snackbar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Authorize;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.Post.PostList;
import com.cncoding.teazer.utilities.Pojos.Post.PostReactionsList;
import com.cncoding.teazer.utilities.Pojos.Post.TaggedUsersList;
import com.cncoding.teazer.utilities.Pojos.React.UserReactionsList;
import com.cncoding.teazer.utilities.Pojos.User.UserProfile;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.cncoding.teazer.MainActivity.BASE_URL;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;
//5346800017115465
//136

/**
 *
 * Created by Prem $ on 10/10/2017.
 */

public class ApiCallingService {

    private static String AUTH_TOKEN = "Bearer 74621f519ea5f547b398adceb794d19b5e2af43282d7f9914801d11482e11ce3";
    public static final int SUCCESS_OK_TRUE = 1;
    public static final int SUCCESS_OK_FALSE = 2;
    public static final int FAIL = 3;

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
                    .addConverterFactory(GsonConverterFactory.create())
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

        public static Call<ResultObject> resetPasswordByOtp(Authorize resetPasswordDetails) {
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
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(TeazerApiCall.AuthenticationCalls.class);
        }
    }

    public static class Friends {
        /**
         * Get the "my circle" with search term
         * */
        public static Call<ResultObject> getMyCircleWithSearchTerm(int page, String searchTerm) {
            return getFriendsService().getMyCircleWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the my followings list
         * */
        public static Call<Pojos.Friends.CircleList> getMyFollowings(int page) {
            return getFriendsService().getMyFollowings(page);
        }

        /**
         * Call this service to send a join request
         * */
        public static Call<ResultObject> sendJoinRequest(int userId) {
            return getFriendsService().sendJoinRequest(userId);
        }

        /**
         * Call this service to accept the join request
         * */
        public static Call<ResultObject> acceptJoinRequest(int notificationId) {
            return getFriendsService().acceptJoinRequest(notificationId);
        }

        /**
         * Call this service to delete the join request
         * */
        public static Call<ResultObject> deleteJoinRequest(int page, String searchTerm) {
            return getFriendsService().deleteJoinRequest(page, searchTerm);
        }

        /**
         * Call this service to get the my circle list
         * @return If “nextPage” is true some more records present. So, you can call again with increase the page count by 1.
         * If “next_page” is false no more records present.
         * */
        public static Call<ResultObject> getMyCircle(int page) {
            return getFriendsService().getMyCircle(page);
        }

        /**
         * Call this service to get the my followings list with search term
         * */
        public static Call<ResultObject> getMyFollowingsWithSearchTerm(int page, String searchTerm) {
            return getFriendsService().getMyFollowingsWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the friends followings list
         * */
        public static Call<ResultObject> getFriendsFollowings(int page, int userId) {
            return getFriendsService().getFriendsFollowings(page, userId);
        }

        /**
         * Call this service to get the friends followings list with search term
         * */
        public static Call<ResultObject> getFriendsFollowingsWithSearchTerm(
                int userId,
                int page,
                String searchTerm) {
            return getFriendsService().getFriendsFollowingsWithSearchTerm(userId, page, searchTerm);
        }

        /**
         * Call this service to get the my followers list
         * */
        public static Call<ResultObject> getMyFollowers(int page) {
            return getFriendsService().getMyFollowers(page);
        }

        /**
         * Call this service to get the my followers list with search term
         * */
        public static Call<ResultObject> getMyFollowersWithSearchTerm(int page, String searchTerm) {
            return getFriendsService().getMyFollowersWithSearchTerm(page, searchTerm);
        }

        /**
         * Call this service to get the friends followers list
         * */
        public static Call<ResultObject> getFriendsFollowers(int page, int userId) {
            return getFriendsService().getFriendsFollowers(page, userId);
        }

        /**
         * Call this service to get the friends followers list with search term
         * */
        public static Call<ResultObject> getFriendsFollowersWithSearchTerm(
                int userId,
                int page,
                String searchTerm) {
            return getFriendsService().getFriendsFollowersWithSearchTerm(userId, page, searchTerm);
        }

        /**
         * Call this service to unfollow a user
         * */
        public static Call<ResultObject> unfollowUser(int userId) {
            return getFriendsService().unfollowUser(userId);
        }

        /**
         * Call this service to get other's profile information
         * @return “account_type” 1 is a Private account, 2 is a Public account.
         *          “can_join” tell whether you peoples are already friends.
         *          Based on “account_type” you can read either private or public profile.
         * */
        public static Call<ResultObject> getOthersProfileInfo(int userId) {
            return getFriendsService().getOthersProfileInfo(userId);
        }

        private static TeazerApiCall.FriendsCalls getFriendsService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientWithAuthToken())
                    .build();
            return retrofit.create(TeazerApiCall.FriendsCalls.class);
        }

        public static int isResponseOk(Response<Pojos.Friends.CircleList> response) {
            switch (response.code()) {
                case 200:
                    if (response.body().isNextPage())
                        return SUCCESS_OK_TRUE;
                    else return SUCCESS_OK_FALSE;
                default:
                    return FAIL;
            }
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
        public static Call<ResultObject> postReaction(MultipartBody.Part video, int postId) {
            return getReactService().postReaction(video, postId);
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
        public static Call<ResultObject> likeDislikeReaction(int reactId, int status) {
            return getReactService().likeDislikeReaction(reactId, status);
        }

        /**
         * Call this service to increase the reacted video view count
         *
         * @return 200 : If “status” is true reacted video view count increased successfully.
         * If “status” is false reacted video view count increased failed.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> incrementReactionViewCount(int mediaId) {
            return getReactService().incrementReactionViewCount(mediaId);
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
        public static Call<ResultObject> deleteReaction(int reactId) {
            return getReactService().deleteReaction(reactId);
        }

        /**
         * Call this service to report the reaction.
         *
         * @return 200 : If “status” is true reported successfully.
         * If “status” is false reported failed or you have already reported.
         * 401 : Un-Authorized access.
         * 412 : Validation failed.
         */
        public static Call<ResultObject> reportReaction(Pojos.React.ReportReaction reportReaction) {
            return getReactService().reportReaction(reportReaction);
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
        public static Call<ResultObject> hideOrShowReaction(int reactId, int status) {
            return getReactService().hideOrShowReaction(reactId, status);
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
        public static Call<UserReactionsList> getMyReactions(int page) {
            return getReactService().getMyReactions(page);
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
        public static Call<ResultObject> getFriendsReactions(int page, int friend_id) {
            return getReactService().getFriendsReactions(page, friend_id);
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
        public static Call<ResultObject> likeDislikeReaction(int page) {
            return getReactService().getHiddenReactions(page);
        }

        private static TeazerApiCall.ReactCalls getReactService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientWithAuthToken())
                    .build();
            return retrofit.create(TeazerApiCall.ReactCalls.class);
        }
    }

    public static class Posts {

        public static Call<ResultObject> uploadVideo(MultipartBody.Part videoPartFile, String title) {
            return getPostalService().uploadVideoToServer(videoPartFile, title, "Bangalore", 12.971599, 77.594563);
        }

        public static Call<ResultObject> likeDislikePost(int postId, int status) {
            return getPostalService().likeDislikePost(postId, status);
        }

        public static Call<ResultObject> incrementViewCount(int mediaId) {
            return getPostalService().incrementViewCount(mediaId);
        }

        public static Call<ResultObject> deletePost(int postId) {
            return getPostalService().deletePost(postId);
        }

        public static Call<ResultObject> reportPost(Pojos.Post.ReportPost reportPostDetails) {
            return getPostalService().reportPost(reportPostDetails);
        }

        public static Call<ResultObject> hideOrShowPost(int postId, int status) {
            return getPostalService().hideOrShowPost(postId, status);
        }

        public static Call<PostDetails> getPostDetails(int postId) {
            return getPostalService().getPostDetails(postId);
        }

        public static Call<TaggedUsersList> getTaggedUsers(int postId, int page) {
            return getPostalService().getTaggedUsers(postId, page);
        }

        public static Call<PostList> getHiddenPosts(int page) {
            return getPostalService().getHiddenPosts(page);
        }

        public static Call<PostList> getHomePagePosts(int page) {
            return getPostalService().getHomePagePosts(page);
        }

        public static Call<PostList> getPostedVideos(int page) {
            return getPostalService().getPostedVideos(page);
        }

        public static Call<PostList> getVideosPostedByFriends(int page, int friendId) {
            return getPostalService().getVideosPostedByFriends(page, friendId);
        }

        public static Call<PostReactionsList> getReactionsOfPost(int postId, int page) {
            return getPostalService().getReactionsOfPost(postId, page);
        }

        private static TeazerApiCall.Posts getPostalService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientWithAuthToken())
                    .build();
            return retrofit.create(TeazerApiCall.Posts.class);
        }
    }

    public static class User {
        public static Call<ResultObject> updateUserProfileMedia(MultipartBody.Part file) {
            return getUserService().updateUserProfileMedia(file);
        }

        public static  Call<ResultObject> resetFcmToken(String header, String token) {
            return getUserService().resetFcmToken(header, token);
        }

        public static Call<ResultObject> setAccountVisibility(int accountType) {
            return getUserService().setAccountVisibility(accountType);
        }

        public static Call<UserProfile> getUserProfile() {
            return getUserService().getUserProfile();
        }

        public static Call<ResultObject> updateUserProfile(Pojos.User.UpdateProfile updateProfileDetails) {
            return getUserService().updateUserProfile(updateProfileDetails);
        }

        public static Call<ResultObject> updatePassword(Pojos.User.UpdatePassword updatePasswordDetails) {
            return getUserService().updatePassword(updatePasswordDetails);
        }

        public static Call<ResultObject> updateCategories(Pojos.User.UpdateCategories categories) {
            return getUserService().updateCategories(categories);
        }

        public static Call<ResultObject> getNotifications(int page) {
            return getUserService().getNotifications(page);
        }

        public static Call<ResultObject> logout(String header) {
            return getUserService().logout(header);
        }

        private static TeazerApiCall.UserCalls getUserService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClientWithAuthToken())
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

    private static OkHttpClient getOkHttpClientWithAuthToken() {
        return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("Authorization", AUTH_TOKEN)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        }).build();
    }
}
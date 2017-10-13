package com.cncoding.teazer.apiCalls;

import android.support.annotation.Nullable;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * Created by Prem $ on 10/3/2017.
 */

public class TeazerApiCall {

    /**
     * Authentication interfaces
     */
    public interface AuthenticationCalls {

        /**
         * Perform Signup, pass in the user details in a JSON
         *      {
         *          "user_name": "string",
         *          "first_name": "string",
         *          "last_name": "string",
         *          "email": "string",
         *          "password": "string",
         *          "phone_number": 0,
         *          "country_code": 0,
         *          "fcm_token": "string",
         *          "device_id": "string",
         *          "device_type": 0
         *      }
         * Responses:
         *      200: Signup failed, Username, Email or Phone Number already exist. Status will be false
         *      201: Signup successful. Status will be true
         *      400: Invalid JSON Format present in Request Body
         *      412: Validation Failed
         */
        @POST("/api/v1/authentication/signup")
        Call<ResultObject> signUp(@Body UserAuth.SignUp signUpBody);

        @POST("/api/v1/authentication/signup/verify")
        Call<ResultObject> verifySignUp(@Body UserAuth.SignUp.Verify verifySignUp);

        @POST("/api/v1/authentication/social/signup")
        Call<ResultObject> socialSignUp(@Body UserAuth.SignUp.Social socialSignUpDetails);

        /**
         * Perform sign in with password.
         */
        @POST("/api/v1/authentication/signin/with/password")
        Call<ResultObject> loginWithPassword(@Body UserAuth.LoginWithPassword loginWithPassword);

        /**
         * Perform sign in with OTP.
         */
        @POST("/api/v1/authentication/signin/with/otp")
        Call<ResultObject> loginWithOtp(@Body UserAuth.PhoneNumberDetails phoneNumberDetails);

        /**
         * Perform sign in with OTP.
         */
        @POST("/api/v1/authentication/signin/with/otp")
        Call<ResultObject> verifyLoginWithOtp(@Body UserAuth.PhoneNumberDetails.Verify verifyLoginWithOtp);

        /**
         * Check username availability, Pass in @param String username
         * */
        @GET("/api/v1/authentication/check/username/{username}")
        Call<ResultObject> checkUsernameAvailability(@Path("username") String username);

        /**
         * Check email availability, Pass in @param String email
         * */
        @GET("/api/v1/authentication/check/email/{email}")
        Call<ResultObject> checkEmailAvailability(@Path("email") String email);

        /**
         * Check phone number availability, Pass in @param String phoneNumber
         * */
        @GET("/api/v1/authentication/check/phonenumber")
        Call<ResultObject> checkPhoneNumberAvailability(
                @Query("countryCode") int countryCode, @Query("phonenumber") long phoneNumber);

        /**
         * To verify the forgot password OTP
         * */
        @GET("/api/v1/authentication/reset/password/verify/otp/{otp}")
        Call<ResultObject> verifyForgotPasswordOtp(@Path("otp") int otp);

        /**
         * Forgot password reset by email
         * Call this service to get OTP for reset the password from forgot password
         * */
        @POST("/api/v1/authentication/reset/password/by/{email_id}")
        Call<ResultObject> requestResetPasswordByEmail(@Path("email_id") String email);

        /**
         * Reset password by OTP
         * Call this service to reset the password by OTP received in email or phone number
         */
        @POST("/api/v1/authentication/reset/password/by/phonenumber")
        Call<ResultObject> requestResetPasswordByPhone(@Body UserAuth.PhoneNumberDetails phoneNumberDetails);

        /**
         * Call this service to reset the password by OTP received in email or phone number.
         * Either you need to send Email or Phone number and Country code along with OTP and New password
         * */
        @POST("/api/v1/authentication/password/reset")
        Call<ResultObject> resetPasswordByOtp(@Body UserAuth.ResetPasswordDetails resetPasswordDetails);
    }

    /**
     * Videos upload interface
     */
    public interface UploadCalls {

        /**
         * Call this service to upload the video
         * */
        @Multipart
        @POST("/api/v1/post/create")
        Call<ResultObject> uploadVideoToServer(
                @Part MultipartBody.Part video,
                @Part("title") String title,
                @Part("location") String location,
                @Part("latitude") double latitude,
                @Part("longitude") double longitude
        );
    }

    /**
     * User actions
     */
    public interface UserActionCalls {

        /**
         * Reset the FCM Token
         * Call this service for update the FCM Token.
         * */
        @PUT("/api/v1/user/update/fcm/{fcmToken}")
        Call<ResultObject> resetFcmToken(
                @Nullable @Part("Authorization") String header,
                @Path("fcmToken") String token
        );

        /**
         * Set account as Private or Public
         * Call this service to make a profile as Private or Public.
         * Send accountType as @int 1 for Private, @int 2 for Public
         * */
        @PUT("/api/v1/user/profile/visibility")
        Call<ResultObject> setAccountVisibility(@Part("accountType") int accountType);

        /**
         * Get user profile
         * Call this service to get user profile details
         * */
        @GET("/api/v1/user/profile")
        Call<ResultObject> getUserProfile();

        /**
         * Update user profile
         * Call this service to update user profile
         *
         * body JSON format:
         *      {
         *          "first_name": "string",
         *          "last_name": "string",
         *          "email": "string",
         *          "phone_number": 0,
         *          "country_code": 0
         *      }
         * */
        @PUT("/api/v1/user/update/profile")
        Call<ResultObject> updateUserProfile();

        /**
         * Call this service to update account password
         * body JSON format:
         *      {
         *          "old_password": "string",
         *          "new_password": "string"
         *      }
         * */
        @PUT("/api/v1/user/update/password")
        Call<ResultObject> updatePassword();

        /**
         * Call this service to get notifications
         * @param page is the initial page of notifications
         *      If “nextPage” is true, then some more records are present.
         *      So, you can call this method again after increasing the page count by 1,
         *      If “next_page” is false no more records present.
         *
         *      Return JSON format:
         *      {
         *          "notifications": [
         *              {
         *                  "notification_id": 0,
         *                  "notification_type": 0,
         *                  "source_id": 0,
         *                  "meta_data": "string",
         *                  "title": "string",
         *                  "message": "string",
         *                  "created_at": "2017-10-04T06:46:13.307Z",
         *                  "has_profile_picture": true,
         *                  "profile_picture": {
         *                      "profile_image_path": "string",
         *                      "thumb_image_path": "string"
         *                  }
         *              }
         *          ],
         *         "next_page": true
         *      }
         * */
        @GET("/api/v1/user/notifications/{page}")
        Call<ResultObject> getNotifications(@Path("page") int page);

        /**
         * Invalidate the AuthToken
         * Call this service for Logout the user.
         * */
        @DELETE("/api/v1/user/logout")
        Call<ResultObject> logout(@Part("Authorization") String header);
    }
}
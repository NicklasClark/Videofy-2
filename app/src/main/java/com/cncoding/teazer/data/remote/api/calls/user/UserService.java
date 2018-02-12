package com.cncoding.teazer.data.remote.api.calls.user;

import android.support.annotation.Nullable;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.model.user.NotificationsList;
import com.cncoding.teazer.model.user.Profile;
import com.cncoding.teazer.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.model.user.ReportUser;
import com.cncoding.teazer.model.user.SetPasswordRequest;
import com.cncoding.teazer.model.user.UpdateCategories;
import com.cncoding.teazer.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.model.user.UserProfile;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Prem$ on 2/5/2018.
 **/

public interface UserService {
//        @GET("/api/DepartmentCRUD/FindAllDepartment")
//        Call<List<Test1>>  getUserDetail();

    /**
     * To update a user profile media
     * Call this service to update a user profile media
     * */
    @Multipart
    @POST("/api/v1/user/update/profile/media")
    Call<ResultObject> updateUserProfileMedia(@Part MultipartBody.Part media);

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
    Call<ResultObject> setAccountVisibility(@Query("accountType") int accountType);

    /**
     * Get user profile
     * Call this service to get user profile details
     * @return {@link UserProfile}
     * */
    @GET("/api/v1/user/profile")
    Call<UserProfile> getUserProfile();

    @POST("/api/v1/user/change/phonenumber")
    Call<ResultObject> changeMobileNumber(@Body ChangeMobileNumber changeMobileNumber);

    @PUT("/api/v1/user/change/phonenumber/verify/otp")
    Call<ResultObject> updateMobileNumber(@Body UpdateMobileNumber updateMobileNumber);

    @GET("/api/v1/user/profile")
    Call<Profile> getUserProfileDetail();

    /**
     * Update user profile
     * Call this service to update user profile.
     * */
    @PUT("/api/v1/user/update/profile")
    Call<ResultObject> updateUserProfile(@Body ProfileUpdateRequest updateProfileDetails);

    /**
     * Call this service to update account password.
     * */
    @PUT("/api/v1/user/update/password")
    Call<ResultObject> updatePassword(@Body UpdatePasswordRequest updatePasswordDetails);

    /**
     * Call this service to update account password.
     * */
    @PUT("api/v1/user/set/new/password")
    Call<ResultObject> setPassword(@Body SetPasswordRequest setPasswordDetails);

    /**
     * Call this service to get Following Notification.
     * */
    @GET("/api/v1/user/notifications/followings/{page}")
    Call<NotificationsList> getFollowingNotifications(@Path("page") int page);

    /**
     * Call this service to get Request Notification.
     * */
    @GET("/api/v1/user/notifications/requests/{page}")
    Call<NotificationsList> getRequestNotifications(@Path("page") int page);

    @POST("/api/v1/user/update/categories")
    Call<ResultObject> updateCategories(@Body UpdateCategories categories);

    /**
     * Invalidate the AuthToken
     * Call this service for Logout the
     * */
    @DELETE("/api/v1/user/logout")
    Call<ResultObject> logout(@Header("Authorization") String header);

    @DELETE("/api/v1/user/remove/profile/media")
    Call<ResultObject> removeProfilePic();

    @POST("/api/v1/user/report")
    Call<ResultObject> reportUser(@Body ReportUser reportuser);

    @POST("/api/v1/user/deactivate")
    Call<ResultObject> deactivateAccount(@Body DeactivateAccountRequest deactivateAccountRequest);

    @PUT("api/v1/user/reset/notification/count")
    Call<ResultObject> resetUnreadNotification(@Query("type") int notificationType);
}
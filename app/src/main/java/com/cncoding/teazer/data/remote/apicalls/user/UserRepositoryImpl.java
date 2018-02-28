package com.cncoding.teazer.data.remote.apicalls.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.data.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.data.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.Profile;
import com.cncoding.teazer.data.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.data.model.user.ReportUser;
import com.cncoding.teazer.data.model.user.SetPasswordRequest;
import com.cncoding.teazer.data.model.user.UpdateCategories;
import com.cncoding.teazer.data.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.remote.ResultObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.notificationListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.resultObjectCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;
import static com.cncoding.teazer.utilities.common.Annotations.AccountType;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_CHANGE_MOBILE_NUMBER;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_DEACTIVATE_ACCOUNT;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_FOLLOWING_NOTIFICATIONS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_REQUEST_NOTIFICATIONS;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_USER_PROFILE;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_USER_PROFILE_DETAIL;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_LOGOUT;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_REMOVE_PROFILE_PIC;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_REPORT_USER;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_RESET_FCM_TOKEN;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_RESET_UNREAD_NOTIFICATION;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_SET_ACCOUNT_VISIBILITY;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_SET_PASSWORD;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPDATE_CATEGORIES;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPDATE_MOBILE_NUMBER;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPDATE_PASSWORD;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPDATE_USER_PROFILE;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_UPDATE_USER_PROFILE_MEDIA;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class UserRepositoryImpl implements UserRepository {

    private UserService userService;

    public UserRepositoryImpl(String token) {
        userService = getRetrofitWithAuthToken(token).create(UserService.class);
    }

    @Override
    public LiveData<ResultObject> updateUserProfileMedia(MultipartBody.Part media) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateUserProfileMedia(media).enqueue(resultObjectCallback(liveData, CALL_UPDATE_USER_PROFILE_MEDIA));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> resetFcmToken(String header, String token) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.resetFcmToken(header, token).enqueue(resultObjectCallback(liveData, CALL_RESET_FCM_TOKEN));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> setAccountVisibility(@AccountType int accountType) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.setAccountVisibility(accountType).enqueue(resultObjectCallback(liveData, CALL_SET_ACCOUNT_VISIBILITY));
        return liveData;
    }

    @Override
    public LiveData<UserProfile> getUserProfile() {
        final MutableLiveData<UserProfile> liveData = new MutableLiveData<>();
        userService.getUserProfile().enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_USER_PROFILE) :
                        new UserProfile(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_USER_PROFILE));
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new UserProfile(new Throwable(FAILED)).setCallType(CALL_GET_USER_PROFILE));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ResultObject> changeMobileNumber(ChangeMobileNumber changeMobileNumber) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.changeMobileNumber(changeMobileNumber).enqueue(resultObjectCallback(liveData, CALL_CHANGE_MOBILE_NUMBER));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updateMobileNumber(UpdateMobileNumber updateMobileNumber) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateMobileNumber(updateMobileNumber).enqueue(resultObjectCallback(liveData, CALL_UPDATE_MOBILE_NUMBER));
        return liveData;
    }

    @Override
    public LiveData<Profile> getUserProfileDetail() {
        final MutableLiveData<Profile> liveData = new MutableLiveData<>();
        userService.getUserProfileDetail().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                liveData.setValue(response.isSuccessful() ?
                        response.body().setCallType(CALL_GET_USER_PROFILE_DETAIL) :
                        new Profile(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_USER_PROFILE_DETAIL));
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new Profile(new Throwable(FAILED)).setCallType(CALL_GET_USER_PROFILE_DETAIL));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updateUserProfile(ProfileUpdateRequest updateProfileDetails) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateUserProfile(updateProfileDetails).enqueue(resultObjectCallback(liveData, CALL_UPDATE_USER_PROFILE));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updatePassword(updatePasswordDetails).enqueue(resultObjectCallback(liveData, CALL_UPDATE_PASSWORD));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> setPassword(SetPasswordRequest setPasswordDetails) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.setPassword(setPasswordDetails).enqueue(resultObjectCallback(liveData, CALL_SET_PASSWORD));
        return liveData;
    }

    @Override
    public LiveData<NotificationsList> getFollowingNotifications(int page) {
        MutableLiveData<NotificationsList> liveData = new MutableLiveData<>();
        userService.getFollowingNotifications(page).enqueue(notificationListCallback(liveData, CALL_GET_FOLLOWING_NOTIFICATIONS));
        return liveData;
    }

    @Override
    public LiveData<NotificationsList> getRequestNotifications(int page) {
        MutableLiveData<NotificationsList> liveData = new MutableLiveData<>();
        userService.getRequestNotifications(page).enqueue(notificationListCallback(liveData, CALL_GET_REQUEST_NOTIFICATIONS));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updateCategories(UpdateCategories categories) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateCategories(categories).enqueue(resultObjectCallback(liveData, CALL_UPDATE_CATEGORIES));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> logout(String header) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.logout(header).enqueue(resultObjectCallback(liveData, CALL_LOGOUT));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> removeProfilePic() {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.removeProfilePic().enqueue(resultObjectCallback(liveData, CALL_REMOVE_PROFILE_PIC));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> reportUser(ReportUser reportuser) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.reportUser(reportuser).enqueue(resultObjectCallback(liveData, CALL_REPORT_USER));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deactivateAccount(DeactivateAccountRequest deactivateAccountRequest) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.deactivateAccount(deactivateAccountRequest).enqueue(resultObjectCallback(liveData, CALL_DEACTIVATE_ACCOUNT));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> resetUnreadNotification(int notificationType) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.resetUnreadNotification(notificationType).enqueue(resultObjectCallback(liveData, CALL_RESET_UNREAD_NOTIFICATION));
        return liveData;
    }
}
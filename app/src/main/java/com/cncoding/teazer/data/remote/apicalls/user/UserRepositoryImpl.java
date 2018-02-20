package com.cncoding.teazer.data.remote.apicalls.user;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.IntDef;

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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.MainActivity.PRIVATE_ACCOUNT;
import static com.cncoding.teazer.MainActivity.PUBLIC_ACCOUNT;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.notificationListCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.resultObjectCallback;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class UserRepositoryImpl implements UserRepository {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PUBLIC_ACCOUNT, PRIVATE_ACCOUNT})
    public @interface AccountType {}
    
    private UserService userService;

    public UserRepositoryImpl(String token) {
        userService = getRetrofitWithAuthToken(token).create(UserService.class);
    }

    @Override
    public LiveData<ResultObject> updateUserProfileMedia(MultipartBody.Part media) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateUserProfileMedia(media).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> resetFcmToken(String header, String token) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.resetFcmToken(header, token).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> setAccountVisibility(@AccountType int accountType) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.setAccountVisibility(accountType).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<UserProfile> getUserProfile() {
        final MutableLiveData<UserProfile> liveData = new MutableLiveData<>();
        userService.getUserProfile().enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new UserProfile(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new UserProfile(new Throwable(FAILED)));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ResultObject> changeMobileNumber(ChangeMobileNumber changeMobileNumber) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.changeMobileNumber(changeMobileNumber).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updateMobileNumber(UpdateMobileNumber updateMobileNumber) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateMobileNumber(updateMobileNumber).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<Profile> getUserProfileDetail() {
        final MutableLiveData<Profile> liveData = new MutableLiveData<>();
        userService.getUserProfileDetail().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                liveData.setValue(response.isSuccessful() ? response.body() : new Profile(new Throwable(NOT_SUCCESSFUL)));
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                t.printStackTrace();
                liveData.setValue(new Profile(new Throwable(FAILED)));
            }
        });
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updateUserProfile(ProfileUpdateRequest updateProfileDetails) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateUserProfile(updateProfileDetails).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updatePassword(updatePasswordDetails).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> setPassword(SetPasswordRequest setPasswordDetails) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.setPassword(setPasswordDetails).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<NotificationsList> getFollowingNotifications(int page) {
        MutableLiveData<NotificationsList> liveData = new MutableLiveData<>();
        userService.getFollowingNotifications(page).enqueue(notificationListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<NotificationsList> getRequestNotifications(int page) {
        MutableLiveData<NotificationsList> liveData = new MutableLiveData<>();
        userService.getRequestNotifications(page).enqueue(notificationListCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> updateCategories(UpdateCategories categories) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.updateCategories(categories).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> logout(String header) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.logout(header).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> removeProfilePic() {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.removeProfilePic().enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> reportUser(ReportUser reportuser) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.reportUser(reportuser).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> deactivateAccount(DeactivateAccountRequest deactivateAccountRequest) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.deactivateAccount(deactivateAccountRequest).enqueue(resultObjectCallback(liveData));
        return liveData;
    }

    @Override
    public LiveData<ResultObject> resetUnreadNotification(int notificationType) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        userService.resetUnreadNotification(notificationType).enqueue(resultObjectCallback(liveData));
        return liveData;
    }
}
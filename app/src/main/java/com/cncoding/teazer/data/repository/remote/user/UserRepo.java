package com.cncoding.teazer.data.repository.remote.user;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.api.ResultObject;
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

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class UserRepo implements UserRepository {
    @Override
    public LiveData<ResultObject> updateUserProfileMedia(MultipartBody.Part media) {
        return null;
    }

    @Override
    public LiveData<ResultObject> resetFcmToken(String header, String token) {
        return null;
    }

    @Override
    public LiveData<ResultObject> setAccountVisibility(int accountType) {
        return null;
    }

    @Override
    public LiveData<UserProfile> getUserProfile() {
        return null;
    }

    @Override
    public LiveData<ResultObject> changeMobileNumber(ChangeMobileNumber changeMobileNumber) {
        return null;
    }

    @Override
    public LiveData<ResultObject> updateMobileNumber(UpdateMobileNumber updateMobileNumber) {
        return null;
    }

    @Override
    public LiveData<Profile> getUserProfileDetail() {
        return null;
    }

    @Override
    public LiveData<ResultObject> updateUserProfile(ProfileUpdateRequest updateProfileDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails) {
        return null;
    }

    @Override
    public LiveData<ResultObject> setPassword(SetPasswordRequest setPasswordDetails) {
        return null;
    }

    @Override
    public LiveData<NotificationsList> getFollowingNotifications(int page) {
        return null;
    }

    @Override
    public LiveData<NotificationsList> getRequestNotifications(int page) {
        return null;
    }

    @Override
    public LiveData<ResultObject> updateCategories(UpdateCategories categories) {
        return null;
    }

    @Override
    public LiveData<ResultObject> logout(String header) {
        return null;
    }

    @Override
    public LiveData<ResultObject> removeProfilePic() {
        return null;
    }

    @Override
    public LiveData<ResultObject> reportUser(ReportUser reportuser) {
        return null;
    }

    @Override
    public LiveData<ResultObject> deactivateAccount(DeactivateAccountRequest deactivateAccountRequest) {
        return null;
    }

    @Override
    public LiveData<ResultObject> resetUnreadNotification(int notificationType) {
        return null;
    }
}

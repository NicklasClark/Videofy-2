package com.cncoding.teazer.data.remote.api.calls.user;

import android.arch.lifecycle.LiveData;

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

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface UserRepository {

    LiveData<ResultObject> updateUserProfileMedia(MultipartBody.Part media);

    LiveData<ResultObject> resetFcmToken(String header, String token);

    LiveData<ResultObject> setAccountVisibility(int accountType);

    LiveData<UserProfile> getUserProfile();

    LiveData<ResultObject> changeMobileNumber(ChangeMobileNumber changeMobileNumber);

    LiveData<ResultObject> updateMobileNumber(UpdateMobileNumber updateMobileNumber);

    LiveData<Profile> getUserProfileDetail();

    LiveData<ResultObject> updateUserProfile(ProfileUpdateRequest updateProfileDetails);

    LiveData<ResultObject> updatePassword(UpdatePasswordRequest updatePasswordDetails);

    LiveData<ResultObject> setPassword(SetPasswordRequest setPasswordDetails);

    LiveData<NotificationsList> getFollowingNotifications(int page);

    LiveData<NotificationsList> getRequestNotifications(int page);

    LiveData<ResultObject> updateCategories(UpdateCategories categories);

    LiveData<ResultObject> logout(String header);

    LiveData<ResultObject> removeProfilePic();

    LiveData<ResultObject> reportUser(ReportUser reportuser);

    LiveData<ResultObject> deactivateAccount(DeactivateAccountRequest deactivateAccountRequest);

    LiveData<ResultObject> resetUnreadNotification(int notificationType);
}

package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.data.model.updatemobilenumber.UpdateMobileNumber;
import com.cncoding.teazer.data.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.data.model.user.ReportUser;
import com.cncoding.teazer.data.model.user.SetPasswordRequest;
import com.cncoding.teazer.data.model.user.UpdateCategories;
import com.cncoding.teazer.data.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.user.UserRepository;
import com.cncoding.teazer.data.remote.apicalls.user.UserRepositoryImpl;
import com.cncoding.teazer.utilities.common.Annotations.AccountType;
import com.cncoding.teazer.utilities.common.Annotations.LikeDislike;

import okhttp3.MultipartBody;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public class UserViewModel extends ViewModel {

    private MediatorLiveData<ResultObject> resultObjectLiveData;
    private MediatorLiveData<UserProfile> userProfileLiveData;
    private MediatorLiveData<NotificationsList> notificationsLiveData;
    private MediatorLiveData<LikedUserList> likedUserListLiveData;
    private UserRepository userRepository;

    public UserViewModel(MediatorLiveData<ResultObject> resultObjectLiveData, MediatorLiveData<UserProfile> userProfileLiveData,
                         MediatorLiveData<NotificationsList> notificationsLiveData,
                         MediatorLiveData<LikedUserList> likedUserListLiveData, UserRepository userRepository) {
        this.resultObjectLiveData = resultObjectLiveData;
        this.userProfileLiveData = userProfileLiveData;
        this.notificationsLiveData = notificationsLiveData;
        this.likedUserListLiveData = likedUserListLiveData;
        this.userRepository = userRepository;
    }

    public UserViewModel(String token) {
        this.resultObjectLiveData = new MediatorLiveData<>();
        this.userProfileLiveData = new MediatorLiveData<>();
        this.notificationsLiveData = new MediatorLiveData<>();
        this.likedUserListLiveData = new MediatorLiveData<>();
        this.userRepository = new UserRepositoryImpl(token);
    }

    //region Getters
    public MediatorLiveData<ResultObject> getResultObjectLiveData() {
        return resultObjectLiveData;
    }

    public MediatorLiveData<UserProfile> getUserProfileLiveData() {
        return userProfileLiveData;
    }

    public MediatorLiveData<NotificationsList> getNotificationsLiveData() {
        return notificationsLiveData;
    }

    public MediatorLiveData<LikedUserList> getLikedUserListLiveData() {
        return likedUserListLiveData;
    }
    //endregion

    //region API Calls
    public void updateUserProfileMedia(MultipartBody.Part media){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateUserProfileMedia(media),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetFcmToken(String header, String token){
        try {
            resultObjectLiveData.addSource(
                    userRepository.resetFcmToken(header, token),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAccountVisibility(@AccountType int accountType){
        try {
            resultObjectLiveData.addSource(
                    userRepository.setAccountVisibility(accountType),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserProfile(){
        try {
            userProfileLiveData.addSource(
                    userRepository.getUserProfile(),
                    new Observer<UserProfile>() {
                        @Override
                        public void onChanged(@Nullable UserProfile userProfile) {
                            userProfileLiveData.setValue(userProfile);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeMobileNumber(ChangeMobileNumber changeMobileNumber){
        try {
            resultObjectLiveData.addSource(
                    userRepository.changeMobileNumber(changeMobileNumber),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMobileNumber(UpdateMobileNumber updateMobileNumber){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateMobileNumber(updateMobileNumber),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserProfile(ProfileUpdateRequest updateProfileDetails){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateUserProfile(updateProfileDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(UpdatePasswordRequest updatePasswordDetails){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updatePassword(updatePasswordDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPassword(SetPasswordRequest setPasswordDetails){
        try {
            resultObjectLiveData.addSource(
                    userRepository.setPassword(setPasswordDetails),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getFollowingNotifications(int page){
        try {
            notificationsLiveData.addSource(
                    userRepository.getFollowingNotifications(page),
                    new Observer<NotificationsList>() {
                        @Override
                        public void onChanged(@Nullable NotificationsList notificationsList) {
                            notificationsLiveData.setValue(notificationsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRequestNotifications(int page){
        try {
            notificationsLiveData.addSource(
                    userRepository.getRequestNotifications(page),
                    new Observer<NotificationsList>() {
                        @Override
                        public void onChanged(@Nullable NotificationsList notificationsList) {
                            notificationsLiveData.setValue(notificationsList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCategories(UpdateCategories categories){
        try {
            resultObjectLiveData.addSource(
                    userRepository.updateCategories(categories),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(String header){
        try {
            resultObjectLiveData.addSource(
                    userRepository.logout(header),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeProfilePic(){
        try {
            resultObjectLiveData.addSource(
                    userRepository.removeProfilePic(),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reportUser(ReportUser reportuser){
        try {
            resultObjectLiveData.addSource(
                    userRepository.reportUser(reportuser),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deactivateAccount(DeactivateAccountRequest deactivateAccountRequest){
        try {
            resultObjectLiveData.addSource(
                    userRepository.deactivateAccount(deactivateAccountRequest),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetUnreadNotification(int notificationType){
        try {
            resultObjectLiveData.addSource(
                    userRepository.resetUnreadNotification(notificationType),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void likeDislikeProfile(int userId, @LikeDislike int status){
        try {
            resultObjectLiveData.addSource(
                    userRepository.likeDislikeProfile(userId, status),
                    new Observer<ResultObject>() {
                        @Override
                        public void onChanged(@Nullable ResultObject resultObject) {
                            resultObjectLiveData.setValue(resultObject);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLikedUsersList(int userId){
        try {
            likedUserListLiveData.addSource(
                    userRepository.getLikedUsersList(userId),
                    new Observer<LikedUserList>() {
                        @Override
                        public void onChanged(@Nullable LikedUserList likedUserList) {
                            likedUserListLiveData.setValue(likedUserList);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}
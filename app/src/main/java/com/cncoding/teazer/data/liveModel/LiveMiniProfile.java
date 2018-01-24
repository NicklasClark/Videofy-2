package com.cncoding.teazer.data.liveModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.cncoding.teazer.data.liveModel.MutableDataProvider.getMutableData;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class LiveMiniProfile extends ViewModel {

    @SerializedName("user_id") @Expose private MutableLiveData<Integer> userId;
    @SerializedName("user_name") @Expose private MutableLiveData<String> userName;
    @SerializedName("first_name") @Expose private MutableLiveData<String> firstName;
    @SerializedName("last_name") @Expose private MutableLiveData<String> lastName;
    @SerializedName("account_type") @Expose private MutableLiveData<Integer> accountType;
    @SerializedName("request_id") @Expose private MutableLiveData<Integer> requestId;
    @SerializedName("request_recieved") @Expose private MutableLiveData<Boolean> requestReceived;
    @SerializedName("following") @Expose private MutableLiveData<Boolean> following;
    @SerializedName("follower") @Expose private MutableLiveData<Boolean> follower;
    @SerializedName("request_sent") @Expose private MutableLiveData<Boolean> requestSent;
    @SerializedName("has_profile_media") @Expose private MutableLiveData<Boolean> hasProfileMedia;
    @SerializedName("you_blocked") @Expose private MutableLiveData<Boolean> youBlocked;
    @SerializedName("profile_media") @Expose private MutableLiveData<LiveProfileMedia> profileMedia;

    public MutableLiveData<Integer> getUserId() {
        return (MutableLiveData<Integer>) getMutableData(userId);
    }

    public MutableLiveData<String> getUserName() {
        return (MutableLiveData<String>) getMutableData(userName);
    }

    public MutableLiveData<String> getFirstName() {
        return (MutableLiveData<String>) getMutableData(firstName);
    }

    public MutableLiveData<String> getLastName() {
        return (MutableLiveData<String>) getMutableData(lastName);
    }

    public MutableLiveData<Integer> getAccountType() {
        return (MutableLiveData<Integer>) getMutableData(accountType);
    }

    public MutableLiveData<Boolean> isFollowing() {
        return (MutableLiveData<Boolean>) getMutableData(following);
    }

    public MutableLiveData<Boolean> isFollower() {
        return (MutableLiveData<Boolean>) getMutableData(follower);
    }

    public MutableLiveData<Boolean> isRequestSent() {
        return (MutableLiveData<Boolean>) getMutableData(requestSent);
    }

    public MutableLiveData<Boolean> hasProfileMedia() {
        return (MutableLiveData<Boolean>) getMutableData(hasProfileMedia);
    }

    public MutableLiveData<LiveProfileMedia> getProfileMedia() {
        return (MutableLiveData<LiveProfileMedia>) getMutableData(profileMedia);
    }

    public MutableLiveData<Boolean> getRequestReceived() {
        return (MutableLiveData<Boolean>) getMutableData(requestReceived);
    }

    public MutableLiveData<Boolean> getYouBlocked() {
        return (MutableLiveData<Boolean>) getMutableData(youBlocked);
    }
}
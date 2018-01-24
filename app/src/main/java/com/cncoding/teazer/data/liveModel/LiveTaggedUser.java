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
public class LiveTaggedUser extends ViewModel {

    @SerializedName("tag_id") @Expose private MutableLiveData<Integer> tag_id;
    @SerializedName("user_id") @Expose private MutableLiveData<Integer> user_id;
    @SerializedName("user_name") @Expose private MutableLiveData<String> user_name;
    @SerializedName("first_name") @Expose private MutableLiveData<String> first_name;
    @SerializedName("last_name") @Expose private MutableLiveData<String> last_name;
    @SerializedName("my_self") @Expose private MutableLiveData<Boolean> my_self;
    @SerializedName("is_blocked_you") @Expose private MutableLiveData<Boolean> is_blocked_you;
    @SerializedName("has_profile_media") @Expose private MutableLiveData<Boolean> has_profile_media;
    @SerializedName("profile_media") @Expose private MutableLiveData<LiveProfileMedia> profile_media;

    public MutableLiveData<Integer> getTagId() {
        return (MutableLiveData<Integer>) getMutableData(tag_id);
    }

    public MutableLiveData<Integer> getUserId() {
        return (MutableLiveData<Integer>) getMutableData(user_id);
    }

    public MutableLiveData<String> getUserName() {
        return (MutableLiveData<String>) getMutableData(user_id);
    }

    public MutableLiveData<String> getFirstName() {
        return (MutableLiveData<String>) getMutableData(user_id);
    }

    public MutableLiveData<String> getLastName() {
        return (MutableLiveData<String>) getMutableData(user_id);
    }

    public MutableLiveData<Boolean> isMySelf() {
        return (MutableLiveData<Boolean>) getMutableData(user_id);
    }

    public MutableLiveData<Boolean> isBlockedYou() {
        return (MutableLiveData<Boolean>) getMutableData(user_id);
    }

    public MutableLiveData<Boolean> hasProfileMedia() {
        return (MutableLiveData<Boolean>) getMutableData(user_id);
    }

    public MutableLiveData<LiveProfileMedia> getProfileMedia() {
        return (MutableLiveData<LiveProfileMedia>) getMutableData(profile_media);
    }
}
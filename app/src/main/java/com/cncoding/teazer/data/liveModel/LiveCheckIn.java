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
public class LiveCheckIn extends ViewModel {

    @SerializedName("checkin_id") @Expose private MutableLiveData<Integer> checkin_id;
    @SerializedName("latitude") @Expose private MutableLiveData<Double> latitude;
    @SerializedName("longitude") @Expose private MutableLiveData<Double> longitude;
    @SerializedName("location") @Expose private  MutableLiveData<String> location;

    public MutableLiveData<Integer> getCheckinId() {
        return (MutableLiveData<Integer>) getMutableData(checkin_id);
    }

    public MutableLiveData<Double> getLatitude() {
        return (MutableLiveData<Double>) getMutableData(latitude);
    }

    public MutableLiveData<Double> getLongitude() {
        return (MutableLiveData<Double>) getMutableData(longitude);
    }

    public MutableLiveData<String> getLocation() {
        return (MutableLiveData<String>) getMutableData(location);
    }
}
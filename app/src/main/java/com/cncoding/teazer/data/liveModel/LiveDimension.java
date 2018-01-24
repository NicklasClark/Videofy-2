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
public class LiveDimension extends ViewModel {

    @SerializedName("media_id") @Expose private MutableLiveData<Integer> height;
    @SerializedName("media_url") @Expose private MutableLiveData<Integer> width;

    public LiveDimension(int height, int width) {
        this.height.setValue(height);
        this.width.setValue(width);
    }

    public MutableLiveData<Integer> getHeight() {
        return (MutableLiveData<Integer>) getMutableData(height);
    }

    public MutableLiveData<Integer> getWidth() {
        return (MutableLiveData<Integer>) getMutableData(width);
    }
}
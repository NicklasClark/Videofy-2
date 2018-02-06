package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.cncoding.teazer.data.viewmodel.MutableDataProvider.getMutableData;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class LiveProfileMedia extends ViewModel {

    @SerializedName("picture_id") @Expose private MutableLiveData<Integer> picture_id;
    @SerializedName("media_url") @Expose private MutableLiveData<String> media_url;
    @SerializedName("thumb_url") @Expose public MutableLiveData<String> thumb_url;
    @SerializedName("duration") @Expose private MutableLiveData<String> duration;
    @SerializedName("media_dimension") @Expose private MutableLiveData<LiveDimension> media_dimension;
    @SerializedName("is_image") @Expose private MutableLiveData<Boolean> is_image;

    public MutableLiveData<Integer> getPictureId() {
        return (MutableLiveData<Integer>) getMutableData(picture_id);
    }

    public MutableLiveData<String> getMediaUrl() {
        return (MutableLiveData<String>) getMutableData(picture_id);
    }

    public MutableLiveData<String> getThumbUrl() {
        return (MutableLiveData<String>) getMutableData(picture_id);
    }

    public MutableLiveData<String> getDuration() {
        return (MutableLiveData<String>) getMutableData(picture_id);
    }

    public MutableLiveData<LiveDimension> getDimension() {
        return (MutableLiveData<LiveDimension>) getMutableData(media_dimension);
    }

    public MutableLiveData<Boolean> isImage() {
        return (MutableLiveData<Boolean>) getMutableData(picture_id);
    }
}
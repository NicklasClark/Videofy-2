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
public class LiveMedias extends ViewModel {

    @SerializedName("media_id") @Expose private MutableLiveData<Integer> media_id;
    @SerializedName("media_url") @Expose private MutableLiveData<String> media_url;
    @SerializedName("thumb_url") @Expose private MutableLiveData<String> thumb_url;
    @SerializedName("duration") @Expose private MutableLiveData<String> duration;
    @SerializedName("media_dimension") @Expose private MutableLiveData<LiveDimension> media_dimension;
    @SerializedName("is_image") @Expose private MutableLiveData<Boolean> is_image;
    @SerializedName("views") @Expose public MutableLiveData<Integer> views;
    @SerializedName("created_at") @Expose private MutableLiveData<String> created_at;

    public MutableLiveData<Integer> getMediaId() {
        return (MutableLiveData<Integer>) getMutableData(media_id);
    }

    public MutableLiveData<String> getMediaUrl() {
        return (MutableLiveData<String>) getMutableData(media_url);
    }

    public MutableLiveData<String> getThumbUrl() {
        return (MutableLiveData<String>) getMutableData(thumb_url);
    }

    public MutableLiveData<String> getDuration() {
        return (MutableLiveData<String>) getMutableData(duration);
    }

    public MutableLiveData<LiveDimension> getMediaDimension() {
        return (MutableLiveData<LiveDimension>) getMutableData(media_dimension);
    }

    public MutableLiveData<Boolean> isImage() {
        return (MutableLiveData<Boolean>) getMutableData(is_image);
    }

    public MutableLiveData<Integer> getViews() {
        return (MutableLiveData<Integer>) getMutableData(views);
    }

    public MutableLiveData<String> getCreatedAt() {
        return (MutableLiveData<String>) getMutableData(created_at);
    }
}
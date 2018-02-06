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
public class LiveCategory extends ViewModel {

    @SerializedName("category_id") @Expose private MutableLiveData<Integer> category_id;
    @SerializedName("category_name") @Expose private MutableLiveData<String> category_name;
    @SerializedName("color") @Expose private MutableLiveData<String> color;
    @SerializedName("my_color") @Expose private MutableLiveData<String> my_color;

    public MutableLiveData<Integer> getCategoryId() {
        return (MutableLiveData<Integer>) getMutableData(category_id);
    }

    public MutableLiveData<String> getCategoryName() {
        return (MutableLiveData<String>) getMutableData(category_name);
    }

    public MutableLiveData<String> getColor() {
        return (MutableLiveData<String>) getMutableData(color);
    }

    public MutableLiveData<String> getMyColor() {
        return (MutableLiveData<String>) getMutableData(my_color);
    }
}
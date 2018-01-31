package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

/**
 *
 * Created by Prem $ on 1/12/2018.
 */

class MutableDataProvider {
    @NonNull
    static MutableLiveData<?> getMutableData(MutableLiveData<?> data) {
        if (data == null)
            data = new MutableLiveData<>();
        return data;
    }
}
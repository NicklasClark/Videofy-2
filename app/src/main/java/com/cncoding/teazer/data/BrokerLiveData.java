package com.cncoding.teazer.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.viewmodel.AuthViewModel;
import com.cncoding.teazer.data.viewmodel.BaseViewModel;

/**
 * Created by Prem$ on 3/10/2018.
 *
 * A custom {@link MediatorLiveData} class which is useful for setting up observers efficiently in
 * {@link AuthViewModel} and {@link BaseViewModel}
 *
 * @param <T> The type of data to be held by this instance
 */

public class BrokerLiveData<T> extends MediatorLiveData<T> {


    /**
     * <p> Use this method to observe to the {@link LiveData} emitted by the API call.</p>
     * <p> This method adds the {@link LiveData} returned by the API call
     *     and upon completion, handles the changes and disposes off the source.</p>
     * @param liveData the API call returning the suitable {@link LiveData}.
     */
    public void observeOn(final LiveData<T> liveData) {
        try {
            this.addSource(
                    liveData,
                    new Observer<T>() {
                        @Override
                        public void onChanged(@Nullable T t) {
                            BrokerLiveData.this.removeSource(liveData);
                            BrokerLiveData.this.setValue(t);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p> This method is to be used when making API calls from RecyclerView adapters, so that the suitable positions can be updated.</p>
     * <p> Use only {@link ResultObject} as the {@link BrokerLiveData} parameter else it will have no effect.</p>
     * */
    public void observeOn(final LiveData<T> liveData, final int position) {
        try {
            this.addSource(
                    liveData,
                    new Observer<T>() {
                        @SuppressWarnings("unchecked")
                        @Override
                        public void onChanged(@Nullable T t) {
                            BrokerLiveData.this.removeSource(liveData);
                            if (t instanceof ResultObject) {
                                BrokerLiveData.this.setValue((T) ((ResultObject) t).setAdapterPosition(position));
                            }
                            else BrokerLiveData.this.setValue(t);
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
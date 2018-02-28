package com.cncoding.teazer.data.viewmodel.factory;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepository;
import com.cncoding.teazer.data.viewmodel.AuthViewModel;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public class AuthViewModelFactory implements ViewModelProvider.Factory {

    private MediatorLiveData<ResultObject> apiResponseLiveData;
    private AuthenticationRepository authenticationRepository;
    private Observer<ResultObject> resultObjectObserver;

    @Inject public AuthViewModelFactory(MediatorLiveData<ResultObject> apiResponseLiveData, AuthenticationRepository authenticationRepository,
                                        Observer<ResultObject> resultObjectObserver) {
        this.apiResponseLiveData = apiResponseLiveData;
        this.authenticationRepository = authenticationRepository;
        this.resultObjectObserver = resultObjectObserver;
    }

    @SuppressWarnings("unchecked")
    @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class))
            return (T) new AuthViewModel(apiResponseLiveData, authenticationRepository, resultObjectObserver);
        else throw new IllegalArgumentException("ViewModel Not Found");
    }
}

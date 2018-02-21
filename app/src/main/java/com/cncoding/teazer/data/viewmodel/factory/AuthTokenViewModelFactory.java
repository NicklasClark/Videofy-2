package com.cncoding.teazer.data.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.viewmodel.DiscoverViewModel;
import com.cncoding.teazer.data.viewmodel.PostDetailsViewModel;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class AuthTokenViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String token;

    @Inject public AuthTokenViewModelFactory(Application application, String token) {
        this.application = application;
        this.token = token;
    }

    @SuppressWarnings("unchecked")
    @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PostDetailsViewModel.class))
            return (T) new PostDetailsViewModel(application, token);
        else if (modelClass.isAssignableFrom(DiscoverViewModel.class))
            return (T) new DiscoverViewModel(token);
        else throw new IllegalArgumentException("ViewModel Not Found");
    }
}

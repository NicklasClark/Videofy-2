package com.cncoding.teazer.data.viewmodel.factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.viewmodel.DiscoverViewModel;
import com.cncoding.teazer.data.viewmodel.FriendsViewModel;
import com.cncoding.teazer.data.viewmodel.PostViewModel;
import com.cncoding.teazer.data.viewmodel.ReactViewModel;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class AuthTokenViewModelFactory implements ViewModelProvider.Factory {

    private Application application;
    private String token;
    private boolean isAdapter;

    @Inject public AuthTokenViewModelFactory(Application application, String token) {
        this.application = application;
        this.token = token;
    }

    public AuthTokenViewModelFactory(String token) {
        this.token = token;
    }

    public AuthTokenViewModelFactory(String token, boolean isAdapter) {
        this.token = token;
        this.isAdapter = isAdapter;
    }

    @SuppressWarnings("unchecked")
    @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PostViewModel.class))
            return (T) new PostViewModel(application, token);
        else if (modelClass.isAssignableFrom(DiscoverViewModel.class))
            return (T) new DiscoverViewModel(token);
        else if (modelClass.isAssignableFrom(FriendsViewModel.class))
            return (T) new FriendsViewModel(token, isAdapter);
        else if (modelClass.isAssignableFrom(ReactViewModel.class))
            return (T) new ReactViewModel(token);
        else throw new IllegalArgumentException("ViewModel Not Found");
    }
}

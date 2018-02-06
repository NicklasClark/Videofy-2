package com.cncoding.teazer.data.viewmodel.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.viewmodel.PostDetailsViewModel;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class PostDetailsViewModelFactory implements ViewModelProvider.Factory {

    private String token;

    @Inject public PostDetailsViewModelFactory(String token) {
        this.token = token;
    }

    @SuppressWarnings("unchecked")
    @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PostDetailsViewModel.class))
            return (T) new PostDetailsViewModel(token);
        else throw new IllegalArgumentException("ViewModel Not Found");
    }
}

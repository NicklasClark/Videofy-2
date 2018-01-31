package com.cncoding.teazer.data.viewmodel.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.cncoding.teazer.data.repository.local.post.PostDetailsLocalRepo;
import com.cncoding.teazer.data.repository.remote.post.PostsRepo;
import com.cncoding.teazer.data.viewmodel.LivePostList;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 1/31/2018.
 */

public class PostDetailsViewModelFactory implements ViewModelProvider.Factory {

    private PostsRepo apiRepository;
    private PostDetailsLocalRepo localRepository;

    @Inject public PostDetailsViewModelFactory(PostsRepo apiRepository, PostDetailsLocalRepo localRepository) {
        this.apiRepository = apiRepository;
        this.localRepository = localRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LivePostList.class))
            return (T) new LivePostList(apiRepository, localRepository);

        else throw new IllegalArgumentException("ViewModel Not Found");
    }
}

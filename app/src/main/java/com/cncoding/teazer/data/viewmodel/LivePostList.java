package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.repository.local.post.PostDetailsLocalRepo;
import com.cncoding.teazer.data.repository.remote.post.PostsRepo;

import javax.inject.Inject;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class LivePostList extends ViewModel {

    private MediatorLiveData<PostList> livePostList;
    private PostsRepo apiRepository;
    private PostDetailsLocalRepo localRepository;

    @Inject public LivePostList(PostsRepo apiRepository, PostDetailsLocalRepo localRepository) {
        this.localRepository = localRepository;
        this.apiRepository = apiRepository;
        livePostList = new MediatorLiveData<>();
    }

    @NonNull
    public MediatorLiveData<PostList> getPostList() {
        return livePostList;
    }

    public LiveData<PostList> getLivePosts(int page, String authToken) {
        livePostList.addSource(
                apiRepository.getHomePagePosts(page, authToken),
                new Observer<PostList>() {
                    @Override
                    public void onChanged(@Nullable PostList postList) {
                        LivePostList.this.livePostList.setValue(postList);
                    }
                }
        );
        return livePostList;
    }
}
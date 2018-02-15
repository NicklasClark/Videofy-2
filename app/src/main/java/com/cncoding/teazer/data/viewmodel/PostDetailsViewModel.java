package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.remote.apicalls.post.PostsRepository;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepositoryImpl;
import com.cncoding.teazer.model.post.PostList;

import javax.inject.Inject;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class PostDetailsViewModel extends ViewModel {

    private MediatorLiveData<PostList> livePostDetailsList;
    private PostsRepository apiRepository;

    public PostDetailsViewModel(String token) {
        this.apiRepository = new PostsRepositoryImpl(token);
        livePostDetailsList = new MediatorLiveData<>();
    }

    @Inject public PostDetailsViewModel(MediatorLiveData<PostList> livePostDetailsList, PostsRepository apiRepository) {
        this.livePostDetailsList = livePostDetailsList;
        this.apiRepository = apiRepository;
    }

    @NonNull public MediatorLiveData<PostList> getPostList() {
        return livePostDetailsList;
    }

    public void loadPostList(final int page) {
        try {
            livePostDetailsList.addSource(
                    apiRepository.getHomePagePosts(page),
                    new Observer<PostList>() {
                        @Override
                        public void onChanged(@Nullable PostList postList) {
                            if (postList != null) {
                                livePostDetailsList.setValue(postList);
                            }
                        }
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearData() {
        if (livePostDetailsList.getValue() != null && livePostDetailsList.getValue().getPosts() != null) {
            livePostDetailsList.getValue().getPosts().clear();
        }
    }
}
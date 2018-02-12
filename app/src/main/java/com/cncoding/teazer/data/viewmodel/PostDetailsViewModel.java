package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.api.calls.post.PostsRepository;
import com.cncoding.teazer.data.api.calls.post.PostsRepositoryImpl;
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
//    private PostDetailsLocalRepo localRepository;

    @Inject public PostDetailsViewModel(String token) {
//        this.localRepository = new PostDetailsLocalRepo();
        this.apiRepository = new PostsRepositoryImpl(token);
        livePostDetailsList = new MediatorLiveData<>();
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
        if (livePostDetailsList.getValue() != null) {
            livePostDetailsList.getValue().getPosts().clear();
        }
    }
}
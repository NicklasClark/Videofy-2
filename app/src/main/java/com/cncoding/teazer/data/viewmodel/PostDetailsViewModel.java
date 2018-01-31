package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.repository.local.post.PostDetailsLocalRepo;
import com.cncoding.teazer.data.repository.remote.post.PostsRepo;

import java.util.List;

import javax.inject.Inject;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class PostDetailsViewModel extends ViewModel {

    private MediatorLiveData<List<PostDetails>> livePostDetailsList;
    private PostsRepo apiRepository;
    private PostDetailsLocalRepo localRepository;

    @Inject
    public PostDetailsViewModel(PostsRepo apiRepository, PostDetailsLocalRepo localRepository) {
        this.localRepository = localRepository;
        this.apiRepository = apiRepository;
        livePostDetailsList = new MediatorLiveData<>();
    }

    @NonNull
    public MediatorLiveData<List<PostDetails>> getPostList() {
        return livePostDetailsList;
    }

    public LiveData<List<PostDetails>> getLivePosts(final int page, String authToken) {
        livePostDetailsList.addSource(
                apiRepository.getHomePagePosts(page, authToken),
                new Observer<PostList>() {
                    @Override
                    public void onChanged(@Nullable PostList postList) {
                        if (postList != null) {
                            if (page == 1)
                                PostDetailsViewModel.this.livePostDetailsList.setValue(postList.getPosts());
                            else
                                PostDetailsViewModel.this.livePostDetailsList.getValue().addAll(postList.getPosts());
                        }
                    }
                }
        );
        return livePostDetailsList;
    }
}
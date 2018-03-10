package com.cncoding.teazer.ui.home.discover;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.discover.LandingPostsV2;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;

import java.util.ArrayList;

/**
 *
 * Created by Prem$ on 2/20/2018.
 */

public abstract class BaseDiscoverFragment extends BaseHomeFragment {

    /**
     * Setting observers.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getLandingPosts().observe(this, new Observer<LandingPostsV2>() {
            @Override
            public void onChanged(@Nullable LandingPostsV2 landingPostsV2) {
                handleLiveDataChange(landingPostsV2);
            }
        });

        viewModel.getPostListLiveData().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                handleLiveDataChange(postList);
            }
        });

        viewModel.getMostPopularPosts().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                handleLiveDataChange(postList);
            }
        });

        viewModel.getUsersList().observe(this, new Observer<UsersList>() {
            @Override
            public void onChanged(@Nullable UsersList usersList) {
                handleLiveDataChange(usersList);
            }
        });

        viewModel.getVideosList().observe(this, new Observer<VideosList>() {
            @Override
            public void onChanged(@Nullable VideosList videosList) {
                handleLiveDataChange(videosList);
            }
        });
    }

    protected ArrayList<Category> getUserInterests() {
        try {
            //noinspection ConstantConditions
            return viewModel.getLandingPosts().getValue().getUserInterests();
        } catch (Exception e) {
            return null;
        }
    }

    private void handleLiveDataChange(BaseModel baseModel) {
        if (baseModel != null) {
            if (baseModel.getError() != null) handleError(baseModel);
            else handleResponse(baseModel);
        }
    }

//    private void notifyNoInternetConnection() {
//        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
//    }

    protected abstract void handleResponse(BaseModel resultObject);

    protected abstract void handleError(BaseModel baseModel);
}
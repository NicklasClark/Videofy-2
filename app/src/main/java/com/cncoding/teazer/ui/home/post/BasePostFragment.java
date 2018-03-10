package com.cncoding.teazer.ui.home.post;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;

import static com.cncoding.teazer.utilities.common.Annotations.NO_CALL;

/**
 *
 * Created by Prem$ on 2/26/2018.
 */

public abstract class BasePostFragment extends BaseHomeFragment {

    /**
     * Setting observers.
     */
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.getPostList().observe(this, new Observer<PostList>() {
            @Override
            public void onChanged(@Nullable PostList postList) {
                handleLiveDataChange(postList);
            }
        });

        viewModel.getResultObjectLiveData().observe(this, new Observer<ResultObject>() {
            @Override
            public void onChanged(@Nullable ResultObject resultObject) {
                handleLiveDataChange(resultObject);
            }
        });

        viewModel.getPostDetailsLiveData().observe(this, new Observer<PostDetails>() {
            @Override
            public void onChanged(@Nullable PostDetails postDetails) {
                handleLiveDataChange(postDetails);
            }
        });

        viewModel.getPostUploadResultLiveData().observe(this, new Observer<PostUploadResult>() {
            @Override
            public void onChanged(@Nullable PostUploadResult postUploadResult) {
                handleLiveDataChange(postUploadResult);
            }
        });

        viewModel.getTaggedUsersListLiveData().observe(this, new Observer<TaggedUsersList>() {
            @Override
            public void onChanged(@Nullable TaggedUsersList taggedUsersList) {
                handleLiveDataChange(taggedUsersList);
            }
        });

        viewModel.getReactionsListLiveData().observe(this, new Observer<ReactionsList>() {
            @Override
            public void onChanged(@Nullable ReactionsList reactionsList) {
                handleLiveDataChange(reactionsList);
            }
        });

        viewModel.getReactionResponseLiveData().observe(this, new Observer<ReactionResponse>() {
            @Override
            public void onChanged(@Nullable ReactionResponse reactionResponse) {
                handleLiveDataChange(reactionResponse);
            }
        });
    }

    private void handleLiveDataChange(BaseModel baseModel) {
        if (baseModel != null) {
            if (baseModel.getError() != null) handleError(baseModel);
            else handleResponse(baseModel);
        }
        else handleError(
                new BaseModel()
                        .loadError(new Throwable(getString(R.string.something_went_wrong)))
                        .loadCallType(NO_CALL));
    }

//    private void notifyNoInternetConnection() {
//        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
//    }

    protected abstract void handleResponse(BaseModel resultObject);

    protected abstract void handleError(BaseModel baseModel);
}
package com.cncoding.teazer.data.liveModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.Repo;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.room.databases.TeazerDB;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@SuppressWarnings("unchecked")
public class LivePostList extends ViewModel {

    private MediatorLiveData<ArrayList<PostDetails>> postList;
    private Repo repo;
    private TeazerDB teazerDB;

    public LivePostList(Context context) {
        teazerDB = TeazerDB.getInstance(context);
        repo = new Repo();
        postList = new MediatorLiveData<>();
        postList.setValue(teazerDB.postDetailsDao().getAllPosts());
    }

    @NonNull
    public MediatorLiveData<ArrayList<PostDetails>> getPostList() {
        return postList;
    }

    public LiveData<ArrayList<PostDetails>> getLivePosts(int page, String authToken) {
        postList.addSource(
                repo.getHomePagePosts(page, authToken),
                new Observer<PostList>() {
                    @Override
                    public void onChanged(@Nullable PostList postList) {

                    }
                }
        );
        return postList;
    }
}
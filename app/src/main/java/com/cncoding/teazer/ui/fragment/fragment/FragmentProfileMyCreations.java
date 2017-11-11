package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by MOHD ARIF on 07-11-2017.
 */

public class FragmentProfileMyCreations extends Fragment {

    CircularAppCompatImageView menuitem;
    Context context;
    ArrayList<Pojos.Post.PostDetails>list;
    RecyclerView recyclerView;
    ProfileMyCreationAdapter profileMyCreationAdapter;
    RecyclerView.LayoutManager layoutManager;



    public static FragmentProfileMyCreations newInstance(int page) {
        return new FragmentProfileMyCreations();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=container.getContext();

        View view = inflater.inflate(R.layout.fragment_profile_mycreations, container, false);

        recyclerView=view.findViewById(R.id.recycler_view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getProfileVideos();
    }

    public void getProfileVideos() {
        list=new ArrayList<>();
        ApiCallingService.Posts.getPostedVideos(context,1).enqueue(new Callback<Pojos.Post.PostList>() {
            @Override
            public void onResponse(Call<Pojos.Post.PostList> call, Response<Pojos.Post.PostList> response) {

                if (response.code() == 200) {
                    list = response.body().getPosts();
                    profileMyCreationAdapter=new ProfileMyCreationAdapter(context,list);
                    recyclerView.setAdapter(profileMyCreationAdapter);
                }
            }
            @Override
            public void onFailure(Call<Pojos.Post.PostList> call, Throwable t) {
            }
        });
    }

}

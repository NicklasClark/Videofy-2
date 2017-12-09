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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.utilities.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MOHD ARIF on 07-11-2017.
 */

public class FragmentProfileMyCreations extends Fragment {

    CircularAppCompatImageView menuitem;
    Context context;
    ArrayList<Pojos.Post.PostDetails> list = new ArrayList<>();
    RecyclerView recyclerView;
    ProfileMyCreationAdapter profileMyCreationAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progress_bar;
    int page;
    ProximaNovaRegularTextView alert1;
    private EndlessRecyclerViewScrollListener scrollListener;
    boolean next = false;

    public static FragmentProfileMyCreations newInstance(int page) {
        return new FragmentProfileMyCreations();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        // list=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_profile_mycreations, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        progress_bar = view.findViewById(R.id.progress_bar);
        alert1 = view.findViewById(R.id.alert1);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        page = 1;
        getProfileVideos();


        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page1, int totalItemsCount, RecyclerView view) {

                //loadNextDataFromApi(page);
//                if (next) {
//                    getProfileVideos(page1);
//
//                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);


    }

    public void getProfileVideos() {


        ApiCallingService.Posts.getPostedVideos(context, page).enqueue(new Callback<Pojos.Post.PostList>() {
            @Override
            public void onResponse(Call<Pojos.Post.PostList> call, Response<Pojos.Post.PostList> response) {

                if (response.code() == 200) {
                    if (response.body().getPosts().size() == 0 || response.body().getPosts() == null) {
                        alert1.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        progress_bar.setVisibility(View.GONE);
                    } else {
                        next = response.body().isNextPage();
                        list.addAll(response.body().getPosts());
                        profileMyCreationAdapter = new ProfileMyCreationAdapter(context, list);
                        recyclerView.setAdapter(profileMyCreationAdapter);

                        if (next) {
                            page++;
                            getProfileVideos();
                        }
                        progress_bar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Pojos.Post.PostList> call, Throwable t) {
            }
        });
    }

    public void loadNextDataFromApi(int offset) {


    }

}

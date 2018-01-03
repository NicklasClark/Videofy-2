package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.utilities.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifTextView;
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
    ArrayList<PostDetails> list = new ArrayList<>();
    RecyclerView recyclerView;
    ProfileMyCreationAdapter profileMyCreationAdapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progress_bar;
    ProximaNovaRegularTextView alert1;
    private EndlessRecyclerViewScrollListener scrollListener;
    boolean next = false;
    public static boolean checkIsLiked=false;
    GifTextView loader;


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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_profile_mycreations, container, false);
        loader=view.findViewById(R.id.loader);
        recyclerView = view.findViewById(R.id.recycler_view);
        progress_bar = view.findViewById(R.id.progress_bar);
        alert1 = view.findViewById(R.id.alert1);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        getProfileVideos(1);
        profileMyCreationAdapter = new ProfileMyCreationAdapter(context, list,getParentFragment());
        recyclerView.setAdapter(profileMyCreationAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if(next) {

                    if(page>2)
                    {
                        loader.setVisibility(View.VISIBLE);
                    }
                    getProfileVideos(page);
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    public void getProfileVideos(final int page) {

        ApiCallingService.Posts.getPostedVideos(context, page).enqueue(new Callback<PostList>() {

            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                try {
                    if (response.code() == 200) {
                        if ((response.body().getPosts().size() == 0) && page == 1) {
                            alert1.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            progress_bar.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                        } else
                        {
                            next = response.body().isNextPage();
                            list.addAll(response.body().getPosts());
                            recyclerView.getAdapter().notifyDataSetChanged();

                            profileMyCreationAdapter.notifyItemRangeInserted(profileMyCreationAdapter.getItemCount(), list.size() - 1);
                            progress_bar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    loader.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                loader.setVisibility(View.GONE);

            }
        });
    }

    public void loadNextDataFromApi(int offset) {


    }

}

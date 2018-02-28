package com.cncoding.teazer.ui.home.profile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.profile.adapter.ChangeCoverPhotoAdapter;

import java.util.ArrayList;

/**
 *
 * Created by farazhabib on 14/02/18.
 */

public class FragmentChangeCoverPhoto extends Fragment {

    Context context;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progress_bar;
    ChangeCoverPhotoAdapter changeCoverPhotoAdapter;
    boolean next = false;
    ArrayList<Object> coverPicList;

    public static FragmentChangeCoverPhoto newInstance() {

        return new FragmentChangeCoverPhoto();
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

        View view = inflater.inflate(R.layout.fragment_cahnge_coverphoto, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coverPicList = new ArrayList<>();
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add(R.drawable.profiledp);
        coverPicList.add("hello");


        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
       // changeCoverPhotoAdapter=new ChangeCoverPhotoAdapter(coverPicList,context,FragmentChangeCoverPhoto.this);

        recyclerView.setAdapter(changeCoverPhotoAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (next) {
                    if (page > 2) {
                    }
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }



}

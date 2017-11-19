package com.cncoding.teazer.home.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.home.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends BaseFragment {

    @BindView(R.id.user_search) ProximaNovaRegularAutoCompleteTextView searSearchView;
    @BindView(R.id.most_popular_list) RecyclerView mostPopularList;
    @BindView(R.id.my_interests_list) RecyclerView myInterestsList;
    @BindView(R.id.trending_list) RecyclerView trendingList;
    @BindView(R.id.featured_videos_list) RecyclerView featuredVideosList;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager horizontalLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager horizontalLinearLayoutManager2 = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager verticalLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL);

        mostPopularList.setLayoutManager(horizontalLinearLayoutManager);
        mostPopularList.setAdapter(new MostPopularListAdapter());

        myInterestsList.setLayoutManager(verticalLinearLayoutManager);
        myInterestsList.setAdapter(new MyInterestsListAdapter(getContext()));

        trendingList.setLayoutManager(horizontalLinearLayoutManager2);
        trendingList.setAdapter(new TrendingListAdapter());

        featuredVideosList.setLayoutManager(staggeredGridLayoutManager);
        featuredVideosList.setAdapter(new FeaturedVideosListAdapter());

        mostPopularList.setNestedScrollingEnabled(false);
        myInterestsList.setNestedScrollingEnabled(false);
        trendingList.setNestedScrollingEnabled(false);
        featuredVideosList.setNestedScrollingEnabled(false);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

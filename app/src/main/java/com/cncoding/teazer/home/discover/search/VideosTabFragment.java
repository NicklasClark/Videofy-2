package com.cncoding.teazer.home.discover.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos.Discover.Videos;
import com.cncoding.teazer.utilities.Pojos.Discover.VideosList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.home.discover.search.DiscoverSearchFragment.SEARCH_TERM;

/**
 * A fragment representing a list of Items.
 */
public class VideosTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_posts) ProximaNovaBoldTextView noPosts;

//    private OnListFragmentInteractionListener mListener;\
    private Call<VideosList> videosListCall;
    private Callback<VideosList> videosListCallback;
    private ArrayList<Videos> videosList;
    private DiscoverSearchAdapter adapter;
    private String searchTerm;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideosTabFragment() {
    }

    public static VideosTabFragment newInstance(String searchTerm) {
        VideosTabFragment fragment = new VideosTabFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_TERM, searchTerm);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchTerm = getArguments().getString(SEARCH_TERM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_interests_tab, container, false);
        ButterKnife.bind(this, rootView);
        videosList = new ArrayList<>();

        adapter = new DiscoverSearchAdapter(getContext(), true, null, videosList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getVideos(page, searchTerm);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getVideos(1, searchTerm);
            }
        });

        if (videosListCallback == null)
            videosListCallback = new Callback<VideosList>() {
                @Override
                public void onResponse(Call<VideosList> call, Response<VideosList> response) {
                    if (response.code() == 200) {
                        is_next_page = response.body().isNextPage();
                        if (response.body().getVideos().size() > 0) {
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            noPosts.setVisibility(View.GONE);
                            videosList.addAll(response.body().getVideos());
                            recyclerView.getRecycledViewPool().clear();
                            adapter.notifyDataSetChanged();
                        } else {
                            swipeRefreshLayout.setVisibility(View.GONE);
                            noPosts.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast.makeText(getContext(), response.code() + " : " + response.message(),
                                Toast.LENGTH_SHORT).show();
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<VideosList> call, Throwable t) {
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            };

        if (videosList != null && videosList.isEmpty())
            getVideos(1, searchTerm);
//        else recyclerView.getAdapter().notifyDataSetChanged();
        return rootView;
    }

    private void getVideos(int page, String searchTerm) {
        if (page == 1)
            videosList.clear();

        videosListCall = ApiCallingService.Discover.getVideosWithSearchTerm(page, searchTerm, getContext());
        
        if (!videosListCall.isExecuted())
            videosListCall.enqueue(videosListCallback);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (videosListCall != null && videosListCall.isExecuted())
            videosListCall.cancel();
        adapter = null;
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnListFragmentInteractionListener {
//        void onListFragmentInteraction(Pojos.User.Notification item);
//    }
}

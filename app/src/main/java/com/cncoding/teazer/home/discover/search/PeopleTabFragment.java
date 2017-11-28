package com.cncoding.teazer.home.discover.search;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos.Friends.UsersList;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 */
public class PeopleTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_notifications) ProximaNovaBoldTextView noNotifications;

//    private OnListFragmentInteractionListener mListener;
    private boolean isSearchTerm;
    private ArrayList<MiniProfile> usersList;
    private DiscoverSearchAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PeopleTabFragment() {
    }

    public static PeopleTabFragment newInstance() {
        return new PeopleTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications_tab, container, false);
        ButterKnife.bind(this, rootView);
        usersList = new ArrayList<>();

        adapter = new DiscoverSearchAdapter(getContext(), false, usersList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    new GetUsersListToFollow(PeopleTabFragment.this).execute(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                new GetUsersListToFollow(PeopleTabFragment.this).execute(1);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (usersList != null && usersList.isEmpty())
            new GetUsersListToFollow(this).execute(1);
    }

    private static class GetUsersListToFollow extends AsyncTask<Integer, Void, Void> {

        private WeakReference<PeopleTabFragment> reference;

        GetUsersListToFollow(PeopleTabFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers[0] == 1)
                reference.get().usersList.clear();

            ApiCallingService.Discover.getUsersListToFollow(integers[0], reference.get().getContext())
                    .enqueue(new Callback<UsersList>() {
                        @Override
                        public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                            if (response.code() == 200) {
                                UsersList usersList = response.body();
                                reference.get().is_next_page = usersList.isNextPage();
                                if (usersList.getUsers().size() > 0) {
                                    reference.get().swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    reference.get().noNotifications.setVisibility(View.GONE);
                                    reference.get().usersList.addAll(usersList.getUsers());
                                    reference.get().recyclerView.getRecycledViewPool().clear();
                                    reference.get().adapter.notifyDataSetChanged();
                                } else {
                                    reference.get().swipeRefreshLayout.setVisibility(View.GONE);
                                    reference.get().noNotifications.setVisibility(View.VISIBLE);
                                }
                            } else {
                                reference.get().noNotifications.setVisibility(View.VISIBLE);
                                reference.get().noNotifications.setText(R.string.error_fetching_posts);
                                reference.get().noNotifications.setCompoundDrawablesWithIntrinsicBounds(
                                        0, R.drawable.ic_no_data_placeholder, 0, 0);
                                Log.e("getUsersListToFollow", response.code() + "_" + response.message());
                            }
                            reference.get().swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(Call<UsersList> call, Throwable t) {
                            Log.e("getUsersListToFollow", t.getMessage() != null ? t.getMessage() : "FAILED!!!");
                            reference.get().swipeRefreshLayout.setRefreshing(false);
                        }
                    });
            return null;
        }
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
//        void onListFragmentInteraction(Notification item);
//    }
}
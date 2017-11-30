package com.cncoding.teazer.home.discover.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
public class PeopleTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_notifications) ProximaNovaBoldTextView noNotifications;

//    private OnListFragmentInteractionListener mListener;
    private ArrayList<MiniProfile> usersList;
    private DiscoverSearchAdapter adapter;
    private Call<UsersList> usersListCall;
    private Callback<UsersList> usersListCallback;
    private String searchTerm;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PeopleTabFragment() {
    }

    public static PeopleTabFragment newInstance(String searchTerm) {
        PeopleTabFragment fragment = new PeopleTabFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications_tab, container, false);
        ButterKnife.bind(this, rootView);
        usersList = new ArrayList<>();

        adapter = new DiscoverSearchAdapter(getContext(), false, usersList, null);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getUsersListWithSearchTerm(page, searchTerm);
//                    new GetUsersListToFollow(PeopleTabFragment.this).execute(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getUsersListWithSearchTerm(1, searchTerm);
//                new GetUsersListToFollow(PeopleTabFragment.this).execute(1);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usersListCallback = new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                if (response.code() == 200) {
                    UsersList users = response.body();
                    is_next_page = users.isNextPage();
                    if (users.getUsers().size() > 0) {
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                        noNotifications.setVisibility(View.GONE);
                        usersList.addAll(users.getUsers());
                        recyclerView.getRecycledViewPool().clear();
                        adapter.notifyDataSetChanged();
                    } else {
                        swipeRefreshLayout.setVisibility(View.GONE);
                        noNotifications.setVisibility(View.VISIBLE);
                    }
                } else {
                    noNotifications.setVisibility(View.VISIBLE);
                    noNotifications.setText(R.string.error_fetching_posts);
                    noNotifications.setCompoundDrawablesWithIntrinsicBounds(
                            0, R.drawable.ic_no_data_placeholder, 0, 0);
                    Log.e("getUsersListToFollow", response.code() + "_" + response.message());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                Log.e("getUsersListToFollow", t.getMessage() != null ? t.getMessage() : "FAILED!!!");
                swipeRefreshLayout.setRefreshing(false);
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        if (usersList != null && usersList.isEmpty())
            getUsersListWithSearchTerm(1, searchTerm);
//            new GetUsersListToFollow(this).execute(1);
    }

//    private static class GetUsersListToFollow extends AsyncTask<Integer, Void, Void> {
//
//        private WeakReference<PeopleTabFragment> reference;
//
//        GetUsersListToFollow(PeopleTabFragment context) {
//            reference = new WeakReference<>(context);
//        }
//
//        @Override
//        protected Void doInBackground(Integer... integers) {
//            if (integers[0] == 1)
//                reference.get().usersList.clear();
//
//            ApiCallingService.Discover.getUsersListToFollow(integers[0], reference.get().getContext())
//                    .enqueue(reference.get().usersListCallback);
//            return null;
//        }
//    }

    private void getUsersListWithSearchTerm(int page, String searchTerm) {
        if (page == 1)
            usersList.clear();

        usersListCall = ApiCallingService.Discover.getUsersListToFollowWithSearchTerm(page, searchTerm, getContext());

        if (!usersListCall.isExecuted())
            usersListCall.enqueue(usersListCallback);
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
        if (usersListCall != null && usersListCall.isExecuted())
            usersListCall.cancel();
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
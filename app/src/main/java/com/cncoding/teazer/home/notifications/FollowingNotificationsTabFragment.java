package com.cncoding.teazer.home.notifications;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.User.NotificationsList;

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
public class FollowingNotificationsTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_notifications) ProximaNovaBoldTextView noNotifications;

//    private OnListFragmentInteractionListener mListener;
    private NotificationsList notificationsList;
    private NotificationsAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FollowingNotificationsTabFragment() {
    }

    public static FollowingNotificationsTabFragment newInstance() {
        return new FollowingNotificationsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications_tab, container, false);
        ButterKnife.bind(this, rootView);
        notificationsList = new NotificationsList(new ArrayList<Pojos.User.Notification>(), 0, false);

        adapter = new NotificationsAdapter(getContext(), true, notificationsList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    new GetFollowingNotifications(FollowingNotificationsTabFragment.this).execute(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                new GetFollowingNotifications(FollowingNotificationsTabFragment.this).execute(1);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (notificationsList.getNotifications() != null && notificationsList.getNotifications().isEmpty())
            new GetFollowingNotifications(this).execute(1);
    }

    private static class GetFollowingNotifications extends AsyncTask<Integer, Void, Void> {

        private WeakReference<FollowingNotificationsTabFragment> reference;

        GetFollowingNotifications(FollowingNotificationsTabFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers[0] == 1)
                reference.get().notificationsList.getNotifications().clear();

            ApiCallingService.User.getFollowingNotifications(integers[0], reference.get().getContext())
                    .enqueue(new Callback<NotificationsList>() {
                        @Override
                        public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                            if (response.code() == 200) {
                                reference.get().is_next_page = response.body().isNextPage();
                                if (response.body().getNotifications().size() > 0) {
                                    reference.get().swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    reference.get().noNotifications.setVisibility(View.GONE);
                                    reference.get().notificationsList.getNotifications().addAll(response.body().getNotifications());
                                    reference.get().recyclerView.getRecycledViewPool().clear();
                                    reference.get().adapter.notifyDataSetChanged();
                                } else {
                                    reference.get().swipeRefreshLayout.setVisibility(View.GONE);
                                    reference.get().noNotifications.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toast.makeText(reference.get().getContext(), response.code() + " : " + response.message(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            reference.get().swipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onFailure(Call<NotificationsList> call, Throwable t) {
                            Toast.makeText(reference.get().getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
//        void onListFragmentInteraction(Pojos.User.Notification item);
//    }
}

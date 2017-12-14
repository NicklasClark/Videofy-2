package com.cncoding.teazer.home.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.user.Notification;
import com.cncoding.teazer.model.user.NotificationsList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 */
public class RequestNotificationsTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_notifications) ProximaNovaBoldTextView noNotifications;

//    private OnListFragmentInteractionListener mListener;
    private NotificationsList notificationsList;
    private NotificationsAdapter adapter;
    private Call<NotificationsList> notificationsListCall;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RequestNotificationsTabFragment() {
    }

    public static RequestNotificationsTabFragment newInstance() {
        return new RequestNotificationsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications_tab, container, false);
        ButterKnife.bind(this, rootView);
        notificationsList = new NotificationsList(new ArrayList<Notification>(), 0, false);

        adapter = new NotificationsAdapter(getContext(), false, notificationsList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getRequestNotifications(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getRequestNotifications(1);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();


        if (notificationsList.getNotifications() != null && notificationsList.getNotifications().isEmpty())
            getRequestNotifications(1);
    }

    private void getRequestNotifications(final int page) {

        notificationsListCall = ApiCallingService.User.getRequestNotifications(page, getContext());

        if (!notificationsListCall.isExecuted())
            notificationsListCall.enqueue(new Callback<NotificationsList>() {
                @Override
                public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                    try {
                        if (isAdded()) {
                            if (response.code() == 200) {
                                is_next_page = response.body().isNextPage();
                                if (response.body().getNotifications().size() > 0) {
                                    if (page == 1) notificationsList.getNotifications().clear();
                                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                                    noNotifications.setVisibility(View.GONE);
                                    notificationsList.getNotifications().addAll(response.body().getNotifications());
                                    recyclerView.getRecycledViewPool().clear();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    if (page == 1) {
                                        swipeRefreshLayout.setVisibility(View.GONE);
                                        noNotifications.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), response.code() + " : " + response.message(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsList> call, Throwable t) {
                    t.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
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
        if (notificationsListCall != null)
            notificationsListCall.cancel();
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
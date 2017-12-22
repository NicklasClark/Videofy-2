package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowingAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.friends.FollowingsList;
import com.cncoding.teazer.model.friends.UserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingListActivities extends BaseFragment {
    private static final String ARG_ID ="FollowingId" ;
    private static final String ARG_IDENTIFIER ="identifier" ;
    Context context;
    List<UserInfo> list;
    List<UserInfo> otherlist;
    RecyclerView recyclerView;
    FollowingAdapter profileMyFollowingAdapter;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.nousertext)
    TextView nousertext;
    @BindView(R.id.loader)GifTextView loader;
    int otherfollowingpage;
    int userfollowingpage;
    String followerid;
    String identifier;
    boolean next;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    public static FollowingListActivities newInstance(String id,String identifier) {
        FollowingListActivities followersListActivity = new FollowingListActivities();

        Bundle bundle=new Bundle();
        bundle.putString(ARG_ID,id);
        bundle.putString(ARG_IDENTIFIER,identifier);
        followersListActivity.setArguments(bundle);
        return followersListActivity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            followerid  = getArguments().getString(ARG_ID);
            identifier = getArguments().getString(ARG_IDENTIFIER);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_followers, container, false);
        ButterKnife.bind(this,view);
        context=container.getContext();
        getParentActivity().updateToolbarTitle("Following List");
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        endlessRecyclerViewScrollListener= new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if(next) {
                    if (identifier.equals("User")) {
                        if(page>2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getUserfollowinglist(page);
                    }
                    else
                    {
                        if(page>2) {
                            loader.setVisibility(View.VISIBLE);
                        }
                        getOthersFollowingList(Integer.parseInt(followerid),page);
                    }


                }

            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        list=new ArrayList<>();
        otherlist=new ArrayList<>();

        if (identifier.equals("User")) {

            layout.setVisibility(View.GONE);
            profileMyFollowingAdapter = new FollowingAdapter(context, list, 100);
            recyclerView.setAdapter(profileMyFollowingAdapter);
            loader.setVisibility(View.VISIBLE);
            getUserfollowinglist(1);
        }
        else if (identifier.equals("Other"))
        {
            layout.setVisibility(View.GONE);
            otherfollowingpage=1;
            profileMyFollowingAdapter = new FollowingAdapter(context, otherlist);
            recyclerView.setAdapter(profileMyFollowingAdapter);
            loader.setVisibility(View.VISIBLE);
            getOthersFollowingList(Integer.parseInt(followerid),1);
        }
    }
    public void getUserfollowinglist(final int userfollowingpage) {
        ApiCallingService.Friends.getMyFollowing(userfollowingpage, context).enqueue(new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                if (response.code() == 200) {

                    try {
                        list.addAll(response.body().getFollowings());
                        if ((list==null||list.size()==0) && userfollowingpage == 1) {

                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);

                        }
                        else {
                            progressBar.setVisibility(View.VISIBLE);
                            next=response.body().getNextPage();
                            profileMyFollowingAdapter.notifyDataSetChanged();
                            profileMyFollowingAdapter.notifyItemRangeInserted(profileMyFollowingAdapter.getItemCount(), list.size() - 1);
                            progressBar.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);

                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);

                    }
                } else {
                    Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);


                }
            }
            @Override
            public void onFailure(Call<FollowingsList> call, Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

    }


    public void getOthersFollowingList(final int userId, final int pageId) {
        ApiCallingService.Friends.getFriendsFollowings(pageId, userId, context).enqueue(new Callback<FollowingsList>() {
            @Override
            public void onResponse(Call<FollowingsList> call, Response<FollowingsList> response) {
                if (response.code() == 200) {
                    try {

                        otherlist.addAll(response.body().getFollowings());
                        if ((otherlist==null||otherlist.size()==0) && pageId == 1) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            nousertext.setVisibility(View.VISIBLE);
                            loader.setVisibility(View.GONE);

                        }
                        else {
                            next=response.body().getNextPage();
                            profileMyFollowingAdapter.notifyItemRangeInserted(profileMyFollowingAdapter.getItemCount(), otherlist.size() - 1);
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);

                        }



                    } catch (Exception e) {
                        Toast.makeText(context, "Oops! Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);


                    }
                }
                else {
                    Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                }
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<FollowingsList> call, Throwable t) {
                Toast.makeText(context, "Something went wrong,Please try again", Toast.LENGTH_LONG).show();
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();



    }
}


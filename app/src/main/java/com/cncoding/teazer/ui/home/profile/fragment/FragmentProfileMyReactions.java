package com.cncoding.teazer.ui.home.profile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.adapter.ProfileMyReactionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by MOHD ARIF on 07-11-2017.
 */

public class FragmentProfileMyReactions extends BaseFragment {

    private static String ARG_ID;
    CircularAppCompatImageView menuitem;
    RecyclerView recyclerView;
    ProfileMyReactionAdapter profileMyReactionAdapter;
    GridLayoutManager layoutManager;
    Context context;
    List<PostReaction> myReactions;
    int page;
    ProximaNovaSemiBoldTextView alert1;
    ProximaNovaRegularTextView alert2;
    ProximaNovaSemiBoldTextView alert3;
    ProximaNovaSemiBoldTextView alert4;
    DynamicProgress loader;
    int followerfollowingid;

    boolean next;

    public static FragmentProfileMyReactions newInstance(int userId) {
        FragmentProfileMyReactions profileMyReactions= new FragmentProfileMyReactions();

        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ID, userId);
        profileMyReactions.setArguments(bundle);

        return profileMyReactions;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = new Bundle();
        followerfollowingid = getArguments().getInt(ARG_ID);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_myreactions, container, false);
        context=container.getContext();
        recyclerView=view.findViewById(R.id.recycler_view);
        alert1=view.findViewById(R.id.alert1);
        alert2=view.findViewById(R.id.alert2);
        alert3=view.findViewById(R.id.alert3);
        alert4=view.findViewById(R.id.alert4);
        loader=view.findViewById(R.id.loader);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        myReactions = new ArrayList<>();
        layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);

        profileMyReactionAdapter = new ProfileMyReactionAdapter(this, myReactions, getParentFragment(),context);
        recyclerView.setAdapter(profileMyReactionAdapter);

        if(followerfollowingid==0) {
            getReactions(1);
        }
        else
        {
             getFriendsReactions(followerfollowingid,1);

        }

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (next) {
                    if (page > 2) {
                        loader.setVisibility(View.VISIBLE);
                    }
                    if(followerfollowingid==0) {
                        getReactions(page);

                    }
                    else
                    {
                        getFriendsReactions(followerfollowingid,page);

                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    public void getReactions(final int page) {
        ApiCallingService.React.getMyReactions(page,context).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {
                if (response.code() == 200) {
                    try {
                        if ((response.body().getReactions() == null||response.body().getReactions().size()==0) && page==1) {
                            alert1.setVisibility(View.VISIBLE);
                            alert2.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                        }
                        else
                        {

                                next = response.body().isNextPage();
                                myReactions.addAll(response.body().getReactions());
                                recyclerView.getAdapter().notifyDataSetChanged();
                                profileMyReactionAdapter.notifyItemRangeInserted(profileMyReactionAdapter.getItemCount(), myReactions.size() - 1);
                                loader.setVisibility(View.GONE);

                            }

                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_LONG).show();
                        loader.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_LONG).show();

                    loader.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t)
            {
                Toast.makeText(context, "Oops Something went wrong, Please try again", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void getFriendsReactions(final int userId, final int page) {
        ApiCallingService.React.getFriendsReactions(userId,page,context).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {
                if (response.code() == 200) {
                    try {

                        if (response.body().getCanShowReactions()) {

                            if ((response.body().getReactions() == null || response.body().getReactions().size() == 0) && page == 1) {

                                alert3.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                                loader.setVisibility(View.GONE);
                            } else {
                                next = response.body().isNextPage();
                                myReactions.addAll(response.body().getReactions());
                                recyclerView.getAdapter().notifyDataSetChanged();
                                profileMyReactionAdapter.notifyItemRangeInserted(profileMyReactionAdapter.getItemCount(), myReactions.size() - 1);
                                loader.setVisibility(View.GONE);
                            }
                        } else {
                            alert4.setVisibility(View.VISIBLE);
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_LONG).show();
                        loader.setVisibility(View.GONE);
                    }
                }
                else
                {
                    Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_LONG).show();

                    loader.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t)
            {
                Toast.makeText(context, "Oops Something went wrong, Please try again", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

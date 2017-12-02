package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.discover.DiscoverFragment;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListAdapter extends RecyclerView.Adapter<MyInterestsListAdapter.ViewHolder> {

    private ArrayList<Category> myInterestsCategoriesArrayList;
    private Map<String, ArrayList<PostDetails>> myInterests;
    private Context context;
    private DiscoverFragment.OnSearchInteractionListener mListener;

    public MyInterestsListAdapter(ArrayList<Category> myInterestsCategoriesArrayList, Map<String, ArrayList<PostDetails>> myInterests,
                                  Context context, DiscoverFragment.OnSearchInteractionListener mListener) {
        this.myInterestsCategoriesArrayList = myInterestsCategoriesArrayList;
        this.myInterests = myInterests;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public MyInterestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_interests, parent, false);
        return new MyInterestsListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyInterestsListAdapter.ViewHolder holder, int position) {
        if (position < 3) {
            holder.header.setText(myInterestsCategoriesArrayList.get(position).getCategoryName());

            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(new MyInterestsListItemAdapter(
                    myInterests.get(myInterestsCategoriesArrayList.get(position).getCategoryName()), context, mListener));
        }
    }

    @Override
    public int getItemCount() {
        return myInterestsCategoriesArrayList.size() >=3 ? 3 : myInterestsCategoriesArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.interests_header) ProximaNovaRegularTextView header;
        @BindView(R.id.no_my_interests_posts) ProximaNovaBoldTextView noMyInterests;
        @BindView(R.id.item_my_interests_list) RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
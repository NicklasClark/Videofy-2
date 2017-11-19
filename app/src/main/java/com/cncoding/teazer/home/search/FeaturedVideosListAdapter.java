package com.cncoding.teazer.home.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class FeaturedVideosListAdapter extends RecyclerView.Adapter<FeaturedVideosListAdapter.ViewHolder> {

    public FeaturedVideosListAdapter() {
    }

    @Override
    public FeaturedVideosListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reaction_post, parent, false);
        return new FeaturedVideosListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeaturedVideosListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
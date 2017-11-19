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

public class MyInterestsListItemAdapter extends RecyclerView.Adapter<MyInterestsListItemAdapter.ViewHolder> {

    public MyInterestsListItemAdapter() {
    }

    @Override
    public MyInterestsListItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_interests_list_item, parent, false);
        return new MyInterestsListItemAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyInterestsListItemAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
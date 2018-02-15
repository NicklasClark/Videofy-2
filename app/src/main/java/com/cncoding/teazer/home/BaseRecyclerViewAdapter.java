package com.cncoding.teazer.home;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 *
 * Created by Prem$ on 2/8/2018.
 */

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    public abstract BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bind(position);
    }

    public abstract int getItemCount();

    public abstract void release();
}

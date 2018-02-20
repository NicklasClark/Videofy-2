package com.cncoding.teazer.home;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Created by Prem$ on 2/8/2018.
 */

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    public abstract BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bind(position);
//        onBindViewHolder(holder, position, null);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null) {
            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            } else holder.bind(position, payloads);
        } else holder.bind(position);
    }

    public abstract int getItemCount();

    public abstract void release();
}

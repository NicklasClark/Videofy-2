package com.cncoding.teazer.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Created by Prem$ on 2/21/2018.
 */

public class BaseRecyclerView {

    @SuppressWarnings("unchecked")
    public abstract static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
            if (payloads != null) {
                if (!payloads.isEmpty()) {
                    holder.bind(payloads);
                }
                else holder.bind();
            }
            else holder.bind();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        public abstract int getItemCount();

        public abstract void release();

        public abstract void notifyDataChanged();
    }

    public abstract static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind();

        public void bind(List<Object> payloads) {}
    }
}
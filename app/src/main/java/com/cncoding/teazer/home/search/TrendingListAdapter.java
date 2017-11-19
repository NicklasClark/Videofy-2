package com.cncoding.teazer.home.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class TrendingListAdapter extends RecyclerView.Adapter<TrendingListAdapter.ViewHolder> {

    private String[] titles = new String[]{"Singing", "Dance", "Comedy", "Videos"};

    public TrendingListAdapter() {
    }

    @Override
    public TrendingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending, parent, false);
        return new TrendingListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrendingListAdapter.ViewHolder holder, int position) {
        holder.title.setText(titles[new Random().nextInt(titles.length - 1)]);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_trending) ProximaNovaRegularTextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
package com.cncoding.teazer.home.search;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.home.search.DummyData.getMyInterestsList;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListAdapter extends RecyclerView.Adapter<MyInterestsListAdapter.ViewHolder> {

    private String[] titles = new String[] {"Dance", "Music", "VideoGraphy"};
    private Context context;

    MyInterestsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyInterestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_interests, parent, false);
        return new MyInterestsListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyInterestsListAdapter.ViewHolder holder, int position) {
        holder.header.setText(titles[position]);

        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(new MyInterestsListItemAdapter(getMyInterestsList(), context));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.interests_header) ProximaNovaRegularTextView header;
        @BindView(R.id.item_my_interests_list) RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
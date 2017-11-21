package com.cncoding.teazer.home.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class TrendingListAdapter extends RecyclerView.Adapter<TrendingListAdapter.ViewHolder> {

    private String[] titles = new String[]{
            "Singing", "Dance", "Comedy", "Videos", "Travel", "Photography", "Adventure", "Instruments", "Comedy", "Acting", "Technology", "iOS", "Android",
            "Fashion", "Lifestyle", "Sports", "Restaurants", "Wildlife", "Nightlife", "Photography", "Love", "Health And Fitness", "History", "Home Décor", "Humour",
            "Kids And Parenting", "Men's Fashion", "Outdoors", "Photography", "Quotes", "Science", "Nature", "Sports", "Tattoos", "Technology", "Travel", "Weddings",
            "Women's Fashion", "Popular", "Everything", "Animals And Pets", "Architecture", "Art", "Cars And Motorcycles", "Celebrations And Events",
            "Celebrities", "DIY And Crafts", "Design", "Education", "Entertainment", "Food And Drink", "Gardening", "Geek", "Hair And Beauty", "Crazy"
    };

    TrendingListAdapter() {
    }

    @Override
    public TrendingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending, parent, false);
        return new TrendingListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrendingListAdapter.ViewHolder holder, int position) {
        holder.title.setText(titles[position]);
    }

    @Override
    public int getItemCount() {
        return 54;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_trending) ProximaNovaRegularTextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.model.base.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class TrendingListAdapter extends RecyclerView.Adapter<TrendingListAdapter.ViewHolder> {

    private SparseIntArray sparseIntArray;
    private ArrayList<Category> trendingCategories;
    private TrendingListInteractionListener mListener;

    public TrendingListAdapter(ArrayList<Category> trendingCategories, Context context) {
        this.trendingCategories = trendingCategories;
        sparseIntArray = new SparseIntArray();
        if (context instanceof TrendingListInteractionListener) {
            mListener = (TrendingListInteractionListener) context;
        }
    }

    @Override
    public TrendingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending, parent, false);
        return new TrendingListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrendingListAdapter.ViewHolder holder, int position) {
        holder.title.setBackground(getBackground(holder.title, position));
        holder.title.setText(trendingCategories.get(position).getCategoryName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onTrendingListInteraction(trendingCategories.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    private GradientDrawable getBackground(ProximaNovaSemiboldTextView title, int position) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (sparseIntArray.get(position) == 0) {
            sparseIntArray.put(position, Color.parseColor(trendingCategories.get(position).getColor()));
        }
        gradientDrawable.setColor(Color.TRANSPARENT);
        gradientDrawable.setCornerRadius(3);
        gradientDrawable.setStroke(1, sparseIntArray.get(position));
        title.setTextColor(sparseIntArray.get(position));
        return gradientDrawable;
    }

    @Override
    public int getItemCount() {
        return trendingCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_trending) ProximaNovaSemiboldTextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface TrendingListInteractionListener {
        void onTrendingListInteraction(Category category);
    }
}
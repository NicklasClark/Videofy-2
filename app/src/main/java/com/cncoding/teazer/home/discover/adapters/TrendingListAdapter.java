package com.cncoding.teazer.home.discover.adapters;

import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.BaseRecyclerView;
import com.cncoding.teazer.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.home.discover.SubDiscoverFragment;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.utilities.diffutil.CategoryDiffCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.util.DiffUtil.calculateDiff;
import static android.view.LayoutInflater.from;
import static com.cncoding.teazer.home.discover.DiscoverFragment.ACTION_VIEW_TRENDING;
import static com.cncoding.teazer.utilities.ViewUtils.getClassicCategoryBackground;
import static com.cncoding.teazer.utilities.diffutil.CategoryDiffCallback.NEW_CATEGORY;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class TrendingListAdapter extends BaseRecyclerView.Adapter {

    private ArrayList<Category> trendingCategories;
    private BaseDiscoverFragment fragment;

    public TrendingListAdapter(BaseDiscoverFragment fragment) {
        this.fragment = fragment;
        if (this.trendingCategories == null) this.trendingCategories = new ArrayList<>();
    }

    @Override public TrendingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TrendingListViewHolder(from(parent.getContext()).inflate(R.layout.item_trending, parent, false));
    }

    @Override public int getItemCount() {
        return trendingCategories.size();
    }

    @Override public void release() {}

    @Override
    public void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void updateCategories(List<Category> categoryList) {
        try {
            final DiffUtil.DiffResult result = calculateDiff(new CategoryDiffCallback(new ArrayList<>(trendingCategories), categoryList));
            trendingCategories.clear();
            trendingCategories.addAll(categoryList);
            fragment.getParentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.dispatchUpdatesTo(TrendingListAdapter.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            try {
                addCategoryList(categoryList);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void addCategoryList(List<Category> categoryList) {
        try {
            if (trendingCategories == null) trendingCategories = new ArrayList<>();
            else trendingCategories.clear();
            trendingCategories.addAll(categoryList);
            notifyDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TrendingListViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.item_trending) ProximaNovaSemiBoldTextView title;
        private Category category;

        TrendingListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override public void bind() {
            category = trendingCategories.get(getAdapterPosition());
            title.setBackground(getClassicCategoryBackground(title, category.getMyColor()));
            title.setText(category.getCategoryName());
        }

        @Override public void bind(List<Object> payloads) {
            if (payloads == null || payloads.isEmpty()) return;

            Bundle bundle = (Bundle) payloads.get(0);
            if (bundle.containsKey(NEW_CATEGORY)) {
                category = bundle.getParcelable(NEW_CATEGORY);
            }
        }

        @OnClick(R.id.item_trending) public void openTrendingPostsOfThisCategory() {
            ArrayList<Category> categories = new ArrayList<>();
            categories.add(category);
            fragment.navigation.pushFragment(SubDiscoverFragment.newInstance(ACTION_VIEW_TRENDING, categories));
        }
    }
}
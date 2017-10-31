package com.cncoding.teazer.camera.upload;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;

import java.util.ArrayList;

/**
 * 
 * Created by Prem $ on 10/20/2017.
 */

class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private ArrayList<Category> categories;
    private Fragment fragment;
    private SparseBooleanArray selectedCategoriesArray;
    private SparseArray<Category> selectedCategories;

    CategoriesAdapter(ArrayList<Category> categories, Fragment fragment) {
        this.categories = categories;
        this.fragment = fragment;
        selectedCategoriesArray = new SparseBooleanArray();
        selectedCategories = new SparseArray<>();
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent, false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoriesAdapter.ViewHolder holder, int position) {
        final Category category = this.categories.get(position);
        holder.nameView.setText(category.getCategoryName());

        holder.nameView.setChecked(selectedCategoriesArray.get(holder.getAdapterPosition()));

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = !holder.nameView.isChecked();
                selectedCategoriesArray.put(holder.getAdapterPosition(), isChecked);
                holder.nameView.setChecked(isChecked);
                ((TagsAndCategoryFragment) fragment).changeVisibility(View.VISIBLE);
                if (isChecked) {
                    selectedCategories.put(holder.getAdapterPosition(), category);
                }
                else selectedCategories.delete(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rootLayout;
        private ProximaNovaRegularCheckedTextView nameView;

        ViewHolder(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.categories_item_layout);
            nameView = view.findViewById(R.id.list_item_checked);
        }
    }

    SparseArray<Category> getSelectedCategories() {
        return selectedCategories;
    }
}
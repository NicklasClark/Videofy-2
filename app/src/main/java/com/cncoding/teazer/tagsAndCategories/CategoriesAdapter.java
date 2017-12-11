package com.cncoding.teazer.tagsAndCategories;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.utilities.Pojos.Category;

import java.util.ArrayList;

/**
 * 
 * Created by Prem $ on 10/20/2017.
 */

class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private ArrayList<Category> categories;
    private Fragment fragment;
    private String selectedCategoriesString;
    private SparseBooleanArray selectedCategoriesArray;
    private SparseArray<Category> categorySparseArray;

    CategoriesAdapter(ArrayList<Category> categories, Fragment fragment, String selectedCategoriesString) {
        this.categories = categories;
        this.fragment = fragment;
        this.selectedCategoriesString = selectedCategoriesString;
        selectedCategoriesArray = new SparseBooleanArray();
        this.categorySparseArray = new SparseArray<>();
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories, parent, false);
        return new CategoriesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CategoriesAdapter.ViewHolder holder, int position) {
        final Category category = this.categories.get(position);
        holder.nameView.setText(category.getCategoryName());

        setCheck(holder.nameView, position, category,
                selectedCategoriesString != null && selectedCategoriesString.contains(category.getCategoryName()));
        if (selectedCategoriesString == null || selectedCategoriesString.isEmpty())
            setCheck(holder.nameView, position, category, selectedCategoriesArray.get(position));

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categorySparseArray.size() < 5) {
                    boolean isChecked = !holder.nameView.isChecked();
                    selectedCategoriesArray.put(holder.getAdapterPosition(), isChecked);
                    ((TagsAndCategoryFragment) fragment).changeDoneBtnVisibility(View.VISIBLE);
                    setCheck(holder.nameView, holder.getAdapterPosition(), category, !holder.nameView.isChecked());
                } else {
                    Toast.makeText(fragment.getContext(), "Maximum 5 categories can be selected", Toast.LENGTH_SHORT).show();
                    holder.nameView.setChecked(false);
                    categorySparseArray.delete(holder.getAdapterPosition());
                }
            }
        });
    }

    private void setCheck(AppCompatCheckedTextView textView, int position, Category category, boolean checked) {
        textView.setChecked(checked);
        if (textView.isChecked()) {
            textView.setTextColor(Color.parseColor("#26C6DA"));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_accent, 0);
            categorySparseArray.put(position, category);
        }
        else {
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            categorySparseArray.delete(position);
        }
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
            nameView = view.findViewById(R.id.chip);
        }
    }

    SparseArray<Category> getSelectedCategories() {
        return categorySparseArray;
    }
}
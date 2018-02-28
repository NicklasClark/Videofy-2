package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.model.base.Category;

import java.util.List;
import java.util.Objects;

/**
 * 
 * Created by Prem$ on 2/20/2018.
 */

public class CategoryDiffCallback extends DiffUtil.Callback {

    public static final String NEW_CATEGORY = "newCategory";

    private List<Category> oldCategories;
    private List<Category> newCategories;

    public CategoryDiffCallback(List<Category> oldCategories, List<Category> newCategories) {
        this.oldCategories = oldCategories;
        this.newCategories = newCategories;
    }

    @Override
    public int getOldListSize() {
        return oldCategories != null ? oldCategories.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newCategories != null ? newCategories.size() : 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newCategories.get(newItemPosition).getCategoryId(), oldCategories.get(oldItemPosition).getCategoryId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newCategories.get(newItemPosition), oldCategories.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Category oldCategory = oldCategories.get(oldItemPosition);
        Category newCategory = newCategories.get(newItemPosition);

        if (oldCategory != null && newCategory != null) {
            Bundle diffBundle = new Bundle();
            diffBundle.putParcelable(NEW_CATEGORY, newCategory);
            return diffBundle;
        }
        return null;
    }
}

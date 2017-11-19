package com.cncoding.teazer.tagsAndCategories;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;

import java.util.ArrayList;

/**
 * 
 * Created by Prem $ on 10/20/2017.
 */

class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private ArrayList<MiniProfile> circles;
    private Fragment fragment;
    private SparseBooleanArray selectedTagsArray;
    private SparseArray<MiniProfile> selectedTags;

    @SuppressLint("UseSparseArrays")
    TagsAdapter(ArrayList<MiniProfile> circles, Fragment fragment) {
        this.circles = circles;
        this.fragment = fragment;
        selectedTagsArray = new SparseBooleanArray();
        selectedTags = new SparseArray<>();
    }

    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, parent, false);
        return new TagsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TagsAdapter.ViewHolder holder, int position) {
        final MiniProfile circle = circles.get(position);
        holder.nameView.setText(circle.getFirstName() + " " + circle.getLastName());
        if (circle.hasProfileMedia()) {
            Glide.with(fragment)
                    .load(circle.getProfileMedia().getThumbUrl())
                    .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                    .crossFade(400)
                    .into(holder.image);
        } else //noinspection ConstantConditions
            Glide.with(fragment).load("")
                .placeholder(fragment.getContext().getResources().getDrawable(R.drawable.ic_user_dp_small, null))
                .into(holder.image);

        holder.nameView.setChecked(selectedTagsArray.get(holder.getAdapterPosition()));

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = !holder.nameView.isChecked();
                selectedTagsArray.put(holder.getAdapterPosition(), isChecked);
                holder.nameView.setChecked(isChecked);
                ((TagsAndCategoryFragment) fragment).changeVisibility(View.VISIBLE);
                if (isChecked) {
                    selectedTags.put(holder.getAdapterPosition(), circle);
                }
                else selectedTags.delete(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rootLayout;
        private CircularImageView image;
        private ProximaNovaRegularCheckedTextView nameView;

        ViewHolder(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.tags_item_layout);
            image = view.findViewById(R.id.list_item_checked_thumbnail_image);
            nameView = view.findViewById(R.id.list_item_checked);
        }
    }

    SparseArray<MiniProfile> getSelectedTags() {
        return selectedTags;
    }
}
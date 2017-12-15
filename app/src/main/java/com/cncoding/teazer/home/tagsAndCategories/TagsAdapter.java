package com.cncoding.teazer.home.tagsAndCategories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.model.base.MiniProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 
 * Created by Prem $ on 10/20/2017.
 */

class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private ArrayList<MiniProfile> circles;
    private Fragment fragment;
    private String selectedTagsString;
    private SparseBooleanArray selectedTagsArray;
    private SparseArray<MiniProfile> tagsSparseArray;
    private Typeface SEMI_BOLD;
    private Typeface REGULAR;

    TagsAdapter(Context context, ArrayList<MiniProfile> circles, Fragment fragment, String selectedTagsString) {
        this.circles = circles;
        this.fragment = fragment;
        this.selectedTagsString = selectedTagsString;
        selectedTagsArray = new SparseBooleanArray();
        this.tagsSparseArray = new SparseArray<>();
        TypeFactory typeFactory = new TypeFactory(context);
        SEMI_BOLD = typeFactory.semiBold;
        REGULAR = typeFactory.regular;
    }

    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, parent, false);
        return new TagsAdapter.ViewHolder(view);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBindViewHolder(final TagsAdapter.ViewHolder holder, int position) {
        final MiniProfile circle = circles.get(position);
        String name = circle.getFirstName() + " " + circle.getLastName();

        holder.nameView.setText(name);

        Glide.with(fragment)
                .load(circle.getProfileMedia() != null ? circle.getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
                .placeholder(R.drawable.ic_user_male_dp_small)
                .crossFade()
                .into(holder.image);

        setCheck(holder.nameView, position, circle,
                selectedTagsString != null && selectedTagsString.contains(circle.getFirstName()));

        if (selectedTagsString == null || selectedTagsString.isEmpty())
            setCheck(holder.nameView, position, circle, selectedTagsArray.get(position));

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheck(holder.nameView, holder.getAdapterPosition(), circle, !holder.nameView.isChecked());
            }
        });
    }

    private void setCheck(AppCompatCheckedTextView textView, int position, MiniProfile circle, boolean checked) {
        textView.setChecked(checked);
        if (textView.isChecked()) {
            textView.setTextColor(Color.parseColor("#26C6DA"));
            textView.setTypeface(SEMI_BOLD);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_accent, 0);
            tagsSparseArray.put(position, circle);
        }
        else {
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setTypeface(REGULAR);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tagsSparseArray.delete(position);
        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tags_item_layout) RelativeLayout rootLayout;
        @BindView(R.id.list_item_checked_thumbnail_image) CircularAppCompatImageView image;
        @BindView(R.id.chip) ProximaNovaRegularCheckedTextView nameView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    SparseArray<MiniProfile> getSelectedTags() {
        return tagsSparseArray;
    }
}
package com.cncoding.teazer.tagsAndCategories;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.utilities.Pojos.Category;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/12/2017.
 */

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {

    private ArrayList<Category> interestsList;
    private Interests interests;
    private boolean isEditing;
    private SparseBooleanArray selectedInterestsArray;
    private SparseArray<Category> selectedInterests;

    InterestsAdapter(ArrayList<Category> interestsList, Interests interests, boolean isEditing, SparseBooleanArray categories) {
        this.interestsList = interestsList;
        this.interests = interests;
        this.isEditing = isEditing;
        if (isEditing) {
            selectedInterestsArray = categories;
        } else
            selectedInterestsArray = new SparseBooleanArray();
        selectedInterests = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (!isEditing)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories, parent, false);

        return new InterestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String categoryText;
        if (!isEditing)
            categoryText = "+ " + interestsList.get(position).getCategoryName();
        else categoryText = interestsList.get(position).getCategoryName();
        holder.chip.setText(categoryText);

        holder.chip.setChecked(selectedInterestsArray.get(position));
        checkAction(holder.chip, position, false);

        holder.chip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectedInterestsArray.put(holder.getAdapterPosition(), !holder.chip.isChecked());
                holder.chip.setChecked(!holder.chip.isChecked());

                checkAction(holder.chip, holder.getAdapterPosition(), true);

                if (selectedInterests.size() >= 5) {
                    if (!interests.isSaveBtnEnabled())
                        interests.enableSaveBtn();
                } else {
                    if (interests.isSaveBtnEnabled())
                        interests.disableSaveBtn();
                }
            }
        });
    }

    @SuppressWarnings("ConstantConditions")
    private void checkAction(ProximaNovaRegularCheckedTextView view, int position, boolean animate) {
        if (view.isChecked()) {
            selectedInterests.put(position, interestsList.get(position));
            if (!isEditing) {
                view.setBackground(interests.getActivity().getResources().getDrawable(R.drawable.chip_selected));
                if (animate)
                    view.startAnimation(AnimationUtils.loadAnimation(interests.getContext(), R.anim.selected));
            }
        } else {
            selectedInterests.delete(position);
            if (!isEditing) {
                view.setBackground(interests.getActivity().getResources().getDrawable(R.drawable.chip_default));
                if (animate)
                    view.startAnimation(AnimationUtils.loadAnimation(interests.getContext(), R.anim.deselected));
            }
        }
    }

    @Override
    public int getItemCount() {
        return interestsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chip) ProximaNovaRegularCheckedTextView chip;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    SparseArray<Category> getSelectedInterests() {
        return selectedInterests;
    }
}

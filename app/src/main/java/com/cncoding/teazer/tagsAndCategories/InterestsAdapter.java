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
    private SparseBooleanArray selectedInterestsArray;
    private SparseArray<Category> selectedInterests;

    InterestsAdapter(ArrayList<Category> interestsList, Interests interests) {
        this.interestsList = interestsList;
        this.interests = interests;
        selectedInterestsArray = new SparseBooleanArray();
        selectedInterests = new SparseArray<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        return new InterestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.chip.setText("+ " + interestsList.get(position).getCategoryName());

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
            view.setBackground(interests.getActivity().getResources().getDrawable(R.drawable.chip_selected));
            if (animate)
                view.startAnimation(AnimationUtils.loadAnimation(interests.getContext(), R.anim.selected));
        } else {
            selectedInterests.delete(position);
            view.setBackground(interests.getActivity().getResources().getDrawable(R.drawable.chip_default));
            if (animate)
                view.startAnimation(AnimationUtils.loadAnimation(interests.getContext(), R.anim.deselected));
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

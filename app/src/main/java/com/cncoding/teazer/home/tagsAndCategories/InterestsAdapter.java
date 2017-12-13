package com.cncoding.teazer.home.tagsAndCategories;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/12/2017.
 */

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> {

    private Typeface REGULAR;
    private Typeface SEMI_BOLD;
    private ArrayList<Category> interestsList;
    private Interests interests;
//    private SparseIntArray sparseIntArray;
    private String selectedCategoriesString;
    private final boolean isForVideo;
    private SparseBooleanArray selectedInterestsArray;
    private SparseArray<Category> selectedInterests;

    InterestsAdapter(ArrayList<Category> interestsList, final Interests interests, SparseBooleanArray categories,
                     String selectedCategoriesString, final boolean isForVideo) {
        this.interestsList = interestsList;
        this.interests = interests;
        REGULAR = new TypeFactory(interests.getContext()).regular;
        SEMI_BOLD = new TypeFactory(interests.getContext()).semiBold;
//        sparseIntArray = new SparseIntArray();
        selectedInterestsArray = categories != null ? categories : new SparseBooleanArray();
        this.selectedCategoriesString = selectedCategoriesString;
        this.isForVideo = isForVideo;
        selectedInterests = new SparseArray<>();

        try {
            //noinspection ConstantConditions
            interests.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isForVideo) {
                        if (selectedInterestsArray.size() >= 5) {
                            if (!interests.isSaveBtnEnabled())
                                interests.enableSaveBtn();
                        } else {
                            if (interests.isSaveBtnEnabled())
                                interests.disableSaveBtn();
                        }
                    } else interests.enableSaveBtn();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip, parent, false);
        return new InterestsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String categoryText = "+ " + interestsList.get(position).getCategoryName();
        holder.chip.setText(categoryText);

        setCheck(holder.chip, position, false,
                selectedCategoriesString != null &&
                        selectedCategoriesString.contains(categoryText.replace("+ ", "")));
        if (selectedCategoriesString == null || selectedCategoriesString.isEmpty())
            setCheck(holder.chip, position, false, selectedInterestsArray.get(position));

        checkButtonAccess();

        holder.chip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                selectedInterestsArray.put(holder.getAdapterPosition(), !holder.chip.isChecked());
                setCheck(holder.chip, holder.getAdapterPosition(), true, !holder.chip.isChecked());
                checkButtonAccess();
            }
        });
    }

    private void checkButtonAccess() {
        if (!isForVideo) {
            if (selectedInterests.size() >= 5) {
                if (!interests.isSaveBtnEnabled())
                    interests.enableSaveBtn();
            } else {
                if (interests.isSaveBtnEnabled())
                    interests.disableSaveBtn();
            }
        }
    }

    private void setCheck(ProximaNovaRegularCheckedTextView view, int position, boolean animate, boolean checked) {
        if (!isForVideo) {
            checkAction(view, position, checked);
            if (animate)
                view.startAnimation(AnimationUtils.loadAnimation(interests.getContext(), R.anim.selected));
        } else {
            if (selectedInterests.size() < 5) {
                checkAction(view, position, checked);
            } else {
                if (checked)
                    Snackbar.make(interests.saveBtn, R.string.selection_limit_message, Snackbar.LENGTH_SHORT).show();
                else
                    checkAction(view, position, false);
            }
        }
    }

    private void checkAction(ProximaNovaRegularCheckedTextView view, int position, boolean checked) {
        view.setChecked(checked);
        if (view.isChecked()) {
            selectedInterests.put(position, interestsList.get(position));
            view.setTypeface(SEMI_BOLD);
//            if (!isForVideo) {
                view.setBackground(ViewUtils.getBackground(interests.getContext(), view, Color.parseColor("#26C6DA"),
                        Color.parseColor("#26C6DA"), Color.WHITE, 40));
//            } else {
//                view.setBackground(ViewUtils.getBackground(interests.getContext(), view, Color.TRANSPARENT,
//                        Color.parseColor("#26C6DA"), Color.parseColor("#26C6DA"), 40));
//            }
        } else {
            selectedInterests.delete(position);
            view.setTypeface(REGULAR);
            if (!isForVideo) {
                view.setBackground(ViewUtils.getBackground(interests.getContext(), view, Color.TRANSPARENT,
                        Color.WHITE, Color.WHITE, 40));
            } else {
                view.setBackground(ViewUtils.getBackground(interests.getContext(), view, Color.TRANSPARENT,
                        Color.parseColor("#333333"), Color.parseColor("#333333"), 40));
            }
        }
//        view.startAnimation(AnimationUtils.loadAnimation(interests.getContext(), R.anim.selected));
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

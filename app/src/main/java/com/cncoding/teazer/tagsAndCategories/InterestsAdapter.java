package com.cncoding.teazer.tagsAndCategories;

import android.graphics.Color;
import android.graphics.Typeface;
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

    InterestsAdapter(ArrayList<Category> interestsList, Interests interests, SparseBooleanArray categories,
                     String selectedCategoriesString, boolean isForVideo) {
        this.interestsList = interestsList;
        this.interests = interests;
        REGULAR = new TypeFactory(interests.getContext()).regular;
        SEMI_BOLD = new TypeFactory(interests.getContext()).semiBold;
//        sparseIntArray = new SparseIntArray();
        selectedInterestsArray = categories != null ? categories : new SparseBooleanArray();
        this.selectedCategoriesString = selectedCategoriesString;
        this.isForVideo = isForVideo;
        selectedInterests = new SparseArray<>();

        if (selectedInterestsArray.size() >= 5) {
            if (!interests.isSaveBtnEnabled())
                interests.enableSaveBtn();
        } else {
            if (interests.isSaveBtnEnabled())
                interests.disableSaveBtn();
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
                selectedCategoriesString != null && selectedCategoriesString.contains(categoryText));
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

//    private void setChecked(AppCompatCheckedTextView textView, int position, Category category, boolean checked) {
//        textView.setChecked(checked);
//        if (textView.isChecked()) {
//            textView.setTextColor(Color.parseColor("#26C6DA"));
//            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_accent, 0);
//            selectedInterests.put(position, category);
//        }
//        else {
//            textView.setTextColor(Color.parseColor("#333333"));
//            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//            selectedInterests.delete(position);
//        }
//    }

    @SuppressWarnings("ConstantConditions")
    private void setCheck(ProximaNovaRegularCheckedTextView view, int position, boolean animate, boolean checked) {
        view.setChecked(checked);
        if (view.isChecked()) {
            selectedInterests.put(position, interestsList.get(position));
            view.setTypeface(SEMI_BOLD);
            view.setBackground(ViewUtils.getBackground(interests.getContext(), view, Color.TRANSPARENT,
                    Color.parseColor("#26C6DA"), Color.parseColor("#26C6DA"), 40));
        } else {
            selectedInterests.delete(position);
            view.setTypeface(REGULAR);
            view.setBackground(ViewUtils.getBackground(interests.getContext(), view, Color.TRANSPARENT,
                    Color.parseColor("#333333"), Color.parseColor("#333333"), 40));
        }
        if (animate)
            view.startAnimation(AnimationUtils.loadAnimation(interests.getContext(), R.anim.selected));
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

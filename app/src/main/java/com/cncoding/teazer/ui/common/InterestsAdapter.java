package com.cncoding.teazer.ui.common;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.ui.customviews.common.TypeFactory;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.utilities.common.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * Created by Prem $ on 11/12/2017.
 */

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder> implements Filterable {

    private Typeface REGULAR;
    private Typeface SEMI_BOLD;
    private ArrayList<Category> interestsList;
    private ArrayList<Category> interestsListFiltered;
    private Interests interests;
    private String selectedCategoriesString;
    private final boolean isForVideo;
    private SparseBooleanArray selectedInterestsArray;
    private SparseArray<Category> selectedInterests;

    InterestsAdapter(ArrayList<Category> interestsList, final Interests interests, SparseBooleanArray categories,
                     String selectedCategoriesString, final boolean isForVideo) {
        this.interestsList = interestsList;
        interestsListFiltered = interestsList;
        this.interests = interests;
        REGULAR = new TypeFactory(interests.getTheContext()).regular;
        SEMI_BOLD = new TypeFactory(interests.getTheContext()).semiBold;
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
        String categoryText = "+ " + interestsListFiltered.get(position).getCategoryName();
        holder.chip.setText(categoryText);

        if (selectedInterestsArray.size() == 0) {
            setCheck(holder.chip, position, false,
                    selectedCategoriesString != null &&
                            selectedCategoriesString.contains(categoryText.replace("+ ", "")), false);
        }
        else {
            setCheck(holder.chip, position, false,
                    selectedInterestsArray.get(interestsListFiltered.get(holder.getAdapterPosition()).getCategoryId() - 1), false);
        }
        checkButtonAccess();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    interestsListFiltered = interestsList;
                } else {
                    ArrayList<Category> filteredList = new ArrayList<>();
                    for (Category category : interestsList) {
                        if (category.getCategoryName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(category);
                        }
                    }
                    interestsListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = interestsListFiltered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                interestsListFiltered = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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

    private void setCheck(ProximaNovaRegularCheckedTextView view, int position, boolean animate, boolean checked, boolean isOnClick) {
        if (!isForVideo) {
            checkAction(view, position, checked);
        } else {
            if (isOnClick ? selectedInterests.size() < 5 : selectedInterests.size() <= 5) {
                checkAction(view, position, checked);
            } else {
                if (checked) {
                    view.startAnimation(AnimationUtils.loadAnimation(interests.getTheContext(), R.anim.deselected));
                    Toast.makeText(interests.getTheContext(), R.string.selection_limit_message, Toast.LENGTH_SHORT).show();
                } else
                    checkAction(view, position, false);
            }
        }
        if (animate && view.getAnimation() != null)
            view.startAnimation(AnimationUtils.loadAnimation(interests.getTheContext(), R.anim.selected));
    }

    private void checkAction(ProximaNovaRegularCheckedTextView view, int position, boolean checked) {
        view.setChecked(checked);
        if (view.isChecked()) {
            selectedInterests.put(interestsList.get(position).getCategoryId(), interestsList.get(position));
            view.setTypeface(SEMI_BOLD);
//            if (!isForVideo) {
                view.setBackground(ViewUtils.getBackground(interests.getTheContext(), view, Color.parseColor("#26C6DA"),
                        Color.parseColor("#26C6DA"), Color.WHITE, 40));
//            } else {
//                view.setBackground(ViewUtils.getBackground(interests.getTheContext(), view, Color.TRANSPARENT,
//                        Color.parseColor("#26C6DA"), Color.parseColor("#26C6DA"), 40));
//            }
        } else {
            selectedInterests.delete(interestsList.get(position).getCategoryId());
            view.setTypeface(REGULAR);
            if (!isForVideo) {
                view.setBackground(ViewUtils.getBackground(interests.getTheContext(), view, Color.TRANSPARENT,
                        Color.WHITE, Color.WHITE, 40));
            } else {
                view.setBackground(ViewUtils.getBackground(interests.getTheContext(), view, Color.TRANSPARENT,
                        Color.parseColor("#333333"), Color.parseColor("#333333"), 40));
            }
        }
//        view.startAnimation(AnimationUtils.loadAnimation(interests.getTheContext(), R.anim.selected));
    }

    @Override
    public int getItemCount() {
        return interestsListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chip) ProximaNovaRegularCheckedTextView chip;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.chip) public void interestSelected() {
            selectedInterestsArray.put(interestsListFiltered.get(getAdapterPosition()).getCategoryId() - 1, !chip.isChecked());
            setCheck(chip, getAdapterPosition(), true, !chip.isChecked(), true);
            checkButtonAccess();
        }
    }

    SparseArray<Category> getSelectedInterests() {
        return selectedInterests;
    }
}
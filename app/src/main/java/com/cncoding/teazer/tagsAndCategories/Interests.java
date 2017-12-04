package com.cncoding.teazer.tagsAndCategories;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Interests extends BaseFragment {
    private static final String ARG_IS_EDITING = "isEditing";
    private static final String ARG_CATEGORIES = "categories";
//    private static final String CATEGORIES_LIST = "categoriesList";

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.save_interests_btn) ProximaNovaSemiboldTextView saveBtn;

//    private String previousTitle;
//    private ActionBar actionBar;
    private ArrayList<Category> categories;
    private boolean isEditing;
    private InterestsAdapter interestsAdapter;
    private OnInterestsInteractionListener mListener;
    private ArrayList<Category> interestList;

    public Interests() {
        // Required empty public constructor
    }

    public static Interests newInstance(boolean isEditing, ArrayList<Category> categories) {
        Interests interests = new Interests();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_EDITING, isEditing);
        if (isEditing)
            args.putParcelableArrayList(ARG_CATEGORIES, categories);
        interests.setArguments(args);
        return interests;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isEditing = getArguments().getBoolean(ARG_IS_EDITING);
            if (isEditing)
                categories = getArguments().getParcelableArrayList(ARG_CATEGORIES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        if (!isEditing) {
            rootView = inflater.inflate(R.layout.fragment_interests, container, false);
        } else {
            rootView = inflater.inflate(R.layout.fragment_edit_interests, container, false);
            rootView.findViewById(R.id.root_layout).setBackgroundColor(getResources().getColor(R.color.bgInterests));
        }
        ButterKnife.bind(this, rootView);

        interestList = new ArrayList<>();
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext(), FlexDirection.ROW, FlexWrap.WRAP);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        if (!isEditing) {
            interestsAdapter = new InterestsAdapter(interestList, this, null);
            recyclerView.setAdapter(interestsAdapter);
        } else {
            enableSaveBtn();
            //noinspection unchecked
            new SetInterestsAdapter(this).execute(categories);
        }

        recyclerView.setLayoutAnimation(new LayoutAnimationController(
                AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in), 0.04f)
        );
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isEditing && interestList != null && interestList.isEmpty())
            new GetInterests(this).execute();
    }

    private static class SetInterestsAdapter extends AsyncTask<ArrayList<Category>, Void, InterestsAdapter> {

        private WeakReference<Interests> reference;

        SetInterestsAdapter(Interests context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected InterestsAdapter doInBackground(ArrayList<Category>[] categories) {
            SparseBooleanArray booleanArray = new SparseBooleanArray();
            for (Category category : categories[0]) {
                booleanArray.put(category.getCategoryId() - 1, true);
            }
            return new InterestsAdapter(reference.get().interestList, reference.get(), booleanArray);
        }

        @Override
        protected void onPostExecute(InterestsAdapter interestsAdapter) {
            reference.get().interestsAdapter = interestsAdapter;
            reference.get().recyclerView.setAdapter(reference.get().interestsAdapter);
            new GetInterests(reference.get()).execute();
            super.onPostExecute(interestsAdapter);
        }
    }

    private static class GetInterests extends AsyncTask<Void, Void, Void> {

        private WeakReference<Interests> reference;

        GetInterests(Interests context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<Category>>() {
                @Override
                public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                    if (response.code() == 200) {
                        reference.get().interestList.clear();
                        reference.get().interestList.addAll(response.body());
                        reference.get().recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    Log.e("getCategories", t.getMessage());
                }
            });
            return null;
        }
    }

    @OnClick(R.id.save_interests_btn) public void saveInterests() {
        ApiCallingService.User.updateCategories(new Pojos.User.UpdateCategories(
                getSelectedInterestsToSend(interestsAdapter.getSelectedInterests())), getContext())
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200) {
                            Log.d("Updating interests", "Updated successfully");
                        } else {
                            Log.d("Updating interests", response.code() + " : " + response.message());
                        }
                        mListener.onInterestsInteraction();
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Log.e("Updating categories", t.getMessage());
                    }
                });
    }

    private String getSelectedInterestsToSend(SparseArray<Category> sparseArray) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            string.append(sparseArray.valueAt(i).getCategoryId());
            if (i < sparseArray.size() - 1) {
                string.append(",");
            }
        }
        return string.toString();
    }

    public void enableSaveBtn() {
        if (!isEditing) {
            saveBtn.setText(R.string.save_with_leading_spaces);
            saveBtn.setEnabled(true);
            if (saveBtn.getCompoundDrawables()[2] == null)
                saveBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle_outline, 0);
        } else {
            saveBtn.setEnabled(true);
            saveBtn.setAlpha(1);
        }
    }

    public void disableSaveBtn() {
        if (!isEditing) {
            saveBtn.setText(R.string.select_at_least_5_interests);
            saveBtn.setEnabled(false);
            if (saveBtn.getCompoundDrawables()[2] != null)
                saveBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            saveBtn.setEnabled(false);
            saveBtn.setAlpha(0.4f);
        }
    }

    public boolean isSaveBtnEnabled() {
        return saveBtn.isEnabled();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInterestsInteractionListener) {
            mListener = (OnInterestsInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnInterestsInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnInterestsInteractionListener {
        void onInterestsInteraction();
    }
}

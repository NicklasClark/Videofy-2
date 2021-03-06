package com.cncoding.teazer.tagsAndCategories;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Interests extends Fragment {
//    private static final String CATEGORIES_LIST = "categoriesList";

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.save_interests_btn) ProximaNovaSemiboldButton saveBtn;

//    private String previousTitle;
//    private ActionBar actionBar;
    private InterestsAdapter interestsAdapter;
    private OnInterestsInteractionListener mListener;
    private ArrayList<Category> interestList;

    public Interests() {
        // Required empty public constructor
    }

    public static Interests newInstance() {
        return new Interests();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_interests, container, false);
        ButterKnife.bind(this, rootView);
        interestList = new ArrayList<>();
        interestsAdapter = new InterestsAdapter(interestList, this);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext(), FlexDirection.ROW, FlexWrap.WRAP);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
        flexboxLayoutManager.setJustifyContent(JustifyContent.CENTER);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setLayoutAnimation(new LayoutAnimationController(
                AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in), 0.04f)
        );
        recyclerView.setAdapter(interestsAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.code() == 200) {
                    interestList.clear();
                    interestList.addAll(response.body());
                    interestsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.e("getCategories", t.getMessage());
            }
        });
    }

    @OnClick(R.id.save_interests_btn) public void saveInterests() {
        ApiCallingService.User.updateCategories(new Pojos.User.UpdateCategories(
                getSelectedInterestsToSend(interestsAdapter.getSelectedInterests())), getContext())
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Thank you", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), response.code() + " : " + response.message(), Toast.LENGTH_SHORT).show();
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
        saveBtn.setText(R.string.save_with_leading_spaces);
        saveBtn.setEnabled(true);
        if (saveBtn.getCompoundDrawables()[2] == null)
            saveBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle_outline, 0);
    }

    public void disableSaveBtn() {
        saveBtn.setText(R.string.select_at_least_5_interests);
        saveBtn.setEnabled(false);
        if (saveBtn.getCompoundDrawables()[2] != null)
            saveBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
    }

    public boolean isSaveBtnEnabled() {
        return saveBtn.isEnabled();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInterestsInteractionListener) {
            mListener = (OnInterestsInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInterestsInteractionListener");
        }
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

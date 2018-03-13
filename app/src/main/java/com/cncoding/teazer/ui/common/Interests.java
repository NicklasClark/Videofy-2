package com.cncoding.teazer.ui.common;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.user.UpdateCategories;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;
import com.cncoding.teazer.ui.home.camera.CameraActivity;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;
import static com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment.SELECTED_DATA;

public class Interests extends BaseHomeFragment {

    private static final String ARG_IS_FOR_VIDEO = "isForVideo";
    private static final String ARG_IS_FROM_DISCOVER = "isFromDiscover";
    private static final String ARG_IS_EDITING = "isEditing";
    private static final String ARG_CATEGORIES = "userSelectedCategories";

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.save_interests_btn) ProximaNovaSemiBoldTextView saveBtn;

    private ArrayList<Category> userSelectedCategories;
    private ArrayList<Category> totalCategories;
    private boolean isEditing;
    private boolean isForVideo;
    private boolean isFromDiscover;
    private String selectedData;
    private SparseArray<Category> selectedInterests;
    private Call<ArrayList<Category>> categoriesCall;
    private InterestsAdapter interestsAdapter;
    private OnInterestsInteractionListener mListener;

    public Interests() {}

    public static Interests newInstance(boolean isForVideo, boolean isEditing, List<Category> categories,
                                        String selectedData, boolean isFromDiscover) {
        Interests interests = new Interests();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_FOR_VIDEO, isForVideo);
        args.putBoolean(ARG_IS_FROM_DISCOVER, isFromDiscover);
        args.putBoolean(ARG_IS_EDITING, isEditing);
        args.putString(SELECTED_DATA, selectedData);
        if (isEditing)
            args.putParcelableArrayList(ARG_CATEGORIES, new ArrayList<Parcelable>(categories));
        interests.setArguments(args);
        return interests;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isForVideo = getArguments().getBoolean(ARG_IS_FOR_VIDEO);
            isFromDiscover = getArguments().getBoolean(ARG_IS_FROM_DISCOVER);
            isEditing = getArguments().getBoolean(ARG_IS_EDITING);
            if (isEditing) {
                userSelectedCategories = getArguments().getParcelableArrayList(ARG_CATEGORIES);
                if (userSelectedCategories == null) userSelectedCategories = new ArrayList<>();
            }
            selectedData = getArguments().getString(SELECTED_DATA);
        }
        selectedInterests = new SparseArray<>();
        totalCategories = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = !isForVideo ?
                !isEditing ?
                        inflater.inflate(R.layout.fragment_interests, container, false) :
                        inflater.inflate(R.layout.fragment_edit_interests, container, false) :
                inflater.inflate(R.layout.fragment_video_categories, container, false);
        ButterKnife.bind(this, rootView);

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getTheContext(), FlexDirection.ROW, FlexWrap.WRAP);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
        flexboxLayoutManager.setJustifyContent(isForVideo ? JustifyContent.FLEX_START : JustifyContent.CENTER);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        if (!isForVideo) {
            if (!isEditing) {
                interestsAdapter = new InterestsAdapter(totalCategories, this, null,
                        null, false);
                recyclerView.setAdapter(interestsAdapter);
            } else {
                enableSaveBtn();
                //noinspection unchecked
                new SetInterestsAdapter(this).execute(userSelectedCategories);
            }
        } else {
            recyclerView.setHasFixedSize(true);
            getCategories();
            interestsAdapter = new InterestsAdapter(totalCategories, this, null, selectedData, true);
            recyclerView.setAdapter(interestsAdapter);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getActivity() instanceof CameraActivity) {
            ((CameraActivity) getActivity()).updateBackButton(R.drawable.ic_arrow_back_dark);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (totalCategories != null && totalCategories.isEmpty())
            getCategories();
    }

    private void getCategories() {
        categoriesCall = ApiCallingService.Application.getCategories();

        if (categoriesCall.isExecuted()) categoriesCall.cancel();

        categoriesCall.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.code() == 200) {
                    try {
                        ArrayList<Category> tempList = response.body();
                        if (!tempList.isEmpty()) {
                            totalCategories.clear();
                            totalCategories.addAll(tempList);
                            recyclerView.getAdapter().notifyDataSetChanged();
                            if(!isEditing)
                                changeDoneBtnVisibility(VISIBLE);
                        } else {
                            changeDoneBtnVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    changeDoneBtnVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void changeDoneBtnVisibility(int visibility) {
        if (saveBtn.getVisibility() != visibility) {
            saveBtn.setVisibility(visibility);
        }
    }

    private static class SetInterestsAdapter extends AsyncTask<ArrayList<Category>, Void, SparseBooleanArray> {

        private WeakReference<Interests> reference;

        SetInterestsAdapter(Interests context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected SparseBooleanArray doInBackground(ArrayList<Category>[] categories) {
            SparseBooleanArray booleanArray = new SparseBooleanArray();
            for (Category category : categories[0]) {
                booleanArray.put(category.getCategoryId() - 1, true);
            }
            return booleanArray;
        }

        @Override
        protected void onPostExecute(SparseBooleanArray sparseBooleanArray) {
            reference.get().interestsAdapter = new InterestsAdapter(reference.get().totalCategories,
                    reference.get(), sparseBooleanArray, null, false);
            reference.get().recyclerView.setAdapter(reference.get().interestsAdapter);
            reference.get().getCategories();
        }
    }

    @OnTextChanged(R.id.interests_header) public void searchInterests(CharSequence charSequence) {
        if (isForVideo)
            interestsAdapter.getFilter().filter(charSequence);
    }

    @OnClick(R.id.save_interests_btn) public void saveInterests() {
        selectedInterests = interestsAdapter.getSelectedInterests();
        final ArrayList<Category> categories = new ArrayList<>();
        selectedData = getSelectedInterestsToShow(selectedInterests);
        String resultToSend = getSelectedInterestsToSend(selectedInterests);
        if (!isForVideo) {
            if (selectedInterests.size() >= 5) {
                ApiCallingService.User.updateCategories(new UpdateCategories(resultToSend), getTheContext())
                        .enqueue(new Callback<ResultObject>() {
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                try {
                                    if (response.code() == 200) {
                                        Log.i("Updating interests", "Updated successfully");
                                    } else {
                                        Log.i("Updating interests", response.code() + " : " + response.message());
                                    }
                                    for (int i = 0; i < selectedInterests.size(); i++) {
                                        categories.add(selectedInterests.valueAt(i));
                                    }
                                    mListener.onInterestsInteraction(isFromDiscover, categories);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });
            } else Toast.makeText(getTheContext(), R.string.please_select_5_interests, Toast.LENGTH_SHORT).show();
        }
        else {
            SparseBooleanArray selectedCategoriesArray = new SparseBooleanArray();
            for (int i = 0; i < selectedInterests.size(); i++) {
                selectedCategoriesArray.put(selectedInterests.keyAt(i), true);
            }
            mListener.onInterestsSelected(selectedData, resultToSend, interestsAdapter.getSelectedInterests().size());
        }
    }

    @NonNull private String getSelectedInterestsToShow(SparseArray<Category> sparseArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            stringBuilder.append(sparseArray.valueAt(i).getCategoryName());
            if (i < sparseArray.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    @NonNull private String getSelectedInterestsToSend(SparseArray<Category> sparseArray) {
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
        if (!isForVideo) {
            if (!isEditing) {
                saveBtn.setText(R.string.save);
            } else {
                saveBtn.setAlpha(1);
            }
        }
        saveBtn.setEnabled(true);
    }

    public void disableSaveBtn() {
        if (!isForVideo) {
            if (!isEditing) {
                saveBtn.setText(R.string.select_at_least_5_interests);
                saveBtn.setEnabled(false);
                if (saveBtn.getCompoundDrawables()[2] != null)
                    saveBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            } else {
                saveBtn.setEnabled(false);
                saveBtn.setAlpha(0.4f);
            }
        } else
            saveBtn.setEnabled(false);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (categoriesCall != null) categoriesCall.cancel();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnInterestsInteractionListener {
        void onInterestsInteraction(boolean isFromDiscover, ArrayList<Category> categories);
        void onInterestsSelected(String resultToShow, String resultToSend, int count);
    }
}
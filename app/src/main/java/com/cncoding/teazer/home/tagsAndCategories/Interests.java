package com.cncoding.teazer.home.tagsAndCategories;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.camera.CameraActivity;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.user.UpdateCategories;
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

import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.tagsAndCategories.TagsAndCategoryFragment.SELECTED_DATA;

public class Interests extends BaseFragment {
    private static final String ARG_IS_FOR_VIDEO = "isForVideo";
    private static final String ARG_IS_EDITING = "isEditing";
    private static final String ARG_CATEGORIES = "categories";
//    private static final String CATEGORIES_LIST = "categoriesList";

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.save_interests_btn) ProximaNovaSemiboldTextView saveBtn;

//    private String previousTitle;
//    private ActionBar actionBar;
    private ArrayList<Category> categories;
    private boolean isEditing;
    private boolean isForVideo;
    private String resultToSend;
    private String selectedData;
    private InterestsAdapter interestsAdapter;
    private OnInterestsInteractionListener mListener;
    private ArrayList<Category> interestList;

    public Interests() {
        // Required empty public constructor
    }

    public static Interests newInstance(boolean isForVideo, boolean isEditing, ArrayList<Category> categories,
                                        String selectedData) {
        Interests interests = new Interests();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_FOR_VIDEO, isForVideo);
        args.putBoolean(ARG_IS_EDITING, isEditing);
        args.putString(SELECTED_DATA, selectedData);
        if (isEditing)
            args.putParcelableArrayList(ARG_CATEGORIES, categories);
        interests.setArguments(args);
        return interests;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isForVideo = getArguments().getBoolean(ARG_IS_FOR_VIDEO);
            isEditing = getArguments().getBoolean(ARG_IS_EDITING);
            if (isEditing) {
                categories = getArguments().getParcelableArrayList(ARG_CATEGORIES);
                setHasOptionsMenu(true);
            }
            selectedData = getArguments().getString(SELECTED_DATA);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
        if (!isForVideo) {
            if (!isEditing) {
                rootView = inflater.inflate(R.layout.fragment_interests, container, false);
            } else {
                rootView = inflater.inflate(R.layout.fragment_edit_interests, container, false);
                rootView.findViewById(R.id.root_layout).setBackgroundColor(getResources().getColor(R.color.bgInterests));
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_video_categories, container, false);
        }
        ButterKnife.bind(this, rootView);

        interestList = new ArrayList<>();
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext(), FlexDirection.ROW, FlexWrap.WRAP);
        flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
        flexboxLayoutManager.setJustifyContent(isForVideo ? JustifyContent.FLEX_START : JustifyContent.CENTER);
        recyclerView.setLayoutManager(flexboxLayoutManager);
        if (!isForVideo) {
            if (!isEditing) {
                interestsAdapter = new InterestsAdapter(interestList, this, null,
                        null, false);
                recyclerView.setAdapter(interestsAdapter);
            } else {
                enableSaveBtn();
                //noinspection unchecked
                new SetInterestsAdapter(this).execute(categories);
            }
        } else {
            recyclerView.setHasFixedSize(true);
            getCategories();
            if (categories == null) categories = new ArrayList<>();
            interestsAdapter = new InterestsAdapter(categories, this, null,
                    selectedData, true);
            recyclerView.setAdapter(interestsAdapter);
        }

        recyclerView.setLayoutAnimation(new LayoutAnimationController(
                AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in), 0.04f)
        );
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getActivity() instanceof CameraActivity) {
            ((CameraActivity) getActivity()).updateBackButton(R.drawable.ic_arrow_back);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isEditing && interestList != null && interestList.isEmpty())
            new GetInterests(this).execute();
    }

    private void getCategories() {
        ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.code() == 200) {
                    ArrayList<Category> tempList = response.body();
                    if (!tempList.isEmpty()) {
                        categories.addAll(tempList);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        changeDoneBtnVisibility(VISIBLE);
                    } else {
                        changeDoneBtnVisibility(View.GONE);
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
            return new InterestsAdapter(reference.get().interestList, reference.get(), booleanArray,
                    null, false);
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
                    try {
                        if (response.code() == 200) {
                            reference.get().interestList.clear();
                            reference.get().interestList.addAll(response.body());
                            reference.get().recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
            return null;
        }
    }

    @OnClick(R.id.save_interests_btn) public void saveInterests() {
        final SparseArray<Category> selectedInterests = interestsAdapter.getSelectedInterests();
        final ArrayList<Category> categories = new ArrayList<>();
        selectedData = getSelectedInterestsToShow(selectedInterests);
        resultToSend = getSelectedInterestsToSend(selectedInterests);
        if (!isForVideo) {
            ApiCallingService.User.updateCategories(new UpdateCategories(resultToSend), getContext())
                    .enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            try {
                                if (response.code() == 200) {
                                    Log.d("Updating interests", "Updated successfully");
                                } else {
                                    Log.d("Updating interests", response.code() + " : " + response.message());
                                }
                                for (int i = 0; i < selectedInterests.size(); i++) {
                                    categories.add(selectedInterests.valueAt(i));
                                }
                                mListener.onInterestsInteraction(isEditing, categories);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }
        else
            mListener.onInterestsSelected(selectedData, resultToSend, interestsAdapter.getSelectedInterests().size());
    }

    private String getSelectedInterestsToShow(SparseArray<Category> sparseArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            stringBuilder.append(sparseArray.valueAt(i).getCategoryName());
            if (i < sparseArray.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
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
        if (!isForVideo) {
            if (!isEditing) {
                saveBtn.setText(R.string.save_with_leading_spaces);
                saveBtn.setEnabled(true);
                if (saveBtn.getCompoundDrawables()[2] == null)
                    saveBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle_outline, 0);
            } else {
                saveBtn.setEnabled(true);
                saveBtn.setAlpha(1);
            }
        } else
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
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
        void onInterestsInteraction(boolean isEditing, ArrayList<Category> categories);
        void onInterestsSelected(String resultToShow, String resultToSend, int count);
    }
}

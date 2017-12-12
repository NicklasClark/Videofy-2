package com.cncoding.teazer.home.tagsAndCategories;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.home.camera.CameraActivity;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Friends.CircleList;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.utilities.AuthUtils.getErrorMessage;

public class TagsAndCategoryFragment extends Fragment {

    private static final String ACTION = "action";
    public static final String ACTION_CATEGORIES_FRAGMENT = "categoriesFragment";
    public static final String ACTION_TAGS_FRAGMENT = "tagsFragment";
    public static final String SELECTED_DATA = "selectedData";
//    private static final int CATEGORIES_LIMIT = 5;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.headerTextView) ProximaNovaRegularAutoCompleteTextView headerTextView;
    @BindView(R.id.tags_categories_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tags_categories_save) ProximaNovaSemiboldButton doneBtn;
    @BindView(R.id.no_friends_text_view) ProximaNovaBoldTextView noFriendsTextView;

    private String action;
    private ArrayList<Category> categories;
    private CategoriesAdapter categoriesAdapter;

    private ArrayList<MiniProfile> circles;
    private TagsAdapter tagsAdapter;
    private TagsAndCategoriesInteractionListener listener;
    private boolean nextPage;
    private String selectedData;

    public TagsAndCategoryFragment() {
        // Required empty public constructor
    }

    public static TagsAndCategoryFragment newInstance(String action, String selectedData) {
        TagsAndCategoryFragment fragment = new TagsAndCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, action);
        bundle.putString(SELECTED_DATA, selectedData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getString(ACTION);
            selectedData = getArguments().getString(SELECTED_DATA);
            if (selectedData == null)
                selectedData = "";
        }
        categories = new ArrayList<>();
        circles = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tags_and_categories, container, false);
        ButterKnife.bind(this, rootView);
        prepareRecyclerView();
        switch (action) {
            case ACTION_CATEGORIES_FRAGMENT:
                recyclerView.setHasFixedSize(true);
                getCategories();
                break;
            case ACTION_TAGS_FRAGMENT:
                EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(
                        (LinearLayoutManager) recyclerView.getLayoutManager()) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        if (nextPage)
                            getMyCircle(page);
                    }
                };
                recyclerView.addOnScrollListener(scrollListener);
                getMyCircle(1);
                break;
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getActivity() instanceof CameraActivity) {
            ((CameraActivity) getActivity()).updateBackButton(R.drawable.ic_arrow_back);
        }
    }

    private void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        switch (action) {
            case ACTION_TAGS_FRAGMENT:
                headerTextView.setHint(R.string.tag_your_friends);
                tagsAdapter = new TagsAdapter(getContext(), circles, this, selectedData);
                recyclerView.setAdapter(tagsAdapter);
                break;
            case ACTION_CATEGORIES_FRAGMENT:
                headerTextView.setHint(R.string.select_up_to_5_categories);
                categoriesAdapter = new CategoriesAdapter(categories, this, selectedData);
                recyclerView.setAdapter(categoriesAdapter);
                break;
            default:
                break;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 5) {
                    changeDoneBtnVisibility(INVISIBLE);
                }
                else if (dy < -5) {
                    changeDoneBtnVisibility(VISIBLE);
                }
            }
        });
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
                        noFriendsTextView.setText(R.string.error_fetchng_categories);
                        noFriendsTextView.setVisibility(VISIBLE);
                    }
                } else {
                    changeDoneBtnVisibility(View.GONE);
                    noFriendsTextView.setText(R.string.error_fetchng_categories);
                    noFriendsTextView.setVisibility(VISIBLE);
//                    ViewUtils.showSnackBar(doneBtn, getErrorMessage(response.errorBody()));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<Pojos.Category>> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getMyCircle(final int page) {
        ApiCallingService.Friends.getMyCircle(page, getContext()).enqueue(new Callback<CircleList>() {
            @Override
            public void onResponse(Call<CircleList> call, Response<CircleList> response) {
                if (response.code() == 200) {
                    CircleList circleList = response.body();
                    if (!circleList.getCircles().isEmpty()) {
                        nextPage = circleList.isNextPage();
                        circles.addAll(circleList.getCircles());
                        recyclerView.getAdapter().notifyDataSetChanged();
                        changeDoneBtnVisibility(VISIBLE);
                    } else {
                        changeDoneBtnVisibility(View.GONE);
                        noFriendsTextView.setVisibility(VISIBLE);
                    }
                } else {
                    changeDoneBtnVisibility(View.GONE);
                    noFriendsTextView.setText(getErrorMessage(response.errorBody()));
                    noFriendsTextView.setVisibility(VISIBLE);
//                    ViewUtils.showSnackBar(doneBtn, getErrorMessage(response.errorBody()));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CircleList> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @OnClick(R.id.tags_categories_save) public void getResult() {
        try {
            switch (action) {
                case ACTION_TAGS_FRAGMENT:
                    if (circles != null && circles.size() > 0 && tagsAdapter != null) {
                        new ParseResult(this, ACTION_TAGS_FRAGMENT).execute(tagsAdapter.getSelectedTags());
                    }
                    break;
                case ACTION_CATEGORIES_FRAGMENT:
                    if (categories != null && categories.size() > 0) {
                        new ParseResult(this, ACTION_CATEGORIES_FRAGMENT).execute(categoriesAdapter.getSelectedCategories());
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ParseResult extends AsyncTask<SparseArray<? extends Parcelable>, Void, String[]> {

        private WeakReference<TagsAndCategoryFragment> reference;
        private final String action;

        ParseResult(TagsAndCategoryFragment context, String action) {
            reference = new WeakReference<>(context);
            this.action = action;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected String[] doInBackground(SparseArray<? extends Parcelable>[] sparseArrays) {
            switch (action) {
                case ACTION_TAGS_FRAGMENT:
                    SparseArray<MiniProfile> tagsSparseArray = (SparseArray<MiniProfile>) sparseArrays[0];
                    return new String[] {
                            reference.get().getSelectedTagsToShow(tagsSparseArray),
                            reference.get().getSelectedTagsToSend(tagsSparseArray),
                            String.valueOf(reference.get().getCount(tagsSparseArray))
                    };
                case ACTION_CATEGORIES_FRAGMENT:
                    SparseArray<Category> categorySparseArray = (SparseArray<Category>) sparseArrays[0];
                    return new String[] {
                            reference.get().getSelectedCategoriesToShow(categorySparseArray),
                            reference.get().getSelectedCategoriesToSend(categorySparseArray),
                            String.valueOf(reference.get().getCount(categorySparseArray))
                    };
                default:
                    return null;
            }
        }

        @Override
        protected void onPostExecute(String[] strings) {
            try {
                reference.get().listener.onTagsAndCategoriesInteraction(action, strings[0], strings[1], Integer.parseInt(strings[2]));
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                if (e instanceof NumberFormatException)
                    Log.e("NumberFormatException", "Trying to parse" + strings[2]);
            }
        }
    }

    private int getCount(SparseArray<? extends Parcelable> sparseArray) {
        return sparseArray.size();
    }

    private String getSelectedTagsToShow(SparseArray<MiniProfile> sparseArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            stringBuilder.append(sparseArray.valueAt(i).getFirstName());
            if (i < sparseArray.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    private String getSelectedTagsToSend(SparseArray<MiniProfile> sparseArray) {
        if (sparseArray.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < sparseArray.size(); i++) {
                stringBuilder.append(sparseArray.valueAt(i).getUserId());
                if (i < sparseArray.size() - 1) {
                    stringBuilder.append(",");
                }
            }
            return stringBuilder.toString();
        } else {
            return null;
        }
    }

    private String getSelectedCategoriesToShow(SparseArray<Category> sparseArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            stringBuilder.append(sparseArray.valueAt(i).getCategoryName());
            if (i < sparseArray.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    private String getSelectedCategoriesToSend(SparseArray<Category> sparseArray) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            string.append(sparseArray.valueAt(i).getCategoryId());
            if (i < sparseArray.size() - 1) {
                string.append(",");
            }
        }
        return string.toString();
    }

    public void changeDoneBtnVisibility(int visibility) {
        if (doneBtn.getVisibility() != visibility) {
            doneBtn.setVisibility(visibility);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TagsAndCategoriesInteractionListener) {
            listener = (TagsAndCategoriesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TagsAndCategoriesInteractionListener");
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (getActivity() != null && getActivity() instanceof CameraActivity) {
//            ((CameraActivity) getActivity()).updateBackButton(R.drawable.ic_previous);
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public interface TagsAndCategoriesInteractionListener {
        void onTagsAndCategoriesInteraction(String action, String resultToShow, String resultToSend, int count);
    }
}

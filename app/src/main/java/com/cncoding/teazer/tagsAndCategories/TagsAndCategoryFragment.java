package com.cncoding.teazer.tagsAndCategories;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaBoldTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Friends.CircleList;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;

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
import static com.cncoding.teazer.utilities.AuthUtils.logTheError;

public class TagsAndCategoryFragment extends Fragment {

    private static final String ACTION = "action";
    public static final String ACTION_CATEGORIES_FRAGMENT = "categoriesFragment";
    public static final String ACTION_TAGS_FRAGMENT = "tagsFragment";
//    private static final int CATEGORIES_LIMIT = 5;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.headerTextView) ProximaNovaRegularTextView headerTextView;
    @BindView(R.id.tags_categories_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tags_categories_done) FloatingActionButton doneBtn;
    @BindView(R.id.no_friends_text_view) ProximaNovaBoldTextView noFriendsTextView;

    private String action;
    private ArrayList<Category> categories;
    private CategoriesAdapter categoriesAdapter;

    private ArrayList<MiniProfile> circles;
    private TagsAdapter tagsAdapter;
    private TagsAndCategoriesInteractionListener listener;
    private boolean nextPage;

    public TagsAndCategoryFragment() {
        // Required empty public constructor
    }

    public static TagsAndCategoryFragment newInstance(String action) {
        TagsAndCategoryFragment fragment = new TagsAndCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, action);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getString(ACTION);
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

    private void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        switch (action) {
            case ACTION_TAGS_FRAGMENT:
                headerTextView.setText(R.string.tag_your_friends);
                tagsAdapter = new TagsAdapter(circles, this);
                recyclerView.setAdapter(tagsAdapter);
                break;
            case ACTION_CATEGORIES_FRAGMENT:
                headerTextView.setText(R.string.select_up_to_5_categories);
                categoriesAdapter = new CategoriesAdapter(categories, this, getContext());
                recyclerView.setAdapter(categoriesAdapter);
                break;
            default:
                break;
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 5) {
                    changeVisibility(INVISIBLE);
                }
                else if (dy < -5) {
                    changeVisibility(VISIBLE);
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
                        doneBtn.setVisibility(VISIBLE);
                    } else {
                        doneBtn.setVisibility(View.GONE);
                        noFriendsTextView.setText(R.string.error_fetchng_categories);
                        noFriendsTextView.setVisibility(VISIBLE);
                    }
                } else {
                    doneBtn.setVisibility(View.GONE);
                    noFriendsTextView.setText(R.string.error_fetchng_categories);
                    noFriendsTextView.setVisibility(VISIBLE);
//                    ViewUtils.showSnackBar(doneBtn, getErrorMessage(response.errorBody()));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<Pojos.Category>> call, Throwable t) {
                logTheError("getCategories", t.getMessage());
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
                        doneBtn.setVisibility(VISIBLE);
                    } else {
                        doneBtn.setVisibility(View.GONE);
                        noFriendsTextView.setVisibility(VISIBLE);
                    }
                } else {
                    doneBtn.setVisibility(View.GONE);
                    noFriendsTextView.setText(getErrorMessage(response.errorBody()));
                    noFriendsTextView.setVisibility(VISIBLE);
//                    ViewUtils.showSnackBar(doneBtn, getErrorMessage(response.errorBody()));
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CircleList> call, Throwable t) {
                logTheError("getMyCircle", t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @OnClick(R.id.tags_categories_done) public void getResult() {
        try {
            switch (action) {
                case ACTION_TAGS_FRAGMENT:
                    if (circles != null && circles.size() > 0 && tagsAdapter != null) {
                        listener.onTagsAndCategoriesInteraction(ACTION_TAGS_FRAGMENT,
                                getSelectedTags(tagsAdapter.getSelectedTags()), null, getCount(tagsAdapter.getSelectedTags()));
                    }
                    break;
                case ACTION_CATEGORIES_FRAGMENT:
                    if (categories != null && categories.size() > 0) {
                        listener.onTagsAndCategoriesInteraction(ACTION_CATEGORIES_FRAGMENT,
                                getSelectedCategoriesToShow(categoriesAdapter.getSelectedCategories()),
                                getSelectedCategoriesToSend(categoriesAdapter.getSelectedCategories()),
                                getCount(categoriesAdapter.getSelectedCategories()));
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSelectedTags(SparseArray<MiniProfile> sparseArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            stringBuilder.append(sparseArray.valueAt(i).getFirstName());
            if (i < sparseArray.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    private int getCount(SparseArray<? extends Parcelable> sparseArray) {
        return sparseArray.size();
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

    public void changeVisibility(int visibility) {
        if (doneBtn.getVisibility() != visibility) {
            switch (visibility) {
                case VISIBLE:
                    doneBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.float_up));
                    break;
                case INVISIBLE:
                    doneBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sink_down));
                    break;
                default:
                    break;
            }
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

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface TagsAndCategoriesInteractionListener {
        void onTagsAndCategoriesInteraction(String action, String resultToShow, String resultToSend, int count);
    }
}

package com.cncoding.teazer.camera.upload;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.cncoding.teazer.R;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TagsAndCategoryFragment extends Fragment {

    private static final String ACTION = "action";
    public static final String ACTION_CATEGORIES_FRAGMENT = "categoriesFragment";
    public static final String ACTION_TAGS_FRAGMENT = "tagsFragment";

    @BindView(R.id.tags_categories_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.tags_categories_done) FloatingActionButton doneBtn;

    private String action;
    private ArrayList<Category> categories;
    private CategoriesAdapter categoriesAdapter;

    private ArrayList<MiniProfile> circles;
    private TagsAdapter tagsAdapter;
    private TagsAndCategoriesInteractionListener listener;

    public TagsAndCategoryFragment() {
        // Required empty public constructor
    }

    public static TagsAndCategoryFragment newInstance(String action, ArrayList<? extends Parcelable> args) {
        TagsAndCategoryFragment fragment = new TagsAndCategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ACTION, action);
        bundle.putParcelableArrayList(action, args);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            action = getArguments().getString(ACTION);
            if (action != null) {
                switch (action) {
                    case ACTION_TAGS_FRAGMENT:
                        circles = getArguments().getParcelableArrayList(ACTION_TAGS_FRAGMENT);
                        break;
                    case ACTION_CATEGORIES_FRAGMENT:
                        categories = getArguments().getParcelableArrayList(ACTION_CATEGORIES_FRAGMENT);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tags_and_categories, container, false);
        ButterKnife.bind(this, rootView);
        prepareRecyclerView();
        return rootView;
    }

    private void prepareRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        switch (action) {
            case ACTION_TAGS_FRAGMENT:
                tagsAdapter = new TagsAdapter(circles, this);
                recyclerView.setAdapter(tagsAdapter);
                break;
            case ACTION_CATEGORIES_FRAGMENT:
                categoriesAdapter = new CategoriesAdapter(categories, this);
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

    @OnClick(R.id.tags_categories_done) public void getResult() {
        switch (action) {
            case ACTION_TAGS_FRAGMENT:
                listener.onTagsAndCategoriesInteraction(getSelectedTags(tagsAdapter.getSelectedTags()));
                break;
            case ACTION_CATEGORIES_FRAGMENT:
                listener.onTagsAndCategoriesInteraction(getSelectedCategories(categoriesAdapter.getSelectedCategories()));
                break;
            default:
                break;
        }
        listener.onTagsAndCategoriesInteraction(getSelectedTags(tagsAdapter.getSelectedTags()));
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

    private String getSelectedCategories(SparseArray<Category> sparseArray) {
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
        void onTagsAndCategoriesInteraction(String result);
    }
}

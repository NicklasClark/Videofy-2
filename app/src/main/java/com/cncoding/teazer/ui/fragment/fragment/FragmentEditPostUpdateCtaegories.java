package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.EditPostUpdateCategoriesAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.friends.PublicProfile;
import com.cncoding.teazer.model.post.PostDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 20/12/17.
 */

public class FragmentEditPostUpdateCtaegories extends Fragment {


    Context context;
    static final String USER_PROFILE = "postdetails";
    private ArrayList<Category> categories;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    EditPostUpdateCategoriesAdapter adapter;
    PublicProfile userProfile;
    ArrayList<Category> usercategoryList;
    @BindView(R.id.tags_categories_save)
    FloatingActionButton tags_categories_done;
    StringBuilder categoryId;
    StringBuilder categoryName;
    PostDetails postDetails;
    UpdateCategoriesResponse mListener;
    @BindView(R.id.headerTextView)
    ProximaNovaRegularTextView headerTextView;

    public static FragmentEditPostUpdateCtaegories newInstance(PostDetails postDetails) {
        FragmentEditPostUpdateCtaegories fragment = new FragmentEditPostUpdateCtaegories();
        Bundle args = new Bundle();
        args.putParcelable("Categ",postDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            postDetails=bundle.getParcelable("Categ");
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentEditPostUpdateCtaegories.UpdateCategoriesResponse) {
            mListener = (FragmentEditPostUpdateCtaegories.UpdateCategoriesResponse)  context;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_categories, container, false);
        ButterKnife.bind(this, view);
        context = container.getContext();

        headerTextView.setHint(R.string.select_up_to_5_categories);
        // usercategoryList=new ArrayList<>();
//        usercategoryList.addAll(postDetails.getCategories());
        //Toast.makeText(context, selecCateg.getCategories().get(1).getCategoryName()+ selecCateg.getCategories().get(2).getCategoryName(), Toast.LENGTH_SHORT).show();

        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        tags_categories_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<Integer> selectedCategoriesList = adapter.getSelectedCategories();
                List<String> selectedCategoriesListName = adapter.getSelectedCategoriesName();
                categoryId = new StringBuilder();
                categoryName=new StringBuilder();

                for (int i = 0; i < selectedCategoriesList.size(); i++) {
                    Log.d("getID", String.valueOf(selectedCategoriesList.get(i)));

                    categoryId.append(selectedCategoriesList.get(i));
                    categoryName.append(selectedCategoriesListName.get(i));
                    if (i == selectedCategoriesList.size() - 1) {
                    } else {
                        categoryId.append(",");
                        categoryName.append(",");
                    }
                }
                 mListener.updatedCategoriesResponse(categoryId.toString(),categoryName.toString());
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getCategories();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public void getCategories() {
        ApiCallingService.Application.getCategories(getContext()).enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        categories = response.body();
                        adapter = new EditPostUpdateCategoriesAdapter(categories, context, postDetails.getCategories());
                        recyclerView.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.e("getCategories", t.getMessage());
            }
        });
    }

    public  interface  UpdateCategoriesResponse
    {
       public void updatedCategoriesResponse(String categ,String categoryNmae);
    }
}
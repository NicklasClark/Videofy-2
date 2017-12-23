package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ChangeCategoriesAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.friends.PublicProfile;
import com.cncoding.teazer.model.user.UpdateCategories;
import com.cncoding.teazer.model.user.UserProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 
 * Created by farazhabib on 03/12/17.
 */

public class FragmentChangeCategories extends Fragment{


    Context context;
    static final String USER_PROFILE = "postdetails";
    private ArrayList<Category> categories;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ChangeCategoriesAdapter adapter;
    PublicProfile userProfile;
    ArrayList<Category> usercategoryList;
    @BindView(R.id.tags_categories_save)
    FloatingActionButton tags_categories_done;
    StringBuilder categoryId;
    @BindView(R.id.headerTextView)
    ProximaNovaRegularTextView headerTextView;
    @BindView(R.id.loader)
    GifTextView loader;
    @BindView(R.id.layout)
    RelativeLayout layout;

    public static FragmentChangeCategories newInstance() {
        FragmentChangeCategories fragment= new FragmentChangeCategories();
        Bundle args = new Bundle();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_categories, container, false);
        ButterKnife.bind(this,view);
        context=container.getContext();
        recyclerView=view.findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        headerTextView.setHint(R.string.select_minimum_categories);

        tags_categories_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> selectedCategoriesList= adapter.getSelectedCategories();
                categoryId=new StringBuilder();
                for(int i=0;i<selectedCategoriesList.size();i++)
               {
                   categoryId.append(selectedCategoriesList.get(i));
                   if (i == selectedCategoriesList.size() - 1) {
                   } else {
                       categoryId.append(",");
                   }
               }
                UpdateCategories categories= new UpdateCategories(categoryId.toString());
                updateCategories(categories);


            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        getProfileInfo();

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    public void getCategories(final ArrayList<Category> usercategoryList) {



        ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
             try {
                 if (response.code() == 200) {
                     if (response.body() != null) {
                         categories = response.body();
                         adapter = new ChangeCategoriesAdapter(categories, context, usercategoryList);
                         recyclerView.setAdapter(adapter);
                         layout.setVisibility(View.VISIBLE);
                         loader.setVisibility(View.GONE);
                     }
                 }
             }catch(Exception e)
             {
                 layout.setVisibility(View.VISIBLE);
                 loader.setVisibility(View.GONE);
             }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.e("getCategories", t.getMessage());
                layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
        });
    }

    public void getProfileInfo()
    {

        layout.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {

                try {
                    usercategoryList = response.body().getUserProfile().getCategories();
                    getCategories(usercategoryList);

                } catch (Exception e) {
                    layout.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                t.printStackTrace();
                layout.setVisibility(View.VISIBLE);
                loader.setVisibility(View.GONE);
            }
        });
    }
    public void updateCategories(UpdateCategories categories)
    {
        loader.setVisibility(View.VISIBLE);
        ApiCallingService.User.updateCategories(categories,context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        if(response.body().getStatus()) {
                            Toast.makeText(context,"Categories have been updated successfully",Toast.LENGTH_SHORT).show();
                            if (isAdded()) {
                                getActivity().onBackPressed();
                            }
                        }
                    } catch (Exception e) {
                        loader.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                loader.setVisibility(View.VISIBLE);
            }
        });
    }

}

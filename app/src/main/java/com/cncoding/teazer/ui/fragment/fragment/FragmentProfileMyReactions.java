package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.model.react.Reactions;
import com.cncoding.teazer.model.react.ReactionsList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by MOHD ARIF on 07-11-2017.
 */

public class FragmentProfileMyReactions extends Fragment {

    CircularAppCompatImageView menuitem;
    RecyclerView recyclerView;
    ProfileMyReactionAdapter profileMyReactionAdapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    List<Reactions>list;
    int page;
    ProximaNovaRegularTextView alert1;
    public static FragmentProfileMyReactions newInstance(int page) {
        return new FragmentProfileMyReactions();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_myreactions, container, false);
        context=container.getContext();
        recyclerView=view.findViewById(R.id.recycler_view);
        alert1=view.findViewById(R.id.alert1);

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        list=new ArrayList<>();
        page=1;
        getReactions();
    }
    public void getReactions() {
        ApiCallingService.React.getMyReactions(page,context).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {
                if (response.code() == 200) {
                    try {
//                        response.body().getReactions();
                        if (response.body().getReactions() == null||response.body().getReactions().size()==0) {

                            alert1.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        else {
                            boolean next=response.body().isNextPage();
                            list.addAll(response.body().getReactions());
                            profileMyReactionAdapter = new ProfileMyReactionAdapter(context, list);
                            recyclerView.setAdapter(profileMyReactionAdapter);
                            if(next)
                            {
                                page++;
                                getReactions();
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, "Oops Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(context, "Oops Something went wrong, Please try again", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t)
            {
                Toast.makeText(context, "Oops Something went wrong, Please try again", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

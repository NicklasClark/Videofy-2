package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.os.Bundle;
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
import com.cncoding.teazer.model.profile.reaction.ProfileReaction;
import com.cncoding.teazer.model.profile.reaction.Reaction;

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
    List<Reaction>list;
    public static FragmentProfileMyReactions newInstance(int page) {

        return new FragmentProfileMyReactions();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_myreactions, container, false);
        context=container.getContext();
        recyclerView=view.findViewById(R.id.recycler_view);

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getProfileVideos();
    }

    public void getProfileVideos() {
        list=new ArrayList<>();
        ApiCallingService.React.getMyReaction(1,context).enqueue(new Callback<ProfileReaction>() {
            @Override
            public void onResponse(Call<ProfileReaction> call, Response<ProfileReaction> response) {

                if (response.code() == 200) {
                    try {
                        list = response.body().getReactions();
                        if (list == null || list.size() == 0) {
                       // Toast.makeText(context, "No Reactions found", Toast.LENGTH_LONG).show();
                        }
                        else {
                            profileMyReactionAdapter = new ProfileMyReactionAdapter(context, list);
                            recyclerView.setAdapter(profileMyReactionAdapter);
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
            public void onFailure(Call<ProfileReaction> call, Throwable t)
            {
                Toast.makeText(context, "Oops Something went wrong, Please try again", Toast.LENGTH_LONG).show();
            }
        });


    }
}

package com.cncoding.teazer.ui.home.profile.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.post.LikedUser;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.post.detailspage.FragmentLikedUser;
import com.cncoding.teazer.ui.home.profile.adapter.LikedUserAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by farazhabib on 06/02/18.
 */

public class FragmentReactionLikedUser extends BaseFragment {

    int postId;
    PostDetails postDetails;
    Context context;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.btnClose)
    AppCompatImageView btnClose;
    RecyclerView.LayoutManager layoutManager;
    List<LikedUser> likedUsersList;
    LikedUserAdapter likedUserAdapter;
    boolean next;


    public static FragmentLikedUser newInstance(PostDetails postDetails) {
        FragmentLikedUser fragment = new FragmentLikedUser();
        Bundle args = new Bundle();

        args.putString("PostId", String.valueOf(postDetails.getPostId()));
        args.putParcelable("PostDetails", postDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            postId = Integer.parseInt(bundle.getString("PostId"));
            postDetails = bundle.getParcelable("PostDetails");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_user, container, false);
        context = getContext();
        ButterKnife.bind(this, view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        likedUsersList = new ArrayList<>();
        likedUserAdapter = new LikedUserAdapter(context, likedUsersList, postDetails, this);
        recyclerView.setAdapter(likedUserAdapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (next) {
                    getLikedUser(postId, page);
                }

            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((AppCompatActivity) context).getSupportFragmentManager().popBackStackImmediate();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getLikedUser(postId, 1);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getLikedUser(final int postId, final int pageId) {

        ApiCallingService.Friends.getLikedUsers(postId, pageId, context).enqueue(new Callback<LikedUserList>() {

            @Override
            public void onResponse(Call<LikedUserList> call, Response<LikedUserList> response) {

                try {

                    Log.d("response", response.message());
                    likedUsersList.addAll(response.body().getLikedUsers());

                    if ((likedUsersList != null && likedUsersList.size() != 0) || pageId != 1) {
                        next = response.body().getNextPage();
                        recyclerView.getAdapter().notifyDataSetChanged();
                        likedUserAdapter.notifyItemRangeInserted(likedUserAdapter.getItemCount(), likedUsersList.size() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LikedUserList> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });
    }
}

package com.cncoding.teazer.ui.home.post.detailspage;


import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.ui.customviews.common.CustomLinearLayoutManager;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;
import com.cncoding.teazer.ui.home.profile.adapter.LikedUserAdapter;

import org.jetbrains.annotations.Contract;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by farazhabib on 29/12/17.
 */

public class FragmentLikedUser extends BaseHomeFragment {

    private static final String ID = "id";
    private static final String LIKED_USERS_OF = "likedUserOf";
    public static final int LIKED_USERS_OF_POSTS = 1;
    public static final int LIKED_USERS_OF_REACTION = 2;
    public static final int LIKED_USERS_OF_PROFILE = 3;
    public static final int SELF = -1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIKED_USERS_OF_POSTS, LIKED_USERS_OF_REACTION, LIKED_USERS_OF_PROFILE})
    @interface LikedUserOf {}

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    private int id;
    @LikedUserOf private int likedUserOf;
    private LikedUserAdapter likedUserAdapter;

    public static FragmentLikedUser newInstance(int id, @LikedUserOf int likedUserOf) {
        FragmentLikedUser fragment = new FragmentLikedUser();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        args.putInt(LIKED_USERS_OF, likedUserOf);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ID);
            likedUserOf = getArguments().getInt(LIKED_USERS_OF);
        }
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liked_user, container, false);
        context = getContext();
        ButterKnife.bind(this, view);

        CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        likedUserAdapter = new LikedUserAdapter(this);
        recyclerView.setAdapter(likedUserAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page) {
                        getLikedUser(page);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getLikedUser(1);
    }

    public void getLikedUser(int page) {
        switch (likedUserOf) {
            case LIKED_USERS_OF_POSTS:
                getLikedUsersOfPost(page);
                break;
            case LIKED_USERS_OF_PROFILE:
                getLikedUsersOfProfile(page);
                break;
            case LIKED_USERS_OF_REACTION:
                getLikedUsersOfReaction(page);
                break;
            default:
                break;
        }
    }

    private void getLikedUsersOfPost(final int page) {
        ApiCallingService.Posts.getLikedUsers(id, page, context).enqueue(likedUserCallback(page));
    }

    private void getLikedUsersOfReaction(int page) {
        ApiCallingService.React.getLikedUsersReaction(id, page, context).enqueue(likedUserCallback(page));
    }

    private void getLikedUsersOfProfile(int page) {
        if (id == SELF) getMyProfileLikedUsers(page);
        else getOtherProfileLikedUser(page);
    }

    private void getMyProfileLikedUsers(int page) {
        ApiCallingService.User.getLikedUserProfile(page, context).enqueue(likedUserCallback(page));
    }

    private void getOtherProfileLikedUser(int page) {
        ApiCallingService.Friends.getLikedUserFriendProfile(id, page, context).enqueue(likedUserCallback(page));
    }

    @NonNull @Contract(pure = true)
    private Callback<LikedUserList> likedUserCallback(final int page) {
        return new Callback<LikedUserList>() {
            @Override
            public void onResponse(Call<LikedUserList> call, Response<LikedUserList> response) {
                if (isAdded()) {
                    handleResponse(page, response.body());
                }
            }

            @Override
            public void onFailure(Call<LikedUserList> call, Throwable t) {
                t.printStackTrace();
                if (isAdded()) {
                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void handleResponse(int page, LikedUserList likedUserList) {
        try {
            if ((likedUserList.getLikedUsers() != null && !likedUserList.getLikedUsers().isEmpty())) {
                is_next_page = likedUserList.getNextPage();
                if (page == 1)
                    likedUserAdapter.updatePosts(likedUserList.getLikedUsers());
                else likedUserAdapter.addPosts(page, likedUserList.getLikedUsers());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        }
    }
}
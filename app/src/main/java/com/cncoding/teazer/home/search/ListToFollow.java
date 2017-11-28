package com.cncoding.teazer.home.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListToFollow extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.user_search) ProximaNovaRegularAutoCompleteTextView searSearchView;
    @BindView(R.id.list) RecyclerView recyclerView;

    private String mParam1;
    private String mParam2;

    private ListToFollow.OnFragmentInteractionListener mListener;
    private ArrayList<Pojos.MiniProfile> usersList;
    private ListToFollow.UserListAdapter userListAdapter;

    public ListToFollow() {
        // Required empty public constructor
    }

    public static ListToFollow newInstance(String param1, String param2) {
        ListToFollow fragment = new ListToFollow();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, rootView);
        usersList = new ArrayList<>();
        getUsersToFollow(1);
        return rootView;
    }

    private void getUsersToFollow(final int page) {
        ApiCallingService.Friends.getUsersListToFollow(page, getContext()).enqueue(new Callback<Pojos.Friends.UsersList>() {
            @Override
            public void onResponse(Call<Pojos.Friends.UsersList> call, Response<Pojos.Friends.UsersList> response) {
                if (response.code() == 200) {
//                            Posts came! now fetching the posts and checking if next page is true (More posts are available) or not.
                    usersList.addAll(response.body().getUsers());
                    if (response.body().isNextPage()) {
//                                More posts are available, so incrementing the page number and re-calling this method.
                        getUsersToFollow(page + 1);
                    }
                    else {
//                                No more posts available. So populating the posts in the mostPopularList.
                        if (usersList.size() > 0) {
                            recyclerView.setLayoutManager(
                                    new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            userListAdapter = new ListToFollow.UserListAdapter(usersList, getContext());
                            recyclerView.setAdapter(userListAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Pojos.Friends.UsersList> call, Throwable t) {
            }
        });
    }

    /**
     * {@link RecyclerView.Adapter} that can display {@link Pojos.MiniProfile}
     */
    public class UserListAdapter extends RecyclerView.Adapter<ListToFollow.UserListAdapter.ViewHolder> {

        private final List<Pojos.MiniProfile> users;
        private Context context;

        UserListAdapter(List<Pojos.MiniProfile> users, Context context) {
            this.users = users;
            this.context = context;
        }

        @Override
        public ListToFollow.UserListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
            return new ListToFollow.UserListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ListToFollow.UserListAdapter.ViewHolder holder, int position) {
            final Pojos.MiniProfile miniProfile = users.get(position);

            if (miniProfile.hasProfileMedia())
                Glide.with(context)
                        .load(miniProfile.getProfileMedia().getThumbUrl())
                        .placeholder(R.drawable.ic_user_male_dp_small)
                        .crossFade()
                        .into(holder.profilePic);
            holder.profileName.setText(miniProfile.getFirstName() + " " + miniProfile.getLastName());
            holder.username.setText("@" + miniProfile.getUserName());

            View.OnClickListener viewUserDetails = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.addFriendBtn.setEnabled(false);
                    ApiCallingService.Friends.sendJoinRequestByUserId(miniProfile.getUserId(), context).enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            if (response.code() == 200) {
                                holder.addFriendBtn.setText(R.string.added);
                                if (!response.body().getStatus()) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            holder.addFriendBtn.setText(R.string.add);
                                            holder.addFriendBtn.setEnabled(true);
                                        }
                                    }, 1500);
                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        usersList.remove(holder.getAdapterPosition());
                                        userListAdapter.notifyItemRemoved(holder.getAdapterPosition());
                                    }
                                }, 1000);
                            } else
                                Toast.makeText(getContext(), response.code() + " : " + response.message(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                        }
                    });
                }
            };
//            View.OnClickListener viewProfile = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.onUserAdapterInteraction(ACTION_VIEW_PROFILE, miniProfile);
//                }
//            };
            holder.addFriendBtn.setOnClickListener(viewUserDetails);
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

//            @BindView(R.id.root_layout) LinearLayout rootLayout;
            @BindView(R.id.profile_name) ProximaNovaRegularTextView profileName;
            @BindView(R.id.username) ProximaNovaRegularTextView username;
            @BindView(R.id.username_dp) CircularAppCompatImageView profilePic;
            @BindView(R.id.add_friend_btn) ProximaNovaBoldButton addFriendBtn;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + profileName.getText();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListToFollow.OnFragmentInteractionListener) {
            mListener = (ListToFollow.OnFragmentInteractionListener) context;
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnUploadFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
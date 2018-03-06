package com.cncoding.teazer.ui.home.profile.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.PublicProfile;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.user.PrivateProfile;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.SignPainterTextView;
import com.cncoding.teazer.ui.home.profile.adapter.FollowersCreationAdapter;
import com.cncoding.teazer.ui.home.profile.fragment.ReportUserDialogFragment;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//910
public class OthersProfileFragment extends BaseFragment {

    private static final String ARG_ID = "UserID";
    private static final String ARG_IDENTIFIER = "Usertype";
    private static final String ARG_USERNAME = "UserName";
    private static final String ARG_NOTIFICATION_ID = "notifiaction";
    @BindView(R.id.username_title)
    SignPainterTextView _usernameTitle;
    @BindView(R.id.creations)
    TextView _creations;

    @BindView(R.id.layoutDetail)
    RelativeLayout layoutDetail;
    @BindView(R.id.layoutDetail2)
    RelativeLayout layoutDetail2;
    @BindView(R.id.layoutDetail3)
    RelativeLayout layoutDetail3;
    @BindView(R.id.username)
    ProximaNovaRegularCheckedTextView _name;
    @BindView(R.id.following)
    TextView _following;
    @BindView(R.id.followers)
    TextView _followers;
    @BindView(R.id.recycler_view)
    RecyclerView _recycler_view;
    @BindView(R.id.btnfollow)
    Button _btnfollow;
    @BindView(R.id.unHideAll)
    Button _unHideAll;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout layout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.nomessagetxt)
    TextView nomessagetxt;
    @BindView(R.id.background_profile)
    ImageView background_profile;
    @BindView(R.id.profile_id)
    ImageView profile_id;
    @BindView(R.id.hobby)
    ProximaNovaRegularCheckedTextView hobby;
    @BindView(R.id.loader)
    DynamicProgress loader;
    @BindView(R.id.blur_bacground)
    CoordinatorLayout blur_bacground;
//893
    Context context;
    ProfileInfo profileInfo;

    public static final int PRIVATE_ACCOUNT = 1;
    public static final int PUBLIC_ACCOUNT = 2;
    public static final boolean FRIENDS = true;
    public static final boolean NOTFRIEND = false;
    CircularAppCompatImageView menu;
    List<PostDetails> list;
    FollowersCreationAdapter followerCreationAdapter;
    RecyclerView.LayoutManager layoutManager;
    @BindView(R.id.userCreationTitle)

    ProximaNovaBoldTextView userCreationTitle;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private static final int BLOCK_STATUS = 1;
    private static final int UNBLOCK_STATUS = 2;

    int accountType;
    boolean requestRecieved;
    String status;
    boolean hassentrequest;
    boolean isBlockedyou;
    boolean isfollower;
    boolean isfollowing;
    boolean youBlocked;
    Boolean isHideAllPost;

    String firstName;
    private String userProfileThumbnail;
    private String userProfileUrl;
    private int requestId;
    String details;

    String username;
    int followerfollowingid;
    String userType;
    String getNotificationType;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    boolean next;


//2028
    public static OthersProfileFragment newInstance(String id, String identifier, String username) {
        OthersProfileFragment othersProfileFragment = new OthersProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, id);
        bundle.putString(ARG_IDENTIFIER, identifier);
        bundle.putString(ARG_USERNAME, username);
        othersProfileFragment.setArguments(bundle);
        return othersProfileFragment;

    }


    public static OthersProfileFragment newInstance3(String id, String notificationId) {
        OthersProfileFragment othersProfileFragment = new OthersProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, id);
        bundle.putString(ARG_NOTIFICATION_ID, notificationId);
        othersProfileFragment.setArguments(bundle);
        return othersProfileFragment;

    }

    public static OthersProfileFragment newInstance2(String id, String identifier, String username) {
        OthersProfileFragment othersProfileFragment = new OthersProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, id);
        bundle.putString(ARG_IDENTIFIER, identifier);
        bundle.putString(ARG_USERNAME, username);
        othersProfileFragment.setArguments(bundle);
        return othersProfileFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            followerfollowingid = Integer.parseInt(getArguments().getString(ARG_ID));
            userType = getArguments().getString(ARG_IDENTIFIER);
            username = getArguments().getString(ARG_USERNAME);
            getNotificationType = getArguments().getString(ARG_NOTIFICATION_ID);
            setHasOptionsMenu(true);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_others_profile, container, false);
        ButterKnife.bind(this, view);
        context = container.getContext();
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        menu = view.findViewById(R.id.menu);
        layoutManager = new LinearLayoutManager(context);
        _recycler_view.setLayoutManager(layoutManager);

        _btnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (_btnfollow.getText().equals("Accept")) {

                    acceptUser(requestId,true);
                }
                if (_btnfollow.getText().equals("Follow")) {

                    if(requestRecieved)
                    {
                        acceptUser(requestId,false);

                    }
                    else {
                        followUser(followerfollowingid, context);

                    }

                }

                if (_btnfollow.getText().equals(context.getString(R.string.following))) {

                    _btnfollow.setText("Follow");

                    unFollowUser(followerfollowingid, context);

                }
                if (_btnfollow.getText().equals(context.getString(R.string.requested))) {

                    cancelRequest(followerfollowingid, context);
                }
                if (_btnfollow.getText().equals("Unblock")) {

                    blockUnblockUsers(followerfollowingid, UNBLOCK_STATUS);

                }
            }
        });

        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountType == 1) {
                    if (isfollowing == true) {
                        navigation.pushFragment(FollowingListFragment.newInstance(String.valueOf(followerfollowingid), "Other"));
                    } else if (hassentrequest == true) {

                        if (requestRecieved == true) {
                            navigation.pushFragment(FollowingListFragment.newInstance(String.valueOf(followerfollowingid), "Other"));
                        } else {
                            Toast.makeText(context, "You can not view following List now", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(context, "You can not view following List now", Toast.LENGTH_SHORT).show();
                } else {
                    navigation.pushFragment(FollowingListFragment.newInstance(String.valueOf(followerfollowingid), "Other"));
                }
            }

        });
        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (accountType == 1) {

                    if (isfollowing) {
                        navigation.pushFragment(FollowersListFragment.newInstance(String.valueOf(followerfollowingid), "Other"));

                    } else if (hassentrequest == true) {

                        if (requestRecieved == true) {
                            navigation.pushFragment(FollowersListFragment.newInstance(String.valueOf(followerfollowingid), "Other"));
                        } else {
                            Toast.makeText(context, "You can not view follower List now", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "You can not view follower List", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    navigation.pushFragment(FollowersListFragment.newInstance(String.valueOf(followerfollowingid), "Other"));
                }

            }
        });


        hobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentHobbyDetails reportPostDialogFragment = FragmentHobbyDetails.newInstance(details,userProfileUrl);
//                if (fragmentManager != null) {
//                    reportPostDialogFragment.show(fragmentManager, "fragment_report_post");
//
//                }


            }
        });

        profile_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OpenProfilePicActivity.class);
                intent.putExtra("Image", userProfileUrl);
                intent.putExtra("candelete",false);

                Pair<View, String> p1 = Pair.create((View)profile_id, "profile");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), profile_id, "profile");
                startActivity(intent, options.toBundle());
            }
        });


        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (next) {
                    if (page > 2) {
                        loader.setVisibility(View.VISIBLE);
                    }
                    getProfileVideos(followerfollowingid, page);


                }

            }
        };

        _recycler_view.addOnScrollListener(endlessRecyclerViewScrollListener);


        _unHideAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.support.v7.app.AlertDialog.Builder(context)
                        .setTitle(R.string.unhiding_videos)
                        .setMessage(R.string.unhide_video_confirm)
                        .setPositiveButton(context.getString(R.string.yes_unhide_ALl), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {

                                unHideAllVideos(followerfollowingid);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        })
                        .show();

            }
        });



        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        getProfileInformation(followerfollowingid);
        followerCreationAdapter = new FollowersCreationAdapter(context, list);
        _recycler_view.setAdapter(followerCreationAdapter);
    }

    private void unHideAllVideos(final int userId) {

        ApiCallingService.Posts.getAllHiddenVideosList(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {

                        if(response.body().getStatus())
                        {
                            Toast.makeText(context,"Now you can see the videos those previously hidden",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                blur_bacground.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
            }
        });
    }

    public void getProfileInformation(final int followersid) {
        blur_bacground.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        ApiCallingService.Friends.getOthersProfileInfo(followersid, context).enqueue(new Callback<ProfileInfo>() {
            @Override
            public void onResponse(Call<ProfileInfo> call, Response<ProfileInfo> response) {
                if (response.code() == 200) {
                    try {
                        ProfileInfo profileInfo = response.body();
                        int follower = profileInfo.getFollowers();
                        int following = profileInfo.getFollowings();
                        int totalvideos = profileInfo.getTotalVideos();
                        hassentrequest = profileInfo.getFollowInfo().getRequestSent();
                        requestRecieved = profileInfo.getFollowInfo().isRequestReceived();
                        isBlockedyou = profileInfo.getFollowInfo().getIsBlockedYou();
                        isfollower = profileInfo.getFollowInfo().getFollower();
                        isfollowing = profileInfo.getFollowInfo().getFollowing();
                        youBlocked = profileInfo.getFollowInfo().getYouBlocked();
                        isHideAllPost=profileInfo.getIsHidedAllPosts();
                        if(requestRecieved) {
                            requestId = profileInfo.getFollowInfo().getRequestId();
                        }

                        if(isHideAllPost) {
                            _unHideAll.setVisibility(View.VISIBLE);
                        }

                        _followers.setText(follower + " Followers");
                        _following.setText(following + " Following");
                        _creations.setText(totalvideos + " Creations");

                        if (response.body().getPrivateProfile() == null) {
                            PublicProfile publicProfile = response.body().getPublicProfile();
                            userCreationTitle.setText("Creations of " + publicProfile.getFirstName());
                            username = publicProfile.getUserName();
                            String firstName = publicProfile.getFirstName();
                            String lastName = publicProfile.getLastName();
                            String firstnamelastname=firstName+" "+lastName;
                            details = publicProfile.getDescription();
                            int gender = publicProfile.getGender();

                            accountType = publicProfile.getAccountType();
                            _usernameTitle.setText(username);
                            _name.setText(firstnamelastname);
                            if (details == null || details.equals("")) {
                                hobby.setText("");
                            } else {
                                hobby.setText(details);
                            }
                            Boolean hasProfileMedia = publicProfile.getHasProfileMedia();
                            if (hasProfileMedia == true) {
                                userProfileThumbnail = publicProfile.getProfileMedia().getThumbUrl();
                                userProfileUrl = publicProfile.getProfileMedia().getMediaUrl();
                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.material_flat);
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @SuppressWarnings("ResourceType")
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                                        collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                                        collapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryDark);
                                    }
                                });
                            }

                            if (userProfileUrl != null) {
                                Glide.with(context)
                                        .load(Uri.parse(userProfileUrl))
                                        .into(profile_id);
                                profileBlur(userProfileUrl);
                            }
                            else
                            {
                                if(gender==1) { Glide.with(context)
                                        .load(R.drawable.ic_user_male_dp)
                                        .into(profile_id);
                                    profileBlur(userProfileUrl);}
                                else{
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(profile_id);
                                    profileBlur(userProfileUrl);}
                            }
                            if (youBlocked) {
                                _btnfollow.setText("Unblock");
                                layoutDetail2.setVisibility(View.VISIBLE);
                                layoutDetail.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);

                            } else {
                                if (isfollowing) {

                                    if (requestRecieved == true) {
                                        setActionButtonText(context, _btnfollow, R.string.accept);
                                    } else {

                                        setActionButtonText(context, _btnfollow, R.string.following);
                                    }
                                } else {

                                    if (hassentrequest == true) {

                                        if (requestRecieved == true) {
                                           // requestId = profileInfo.getFollowInfo().getRequestId();
                                            setActionButtonText(context, _btnfollow, R.string.accept);

                                        } else {
                                            setActionButtonText(context, _btnfollow, R.string.requested);
                                        }
                                    } else {

                                        if (requestRecieved == true && isfollower) {

                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                        else if(requestRecieved == true && !isfollower)
                                        {
                                            setActionButtonText(context, _btnfollow, R.string.accept);
                                        }
                                        else if(requestRecieved == false && isfollower)
                                        {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                        else if(requestRecieved == false)
                                        {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                    }
                                    layout.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                                if (accountType == 2) {
                                    getProfileVideos(followersid,1);
                                }
                                else {
                                    if (isfollowing) {
                                        getProfileVideos(followersid,1);

                                    }else {
                                        layoutDetail2.setVisibility(View.VISIBLE);
                                        layoutDetail.setVisibility(View.GONE);
                                    }

                                }
                            }

                        } else if (response.body().getPublicProfile() == null) {
                          //  Toast.makeText(context,"private",Toast.LENGTH_SHORT).show();
                            PrivateProfile privateProfile = response.body().getPrivateProfile();
                            accountType = privateProfile.getAccountType();
                            userCreationTitle.setText("Creations of " + privateProfile.getFirstName());
                            username = privateProfile.getUsername();
                            String firstName = privateProfile.getFirstName();
                            String lastName = privateProfile.getLastName();
                            String firstnamelastname=firstName+" "+lastName;

                            int gender = privateProfile.getGender();
                            Boolean hasProfileMedia = privateProfile.hasProfileMedia();

                            if (hasProfileMedia) {
                                userProfileThumbnail = privateProfile.getProfileMedia().getThumbUrl();
                                userProfileUrl = privateProfile.getProfileMedia().getMediaUrl();
                            }
                            if (userProfileUrl != null) {
                                Glide.with(context)
                                        .load(Uri.parse(userProfileUrl))
                                        .into(profile_id);
                                profileBlur(userProfileUrl);
                            }
                            else
                            {
                                if(gender==1) { Glide.with(context)
                                        .load(R.drawable.ic_user_male_dp)
                                        .into(profile_id);
                                    profileBlur(userProfileUrl);}
                                else{
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(profile_id);
                                    profileBlur(userProfileUrl);}

                            }
                            _usernameTitle.setText(username);
                            _name.setText(firstnamelastname);
                            hobby.setText("");

                            if (youBlocked) {
                                _btnfollow.setText("Unblock");
                                layoutDetail2.setVisibility(View.VISIBLE);
                                layoutDetail.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.GONE);

                            } else {

                                if (isfollowing) {

                                    if (requestRecieved == true) {
                                       // requestId = profileInfo.getFollowInfo().getRequestId();
                                        setActionButtonText(context, _btnfollow, R.string.accept);
                                    } else {
                                        setActionButtonText(context, _btnfollow, R.string.following);
                                    }
                                } else
                                    {
                                    if (hassentrequest == true) {

                                        if (requestRecieved == true) {
                                            setActionButtonText(context, _btnfollow, R.string.accept);
                                        }
                                        else {
                                            setActionButtonText(context, _btnfollow, R.string.requested);
                                        }
                                    } else {

                                        if (requestRecieved == true && isfollower) {

                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                        else if(requestRecieved == true && !isfollower)
                                        {
                                            setActionButtonText(context, _btnfollow, R.string.accept);
                                        }
                                        else if(requestRecieved == false && isfollower)
                                        {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                        else if(requestRecieved == false)
                                        {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }

                                    }

                                }
                                if (accountType == 2) {
                                    getProfileVideos(followersid,1);
                                }
                                else {
                                    if (isfollowing) {
                                        getProfileVideos(followersid,1);

                                    } else {
                                        layoutDetail2.setVisibility(View.VISIBLE);
                                        layoutDetail.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }

                        blur_bacground.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                        blur_bacground.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileInfo> call, Throwable t) {
                blur_bacground.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void profileBlur(final String pic) {

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(final Void... params) {
                Bitmap bitmap = null;
                try {
                    final URL url = new URL(pic);
                    try {
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return bitmap;
            }
            @Override
            protected void onPostExecute(final Bitmap result) {

                try {
                    //Blurry.with(context).from(result).into(background_profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_other_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_profile_report:
                openReportUser(followerfollowingid);
                break;
            case R.id.action_profile_block:
                if(youBlocked) {
                    blockUnblockUsers(followerfollowingid, UNBLOCK_STATUS);
                    youBlocked=false;
                }
                else
                {
                    blockunBlock(followerfollowingid, BLOCK_STATUS);
                    youBlocked=true;
                }
                break;
            case android.R.id.home:
                FragmentManager fm = getChildFragmentManager();
                for (Fragment frag : fm.getFragments()) {
                    if (frag.isVisible()) {
                        FragmentManager childFm = frag.getChildFragmentManager();
                        if (childFm.getBackStackEntryCount() > 0) {
                            childFm.popBackStack();
                        }
                    }
                }
        }
        return true;

    }

    public void getProfileVideos(final int followerId , final int page) {


        ApiCallingService.Posts.getVideosPostedByFriend(page, followerId, context).enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                Log.d("getProfileVideos", String.valueOf(response));
                if (response.code() == 200) {
                    try {

                        if ((response.body().getPosts() == null || response.body().getPosts().size() == 0) && page == 1) {
                            layoutDetail3.setVisibility(View.VISIBLE);
                            layoutDetail.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.GONE);

                        } else {
                            next = response.body().isNextPage();
                            list.addAll(response.body().getPosts());

                            _recycler_view.getAdapter().notifyDataSetChanged();
                            followerCreationAdapter.notifyItemRangeInserted(followerCreationAdapter.getItemCount(), list.size() - 1);
                        }
                        blur_bacground.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                        blur_bacground.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                blur_bacground.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
            }
        });
    }


    public void cancelRequest(int userId, final Context context)

    {
        loader.setVisibility(View.VISIBLE);
        _btnfollow.setText("Follow");

        ApiCallingService.Friends.cancelRequest(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(context, "Your Request has been cancelled", Toast.LENGTH_LONG).show();
                            setActionButtonText(context, _btnfollow, R.string.follow);

                        } else {
                            loader.setVisibility(View.GONE);
                            setActionButtonText(context, _btnfollow, R.string.follow);
                            Toast.makeText(context, "Your Request has already been cancelled", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        loader.setVisibility(View.GONE);
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void unFollowUser(int userId, final Context context) {

        loader.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.unfollowUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();

                        if (b == true) {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(context, "User has been unfollowed", Toast.LENGTH_SHORT).show();
                            setActionButtonText(context, _btnfollow, R.string.follow);
                        } else {
                            loader.setVisibility(View.GONE);
                            setActionButtonText(context, _btnfollow, R.string.follow);
                            Toast.makeText(context, "You have already unfollowed", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        loader.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void followUser(int userId, final Context context) {
        loader.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();

//
                        if (b == true) {


                            if (accountType == 1) {
                                setActionButtonText(context, _btnfollow, R.string.requested);
                                Toast.makeText(context, "Your request has been sent", Toast.LENGTH_SHORT).show();

                            } else {

                                setActionButtonText(context, _btnfollow, R.string.following);
                                Toast.makeText(context, "You have started following", Toast.LENGTH_SHORT).show();
                            }
                            loader.setVisibility(View.INVISIBLE);
                        } else {
                            if (isfollowing) {

                                setActionButtonText(context, _btnfollow, R.string.following);
                            } else {

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                            loader.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        loader.setVisibility(View.GONE);

                        e.printStackTrace();
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }


    public void openBlockUser(final int blockUserId) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Confirm Block...");
        alertDialog.setMessage("Are you sure you want to block this user");
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                blockunBlock(blockUserId, BLOCK_STATUS);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    public void blockUnblockUsers(final int userId, final int status) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        dialogBuilder.setMessage("Are you sure you want to Unblock " + firstName + "?");
        dialogBuilder.setPositiveButton("CONFIRM", null);
        dialogBuilder.setNegativeButton("CANCEL", null);

        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#666666"));
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blockunBlock(userId, status);
                alertDialog.dismiss();


            }
        });
    }

    public void openReportUser(final int blockUserId) {
        FragmentManager fm = getFragmentManager();
        ReportUserDialogFragment reportUserDialogFragment = ReportUserDialogFragment.newInstance(blockUserId, username);
        reportUserDialogFragment.setTargetFragment(OthersProfileFragment.this, 301);
        reportUserDialogFragment.show(fm, "fragment_report_post");
    }

    public void blockunBlock(int userId, final int status) {
        loader.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.blockUnblockUser(userId, status, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {

                        if (status == 2) {
                            Toast.makeText(context, "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                            _btnfollow.setText("Follow");
                            loader.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(context, "You have blocked this user", Toast.LENGTH_SHORT).show();
                            _btnfollow.setText("Unblock");
                            loader.setVisibility(View.GONE);
                        }
                    } else {

                        Toast.makeText(context, "Already blocked this user", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }

    public void acceptUser(final int requestId,final boolean isacceptFollow) {

        loader.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.acceptJoinRequest(requestId, context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        try {
                            if (response.code() == 200) {
                                if (response.body().getStatus()) {
                                    requestRecieved= response.body().getFollowInfo().getRequestSent();
                                    isfollowing=response.body().getFollowInfo().getFollowing();

                                    if(isacceptFollow) {
                                        Toast.makeText(context, "Request Accepted", Toast.LENGTH_LONG).show();
                                        if (isfollowing) {
                                            setActionButtonText(context, _btnfollow, R.string.following);
                                            loader.setVisibility(View.GONE);
                                        } else if (hassentrequest) {
                                            loader.setVisibility(View.GONE);
                                            setActionButtonText(context, _btnfollow, R.string.requested);
                                        } else {
                                            loader.setVisibility(View.GONE);
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                    }
                                    else {
                                        if(isfollowing)
                                        {
                                            Toast.makeText(context, "You also have started following", Toast.LENGTH_LONG).show();
                                            setActionButtonText(context, _btnfollow, R.string.following);
                                            loader.setVisibility(View.GONE);
                                        }
                                        else if(requestRecieved)
                                        {
                                            Toast.makeText(context, "Your request has been sent", Toast.LENGTH_LONG).show();
                                            setActionButtonText(context, _btnfollow, R.string.requested);
                                            loader.setVisibility(View.GONE);
                                        }
                                        else{
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                            loader.setVisibility(View.GONE);

                                        }
                                    }
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    loader.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Something went wrong, Please try again..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        loader.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static void setActionButtonText(Context context, TextView textView, int resId) {
        textView.setText(resId);
        switch (resId) {
            case R.string.follow:
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorIcons),
                        context.getResources().getColor(R.color.colorIcons), 2));
                break;
            case R.string.following:
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_light, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorIcons),
                        context.getResources().getColor(R.color.colorIcons), 2));
                break;
            case R.string.requested:
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.TRANSPARENT,
                        context.getResources().getColor(R.color.colorIcons),
                        context.getResources().getColor(R.color.colorIcons), 2));
                break;
            default:
                break;
        }
    }


    public static GradientDrawable getBackground(Context context, TextView textView, int bgColor,
                                                 int strokeColor, int textColor, float cornerRadius) {
        float density = context.getResources().getDisplayMetrics().density;
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(bgColor);
        gradientDrawable.setCornerRadius((float) (cornerRadius * density + 0.5));
        gradientDrawable.setStroke((int) (1 * density + 0.5), strokeColor);
        textView.setTextColor(textColor);
        return gradientDrawable;
    }

    public void resetRecyclerData() {
        _recycler_view.getAdapter().notifyDataSetChanged();
    }

}


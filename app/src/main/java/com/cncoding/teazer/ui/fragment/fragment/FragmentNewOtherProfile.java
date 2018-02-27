package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.friends.ProfileInfo;
import com.cncoding.teazer.model.friends.PublicProfile;
import com.cncoding.teazer.model.user.PrivateProfile;
import com.cncoding.teazer.ui.fragment.activity.OpenProfilePicActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 15/02/18.
 */

public class FragmentNewOtherProfile extends BaseFragment implements ProfileMyCreationAdapter.OnChildFragmentUpdateVideos, ProfileMyReactionAdapter.OnChildFragmentUpdateReaction

{
    private static final int RC_REQUEST_STORAGE = 1001;
    private static final String ARG_ID = "UserID";
    private static final String ARG_IDENTIFIER = "Usertype";
    private static final String ARG_USERNAME = "UserName";
    private static final String ARG_NOTIFICATION_ID = "notifiaction";

    private static final int BLOCK_STATUS = 1;
    private static final int UNBLOCK_STATUS = 2;

    PublicProfile userProfile;
    TextView _creations;
    TextView _followers;
    TextView _following;
    TextView _reactions;

    ProximaNovaRegularCheckedTextView _name;
    ProximaNovaSemiBoldTextView _username;
    ProximaNovaRegularCheckedTextView _detail;
    CircularAppCompatImageView profile_id;
    ImageView placeholder;

    Context context;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

 @BindView(R.id.message1)
 RelativeLayout message1;

    @BindView(R.id.follow)
    Button _btnfollow;
    private FragmentNewProfile2.FollowerListListener mListener;
    AppBarLayout app_bar;


    int accountType;
    boolean requestRecieved;
    boolean hassentrequest;
    boolean isBlockedyou;
    boolean isfollower;
    boolean isfollowing;
    boolean youBlocked;
    Boolean isHideAllPost;
    String firstName;
    private String userProfileUrl;
    private String userCoverPicUrl;
    private int requestId;
    String details;
    String username;
    int followerfollowingid;
    String userType;
    String getNotificationType;
    EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    boolean next;

    public static FragmentNewOtherProfile newInstance(String id, String identifier, String username) {
        FragmentNewOtherProfile othersProfileFragment = new FragmentNewOtherProfile();

        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, id);
        bundle.putString(ARG_IDENTIFIER, identifier);
        bundle.putString(ARG_USERNAME, username);
        othersProfileFragment.setArguments(bundle);
        return othersProfileFragment;

    }


    public static FragmentNewOtherProfile newInstance3(String id, String notificationId) {


        FragmentNewOtherProfile othersProfileFragment = new FragmentNewOtherProfile();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ID, id);
        bundle.putString(ARG_NOTIFICATION_ID, notificationId);
        othersProfileFragment.setArguments(bundle);
        return othersProfileFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        followerfollowingid = Integer.parseInt(getArguments().getString(ARG_ID));
        userType = getArguments().getString(ARG_IDENTIFIER);
        username = getArguments().getString(ARG_USERNAME);
        getNotificationType = getArguments().getString(ARG_NOTIFICATION_ID);
        setHasOptionsMenu(true);
        try {
            previousTitle = getParentActivity().getToolbarTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_others_profile, container, false);
        ButterKnife.bind(this, view);
        context = container.getContext();
        final android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        getParentActivity().setSupportActionBar(toolbar);
        getParentActivity().getSupportActionBar().setTitle("");
        //  toolbar.setSubtitle("Android-er.blogspot.com");
        //  toolbar.setLogo(android.R.drawable.ic_menu_info_details);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        placeholder = view.findViewById(R.id.background);
        _name = view.findViewById(R.id.name);
        _username = view.findViewById(R.id.username);
        _detail = view.findViewById(R.id.detail);
        _creations = view.findViewById(R.id.creations);
        _followers = view.findViewById(R.id.follower);
        _following = view.findViewById(R.id.following);
        _reactions = view.findViewById(R.id.reaction);
        profile_id = view.findViewById(R.id.profilepic);
        app_bar = view.findViewById(R.id.app_bar);

        _btnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(_btnfollow.getText().equals("Accept")) {
                    acceptUser(requestId, true);
                }
                if(_btnfollow.getText().equals("Follow")) {
                    if (requestRecieved) {
                        acceptUser(requestId, false);
                    } else {
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

        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountType == 1) {

                    if (isfollowing) {
                        mListener.onFollowerListListener(String.valueOf(followerfollowingid), "Other");
                    } else if (hassentrequest == true) {
                        if (requestRecieved == true) {
                            mListener.onFollowerListListener(String.valueOf(followerfollowingid), "Other");
                        } else {
                            Toast.makeText(context, "You can not view follower List now", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "You can not view follower List", Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    mListener.onFollowerListListener(String.valueOf(followerfollowingid), "Other");
                }

            }
        });


        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (accountType == 1) {

                    if (isfollowing == true) {

                        mListener.onFollowingListListener(String.valueOf(followerfollowingid), "Other");

                    } else if (hassentrequest == true) {
                        if (requestRecieved == true) {
                            mListener.onFollowingListListener(String.valueOf(followerfollowingid), "Other");
                        } else {
                            Toast.makeText(context, "You can not view following List now", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(context, "You can not view following List now", Toast.LENGTH_SHORT).show();
                }
                else {

                    mListener.onFollowingListListener(String.valueOf(followerfollowingid), "Other");
                }
            }
        });


        profile_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OpenProfilePicActivity.class);
                intent.putExtra("Image", userProfileUrl);
                intent.putExtra("candelete", false);

                Pair<View, String> p1 = Pair.create((View) profile_id, "profile");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), profile_id, "profile");
                startActivity(intent, options.toBundle());
            }

        });

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    _name.setTextColor(Color.parseColor("#FFFFFF"));
                    _username.setTextColor(Color.parseColor("#FFFFFF"));
                    toolbar.setBackgroundResource(R.color.blur);

                } else if (verticalOffset < -650) {
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur3);
                } else if (verticalOffset < -450 && verticalOffset > -650) {
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur4);
                } else if (verticalOffset < -300 && verticalOffset > -450) {
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur2);
                } else if (verticalOffset < 0 && verticalOffset > -300) {
                    _name.setTextColor(Color.parseColor("#88232323"));
                    _username.setTextColor(Color.parseColor("#88232323"));
                    toolbar.setBackgroundResource(R.color.blur0);

                } else if (verticalOffset == 0) {
                    // Expanded
                    _name.setTextColor(Color.parseColor("#000000"));
                    _username.setTextColor(Color.parseColor("#000000"));
                    toolbar.setBackgroundResource(R.color.blur0);


                } else {
                    // Somewhere in between
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur2);


                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getParentActivity().hideToolbar();



    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
        getParentActivity().showToolbar();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getProfileInformation(followerfollowingid);


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof NewProfileFragment.FollowerListListener) {
            mListener = (FragmentNewProfile2.FollowerListListener) context;
        }


    }


    @Override
    public void onDetach() {
        super.onDetach();
        getParentActivity().showToolbar();
        getParentActivity().updateToolbarTitle(previousTitle);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_other_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_profile_report:
                openReportUser(followerfollowingid);
                break;

            case R.id.action_profile_block:
                if (youBlocked) {
                    blockUnblockUsers(followerfollowingid, UNBLOCK_STATUS);
                    youBlocked = false;
                } else {
                    blockunBlock(followerfollowingid, BLOCK_STATUS);
                    youBlocked = true;
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

    @Override
    public void updateVideosCreation(int count) {

    }

    @Override
    public void updateReaction(int count) {


    }


    public static void setActionButtonText(Context context, TextView textView, int resId) {
        textView.setText(resId);
        switch (resId) {
            case R.string.follow:

                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.RED,
                        context.getResources().getColor(R.color.colorIcons),
                        context.getResources().getColor(R.color.colorIcons), 4));
                break;

            case R.string.accept:

                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.RED,
                        context.getResources().getColor(R.color.colorIcons),
                        context.getResources().getColor(R.color.colorIcons), 4));
                break;
            case R.string.following:

                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_light, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.RED,
                        context.getResources().getColor(R.color.colorIcons),
                        context.getResources().getColor(R.color.colorIcons), 4));
                break;

            case R.string.requested:

                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setBackground(getBackground(context, textView, Color.RED,
                        context.getResources().getColor(R.color.colorIcons),
                        context.getResources().getColor(R.color.colorIcons), 4));
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
        //  gradientDrawable.setStroke((int) (1 * density + 0.5), strokeColor);
        textView.setTextColor(textColor);
        return gradientDrawable;
    }


    public void getProfileInformation(final int followersid) {


        ApiCallingService.Friends.getOthersProfileInfo(followersid, context).enqueue(new Callback<ProfileInfo>() {

            @Override
            public void onResponse(Call<ProfileInfo> call, Response<ProfileInfo> response) {
                if (response.code() == 200) {
                    try {
                        ProfileInfo profileInfo = response.body();
                        int follower = profileInfo.getFollowers();
                        int following = profileInfo.getFollowings();
                        int totalvideos = profileInfo.getTotalVideos();
                        int totalreactions = profileInfo.getTotalReactions();
                        hassentrequest = profileInfo.getFollowInfo().getRequestSent();
                        requestRecieved = profileInfo.getFollowInfo().getRequestReceived();
                        isBlockedyou = profileInfo.getFollowInfo().getIsBlockedYou();
                        isfollower = profileInfo.getFollowInfo().getFollower();
                        isfollowing = profileInfo.getFollowInfo().getFollowing();
                        youBlocked = profileInfo.getFollowInfo().getYouBlocked();
                        isHideAllPost = profileInfo.getIsHidedAllPosts();
                        if (requestRecieved) {
                            requestId = profileInfo.getFollowInfo().getRequestId();
                        }

                        if (isHideAllPost) {
                            // _unHideAll.setVisibility(View.VISIBLE);
                        }

                        _followers.setText(String.valueOf(follower));
                        _following.setText(String.valueOf(following));
                        _creations.setText(String.valueOf(totalvideos));
                        _reactions.setText(String.valueOf(totalreactions));

                        if (response.body().getPrivateProfile() == null) {
                            PublicProfile publicProfile = response.body().getPublicProfile();
                            username = publicProfile.getUserName();
                            String firstName = publicProfile.getFirstName();
                            String lastName = publicProfile.getLastName();
                            String firstnamelastname = firstName + " " + lastName;
                            details = publicProfile.getDescription();
                            int gender = publicProfile.getGender();

                            accountType = publicProfile.getAccountType();
                            _username.setText(username);
                            _name.setText(firstnamelastname);
                            if (details == null || details.equals("")) {
                                _detail.setText("");
                            } else {
                                _detail.setText(details);
                            }
                            Boolean hasProfileMedia = publicProfile.getHasProfileMedia();



//                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                                        R.drawable.material_flat);
//                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                                    @SuppressWarnings("ResourceType")
//                                    @Override
//                                    public void onGenerated(Palette palette) {
//                                        int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
//                                        collapsingToolbarLayout.setContentScrimColor(vibrantColor);
//                                        collapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryDark);
//                                    }
//                                });

                            if (hasProfileMedia == true) {
                                userProfileUrl = publicProfile.getProfileMedia().getMediaUrl();
                            }
                            Boolean hasCoverMedia=publicProfile.getHasCoverMedia();
                            if (hasCoverMedia == true)
  {
                                userCoverPicUrl = publicProfile.getCoverMedia().getMediaUrl();
                            }

                            if (userCoverPicUrl != null) {
                                Glide.with(context)
                                        .load(Uri.parse(userCoverPicUrl))
                                        .into(placeholder);
                            } else {

                                Glide.with(context)
                                        .load(R.drawable.backgroundprofile)
                                        .into(placeholder);
                            }

                            if (userProfileUrl != null) {
                                Glide.with(context)
                                        .load(Uri.parse(userProfileUrl))
                                        .into(profile_id);
                                //  profileBlur(userProfileUrl);
                            } else {
                                if (gender == 1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(profile_id);
                                    //   profileBlur(userProfileUrl);
                                } else {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(profile_id);
                                    //profileBlur(userProfileUrl);
                                }
                            }
                            if (youBlocked) {
                                _btnfollow.setText("Unblock");
//                                layoutDetail2.setVisibility(View.VISIBLE);
//                                layoutDetail.setVisibility(View.INVISIBLE);
//                                progressBar.setVisibility(View.GONE);

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
                                        } else if (requestRecieved == true && !isfollower) {
                                            setActionButtonText(context, _btnfollow, R.string.accept);
                                        } else if (requestRecieved == false && isfollower) {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        } else if (requestRecieved == false) {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                    }
                                    //   layout.setVisibility(View.VISIBLE);
                                    //  progressBar.setVisibility(View.GONE);
                                }
                                if (accountType == 2) {

                                    //  getProfileVideos(followersid,1);

                                    viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context, FragmentNewOtherProfile.this, followerfollowingid));
                                    tabLayout.setupWithViewPager(viewPager);
                                }
                                else {
                                    if (isfollowing) {
                                        //getProfileVideos(followersid,1);

                                        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context, FragmentNewOtherProfile.this, followerfollowingid));
                                        tabLayout.setupWithViewPager(viewPager);

                                    } else {

                                        message1.setVisibility(View.VISIBLE);
                                        tabLayout.setVisibility(View.GONE);
                                        viewPager.setVisibility(View.GONE);
                                        // layoutDetail2.setVisibility(View.VISIBLE);
                                        // layoutDetail.setVisibility(View.GONE);
                                    }

                                }
                            }

                        } else if (response.body().getPublicProfile() == null) {

                            PrivateProfile privateProfile = response.body().getPrivateProfile();
                            accountType = privateProfile.getAccountType();
                            username = privateProfile.getUsername();
                            String firstName = privateProfile.getFirstName();
                            String lastName = privateProfile.getLastName();
                            String firstnamelastname = firstName + " " + lastName;

                            int gender = privateProfile.getGender();
                            Boolean hasProfileMedia = privateProfile.hasProfileMedia();

                            if (hasProfileMedia) {
                                userProfileUrl = privateProfile.getProfileMedia().getMediaUrl();
                            }

                            Boolean hasCoverMedia=privateProfile.isHas_cover_media();
                            if (hasCoverMedia == true)
                            {
                                userCoverPicUrl = privateProfile.getCover_media().getMediaUrl();
                            }


                            if (userCoverPicUrl != null) {
                                Glide.with(context)
                                        .load(Uri.parse(userCoverPicUrl))
                                        .into(placeholder);
                            } else {

                                Glide.with(context)
                                        .load(R.drawable.backgroundprofile)
                                        .into(placeholder);
                            }


                            if (userProfileUrl != null) {

                                Glide.with(context)
                                        .load(Uri.parse(userProfileUrl))
                                        .into(profile_id);
                            } else {
                                if (gender == 1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(profile_id);
                                } else {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(profile_id);
                                }

                            }
                            _username.setText(username);
                            _name.setText(firstnamelastname);
                            _detail.setText("");

                            if (youBlocked) {
                                _btnfollow.setText("Unblock");
//                                layoutDetail2.setVisibility(View.VISIBLE);
//                                layoutDetail.setVisibility(View.INVISIBLE);
//                                progressBar.setVisibility(View.GONE);

                            } else {

                                if (isfollowing) {

                                    if (requestRecieved == true) {
                                        // requestId = profileInfo.getFollowInfo().getRequestId();
                                        setActionButtonText(context, _btnfollow, R.string.accept);
                                    } else {
                                        setActionButtonText(context, _btnfollow, R.string.following);
                                    }
                                } else {
                                    if (hassentrequest == true) {

                                        if (requestRecieved == true) {
                                            setActionButtonText(context, _btnfollow, R.string.accept);
                                        } else {
                                            setActionButtonText(context, _btnfollow, R.string.requested);
                                        }
                                    } else {

                                        if (requestRecieved == true && isfollower) {

                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        } else if (requestRecieved == true && !isfollower) {
                                            setActionButtonText(context, _btnfollow, R.string.accept);
                                        } else if (requestRecieved == false && isfollower) {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        } else if (requestRecieved == false) {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }

                                    }

                                }
                                if (accountType == 2) {
                                    // getProfileVideos(followersid,1);

                                    viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context, FragmentNewOtherProfile.this, followerfollowingid));
                                    tabLayout.setupWithViewPager(viewPager);
                                } else {
                                    if (isfollowing) {
                                        //getProfileVideos(followersid,1);
                                        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context, FragmentNewOtherProfile.this, followerfollowingid));
                                        tabLayout.setupWithViewPager(viewPager);

                                    } else {
                                        message1.setVisibility(View.VISIBLE);
                                        tabLayout.setVisibility(View.GONE);
                                        viewPager.setVisibility(View.GONE);
                                        // layoutDetail2.setVisibility(View.VISIBLE);
                                        // layoutDetail.setVisibility(View.GONE);
                                    }
                                }
                            }
                        }

//                        blur_bacground.setVisibility(View.VISIBLE);
//                        loader.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
//                        blur_bacground.setVisibility(View.GONE);
//                        loader.setVisibility(View.GONE);
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileInfo> call, Throwable t) {
//                blur_bacground.setVisibility(View.GONE);
//                loader.setVisibility(View.GONE);
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });

    }


    public void unFollowUser(int userId, final Context context) {

        // loader.setVisibility(View.VISIBLE);

        _btnfollow.setClickable(false);

        ApiCallingService.Friends.unfollowUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();

                        if (b == true) {
                            // loader.setVisibility(View.GONE);
                            Toast.makeText(context, "User has been unfollowed", Toast.LENGTH_SHORT).show();
                            setActionButtonText(context, _btnfollow, R.string.follow);
                        } else {
                            //loader.setVisibility(View.GONE);
                            setActionButtonText(context, _btnfollow, R.string.follow);
                            Toast.makeText(context, "You have already unfollowed", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        //  loader.setVisibility(View.GONE);
                        e.printStackTrace();
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                //  loader.setVisibility(View.GONE);
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
            }
        });
        _btnfollow.setClickable(true);

    }

    public void followUser(int userId, final Context context) {
        // loader.setVisibility(View.VISIBLE);
        _btnfollow.setClickable(false);

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
                            //   loader.setVisibility(View.INVISIBLE);
                        } else {
                            if (isfollowing) {

                                setActionButtonText(context, _btnfollow, R.string.following);
                            } else {

                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                            //  loader.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        //  loader.setVisibility(View.GONE);

                        e.printStackTrace();
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
                // loader.setVisibility(View.GONE);
            }
        });
        _btnfollow.setClickable(true);

    }


    public void acceptUser(final int requestId, final boolean isacceptFollow) {

        // loader.setVisibility(View.VISIBLE);
        _btnfollow.setClickable(false);
        ApiCallingService.Friends.acceptJoinRequest(requestId, context)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        try {

                            if (response.code() == 200) {

                                if (response.body().getStatus()) {

                                    requestRecieved = response.body().getFollowInfo().getRequestSent();
                                    isfollowing = response.body().getFollowInfo().getFollowing();

                                    if (isacceptFollow) {
                                        Toast.makeText(context, "Request Accepted", Toast.LENGTH_LONG).show();
                                        if (isfollowing) {
                                            setActionButtonText(context, _btnfollow, R.string.following);
                                            //  loader.setVisibility(View.GONE);
                                        } else if (hassentrequest) {
                                            //  loader.setVisibility(View.GONE);
                                            setActionButtonText(context, _btnfollow, R.string.requested);
                                        } else {
                                            // loader.setVisibility(View.GONE);
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                        }
                                    } else {
                                        if (isfollowing) {
                                            Toast.makeText(context, "You also have started following", Toast.LENGTH_LONG).show();
                                            setActionButtonText(context, _btnfollow, R.string.following);
                                            //  loader.setVisibility(View.GONE);
                                        } else if (requestRecieved) {
                                            Toast.makeText(context, "Your request has been sent", Toast.LENGTH_LONG).show();
                                            setActionButtonText(context, _btnfollow, R.string.requested);
                                            //   loader.setVisibility(View.GONE);
                                        } else {
                                            setActionButtonText(context, _btnfollow, R.string.follow);
                                            //   loader.setVisibility(View.GONE);

                                        }
                                    }
                                } else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    // loader.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(context, "Something went wrong, Please try again..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        //   loader.setVisibility(View.GONE);
                    }
                });

        _btnfollow.setClickable(true);


    }

    public void cancelRequest(int userId, final Context context)

    {
        // loader.setVisibility(View.VISIBLE);
        _btnfollow.setText("Follow");

        ApiCallingService.Friends.cancelRequest(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {
                            // loader.setVisibility(View.GONE);
                            Toast.makeText(context, "Your Request has been cancelled", Toast.LENGTH_LONG).show();
                            setActionButtonText(context, _btnfollow, R.string.follow);

                        } else {
                            //loader.setVisibility(View.GONE);
                            setActionButtonText(context, _btnfollow, R.string.follow);
                            Toast.makeText(context, "Your Request has already been cancelled", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        //  loader.setVisibility(View.GONE);
                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                //  loader.setVisibility(View.GONE);
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
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

    public void blockunBlock(int userId, final int status) {
        //loader.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.blockUnblockUser(userId, status, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {

                        if (status == 2) {
                            Toast.makeText(context, "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                            _btnfollow.setText("Follow");
                            //   loader.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(context, "You have blocked this user", Toast.LENGTH_SHORT).show();
                            _btnfollow.setText("Unblock");
                            // loader.setVisibility(View.GONE);
                        }
                    } else {

                        Toast.makeText(context, "Already blocked this user", Toast.LENGTH_SHORT).show();
                        //  loader.setVisibility(View.GONE);
                    }

                } catch (Exception e) {

                    e.printStackTrace();

                    Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                    //loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
                //  loader.setVisibility(View.GONE);
            }
        });
    }

    public void openReportUser(final int blockUserId) {
        FragmentManager fm = getFragmentManager();
        ReportUserDialogFragment reportUserDialogFragment = ReportUserDialogFragment.newInstance(blockUserId, username);
        reportUserDialogFragment.setTargetFragment(FragmentNewOtherProfile.this, 301);
        reportUserDialogFragment.show(fm, "fragment_report_post");
    }


    public interface FollowerListListener {

        void onFollowerListListener(String id, String identifier);

        void onFollowingListListener(String id, String identifier);
    }


}

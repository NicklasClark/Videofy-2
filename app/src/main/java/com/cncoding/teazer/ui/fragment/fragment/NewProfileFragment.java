package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.friends.PublicProfile;
import com.cncoding.teazer.model.user.UserProfile;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.OpenProfilePicActivity;
import com.cncoding.teazer.ui.fragment.activity.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.FabricAnalyticsUtil.logProfileShareEvent;

/**
 * Created by farazhabib on 08/02/18.
 */

public class NewProfileFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener,ProfileMyCreationAdapter.OnChildFragmentUpdateVideos, ProfileMyReactionAdapter.OnChildFragmentUpdateReaction{


    PublicProfile userProfile;
    TextView _creations;
    TextView _followers;
    TextView _following;
    TextView _reactions;

    ProximaNovaRegularCheckedTextView _name;
    ProximaNovaSemiBoldTextView _username;
    ProximaNovaRegularCheckedTextView _detail;
    de.hdodenhof.circleimageview.CircleImageView profile_id;

    int totalfollowers;
    int totalfollowing;
    int totalvideos;
    int reactions;

    String firstname;
    String userId;
    String lastname;
    String username;
    String email;
    int accountType;
    boolean hasProfleMedia;
    boolean hasCoverMedia;
    Long mobilenumber;
    int gender;
    int countrycode;
    String detail;
    private String userProfileThumbnail;
    private String userCoverThumbnail;
    private String userCoverUrl;
    private String userProfileUrl;
    ImageView placeholder;

    Context context;
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;
    public static boolean checkpostupdated = false;
    public static boolean checkprofileupdated = false;
    public static boolean checkpicUpdated = false;
    public static boolean checkCoverpicUpdated = false;



    private LinearLayout mTitleContainer;
    private ProximaNovaRegularCheckedTextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private NewProfileFragment.FollowerListListener mListener;

    public static NewProfileFragment newInstance(int page) {
        return new NewProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    // 12-02-2017


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View view = inflater.inflate(R.layout.fragment_new_profile3, container, false);
        ButterKnife.bind(this, view);
        mToolbar = view.findViewById(R.id.main_toolbar);
        mTitle = view.findViewById(R.id.main_textview_title);
        mTitleContainer = view.findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = view.findViewById(R.id.main_appbar);

//        placeholder = view.findViewById(R.id.placeholder);
        _name = view.findViewById(R.id.name);
        _username = view.findViewById(R.id.username);
        _detail = view.findViewById(R.id.detail);
        _creations = view.findViewById(R.id.creations);
        _followers = view.findViewById(R.id.follower);
        _following = view.findViewById(R.id.following);
        _reactions = view.findViewById(R.id.reaction);
        profile_id = view.findViewById(R.id.profilepic);

        mAppBarLayout.addOnOffsetChangedListener(NewProfileFragment.this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        getParentActivity().setSupportActionBar(mToolbar);

//
//        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
//
//        collapsingToolbar.setTitle("Mohd Arif");
//
//        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//        AppBarLayout appBarLayout =  view.findViewById(R.id.app_bar_layout);
//
//  appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
//                    // Collapsed
//                    mainlayout.setVisibility(View.GONE);
//
//                } else if (verticalOffset == 0) {
//                    mainlayout.setVisibility(View.VISIBLE);
//
//                } else if(verticalOffset< -750){
//                    mainlayout.setVisibility(View.VISIBLE);
//                }
//
//            }
//        });


//        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//           // boolean isShow = false;
//            //int scrollRange = -1;
//
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//
//                int maxScroll = appBarLayout.getTotalScrollRange();
//                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
//                handleAlphaOnTitle(percentage);
//                handleToolbarTitleVisibility(percentage);
//
////                if (scrollRange == -1) {
////                    scrollRange = appBarLayout.getTotalScrollRange();
////
////                }
////                if (scrollRange + verticalOffset ==0) {
////                    isShow = true;
////
////                    fadeOutAndHideImage(mainlayout);
////
////                } else  {
////                    isShow = false;
////                    mainlayout.setVisibility(View.VISIBLE);
////
////                }
//            }
//        });
//


        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFollowerListListener(String.valueOf(0), "User");
            }
        });

        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFollowingListListener(String.valueOf(0), "User");
            }
        });


        profile_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OpenProfilePicActivity.class);
                intent.putExtra("Image", userProfileUrl);
                intent.putExtra("candelete",true);
                intent.putExtra("gender",gender);
                Pair<View, String> p1 = Pair.create((View)profile_id, "profile");
                Pair<View, String> p2 = Pair.create((View)_username, "text");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), profile_id, "profile");
                startActivity(intent, options.toBundle());
            }

        });

        return view;
    }

    private void fadeOutAndHideImage(final RelativeLayout img) {
        Animation fadeOut = new AlphaAnimation(0, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(10);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                img.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        img.startAnimation(fadeOut);
    }

    @Override
    public void onResume() {
        super.onResume();
        getParentActivity().hideToolbar();


        if (NewProfileFragment.checkprofileupdated) {
            updateProfile();
            NewProfileFragment.checkprofileupdated = false;
        }


        if(NewProfileFragment.checkpostupdated)
        {

//            viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext(),NewProfileFragment.this));
//            tabLayout.setupWithViewPager(viewPager);
//            NewProfileFragment.checkpostupdated=false;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        getParentActivity().showToolbar();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfileDetail();


//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//                R.drawable.backgroundprofile);
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                collapsingToolbar.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
//                collapsingToolbar.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
//            }
//        });
//        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext(),NewProfileFragment.this));
//        tabLayout.setupWithViewPager(viewPager);


    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof NewProfileFragment.FollowerListListener) {
            mListener = (NewProfileFragment.FollowerListListener) context;

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getParentActivity().showToolbar();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_new_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.ic_settings:
                Intent mintent = new Intent(context, Settings.class);
                mintent.putExtra("AccountType", String.valueOf(accountType));
                mintent.putExtra("UserProfile", userProfile);
                startActivity(mintent);
                break;

            case R.id.share:



                String profileUrl = null;
                try {
                    profileUrl = userProfile.getProfileMedia() == null? null:userProfile.getProfileMedia().getMediaUrl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier(String.valueOf(userProfile.getUserId()))
                        .setTitle(userProfile.getFirstName())
                        .setContentDescription("Hi, follow me on Teazer and share cool videos")
                        .setContentImageUrl(profileUrl);

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("user_id", String.valueOf(userProfile.getUserId()))
                        .addControlParameter("$desktop_url", "https://teazer.online/")
                        .addControlParameter("$ios_url", "https://teazer.online/");

                ShareSheetStyle shareSheetStyle = new ShareSheetStyle(getActivity(), "Teazer", "Hi, follow me on Teazer and express better")
                        .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                        .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.INSTAGRAM)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                        .setAsFullWidthStyle(true)
                        .setSharingTitle("Share With");

                branchUniversalObject.generateShortUrl(getContext(), linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            //fabric event
                            logProfileShareEvent("Branch", userProfile.getEmail(), "Profile", String.valueOf(userProfile.getUserId()));

                            //loader.setVisibility(View.GONE);
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        }
                        else
                        {

                        }
                          //  loader.setVisibility(View.GONE);
                    }
                });


                break;


            case R.id.edit:

                Intent intent = new Intent(context, EditProfile.class);
                intent.putExtra("UserName", username);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("LastName", lastname);
                intent.putExtra("EmailId", email);
                intent.putExtra("MobileNumber", String.valueOf(mobilenumber));
                intent.putExtra("Gender", String.valueOf(gender));
                intent.putExtra("CountryCode", String.valueOf(countrycode));
                intent.putExtra("ProfileThumb", userProfileThumbnail);
                intent.putExtra("ProfileMedia", userProfileUrl);
                intent.putExtra("CoverMedia", userCoverUrl);

                if (detail == null)
                    intent.putExtra("Detail", "");
                else {
                    intent.putExtra("Detail", detail);
                }
                startActivity(intent);
                break;



        }
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;
        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }





    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }


    public void getProfileDetail() {
        // blur_bacground.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {


                    userProfile = response.body().getUserProfile();
                    firstname = userProfile.getFirstName();
                    lastname = userProfile.getLastName();
                    username = userProfile.getUserName();
                    email = userProfile.getEmail();
                    accountType = userProfile.getAccountType();
                    hasProfleMedia = userProfile.getHasProfileMedia();
                    hasCoverMedia = userProfile.getHasCoverMedia();
                    totalfollowers = response.body().getFollowers();
                    totalfollowing = response.body().getFollowings();
                    totalvideos = response.body().getTotalVideos();
                    userId = String.valueOf(userProfile.getUserId());
                    countrycode = userProfile.getCountryCode();
                    detail = userProfile.getDescription();
                    gender = userProfile.getGender();
                    reactions=response.body().getTotalReactions();

                    Long mobilno = userProfile.getPhoneNumber();

                    if (mobilno == null) {
                        mobilenumber = 0L;
                    } else {
                        mobilenumber = mobilno;
                    }


                    if (userProfile.getHasProfileMedia()) {

                        userProfileThumbnail = userProfile.getProfileMedia().getThumbUrl();
                        userProfileUrl = userProfile.getProfileMedia().getMediaUrl();

                        Glide.with(context)
                                .load(userProfileUrl)
                                .into(profile_id);

                    }
                    else
                    {



                        if(gender==1)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);

                        }
                        else if(gender==2)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_female_dp)
                                    .into(profile_id);

                        }
                        else
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);

                        }
                    }


                    if (hasCoverMedia)
                    {

                        userCoverUrl=userProfile.getCoverMedia().getMediaUrl();
                        //userCoverUrl=userProfile.getCoverMedia().getThumbUrl();

                        Glide.with(context)
                                .load(userCoverUrl)
                                .into(placeholder);

                    }
                    else
                    {

                    }
                    _detail.setText(detail);
                    _name.setText(firstname + " " + lastname);
                    _username.setText(username);
                    mTitle.setText(username);

                    _followers.setText(String.valueOf(totalfollowers));
                    _following.setText(String.valueOf(totalfollowing));
                    _creations.setText(String.valueOf(totalvideos));
                    _reactions.setText(String.valueOf(reactions));

//
//
//
//                    blur_bacground.setVisibility(View.VISIBLE);
//                    loader.setVisibility(View.GONE);
                } catch (Exception e) {
//                    blur_bacground.setVisibility(View.GONE);
//                    loader.setVisibility(View.GONE);
//                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //blur_bacground.setVisibility(View.GONE);
                // loader.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }


    public void updateProfile() {
       // loader.setVisibility(View.VISIBLE);
        //blur_bacground.setVisibility(View.GONE);
        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {

                    userProfile = response.body().getUserProfile();
                    firstname = userProfile.getFirstName();
                    lastname = userProfile.getLastName();
                    username = userProfile.getUserName();
                    email = userProfile.getEmail();
                    accountType = userProfile.getAccountType();
                    hasProfleMedia = userProfile.getHasProfileMedia();
                    totalfollowers = response.body().getFollowers();
                    totalfollowing = response.body().getFollowings();
                    totalvideos = response.body().getTotalVideos();
                    userId = String.valueOf(userProfile.getUserId());
                    gender = userProfile.getGender();

                    Long mobilno = userProfile.getPhoneNumber();
                    if (mobilno == null) {
                        mobilenumber = 0L;
                    } else {
                        mobilenumber = mobilno;
                    }
                    countrycode = userProfile.getCountryCode();
                    detail = userProfile.getDescription();

                    if(NewProfileFragment.checkpicUpdated){
                        NewProfileFragment.checkpicUpdated=false;
                        userProfileUrl=null;
                    }

                    if (userProfile.getHasProfileMedia()) {
                        userProfileThumbnail = userProfile.getProfileMedia().getThumbUrl();
                        userProfileUrl = userProfile.getProfileMedia().getMediaUrl();

                        Glide.with(context)
                                .load(userProfileUrl)
                                .into(profile_id);
                   //     profileBlur(userProfileUrl);
                    }
                    else
                    {
//                        Glide.with(context)
//                                .load(R.drawable.ic_default_bg)
//                                .into(bgImage);

                        if(gender==1)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);

                        }
                        else if(gender==2)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_female_dp)
                                    .into(profile_id);

                        }
                        else
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);

                        }
                    }
                    _detail.setText(detail);
                    _name.setText(firstname);
                    _username.setText(username);
                    _followers.setText(String.valueOf(totalfollowers));
                    _following.setText(String.valueOf(totalfollowing));
                    _creations.setText(String.valueOf(totalvideos));
                    _reactions.setText(String.valueOf(reactions));


                 //   loader.setVisibility(View.GONE);
                  //  blur_bacground.setVisibility(View.VISIBLE);
                }
                catch (Exception e) {

                  //  loader.setVisibility(View.GONE);
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //loader.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void updateVideosCreation(int count) {


            int counter=totalvideos-count;
            totalvideos=counter;
            _creations.setText(String.valueOf(totalvideos));

    }

    @Override
    public void updateReaction(int count) {

        int counter=reactions-count;
        reactions=counter;
        _reactions.setText(String.valueOf(reactions));
    }


    public interface FollowerListListener {

        void onFollowerListListener(String id, String identifier);

        void onFollowingListListener(String id, String identifier);
    }


}

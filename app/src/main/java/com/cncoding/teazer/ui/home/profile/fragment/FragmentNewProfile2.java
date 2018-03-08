package com.cncoding.teazer.ui.home.profile.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.friends.PublicProfile;
import com.cncoding.teazer.data.model.profile.Preference;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.model.user.userProfile.TopReactedUser;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaBoldTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.activity.EditProfile;
import com.cncoding.teazer.ui.home.profile.activity.FollowersListFragment;
import com.cncoding.teazer.ui.home.profile.activity.FollowingListFragment;
import com.cncoding.teazer.ui.home.profile.activity.Settings;
import com.cncoding.teazer.ui.home.profile.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.ui.home.profile.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.ui.home.profile.adapter.ProfileMyReactionAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logProfileShareEvent;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getUserId;
import static com.cncoding.teazer.utilities.common.ViewUtils.openProfile;

/**
 *
 * Created by farazhabib on 19/02/18.
 */

public class FragmentNewProfile2 extends BaseFragment implements ProfileMyCreationAdapter.OnChildFragmentUpdateVideos, ProfileMyReactionAdapter.OnChildFragmentUpdateReaction {

    private static final int RC_REQUEST_STORAGE = 1001;

    PublicProfile userProfile;

    ProximaNovaBoldTextView _creations;
    ProximaNovaBoldTextView _followers;
    ProximaNovaBoldTextView _following;
    ProximaNovaBoldTextView _reactions;

    ProximaNovaRegularCheckedTextView _username;
    ProximaNovaSemiBoldTextView _name;
    ProximaNovaRegularCheckedTextView _detail;
    CircularAppCompatImageView profile_id;

    int totalfollowers;
    int totalfollowing;
    int totalvideos;
    int reactions;
    int totalProfilelikes;
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
    private String userCoverUrl;
    private String userProfileUrl;
    List<TopReactedUser> topReactedUserList;


    ArrayList<Preference> userPrefrences;

    @BindView(R.id.reaction1)
    CircularAppCompatImageView reaction1;
    @BindView(R.id.reaction2)
    CircularAppCompatImageView reaction2;
    @BindView(R.id.reaction3)
    CircularAppCompatImageView reaction3;


    ImageView placeholder;

    Context context;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    @BindView(R.id.totallikes)
    ProximaNovaRegularCheckedTextView _totallikes;
    @BindView(R.id.topReactions)
    ProximaNovaRegularCheckedTextView topReactions;
    AppBarLayout app_bar;

    public static boolean checkpostupdated = false;
    public static boolean checkprofileupdated = false;
    public static boolean checkpicUpdated = false;


    public static FragmentNewProfile2 newInstance() {
        return new FragmentNewProfile2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_profile3, container, false);
        ButterKnife.bind(this, view);
        context=container.getContext();
        final android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        getParentActivity().setSupportActionBar(toolbar);
        getParentActivity().getSupportActionBar().setTitle("");
        //  toolbar.setSubtitle("Android-er.blogspot.com");
        //  toolbar.setLogo(android.R.drawable.ic_menu_info_details);


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


        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.pushFragment(FollowersListFragment.newInstance(String.valueOf(0), "User"));
            }
        });

        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.pushFragment(FollowingListFragment.newInstance(String.valueOf(0), "User"));

            }
        });


//        profile_id.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
////                Intent intent = new Intent(context, OpenProfilePicActivity.class);
////                intent.putExtra("Image", userProfileUrl);
////                intent.putExtra("candelete",true);
////                intent.putExtra("gender",gender);
////                Pair<View, String> p1 = Pair.create((View)profile_id, "profile");
////                Pair<View, String> p2 = Pair.create((View)_username, "text");
////                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), profile_id, "profile");
////                startActivity(intent, options.toBundle());
//            }
//
//        });

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    _name.setTextColor(Color.parseColor("#FFFFFF"));
                    _username.setTextColor(Color.parseColor("#FFFFFF"));
                    toolbar.setBackgroundResource(R.color.blur);
                }
                else if(verticalOffset<-650)
                {
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur3);
                }
                else if (verticalOffset <-450 && verticalOffset>-650)
                {
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur4);
                }
                else if (verticalOffset <-300 && verticalOffset>-450)
                {
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur2);
                }
                else if (verticalOffset < 0 && verticalOffset>-300)
                {
                    _name.setTextColor(Color.parseColor("#88232323"));
                    _username.setTextColor(Color.parseColor("#88232323"));
                    toolbar.setBackgroundResource(R.color.blur0);

                }
                else if (verticalOffset == 0) {
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

        _totallikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation.pushFragment(FragmentLikedUserProfile.newInstance());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        getParentActivity().hideToolbarOnly();

        if (FragmentNewProfile2.checkprofileupdated) {
            updateProfile();
        }
        if(FragmentNewProfile2.checkpostupdated) {
            viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext(), FragmentNewProfile2.this,0));
            tabLayout.setupWithViewPager(viewPager);
            FragmentNewProfile2.checkpostupdated=false;
        }
    }

    @OnClick(R.id.reaction1) public void openTopReactor1Profile() {
        openProfile(getUserId(context), navigation, topReactedUserList.get(0).getUserId());
    }

    @OnClick(R.id.reaction2) public void openTopReactor2Profile() {
        openProfile(getUserId(context), navigation, topReactedUserList.get(1).getUserId());
    }

    @OnClick(R.id.reaction3) public void openTopReactor3Profile() {
        openProfile(getUserId(context), navigation, topReactedUserList.get(2).getUserId());
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        getParentActivity().showToolbar();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext(),FragmentNewProfile2.this,0));
        tabLayout.setupWithViewPager(viewPager);
        getProfileDetail();
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        getParentActivity().showToolbar();
//        getParentActivity().updateToolbarTitle(previousTitle);
//    }

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
               // mintent.putExtra("UserProfile", userProfile);


              //  Bundle bundle = new Bundle();
             //   bundle.putParcelableArrayList("UserPrefrences", userPrefrences);
               // mintent.putExtras(bundle);
                mintent.putParcelableArrayListExtra("UserPrefrences", userPrefrences);
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
                    totalProfilelikes=response.body().getTotalProfileLikes();
                    userPrefrences=response.body().getPreferences();
                    topReactedUserList=response.body().getTopReactedUsers();


                    if (topReactedUserList!=null)
                    {
                        if(topReactedUserList.size()==1){
                            TopReactedUser  topReactedUser1=topReactedUserList.get(0);
                            reaction1.setVisibility(View.VISIBLE);
                            if(topReactedUser1.getHasProfileMedia())
                            {
                                Glide.with(context)
                                        .load(Uri.parse(topReactedUser1.getProfileMedia().getThumbUrl()))
                                        .into(reaction1);
                            }
                            else
                            {
                                if(topReactedUser1.getGender()==1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(reaction1);
                                }
                                else
                                {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(reaction1);
                                }

                            }
                        }
                        else if((topReactedUserList.size()==2))
                        {
                            TopReactedUser  topReactedUser1=topReactedUserList.get(0);
                            TopReactedUser  topReactedUser2=topReactedUserList.get(1);
                            reaction1.setVisibility(View.VISIBLE);
                            reaction2.setVisibility(View.VISIBLE);
                            if(topReactedUser1.getHasProfileMedia())
                            {
                                Glide.with(context)
                                        .load(Uri.parse(topReactedUser1.getProfileMedia().getThumbUrl()))
                                        .into(reaction1);
                            }
                            else
                            {
                                if(topReactedUser1.getGender()==1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(reaction1);
                                }
                                else
                                {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(reaction1);
                                }

                            }

                            if(topReactedUser2.getHasProfileMedia())
                            {
                                Glide.with(context)
                                        .load(Uri.parse(topReactedUser2.getProfileMedia().getThumbUrl()))
                                        .into(reaction2);
                            }
                            else
                            {
                                if(topReactedUser2.getGender()==1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(reaction2);
                                }
                                else
                                {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(reaction2);
                                }

                            }
                        }

                        else if((topReactedUserList.size()==3))
                        {
                            TopReactedUser  topReactedUser1=topReactedUserList.get(0);
                            TopReactedUser  topReactedUser2=topReactedUserList.get(1);
                            TopReactedUser  topReactedUser3=topReactedUserList.get(2);
                            reaction1.setVisibility(View.VISIBLE);
                            reaction2.setVisibility(View.VISIBLE);
                            reaction3.setVisibility(View.VISIBLE);

                            if(topReactedUser1.getHasProfileMedia())
                            {
                                Glide.with(context)
                                        .load(Uri.parse(topReactedUser1.getProfileMedia().getThumbUrl()))
                                        .into(reaction1);
                            }
                            else
                            {
                                if(topReactedUser1.getGender()==1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(reaction1);
                                }
                                else
                                {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(reaction1);
                                }

                            }

                            if(topReactedUser2.getHasProfileMedia())
                            {
                                Glide.with(context)
                                        .load(Uri.parse(topReactedUser2.getProfileMedia().getThumbUrl()))
                                        .into(reaction2);
                            }
                            else
                            {
                                if(topReactedUser2.getGender()==1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(reaction2);
                                }
                                else
                                {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(reaction2);
                                }

                            }
                            if(topReactedUser3.getHasProfileMedia())
                            {
                                Glide.with(context)
                                        .load(Uri.parse(topReactedUser3.getProfileMedia().getThumbUrl()))
                                        .into(reaction3);
                            }
                            else
                            {
                                if(topReactedUser3.getGender()==1) {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_male_dp)
                                            .into(reaction3);
                                }
                                else
                                {
                                    Glide.with(context)
                                            .load(R.drawable.ic_user_female_dp)
                                            .into(reaction3);
                                }

                            }
                        }
                        else if(topReactedUserList.size()==0)
                        {
                            topReactions.setVisibility(View.GONE);

                        }

                    }
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

                        Glide.with(context)
                                .load(userCoverUrl)
                                .into(placeholder);
                    }
                    else {
                        Glide.with(context)
                                .load(R.drawable.backgroundprofile)
                                .into(placeholder);
                    }
                    _detail.setText(detail);
                    _name.setText(firstname + " " + lastname);
                    _username.setText("@"+username);
                    if(totalfollowers<10)
                    {
                        _followers.setText(String.valueOf("0"+totalfollowers));

                    }
                    else {
                        _followers.setText(String.valueOf(totalfollowers));
                    }
                    if(totalfollowing<10)
                    {
                        _following.setText(String.valueOf("0"+totalfollowing));

                    }
                    else {
                        _following.setText(String.valueOf(totalfollowing));
                    }
                    if(totalvideos<10)
                    {
                        _creations.setText(String.valueOf("0"+totalvideos));

                    }
                    else {
                        _creations.setText(String.valueOf(totalvideos));
                    }
                    if (reactions<10)
                    {

                        _reactions.setText(String.valueOf("0"+reactions));

                    }else {
                        _reactions.setText(String.valueOf(reactions));

                    }
                    if(totalProfilelikes==0||totalProfilelikes==1){
                        _totallikes.setText(String.valueOf(totalProfilelikes)+" like");
                    } else
                    {
                        _totallikes.setText(String.valueOf(totalProfilelikes)+" likes");
                    }

//                    blur_bacground.setVisibility(View.VISIBLE);
//                    loader.setVisibility(View.GONE);
                }
                catch (Exception e) {
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

        FragmentNewProfile2.checkprofileupdated = false;
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
                    hasCoverMedia = userProfile.getHasCoverMedia();
                    totalfollowers = response.body().getFollowers();
                    totalfollowing = response.body().getFollowings();
                    totalvideos = response.body().getTotalVideos();
                    userId = String.valueOf(userProfile.getUserId());
                    gender = userProfile.getGender();
                    userPrefrences=response.body().getPreferences();

                    Long mobilno = userProfile.getPhoneNumber();
                    if (mobilno == null) {
                        mobilenumber = 0L;
                    } else {
                        mobilenumber = mobilno;
                    }
                    countrycode = userProfile.getCountryCode();
                    detail = userProfile.getDescription();

                    if(FragmentNewProfile2.checkpicUpdated){
                        FragmentNewProfile2.checkpicUpdated=false;
                        userProfileUrl=null;
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
                        userCoverUrl=null;

                    }
                    if (hasCoverMedia)
                    {
                        userCoverUrl=userProfile.getCoverMedia().getMediaUrl();
                        Glide.with(context)
                                .load(userCoverUrl)
                                .into(placeholder);
                    }
                    _detail.setText(detail);
                    _name.setText(firstname + " " + lastname);
                    _username.setText("@ "+username);
                    if(totalfollowers<10)
                    {
                        _followers.setText(String.valueOf("0"+totalfollowers));

                    }
                    else {
                        _followers.setText(String.valueOf(totalfollowers));
                    }
                    if(totalfollowing<10)
                    {
                        _following.setText(String.valueOf("0"+totalfollowing));

                    }
                    else {
                        _following.setText(String.valueOf(totalfollowing));
                    }
                    if(totalvideos<10)
                    {
                        _creations.setText(String.valueOf("0"+totalvideos));

                    }
                    else {
                        _creations.setText(String.valueOf(totalvideos));
                    }
                    if (reactions<10)
                    {

                        _reactions.setText(String.valueOf("0"+reactions));

                    }else {
                        _reactions.setText(String.valueOf(reactions));

                    }


                }
                catch (Exception e) {
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
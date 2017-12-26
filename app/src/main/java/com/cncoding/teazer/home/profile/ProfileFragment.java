package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.friends.PublicProfile;
import com.cncoding.teazer.model.user.UserProfile;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.Settings;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import jp.wasabeef.blurry.Blurry;
import pl.droidsonroids.gif.GifTextView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment implements ProfileMyCreationAdapter.OnChildFragmentUpdateVideos {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RC_REQUEST_STORAGE = 1001;
    public static boolean checkprofileupdated = false;
    public static boolean checkpostupdated = false;
    ImageView profile_image;
    ImageView bgImage;
    ImageView settings;
    ImageView small_profile_icon;
    LinearLayout mContainerView;
    Context context;
    AppBarLayout appBarLayout;
    ProximaNovaRegularCheckedTextView _name;
    SignPainterTextView _username;
    TextView _creations;
    TextView _followers;
    TextView _following;
    ViewPager viewPager;
    TabLayout tabLayout;
    ProximaNovaRegularCheckedTextView _detail;
    ImageView backgroundProfile;
    UserProfile userprofile;
    Button btnedit;
    Button btnshare;
    int totalfollowers;
    int totalfollowing;
    int totalvideos;
    String firstname;
    String userId;
    String lastname;
    String username;
    String email;
    int accountType;
    boolean hasProfleMedia;
    Long mobilenumber;
    int gender;
    int countrycode;
    String detail;
    ProgressBar progressbar;
    CircularAppCompatImageView profile_id;
    PublicProfile userProfile;
    NestedScrollView nestedscrollview;
    private String mParam1;
    private String mParam2;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private FollowerListListener mListener;
    private String imageUri;
    private String userProfileThumbnail;
    private String userProfileUrl;
    @BindView(R.id.loader)GifTextView loader;
    @BindView(R.id.blur_bacground)
    CoordinatorLayout blur_bacground;
    int increamentcounter=1;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);

        return newBitmap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        try {
            previousTitle = getParentActivity().getToolbarTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();
        ButterKnife.bind(this,view);
       // loader = view.findViewById(R.id.loader);
        tabLayout = view.findViewById(R.id.sliding_tabs);
        viewPager = view.findViewById(R.id.viewpager);
        _name = view.findViewById(R.id.username);
        _username = view.findViewById(R.id.username_title);
        _creations = view.findViewById(R.id.creations);
        _followers = view.findViewById(R.id.followers);
        _following = view.findViewById(R.id.following);
        _detail = view.findViewById(R.id.hobby);

        backgroundProfile = view.findViewById(R.id.background_profile);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        btnedit = view.findViewById(R.id.btnedit);
        btnshare = view.findViewById(R.id.btnshare);
        progressbar = view.findViewById(R.id.progress_bar);
        profile_id = view.findViewById(R.id.profile_id);
        bgImage = view.findViewById(R.id.background_profile);
        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                if (detail == null)
                    intent.putExtra("Detail", "");
                else {
                    intent.putExtra("Detail", detail);
                }
                startActivity(intent);
            }
        });
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

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            loader.setVisibility(View.GONE);
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        }
                        else
                            loader.setVisibility(View.GONE);
                    }
                });
//                branchUniversalObject.showShareSheet(getActivity(),
//                        linkProperties,
//                        shareSheetStyle,
//                        new Branch.BranchLinkShareListener() {
//                            @Override
//                            public void onShareLinkDialogLaunched() {
//                            }
//
//                            @Override
//                            public void onShareLinkDialogDismissed() {
//                            }
//
//                            @Override
//                            public void onLinkShareResponse(String sharedLink, String sharedChannel, BranchError error) {
//                            }
//
//                            @Override
//                            public void onChannelSelected(String channelName) {
//                            }
//                        });
            }
        });
        _detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//
//
//                   FragmentHobbyDetails reportPostDialogFragment = FragmentHobbyDetails.newInstance(detail,userProfileUrl);
//                    if (fragmentManager != null) {
//                        reportPostDialogFragment.show(fragmentManager, "fragment_report_post");
//
//                }
           }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ProfileFragment.checkprofileupdated) {
            updateProfile();
            checkprofileupdated = false;
        }

        if(ProfileFragment.checkpostupdated)
        {
            viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext()));
            tabLayout.setupWithViewPager(viewPager);
            checkpostupdated=false;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //  super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_user_profile, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getParentActivity().updateToolbarTitle("My Profile");
        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);
        getProfileDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent intent = new Intent(context, Settings.class);
                intent.putExtra("AccountType", String.valueOf(accountType));
                intent.putExtra("UserProfile", userProfile);
                startActivity(intent);
            case R.id.action_profile_block:

        }
        return true;
    }

    public void getProfileDetail() {

        blur_bacground.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);


       // coordinatorLayout.setVisibility(View.GONE);

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
                    if (userProfile.getHasProfileMedia()) {
                        userProfileThumbnail = userProfile.getProfileMedia().getThumbUrl();
                        userProfileUrl = userProfile.getProfileMedia().getMediaUrl();
                    }
                    _detail.setText(detail);
                    _name.setText(firstname);
                    _username.setText(username);
                    _followers.setText(String.valueOf(totalfollowers) + " Followers");
                    _following.setText(String.valueOf(totalfollowing + " Following"));
                    _creations.setText(String.valueOf(totalvideos + " Creations"));
                    if (userProfileUrl != null) {
                        Glide.with(context)
                                .load(userProfileUrl)
                                .into(profile_id);

                        profileBlur(userProfileUrl);
                    }

                    blur_bacground.setVisibility(View.VISIBLE);
                    loader.setVisibility(View.GONE);
                } catch (Exception e) {
                    blur_bacground.setVisibility(View.GONE);
                    loader.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                blur_bacground.setVisibility(View.GONE);
                loader.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }

    @AfterPermissionGranted(RC_REQUEST_STORAGE)
    public void profileBlur(final String pic) {
        String perm = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(getContext(), perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_REQUEST_STORAGE, perm);
        } else {
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
                        Bitmap userImage = scaleDown(result, 200, true);
                        Bitmap photobitmap = Bitmap.createScaledBitmap(result,
                                300, 300, false);
                        Blurry.with(getContext()).from(photobitmap).into(bgImage);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FollowerListListener) {
            mListener = (FollowerListListener) context;
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        getParentActivity().updateToolbarTitle(previousTitle);
    }
    private void dynamicToolbarColor() {
        if (!hasProfleMedia) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.arif_image);
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
        else {
        }
    }
    public void updateProfile() {
        loader.setVisibility(View.VISIBLE);
        blur_bacground.setVisibility(View.GONE);
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
                    if (userProfile.getHasProfileMedia()) {
                        userProfileThumbnail = userProfile.getProfileMedia().getThumbUrl();
                        userProfileUrl = userProfile.getProfileMedia().getMediaUrl();
                    }
                    _detail.setText(detail);
                    _name.setText(firstname);
                    _username.setText(username);
                    _followers.setText(String.valueOf(totalfollowers) + " Followers");
                    _following.setText(String.valueOf(totalfollowing + " Following"));
                    _creations.setText(String.valueOf(totalvideos + " Creations"));
                    if (userProfileThumbnail == null) {
                    } else {
                        Glide.with(context)
                                .load(userProfileUrl)
                                .into(profile_id);
                        profileBlur(userProfileUrl);
                    }
                    loader.setVisibility(View.GONE);
                    blur_bacground.setVisibility(View.VISIBLE);
                } catch (Exception e) {

                    loader.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                loader.setVisibility(View.GONE);

                t.printStackTrace();
            }
        });
    }
    public void updateUsercreations(int count)
    {


    }

    @Override
    public void updateVideosCreation(int count) {
        int counter=totalvideos-count;
        totalvideos=counter;
        _creations.setText(String.valueOf(totalvideos) + " Creations");
    }
    public interface FollowerListListener {
        void onFollowerListListener(String id, String identifier);
        void onFollowingListListener(String id, String identifier);
    }
}

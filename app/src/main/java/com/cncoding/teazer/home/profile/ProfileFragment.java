package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.Settings;
import com.cncoding.teazer.utilities.Pojos;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import jp.wasabeef.blurry.Blurry;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
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
    ImageView backgroundprofile;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    Pojos.User.UserProfile userprofile;
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
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressbar;
    CircularAppCompatImageView profile_id;
    private FollowerListListener mListener;
    private String imageUri;
    private static final int RC_REQUEST_STORAGE = 1001;
    private String userProfileThumbnail;
    private String userProfileUrl;
    PublicProfile userProfile;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        previousTitle=getParentActivity().getToolbarTitle();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        getParentActivity().updateToolbarTitle("My Profile");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = container.getContext();
        _name = view.findViewById(R.id.username);
        _username = view.findViewById(R.id.username_title);
        _creations = view.findViewById(R.id.creations);
        _followers = view.findViewById(R.id.followers);
        _following = view.findViewById(R.id.following);
        _detail = view.findViewById(R.id.hobby);
        backgroundprofile = view.findViewById(R.id.background_profile);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        btnedit = view.findViewById(R.id.btnedit);
        btnshare = view.findViewById(R.id.btnshare);
        coordinatorLayout = view.findViewById(R.id.layout);
        progressbar = view.findViewById(R.id.progress_bar);
        profile_id = view.findViewById(R.id.profile_id);

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
                mListener.onFollowerListListener(String.valueOf(0),"User");
            }
        });
        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFollowingListListener(String.valueOf(0),"User");
            }
        });
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "https://play.google.com/store/apps/details?id=com.app_towertwtm.layout";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Teazer App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

            }
        });

        tabLayout = view.findViewById(R.id.sliding_tabs);
        viewPager = view.findViewById(R.id.viewpager);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getParentActivity().updateToolbarTitle("Profile");

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //  super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_user_profile,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.action_settings:
              Intent intent=new Intent(context, Settings.class);
                        intent.putExtra("AccountType",String.valueOf(accountType));
                        intent.putExtra("UserProfile",userProfile);

              startActivity(intent);


            case R.id.action_profile_block:
               // openBlockUser(followerfollowingid);



        }
        return true;

    }
    public void getProfileDetail() {

        progressbar.setVisibility(View.VISIBLE);
        coordinatorLayout.setVisibility(View.GONE);


        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<Pojos.User.UserProfile>() {
            @Override
            public void onResponse(Call<Pojos.User.UserProfile> call, Response<Pojos.User.UserProfile> response) {

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
                    _followers.setText(String.valueOf(totalfollowers) + " Follower");
                    _following.setText(String.valueOf(totalfollowing + " Following"));
                    _creations.setText(String.valueOf(totalvideos + " Creations"));
                    coordinatorLayout.setVisibility(View.VISIBLE);

                    if (userProfileThumbnail == null) {
//                        final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";
//
//                        Glide.with(context)
//                                .load(pic)
//                                .into(profile_id);
//                        profileBlur(pic);
                    }

                    else {

                        Picasso.with(context)
                                .load(Uri.parse(userProfileThumbnail))
                                .into(profile_id);
                        profileBlur(userProfileUrl);
                    }

                    progressbar.setVisibility(View.GONE);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<Pojos.User.UserProfile> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
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
            progressbar.setVisibility(View.VISIBLE);
            coordinatorLayout.setVisibility(View.GONE);
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
                    progressbar.setVisibility(View.VISIBLE);
                    coordinatorLayout.setVisibility(View.GONE);

                    try {
                        Bitmap photobitmap = Bitmap.createScaledBitmap(result,
                                260, 260, false);
                        Blurry.with(getContext()).from(photobitmap).into(backgroundprofile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressbar.setVisibility(View.GONE);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }.execute();

            progressbar.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
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

    public interface FollowerListListener {
        void onFollowerListListener(String id,String identifier);
        void onFollowingListListener(String id,String identifier);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context));
        tabLayout.setupWithViewPager(viewPager);
        getProfileDetail();
        getParentActivity().showAppBar();
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
        } else {
        }
    }
}

package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaCondensedTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.model.profile.userProfile.UserProfile;
import com.cncoding.teazer.model.profile.userProfile.UserProfileResponse;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.FollowersListActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowingListActivities;
import com.cncoding.teazer.ui.fragment.activity.Settings;
import com.cncoding.teazer.utilities.Pojos;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

import jp.wasabeef.blurry.Blurry;
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
    ImageView backbutton;
    ImageView small_profile_icon;
    LinearLayout mContainerView;
    Context context;
    AppBarLayout appBarLayout;
    ProximaNovaCondensedTextView _toolbarusername;
    ProximaNovaRegularCheckedTextView _name;
    SignPainterTextView _username;
    TextView _creations;
    TextView _followers;
    TextView _following;
    ProximaNovaRegularCheckedTextView _detail;
    ImageView backgroundprofile;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    Pojos.User.UserProfile userprofile;
    RemoveAppBar removeAppBar;
    Button btnedit;
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
    //    Pojos.User.UserProfile userprofile;
    Long mobilenumber;
    int gender;
    int countrycode;
    String detail;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressbar;
    CircularAppCompatImageView profile_id;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = container.getContext();
        removeAppBar = (RemoveAppBar) context;
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        backbutton = view.findViewById(R.id.backbutton);
        settings = view.findViewById(R.id.settings);
        _toolbarusername = view.findViewById(R.id.toolbarusername);
        _name = view.findViewById(R.id.username);
        _username = view.findViewById(R.id.username_title);
        _creations = view.findViewById(R.id.creations);
        _followers = view.findViewById(R.id.followers);
        _following = view.findViewById(R.id.following);
        _detail = view.findViewById(R.id.hobby);
        backgroundprofile = view.findViewById(R.id.background_profile);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        btnedit = view.findViewById(R.id.btnedit);
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

                Intent intent = new Intent(context, FollowersListActivity.class);
                intent.putExtra("FollowerId", String.valueOf(0));
                intent.putExtra("Identifier", "User");
                startActivity(intent);
            }
        });
        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, FollowingListActivities.class);
                intent.putExtra("FollowerId", String.valueOf(0));
                intent.putExtra("Identifier", "User");
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Settings.class);
                intent.putExtra("AccountType", String.valueOf(accountType));
                startActivity(intent);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().onBackPressed();
            }
        });

        ViewPager viewPager = view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context));
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        appBarLayout = view.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset < -530) {
                    _toolbarusername.setVisibility(View.VISIBLE);

                } else {
                    _toolbarusername.setVisibility(View.INVISIBLE);
                }
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
    public void getProfileDetail() {

        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<Pojos.User.UserProfile>() {
            @Override
            public void onResponse(Call<Pojos.User.UserProfile> call, Response<Pojos.User.UserProfile> response) {
                Log.d("Response", response.toString());


                try {
                     PublicProfile userProfile = response.body().getUserProfile();
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
                    _toolbarusername.setText(firstname);
                    _detail.setText(detail);
                    _name.setText(firstname);
                    _username.setText(username);

                    Log.d("Gender", detail);
                    _followers.setText(String.valueOf(totalfollowers) + " Follower");
                    _following.setText(String.valueOf(totalfollowing + " Following"));
                    _creations.setText(String.valueOf(totalvideos + " Creations"));
                    progressbar.setVisibility(View.GONE);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    profileBlur();

                    SharedPreferences prfs = context.getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                    String imageUri = prfs.getString("MYIMAGES", "");
                    if (imageUri == null) {
                        final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";

                        Glide.with(context)
                                .load(pic)
                                .into(profile_id);
                        profileBlur();

                    } else {

                        Picasso.with(context)
                                .load(Uri.parse(imageUri))
                                .into(profile_id);

                    }
                    if (hasProfleMedia) {

                    } else {
                    }

                    progressbar.setVisibility(View.GONE);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Pojos.User.UserProfile> call, Throwable t) {

                Log.d("errror", t.getMessage());
            }
        });

    }
    public void profileBlur()
    {

        progressbar.setVisibility(View.VISIBLE);
        coordinatorLayout.setVisibility(View.GONE);
        final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";

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

                }

                return bitmap;
            }
            @Override
            protected void onPostExecute(final Bitmap result) {
                Blurry.with(context).from(result).into(backgroundprofile);
                progressbar.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);


            }
        }.execute();


        progressbar.setVisibility(View.GONE);
        coordinatorLayout.setVisibility(View.VISIBLE);

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        progressbar.setVisibility(View.VISIBLE);
        coordinatorLayout.setVisibility(View.GONE);
        getProfileDetail();

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

    public interface RemoveAppBar {
        void removeAppbar();
    }


}
package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.profile.userProfile.UserProfileResponse;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.FollowersListActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowingListActivities;
import com.cncoding.teazer.ui.fragment.activity.Settings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 18/11/17.
 */

public class ProfileFragmentNew extends BaseFragment {


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
    TextView _toolbarusername;
    TextView _name;
    TextView _username;
    TextView _creations;
    TextView _followers;
    TextView _following;
    TextView _detail;
    ImageView backgroundprofile;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

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
    Long mobilenumber;
    int gender;
    int countrycode;
    String detail;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressbar;
    private ProfileFragment.OnFragmentInteractionListener mListener;

    public ProfileFragmentNew() {
    }
    public static ProfileFragmentNew newInstance() {
        ProfileFragmentNew fragment = new ProfileFragmentNew();
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
                else
                {intent.putExtra("Detail", detail);}
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
                intent.putExtra("AccountType",String.valueOf(accountType));
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

       // Toast.makeText(context,"hello new Fragment",Toast.LENGTH_LONG).show();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       // super.onViewCreated(view, savedInstanceState);
      //  progressbar.setVisibility(View.VISIBLE);
      //  coordinatorLayout.setVisibility(View.GONE);
      //  removeAppBar.removeAppbar();
        getProfileDetail();

    }

    public void getProfileDetail() {








    }}





package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.ui.fragment.activity.FollowersActivity;
import com.cncoding.teazer.ui.fragment.activity.FollowingActivities;
import com.cncoding.teazer.ui.fragment.activity.Settings;
import com.cncoding.teazer.utilities.Pojos;

import java.util.List;

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
    TextView _toolbarusername;
    TextView _name;
    TextView _username;
    TextView _creations;
    TextView _followers;
    TextView _following;
    ImageView backgroundprofile;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    Pojos.User.UserProfile userprofile;
    boolean hasProfleMedia;
    RemoveAppBar removeAppBar;



    private OnFragmentInteractionListener mListener;
    public ProfileFragment() {
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        context=container.getContext();
        removeAppBar=(RemoveAppBar)context;

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        backbutton= view.findViewById(R.id.backbutton);
        settings= view.findViewById(R.id.settings);
        _toolbarusername= view.findViewById(R.id.toolbarusername);
        _username= view.findViewById(R.id.username);
        _name= view.findViewById(R.id.username_title);
        _creations= view.findViewById(R.id.creations);
        _followers= view.findViewById(R.id.followers);
        _following= view.findViewById(R.id.following);
//        profile_image= (CircularAppCompatImageView) view.findViewById(R.id.profile_id);
//        bgImage= (CircularAppCompatImageView)view.findViewById(R.id.profile_id);
        backgroundprofile=view.findViewById(R.id.background_profile);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);


        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(context, FollowersActivity.class);
            startActivity(intent);
            }
        });
        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(context, FollowingActivities.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, Settings.class);
                startActivity(intent);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                getActivity().onBackPressed();
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        ViewPager viewPager =  view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context));
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        appBarLayout =  view.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset<-530)
                {
                    _toolbarusername.setVisibility(View.VISIBLE);

                }
                else
                {
                    _toolbarusername.setVisibility(View.INVISIBLE);

                }
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        removeAppBar.removeAppbar();
        dynamicToolbarColor();
        getProfileDetail();


    }
    public void getProfileDetail()
    {
        ApiCallingService.User.getUserProfileDetail(context).enqueue(new Callback<Pojos.User.Profile>() {
            @Override
            public void onResponse(Call<Pojos.User.Profile> call, Response<Pojos.User.Profile> response) {
                if(response.code()==200)
                {
                    try {
                        Pojos.User.UserProfile userProfile = response.body().getUserProfile();
                        int totalfollowers = response.body().getFollowers();
                        int totalfollowing = response.body().getFollowings();
                        int totalvideos = response.body().getTotalVideos();
                        String firstname=userProfile.getFirstName();
                        String lastname=userProfile.getLastName();
                        String username=userProfile.getUsername();
                        String email=userProfile.getEmail();
                        int accountType=userProfile.getAccountType();
                        hasProfleMedia=userProfile.hasProfileMedia();


                        List<Pojos.Category> categoryList=userProfile.getCategories();

                        if(hasProfleMedia) {
    //                        Picasso.with(context).
    //                                load(newUrl)
    //                                .placeholder(ContextCompat.getDrawable(context, R.drawable.blankimage))
    //                                .into(imageView);
                        }
                        else {
                            backgroundprofile.setBackgroundResource(R.drawable.material_flat);
                        }
                        Pojos.ProfileMedia profile_media = userProfile.getProfileMedia();
                        if (profile_media != null) {
                            String thumburl = profile_media.getThumbUrl();
                            String mediaurl = profile_media.getMediaUrl();
                        }
                        _toolbarusername.setText(firstname);
                        _name.setText(firstname);
                        _username.setText(username);
                        _followers.setText(String.valueOf(totalfollowers)+" Follower");
                        _following.setText(String.valueOf(totalfollowing+ " Following"));
                        _creations.setText(String.valueOf(totalvideos +" Creations"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                {

                    Toast.makeText(context,"UserProfile Detail not fetched",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Pojos.User.Profile> call, Throwable t) {

            }
        });

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


    }

    private void dynamicToolbarColor() {
        if (!hasProfleMedia) {
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
    }
    public interface RemoveAppBar {
        void removeAppbar();
    }
}
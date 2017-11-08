package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.ui.fragment.activity.Settings;
import com.cncoding.teazer.utilities.BlurBuilder;

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
    TextView toolbarusername;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private OnFragmentInteractionListener mListener;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view=inflater.inflate(R.layout.fragment_profile, container, false);
         context=container.getContext();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        backbutton= view.findViewById(R.id.backbutton);
        settings= view.findViewById(R.id.settings);
        toolbarusername= view.findViewById(R.id.toolbarusername);
        profile_image= (CircularAppCompatImageView) view.findViewById(R.id.profile_id);
        bgImage= (CircularAppCompatImageView)view.findViewById(R.id.profile_id);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);


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


        toolbarusername.setText(R.string.name);
        //small_profile_icon= (CircularAppCompatImageView)view.findViewById(R.id.small_profile_icon);
        //mContainerView = view.findViewById(R.id.container);
        ViewPager viewPager =  view.findViewById(R.id.viewpager);
        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), context));
        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arifimage);
        Bitmap blurredBitmap = BlurBuilder.blur( getActivity(), originalBitmap );
        collapsingToolbarLayout.setBackground(new BitmapDrawable(getResources(), blurredBitmap));
        appBarLayout =  view.findViewById(R.id.appbar);
        //collapsingToolbarLayout.setTitle(getResources().getString(R.string.username));
        dynamicToolbarColor();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(verticalOffset<-530)
                {
                    toolbarusername.setVisibility(View.VISIBLE);

                }
                else
                {
                    toolbarusername.setVisibility(View.INVISIBLE);


                }
            }
        });
        return view;

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


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.arifimage);

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


    public interface RemoveAppBar
    {
        public void removeAppbar();
    }


}

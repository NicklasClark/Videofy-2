package com.cncoding.teazer.ui.home.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.profile.Preference;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentChangeCategories;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentDeactivateAccount;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentHideVideos;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentSettings;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static android.R.anim.slide_in_left;
import static com.cncoding.teazer.R.anim.slide_in_right;
import static com.cncoding.teazer.R.anim.slide_out_left;
import static com.cncoding.teazer.R.anim.slide_out_right;


public class Settings extends AppCompatActivity implements FragmentSettings.ChangeCategoriesListener{

    Context context;
    ArrayList<Preference> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        context=this;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_dark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();
            }
        });

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#0000000'>Settings</font>"));
        Intent intent=getIntent();
        int accountType = Integer.parseInt(intent.getStringExtra("AccountType"));
        list=intent.getExtras().getParcelableArrayList("UserPrefrences");

        // int i=list.get(0).getValue();
        // int j=list.get(0).getPreferenceId();
        //  Toast.makeText(context,String.valueOf(j),Toast.LENGTH_SHORT).show();
       // Toast.makeText(context,String.valueOf(list.get(0).getPreferenceId()),Toast.LENGTH_SHORT).show();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.container, FragmentSettings.
                        newInstance(String.valueOf(accountType),list)).commit();
    }

    @Override
    public void changeCategoriesListener() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(slide_in_right, slide_out_left, slide_in_left, slide_out_right)
                .replace(R.id.container, FragmentChangeCategories.newInstance())
                .addToBackStack("FragmentChangeCategories")
                .commit();
    }

    @Override
    public void deactivateAccountListener() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(slide_in_right, slide_out_left, slide_in_left, slide_out_right)
                .replace(R.id.container, FragmentDeactivateAccount.newInstance())
                .addToBackStack("FragmentDeactivateAccount")
                .commit();
    }

    @Override
    public void hideVideoList() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(slide_in_right, slide_out_left, slide_in_left, slide_out_right)
                .replace(R.id.container, FragmentHideVideos.newInstance(1))
                .addToBackStack("FragmentHideVideos")
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else
            super.onBackPressed();
    }



}
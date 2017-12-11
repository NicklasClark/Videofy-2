package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.cncoding.teazer.R;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.ui.fragment.fragment.FragmentChangeCategories;

import com.cncoding.teazer.ui.fragment.fragment.FragmentDeactivateAccount;
import com.cncoding.teazer.ui.fragment.fragment.FragmentSettings;

import butterknife.ButterKnife;



public class Settings extends AppCompatActivity implements FragmentSettings.ChangeCategoriesListener{

    Context context;
    private PublicProfile userProfile;
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

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);

                    onBackPressed();
            }
        });

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>Settings</font>"));
        Intent intent=getIntent();
        int accoutType=Integer.parseInt(intent.getStringExtra("AccountType"));
        userProfile=intent.getExtras().getParcelable("UserProfile");
        FragmentSettings fragment= FragmentSettings.newInstance(String.valueOf(accoutType));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();

    }
    @Override
    public void changeCategoriesListener() {

        FragmentChangeCategories fragment= FragmentChangeCategories.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }
    @Override
    public void deactivateAccountListener() {
        FragmentDeactivateAccount fragment= FragmentDeactivateAccount.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();

    }


}

package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


/**
 *
 * Created by Prem $ on 11/02/17.
 */

@SuppressWarnings("unused")
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(Toolbar toolbar, boolean isBackEnabled) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isBackEnabled);
            actionBar.setDisplayShowHomeEnabled(isBackEnabled);
            if(isBackEnabled)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_previous);
        }
    }

    public void initToolbar(Toolbar toolbar, String title, boolean isBackEnabled) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isBackEnabled);
            actionBar.setDisplayShowHomeEnabled(isBackEnabled);
            if (isBackEnabled)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_previous);
        }
        getSupportActionBar().setTitle(title);
    }
}
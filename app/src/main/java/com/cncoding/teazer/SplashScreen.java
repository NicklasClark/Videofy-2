package com.cncoding.teazer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static com.cncoding.teazer.utilities.AuthUtils.isUserLoggedIn;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isUserLoggedIn(getApplicationContext())) {
            startActivity(new Intent(SplashScreen.this, BaseBottomBarActivity.class));
        } else {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
        }
        finish();
    }
}

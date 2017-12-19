package com.cncoding.teazer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

import static com.cncoding.teazer.BaseBottomBarActivity.NOTIFICATION_TYPE;
import static com.cncoding.teazer.utilities.AuthUtils.isUserLoggedIn;
import static com.cncoding.teazer.utilities.CommonWebServicesUtil.getFollowingNotificationsUnreadCount;
import static com.cncoding.teazer.utilities.CommonWebServicesUtil.getRequestNotificationsUnreadCount;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.;
//        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build());

        if (isUserLoggedIn(getApplicationContext())) {
//            if(getIntent().getExtras() != null)
//            {
//                Bundle bundle = getIntent().getExtras().getBundle("bundle");
//                if (bundle != null) {
////                int index = bundle.getInt(TAB_INDEX);
////                if (index != -1)
////                    switchTab(index);
//                    Log.d("NOTIFYM", "BUNDLE Exists on splash");
//                    Log.d("NOTIFYM", String.valueOf(bundle.getInt(NOTIFICATION_TYPE)));
////                    int notification_type = bundle.getInt(NOTIFICATION_TYPE);
////                    int source_id = bundle.getInt(SOURCE_ID);
////                    notificationAction(notification_type, source_id);
//                }
//                else
//                    Log.d("NOTIFYM", "BUNDLE does not Exists on Splash");
//            }
//            else
//                Log.d("NOTIFYM", "Intent is null");
            getFollowingNotificationsUnreadCount(this, 1);
            getRequestNotificationsUnreadCount(this, 1);

            startActivity(new Intent(SplashScreen.this, BaseBottomBarActivity.class));
        } else {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
        }

        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if(getIntent().getExtras() != null)
        {
            Bundle bundle = getIntent().getExtras().getBundle("bundle");
            if (bundle != null) {
//                int index = bundle.getInt(TAB_INDEX);
//                if (index != -1)
//                    switchTab(index);
                Log.d("NOTIFYM", "BUNDLE Exists on splash");
                Log.d("NOTIFYM", String.valueOf(bundle.getInt(NOTIFICATION_TYPE)));
//                    int notification_type = bundle.getInt(NOTIFICATION_TYPE);
//                    int source_id = bundle.getInt(SOURCE_ID);
//                    notificationAction(notification_type, source_id);
            }
            else
                Log.d("NOTIFYM", "BUNDLE does not Exists on Splash");
        }
        else
            Log.d("NOTIFYM", "Intent is null");

    }
}

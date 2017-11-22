package com.cncoding.teazer;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 *
 * Created by Prem $ on 11/21/2017.
 */

public class TeazerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}

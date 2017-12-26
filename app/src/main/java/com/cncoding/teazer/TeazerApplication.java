package com.cncoding.teazer;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import iknow.android.utils.BaseUtils;
import io.branch.referral.Branch;

/**
 *
 * Created by amit on 30/11/17.
 */

public class TeazerApplication extends Application {

    private static final String TAG = "Application";

    @Override
    public void onCreate() {
        super.onCreate();
        BaseUtils.init(this);
//        initImageLoader(this);
        initFFmpegBinary(this);

        // Initialize the Branch object
        Branch.getAutoInstance(this);

    }

    private void initFFmpegBinary(Context context) {

        try {
            FFmpeg.getInstance(context).loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                }
            });

        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}

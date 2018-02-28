package com.cncoding.teazer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.util.Log;

import com.expletus.mobiruck.MobiruckSdk;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import org.jetbrains.annotations.Contract;

import iknow.android.utils.BaseUtils;
import io.branch.referral.Branch;

/**
 *
 * Created by amit on 30/11/17.
 */

public class TeazerApplication extends Application  {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        BaseUtils.init(this);
//        initImageLoader(this);
        initFFmpegBinary(this);

        // Initialize the Branch object
        Branch.getAutoInstance(this);
        final FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);

        final EmojiCompat.Config  config = new
                FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onInitialized() {
                        Log.i("MyApplication", "EmojiCompat initialized");
                    }

                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e("MyApplication", "EmojiCompat initialization failed", throwable);
                    }
                });
        EmojiCompat.init(config);

        //Mobiruck initialization
        MobiruckSdk.getInstance().init(getApplicationContext(), getApplicationContext().getString(R.string.mobiruck_api_key), true);

        MobiruckSdk.getInstance().enableLog(true);  // this controls log prints in sdk

        MobiruckSdk.getInstance().startTracking();  // this starts the tracking system.

//        applicationComponent = DaggerApplicationComponent
//                .builder()
//                .applicationModule(new ApplicationModule(this))
//                .roomModule(new RoomModule(this))
//                .build();
    }

//    public ApplicationComponent getApplicationComponent() {
//        return applicationComponent;
//    }

    @Contract(pure = true)
    public static Context getContext() {
        return context;
    }

    public static TeazerApplication get(Activity activity) {
        return (TeazerApplication) activity.getApplication();
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
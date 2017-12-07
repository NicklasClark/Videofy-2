package com.cncoding.teazer.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.cncoding.teazer.utilities.Pojos.UploadParams;

import static com.cncoding.teazer.utilities.ViewUtils.UPLOAD_PARAMS;

/**
 *
 * Created by Prem $ on 12/7/2017.
 */

public class UploadService extends IntentService {

    public static void launchUploadService(Context context, UploadParams uploadParams) {
        Intent intent = new Intent(context, UploadService.class);
        intent.putExtra(UPLOAD_PARAMS, uploadParams);
        context.startService(intent);
    }

    public UploadService() {
        super("uploadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}

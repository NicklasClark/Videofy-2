package com.cncoding.teazer.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.services.receivers.ReactionUploadReceiver;
import com.cncoding.teazer.utilities.Pojos.UploadParams;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_COMPLETE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.utilities.SharedPrefs.finishReactionUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.saveReactionUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.UPLOAD_PARAMS;

/**
 *
 * Created by Prem $ on 12/7/2017.
 */

public class ReactionUploadService extends IntentService implements ProgressRequestBody.UploadCallbacks {

    private static final String REACTION_UPLOAD_RECEIVER = "reactionUploadReceiver";
    private ResultReceiver receiver;
    private Bundle bundle;
//    private int resultCode;

    public static void launchReactionUploadService(Context context, UploadParams uploadParams, ReactionUploadReceiver reactionUploadReceiver) {
        Intent intent = new Intent(context, ReactionUploadService.class);
        intent.putExtra(UPLOAD_PARAMS, uploadParams);
        intent.putExtra(REACTION_UPLOAD_RECEIVER, reactionUploadReceiver);
        context.startService(intent);
    }

    public ReactionUploadService() {
        super("uploadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            finishReactionUploadSession(getApplicationContext());
            receiver = intent.getParcelableExtra(REACTION_UPLOAD_RECEIVER);
            UploadParams uploadParams = intent.getParcelableExtra(UPLOAD_PARAMS);
            bundle = new Bundle();
            MultipartBody.Part part;
            try {
                saveReactionUploadSession(getApplicationContext(), uploadParams);
                File videoFile = new File(uploadParams.getVideoPath());
                ProgressRequestBody videoBody = new ProgressRequestBody(videoFile, this);
                part = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);

                ApiCallingService.React.uploadReaction(part, uploadParams.getPostDetails().getPostId(),
                        getApplicationContext(), uploadParams.getTitle())
                        .enqueue(new Callback<ResultObject>() {
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                                resultCode = response.code();
                                try {
                                    if (response.code() == 201) {
                                        onUploadFinish();
                                        finishReactionUploadSession(getApplicationContext());
                                    } else onUploadError(new Throwable(response.code() + " : " +response.message()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    onUploadError(e);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                t.printStackTrace();
                                onUploadError(t);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
//        bundle.clear();
        bundle.putInt(UPLOAD_PROGRESS, percentage);
        receiver.send(UPLOAD_IN_PROGRESS_CODE, bundle);
    }

    @Override
    public void onUploadError(Throwable throwable) {
        bundle.clear();
        bundle.putString(UPLOAD_ERROR, throwable.getMessage() != null ? throwable.getMessage() : "Something went wrong");
        receiver.send(UPLOAD_ERROR_CODE, bundle);
    }

    @Override
    public void onUploadFinish() {
        bundle.clear();
        bundle.putString(UPLOAD_COMPLETE, "Video successfully uploaded");
        receiver.send(UPLOAD_COMPLETE_CODE, bundle);
    }
}
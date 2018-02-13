package com.cncoding.teazer.data.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.model.base.UploadParams;
import com.cncoding.teazer.model.react.ReactionUploadResult;
import com.cncoding.teazer.data.receiver.ReactionUploadReceiver;
import com.cncoding.teazer.utilities.SharedPrefs;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

<<<<<<< HEAD:app/src/main/java/com/cncoding/teazer/services/ReactionUploadService.java
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_COMPLETE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.services.VideoUploadService.UPLOAD_PROGRESS;
=======
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_COMPLETE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_COMPLETE_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_ERROR;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_ERROR_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_IN_PROGRESS_CODE;
import static com.cncoding.teazer.data.service.VideoUploadService.UPLOAD_PROGRESS;
import static com.cncoding.teazer.utilities.CommonUtilities.deleteFilePermanently;
>>>>>>> prem_dev:app/src/main/java/com/cncoding/teazer/data/service/ReactionUploadService.java
import static com.cncoding.teazer.utilities.SharedPrefs.finishReactionUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.saveReactionUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.UPLOAD_PARAMS;

/**
 *
 * Created by Prem $ on 12/7/2017.
 */

public class ReactionUploadService extends IntentService implements ProgressRequestBody.UploadCallbacks {

    private static final String REACTION_UPLOAD_RECEIVER = "reactionUploadReceiver";
    private static final String ADD_WATERMARK = "addWatermark";
    private static final String VIDEO_PATH = "videoPath";
    private ResultReceiver receiver;
    private Bundle bundle;
    private Call<ReactionUploadResult> reactionUploadCall;
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




                if (reactionUploadCall == null) {
                    reactionUploadCall = ApiCallingService.React.uploadReaction(part, uploadParams.getPostDetails().getPostId(),
                            getApplicationContext(), uploadParams.getTitle());
                }

                try {
                    Response<ReactionUploadResult> response = reactionUploadCall.execute();

                    if (response.isSuccessful()) {
                        try {
                                onUploadFinish(uploadParams.getVideoPath(), uploadParams.isGallery());
                                finishReactionUploadSession(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                            onUploadError(e.getMessage());
                        }
                    }
                    else {
                        onUploadError(response.body().getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onUploadError(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onUploadError(null);
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
    public void onUploadError(String errorMessage) {
        bundle.clear();
        bundle.putString(UPLOAD_ERROR, errorMessage != null ? errorMessage:"Something went wrong");
        receiver.send(UPLOAD_ERROR_CODE, bundle);
    }

    @Override
    public void onUploadFinish(String videoPath, boolean gallery) {
        bundle.clear();

        if (!SharedPrefs.getSaveVideoFlag(getApplicationContext()) && !gallery) {
            bundle.putBoolean(ADD_WATERMARK, false);
        }
        else if(!gallery)
        {
            bundle.putBoolean(ADD_WATERMARK, true);
        }
        bundle.putString(VIDEO_PATH, videoPath);
        bundle.putString(UPLOAD_COMPLETE, "Video uploaded successfully");
        receiver.send(UPLOAD_COMPLETE_CODE, bundle);
    }
}
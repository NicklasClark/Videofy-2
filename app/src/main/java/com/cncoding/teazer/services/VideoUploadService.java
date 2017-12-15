package com.cncoding.teazer.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ProgressRequestBody.UploadCallbacks;
import com.cncoding.teazer.model.base.UploadParams;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.services.receivers.VideoUploadReceiver;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.saveVideoUploadSession;
import static com.cncoding.teazer.utilities.ViewUtils.UPLOAD_PARAMS;

/**
 *
 * Created by Prem $ on 12/7/2017.
 */

public class VideoUploadService extends IntentService implements UploadCallbacks {

    private static final String VIDEO_UPLOAD_RECEIVER = "videoUploadReceiver";
    public static final String UPLOAD_PROGRESS = "uploadProgress";
    public static final String UPLOAD_ERROR = "uploadErrorMessage";
    public static final String UPLOAD_COMPLETE = "uploadComplete";
    public static final String RESPONSE_CODE = "responseCode";
    public static final int UPLOAD_IN_PROGRESS_CODE = 10;
    public static final int UPLOAD_ERROR_CODE = 11;
    public static final int UPLOAD_COMPLETE_CODE = 12;

    private ResultReceiver receiver;
    private Bundle bundle;
//    private int resultCode;
    private Call<PostUploadResult> videoUploadCall;

    public static void launchVideoUploadService(Context context, UploadParams uploadParams, VideoUploadReceiver videoUploadReceiver) {
        Intent intent = new Intent(context, VideoUploadService.class);
        intent.putExtra(UPLOAD_PARAMS, uploadParams);
        intent.putExtra(VIDEO_UPLOAD_RECEIVER, videoUploadReceiver);
        context.startService(intent);
    }

    public VideoUploadService() {
        super("uploadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            receiver = intent.getParcelableExtra(VIDEO_UPLOAD_RECEIVER);
            UploadParams uploadParams = intent.getParcelableExtra(UPLOAD_PARAMS);
            bundle = new Bundle();
            MultipartBody.Part part;
            try {
                saveVideoUploadSession(getApplicationContext(), uploadParams);
                File videoFile = new File(uploadParams.getVideoPath());
                ProgressRequestBody videoBody = new ProgressRequestBody(videoFile, this);
                part = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);

                if (videoUploadCall == null)
                    videoUploadCall = ApiCallingService.Posts.uploadVideo(
                            part, uploadParams.getTitle(), uploadParams.getLocation(), uploadParams.getLatitude(),
                            uploadParams.getLongitude(), uploadParams.getTags(), uploadParams.getCategories(), getApplicationContext());

                if (!videoUploadCall.isExecuted())
                    videoUploadCall.enqueue(new Callback<PostUploadResult>() {
                        @Override
                        public void onResponse(Call<PostUploadResult> call, Response<PostUploadResult> response) {
                            try {
                                if (response.code() == 201) {
                                    onUploadFinish();
                                    finishVideoUploadSession(getApplicationContext());
                                 //   String videoUrl=response.body().

                                } else onUploadError(new Throwable(response.code() + " : " +response.message()));
                            } catch (Exception e) {
                                e.printStackTrace();
                                onUploadError(e);
                            }
                        }

                        @Override
                        public void onFailure(Call<PostUploadResult> call, Throwable t) {
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
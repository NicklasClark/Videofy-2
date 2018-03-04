package com.cncoding.teazer.data.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.data.apiCalls.ProgressRequestBody.UploadCallbacks;
import com.cncoding.teazer.data.model.base.UploadParams;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.receiver.VideoUploadReceiver;
import com.cncoding.teazer.utilities.common.SharedPrefs;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.common.SharedPrefs.saveVideoUploadSession;
import static com.cncoding.teazer.utilities.common.ViewUtils.UPLOAD_PARAMS;

/**
 *
 * Created by Prem $ on 12/7/2017.
 */

public class VideoUploadService extends IntentService implements UploadCallbacks {

    private static final String VIDEO_UPLOAD_RECEIVER = "videoUploadReceiver";
    public static final String UPLOAD_PROGRESS = "uploadProgress";
    public static final String UPLOAD_ERROR = "uploadErrorMessage";
    public static final String UPLOAD_COMPLETE = "uploadComplete";
    public static final String ADD_WATERMARK = "addWatermark";
    public static final String VIDEO_PATH = "videoPath";
    public static final int UPLOAD_IN_PROGRESS_CODE = 10;
    public static final int UPLOAD_ERROR_CODE = 11;
    public static final int UPLOAD_COMPLETE_CODE = 12;

    private ResultReceiver receiver;
    private Bundle bundle;
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
                try {
                    Response<PostUploadResult> response = videoUploadCall.execute();
                    if (response.isSuccessful()) {
                        PostUploadResult postUploadResult = response.body();
                        int postId = postUploadResult.getPostDetails().getPostId();
                        String postTitle = postUploadResult.getPostDetails().getTitle();
                        String postThumbUrl = postUploadResult.getPostDetails().getMedias().get(0).getThumbUrl();
                        String postOwner = postUploadResult.getPostDetails().getPostOwner().getUserName();
                        onUploadFinish(uploadParams.getVideoPath(), uploadParams.isGallery());
                        finishVideoUploadSession(getApplicationContext());
                        sendBroadcast(postId, postTitle, postThumbUrl, postOwner);
                    }
                    else {
                        onUploadError(null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    onUploadError(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void sendBroadcast (int postId,String postTitle,String postURL,String postOwner){
        Intent intent = new Intent ("message");
        intent.putExtra("PostID", String.valueOf(postId));
        intent.putExtra("PostTitle", postTitle);
        intent.putExtra("PostURL",postURL );
        intent.putExtra("PostOwner", postOwner);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
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
        else if(!gallery) {
            bundle.putBoolean(ADD_WATERMARK, true);
        }
        bundle.putString(VIDEO_PATH, videoPath);
        bundle.putString(UPLOAD_COMPLETE, "Video uploaded successfully");
        receiver.send(UPLOAD_COMPLETE_CODE, bundle);
    }
}
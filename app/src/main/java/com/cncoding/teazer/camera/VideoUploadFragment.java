package com.cncoding.teazer.camera;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;

import static com.cncoding.teazer.apiCalls.ApiCallingService.FAIL;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_FALSE;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_TRUE;

public class VideoUploadFragment extends Fragment implements ProgressRequestBody.UploadCallbacks {

    public static final String ARG_THUMBNAIL = "thumbnail";
    private static final String VIDEO_PATH = "videoPath";
    private byte[] thumbnail;
    private String videoPath;

    @BindView(R.id.thumbnail) ImageView thumbnailView;
    @BindView(R.id.video_upload_cancel_btn) Button backBtn;
    @BindView(R.id.video_upload_check_btn) Button uploadBtn;
    @BindView(R.id.video_upload_progress_bar) ProgressBar videoUploadProgress;
    @BindView(R.id.video_upload_progress_layout) LinearLayout videoUploadProgressLayout;
    private OnVideoUploadInteractionListener mListener;

    public VideoUploadFragment() {
        // Required empty public constructor
    }

    public static VideoUploadFragment newInstance(byte[] thumbnail, String videoPath) {
        VideoUploadFragment fragment = new VideoUploadFragment();
        Bundle args = new Bundle();
        args.putByteArray(ARG_THUMBNAIL, thumbnail);
        args.putString(VIDEO_PATH, videoPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            thumbnail = getArguments().getByteArray(ARG_THUMBNAIL);
            videoPath = getArguments().getString(VIDEO_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_upload, container, false);
        ButterKnife.bind(this, rootView);

        thumbnailView.setImageBitmap(BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length));

        return rootView;
    }

    @OnClick(R.id.video_upload_check_btn)
    public void onUploadBtnClick() {
        showProgressLayout();
        File videoFile = new File(videoPath);
        ProgressRequestBody videoBody = new ProgressRequestBody(videoFile, this);
//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPartFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);

        switch (ApiCallingService.uploadVideo(videoPartFile, videoFile)) {
            case SUCCESS_OK_TRUE:
                onUploadFinish();
                break;
            case SUCCESS_OK_FALSE:
                onUploadError();
                break;
            case FAIL:
                onUploadError();
                break;
            default:
                break;
        }
    }

    private void showProgressLayout() {
        videoUploadProgressLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        videoUploadProgressLayout.setVisibility(View.VISIBLE);
    }

    private void hideProgressLayout() {
        videoUploadProgressLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        videoUploadProgressLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        videoUploadProgress.setProgress(percentage);
    }

    @Override
    public void onUploadError() {
        hideProgressLayout();
        mListener.onVideoUploadInteraction(CameraActivity.VIDEO_UPLOAD_ERROR_ACTION);
    }

    @Override
    public void onUploadFinish() {
        hideProgressLayout();
        mListener.onVideoUploadInteraction(CameraActivity.VIDEO_UPLOAD_FINISHED_ACTION);
    }

//    @NonNull
//    private RequestBody createPartFromString(String descriptionString) {
//        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVideoUploadInteractionListener) {
            mListener = (OnVideoUploadInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVideoUploadInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnVideoUploadInteractionListener {
        void onVideoUploadInteraction(int action);
    }
}

package com.cncoding.teazer.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cncoding.teazer.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class VideoPreviewFragment extends Fragment implements TextureView.SurfaceTextureListener {

    private static final String VIDEO_PATH = "videoPath";
    private static final String WHICH_CAMERA = "whichCamera";
    public static final int VIDEO_PREVIEW_ACTION_CANCEL = 1;
    public static final int VIDEO_PREVIEW_ACTION_CHECK = 2;
//    private static final String TAG = "VideoPreviewFragment";

    private String videoPath;
    private String whichCamera;
    private String videoDuration;
    private boolean isVideoFlipped;

    @BindView(R.id.video_preview) TextureView videoPreview;
    @BindView(R.id.video_preview_cancel_btn) ImageButton cancelButton;
    @BindView(R.id.video_preview_check_btn) ImageButton checkButton;
    @BindView(R.id.video_duration) TextView videoDurationTextView;
    @BindView(R.id.flip_video_btn) ImageButton flipVideoBtn;

    private MediaPlayer mediaPlayer;
    private OnVideoPreviewInteractionListener mListener;

    public VideoPreviewFragment() {
        // Required empty public constructor
    }

    public static VideoPreviewFragment newInstance(String videoPath, String whichCamera) {
        VideoPreviewFragment fragment = new VideoPreviewFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_PATH, videoPath);
        args.putString(WHICH_CAMERA, whichCamera);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            videoPath = bundle.getString(VIDEO_PATH);
            whichCamera = bundle.getString(WHICH_CAMERA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video_preview, container, false);
        ButterKnife.bind(this, rootView);
        videoPreview.setSurfaceTextureListener(this);

        if (whichCamera.equals(CameraActivity.CAMERA_FRONT)) {
            flipVideoBtn.setVisibility(View.VISIBLE);
            isVideoFlipped = true;
        } else {
            isVideoFlipped = false;
        }

        return rootView;
    }

    @OnClick(R.id.flip_video_btn)
    public void onFlipVideoClicked() {
        isVideoFlipped = !isVideoFlipped;
        flipVideo();
    }

    @OnClick(R.id.video_preview_cancel_btn)
    public void onCancelClicked() {
        mListener.onVideoPreviewInteraction(VIDEO_PREVIEW_ACTION_CANCEL, null, null);
    }

    @OnClick(R.id.video_preview_check_btn)
    public void onCheckClicked() {
        mListener.onVideoPreviewInteraction(VIDEO_PREVIEW_ACTION_CHECK, getThumbnail(), videoPath);
    }

    private byte[] getThumbnail() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVideoPreviewInteractionListener) {
            mListener = (OnVideoPreviewInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVideoPreviewInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mediaPlayer != null)
            mediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyMediaPlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        destroyMediaPlayer();
    }

    private void destroyMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void flipVideo() {
        if (isVideoFlipped) {
            videoPreview.setScaleX(-1);
        }
        else {
            videoPreview.setScaleX(1);
        }
    }

    private void prepareMediaPlayer(SurfaceTexture surfaceTexture) {
        Surface surface = new Surface(surfaceTexture);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setScreenOnWhilePlaying(true);
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setSurface(surface);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoPreview.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    mediaPlayer.getVideoWidth(),
                                    mediaPlayer.getVideoHeight())
                    );
                    mediaPlayer.start();
                    if (mediaPlayer.getDuration() > 0) {
                        int duration = mediaPlayer.getDuration();
                        videoDuration = String.format(Locale.UK, "%02d:%02d",
                                MILLISECONDS.toMinutes(duration),
                                MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
                    }
                    videoDurationTextView.setText(videoDuration);
                }
            });
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        if (whichCamera.equals(CameraActivity.CAMERA_FRONT) && isVideoFlipped) {
            Matrix matrix = new Matrix();
            matrix.setScale(-1, 1);
            matrix.postTranslate(width, 0);
            videoPreview.setTransform(matrix);
        }

        prepareMediaPlayer(surfaceTexture);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    interface OnVideoPreviewInteractionListener {
        void onVideoPreviewInteraction(int action, @Nullable byte[] thumbnail, @Nullable String videoFilePath);
    }
}

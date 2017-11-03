package com.cncoding.teazer.home.camera.upload;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.VideoView;

import com.cncoding.teazer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class VideoPreview extends Fragment {

    private static final String VIDEO_PATH = "videoPath";
//    public static final int VIDEO_PREVIEW_ACTION_CANCEL = 1;
//    public static final int VIDEO_PREVIEW_ACTION_CHECK = 2;
//    private static final String WHICH_CAMERA = "whichCamera";
//    private static final String TAG = "VideoPreview";

    private String videoPath;
//    private String videoDuration;
//    private boolean isVideoFlipped;

    @BindView(R.id.video_view_preview) VideoView videoViewPreview;
    @BindView(R.id.play_pause_video_preview) AppCompatImageView playPauseBtn;
//    @BindView(R.id.video_preview) TextureView videoPreview;
//    @BindView(R.id.video_preview_cancel_btn) ImageButton cancelButton;
//    @BindView(R.id.video_preview_check_btn) ImageButton checkButton;
//    @BindView(R.id.video_duration) TextView videoDurationTextView;
//    @BindView(R.id.flip_video_btn) ImageButton flipVideoBtn;

//    private MediaPlayer mediaPlayer;
//    private OnVideoPreviewInteractionListener mListener;

    public VideoPreview() {
        // Required empty public constructor
    }

    public static VideoPreview newInstance(String videoPath) {
        VideoPreview fragment = new VideoPreview();
        Bundle args = new Bundle();
        args.putString(VIDEO_PATH, videoPath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            videoPath = bundle.getString(VIDEO_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_preview, container, false);
        ButterKnife.bind(this, rootView);
//        videoPreview.setSurfaceTextureListener(this);

        videoViewPreview.setVideoPath(videoPath);
        videoViewPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoViewPreview.start();
//                if (mediaPlayer.getReact_duration() > 0) {
//                    int duration = mediaPlayer.getReact_duration();
//                    videoDuration = String.format(Locale.UK, "%02d:%02d",
//                            MILLISECONDS.toMinutes(duration),
//                            MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
//                }
//                videoDurationTextView.setText(videoDuration);
            }
        });
        videoViewPreview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                togglePlayButtonVisibility(VISIBLE);
            }
        });

//        if (whichCamera.equals(CameraActivity.CAMERA_FRONT)) {
//            flipVideoBtn.setVisibility(View.VISIBLE);
//            isVideoFlipped = true;
//        } else {
//            isVideoFlipped = false;
//        }

        return rootView;
    }

    void togglePlayButtonVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                playPauseBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in));
                break;
            case INVISIBLE:
                playPauseBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_out));
                break;
            default:
                break;
        }
        playPauseBtn.setVisibility(visibility);
    }

    @OnClick(R.id.video_view_preview) public void pauseVideo() {
        if (videoViewPreview.isPlaying()) {
            videoViewPreview.pause();
            togglePlayButtonVisibility(VISIBLE);
        }
    }

    @OnClick(R.id.play_pause_video_preview) public void playVideo() {
        videoViewPreview.start();
        togglePlayButtonVisibility(INVISIBLE);
    }

//    @OnClick(R.id.flip_video_btn)
//    public void onFlipVideoClicked() {
//        isVideoFlipped = !isVideoFlipped;
////        flipVideo();
//    }

//    @OnClick(R.id.video_preview_cancel_btn)
//    public void onCancelClicked() {
//        mListener.onVideoPreviewInteraction(VIDEO_PREVIEW_ACTION_CANCEL, null, null);
//    }

//    @OnClick(R.id.video_preview_check_btn)
//    public void onCheckClicked() {
//        mListener.onVideoPreviewInteraction(VIDEO_PREVIEW_ACTION_CHECK, getThumbnail(), videoPath);
//    }

//    private byte[] getThumbnail() {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
//        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        return stream.toByteArray();
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnVideoPreviewInteractionListener) {
//            mListener = (OnVideoPreviewInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnVideoPreviewInteractionListener");
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (!videoViewPreview.isPlaying())
            videoViewPreview.start();
//        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
        videoViewPreview.pause();
//        if (mediaPlayer != null)
//            mediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoViewPreview.stopPlayback();
//        destroyMediaPlayer();
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
//        destroyMediaPlayer();
    }

//    private void destroyMediaPlayer() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }

//    private void flipVideo() {
//        if (isVideoFlipped) {
//            videoPreview.setScaleX(-1);
//        }
//        else {
//            videoPreview.setScaleX(1);
//        }
//    }

//    private void prepareMediaPlayer(SurfaceTexture surfaceTexture) {
//        Surface surface = new Surface(surfaceTexture);
//        mediaPlayer = new MediaPlayer();
//        mediaPlayer.setScreenOnWhilePlaying(true);
//        try {
//            mediaPlayer.setDataSource(videoPath);
//            mediaPlayer.setSurface(surface);
//            mediaPlayer.setLooping(true);
//            mediaPlayer.prepareAsync();
//
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    videoPreview.setLayoutParams(
//                            new RelativeLayout.LayoutParams(
//                                    mediaPlayer.getVideoWidth(),
//                                    mediaPlayer.getVideoHeight())
//                    );
//                    mediaPlayer.start();
//                    if (mediaPlayer.getReact_duration() > 0) {
//                        int duration = mediaPlayer.getReact_duration();
//                        videoDuration = String.format(Locale.UK, "%02d:%02d",
//                                MILLISECONDS.toMinutes(duration),
//                                MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
//                    }
//                    videoDurationTextView.setText(videoDuration);
//                }
//            });
//        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
//        if (whichCamera.equals(CameraActivity.CAMERA_FRONT) && isVideoFlipped) {
//            Matrix matrix = new Matrix();
//            matrix.setScale(-1, 1);
//            matrix.postTranslate(width, 0);
//            videoPreview.setTransform(matrix);
//        }
//
//        prepareMediaPlayer(surfaceTexture);
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//        return false;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//    }

//    interface OnVideoPreviewInteractionListener {
//        void onVideoPreviewInteraction(int action, @Nullable byte[] thumbnail, @Nullable String videoFilePath);
//    }
}

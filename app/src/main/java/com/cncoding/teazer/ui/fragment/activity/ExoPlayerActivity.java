package com.cncoding.teazer.ui.fragment.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.cncoding.teazer.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT;
import static android.media.AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
import static com.cncoding.teazer.utilities.MediaUtils.acquireAudioLock;
import static com.cncoding.teazer.utilities.MediaUtils.releaseAudioLock;

public class ExoPlayerActivity extends AppCompatActivity{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btnClose) ImageView btnClose;
    @BindView(R.id.video_view) SimpleExoPlayerView playerView;

    private SimpleExoPlayer player;
    private String videoURL;
//    private long playbackPosition;
//    private int currentWindow;
    private boolean playWhenReady = true;


    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private boolean audioAccessGranted = false;
    private long playerCurrentPosition = 0;
    private Handler mHandler;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player2);
        ButterKnife.bind(this);

        mHandler = new Handler();
        audioFocusChangeListener =
                new AudioManager.OnAudioFocusChangeListener() {
                    public void onAudioFocusChange(int focusChange) {
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            // Permanent loss of audio focus
                            // Pause playback immediately
                            player.setPlayWhenReady(false);
                            // Wait 30 seconds before stopping playback
                            mHandler.postDelayed(mDelayedStopRunnable,
                                    TimeUnit.SECONDS.toMillis(30));
                        }
                        else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT) {
                            // Pause playback
                            player.setPlayWhenReady(false);
                        } else if (focusChange == AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                            // Lower the volume, keep playing
                        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                            // Your app has been granted audio focus again
                            // Raise volume to normal, restart playback if necessary
                            player.setPlayWhenReady(true);
                            player.seekTo(player.getCurrentWindowIndex(),playerCurrentPosition);
                        }
                    }
                };


        //acquire audio play access(transient)
        audioAccessGranted = acquireAudioLock(this, audioFocusChangeListener);

        videoURL = getIntent().getStringExtra("VIDEO_URL");

//        if (null != toolbar) {
//            this.setSupportActionBar(toolbar);
//            //noinspection ConstantConditions
//            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
//            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
////            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
//        }
    }


    private Runnable mDelayedStopRunnable = new Runnable() {
        @Override
        public void run() {
            player.setPlayWhenReady(false);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

//    @SuppressLint("InlinedApi")
//    private void hideSystemUi() {
//        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//    }

    private void initializePlayer() {
        if (player ==null){
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            playerView.setPlayer(player);
            if (audioAccessGranted) {
                player.setPlayWhenReady(true);
                player.seekTo(player.getCurrentWindowIndex(), playerCurrentPosition);
            }
            MediaSource mediaSource=buildMediaSource(Uri.parse(videoURL));
            LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
            player.prepare(loopingSource,true,false);
            playerView.setResizeMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }

        try {
            playerCurrentPosition = player.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        releaseAudioLock(this, audioFocusChangeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();


        }
        releaseAudioLock(this, audioFocusChangeListener);
        mHandler.removeCallbacks(mDelayedStopRunnable);
    }
    private void releasePlayer() {
        if (player != null) {
//            playbackPosition = player.getCurrentPosition();
//            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @OnClick(R.id.btnClose)
    public void onViewClicked() {
        mHandler.removeCallbacks(mDelayedStopRunnable);
        finish();
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(this,"ua"),
                new DefaultExtractorsFactory(),null,null);
    }
}

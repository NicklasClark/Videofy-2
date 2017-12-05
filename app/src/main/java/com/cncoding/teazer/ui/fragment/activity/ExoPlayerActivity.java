package com.cncoding.teazer.ui.fragment.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExoPlayerActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    SimpleExoPlayerView playerView;
    SimpleExoPlayer exoPlayer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnClose)
    ImageView btnClose;
    private String videoURL;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player2);
        ButterKnife.bind(this);

        videoURL = getIntent().getStringExtra("VIDEO_URL");

        if (null != toolbar) {
            this.setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
//            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
        }

        initializePlayer();
    }

    private void initializePlayer() {

        if (exoPlayer==null){
            exoPlayer= ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(this),
                    new DefaultTrackSelector(),
                    new DefaultLoadControl());
            playerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);
            MediaSource mediaSource=buildMediaSource(Uri.parse(videoURL));
            LoopingMediaSource loopingSource = new LoopingMediaSource(mediaSource);
            exoPlayer.prepare(loopingSource,true,false);
        }
    }

    @OnClick(R.id.btnClose)
    public void onViewClicked() {
        finish();
    }
    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(this,"ua"),
                new DefaultExtractorsFactory(),null,null);

    }
}

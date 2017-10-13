package com.cncoding.teazer.camera;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

import com.cncoding.teazer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPreview extends AppCompatActivity {

    public static final String VIDEO_PATH = "videoPath";
    @BindView(R.id.video_view_preview) VideoView videoViewPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        ButterKnife.bind(this);
        videoViewPreview.setVideoPath(getIntent().getStringExtra(VIDEO_PATH));
        videoViewPreview.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoViewPreview.isPlaying())
            videoViewPreview.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoViewPreview.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        videoViewPreview.stopPlayback();
    }
}

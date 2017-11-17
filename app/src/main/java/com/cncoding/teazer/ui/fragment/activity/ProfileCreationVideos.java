package com.cncoding.teazer.ui.fragment.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.cncoding.teazer.R;

public class ProfileCreationVideos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation_videos);
        Intent intent=getIntent();
        String videoUrl=intent.getStringExtra("VideoURL");
        final VideoView videoView=findViewById(R.id.flContainer);

        Uri video = Uri.parse(videoUrl);
        videoView.setVideoURI(video);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });
    }
}

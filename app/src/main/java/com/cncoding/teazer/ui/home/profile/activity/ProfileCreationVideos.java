package com.cncoding.teazer.ui.home.profile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.cncoding.teazer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileCreationVideos extends AppCompatActivity {
    @BindView(R.id.backbutton)
    ImageView backButton;
    @BindView(R.id.txtlikes)
    TextView txtlikes;
    @BindView(R.id.txtviews)
    TextView txtviews;
    @BindView(R.id.title)
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile_creation_videos);
        ButterKnife.bind(this);


        Intent intent=getIntent();
        String videoUrl=intent.getStringExtra("VideoURL");
        final VideoView videoView=findViewById(R.id.flContainer);
        int likes=Integer.parseInt(intent.getStringExtra("Likes"));
        int views=Integer.parseInt(intent.getStringExtra("Views"));
        String Title=intent.getStringExtra("Title");
        title.setText(Title);
        txtviews.setText(String.valueOf(views));
        txtlikes.setText(String.valueOf(likes));

//        Uri video = Uri.parse(videoUrl);
//        videoView.setVideoURI(video);
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//                videoView.start();
//            }
//        });

        videoView.setVideoPath(videoUrl);
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}


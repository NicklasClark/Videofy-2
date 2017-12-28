package com.cncoding.teazer.videoTrim;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.cncoding.teazer.R;
import com.cncoding.teazer.videoTrim.interfaces.OnTrimVideoListener;
import com.cncoding.teazer.videoTrim.view.VideoTrimmerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.videoTrim.utils.TrimVideoUtil.VIDEO_MAX_DURATION;

public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener {

//    private static final String TAG = "jason";
//    private static final String STATE_IS_PAUSED = "isPaused";
    public static final int VIDEO_TRIM_REQUEST_CODE = 101;
    @BindView(R.id.trimmer_view)
    VideoTrimmerView trimmerView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_trimmer);
        ButterKnife.bind(this);
        Bundle bd = getIntent().getExtras();
        String path = "";
        if (bd != null) {
            path = bd.getString("path");
        }
        trimmerView.setMaxDuration(VIDEO_MAX_DURATION);
        trimmerView.setOnTrimVideoListener(this);
        trimmerView.setVideoURI(Uri.parse(path));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        trimmerView.onPause();
        trimmerView.setRestoreState(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trimmerView.destroy();
    }

    @Override
    public void onStartTrim() {
    }

    @Override
    public void onFinishTrim(String uri) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        Intent intent = new Intent();
        intent.putExtra("trimmed_path", uri);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void onCancel() {
        trimmerView.destroy();
        finish();
    }
}

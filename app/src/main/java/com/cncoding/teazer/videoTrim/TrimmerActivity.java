package com.cncoding.teazer.videoTrim;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.cncoding.teazer.R;
import com.cncoding.teazer.databinding.ActivityTrimmerBinding;
import com.cncoding.teazer.videoTrim.interfaces.OnTrimVideoListener;

import java.io.File;

public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener {

    private static final String TAG = "jason";
    private static final String STATE_IS_PAUSED = "isPaused";
    public static final int VIDEO_TRIM_REQUEST_CODE = 101;
    private File tempFile;
    private ActivityTrimmerBinding binding;
    private int maxDuration;

    public static void go(FragmentActivity from, String videoPath){
        if(!TextUtils.isEmpty(videoPath)) {
            Bundle bundle = new Bundle();
            bundle.putString("path", videoPath);
            Intent intent = new Intent(from,TrimmerActivity.class);
            intent.putExtras(bundle);
            from.startActivityForResult(intent,VIDEO_TRIM_REQUEST_CODE);
        }
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trimmer);
        Bundle bd = getIntent().getExtras();
        String path = "";
        if(bd != null) {
            path = bd.getString("path");
            maxDuration = bd.getInt("MAX_DURATION");
        }

        if (binding.trimmerView != null) {
            binding.trimmerView.setMaxDuration(maxDuration);
            binding.trimmerView.setOnTrimVideoListener(this);
            binding.trimmerView.setVideoURI(Uri.parse(path));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.trimmerView.onPause();
        binding.trimmerView.setRestoreState(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.trimmerView.destroy();
    }

    @Override
    public void onStartTrim() {
    }

    @Override
    public void onFinishTrim(String uri) {
        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }
        Intent intent = new Intent();
        intent.putExtra("trimmed_path", uri);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void onCancel() {
        binding.trimmerView.destroy();
        finish();
    }
}

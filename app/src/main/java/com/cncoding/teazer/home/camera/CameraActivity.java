/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cncoding.teazer.home.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.home.camera.CameraFragment.OnCameraFragmentInteractionListener;
import com.cncoding.teazer.home.camera.VideoGalleryAdapter.VideoGalleryAdapterInteractionListener;
import com.cncoding.teazer.home.camera.upload.VideoUpload;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_SHOW_GALLERY;
import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_UPLOAD;
import static com.cncoding.teazer.home.camera.upload.VideoUpload.VIDEO_PATH;

public class CameraActivity extends AppCompatActivity
        implements OnCameraFragmentInteractionListener, VideoGalleryAdapterInteractionListener {

    private static final String TAG_VIDEO_GALLERY_FRAGMENT = "videoGalleryFragment";
    @BindView(R.id.container_2) FrameLayout container_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        if (null == savedInstanceState) {
            getFragmentManager().beginTransaction().replace(R.id.container, CameraFragment.newInstance(false)).commit();
        }
    }

    @Override
    public void onCameraInteraction(int action, String videoPath) {
        switch (action) {
            case ACTION_UPLOAD:
//                SEND BROADCAST TO UPDATE THE VIDEO IN MEDIASTORE DATABASE.
                sendBroadcast(
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                                .setData(Uri.fromFile(new File(videoPath)))
                );
                startVideoUpload(videoPath);
                break;
            case ACTION_SHOW_GALLERY:
                container_2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_up));
                container_2.setVisibility(View.VISIBLE);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_2, VideoGalleryFragment.newInstance(3), TAG_VIDEO_GALLERY_FRAGMENT).commit();
                break;
            default:
                break;
        }
    }

    private void startVideoUpload(String videoPath) {
        Intent intent = new Intent(this, VideoUpload.class);
        intent.putExtra(VIDEO_PATH, videoPath);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(TAG_VIDEO_GALLERY_FRAGMENT) != null) {
            getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(TAG_VIDEO_GALLERY_FRAGMENT)).commit();
            container_2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.sink_down));
            container_2.setVisibility(View.INVISIBLE);
        } else
            super.onBackPressed();
    }

    @Override
    public void onVideoGalleryAdapterInteraction(String videoPath) {
        startVideoUpload(videoPath);
    }
}
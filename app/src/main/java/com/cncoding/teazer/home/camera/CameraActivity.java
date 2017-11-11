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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoding.teazer.R;
import com.cncoding.teazer.home.camera.CameraFragment.OnCameraFragmentInteractionListener;
import com.cncoding.teazer.home.camera.VideoGalleryAdapter.VideoGalleryAdapterInteractionListener;
import com.cncoding.teazer.home.camera.upload.VideoUpload;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_SHOW_GALLERY;
import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_UPLOAD_REACTION;
import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_UPLOAD_VIDEO_POST;
import static com.cncoding.teazer.home.camera.upload.VideoUpload.VIDEO_PATH;
import static com.cncoding.teazer.utilities.ViewUtils.IS_REACTION;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class CameraActivity extends AppCompatActivity
        implements OnCameraFragmentInteractionListener, VideoGalleryAdapterInteractionListener {

    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.video_gallery_container) RecyclerView recyclerView;
    @BindView(R.id.sliding_panel_arrow) AppCompatImageView slidingPanelArrow;

    private SlidingUpPanelLayout.PanelSlideListener panelSlideListener;
    private ArrayList<Videos> videosList = new ArrayList<>();

    private boolean isReaction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            isReaction = bundle.getBoolean(IS_REACTION);

        slidingUpPanelLayout.setOverlayed(true);
        slidingUpPanelLayout.setScrollableView(recyclerView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, CameraFragment.newInstance(isReaction)).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        prepareVideoGallery();
    }

    @SuppressLint("StaticFieldLeak")
    private void prepareVideoGallery() {
        if (panelSlideListener != null) {
            slidingUpPanelLayout.removePanelSlideListener(panelSlideListener);
            panelSlideListener = null;
        }
        panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                if (newState == COLLAPSED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_up);
                }
                else if (newState == EXPANDED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_down);
                }
                else if (newState == ANCHORED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_drag_handle);
                }
            }
        };
        slidingUpPanelLayout.addPanelSlideListener(panelSlideListener);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                videosList.clear();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                Uri uri;
                Cursor cursor;
                int columnIndexData;
//                int columnId;
//                int columnFolderName;
                int thumbnailData;
                uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                String[] projection = {
                        MediaStore.MediaColumns.DATA,
//                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Thumbnails.DATA
                };
                final String orderByDateTaken = MediaStore.Video.Media.DATE_TAKEN;

                cursor = getContentResolver().query(uri, projection, null, null, orderByDateTaken + " DESC");
                if (cursor != null) {
                    columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                    columnFolderName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
//                    columnId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    thumbnailData = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

                    while (cursor.moveToNext()) {
                        videosList.add(new Videos(
                                cursor.getString(columnIndexData),              //Video path
                                cursor.getString(thumbnailData)                 //Thumbnail
                        ));
                    }
                    cursor.close();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(CameraActivity.this,
//                        FlexboxLayout.LAYOUT_DIRECTION_LTR);
//                flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
////                flexboxLayoutManager.setAlignContent(AlignContent.FLEX_START);
//                flexboxLayoutManager.setAlignItems(AlignItems.CENTER);
//                flexboxLayoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
                recyclerView.setLayoutManager(new GridLayoutManager(CameraActivity.this, 3));
                recyclerView.setAdapter(new VideoGalleryAdapter(videosList, CameraActivity.this));
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @Override
    public void onCameraInteraction(int action, String videoPath) {
        switch (action) {
            case ACTION_UPLOAD_VIDEO_POST:
//                SEND BROADCAST TO UPDATE THE VIDEO IN MEDIASTORE DATABASE.
                sendBroadcast(
                        new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                                .setData(Uri.fromFile(new File(videoPath)))
                );
                startVideoUploadActivity(videoPath);
                break;
            case ACTION_UPLOAD_REACTION:
                break;
            case ACTION_SHOW_GALLERY:
                slidingUpPanelLayout.setPanelState(ANCHORED);
                break;
            default:
                break;
        }
    }

    private void startVideoUploadActivity(String videoPath) {
        Intent intent = new Intent(this, VideoUpload.class);
        intent.putExtra(VIDEO_PATH, videoPath);
        startActivity(intent);
        finish();
    }

    @Override
    public void onVideoGalleryAdapterInteraction(String videoPath) {
        startVideoUploadActivity(videoPath);
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == ANCHORED ||
                slidingUpPanelLayout.getPanelState() == EXPANDED) {
            slidingUpPanelLayout.setPanelState(COLLAPSED);
        }
        else {
            super.onBackPressed();
        }
    }
}
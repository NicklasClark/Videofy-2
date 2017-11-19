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
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.home.camera.CameraFragment.OnCameraFragmentInteractionListener;
import com.cncoding.teazer.home.camera.VideoGalleryAdapter.VideoGalleryAdapterInteractionListener;
import com.cncoding.teazer.home.camera.upload.VideoUpload;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.UploadParams;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_SHOW_GALLERY;
import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_UPLOAD_VIDEO_POST;
import static com.cncoding.teazer.home.camera.upload.VideoUpload.VIDEO_PATH;
import static com.cncoding.teazer.utilities.ViewUtils.IS_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.POST_DETAILS;
import static com.cncoding.teazer.utilities.ViewUtils.updateMediaDatabase;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class CameraActivity extends AppCompatActivity
        implements OnCameraFragmentInteractionListener, VideoGalleryAdapterInteractionListener {

    private static final int REQUEST_CODE_STORAGE_PERMISSIONS = 101;
    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.video_gallery_container) RecyclerView recyclerView;
    @BindView(R.id.sliding_panel_arrow) AppCompatImageView slidingPanelArrow;

    private SlidingUpPanelLayout.PanelSlideListener panelSlideListener;
    private ArrayList<Videos> videosList;

    private boolean isReaction = false;
    private PostDetails postDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        videosList = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isReaction = bundle.getBoolean(IS_REACTION);
            postDetails = bundle.getParcelable(POST_DETAILS);
        }

        slidingUpPanelLayout.setOverlayed(true);
        slidingUpPanelLayout.setScrollableView(recyclerView);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CameraFragment.newInstance(isReaction, postDetails))
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSIONS);
        }
        else
            prepareVideoGallery();
    }

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

        new GetVideoGalleryData(this).execute();
    }

    private static class GetVideoGalleryData extends AsyncTask<Void, Void, Void> {

        private WeakReference<CameraActivity> reference;

        GetVideoGalleryData(CameraActivity context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reference.get().videosList.clear();
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

            cursor = reference.get().getContentResolver().query(uri, projection, null, null,
                    orderByDateTaken + " DESC");
            if (cursor != null) {
                columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                    columnFolderName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
//                    columnId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                thumbnailData = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

                while (cursor.moveToNext()) {
                    reference.get().videosList.add(new Videos(
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
            reference.get().recyclerView.setLayoutManager(new GridLayoutManager(reference.get(), 3));
            reference.get().recyclerView.setAdapter(new VideoGalleryAdapter(reference.get().videosList, reference.get()));
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onCameraInteraction(int action, UploadParams uploadParams) {
        switch (action) {
            case ACTION_UPLOAD_VIDEO_POST:
//                SEND BROADCAST TO UPDATE THE VIDEO IN MEDIASTORE DATABASE.
                updateMediaDatabase(this, uploadParams.getVideoPath());
                startVideoUploadActivity(uploadParams.getVideoPath());
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
//        intent.putExtra(IS_REACTION, isReaction);
        startActivity(intent);
        finish();
    }

    @Override
    public void onVideoGalleryAdapterInteraction(String videoPath) {
        if (!isReaction)
            startVideoUploadActivity(videoPath);
        else
            new CameraFragment.ChooseOptionalTitle(this, new UploadParams(videoPath, postDetails), this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSIONS) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                prepareVideoGallery();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == ANCHORED ||
                slidingUpPanelLayout.getPanelState() == EXPANDED) {
            slidingUpPanelLayout.setPanelState(COLLAPSED);
        }
        else {
            if (!isReaction) {
                startActivity(new Intent(this, BaseBottomBarActivity.class));
                finish();
            } else super.onBackPressed();
        }
    }
}
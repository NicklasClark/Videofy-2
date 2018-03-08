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

package com.cncoding.teazer.ui.home.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.base.UploadParams;
import com.cncoding.teazer.data.model.giphy.Images;
import com.cncoding.teazer.data.model.giphy.TrendingGiphy;
import com.cncoding.teazer.ui.common.Interests;
import com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment;
import com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment.TagsAndCategoriesInteractionListener;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.ui.home.BaseBottomBarActivity;
import com.cncoding.teazer.ui.home.camera.CameraFragment.OnCameraFragmentInteractionListener;
import com.cncoding.teazer.ui.home.camera.UploadFragment.OnUploadFragmentInteractionListener;
import com.cncoding.teazer.ui.home.camera.adapters.TrendingGiphyAdapter;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.NearbyPlacesAdapter.NearbyPlacesInteractionListener;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.NearbyPlacesList;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.NearbyPlacesList.OnNearbyPlacesListInteractionListener;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.SelectedPlace;
import com.cncoding.teazer.utilities.asynctasks.CompressVideoAsyncTask;
import com.cncoding.teazer.utilities.videoTrim.TrimmerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.R.anim.fade_in;
import static com.cncoding.teazer.R.anim.float_up;
import static com.cncoding.teazer.R.anim.sink_down;
import static com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment.ACTION_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment.ACTION_TAGS_FRAGMENT;
import static com.cncoding.teazer.ui.home.camera.CameraFragment.ACTION_SHOW_GALLERY;
import static com.cncoding.teazer.ui.home.camera.CameraFragment.ACTION_SHOW_GIFS;
import static com.cncoding.teazer.ui.home.camera.CameraFragment.ACTION_START_UPLOAD_FRAGMENT;
import static com.cncoding.teazer.ui.home.camera.UploadFragment.TAG_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.ui.home.camera.UploadFragment.TAG_NEARBY_PLACES;
import static com.cncoding.teazer.ui.home.camera.UploadFragment.TAG_NULL_NEARBY_PLACES;
import static com.cncoding.teazer.ui.home.camera.UploadFragment.TAG_TAGS_FRAGMENT;
import static com.cncoding.teazer.utilities.common.FabricAnalyticsUtil.logSearchEvent;
import static com.cncoding.teazer.utilities.common.ViewUtils.IS_REACTION;
import static com.cncoding.teazer.utilities.common.ViewUtils.POST_ID;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.videoTrim.TrimmerActivity.VIDEO_TRIM_REQUEST_CODE;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class CameraActivity extends AppCompatActivity
        implements OnCameraFragmentInteractionListener, OnUploadFragmentInteractionListener,
        TagsAndCategoriesInteractionListener, OnNearbyPlacesListInteractionListener, Interests.OnInterestsInteractionListener,
        NearbyPlacesInteractionListener, OnConnectionFailedListener, CompressVideoAsyncTask.AsyncResponse,
        EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_STORAGE_PERMISSIONS = 101;
    private static final String TAG_UPLOAD_FRAGMENT = "uploadFragment";
    private static final int RC_READ_EXTERNAL_STORAGE = 102;

    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.video_gallery_container) RecyclerView videoGalleryRecyclerView;
    @BindView(R.id.reaction_gif_container) RecyclerView gifRecyclerView;
    @BindView(R.id.sliding_panel_arrow) AppCompatImageView slidingPanelArrow;
    @BindView(R.id.up_btn) AppCompatImageView upBtn;
    @BindView(R.id.giphy_search) ProximaNovaRegularAutoCompleteTextView giphySearch;

    private FragmentManager fragmentManager;
    private GoogleApiClient googleApiClient;
    private SlidingUpPanelLayout.PanelSlideListener panelSlideListener;
    private ArrayList<Videos> videosList;

    private boolean isReaction = false;
    int postId;
    private CameraFragment cameraFragment;
    private UploadFragment uploadFragment;
    private TrendingGiphyAdapter trendingGiphyAdapter;
    private String videoPath;
    boolean checkFromGallery = true;
    private Call<TrendingGiphy> trendingGiphyCall;
    private Call<TrendingGiphy> searchGiphyCall;
    private int offset = 0;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int totalGiphyCount = 0;
    private String giphySearchTerm = "";
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .permitDiskWrites()
//                .penaltyLog()
////                .penaltyDialog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
////                .penaltyLog()
//                .build());

        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        //checkFromGallery=true;
        fragmentManager = getSupportFragmentManager();
        videosList = new ArrayList<>();

        videoGalleryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        videoGalleryRecyclerView.setAdapter(new VideoGalleryAdapter(this, videosList));

        //giphy recycler view
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        gifRecyclerView.setLayoutManager(manager);
        trendingGiphyAdapter = new TrendingGiphyAdapter(this);
        gifRecyclerView.setAdapter(trendingGiphyAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                offset += 30;
                if (offset < totalGiphyCount) {
                    if (giphySearchTerm.length() > 0) {
                        searchGiphys(offset, giphySearchTerm);
                    } else {
                        getTrendingGiphys(offset);
                    }
                }
            }
        };
        gifRecyclerView.addOnScrollListener(scrollListener);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isReaction = bundle.getBoolean(IS_REACTION);
            postId = bundle.getInt(POST_ID);
        }
        slidingUpPanelLayout.setOverlayed(true);
        slidingUpPanelLayout.setScrollableView(videoGalleryRecyclerView);
        cameraFragment = CameraFragment.newInstance(isReaction);

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, cameraFragment)
                    .commit();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        if (isReaction) {
            getTrendingGiphys(offset);
        }
    }

    private void getTrendingGiphys(final int offset) {
        trendingGiphyCall = ApiCallingService.Giphy.getTrendingGiphys(getString(R.string.giphy_api_key), 30, offset, "g");
        if (!trendingGiphyCall.isExecuted())
            trendingGiphyCall.enqueue(new Callback<TrendingGiphy>() {
                @Override
                public void onResponse(Call<TrendingGiphy> call, Response<TrendingGiphy> response) {
                    try {
                        TrendingGiphy trendingGiphy = response.body();
                        if (trendingGiphy.getMeta().getStatus() == 200) {
                            totalGiphyCount = trendingGiphy.getPagination().getTotalCount();
                            trendingGiphyAdapter.addPosts(offset, trendingGiphy.getData());
                        } else {
                            Toast.makeText(CameraActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TrendingGiphy> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    @OnTextChanged(R.id.giphy_search) public void performSearch(final CharSequence charSequence) {
        trendingGiphyAdapter.clearData();
        scrollListener.resetState();
        if (charSequence.length() > 0) {
            if (giphySearch.getCompoundDrawables()[2] == null)
                giphySearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_padded,
                        0, R.drawable.ic_clear_dark, 0);
        } else {
            if (giphySearch.getCompoundDrawables()[2] != null)
                giphySearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_padded, 0, 0, 0);
        }

        search(charSequence);
    }

    private void search(CharSequence charSequence) {
        giphySearchTerm = charSequence.toString();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (giphySearchTerm != null && !giphySearchTerm.isEmpty()) {
                    logSearchEvent(giphySearchTerm);
                    trendingGiphyAdapter.clearData();
                    trendingGiphyAdapter.notifyDataSetChanged();
                    searchGiphys(0, giphySearchTerm);
            }
            else getTrendingGiphys(0);
            }
        };

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        } else {
            handler = new Handler();
        }
        handler.postDelayed(runnable, 200);
    }

    @OnEditorAction(R.id.giphy_search) public boolean searchByKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            search(giphySearch.getText());
            hideKeyboard(this, giphySearch);
            return true;
        }
        return false;
    }

    @OnFocusChange(R.id.giphy_search) public void ontGiphySearchFocused(boolean isFocused) {
        if (isFocused && slidingUpPanelLayout.getPanelState() != EXPANDED)
            slidingUpPanelLayout.setPanelState(EXPANDED);
    }

    @OnTouch(R.id.giphy_search) public boolean clearText(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && giphySearch.getCompoundDrawables()[2] != null &&
                event.getRawX() >= giphySearch.getRight() - giphySearch.getCompoundDrawables()[2].getBounds().width() * 1.5) {
            giphySearch.setText("");
            getTrendingGiphys(0);
            return true;
        }
        return false;
    }

    private void searchGiphys(final int offset, String query) {
        searchGiphyCall = ApiCallingService.Giphy.searchGiphy(getString(R.string.giphy_api_key), 30, offset, "g", "en",query);
        if (!searchGiphyCall.isExecuted())
            searchGiphyCall.enqueue(new Callback<TrendingGiphy>() {
                @Override
                public void onResponse(Call<TrendingGiphy> call, Response<TrendingGiphy> response) {
                    try {
                        TrendingGiphy trendingGiphy = response.body();
                        if (trendingGiphy.getMeta().getStatus() == 200) {
                            totalGiphyCount = trendingGiphy.getPagination().getTotalCount();
                            trendingGiphyAdapter.addPosts(offset, trendingGiphy.getData());
                        } else {
                            Toast.makeText(CameraActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<TrendingGiphy> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {
                    MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (checkFromGallery) {
                checkFromGallery = false;
                Intent intent = getIntent();
                if (getIntent() != null) {
                    String action = intent.getAction();
                    String type = intent.getType();

                    if (Intent.ACTION_SEND.equals(action) && type != null) {

                        if ("text/plain".equals(type)) {
                            Toast.makeText(getApplicationContext(), "Please select a video to upload", Toast.LENGTH_SHORT).show();

                        } else if (type.startsWith("image/")) {
                            Toast.makeText(getApplicationContext(), "Please select a video to upload", Toast.LENGTH_SHORT).show();

                        } else if (type.startsWith("video/")) {
                            Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);

                            MediaPlayer mp = MediaPlayer.create(this, uri);
                            int duration = mp.getDuration();
                            mp.release();

                            if (duration >= 5000)
                                uploadOrTrimAction(getRealPathFromURI(getApplicationContext(), uri));

                            else {
                                Toast.makeText(getApplicationContext(), "Select atleast 5 seconds video to upload", Toast.LENGTH_SHORT).show();
                            }
                            //Log.d("CompressedLength", String.valueOf(intent.getParcelableExtra(Intent.EXTRA_STREAM)));
                        }
                    } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                        if (type.startsWith("image/")) {
                            Toast.makeText(getApplicationContext(), "Please select a video to upload", Toast.LENGTH_SHORT).show();
                        }
                        if (type.startsWith("video/")) {
                            Toast.makeText(getApplicationContext(), "You can select only  one video to upload", Toast.LENGTH_SHORT).show();
                        }
                }}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSIONS);
        } else
            prepareVideoGallery();

        if (googleApiClient != null && !googleApiClient.isConnected())
            googleApiClient.connect();
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
                } else if (newState == EXPANDED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_down);
                } else if (newState == ANCHORED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_drag_handle);
                }
            }
        };
        slidingUpPanelLayout.addPanelSlideListener(panelSlideListener);

//        if (videosList != null && videosList.isEmpty())
        try {
            new GetVideoGalleryData(this).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        else videoGalleryRecyclerView.getAdapter().notifyDataSetChanged();
    }

    private void startVideoUploadFragment() {
        slidingUpPanelLayout.setPanelState(COLLAPSED);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(float_up, sink_down, fade_in, sink_down)
                        .replace(R.id.uploading_container, uploadFragment, TAG_UPLOAD_FRAGMENT)
                        .addToBackStack(TAG_UPLOAD_FRAGMENT)
                        .commitAllowingStateLoss();
                cameraFragment.closePreviewSession();
            }
        }, 300);
    }

    @Override
    public void onCameraInteraction(int action, UploadParams uploadParams) {
        switch (action) {
            case ACTION_START_UPLOAD_FRAGMENT:
//                SEND BROADCAST TO UPDATE THE VIDEO IN MEDIASTORE DATABASE.
//                updateMediaStoreDatabase(this, uploadParams.getVideoPath());
//                CompressVideoAsyncTask compressVideoAsyncTask = new CompressVideoAsyncTask(this);
//                compressVideoAsyncTask.delegate = this;
//                compressVideoAsyncTask.execute(uploadParams.getVideoPath());

                getVideoDurationAndUpload(uploadParams.getVideoPath(), isReaction, false);
//                uploadFragment = UploadFragment.newInstance(uploadParams.getVideoPath(), isReaction, false, (int) getVideoDuration(videoPath));
//                startVideoUploadFragment();
                break;
            case ACTION_SHOW_GALLERY:
                giphySearch.setVisibility(View.GONE);
                videoGalleryRecyclerView.setVisibility(View.VISIBLE);
                gifRecyclerView.setVisibility(View.GONE);
                slidingUpPanelLayout.setScrollableView(videoGalleryRecyclerView);
                slidingUpPanelLayout.setPanelState(ANCHORED);
                break;
            case ACTION_SHOW_GIFS:
                giphySearch.setVisibility(View.VISIBLE);
                videoGalleryRecyclerView.setVisibility(View.GONE);
                gifRecyclerView.setVisibility(View.VISIBLE);
                slidingUpPanelLayout.setScrollableView(gifRecyclerView);
                slidingUpPanelLayout.setPanelState(ANCHORED);
                break;
            default:
                break;
        }
    }

    @AfterPermissionGranted(RC_READ_EXTERNAL_STORAGE)
    public void onVideoGalleryAdapterInteraction(String videoPath) {

        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            uploadOrTrimAction(videoPath);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_READ_EXTERNAL_STORAGE, perms);
        }
    }

    public void onTrendingGiphyAdapterInteraction(Images images) {
        Gson gson = new Gson();
        String imageString = gson.toJson(images);
        uploadFragment = UploadFragment.newInstance(postId, imageString, isReaction, true, 0, true);
        startVideoUploadFragment();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        uploadOrTrimAction(videoPath);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {}

    private void uploadOrTrimAction(String videoPath) {
        try {
            if (new File(videoPath).exists()) {
                String videoFormat = new File(videoPath).getName();
                String filenameArray[] = videoFormat.split("\\.");
                String extension = filenameArray[filenameArray.length - 1];
                if (extension.equals("mp4") || extension.equals("avi") || extension.equals("mov")) {
                    long videoDuration = getVideoDuration(videoPath);
                    if (videoDuration < 60) {
                        if (isReaction) {
                            if (videoDuration >= 3) {
                                getVideoDurationAndUpload(videoPath, true, true);
//                                uploadFragment = UploadFragment.newInstance(videoPath, true, true, (int) videoDuration);
//                                startVideoUploadFragment();
                            } else {
                                Toast.makeText(this, "MyReactions can not be less than 3 seconds", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (videoDuration >= 5) {
                                getVideoDurationAndUpload(videoPath, false, true);
//                                uploadFragment = UploadFragment.newInstance(videoPath, false, true, (int) videoDuration);
//                                startVideoUploadFragment();
                            } else {
                                Toast.makeText(this, "Posts can not be less than 5 seconds", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("path", videoPath);
                        bundle.putInt("MAX_DURATION", (int) getVideoDuration(videoPath));
                        bundle.putBoolean("IS_REACTION", isReaction);
                        Intent intent = new Intent(this, TrimmerActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, VIDEO_TRIM_REQUEST_CODE);
                    }
                } else {
                    Toast.makeText(this, "This video format is not supported", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(this, "Error opening this file", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getVideoFormat(String filename) {
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length - 1];
        Toast.makeText(this, extension, Toast.LENGTH_SHORT).show();
        return extension;
    }

    @Override
    public void compressionProcessFinish(String output) {
//        File trimmedFile = new File(output);
        Log.d("CompressedLength", String.valueOf(new File(output).length()));
        startVideoUploadFragment();
    }

    private long getVideoDuration(String videoFile) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInSec = Long.parseLong(time) / 1000;

        retriever.release();
        return timeInSec;
    }

    private void getVideoDurationAndUpload(String videoFile, boolean isReaction, boolean isGallery) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoFile);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            startUploadFragment(Long.parseLong(time), videoFile, isReaction, isGallery);
            retriever.release();
        } catch (Exception e) {
            e.printStackTrace();
            if (!isGallery) startUploadFragment(cameraFragment.getUpdatedTime(), videoFile, isReaction, false);
        }
    }

    private void startUploadFragment(long duration, String videoFile, boolean isReaction, boolean isGallery) {
        long timeInSec = duration / 1000;
        uploadFragment = UploadFragment.newInstance(postId, videoFile, isReaction, isGallery, (int) timeInSec, false);
        final Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startVideoUploadFragment();
            }
        }, 3000);
    }

    @Override
    public void onNearbyPlacesAdapterInteraction(final SelectedPlace selectedPlace) {
        fragmentManager.popBackStack();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadFragment.onNearbyPlacesAdapterInteraction(selectedPlace);
            }
        }, 100);
    }

    @Override
    public void onNearbyPlacesListInteraction(int action) {
        hideKeyboard(this, upBtn);
        uploadFragment.toggleInteraction(true);
        uploadFragment.onNearbyPlacesListInteraction(action);
    }

    @Override
    public void onPlaceClick(CharSequence placeId) {
        hideKeyboard(this, upBtn);
        try {
            /*
             * Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
             */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, String.valueOf(placeId));
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if (places.getCount() == 1) {
                        //Do the things here on Click.....
                        uploadFragment.onNearbyPlacesAdapterInteraction(new SelectedPlace(
                                places.get(0).getName().toString(),
                                places.get(0).getLatLng().latitude,
                                places.get(0).getLatLng().longitude
                        ));
                        fragmentManager.popBackStack();
                    } else {
                        Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("onPlaceClick()", e.getMessage());
        }
    }

    @Override
    public void onCurrentLocationClick() {
        hideKeyboard(this, upBtn);
        uploadFragment.onNearbyPlacesAdapterInteraction(new SelectedPlace(
                getAddress(uploadFragment.currentLocation.getLatitude(), uploadFragment.currentLocation.getLongitude()),
                uploadFragment.currentLocation.getLatitude(),
                uploadFragment.currentLocation.getLongitude()
        ));
        fragmentManager.popBackStack();
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            //            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();

//            Log.v("IGA", "Address" + add);
            return obj.getSubLocality();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not get address, please try again later", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onTagsAndCategoriesInteraction(final String action, final String resultToShow, final String resultToSend,
                                               final SparseBooleanArray selectedTagsArray, final int count) {
        try {
            fragmentManager.popBackStack();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    uploadFragment.onTagsAndCategoriesInteraction(action, resultToShow, resultToSend, count);
                }
            }, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onInterestsInteraction(boolean isFromDiscover, ArrayList<Category> categories) {
    }

    @Override
    public void onInterestsSelected(final String resultToShow, final String resultToSend, final int count) {
        fragmentManager.popBackStack();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        uploadFragment.onTagsAndCategoriesInteraction(ACTION_CATEGORIES_FRAGMENT,
                                resultToShow, resultToSend, count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }, 100);
    }

    @Override
    public void onUploadInteraction(String tag, ArrayList<HashMap<String, String>> googlePlaces, String selectedData) {
        if (tag != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (tag) {
                case TAG_TAGS_FRAGMENT:
                    fragmentTransaction.replace(R.id.uploading_container,
                            TagsAndCategoryFragment.newInstance(ACTION_TAGS_FRAGMENT, selectedData), tag);
                    break;
                case TAG_CATEGORIES_FRAGMENT:
                    fragmentTransaction.replace(R.id.uploading_container,
                            Interests.newInstance(true, false, null, selectedData, false), tag);
                    break;
                case TAG_NEARBY_PLACES:
                    fragmentTransaction.replace(R.id.uploading_container,
                            NearbyPlacesList.newInstance(googlePlaces), tag);
                    break;
                case TAG_NULL_NEARBY_PLACES:
                    fragmentTransaction.replace(R.id.uploading_container,
                            NearbyPlacesList.newInstance(null), tag);
                    break;
                default:
                    break;
            }
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

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
            int duration;
            int thumbnailData;
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {
                    MediaStore.MediaColumns.DATA,
                    MediaStore.Video.VideoColumns.DURATION,
//                        MediaStore.Video.Media._ID,
//                    MediaStore.Video.Thumbnails.DATA
            };
            final String orderByDateTaken = MediaStore.Video.Media.DATE_TAKEN;

//            cursor = reference.get().getContentResolver().query(uri, projection,
//                    MediaStore.Video.Media.DURATION + ">=3000", null,
//                    orderByDateTaken + " DESC");
            try {
                cursor = reference.get().getContentResolver().query(uri, projection,
                        null, null,
                        orderByDateTaken + " DESC");
                if (cursor != null) {
                    columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    duration = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                    //                    columnId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                    thumbnailData = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

                    while (cursor.moveToNext()) {
                        try {
                            reference.get().videosList.add(new Videos(
                                    cursor.getString(columnIndexData),              //Video path
                                    //                            cursor.getString(thumbnailData),                //Thumbnail
                                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.VideoColumns.DURATION))    //Duration
                            ));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                reference.get().videoGalleryRecyclerView.getAdapter().notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
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

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }

        // checkFromGallery=true;
        //  Toast.makeText(getApplicationContext(),"onStop",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            uploadFragment = null;
            cameraFragment = null;
            videosList.clear();
            videosList = null;
//        gridLayoutManager.removeAndRecycleAllViews(recycler);
            videoGalleryRecyclerView.removeAllViews();
            if (panelSlideListener != null) {
                slidingUpPanelLayout.removePanelSlideListener(panelSlideListener);
                panelSlideListener = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (trendingGiphyCall != null && !trendingGiphyCall.isExecuted())
            trendingGiphyCall.cancel();
        if (searchGiphyCall != null && !searchGiphyCall.isExecuted())
            searchGiphyCall.cancel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VIDEO_TRIM_REQUEST_CODE:
                if (data != null) {
                    videoPath = data.getStringExtra("trimmed_path");
                    getVideoDurationAndUpload(videoPath, isReaction, true);
//                    uploadFragment = UploadFragment.newInstance(videoPath, isReaction, true, (int) getVideoDuration(videoPath));

//                    CompressVideoAsyncTask compressVideoAsyncTask = new CompressVideoAsyncTask(this);
//                    compressVideoAsyncTask.delegate = this;
//                    compressVideoAsyncTask.execute(videoPath);

//                    startVideoUploadFragment();
                }
                break;
            default:
                break;
        }
    }

    public void updateBackButton(@DrawableRes int resId) {
        if (resId != -1) {
            upBtn.setImageResource(resId);
        } else {
            upBtn.setImageResource(R.drawable.ic_clear_white_24dp);
        }
    }

    @OnClick(R.id.up_btn) public void retakeVideo() {
        hideKeyboard(this, upBtn);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == ANCHORED ||
                slidingUpPanelLayout.getPanelState() == EXPANDED) {
            slidingUpPanelLayout.setPanelState(COLLAPSED);
        } else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
//            if (uploadFragment != null)
//                uploadFragment.toggleInteraction(true);
        } else {
            if (!isReaction) {
                startActivity(new Intent(this, BaseBottomBarActivity.class));
                finish();
            } else super.onBackPressed();
        }
        if (fragmentManager.getBackStackEntryCount() == 1)
            cameraFragment.startPreview();
    }
}
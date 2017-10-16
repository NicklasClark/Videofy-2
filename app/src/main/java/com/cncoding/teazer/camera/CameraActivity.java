package com.cncoding.teazer.camera;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.codetail.animation.SupportAnimator;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.hardware.Camera.Parameters.FLASH_MODE_AUTO;
import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FLASH_MODE_ON;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;
import static java.lang.Thread.sleep;

@SuppressWarnings("deprecation")
public class CameraActivity extends AppCompatActivity
        implements VideoPreviewFragment.OnVideoPreviewInteractionListener,
        VideoUploadFragment.OnVideoUploadInteractionListener,
        VideoGalleryAdapter.VideoGalleryAdapterInteractionListener {

//    private static final int SETTINGS_CLICKED = 0;
//    private static final int FLASH_CLICKED = 1;
//    private static final int CAMERA_FILES_CLICKED = 2;
//    private static final int RECORD_CLICKED = 3;
//    private static final int CAMERA_FLIP_CLICKED = 4;
    public static final int VIDEO_UPLOAD_FINISHED_ACTION = 9;
    public static final int VIDEO_UPLOAD_ERROR_ACTION = 10;
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
    private static final String VIDEO_PREVIEW_FRAGMENT_TAG = "videoPreviewFragment";
    private static final String VIDEO_UPLOAD_FRAGMENT_TAG = "videoUploadFragment";
    public static final String CAMERA_FRONT = "frontCamera";
    public static final String CAMERA_BACK = "cameraBack";
    private static final String CAMERA_NONE = "noCamera";
    private static final int VIDEO_PICKER_REQUEST_CODE = 123;
    private static final int MAX_PROGRESS = 600;

    @BindView(R.id.camera_texture_view) FrameLayout cameraPreview;
    @BindView(R.id.camera_record) FrameLayout cameraRecordBtn;
    @BindView(R.id.camera_record_inner) ImageView recordBtnInner;
    @BindView(R.id.camera_record_outer) ImageView recordBtnOuter;
    @BindView(R.id.camera_files) ImageView cameraFilesView;
    @BindView(R.id.camera_flip) ImageView cameraFlipView;
    @BindView(R.id.camera_flash) ImageView cameraFlashView;
    @BindView(R.id.video_preview_container) FrameLayout videoPreviewContainer;
    @BindView(R.id.progress_layout) RelativeLayout progressLayout;
    @BindView(R.id.camera_countdown_progress_bar) ProgressBar cameraCountdownProgressBar;
    @BindView(R.id.camera_countdown_progress_bar_flipped) ProgressBar cameraCountdownProgressBarFlipped;
    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.video_gallery_container) RecyclerView recyclerView;
    @BindView(R.id.sliding_panel_arrow) AppCompatImageView slidingPanelArrow;

    private static final String[] FLASH_MODES = {
            FLASH_MODE_AUTO,
            FLASH_MODE_OFF,
            FLASH_MODE_ON,
            FLASH_MODE_TORCH
    };

    private static final String[] FLASH_TITLES = {
            "Flash auto",
            "Flash off",
            "Flash on",
            "Flash torch"
    };

    private int progress = 600;
    private int currentFlashMode;
    private static int CAMERA_ID = -1;
    private boolean isRecording;
    private boolean isReturningWithResult = false;
    private boolean isProgressBarThreadRunning;
    private Uri offlineVideoData;
    private String currentVideoFilePath;
    private static Camera camera;
    private CameraSurfaceView cameraSurfaceView;
    private Camera.Parameters parameters;
    private MediaRecorder mediaRecorder;
//    private SurfaceHolder surfaceHolder;
    private FragmentManager fragmentManager;
    private Thread progressBarThread;
    private ArrayList<Videos> videosList = new ArrayList<>();
    private SupportAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRecording = false;
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        cameraCountdownProgressBar.setMax(MAX_PROGRESS);
        cameraCountdownProgressBarFlipped.setMax(MAX_PROGRESS);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        slidingUpPanelLayout.setOverlayed(true);
        slidingUpPanelLayout.setScrollableView(recyclerView);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            cameraFlashView.setVisibility(View.VISIBLE);

        initializeCameraWithPermissions();
    }

    private void initializeCameraWithPermissions() {
        if (arePermissionsAllowed(getApplicationContext())) {
            setupCamera();
            prepareVideoGallery();
        } else {
            requestPermissions();
        }
    }

    private boolean setupCamera() {
        if (deviceHasCamera()) {
            if (cameraPreview.getVisibility() != View.VISIBLE)
                cameraPreview.setVisibility(View.VISIBLE);
            if (openCamera()) {
                progress = 600;
                cameraCountdownProgressBar.setProgress(progress);
                cameraCountdownProgressBarFlipped.setProgress(progress);
                cameraSurfaceView = new CameraSurfaceView(this, camera);
                cameraPreview.addView(cameraSurfaceView);
                setCameraDisplayOrientation(this, CAMERA_ID, camera);
                parameters = camera.getParameters();
                return true;
            } else {
                Toast.makeText(this, R.string.camera_failed_message, Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(this, "Couldn't detect camera on this device!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean deviceHasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    private boolean openCamera() {
        boolean isOpened = false;
        try {
//            releaseCamera();
            if (CAMERA_ID > -1) {
                try {
                    camera = Camera.open(CAMERA_ID);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    finish();
                }
            }
            else getFrontCamera();
            camera.setDisplayOrientation(90);
            isOpened = (camera != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpened;
    }

    private void getFrontCamera() {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                CAMERA_ID = CameraInfo.CAMERA_FACING_FRONT;
                break;
            }
        }
        if (CAMERA_ID > -1) {
            openCamera();
        }
    }

    private void getBackCamera() {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                CAMERA_ID = CameraInfo.CAMERA_FACING_BACK;
                break;
            }
        }
        if (CAMERA_ID > -1) {
            openCamera();
        }
    }

    @NonNull
    private String getWhichCamera() {
        switch (CAMERA_ID) {
            case CameraInfo.CAMERA_FACING_BACK:
                return CAMERA_BACK;
            case CameraInfo.CAMERA_FACING_FRONT:
                return CAMERA_FRONT;
            default:
                return CAMERA_NONE;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

//    @OnClick(R.id.camera_settings)
//    public void onCameraSettingsClicked() {
//        cameraAction(SETTINGS_CLICKED);
//    }

    @OnClick(R.id.camera_flash)
    public void onCameraFlashClicked() {
//        parameters = camera.getParameters();
        currentFlashMode = (currentFlashMode + 1) % FLASH_MODES.length;
        try {
            parameters.setFlashMode(FLASH_MODES[currentFlashMode]);
            camera.setParameters(parameters);
            Toast.makeText(this, FLASH_TITLES[currentFlashMode], Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to change flash mode!", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.camera_files)
    public void onCameraFilesClicked() {
        slidingUpPanelLayout.setPanelState(ANCHORED);
    }

    private void prepareVideoGallery() {
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
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
        });

        new AsyncTask<Void, Void, Void>() {
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
                recyclerView.setAdapter(new VideoGalleryAdapter(videosList, CameraActivity.this));
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case VIDEO_PICKER_REQUEST_CODE:
                if (data != null) {
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(this, data.getData());
                    long duration = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                    retriever.release();
                    if (duration <= 60000) {
                        offlineVideoData = data.getData();
                        isReturningWithResult = true;
                    } else {
                        Toast.makeText(this, "Selected video duration exceeds 1 minute", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (isReturningWithResult) {
            showVideoPreview(getRealPathFromURI(CameraActivity.this, offlineVideoData), CAMERA_NONE);
        }
        isReturningWithResult = false;
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        String path = null;
        try {
            String[] selection = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  selection, null, null, null);
            int column_index;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);
            }
            return path;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @OnClick(R.id.camera_record)
    public void onCameraRecordClicked() {
        if (isRecording) {
            stopProgressBarAndShowVideoPreview();
        } else {
            startProgressBar();
            animateRecordButton(this);
            toggleButtonsVisibility(View.INVISIBLE);
            releaseCamera();
            if (!prepareMediaRecorder()) {
                if (!openCamera())
                    Toast.makeText(this, "Failed to open camera", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Error!")
                        .setMessage("Failed to prepare media recorder!")
                        .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                releaseMediaRecorder();
                                releaseCamera();
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }

            try {
                mediaRecorder.start();
                isRecording = true;
            } catch (IllegalStateException e) {
                releaseMediaRecorder();
                releaseCamera();
                finish();
            }
        }
    }

    private void startProgressBar() {
        progressLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.float_down));
        progressLayout.setVisibility(View.VISIBLE);

        isProgressBarThreadRunning = true;
        progressBarThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isProgressBarThreadRunning && progress >= 0) {
                        cameraCountdownProgressBar.setProgress(progress);
                        cameraCountdownProgressBarFlipped.setProgress(progress--);
                        if (progress == 0) {
                            stopProgressBarAndShowVideoPreview();
                            break;
                        }
                        sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        progressBarThread.start();
    }

    private void stopProgressBarAndShowVideoPreview() {
        if (progressBarThread.isAlive()) {
            isProgressBarThreadRunning = false;
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressLayout.startAnimation(AnimationUtils.loadAnimation(CameraActivity.this, R.anim.reveal_preview));
                progressLayout.setVisibility(View.INVISIBLE);
                stopAnimations();
                toggleButtonsVisibility(View.VISIBLE);
            }
        });

        isRecording = false;
        mediaRecorder.stop();
        releaseMediaRecorder();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (currentVideoFilePath != null) {
                    showVideoPreview(currentVideoFilePath, getWhichCamera());
                    currentVideoFilePath = null;
                } else
                    Toast.makeText(CameraActivity.this, "current video file path not found!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void animateRecordButton(Context context) {
        recordBtnInner.startAnimation(AnimationUtils.loadAnimation(context, R.anim.camera_inner_pulse));
        recordBtnOuter.startAnimation(AnimationUtils.loadAnimation(context, R.anim.camera_outer_pulse));
    }

    private void stopAnimations() {
        recordBtnInner.clearAnimation();
        recordBtnOuter.clearAnimation();
    }

    private void showVideoPreview(String videoFilePath, String whichCamera) {
        videoPreviewContainer.setVisibility(View.VISIBLE);
        cameraPreview.setVisibility(View.GONE);
        resetCameraAndPreview();
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, R.anim.slide_out_left,
                        R.anim.slide_in_left, R.anim.slide_out_right)
                .add(R.id.video_preview_container,
                        VideoPreviewFragment.newInstance(videoFilePath, whichCamera),
                        VIDEO_PREVIEW_FRAGMENT_TAG)
                .commit();
    }

    private void toggleButtonsVisibility(int visibility) {
        cameraFilesView.setVisibility(visibility);
        cameraFlipView.setVisibility(visibility);
        cameraFlashView.setVisibility(visibility);
    }

    @OnClick(R.id.camera_flip)
    public void onCameraFlipClicked() {
        resetCameraAndPreview();

        if (CAMERA_ID == CameraInfo.CAMERA_FACING_BACK) {
            getFrontCamera();
        }
        else {
            getBackCamera();
        }
        cameraSurfaceView = new CameraSurfaceView(this, camera);
        cameraPreview.addView(cameraSurfaceView);
            try {
                camera.setPreviewDisplay(cameraSurfaceView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
    }

    private boolean prepareMediaRecorder() {
        mediaRecorder = new MediaRecorder();

        if (openCamera()) {
            camera.unlock();

            mediaRecorder.setCamera(camera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
            File mediaFile = getMediaFile();
            if (mediaFile != null) {
                currentVideoFilePath = mediaFile.getPath();
            }
            mediaRecorder.setOutputFile(currentVideoFilePath);
            mediaRecorder.setPreviewDisplay(cameraSurfaceView.getHolder().getSurface());
            switch (getWhichCamera()) {
                case CAMERA_BACK:
                    mediaRecorder.setOrientationHint(90);
                    break;
                case CAMERA_FRONT:
                    mediaRecorder.setOrientationHint(270);
                    break;
                default:
                    mediaRecorder.setOrientationHint(270);
                    break;
            }
            mediaRecorder.setMaxDuration(60000);
            mediaRecorder.setMaxFileSize(314572800);

            try {
                mediaRecorder.prepare();
            } catch (IllegalStateException e) {
                releaseMediaRecorder();
                releaseCamera();
                return false;
            } catch (IOException e) {
                releaseMediaRecorder();
                releaseCamera();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

//    private static Uri getOutputMediaFileUri(){
//        return Uri.fromFile(getMediaFile());
//    }

    private static File getMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Teazer");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
//                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String dateString = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK)).format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator + "vid_" + dateString + ".mp4");
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null && camera != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
            camera.unlock();
        }
    }

    private void releaseCamera() {
        if (camera != null && cameraSurfaceView != null) {
            cameraSurfaceView.setCamera(null);
            camera.release();
            camera = null;
        }
    }

    private void resetCameraAndPreview() {
        if (camera != null) {
            camera.stopPreview();
            cameraPreview.removeAllViews();
            releaseCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            setupCamera();
        }
    }

    private boolean isFragmentActive(String tag) {
        return fragmentManager.findFragmentByTag(tag) != null;
    }

    private int removeFragment(String tag) {
        return fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(tag)).commit();
    }

    private boolean comeBackFromPreviewFragment() {
        boolean result = false;
        removeFragment(VIDEO_PREVIEW_FRAGMENT_TAG);
        cameraPreview.setVisibility(View.VISIBLE);
        videoPreviewContainer.setVisibility(View.GONE);
        if (currentVideoFilePath != null) {
            try {
                result = new File(currentVideoFilePath).delete();
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        initializeCameraWithPermissions();
        return result;
    }

    @Override
    public void onVideoPreviewInteraction(int action, @Nullable byte[] thumbnail, @Nullable String videoFilePath) {
        switch (action) {
            case VideoPreviewFragment.VIDEO_PREVIEW_ACTION_CANCEL:
                comeBackFromPreviewFragment();
                break;
            case VideoPreviewFragment.VIDEO_PREVIEW_ACTION_CHECK:
                if (thumbnail != null && videoFilePath != null) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right)
                            .replace(R.id.video_preview_container,
                                    VideoUploadFragment.newInstance(thumbnail, videoFilePath),
                                    VIDEO_UPLOAD_FRAGMENT_TAG)
                            .addToBackStack(VIDEO_PREVIEW_FRAGMENT_TAG)
                            .commit();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onVideoUploadInteraction(int action) {
        switch (action) {
            case VIDEO_UPLOAD_FINISHED_ACTION:
                new AlertDialog.Builder(this)
                        .setTitle("Success!")
                        .setMessage("Your video was successfully uploaded.")
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .show();
                break;
            case VIDEO_UPLOAD_ERROR_ACTION:
                new AlertDialog.Builder(this)
                        .setTitle("Sorry!")
                        .setMessage("Videos upload encountered an error, please try again.")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
            default:
                break;
        }
    }

    @Override
    public void onVideoGalleryAdapterInteraction(String videoPath) {
        slidingUpPanelLayout.setPanelState(COLLAPSED);
        showVideoPreview(videoPath, getWhichCamera());
    }

    /**
     * PERMISSIONS AND SHIT
     */
    private boolean arePermissionsAllowed(Context applicationContext) {
        int cameraResult = ContextCompat.checkSelfPermission(applicationContext, CAMERA);
        int recordAudioResult = ContextCompat.checkSelfPermission(applicationContext, RECORD_AUDIO);
        int writeExtStorage = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE);
        int writeIntStorage = ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE);
//        int flashResult = ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.FLASHLIGHT);

        return cameraResult == PackageManager.PERMISSION_GRANTED &&
                recordAudioResult == PackageManager.PERMISSION_GRANTED &&
                writeExtStorage == PackageManager.PERMISSION_GRANTED &&
                writeIntStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{CAMERA, RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                REQUEST_CAMERA_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean recordAudioAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean readAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && recordAudioAccepted && writeAccepted && readAccepted) {
//                        permissions are granted
                        setupCamera();
                        prepareVideoGallery();
                    }
                    else {
//                        permissions are denied
                        showExplanationDialog();
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            if (shouldShowRequestPermissionRationale(CAMERA)) {
//                                return;
//                            }
//                        }
                    }
                } else showExplanationDialog();
                break;
            default:
                break;
        }
    }

    private void showExplanationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.whoa)
                .setMessage(R.string.please_allow_all_permissions_message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create()
                .show();
    }

    private class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

        private SurfaceHolder surfaceHolder;
        private Camera camera;

        public CameraSurfaceView(Context context, Camera camera) {
            super(context);
            stopPreviewAndFreeCamera();
            if (camera != null) {
                this.camera = camera;
                this.camera.setDisplayOrientation(90);
                surfaceHolder = getHolder();
                surfaceHolder.addCallback(this);
            } else {
                Toast.makeText(context,
                        R.string.camera_failed_message,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                stopPreviewAndFreeCamera();
                setupCamera();
//                openCamera();
            }
        }

        /**
         *  Taking care of preview change/rotate events events here.
         *  NOTE: stop the preview before resizing or reformatting it.
         */
        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            if (surfaceHolder.getSurface() == null) {
//                surface doesn't exist
                return;
            }
            try {
                camera.stopPreview();
            } catch (Exception e) {
//                when trying to stop a non-existent preview
                e.printStackTrace();
            }
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            surfaceHolder.getSurface().release();
            releaseMediaRecorder();
            releaseCamera();
        }

        private void setCamera(Camera camera) {
            if (this.camera == camera)
                return;

            stopPreviewAndFreeCamera();

            this.camera = camera;
            if (this.camera != null) {
                requestLayout();
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.camera.startPreview();
            }
        }

        private void stopPreviewAndFreeCamera() {
            if (camera != null) {
                camera.stopPreview();
                camera.release();

                camera = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == ANCHORED ||
                slidingUpPanelLayout.getPanelState() == EXPANDED) {
            slidingUpPanelLayout.setPanelState(COLLAPSED);
        }
        else if (isFragmentActive(VIDEO_UPLOAD_FRAGMENT_TAG)) {
            fragmentManager.popBackStack();
        }
        else if (isFragmentActive(VIDEO_PREVIEW_FRAGMENT_TAG)) {
            comeBackFromPreviewFragment();
        }
        else {
            releaseMediaRecorder();
            releaseCamera();
            super.onBackPressed();
        }
    }
}

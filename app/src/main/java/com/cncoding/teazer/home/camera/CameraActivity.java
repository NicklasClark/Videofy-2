package com.cncoding.teazer.home.camera;

import android.annotation.SuppressLint;
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
import android.support.v4.app.ActivityCompat;
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
import com.cncoding.teazer.home.camera.upload.VideoUpload;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.hardware.Camera.Parameters.FLASH_MODE_AUTO;
import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FLASH_MODE_ON;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;
import static com.cncoding.teazer.home.camera.upload.VideoUpload.VIDEO_PATH;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;
import static java.lang.Thread.sleep;

@SuppressWarnings("deprecation")
public class CameraActivity extends AppCompatActivity
        implements VideoGalleryAdapter.VideoGalleryAdapterInteractionListener {

//    private static final int SETTINGS_CLICKED = 0;
//    private static final int FLASH_CLICKED = 1;
//    private static final int CAMERA_FILES_CLICKED = 2;
//    private static final int RECORD_CLICKED = 3;
//    private static final int CAMERA_FLIP_CLICKED = 4;
//    public static final int VIDEO_UPLOAD_FINISHED_ACTION = 9;
//    public static final int VIDEO_UPLOAD_ERROR_ACTION = 10;
    private static final int REQUEST_CAMERA_PERMISSIONS = 931;
//    private static final String VIDEO_PREVIEW_FRAGMENT_TAG = "videoPreviewFragment";
//    private static final String VIDEO_UPLOAD_FRAGMENT_TAG = "videoUploadFragment";
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
    @BindView(R.id.video_upload_fragment_container) FrameLayout videoPreviewContainer;
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
//    private FragmentManager fragmentManager;
    private Thread progressBarThread;
    private ArrayList<Videos> videosList = new ArrayList<>();
//    private SupportAnimator animator;
    private SlidingUpPanelLayout.PanelSlideListener panelSlideListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRecording = false;
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        cameraCountdownProgressBar.setMax(MAX_PROGRESS);
        cameraCountdownProgressBarFlipped.setMax(MAX_PROGRESS);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        slidingUpPanelLayout.setOverlayed(true);
        slidingUpPanelLayout.setScrollableView(recyclerView);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            cameraFlashView.setVisibility(View.VISIBLE);

//        Point point = new Point();
//        getWindowManager().getDefaultDisplay().getSize(point);
//        cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(
//                (point.y - (point.y / 3)),
//                point.y));

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

    private void setupCamera() {
        if (deviceHasCamera()) {
            if (cameraPreview.getVisibility() != View.VISIBLE)
                cameraPreview.setVisibility(View.VISIBLE);
            if (openCamera()) {
                progress = 600;
                cameraCountdownProgressBar.setProgress(progress);
                cameraCountdownProgressBarFlipped.setProgress(progress);
                cameraSurfaceView = new CameraSurfaceView(this, camera);
                cameraPreview.addView(cameraSurfaceView);
//                setCameraDisplayOrientation(this, CAMERA_ID, camera);
                parameters = camera.getParameters();
            } else {
                Toast.makeText(this, R.string.camera_failed_message, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Couldn't detect camera on this device!", Toast.LENGTH_SHORT).show();
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
            setCameraDisplayOrientation(this, CAMERA_ID, camera);
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

//    private void getBackCamera() {
//        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
//            CameraInfo cameraInfo = new CameraInfo();
//            Camera.getCameraInfo(i, cameraInfo);
//            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
//                CAMERA_ID = CameraInfo.CAMERA_FACING_BACK;
//                break;
//            }
//        }
//        if (CAMERA_ID > -1) {
//            openCamera();
//        }
//    }

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
            String videoPath = getRealPathFromURI(CameraActivity.this, offlineVideoData);
            showVideoPreview(videoPath);
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

        if (currentVideoFilePath != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showVideoPreview(currentVideoFilePath);
                    currentVideoFilePath = null;
                }
            }, 500);
        } else
            Toast.makeText(CameraActivity.this, "current video file path not found!", Toast.LENGTH_SHORT).show();
    }

    private void animateRecordButton(Context context) {
        recordBtnInner.startAnimation(AnimationUtils.loadAnimation(context, R.anim.camera_inner_pulse));
        recordBtnOuter.startAnimation(AnimationUtils.loadAnimation(context, R.anim.camera_outer_pulse));
    }

    private void stopAnimations() {
        recordBtnInner.clearAnimation();
        recordBtnOuter.clearAnimation();
    }

    private void showVideoPreview(String videoFilePath) {
        videoPreviewContainer.setVisibility(View.VISIBLE);
        cameraPreview.setVisibility(View.GONE);
        releaseCamera();
        Intent intent = new Intent(this, VideoUpload.class);
        intent.putExtra(VIDEO_PATH, videoFilePath);
        startActivity(intent);
//        finish();
    }

    private void toggleButtonsVisibility(int visibility) {
        cameraFilesView.setVisibility(visibility);
        cameraFlipView.setVisibility(visibility);
        cameraFlashView.setVisibility(visibility);
    }

    @OnClick(R.id.camera_flip) public void onCameraFlipClicked() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
            try {
                if (CAMERA_ID == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                else {
                    CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_FRONT;
                }
                if (openCamera()) {
                    Camera.Parameters parameters = camera.getParameters();
                    Camera.Size previewSize = parameters.getPreviewSize();
                    for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                        if ((size.height / size.width) == (4 / 3)) {
                            previewSize = size;
                            break;
                        }
                    }
                    parameters.setPreviewSize(previewSize.width, previewSize.height);
                    camera.setParameters(parameters);
                    cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(previewSize.width, previewSize.height));
                    camera.setPreviewDisplay(cameraSurfaceView.getHolder());
                    camera.startPreview();
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
//        releaseCamera();
//        cameraSurfaceView = null;
//
//        if (CAMERA_ID == CameraInfo.CAMERA_FACING_BACK) {
////            CAMERA_ID = CameraInfo.CAMERA_FACING_FRONT;
////            setupCamera();
//            getFrontCamera();
//        }
//        else {
////            CAMERA_ID = CameraInfo.CAMERA_FACING_BACK;
////            setupCamera();
//            getBackCamera();
//        }
//        cameraSurfaceView = new CameraSurfaceView(this, camera);
//        cameraPreview.addView(cameraSurfaceView);
//            try {
//                camera.setPreviewDisplay(cameraSurfaceView.getHolder());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            camera.startPreview();
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
            } catch (IllegalStateException | IOException e) {
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
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
//            cameraSurfaceView.setCamera(null);
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
            comeBackFromPreview();
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean comeBackFromPreview() {
        boolean result = false;
//        removeFragment(VIDEO_PREVIEW_FRAGMENT_TAG);
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
    public void onVideoGalleryAdapterInteraction(String videoPath) {
        slidingUpPanelLayout.setPanelState(COLLAPSED);
        showVideoPreview(videoPath);
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
//        private List<Camera.Size> supportedPreviewSizes;
        private Camera.Size previewSize;

        public CameraSurfaceView(Context context, Camera camera) {
            super(context);
            stopPreviewAndFreeCamera();
            if (camera != null) {
                this.camera = camera;
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
                setCamera(CameraActivity.camera);
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException | RuntimeException e) {
                e.printStackTrace();
                stopPreviewAndFreeCamera();
                setupCamera();
            }
        }

        /**
         *  Taking care of preview change/rotate events events here.
         *  NOTE: stop the preview before resizing or reformatting it.
         */
        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
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
                Camera.Parameters parameters = camera.getParameters();
                for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                    if ((size.height / size.width) == (4 / 3)) {
                        previewSize = size;
                        break;
                    }
                }
                parameters.setPreviewSize(previewSize.width, previewSize.height);
                camera.setParameters(parameters);
                cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(previewSize.width, previewSize.height));
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

//        @Override
//        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
//            setMeasuredDimension(width, height);
//
////            supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
////            if (supportedPreviewSizes != null) {
////                previewSize = getOptimalPreviewSize(supportedPreviewSizes, width, height);
////            }
//        }
//
//        private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
//            final double ASPECT_TOLERANCE = 0.1;
//            double targetRatio=(double)height / width;
//
//            if (sizes == null) return null;
//
//            Camera.Size optimalSize = null;
//            double minDiff = Double.MAX_VALUE;
//
//            for (Camera.Size size : sizes) {
//                double ratio = (double) size.width / size.height;
//                if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
//                if (Math.abs(size.height - height) < minDiff) {
//                    optimalSize = size;
//                    minDiff = Math.abs(size.height - height);
//                }
//            }
//
//            if (optimalSize == null) {
//                minDiff = Double.MAX_VALUE;
//                for (Camera.Size size : sizes) {
//                    if (Math.abs(size.height - height) < minDiff) {
//                        optimalSize = size;
//                        minDiff = Math.abs(size.height - height);
//                    }
//                }
//            }
//            return optimalSize;
//        }

        private void setCamera(Camera camera) {
            if (this.camera == camera)
                return;

            if (camera != null)
                stopPreviewAndFreeCamera();

            this.camera = camera;
            if (camera != null) {
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
//                camera.setPreviewCallback(null);
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
        else {
            releaseMediaRecorder();
            releaseCamera();
            super.onBackPressed();
        }
    }
}

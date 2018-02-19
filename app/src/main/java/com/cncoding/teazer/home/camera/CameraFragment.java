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

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.AutoFitTextureView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.model.base.UploadParams;
import com.cncoding.teazer.utilities.OnSwipeTouchListener;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.utilities.ViewUtils.IS_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.updateMediaStoreDatabase;

public class CameraFragment extends Fragment {

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    public static final int ACTION_START_UPLOAD_FRAGMENT = 21;
    public static final int ACTION_SHOW_GALLERY = 22;
//    public static final int ACTION_EXIT = 23;

    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

    private static final String TAG = "CameraFragment";
    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int CAMERA_FRONT = 1;
    private static final int CAMERA_BACK = 0;

    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };

    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private final static int[] CAMERA_FILTER_MODES = {CaptureRequest.CONTROL_EFFECT_MODE_OFF,
            CaptureRequest.CONTROL_EFFECT_MODE_MONO,
            CaptureRequest.CONTROL_EFFECT_MODE_NEGATIVE,
            CaptureRequest.CONTROL_EFFECT_MODE_SOLARIZE,
            CaptureRequest.CONTROL_EFFECT_MODE_SEPIA,
            CaptureRequest.CONTROL_EFFECT_MODE_POSTERIZE,
            CaptureRequest.CONTROL_EFFECT_MODE_WHITEBOARD,
            CaptureRequest.CONTROL_EFFECT_MODE_BLACKBOARD,
            CaptureRequest.CONTROL_EFFECT_MODE_AQUA};

    private final static String[] CAMERA_FILTER_MODE_NAMES = {
            "None",
            "Mono",
            "Negative",
            "Solarize",
            "Sepia",
            "Posterize",
            "Whiteborad",
            "Blackborad",
            "Aqua"
    };

    private int selected_filter_mode_index = 0;

    @BindView(R.id.camera_preview) AutoFitTextureView mTextureView;
    @BindView(R.id.camera_record) AppCompatImageView recordBtn;
    @BindView(R.id.camera_files) AppCompatImageView cameraFilesView;
    @BindView(R.id.camera_flip) AppCompatImageView cameraFlipView;
    @BindView(R.id.camera_flash) AppCompatImageView cameraFlashView;
    @BindView(R.id.video_duration) ProximaNovaRegularTextView videoDuration;
    @BindView(R.id.swipeCameraTip)
    ProximaNovaSemiBoldTextView swipeForFilterTip;
    @BindView(R.id.swipeCameraFilterTip)
    ProximaNovaSemiBoldTextView swipeForFilterNameTip;
//    @BindView(R.id.chronometer) ProximaNovaRegularChronometer chronometer;

    private long startTime = 0L;

    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;


    /** A reference to the opened {@link android.hardware.camera2.CameraDevice} */
    private CameraDevice mCameraDevice;

    /** A reference to the current {@link android.hardware.camera2.CameraCaptureSession} for preview */
    private CameraCaptureSession mPreviewSession;

    /** The {@link android.util.Size} of camera preview */
    private Size mPreviewSize;

    /** The {@link android.util.Size} of video recording */
    private Size mVideoSize;

    /** MediaRecorder */
    private MediaRecorder mMediaRecorder;

    /** Whether the app is recording video now */
    private boolean mIsRecordingVideo;

    /** An additional thread for running tasks that shouldn't block the UI */
    private HandlerThread mBackgroundThread;

    /** A {@link Handler} for running tasks in the background */
    private Handler mBackgroundHandler;

    /** A {@link Semaphore} to prevent the app from exiting before closing the camera */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    /** {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a {@link TextureView} */
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            try {
                if (isReaction)
                    openCamera(width, height, CAMERA_FRONT);
                else
                    openCamera(width, height, CAMERA_BACK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }
    };

    /** {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its status */
    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }

        @Override
        public void onClosed(@NonNull CameraDevice camera) {
//            closeCamera();
//            stopBackgroundThread();
            super.onClosed(camera);
        }
    };

    private FragmentActivity activity;
    private Context context;
    private Integer mSensorOrientation;
    private String mNextVideoAbsolutePath;
    private CaptureRequest.Builder mPreviewBuilder;
    private File videoFolder;
    private OnCameraFragmentInteractionListener mListener;
    private String cameraId;
    private boolean isReaction;
    private boolean isFlashSupported;
    private boolean isTorchOn = false;

    public static CameraFragment newInstance(boolean isReaction) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_REACTION, isReaction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();

        new CreateVideoFolder(this).execute();

        if (getArguments() != null) {
            isReaction = getArguments().getBoolean(IS_REACTION);
        }
        if (activity instanceof OnCameraFragmentInteractionListener) {
            mListener = (OnCameraFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.getLocalClassName()
                    + " must implement OnCameraFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextureView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override public void onSwipeTop() {
//                Toast.makeText(getContext(), "top", Toast.LENGTH_SHORT).show();
            }
            @Override public void onSwipeRight() {
                if (selected_filter_mode_index > 0) {
                    selected_filter_mode_index--;
                    mPreviewBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CAMERA_FILTER_MODES[selected_filter_mode_index]);
                    showFilterTip(CAMERA_FILTER_MODE_NAMES[selected_filter_mode_index], true);
                    updatePreview();
                }
            }
            @Override public void onSwipeLeft() {
                if (selected_filter_mode_index < CAMERA_FILTER_MODES.length-1) {
                    selected_filter_mode_index++;
                    mPreviewBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CAMERA_FILTER_MODES[selected_filter_mode_index]);
                    showFilterTip(CAMERA_FILTER_MODE_NAMES[selected_filter_mode_index], true);
                    updatePreview();
                }
            }
            @Override public void onSwipeBottom() {
//                Toast.makeText(getContext(), "bottom", Toast.LENGTH_SHORT).show();
            }

        });

        showFilterTip(getContext().getString(R.string.swipe_for_filter_text), false);
    }

    private void showFilterTip(String text, boolean isFilterName) {
        if(isFilterName) {
            swipeForFilterNameTip.setVisibility(View.VISIBLE);
            swipeForFilterNameTip.setText(text);
            swipeForFilterNameTip.postDelayed(new Runnable() {
                public void run() {
                    swipeForFilterNameTip.setVisibility(View.GONE);
                }
            }, 3000);
        }
        else {
            swipeForFilterTip.setVisibility(View.VISIBLE);
            swipeForFilterTip.setText(text);
            swipeForFilterTip.postDelayed(new Runnable() {
                public void run() {
                    swipeForFilterTip.setVisibility(View.GONE);
                }
            }, 4000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            if (isReaction)
                openCamera(mTextureView.getWidth(), mTextureView.getHeight(), CAMERA_FRONT);
            else
                openCamera(mTextureView.getWidth(), mTextureView.getHeight(), CAMERA_BACK);
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
//        Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
//        isFlashSupported = available == null ? false : available;
//
//        setupFlashButton();
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    @OnClick(R.id.camera_record) public void toggleRecording() {
        if (mIsRecordingVideo) {
            if (updatedTime > 5000) {
                stopRecordButtonAnimations();
                stopRecordingVideo();
            }
            else {
                Toast.makeText(context,"Video can not be less than 5 seconds",Toast.LENGTH_SHORT).show();
            }
        } else {
            animateRecordButton(activity);
            startRecordingVideo();
        }
    }

    @OnClick(R.id.camera_flip) public void flipCamera() {
        closeCamera();
        try {
            if (cameraId.equals(String.valueOf(CameraCharacteristics.LENS_FACING_FRONT))) {
                openCamera(mTextureView.getWidth(), mTextureView.getHeight(), CAMERA_FRONT);
            } else
                openCamera(mTextureView.getWidth(), mTextureView.getHeight(), CAMERA_BACK);
        } catch (Exception e) {
            Toast.makeText(context, "Camera not configured correctly, please retry!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @OnClick(R.id.camera_flash) public void actionFlash() {
        try {
            if (cameraId.equals(String.valueOf(CAMERA_BACK))) {
                if (isFlashSupported) {
                    if (isTorchOn) {
                        mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                        mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
                        cameraFlashView.setImageResource(R.drawable.ic_flash_off);
                        isTorchOn = false;
                    } else {
                        mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                        mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
                        cameraFlashView.setImageResource(R.drawable.ic_flash_on);
                        isTorchOn = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setupFlashButton() {
        if (cameraId.equals(String.valueOf(CAMERA_BACK)) && isFlashSupported) {
            cameraFlashView.setVisibility(View.VISIBLE);

            if (isTorchOn) {
                cameraFlashView.setImageResource(R.drawable.ic_flash_on);
            } else {
                cameraFlashView.setImageResource(R.drawable.ic_flash_off);
            }

        } else {
            cameraFlashView.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.camera_files) public void showGallery() {
        mListener.onCameraInteraction(ACTION_SHOW_GALLERY, null);
    }

    private void animateRecordButton(Context context) {
        cameraFilesView.setVisibility(View.GONE);
//        cameraFlashView.setVisibility(View.GONE);
        cameraFlipView.setVisibility(View.GONE);
        recordBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.camera_inner_pulse));
    }

    private void stopRecordButtonAnimations() {
        cameraFilesView.setVisibility(View.VISIBLE);
//        cameraFlashView.setVisibility(View.VISIBLE);
        cameraFlipView.setVisibility(View.VISIBLE);
        recordBtn.clearAnimation();
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        try {
            if (mBackgroundThread != null) {
                mBackgroundThread.quitSafely();
                try {
                    mBackgroundThread.join();
                    mBackgroundThread = null;
                    mBackgroundHandler = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets whether you should show UI with rationale for requesting permissions.
     *
     * @param permissions The permissions your app wants to request.
     * @return Whether you can show permission rationale UI.
     */
    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void requestVideoPermissions() {
        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
            ConfirmationDialog.newInstance(this).show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(activity, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length == VIDEO_PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        ErrorDialog.newInstance(getString(R.string.permission_request), this)
                                .show(getChildFragmentManager(), FRAGMENT_DIALOG);
                        break;
                    }
                }
            } else {
                ErrorDialog.newInstance(getString(R.string.permission_request), this)
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
     */
    private void openCamera(int width, int height, int facing) {
        if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            requestVideoPermissions();
            return;
        }

        if (activity == null || activity.isFinishing()) {
            return;
        }

        CameraManager cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            if (cameraManager != null) {
                if (cameraManager.getCameraIdList().length > 1) {
                    cameraId = cameraManager.getCameraIdList()[facing];
                } else {
                    cameraId = cameraManager.getCameraIdList()[0];
                }
                // Choose the sizes for camera preview and video recording
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);

                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                if (map != null) {
                    mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
                    mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height, mVideoSize);
                }

                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }

                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                isFlashSupported = available == null ? false : available;

                setupFlashButton();

                configureTransform(width, height);
                mMediaRecorder = new MediaRecorder();
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                cameraManager.openCamera(cameraId, mStateCallback, null);
            }
        } catch (CameraAccessException e) {
            Toast.makeText(activity, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            activity.finish();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            ErrorDialog.newInstance(getString(R.string.camera_error), this).show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        } catch (ArrayIndexOutOfBoundsException e) {
            Toast.makeText(context, getString(R.string.cannot_start_camera), Toast.LENGTH_SHORT).show();
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Start the camera preview.
     */
    public void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);
            mPreviewBuilder.set(CaptureRequest.CONTROL_EFFECT_MODE, CAMERA_FILTER_MODES[selected_filter_mode_index]);

            mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface), new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(context, "Error configuring camera", Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error preparing camera", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update the camera preview. {@link #startPreview()} needs to be called in advance.
     */
    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            //noinspection ConstantConditions
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setFlashStatus();
                    setUpCaptureRequestBuilder(mPreviewBuilder);
                    HandlerThread thread = new HandlerThread("CameraPreview");
                    thread.start();
                    try {
                        if(mPreviewSession!=null) {
                            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
                        }
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * By default, we choose a video size with 3x4 aspect ratio. Also, we don't use sizes
     * larger than 1080p, since MediaRecorder cannot handle such a high-resolution video.
     *
     * @param choices The list of available sizes
     * @return The video size
     */
    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, chooses the smallest one whose
     * width and height are at least as large as the respective requested values, and whose aspect
     * ratio matches with the specified value.
     *
     * @param choices     The list of sizes that the camera supports for the intended output class
     * @param width       The minimum desired width
     * @param height      The minimum desired height
     * @param aspectRatio The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_EFFECT_MODE, CAMERA_FILTER_MODES[selected_filter_mode_index]);
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_CAPTURE_INTENT_ZERO_SHUTTER_LAG);
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should not to be called until the camera preview size is determined in
     * openCamera, or until the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(int viewWidth, int viewHeight) {
        if (mPreviewSize == null || activity == null) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void setUpMediaRecorder(){
        if (activity == null) {
            return;
        }
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
                mNextVideoAbsolutePath = getVideoFilePath(activity);
            }
            mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
            mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
            mMediaRecorder.setVideoFrameRate(30);
//        mMediaRecorder.setPreviewDisplay();
//        mMediaRecorder.setVideoEncodingBitRate(10000000);
            mMediaRecorder.setVideoEncodingBitRate(3000000);
            mMediaRecorder.setMaxDuration(59999);
//        mMediaRecorder.setAudioSamplingRate(16000);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            switch (mSensorOrientation) {
                case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                    mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                    break;
                case SENSOR_ORIENTATION_INVERSE_DEGREES:
                    mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                    break;
            }
            mMediaRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                @Override
                public void onInfo(MediaRecorder mediaRecorder, int what, int extra) {
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        stopRecordButtonAnimations();
                        stopRecordingVideo();
                    }
                }
            });
            mMediaRecorder.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static class CreateVideoFolder extends AsyncTask<Void, Void, Boolean> {

        private WeakReference<CameraFragment> reference;

        CreateVideoFolder(CameraFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (reference != null && reference.get() != null) {
                if (reference.get().isAdded()) {
                    Boolean isCreated = false;
                    reference.get().videoFolder = new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
                            reference.get().getString(R.string.app_name));

                    if (!reference.get().videoFolder.exists()) {
                        isCreated = reference.get().videoFolder.mkdirs();
                    }
                    return isCreated;
                }
            }
            return null;
        }
    }

    private String getVideoFilePath(Context context) {
        String fileName = "Teazer_" + (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()));
        File videoFile;
        try {
            videoFile = File.createTempFile(fileName, ".mp4", videoFolder);
            return videoFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            videoFile = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
            if (videoFile != null) {
                return videoFile.getAbsolutePath() + "/" + fileName + ".mp4";
            }
            else return null;
        }
    }

    private void startRecordingVideo() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
//            checkFlashStatus();
            closePreviewSession();
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            // Set up Surface for the MediaRecorder
            Surface mRecorderSurface = mMediaRecorder.getSurface();
            surfaces.add(mRecorderSurface);
            mPreviewBuilder.addTarget(mRecorderSurface);

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mIsRecordingVideo = true;
                            // Start recording
                            if (mMediaRecorder != null) {
                                mMediaRecorder.start();
                            } else {
                                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

                            //starting video duration counter
                            videoDuration.setVisibility(View.VISIBLE);
                            startTime = SystemClock.uptimeMillis();
                            customHandler.postDelayed(updateTimerThread, 0);
                        }
                    });
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (activity != null) {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }, mBackgroundHandler);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void checkFlashStatus() {
        try {
            if (cameraId.equals(String.valueOf(CAMERA_BACK))) {
                if (isFlashSupported) {
                    if (isTorchOn) {
                        mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                        mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
                        cameraFlashView.setImageResource(R.drawable.ic_flash_off);
                        isTorchOn = false;
                    }
                    else {
                        mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                        mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
                        cameraFlashView.setImageResource(R.drawable.ic_flash_on);
                        isTorchOn = true;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setFlashStatus() {
        try {
            if (cameraId.equals(String.valueOf(CAMERA_BACK))) {
                if (isFlashSupported) {
                    if (isTorchOn) {
                        mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                        if(mPreviewSession != null) {
                            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
                        }
                        cameraFlashView.setImageResource(R.drawable.ic_flash_on);
//                        isTorchOn = false;
                    }
                    else {
                        mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                        if(mPreviewSession != null) {
                            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
                        }                        cameraFlashView.setImageResource(R.drawable.ic_flash_off);
//                        isTorchOn = true;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //thread to update recording time
    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            updatedTime = timeSwapBuff + timeInMilliseconds;

            int secs = (int) (updatedTime / 1000);
            int minutes = secs / 60;
            secs = secs % 60;
            String duration = "" + String.format(Locale.getDefault(), "%02d", minutes) +
                    ":" + String.format(Locale.getDefault(), "%02d", secs);
            videoDuration.setText(duration);
            customHandler.postDelayed(this, 0);
        }

    };

    public void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private void stopRecordingVideo() {
        // UI
        mIsRecordingVideo = false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mPreviewSession.stopRepeating();
                    mPreviewSession.abortCaptures();

                    if (isAdded()) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //stopping recording timer
                                timeSwapBuff += timeInMilliseconds;

                                startTime = 0L;
                                timeInMilliseconds = 0L;
                                timeSwapBuff = 0L;
                                updatedTime = 0L;
                                videoDuration.setVisibility(View.INVISIBLE);
                                customHandler.removeCallbacks(updateTimerThread);
                            }
                        });
                    }

                    //checking flash status before stopping camera recording
                    if (cameraId.equals(String.valueOf(CAMERA_BACK))) {
                        if (isFlashSupported) {
                            if (isTorchOn) {
                                mPreviewBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                                mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, null);
                                isTorchOn = false;
                            }
                        }
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
                // Stop recording
                try {
                    mMediaRecorder.stop();
                } catch (RuntimeException e) {
                    if(e != null)
                        e.printStackTrace();
                }
                mMediaRecorder.reset();

            }
        }).start();

//        SEND BROADCAST TO UPDATE THE VIDEO IN MEDIASTORE DATABASE.
        updateMediaStoreDatabase(context, mNextVideoAbsolutePath);
        cameraFlashView.setImageResource(R.drawable.ic_flash_off);
        mListener.onCameraInteraction(ACTION_START_UPLOAD_FRAGMENT, new UploadParams(mNextVideoAbsolutePath));
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static WeakReference<CameraFragment> reference;

        public static ErrorDialog newInstance(String message, CameraFragment context) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            reference = new WeakReference<>(context);
            return dialog;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //noinspection ConstantConditions
            return new AlertDialog.Builder(reference.get().context)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            reference.get().activity.finish();
                        }
                    })
                    .create();
        }

    }

    public static class ConfirmationDialog extends DialogFragment {

        private static WeakReference<CameraFragment> reference;

        public static ConfirmationDialog newInstance(CameraFragment context) {
            reference = new WeakReference<>(context);
            return new ConfirmationDialog();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(reference.get().activity)
                    .setMessage(R.string.permission_request)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(reference.get().activity, VIDEO_PERMISSIONS,
                                    REQUEST_VIDEO_PERMISSIONS);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reference.get().activity.finish();
                                }
                            })
                    .create();
        }

    }

    public interface OnCameraFragmentInteractionListener {
        void onCameraInteraction(int action, UploadParams uploadParams);
    }
}
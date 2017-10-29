package com.cncoding.teazer.camera;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressWarnings("deprecation")
public class Camera2 extends AppCompatActivity implements SurfaceHolder.Callback {

    private static int CAMERA_ID = -1;

    @BindView(R.id.camera_preview) SurfaceView cameraPreview;

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2);
        ButterKnife.bind(this);
        SurfaceHolder surfaceHolder = cameraPreview.getHolder();
        surfaceHolder.addCallback(this);
    }

//    public int getDp(float px){
//        Resources resources = getResources();
//        DisplayMetrics metrics = resources.getDisplayMetrics();
//        return (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
//    }

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

            camera.setPreviewDisplay(cameraPreview.getHolder());
            Camera.Parameters parameters = camera.getParameters();
            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                if ((size.height / size.width) == (4 / 3)) {
                    parameters.setPreviewSize(size.width, size.height);
                    camera.setParameters(parameters);
                    cameraPreview.setLayoutParams(new RelativeLayout.LayoutParams(size.width, size.height));
                    break;
                }
            }
            CameraActivity.setCameraDisplayOrientation(this, CAMERA_ID, camera);
            camera.startPreview();
//            setCameraDisplayOrientation(this, CAMERA_ID, camera);
            isOpened = (camera != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpened;
    }

    private void getFrontCamera() {
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                CAMERA_ID = Camera.CameraInfo.CAMERA_FACING_FRONT;
                break;
            }
        }
        if (CAMERA_ID > -1) {
            openCamera();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null)
            openCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                Toast.makeText(this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }
}

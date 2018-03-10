package com.cncoding.teazer.ui.home.camera.base;

import android.support.annotation.NonNull;

import com.cncoding.teazer.ui.base.BaseViewModelFragment;
import com.cncoding.teazer.ui.home.camera.CameraActivity;

/**
 *
 * Created by Prem$ on 3/9/2018.
 */

public class BaseCameraFragment extends BaseViewModelFragment {

    @NonNull @Override public CameraActivity getParentActivity() {
        try {
            if (getActivity() != null && getActivity() instanceof CameraActivity) {
                return (CameraActivity) getActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (CameraActivity) getActivity();
    }
}
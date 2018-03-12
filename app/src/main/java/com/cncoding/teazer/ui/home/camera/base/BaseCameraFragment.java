package com.cncoding.teazer.ui.home.camera.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.base.FragmentNavigation;
import com.cncoding.teazer.ui.home.camera.CameraActivity;

/**
 *
 * Created by Prem$ on 3/9/2018.
 */

public class BaseCameraFragment extends BaseFragment {

    public FragmentNavigation navigation;
    protected BaseViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getParentActivity().getBaseViewModel();
    }

    public BaseViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            navigation = (FragmentNavigation) context;
        }
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigation = null;
    }
}
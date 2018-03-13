package com.cncoding.teazer.ui.home.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.injection.component.DaggerBaseComponent;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.base.BaseRecyclerView;
import com.cncoding.teazer.ui.base.FragmentNavigation;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.camera.CameraActivity;

/**
 *
 * Created by Prem $ on 11/02/2017.
 */

public abstract class BaseHomeFragment extends BaseFragment {

    public FragmentNavigation navigation;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected boolean is_next_page = false;
    private BaseRecyclerView.Adapter adapter;
    protected int currentPage;
    protected BaseViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPage = 1;
        if (getActivity() instanceof BaseBottomBarActivity) {
            viewModel = ((BaseBottomBarActivity) getActivity()).getBaseViewModel();
        }
        else if (getActivity() instanceof CameraActivity) {
            viewModel = ((CameraActivity) getActivity()).getBaseViewModel();
        }
        else viewModel = DaggerBaseComponent.builder()
                    .appComponent(TeazerApplication.get(getActivity()).getAppComponent())
                    .build()
                    .baseViewModel();
    }

    public BaseViewModel getViewModel() {
        return viewModel;
    }

    protected void bindRecyclerViewAdapter(BaseRecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @NonNull public BaseBottomBarActivity getParentActivity() {
        try {
            if (getActivity() != null && getActivity() instanceof BaseBottomBarActivity) {
                return (BaseBottomBarActivity) getActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (BaseBottomBarActivity) getActivity();
    }

    @NonNull public CameraActivity getParentCameraActivity() {
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            navigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navigation = null;
        if (scrollListener != null) {
            scrollListener.resetState();
            scrollListener = null;
        }
        if (adapter != null) adapter.release();
    }
}
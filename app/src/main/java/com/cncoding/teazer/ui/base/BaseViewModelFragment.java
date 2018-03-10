package com.cncoding.teazer.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.injection.home.base.component.DaggerBaseActivityComponent;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public abstract class BaseViewModelFragment extends BaseFragment {

    protected BaseViewModel viewModel;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = DaggerBaseActivityComponent.builder()
                .appComponent(TeazerApplication.get(getParentActivity()).getAppComponent())
                .build()
                .baseViewModel();
    }

    @NonNull public Context getTheContext() {
        return context;
    }

    public BaseViewModel getViewModel() {
        return viewModel;
    }

    @NonNull @Override public Activity getParentActivity() {
        try {
            if (getActivity() != null) {
                return getActivity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getActivity();
    }
}
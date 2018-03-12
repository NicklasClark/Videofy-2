package com.cncoding.teazer.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.injection.component.DaggerBaseComponent;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public abstract class BaseViewModelActivity extends BaseActivity {

    protected static BaseViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (viewModel == null)
            viewModel = DaggerBaseComponent.builder()
                    .appComponent(TeazerApplication.get(this).getAppComponent())
                    .build()
                    .baseViewModel();
    }

    public BaseViewModel getBaseViewModel() {
        return viewModel != null ?
                viewModel :
                DaggerBaseComponent.builder()
                        .appComponent(TeazerApplication.get(this).getAppComponent())
                        .build()
                        .baseViewModel();
    }
}
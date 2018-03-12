package com.cncoding.teazer.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.viewmodel.AuthViewModel;

/**
 *
 * Created by Prem$ on 3/11/2018.
 */

public abstract class BaseAuthActivity extends BaseActivity {

    protected AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = TeazerApplication.get(this).getAppComponent().authComponentBuilder().build().authViewModel();
    }

    public AuthViewModel getAuthViewModel() {
        return viewModel != null ?
                viewModel :
                TeazerApplication.get(this).getAppComponent().authComponentBuilder().build().authViewModel();
    }
}
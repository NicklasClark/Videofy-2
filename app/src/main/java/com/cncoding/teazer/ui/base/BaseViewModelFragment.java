package com.cncoding.teazer.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cncoding.teazer.data.viewmodel.BaseViewModel;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public class BaseViewModelFragment extends Fragment {

    @Inject protected BaseViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
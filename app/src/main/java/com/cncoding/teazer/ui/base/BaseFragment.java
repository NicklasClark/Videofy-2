package com.cncoding.teazer.ui.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 *
 * Created by Prem$ on 3/9/2018.
 */

public abstract class BaseFragment extends Fragment {

    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @NonNull public Context getTheContext() {
        return context;
    }

    @NonNull public abstract Activity getParentActivity();
}
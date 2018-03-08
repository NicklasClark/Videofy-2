package com.cncoding.teazer.injection.module.common;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides public Context getContext() {
        return context;
    }
}
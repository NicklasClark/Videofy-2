package com.cncoding.teazer.injection.module.app;

import android.content.Context;

import com.cncoding.teazer.injection.component.AuthComponent;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module(subcomponents = AuthComponent.class)
public class ContextModule {

    private final Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton public Context getContext() {
        return context;
    }
}
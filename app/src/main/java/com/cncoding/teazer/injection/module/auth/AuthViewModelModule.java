package com.cncoding.teazer.injection.module.auth;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.viewmodel.AuthViewModel;
import com.cncoding.teazer.injection.scope.AuthScope;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 3/9/2018.
 */

@Module
public class AuthViewModelModule {

    @Provides @AuthScope AuthViewModel getAuthViewModel(TeazerApplication application) {
        return new AuthViewModel(application);
    }
}
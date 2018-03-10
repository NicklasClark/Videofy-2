package com.cncoding.teazer.injection.auth.component;

import com.cncoding.teazer.data.viewmodel.AuthViewModel;
import com.cncoding.teazer.injection.auth.module.AuthRepositoryModule;
import com.cncoding.teazer.injection.auth.module.AuthViewModelModule;
import com.cncoding.teazer.injection.scope.AuthScope;

import dagger.Subcomponent;

/**
 *
 * Created by Prem$ on 3/8/2018.
 */

@AuthScope
@Subcomponent(modules = {AuthRepositoryModule.class, AuthViewModelModule.class})
public interface AuthComponent {

    AuthViewModel authViewModel();

    void inject(AuthViewModel authViewModel);

    @Subcomponent.Builder interface Builder {
        Builder repositoryModule(AuthRepositoryModule authRepositoryModule);
        AuthComponent build();
    }
}
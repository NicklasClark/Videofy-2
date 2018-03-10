package com.cncoding.teazer.injection.home.base.component;

import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.injection.app.component.AppComponent;
import com.cncoding.teazer.injection.home.base.module.local.LiveDataModule;
import com.cncoding.teazer.injection.home.base.module.local.RoomModule;
import com.cncoding.teazer.injection.home.base.module.local.ViewModelModule;
import com.cncoding.teazer.injection.home.base.module.remote.RepositoryModule;
import com.cncoding.teazer.injection.scope.BaseScope;

import dagger.Component;

/**
 *
 * Created by Prem$ on 3/8/2018.
 */

@BaseScope
@Component(dependencies = AppComponent.class,
        modules = {RoomModule.class, RepositoryModule.class, LiveDataModule.class, ViewModelModule.class})
public interface BaseActivityComponent {

    BaseViewModel baseViewModel();

    void inject(BaseViewModel baseViewModel);
}
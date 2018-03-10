package com.cncoding.teazer.injection.component;

import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.injection.module.base.local.LiveDataModule;
import com.cncoding.teazer.injection.module.base.local.RoomModule;
import com.cncoding.teazer.injection.module.base.local.ViewModelModule;
import com.cncoding.teazer.injection.module.base.remote.RepositoryModule;
import com.cncoding.teazer.injection.scope.BaseScope;

import dagger.Component;

/**
 *
 * Created by Prem$ on 3/8/2018.
 */

@BaseScope
@Component(dependencies = AppComponent.class,
        modules = {RoomModule.class, RepositoryModule.class, LiveDataModule.class, ViewModelModule.class})
public interface BaseComponent {

    BaseViewModel baseViewModel();

    void inject(BaseViewModel baseViewModel);
}
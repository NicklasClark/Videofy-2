package com.cncoding.teazer.injection.module.base.local;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.viewmodel.BaseViewModel;
import com.cncoding.teazer.injection.scope.BaseScope;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 3/9/2018.
 */

@Module
public class ViewModelModule {

    @Provides @BaseScope BaseViewModel getBaseViewModel(TeazerApplication application) {
        return new BaseViewModel(application);
    }
}
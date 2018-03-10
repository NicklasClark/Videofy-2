package com.cncoding.teazer.injection.home.base.module.local;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.model.BaseModel;
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

    @Provides @BaseScope
    Observer<? extends BaseModel> getObserver(final MediatorLiveData<BaseModel> mediatorLiveData) {
        return new Observer<BaseModel>() {
            @Override
            public void onChanged(@Nullable BaseModel baseModel) {
                mediatorLiveData.setValue(baseModel);
            }
        };
    }

    @Provides @BaseScope BaseViewModel getBaseViewModel(TeazerApplication application) {
        return new BaseViewModel(application);
    }
}
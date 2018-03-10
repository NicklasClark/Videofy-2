/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cncoding.teazer.injection.component;

import android.content.Context;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.injection.module.app.ApplicationModule;
import com.cncoding.teazer.injection.module.app.ContextModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 *
 * Created by Prem$ on 8/15/2017.
 */

@Singleton
@Component(modules = {ContextModule.class, ApplicationModule.class})
public interface AppComponent {

//    Exposing getters for dependent components
    Context context();
    TeazerApplication application();

//    Exposing instances of sub components
    AuthComponent.Builder authComponentBuilder();
}
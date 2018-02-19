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

package com.cncoding.teazer.injection.module;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.cncoding.teazer.data.local.repository.LocalPostsRepository;
import com.cncoding.teazer.data.local.room.dao.PostDetailsDao;
import com.cncoding.teazer.data.local.room.database.TeazerDB;
import com.cncoding.teazer.data.viewmodel.factory.AuthTokenViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 8/18/2017.
 */

@Module
public class RoomModule {

    private final TeazerDB database;

    public RoomModule(Application application) {
        this.database = Room.databaseBuilder(
                application,
                TeazerDB.class,
                "Teazer.db"
        ).build();
    }

    @Provides
    @Singleton
    LocalPostsRepository provideListItemRepository(PostDetailsDao postDetailsDao){
        return new LocalPostsRepository(postDetailsDao);
    }

    @Provides
    @Singleton
    PostDetailsDao provideListItemDao(TeazerDB database){
        return database.postDetailsDao();
    }

    @Provides
    @Singleton
    TeazerDB provideListItemDatabase(Application application){
        return database;
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(String token){
        return new AuthTokenViewModelFactory(token);
    }
}

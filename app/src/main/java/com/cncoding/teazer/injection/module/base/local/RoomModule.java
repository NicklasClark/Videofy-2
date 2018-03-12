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

package com.cncoding.teazer.injection.module.base.local;

import android.arch.persistence.room.Room;

import com.cncoding.teazer.base.TeazerApplication;
import com.cncoding.teazer.data.local.dao.PostDetailsDao;
import com.cncoding.teazer.data.local.database.TeazerDB;
import com.cncoding.teazer.injection.scope.BaseScope;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 8/18/2017.
 */

@BaseScope
@Module
public class RoomModule {

    @Provides @BaseScope
    TeazerDB getDatabase(TeazerApplication application) {
        return Room.databaseBuilder(
                application,
                TeazerDB.class,
                "Teazer.db"
        ).build();
    }

    @Provides @BaseScope
    PostDetailsDao getPostDetailsDao(TeazerDB database){
        return database.dao();
    }
}
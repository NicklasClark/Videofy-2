package com.cncoding.teazer.data.local.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.data.local.ConvertersFactory;
import com.cncoding.teazer.data.local.dao.PostDetailsDao;

import javax.inject.Singleton;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

@Singleton
@Database(entities =  {PostDetails.class}, version = 1, exportSchema = false)
@TypeConverters({ConvertersFactory.class})
public abstract class TeazerDB extends RoomDatabase {
    private static TeazerDB INSTANCE;
    public abstract PostDetailsDao dao();
    private static final Object sLock = new Object();

    public static TeazerDB getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TeazerDB.class, "Teazer.db").build();
            }
            return INSTANCE;
        }
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
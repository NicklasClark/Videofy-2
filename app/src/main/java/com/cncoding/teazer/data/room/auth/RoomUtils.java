package com.cncoding.teazer.data.room.auth;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.cncoding.teazer.data.room.databases.TeazerDB;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public class RoomUtils{

    public static TeazerDB getAuthTokenDb(Context applicationContext) {
        return Room.databaseBuilder(applicationContext, TeazerDB.class, "AuthTokenDatabase").build();
    }
}

package com.cncoding.teazer.data.local.room.auth;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by Prem$ on 1/23/2018.
 */

@Dao
public interface AuthTokenDao {
    @Query("SELECT * FROM AuthToken LIMIT 1")
    AuthTokenEntity getCurrentAuthToken();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long updateAuthToken(AuthTokenEntity authTokenEntity);
}
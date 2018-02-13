package com.cncoding.teazer.data.local.room.auth;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

@Entity(tableName = "AuthToken")
public class AuthTokenEntity {
    @ColumnInfo(name = "auth_token") private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

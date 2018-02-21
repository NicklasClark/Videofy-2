package com.cncoding.teazer.data.local.database;

/*
 * Created by Prem $ on 4/7/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;


class DatabaseHolder {
    private static final String database_name = "TeazerAuthToken";
    private static final int database_version = 1;

    private static final String AUTH_TOKEN_TABLE = "AuthToken";
    private static final String AUTH_TOKEN_COLUMN = "auth_token";
    private static final String CREATE_TABLE_AUTH_TOKEN = "create table if not exists " +
            AUTH_TOKEN_TABLE + " (" + AUTH_TOKEN_COLUMN + "  text not null);";

    private static DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    DatabaseHolder(Context context) {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
        }
    }

    void open() {
        db  = dbHelper.getWritableDatabase();
    }

    void close() {
        dbHelper.close();
    }

    /*
    *INSERTION / REMOVAL METHODS
     */
    public void updateAuthToken(final String authToken){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (deleteTable() > 0) {
                    ContentValues content = new ContentValues();
                    content.put(AUTH_TOKEN_COLUMN, authToken);
                    db.insertWithOnConflict(AUTH_TOKEN_TABLE, null, content, SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
        });
    }

    /*
    * RETRIEVAL METHODS
     */
    public String getAuthToken(){
        String authToken = null;
        Cursor cursor;
        try {
            cursor = db.query(true, AUTH_TOKEN_TABLE, new String[]{AUTH_TOKEN_COLUMN},
                    null, null, null, null, null, "1");
            if (cursor != null) {
                authToken = cursor.getString(cursor.getColumnIndex(AUTH_TOKEN_COLUMN));
                cursor.close();
            }
            return authToken;
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }
    }

    /*RESET TABLES METHOD*/
    private int deleteTable() {
        return db.delete(AUTH_TOKEN_TABLE, "1", null);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, database_name, null, database_version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE_AUTH_TOKEN);
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + AUTH_TOKEN_TABLE);
            onCreate(db);
        }
    }
}
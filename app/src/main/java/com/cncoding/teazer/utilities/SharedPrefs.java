package com.cncoding.teazer.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import static com.cncoding.teazer.utilities.OfflineUserProfile.TEAZER;

/**
 *
 * Created by Prem $ on 10/24/2017.
 */

public class SharedPrefs {

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(TEAZER, Context.MODE_PRIVATE);
    }


}

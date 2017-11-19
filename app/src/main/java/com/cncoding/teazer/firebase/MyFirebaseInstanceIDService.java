package com.cncoding.teazer.firebase;

import com.cncoding.teazer.utilities.AuthUtils;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 *
 * Created by Prem $ on 11/9/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        if (!AuthUtils.isUserLoggedIn(getApplicationContext()))
            SharedPrefs.saveFcmToken(getApplicationContext(), FirebaseInstanceId.getInstance().getToken());
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
    }
}
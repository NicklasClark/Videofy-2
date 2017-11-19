package com.cncoding.teazer.authentication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 *
 * Created by Prem $ on 10/10/2017.
 */

public class SmsReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
//    final SmsManager sms = SmsManager.getDefault();

    @SuppressWarnings("deprecation")
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                if (pdusObj != null) {
                    for (Object aPdusObj : pdusObj) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String message = currentMessage.getDisplayMessageBody().split(":")[1];

                        message = message.substring(0, message.length() - 1);
                        Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);

                        Intent myIntent = new Intent("otp");
                        myIntent.putExtra("message", message);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}

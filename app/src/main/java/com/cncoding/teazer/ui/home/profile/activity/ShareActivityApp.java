package com.cncoding.teazer.ui.home.profile.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cncoding.teazer.R;

public class ShareActivityApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_app);



    }


    @Override
    protected void onStart() {
        super.onStart();


//        Intent intent = getIntent();
//        String action = intent.getAction();
//        String type = intent.getType();
//
//        if (Intent.ACTION_SEND.equals(action) && type != null) {
//
//            if ("text/plain".equals(type)) {
//                handleSendText(intent); // Handle text being sent
//            }
//
//            else if (type.startsWith("image/")) {
//                handleSendImage(intent); // Handle single image being sent
//            }
//            else if (type.startsWith("video/")) {
//                handleSendImage(intent); // Handle single image being sent
//            }
//        }
//        else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
//            if (type.startsWith("image/")) {
//                handleSendMultipleImages(intent); // Handle multiple images being sent
//            }
//        } else {
//            // Handle other intents, such as being started from the home screen
//        }




    }

//    void handleSendText(Intent intent) {
//        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//        if (sharedText != null) {
//            // Update UI to reflect text being shared
//        }
//    }
//
//    void handleSendImage(Intent intent) {
//        Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
//        if (imageUri != null) {
//            // Update UI to reflect image being shared
//            Toast.makeText(getApplicationContext(),String.valueOf(imageUri),Toast.LENGTH_SHORT).show();
//            Log.d("NOTIFYM", String.valueOf(imageUri));
//
//
//        }
//    }
//
//    void handleSendMultipleImages(Intent intent) {
//        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//        if (imageUris != null) {
//            // Update UI to reflect multiple images being shared
//        }
//    }
}

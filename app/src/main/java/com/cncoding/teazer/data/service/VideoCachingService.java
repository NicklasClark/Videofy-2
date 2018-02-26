package com.cncoding.teazer.data.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import static com.cncoding.teazer.utilities.SharedPrefs.saveMedia;

/**
 *
 * Created by Prem$ on 2/26/2018.
 */

public class VideoCachingService extends IntentService {

    public VideoCachingService() {
        super(VideoCachingService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra("url");
        String path = intent.getStringExtra("path");

        if (!TextUtils.isEmpty(url)) {
            downloadData(url,path);
        }
        this.stopSelf();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void downloadData(String requestUrl, String path) {
        try {
            File rootDir = new File(path);
            if (!rootDir.exists()) rootDir.mkdir();
            String fileExtension = "." +StringUtils.substringAfterLast(requestUrl, ".");
            File rootFile = new File(rootDir, new Date().getTime() + fileExtension);
            if (rootFile.exists()) {
                stopSelf();
                return;
            } else rootFile.createNewFile();

            URL url = new URL(requestUrl);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            FileOutputStream f = new FileOutputStream(rootFile);
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();
            saveMedia(getApplicationContext(), requestUrl, rootFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

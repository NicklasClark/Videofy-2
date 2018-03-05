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

import static com.cncoding.teazer.utilities.common.SharedPrefs.saveMedia;

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
        boolean isVideo = intent.getBooleanExtra("isVideo", true);

        if (!TextUtils.isEmpty(url)) {
            downloadData(url,path, isVideo);
        }
        this.stopSelf();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void downloadData(String requestUrl, String path, boolean isVideo) {
        try {
            File rootDir = new File(path);
            if (!rootDir.exists()) rootDir.mkdir();
            String fileExtension = "." + StringUtils.substringAfterLast(requestUrl, ".");
            File rootFile = File.createTempFile(String.valueOf(new Date().getTime()), fileExtension, rootDir);
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
            saveMedia(getApplicationContext(), requestUrl, rootFile.getAbsolutePath(), isVideo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

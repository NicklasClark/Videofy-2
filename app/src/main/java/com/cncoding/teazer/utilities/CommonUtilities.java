package com.cncoding.teazer.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by amit on 22/11/17.
 */

public class CommonUtilities {
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_GIF = 3;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static String decodeUnicodeString(String input)
    {
        try {
            return StringEscapeUtils.unescapeJava(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String encodeUnicodeString(String input)
    {
        try {
            if(input != null)
                return StringEscapeUtils.escapeJava(input);
            else
                return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean deleteFilePermanently(String path) {
        File fdelete = new File(path);
        return fdelete.exists() && fdelete.delete();
    }
}

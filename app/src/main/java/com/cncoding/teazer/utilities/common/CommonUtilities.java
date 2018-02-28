package com.cncoding.teazer.utilities.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.BaseModel;

import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 *
 * Created by amit on 22/11/17.
 */

public class CommonUtilities {

    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int MEDIA_TYPE_GIF = 3;
    public static final int MEDIA_TYPE_GIPHY = 4;

    @Nullable
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

    @Nullable
    public static String decodeUnicodeString(String input) {
        try {
            return StringEscapeUtils.unescapeJava(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public static String encodeUnicodeString(String input) {
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

    @Contract("null, _ -> false; !null, null -> false")
    public static boolean areEqualCollections(List<? extends BaseModel> a, List<? extends BaseModel> b) {
        if (a == null || b == null) return false;

        if (a.isEmpty() && b.isEmpty()) return true;

        if (a.size() <= b.size()) {
            for (BaseModel a1 : a) {
                if (!b.contains(a1)) {
                    return false;
                }
            }
        } else {
            for (BaseModel b1 : b) {
                if (!a.contains(b1)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int hashCode(List<? extends BaseModel> list) {
        int hashCode = 1;
        if (list != null && !list.isEmpty()) {
            for (BaseModel e : list)
                hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        else hashCode = 31 * hashCode;
        return hashCode;
    }
}
package com.cncoding.teazer.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created by Prem $ on 10/22/2017.
 */

public class ImageLoader {

    private String imageUrl;

    public ImageLoader() {
    }

    public ImageLoader load(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void into(ImageView imageView) {
        new LoadProfilePic(imageView).execute(imageUrl);
    }

    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        LoadProfilePic(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap new_icon = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                new_icon = BitmapFactory.decodeStream(in_stream);
                in_stream.close();
            } catch (IOException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return new_icon;
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
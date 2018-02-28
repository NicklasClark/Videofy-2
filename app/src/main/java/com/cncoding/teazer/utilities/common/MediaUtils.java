package com.cncoding.teazer.utilities.common;

import android.content.Context;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.data.model.post.PostDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amit on 15/1/18.
 */

public class MediaUtils {

    public static boolean acquireAudioLock(Context context, AudioManager.OnAudioFocusChangeListener afChangeListener) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        try {
// Request audio focus for playback
            int result = 0;
            if (am != null) {
                result = am.requestAudioFocus(afChangeListener,
                        // Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        // Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            }

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                // Start playback
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static void releaseAudioLock(Context context, AudioManager.OnAudioFocusChangeListener afChangeListener) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        try {
            if (am != null) {
                am.abandonAudioFocus(afChangeListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ThumbPrefetchTask extends AsyncTask<Void, Void, List<String>> {

        private Fragment fragment;
        private List<PostDetails> postDetailsList;

        public ThumbPrefetchTask(@NonNull Fragment fragment, @NonNull List<PostDetails> postDetailsList) {
            this.fragment = fragment;
            this.postDetailsList = postDetailsList;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> urls = new ArrayList<>();
            for (PostDetails postDetails : postDetailsList)
                urls.add(postDetails.getMedias().get(0).getThumbUrl());
            return urls;
        }

        @Override
        protected void onPostExecute(List<String> urls) {
            for (String url : urls) {
                Glide.with(fragment).load(url).preload();
            }
        }
    }
}

package com.cncoding.teazer.utilities;

import android.content.Context;
import android.media.AudioManager;

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
}

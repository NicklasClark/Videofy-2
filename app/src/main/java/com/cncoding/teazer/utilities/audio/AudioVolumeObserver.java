package com.cncoding.teazer.utilities.audio;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.support.annotation.NonNull;

/**
 *
 * Created by Prem$ on 1/26/2018.
 */

public class AudioVolumeObserver {

    private final Context context;
    private final AudioManager audioManager;
    private AudioVolumeContentObserver audioVolumeContentObserver;

    public AudioVolumeObserver(@NonNull Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void register(int audioStreamType, @NonNull AudioVolumeContentObserver.OnAudioVolumeChangedListener listener) {

        Handler handler = new Handler();
        // with this handler AudioVolumeContentObserver#onChange()
        //   will be executed in the main thread
        // To execute in another thread you can use a Looper
        // +info: https://stackoverflow.com/a/35261443/904907

        audioVolumeContentObserver = new AudioVolumeContentObserver(
                handler,
                audioManager,
                audioStreamType,
                listener);

        context.getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI,
                true,
                audioVolumeContentObserver);
    }

    public AudioVolumeContentObserver getAudioVolumeContentObserver() {
        return audioVolumeContentObserver;
    }

    public void unregister() {
        if (audioVolumeContentObserver != null) {
            context.getContentResolver().unregisterContentObserver(audioVolumeContentObserver);
            audioVolumeContentObserver = null;
        }
    }
}

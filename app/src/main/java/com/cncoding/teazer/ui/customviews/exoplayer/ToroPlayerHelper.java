package com.cncoding.teazer.ui.customviews.exoplayer;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import im.ene.toro.media.PlaybackInfo;

/**
 * @author eneim | 6/11/17.
 *
 *         General interface for a helper class for a specific {@link ToroPlayer}. This class helps
 *         forwarding the playback state to the {@link ToroPlayer} if there is any
 *         {@link ToroPlayer.EventListener} registered. It also requests the initialization for the
 *         Player.
 */
public abstract class ToroPlayerHelper {

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override public boolean handleMessage(Message msg) {
            boolean playWhenReady = (boolean) msg.obj;
            switch (msg.what) {
                case ToroPlayer.State.STATE_BUFFERING /* Player.STATE_BUFFERING */:
                    internalListener.onBuffering();
                    for (ToroPlayer.EventListener callback : eventListeners) {
                        callback.onBuffering();
                    }
                    break;
                case ToroPlayer.State.STATE_READY /*  Player.STATE_READY */:
                    if (playWhenReady) {
                        internalListener.onPlaying();
                    } else {
                        internalListener.onPaused();
                    }

                    for (ToroPlayer.EventListener callback : eventListeners) {
                        if (playWhenReady) {
                            callback.onPlaying();
                        } else {
                            callback.onPaused();
                        }
                    }
                    break;
                case ToroPlayer.State.STATE_END /* Player.STATE_ENDED */:
                    internalListener.onCompleted();
                    for (ToroPlayer.EventListener callback : eventListeners) {
                        callback.onCompleted();
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @NonNull protected final Container container;
    @NonNull protected final ToroPlayer player;

    @SuppressWarnings("WeakerAccess")
    final ArrayList<ToroPlayer.EventListener> eventListeners = new ArrayList<>();
    @SuppressWarnings("WeakerAccess")
    final ToroPlayer.EventListener internalListener = new ToroPlayer.EventListener() {
        @Override public void onBuffering() {
            // do nothing
        }

        @Override public void onPlaying() {
            // do nothing
        }

        @Override public void onPaused() {
            // do nothing
        }

        @Override public void onCompleted() {
            container.savePlaybackInfo(player.getPlayerOrder(), new PlaybackInfo());
        }
    };

    public ToroPlayerHelper(@NonNull Container container, @NonNull ToroPlayer player) {
        this.container = container;
        this.player = player;
    }

    // Hook into the scroll state change event. Called by the enclosing ToroPlayer.
    public void onSettled() {
        // Do nothing, sub class can override this.
    }

    @SuppressWarnings("ConstantConditions")
    public final void addPlayerEventListener(@NonNull ToroPlayer.EventListener eventListener) {
        if (eventListener != null) this.eventListeners.add(eventListener);
    }

    public final void removePlayerEventListener(ToroPlayer.EventListener eventListener) {
        this.eventListeners.remove(eventListener);
    }

    /**
     * Initialize the necessary resource for the incoming playback. For example, prepare the
     * ExoPlayer instance for SimpleExoPlayerView. The initialization is feed by an initial playback
     * info, telling if the playback should start from a specific position or from beginning.
     *
     * Normally this info can be obtained from cache if there is cache manager, or null if there is no
     * such cached information.
     *
     * @param playbackInfo the initial playback info. {@code null} if no such info available.
     */
    public abstract void initialize(@Nullable PlaybackInfo playbackInfo);

    public abstract void play();

    public abstract void pause();

    public abstract boolean isPlaying();

    /**
     * Get latest playback info. Either on-going playback info if current player is playing, or latest
     * playback info available if player is paused.
     *
     * @return latest {@link PlaybackInfo} of current Player.
     */
    @NonNull public abstract PlaybackInfo getLatestPlaybackInfo();

    // Mimic ExoPlayer
    @CallSuper
    protected final void onPlayerStateUpdated(boolean playWhenReady, @ToroPlayer.State int playbackState) {
        handler.obtainMessage(playbackState, playWhenReady).sendToTarget();
    }

    @CallSuper
    public void release() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override public String toString() {
        return "ToroPlayerHelper{" + "container=" + container + ", player=" + player + '}';
    }
}
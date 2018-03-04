package com.cncoding.teazer.ui.customviews.exoplayer;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.Player;

import java.util.concurrent.atomic.AtomicInteger;

import im.ene.toro.media.PlaybackInfo;

/**
 * 
 * Created by Prem$ on 2/2/2018.
 */

public class ExoPlayerViewHelper extends ToroPlayerHelper {

    private final ExoPlayerHelper.EventListener internalListener =
            new ExoPlayerHelper.EventListener() {
                @Override public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    ExoPlayerViewHelper.super.onPlayerStateUpdated(playWhenReady, playbackState);
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                }
            };

    private final AtomicInteger counter = new AtomicInteger(0); // initialize count
    private final ExoPlayerHelper helper;
    private final LoopingMediaSourceBuilder mediaSourceBuilder;

    public ExoPlayerViewHelper(Container container, ToroPlayer player, @NonNull Uri mediaUri) {
        super(container, player);
        if (!(player.getPlayerView() instanceof SimpleExoPlayerView)) {
            throw new IllegalArgumentException("Only SimpleExoPlayerView is supported.");
        }
        //noinspection ConstantConditions
        if (mediaUri == null) throw new NullPointerException("Media Uri is null.");
        this.mediaSourceBuilder = new LoopingMediaSourceBuilder(container.getContext(), mediaUri);
        this.helper = new ExoPlayerHelper((SimpleExoPlayerView) player.getPlayerView());
    }

    public final void setEventListener(Player.EventListener eventListener) {
        this.internalListener.setDelegate(eventListener);
    }

    /**
     * {@inheritDoc}
     *
     * @param playbackInfo the initial playback info. {@code null} if no such info available.
     */
    @Override public void initialize(@Nullable PlaybackInfo playbackInfo) {
        if (counter.getAndIncrement() == 0) { // prevent the multiple time init
            this.helper.addEventListener(internalListener);
            try {
                this.helper.prepare(this.mediaSourceBuilder);
            } catch (ParserException e) {
                e.printStackTrace();
            }
        }

        this.helper.setPlaybackInfo(playbackInfo);
    }

    @Override public void play() {
        this.helper.play();
    }

    @Override public void pause() {
        this.helper.pause();
    }

    @Override public boolean isPlaying() {
        return this.helper.isPlaying();
    }

    @NonNull @Override public PlaybackInfo getLatestPlaybackInfo() {
        return this.helper.getPlaybackInfo();
    }

    @Override public void release() {
        counter.set(0); // reset
        this.helper.removeEventListener(internalListener);
        this.helper.release();
        super.release();
    }
}

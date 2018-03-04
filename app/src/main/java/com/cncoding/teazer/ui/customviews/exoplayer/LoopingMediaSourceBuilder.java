package com.cncoding.teazer.ui.customviews.exoplayer;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.BandwidthMeter;

/**
 *
 * Created by Prem$ on 2/2/2018.
 */

class LoopingMediaSourceBuilder extends im.ene.toro.exoplayer.MediaSourceBuilder {

    LoopingMediaSourceBuilder(Context context, Uri mediaUri) {
        super(context, mediaUri);
    }

    @Override public MediaSource build(BandwidthMeter bandwidthMeter) {
        MediaSource source = super.build(bandwidthMeter);
        return new LoopingMediaSource(source);
    }
}


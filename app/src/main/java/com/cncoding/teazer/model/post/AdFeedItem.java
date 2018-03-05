package com.cncoding.teazer.model.post;

import com.cncoding.teazer.data.model.post.PostDetails;
import com.inmobi.ads.InMobiNative;

/**
 *
 * Created by amit on 12/2/18.
 */

public class AdFeedItem extends PostDetails {
    public InMobiNative mNativeStrand;

    public AdFeedItem(InMobiNative nativeStrand) {
        super("");
        mNativeStrand = nativeStrand;
    }
}
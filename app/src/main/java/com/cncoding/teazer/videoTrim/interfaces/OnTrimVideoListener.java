package com.cncoding.teazer.videoTrim.interfaces;

import android.net.Uri;

public interface OnTrimVideoListener {

    void onStartTrim();

    void onFinishTrim(final Uri uri);

    void onCancel();
}

package com.cncoding.teazer.videoTrim.interfaces;

public interface OnTrimVideoListener {

    void onStartTrim();

    void onFinishTrim(final String uri);

    void onCancel();
}

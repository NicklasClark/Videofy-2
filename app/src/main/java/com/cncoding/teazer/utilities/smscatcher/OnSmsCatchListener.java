package com.cncoding.teazer.utilities.smscatcher;

/**
 *
 * Created by Prem$ on 2/9/2018.
 */

public interface OnSmsCatchListener<T> {
    void onSmsCatch(String message);
}
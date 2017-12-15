package com.cncoding.teazer.utilities;

import android.widget.TextView;

import java.util.Locale;

/**
 * Created by amit on 15/12/17.
 */

public class StartCountDownClass extends CountDownTimer {

    TextView textView;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public StartCountDownClass(long millisInFuture, long countDownInterval, TextView timerText) {
        super(millisInFuture, countDownInterval);
        this.textView = timerText;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        int secs = (int) (millisUntilFinished / 1000);
        int minutes = secs / 60;
        secs = secs % 60;
//            int milliseconds = (int) (updatedTime % 1000);
        String duration = "" + minutes + ":" + String.format(Locale.getDefault(), "%02d", secs);
        textView.setText(duration);
    }

    @Override
    public void onFinish() {
//        textView.setText("dfsfds");
    }
}

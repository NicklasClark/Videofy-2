package com.cncoding.teazer.data.model.application;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public class PushNotificationBody {

    private String title;
    private String message;

    public PushNotificationBody(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
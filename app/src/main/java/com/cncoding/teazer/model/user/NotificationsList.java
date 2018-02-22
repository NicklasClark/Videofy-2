package com.cncoding.teazer.model.user;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.utilities.Annotations.CallType;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class NotificationsList extends BaseModel {

    private ArrayList<Notification> notifications;
    private int unread_count;
    private boolean next_page;

    public NotificationsList(Throwable error) {
        this.error = error;
    }

    public NotificationsList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public NotificationsList(ArrayList<Notification> notifications, int unread_count, boolean next_page) {
        this.notifications = notifications;
        this.unread_count = unread_count;
        this.next_page = next_page;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public int getUnreadCount() {
        return unread_count;
    }

    public boolean isNextPage() {
        return next_page;
    }
}
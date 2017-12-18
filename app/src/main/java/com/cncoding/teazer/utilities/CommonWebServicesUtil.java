package com.cncoding.teazer.utilities;

import android.content.Context;

import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.user.NotificationsList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.SharedPrefs.setFollowingNotificationCount;
import static com.cncoding.teazer.utilities.SharedPrefs.setRequestNotificationCount;

/**
 * Created by amit on 18/12/17.
 */

public class CommonWebServicesUtil {

    public static void getFollowingNotificationsUnreadCount(final Context context, final int page) {
        Call<NotificationsList> notificationsListCall;
        notificationsListCall = ApiCallingService.User.getFollowingNotifications(page, context);

        if (!notificationsListCall.isExecuted())
            notificationsListCall.enqueue(new Callback<NotificationsList>() {
                @Override
                public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                    try {
                            if (response.code() == 200) {
                                setFollowingNotificationCount(context ,response.body().getUnreadCount());
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    public static void getRequestNotificationsUnreadCount(final Context context, final int page) {
        Call<NotificationsList> notificationsListCall;
        notificationsListCall = ApiCallingService.User.getRequestNotifications(page, context);

        if (!notificationsListCall.isExecuted())
            notificationsListCall.enqueue(new Callback<NotificationsList>() {
                @Override
                public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                    try {
                            if (response.code() == 200) {
                                setRequestNotificationCount(context ,response.body().getUnreadCount());
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsList> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

}

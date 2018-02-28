package com.cncoding.teazer.utilities;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.InviteEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.crashlytics.android.answers.ShareEvent;
import com.crashlytics.android.answers.SignUpEvent;

/**
 * Created by amit on 16/1/18.
 */

public class FabricAnalyticsUtil {

    public static void logSignUpEvent(String medium, boolean isSuccess, String id)
    {
        if (id != null) {
            Answers.getInstance().logSignUp(new SignUpEvent()
                    .putMethod(medium)
                    .putCustomAttribute("id", id)
                    .putSuccess(isSuccess));
        } else {
            Answers.getInstance().logSignUp(new SignUpEvent()
                    .putMethod(medium)
                    .putSuccess(isSuccess));
        }
    }

    public static void logLoginEvent(String medium, boolean isSuccess, String id) {
        if (id != null) {
            Answers.getInstance().logLogin(new LoginEvent()
                .putMethod(medium)
                .putCustomAttribute("id", id)
                .putSuccess(isSuccess));
        } else {
            Answers.getInstance().logLogin(new LoginEvent()
                .putMethod(medium)
                .putSuccess(isSuccess));
        }
    }

    public static void logInviteEvent(String medium)
    {
        Answers.getInstance().logInvite(new InviteEvent()
            .putMethod(medium));
    }

    public static void logVideoShareEvent(String medium, String contentName, String contentType, String contentId)
    {
        Answers.getInstance().logShare(new ShareEvent()
            .putMethod(medium)
            .putContentName(contentName)
            .putContentType(contentType)
            .putContentId(contentId));
    }

    public static void logProfileShareEvent(String medium, String contentName, String contentType, String contentId)
    {
        Answers.getInstance().logShare(new ShareEvent()
            .putMethod(medium)
            .putContentName(contentName)
            .putContentType(contentType)
            .putContentId(contentId));
    }

    public static void logSearchEvent(String query)
    {
        Answers.getInstance().logSearch(new SearchEvent()
                .putQuery(query));
    }
}

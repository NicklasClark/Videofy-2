package com.cncoding.teazer.utilities;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * Created by Prem$ on 2/20/2018.
 */

public class Annotations {

    public static final int NO_CALL = 0;

    public static final int SIGNUP = 10;
    public static final int VERIFY_SIGNUP = 11;
    public static final int SOCIAL_SIGNUP = 12;
    public static final int LOGIN_WITH_PASSWORD = 13;
    public static final int LOGIN_WITH_OTP = 14;
    public static final int VERIFY_LOGIN_WITH_OTP = 15;
    public static final int CHECK_USERNAME_AVAILABILITY = 16;
    public static final int CHECK_EMAIL_AVAILABILITY = 17;
    public static final int CHECK_PHONE_NUMBER_AVAILABILITY = 18;
    public static final int VERIFY_FORGOT_PASSWORD_OTP = 19;
    public static final int REQUEST_RESET_PASSWORD_BY_EMAIL = 20;
    public static final int REQUEST_RESET_PASSWORD_BY_PHONE = 21;
    public static final int RESET_PASSWORD_BY_OTP = 22;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SIGNUP, VERIFY_SIGNUP, SOCIAL_SIGNUP, LOGIN_WITH_PASSWORD, LOGIN_WITH_OTP, VERIFY_LOGIN_WITH_OTP,
            CHECK_USERNAME_AVAILABILITY, CHECK_EMAIL_AVAILABILITY, CHECK_PHONE_NUMBER_AVAILABILITY, VERIFY_FORGOT_PASSWORD_OTP,
            REQUEST_RESET_PASSWORD_BY_EMAIL, REQUEST_RESET_PASSWORD_BY_PHONE, RESET_PASSWORD_BY_OTP})
    public @interface AuthCallType {}

    /**
     * Discover calls
     */
    public static final int CALL_LANDING_POSTS = 10;
    public static final int CALL_MOST_POPULAR_POSTS = 11;
    public static final int CALL_FEATURED_POSTS = 12;
    public static final int CALL_ALL_INTERESTED_CATEGORIES_POSTS = 13;
    public static final int CALL_TRENDING_POSTS = 14;
    public static final int CALL_USERS_LIST = 15;
    public static final int CALL_USERS_LIST_WITH_SEARCH_TERM = 16;
    public static final int CALL_VIDEOS_LIST_WITH_SEARCH_TERM = 18;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NO_CALL, CALL_LANDING_POSTS, CALL_MOST_POPULAR_POSTS, CALL_FEATURED_POSTS, CALL_ALL_INTERESTED_CATEGORIES_POSTS,
            CALL_TRENDING_POSTS, CALL_USERS_LIST, CALL_USERS_LIST_WITH_SEARCH_TERM, CALL_VIDEOS_LIST_WITH_SEARCH_TERM})
    public @interface CallType {}
}

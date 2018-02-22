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

    //region Gender types
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    //endregion

    //region Account types
    public static final int PRIVATE_ACCOUNT = 1;
    public static final int PUBLIC_ACCOUNT = 2;
    //endregion

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PUBLIC_ACCOUNT, PRIVATE_ACCOUNT})
    public @interface AccountType {}

    //region Authentication calls
    /**
     * Authentication calls
     */
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
    //endregion

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SIGNUP, VERIFY_SIGNUP, SOCIAL_SIGNUP, LOGIN_WITH_PASSWORD, LOGIN_WITH_OTP, VERIFY_LOGIN_WITH_OTP,
            CHECK_USERNAME_AVAILABILITY, CHECK_EMAIL_AVAILABILITY, CHECK_PHONE_NUMBER_AVAILABILITY, VERIFY_FORGOT_PASSWORD_OTP,
            REQUEST_RESET_PASSWORD_BY_EMAIL, REQUEST_RESET_PASSWORD_BY_PHONE, RESET_PASSWORD_BY_OTP})
    public @interface AuthCallType {}

    //region Call types
    /**
     * Discover calls
     */
    public static final int CALL_LANDING_POSTS = 10;
    public static final int CALL_MOST_POPULAR_POSTS = 11;
    public static final int CALL_FEATURED_POSTS = 12;
    public static final int CALL_ALL_INTERESTED_CATEGORIES_POSTS = 13;
    public static final int CALL_TRENDING_POSTS_BY_CATEGORY = 14;
    public static final int CALL_TRENDING_VIDEOS = 9;
    public static final int CALL_USERS_LIST = 15;
    public static final int CALL_USERS_LIST_WITH_SEARCH_TERM = 16;
    public static final int CALL_VIDEOS_LIST_WITH_SEARCH_TERM = 17;

    /**
     * Friends calls
     */
    public static final int CALL_SEND_JOIN_REQUEST_BY_USER_ID = 19;
    public static final int CALL_SEND_JOIN_REQUEST_BY_USERNAME = 20;
    public static final int CALL_ACCEPT_JOIN_REQUEST = 21;
    public static final int CALL_DELETE_JOIN_REQUEST = 22;
    public static final int CALL_GET_MY_CIRCLE = 23;
    public static final int CALL_GET_MY_CIRCLE_WITH_SEARCH_TERM = 24;
    public static final int CALL_GET_MY_FOLLOWING = 25;
    public static final int CALL_GET_MY_FOLLOWINGS_WITH_SEARCH_TERM = 26;
    public static final int CALL_GET_FRIENDS_FOLLOWINGS = 27;
    public static final int CALL_GET_FRIENDS_FOLLOWINGS_WITH_SEARCH_TERM = 28;
    public static final int CALL_GET_MY_FOLLOWERS = 29;
    public static final int CALL_GET_MY_FOLLOWERS_WITH_SEARCH_TERM = 30;
    public static final int CALL_GET_FRIENDS_FOLLOWERS = 31;
    public static final int CALL_GET_FRIENDS_FOLLOWERS_WITH_SEARCH_TERM = 32;
    public static final int CALL_UNFOLLOW_USER = 33;
    public static final int CALL_CANCEL_REQUEST = 34;
    public static final int CALL_FOLLOW_USER = 35;
    public static final int CALL_GET_OTHERS_PROFILE_INFO = 36;
    public static final int CALL_BLOCK_UNBLOCK_USER = 37;
    public static final int CALL_GET_BLOCKED_USERS = 38;
    public static final int CALL_GET_USERS_LIST_TO_FOLLOW = 39;
    public static final int CALL_GET_USERS_LIST_TO_FOLLOW_WITH_SEARCH_TERM = 40;
    public static final int CALL_GET_LIKED_USERS = 41;

    /**
     * Posts calls
     */
    public static final int CALL_UPLOAD_VIDEO = 42;
    public static final int CALL_UPDATE_POST = 43;
    public static final int CALL_LIKE_DISLIKE_POST = 44;
    public static final int CALL_INCREMENT_VIEW_COUNT = 45;
    public static final int CALL_DELETE_POST = 46;
    public static final int CALL_DELETE_POST_VIDEO = 47;
    public static final int CALL_REPORT_POST = 48;
    public static final int CALL_HIDE_OR_SHOW_POST = 49;
    public static final int CALL_GET_POST_DETAILS = 50;
    public static final int CALL_GET_TAGGED_USERS = 51;
    public static final int CALL_GET_HIDDEN_POSTS = 52;
    public static final int CALL_GET_HOME_PAGE_POSTS = 53;
    public static final int CALL_GET_MY_POSTED_VIDEOS = 54;
    public static final int CALL_GET_VIDEOS_POSTED_BY_FRIEND = 55;
    public static final int CALL_GET_REACTIONS_OF_POST = 56;
    public static final int CALL_GET_HIDDEN_VIDEOS_LIST = 57;
    public static final int CALL_GET_ALL_HIDDEN_VIDEOS_LIST = 58;

    /**
     * React calls
     */
    public static final int CALL_UPLOAD_REACTION = 59;
    public static final int CALL_LIKE_DISLIKE_REACTION = 60;
    public static final int CALL_INCREMENT_REACTION_VIEW_COUNT = 61;
    public static final int CALL_DELETE_REACTION = 62;
    public static final int CALL_REPORT_REACTION = 63;
    public static final int CALL_HIDE_OR_SHOW_REACTION = 64;
    public static final int CALL_GET_MY_REACTIONS = 65;
    public static final int CALL_GET_HIDDEN_REACTIONS = 66;
    public static final int CALL_GET_REACTION_DETAIL = 67;
    
    /**
     * User calls
     */
    public static final int CALL_UPDATE_USER_PROFILE_MEDIA = 68;
    public static final int CALL_RESET_FCM_TOKEN = 69;
    public static final int CALL_SET_ACCOUNT_VISIBILITY = 70;
    public static final int CALL_GET_USER_PROFILE = 71;
    public static final int CALL_CHANGE_MOBILE_NUMBER = 72;
    public static final int CALL_UPDATE_MOBILE_NUMBER = 73;
    public static final int CALL_GET_USER_PROFILE_DETAIL = 74;
    public static final int CALL_UPDATE_USER_PROFILE = 75;
    public static final int CALL_UPDATE_PASSWORD = 76;
    public static final int CALL_SET_PASSWORD = 77;
    public static final int CALL_GET_FOLLOWING_NOTIFICATIONS = 78;
    public static final int CALL_GET_REQUEST_NOTIFICATIONS = 79;
    public static final int CALL_UPDATE_CATEGORIES = 80;
    public static final int CALL_LOGOUT = 81;
    public static final int CALL_REMOVE_PROFILE_PIC = 82;
    public static final int CALL_REPORT_USER = 83;
    public static final int CALL_DEACTIVATE_ACCOUNT = 84;
    public static final int CALL_RESET_UNREAD_NOTIFICATION = 85;

    /**
     * Application calls
     */
    public static final int CALL_GET_POST_REPORT_TYPES = 86;
    public static final int CALL_GET_PROFILE_REPORT_TYPES = 87;
    public static final int CALL_GET_DEACTIVATION_TYPES_LIST = 88;
    public static final int CALL_GET_CATEGORIES = 89;
    //endregion

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NO_CALL,

            CALL_GET_POST_REPORT_TYPES, CALL_GET_PROFILE_REPORT_TYPES, CALL_GET_DEACTIVATION_TYPES_LIST, CALL_GET_CATEGORIES,

            CALL_LANDING_POSTS, CALL_MOST_POPULAR_POSTS, CALL_FEATURED_POSTS, CALL_ALL_INTERESTED_CATEGORIES_POSTS, CALL_TRENDING_VIDEOS,
            CALL_TRENDING_POSTS_BY_CATEGORY, CALL_USERS_LIST, CALL_USERS_LIST_WITH_SEARCH_TERM, CALL_VIDEOS_LIST_WITH_SEARCH_TERM,

            CALL_SEND_JOIN_REQUEST_BY_USER_ID, CALL_SEND_JOIN_REQUEST_BY_USERNAME, CALL_ACCEPT_JOIN_REQUEST, CALL_DELETE_JOIN_REQUEST, CALL_GET_MY_CIRCLE,
            CALL_GET_MY_CIRCLE_WITH_SEARCH_TERM, CALL_GET_MY_FOLLOWING, CALL_GET_MY_FOLLOWINGS_WITH_SEARCH_TERM, CALL_GET_FRIENDS_FOLLOWINGS,
            CALL_GET_FRIENDS_FOLLOWINGS_WITH_SEARCH_TERM, CALL_GET_MY_FOLLOWERS, CALL_GET_MY_FOLLOWERS_WITH_SEARCH_TERM, CALL_GET_FRIENDS_FOLLOWERS,
            CALL_GET_FRIENDS_FOLLOWERS_WITH_SEARCH_TERM, CALL_UNFOLLOW_USER, CALL_CANCEL_REQUEST, CALL_FOLLOW_USER, CALL_GET_OTHERS_PROFILE_INFO,
            CALL_BLOCK_UNBLOCK_USER, CALL_GET_BLOCKED_USERS, CALL_GET_USERS_LIST_TO_FOLLOW, CALL_GET_USERS_LIST_TO_FOLLOW_WITH_SEARCH_TERM, CALL_GET_LIKED_USERS,

            CALL_UPLOAD_VIDEO, CALL_UPDATE_POST, CALL_LIKE_DISLIKE_POST, CALL_INCREMENT_VIEW_COUNT, CALL_DELETE_POST, CALL_DELETE_POST_VIDEO,
            CALL_REPORT_POST, CALL_HIDE_OR_SHOW_POST, CALL_GET_POST_DETAILS, CALL_GET_TAGGED_USERS, CALL_GET_HIDDEN_POSTS, CALL_GET_HOME_PAGE_POSTS,
            CALL_GET_MY_POSTED_VIDEOS, CALL_GET_VIDEOS_POSTED_BY_FRIEND, CALL_GET_REACTIONS_OF_POST, CALL_GET_HIDDEN_VIDEOS_LIST, CALL_GET_ALL_HIDDEN_VIDEOS_LIST,

            CALL_UPLOAD_REACTION, CALL_LIKE_DISLIKE_REACTION, CALL_INCREMENT_REACTION_VIEW_COUNT, CALL_DELETE_REACTION,
            CALL_REPORT_REACTION, CALL_HIDE_OR_SHOW_REACTION, CALL_GET_MY_REACTIONS, CALL_GET_HIDDEN_REACTIONS, CALL_GET_REACTION_DETAIL,

            CALL_UPDATE_USER_PROFILE_MEDIA, CALL_RESET_FCM_TOKEN, CALL_SET_ACCOUNT_VISIBILITY, CALL_GET_USER_PROFILE, CALL_CHANGE_MOBILE_NUMBER,
            CALL_UPDATE_MOBILE_NUMBER, CALL_GET_USER_PROFILE_DETAIL, CALL_UPDATE_USER_PROFILE, CALL_UPDATE_PASSWORD, CALL_SET_PASSWORD,
            CALL_GET_FOLLOWING_NOTIFICATIONS, CALL_GET_REQUEST_NOTIFICATIONS, CALL_UPDATE_CATEGORIES, CALL_LOGOUT, CALL_REMOVE_PROFILE_PIC,
            CALL_REPORT_USER, CALL_DEACTIVATE_ACCOUNT, CALL_RESET_UNREAD_NOTIFICATION
    })
    public @interface CallType {}

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MALE, FEMALE})
    public @interface Gender {}
}
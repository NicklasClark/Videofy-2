package com.cncoding.teazer.home.post;

/**
 *
 * Created by Prem $ on 12/2/2017.
 */

public class UpdateResult {
    public static final String INCREMENT = "++";
    public static final String DECREMENT = "--";

    public String updateViews;
    public String updateLikes;
    public String updateReaction;

    public static String increment() {
        return INCREMENT;
    }

    public static String decrement() {
        return DECREMENT;
    }
}

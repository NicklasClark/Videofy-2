package com.cncoding.teazer.ui.home.base;

import com.expletus.mobiruck.MobiruckEvent;
import com.expletus.mobiruck.MobiruckSdk;

/**
 *
 * Created by Prem$ on 3/12/2018.
 */

public class IntentHandler {

    public static final String IS_SIGNUP = "signupOrLogin";

    static void logMobiruckEvent() {
        //logging mobiruck event
        MobiruckEvent mobiruckEvent = new MobiruckEvent();

        mobiruckEvent.setEvent("logged_in");  // event name should match as added in the dashboard.

        MobiruckSdk.getInstance().logEvent(mobiruckEvent);
    }
}
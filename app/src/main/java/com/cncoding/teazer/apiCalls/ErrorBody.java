package com.cncoding.teazer.apiCalls;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/2/2017.
 */

public class ErrorBody {

    private String message;
    private ArrayList<String> reason;

    public String getMessage() {
        return message;
    }

    public ArrayList<String> getReason() {
        return reason;
    }
}

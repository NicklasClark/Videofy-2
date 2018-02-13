package com.cncoding.teazer.data.remote;

import java.util.List;

/**
 *
 * Created by Prem $ on 12/2/2017.
 */

public class ErrorBody {

    private String message;
    private List<String> reason;

    public String getMessage() {
        return message;
    }

    public List<String> getReason() {
        return reason;
    }
}

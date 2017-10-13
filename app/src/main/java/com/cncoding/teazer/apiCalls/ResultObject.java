package com.cncoding.teazer.apiCalls;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 9/26/2017.
 */

public class ResultObject {

    @SerializedName("code") @Expose private int code;
    @SerializedName("message") @Expose private String message;
    @SerializedName("status") @Expose private boolean status;

    public ResultObject(int code, String message, boolean status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return status;
    }
}

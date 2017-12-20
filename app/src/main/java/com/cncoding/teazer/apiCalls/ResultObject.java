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
    @SerializedName("auth_token") @Expose private String auth_token;
    @SerializedName("status") @Expose private boolean status;
    @SerializedName("errorBody") @Expose private ErrorBody errorBody;
    @SerializedName("user_id") @Expose private int user_id;

    public ResultObject(int code, String message, String auth_token, boolean status, ErrorBody errorBody, int user_id) {
        this.code = code;
        this.message = message;
        this.auth_token = auth_token;
        this.status = status;
        this.errorBody = errorBody;
        this.user_id = user_id;
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

    public String getAuthToken() {

        return auth_token;
    }
    public ErrorBody getErrorBody() {
        return errorBody;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

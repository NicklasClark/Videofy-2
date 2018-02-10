package com.cncoding.teazer.data.api;

import com.cncoding.teazer.data.api.calls.authentication.AuthenticationRepositoryImpl.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 9/26/2017.
 */

public class ResultObject {

    @SerializedName("code") @Expose private Integer code;
    @SerializedName("message") @Expose private String message;
    @SerializedName("auth_token") @Expose private String authToken;
    @SerializedName("status") @Expose private Boolean status;
    @SerializedName("errorBody") @Expose private ErrorBody errorBody;
    @SerializedName("user_id") @Expose private Integer userId;
    @CallType private int callType;
    private Throwable error;

    public ResultObject(Integer code, String message, String authToken, Boolean status, ErrorBody errorBody, Integer userId) {
        this.code = code;
        this.message = message;
        this.authToken = authToken;
        this.status = status;
        this.errorBody = errorBody;
        this.userId = userId;
    }

    public ResultObject(Throwable error) {
        this.error = error;
    }

    public ResultObject(Throwable error, int callType) {
        this.error = error;
        this.callType = callType;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getAuthToken() {
        return authToken;
    }

    public ErrorBody getErrorBody() {
        return errorBody;
    }

    public Integer getUserId() {
        return userId;
    }

    public Throwable getError() {
        return error;
    }

    public ResultObject setError(Throwable error) {
        this.error = error;
        return this;
    }

    public void clearData() {
        code = null;
        message = null;
        status = null;
        authToken = null;
        errorBody = null;
        userId = null;
        error = null;
    }

    @CallType public int getCallType() {
        return callType;
    }

    public void setCallType(@CallType int callType) {
        this.callType = callType;
    }
}

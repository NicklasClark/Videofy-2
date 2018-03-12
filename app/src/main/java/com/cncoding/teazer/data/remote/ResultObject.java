package com.cncoding.teazer.data.remote;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.friends.FollowInfo;
import com.cncoding.teazer.utilities.common.Annotations.AuthCallType;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 9/26/2017.
 */

public class ResultObject extends BaseModel {

    @SerializedName("code") @Expose private int code;
    @SerializedName("message") @Expose private String message;
    @SerializedName("auth_token") @Expose private String authToken;
    @SerializedName("status") @Expose private boolean status;
    @SerializedName("errorBody") @Expose private ErrorBody errorBody;
    @SerializedName("user_id") @Expose private int userId;
    @SerializedName("follow_info") @Expose private FollowInfo followInfo;
    @AuthCallType private int authCallType;
    private int adapterPosition;

    public ResultObject(int code, String message, String authToken, boolean status,
                        ErrorBody errorBody, int userId, FollowInfo followInfo) {
        this.code = code;
        this.message = message;
        this.authToken = authToken;
        this.status = status;
        this.errorBody = errorBody;
        this.userId = userId;
        this.followInfo = followInfo;
    }

    public ResultObject(Throwable error) {
        this.error = error;
    }

    public ResultObject(Throwable error, int authCallType) {
        this.error = error;
        this.authCallType = authCallType;
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

    public FollowInfo getFollowInfo() {
        return followInfo;
    }

    public String getAuthToken() {
        return authToken;
    }

    public ErrorBody getErrorBody() {
        return errorBody;
    }

    public int getUserId() {
        return userId;
    }

    public Throwable getError() {
        return error;
    }

    public ResultObject setErrorOnly(Throwable error) {
        this.error = error;
        return this;
    }

    @AuthCallType public int getAuthCallType() {
        return authCallType;
    }

    public ResultObject setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public void setCodeOnly(int code) {
        this.code = code;
    }

    public ResultObject setCode(int code) {
        this.code = code;
        return this;
    }

    public ResultObject setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public ResultObject setAdapterPosition(int adapterPosition) {
        this.adapterPosition = adapterPosition;
        return this;
    }

    public void setAuthCallType(@AuthCallType int authCallType) {
        this.authCallType = authCallType;
    }
}

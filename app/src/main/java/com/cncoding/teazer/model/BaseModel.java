package com.cncoding.teazer.model;

import android.arch.persistence.room.Ignore;

import com.cncoding.teazer.utilities.Annotations.CallType;

/**
 *
 * Created by Prem$ on 2/20/2018.
 */

public class BaseModel {

    @Ignore protected Throwable error;
    @Ignore @CallType private int callType;

    @Ignore public BaseModel loadError(Throwable error) {
        this.error = error;
        return this;
    }

    @Ignore public BaseModel loadCallType(@CallType int callType) {
        this.callType = callType;
        return this;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    @CallType public int getCallType() {
        return callType;
    }

    public void setCall(@CallType int callType) {
        this.callType = callType;
    }
}

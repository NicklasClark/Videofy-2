package com.cncoding.teazer.data.model.application;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public class ConfigDetails extends BaseModel {

    @SerializedName("force_update") @Expose private boolean forceUpdate;
    @SerializedName("update_available") @Expose private boolean updateAvailable;

    public ConfigDetails(boolean forceUpdate, boolean updateAvailable) {
        this.forceUpdate = forceUpdate;
        this.updateAvailable = updateAvailable;
    }

    public ConfigDetails(Throwable error) {
        this.error = error;
    }

    public ConfigDetails setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }
}
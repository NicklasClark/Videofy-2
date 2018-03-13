package com.cncoding.teazer.data.model.application;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 06/12/17.
 */

public class DeactivateTypes extends BaseModel {

    @SerializedName("deactivate_id")
    @Expose
    private Integer deactivateId;
    @SerializedName("own_reason")
    @Expose
    private Boolean ownReason;
    @SerializedName("title")
    @Expose
    private String title;

    public DeactivateTypes(Throwable error) {
        this.error = error;
    }

    public DeactivateTypes setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public Integer getDeactivateId() {
        return deactivateId;
    }

    public void setDeactivateId(Integer deactivateId) {
        this.deactivateId = deactivateId;
    }

    public Boolean getOwnReason() {
        return ownReason;
    }

    public void setOwnReason(Boolean ownReason) {
        this.ownReason = ownReason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

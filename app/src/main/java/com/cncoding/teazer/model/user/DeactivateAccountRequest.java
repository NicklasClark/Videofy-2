package com.cncoding.teazer.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 06/12/17.
 */

public class DeactivateAccountRequest {

    @SerializedName("deactivate_id")
    @Expose
    private Integer deactivateId;
    @SerializedName("reason")
    @Expose
    private String reason;

    public Integer getDeactivateId() {
        return deactivateId;
    }

    public void setDeactivateId(Integer deactivateId) {
        this.deactivateId = deactivateId;
    }

    public String getReason() {
        return reason;
    }

    public DeactivateAccountRequest(Integer deactivateId, String reason) {
        this.deactivateId = deactivateId;
        this.reason = reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

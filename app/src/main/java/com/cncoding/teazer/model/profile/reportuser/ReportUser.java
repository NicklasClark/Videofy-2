package com.cncoding.teazer.model.profile.reportuser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 23/11/17.
 */

public class ReportUser {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("report_type_id")
    @Expose
    private Integer reportTypeId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(Integer reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public ReportUser(Integer userId, Integer reportTypeId) {
        this.userId = userId;
        this.reportTypeId = reportTypeId;
    }
}

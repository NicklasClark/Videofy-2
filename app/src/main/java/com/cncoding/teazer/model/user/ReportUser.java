package com.cncoding.teazer.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReportUser implements Parcelable {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("report_type_id")
    @Expose
    private Integer reportTypeId;
    @SerializedName("other_reason")
    @Expose
    private String reportRemarks;

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

    public ReportUser(Integer userId, Integer reportTypeId, String reportRemarks) {
        this.userId = userId;
        this.reportTypeId = reportTypeId;
        this.reportRemarks = reportRemarks;
    }

    public String getReportRemarks() {
        return reportRemarks;
    }

    public void setReportRemarks(String reportRemarks) {
        this.reportRemarks = reportRemarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
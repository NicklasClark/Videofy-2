package com.cncoding.teazer.data.model.application;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by amit on 24/11/17.
 */

public class ReportPostTitlesResponse implements Parcelable {
    @SerializedName("report_type_id")
    @Expose
    private Integer reportTypeId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_reports")
    @Expose
    private List<ReportPostSubTitleResponse> subReports = null;

    public Integer getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(Integer reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ReportPostSubTitleResponse> getSubReports() {
        return subReports;
    }

    public void setSubReports(List<ReportPostSubTitleResponse> subReports) {
        this.subReports = subReports;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.reportTypeId);
        dest.writeString(this.title);
        dest.writeTypedList(this.subReports);
    }

    public ReportPostTitlesResponse() {
    }

    protected ReportPostTitlesResponse(Parcel in) {
        this.reportTypeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.subReports = in.createTypedArrayList(ReportPostSubTitleResponse.CREATOR);
    }

    public static final Creator<ReportPostTitlesResponse> CREATOR = new Creator<ReportPostTitlesResponse>() {
        @Override
        public ReportPostTitlesResponse createFromParcel(Parcel source) {
            return new ReportPostTitlesResponse(source);
        }

        @Override
        public ReportPostTitlesResponse[] newArray(int size) {
            return new ReportPostTitlesResponse[size];
        }
    };
}

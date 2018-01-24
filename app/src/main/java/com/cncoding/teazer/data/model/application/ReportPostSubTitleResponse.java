package com.cncoding.teazer.data.model.application;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by amit on 24/11/17.
 */

public class ReportPostSubTitleResponse implements Parcelable {
    @SerializedName("report_type_id")
    @Expose
    private Integer reportTypeId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_reports")
    @Expose
    private List<Object> subReports = null;

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

    public List<Object> getSubReports() {
        return subReports;
    }

    public void setSubReports(List<Object> subReports) {
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
        dest.writeList(this.subReports);
    }

    public ReportPostSubTitleResponse() {
    }

    protected ReportPostSubTitleResponse(Parcel in) {
        this.reportTypeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.subReports = new ArrayList<Object>();
        in.readList(this.subReports, Object.class.getClassLoader());
    }

    public static final Creator<ReportPostSubTitleResponse> CREATOR = new Creator<ReportPostSubTitleResponse>() {
        @Override
        public ReportPostSubTitleResponse createFromParcel(Parcel source) {
            return new ReportPostSubTitleResponse(source);
        }

        @Override
        public ReportPostSubTitleResponse[] newArray(int size) {
            return new ReportPostSubTitleResponse[size];
        }
    };
}

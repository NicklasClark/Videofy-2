package com.cncoding.teazer.data.model.application;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by amit on 24/11/17.
 */

public class ReportTypes extends BaseModel implements Parcelable {
    @SerializedName("report_type_id")
    @Expose
    private Integer reportTypeId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("sub_reports")
    @Expose
    private List<ReportPostSubTitleResponse> subReports = null;

    public ReportTypes(Throwable error) {
        this.error = error;
    }

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

    public ReportTypes setCallType(@CallType int callType) {
        setCall(callType);
        return this;
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

    public ReportTypes() {
    }

    protected ReportTypes(Parcel in) {
        this.reportTypeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.title = in.readString();
        this.subReports = in.createTypedArrayList(ReportPostSubTitleResponse.CREATOR);
    }

    public static final Creator<ReportTypes> CREATOR = new Creator<ReportTypes>() {
        @Override
        public ReportTypes createFromParcel(Parcel source) {
            return new ReportTypes(source);
        }

        @Override
        public ReportTypes[] newArray(int size) {
            return new ReportTypes[size];
        }
    };
}

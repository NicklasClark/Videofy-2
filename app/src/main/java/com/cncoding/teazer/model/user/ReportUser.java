package com.cncoding.teazer.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReportUser implements Parcelable {
    private int user_id;
    private int report_type_id;

    public ReportUser(int user_id, int report_type_id) {
        this.user_id = user_id;
        this.report_type_id = report_type_id;
    }

    public int getUserId() {
        return user_id;
    }

    public int getReportTypeId() {
        return report_type_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(user_id);
        parcel.writeInt(report_type_id);
    }

    protected ReportUser(Parcel in) {
        user_id = in.readInt();
        report_type_id = in.readInt();
    }

    public static final Creator<ReportUser> CREATOR = new Creator<ReportUser>() {
        @Override
        public ReportUser createFromParcel(Parcel in) {
            return new ReportUser(in);
        }

        @Override
        public ReportUser[] newArray(int size) {
            return new ReportUser[size];
        }
    };
}
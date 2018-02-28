package com.cncoding.teazer.model.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by farazhabib on 27/02/18.
 */

public class Preference implements Parcelable {



    @SerializedName("preference_id")
    @Expose
    private Integer preferenceId;
    @SerializedName("preference_name")
    @Expose
    private String preferenceName;
    @SerializedName("value")
    @Expose
    private Integer value;

    public Preference(Integer preferenceId, String preferenceName, Integer value) {
        this.preferenceId = preferenceId;
        this.preferenceName = preferenceName;
        this.value = value;
    }

    protected Preference(Parcel in) {
        if (in.readByte() == 0) {
            preferenceId = null;
        } else {
            preferenceId = in.readInt();
        }
        preferenceName = in.readString();
        if (in.readByte() == 0) {
            value = null;
        } else {
            value = in.readInt();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (preferenceId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(preferenceId);
        }
        dest.writeString(preferenceName);
        if (value == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(value);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Preference> CREATOR = new Creator<Preference>() {
        @Override
        public Preference createFromParcel(Parcel in) {
            return new Preference(in);
        }

        @Override
        public Preference[] newArray(int size) {
            return new Preference[size];
        }
    };

    public Integer getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(Integer preferenceId) {
        this.preferenceId = preferenceId;
    }

    public String getPreferenceName() {
        return preferenceName;
    }

    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}

package com.cncoding.teazer.model.base;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "Dimension")
public class Dimension extends ViewModel implements Parcelable {

    @SerializedName("height") @Expose private int height;
    @SerializedName("width") @Expose private int width;

    public Dimension(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(height);
        parcel.writeInt(width);
    }

    protected Dimension(Parcel in) {
        height = in.readInt();
        width = in.readInt();
    }

    public static final Creator<Dimension> CREATOR = new Creator<Dimension>() {
        @Override
        public Dimension createFromParcel(Parcel in) {
            return new Dimension(in);
        }

        @Override
        public Dimension[] newArray(int size) {
            return new Dimension[size];
        }
    };

    @Override
    public int hashCode() {
        return new HashCodeBuilder(10, 31)
                .append(width)
                .append(height)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof ProfileMedia &&
                new EqualsBuilder()
                        .append(width, ((ProfileMedia) obj).getPictureId())
                        .append(height, ((ProfileMedia) obj).isImage())
                        .isEquals();
    }
}
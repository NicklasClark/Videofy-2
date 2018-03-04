package com.cncoding.teazer.data.model.base;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */


@Entity(tableName = "Category")
public class Category extends BaseModel implements Parcelable {

    @SerializedName("category_id") @Expose private int category_id;
    @SerializedName("category_name") @Expose private String category_name;
    @SerializedName("color") @Expose private String color;
    @SerializedName("my_color") @Expose private String my_color;

    public Category(int category_id, String category_name, String color, String my_color) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.color = color;
        this.my_color = my_color;
    }

    protected Category(Parcel in) {
        category_id = in.readInt();
        category_name = in.readString();
        color = in.readString();
        my_color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(category_id);
        dest.writeString(category_name);
        dest.writeString(color);
        dest.writeString(my_color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getCategoryId() {
        return category_id;
    }

    public String getCategoryName() {
        return category_name;
    }

    public String getColor() {
        return color;
    }

    public String getMyColor() {
        return my_color;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .append(category_id)
                .append(category_name)
                .append(color)
                .append(my_color)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Category &&
                new EqualsBuilder()
                        .append(category_id, ((Category) obj).getCategoryId())
                        .append(category_name, ((Category) obj).getCategoryName())
                        .append(color, ((Category) obj).getColor())
                        .append(my_color, ((Category) obj).getMyColor())
                        .isEquals();
    }
}
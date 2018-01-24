package com.cncoding.teazer.data.model.base;

import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

@Entity(tableName = "ProfileMedia")
//        foreignKeys = {
//                @ForeignKey(entity = MiniProfile.class, parentColumns = {"user_id"}, childColumns = {"userId"}),
//                @ForeignKey(entity = TaggedUser.class, parentColumns = {"user_id"}, childColumns = {"userId"}),
//                @ForeignKey(entity = ReactedUser.class, parentColumns = {"user_id"}, childColumns = {"userId"})
//})
public class ProfileMedia extends ViewModel implements Parcelable {

    @PrimaryKey @SerializedName("picture_id") @Expose private int picture_id;
//    private int userId;
    @SerializedName("media_url") @Expose private String media_url;
    @SerializedName("thumb_url") @Expose private String thumb_url;
    @SerializedName("duration") @Expose private String duration;
    @Embedded(prefix = "dimnesion_") @SerializedName("media_dimension") @Expose private Dimension media_dimension;
    @SerializedName("is_image") @Expose private boolean is_image;

    protected ProfileMedia(Parcel in) {
        picture_id = in.readInt();
        media_url = in.readString();
        thumb_url = in.readString();
        duration = in.readString();
        media_dimension = in.readParcelable(Dimension.class.getClassLoader());
        is_image = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(picture_id);
        dest.writeString(media_url);
        dest.writeString(thumb_url);
        dest.writeString(duration);
        dest.writeParcelable(media_dimension, flags);
        dest.writeByte((byte) (is_image ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileMedia> CREATOR = new Creator<ProfileMedia>() {
        @Override
        public ProfileMedia createFromParcel(Parcel in) {
            return new ProfileMedia(in);
        }

        @Override
        public ProfileMedia[] newArray(int size) {
            return new ProfileMedia[size];
        }
    };

    public int getPictureId() {
        return picture_id;
    }

    public String getMediaUrl() {
        return media_url;
    }

    public String getThumbUrl() {
        return thumb_url;
    }

    public String getDuration() {
        return duration;
    }

    public Dimension getDimension() {
        return media_dimension;
    }

    public boolean isImage() {
        return is_image;
    }
}
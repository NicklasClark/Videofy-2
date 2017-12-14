package com.cncoding.teazer.model.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class MetaData implements Parcelable {
    private int notification_type;
    private String thumb_url;
    private int from_id;
    private int to_id;
    private int source_id;
    private int post_id;

    public MetaData(int notification_type, String thumb_url, int from_id, int to_id, int source_id, int post_id) {
        this.notification_type = notification_type;
        this.thumb_url = thumb_url;
        this.from_id = from_id;
        this.to_id = to_id;
        this.source_id = source_id;
        this.post_id = post_id;
    }

    protected MetaData(Parcel in) {
        notification_type = in.readInt();
        thumb_url = in.readString();
        from_id = in.readInt();
        to_id = in.readInt();
        source_id = in.readInt();
        post_id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(notification_type);
        dest.writeString(thumb_url);
        dest.writeInt(from_id);
        dest.writeInt(to_id);
        dest.writeInt(source_id);
        dest.writeInt(post_id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MetaData> CREATOR = new Creator<MetaData>() {
        @Override
        public MetaData createFromParcel(Parcel in) {
            return new MetaData(in);
        }

        @Override
        public MetaData[] newArray(int size) {
            return new MetaData[size];
        }
    };

    public int getPostId() {
        return post_id;
    }

    public int getNotificationType() {
        return notification_type;
    }

    public String getThumbUrl() {
        return thumb_url;
    }

    public int getFromId() {
        return from_id;
    }

    public int getToId() {
        return to_id;
    }

    public int getSourceId() {
        return source_id;
    }
}
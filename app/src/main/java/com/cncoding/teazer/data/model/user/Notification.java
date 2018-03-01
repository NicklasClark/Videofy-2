package com.cncoding.teazer.data.model.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.base.ProfileMedia;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class Notification implements Parcelable {
    private int notification_id;
    private int notification_type;
    private int source_id;
    private int account_type;
    private String title;
    private String message;
    private String created_at;
    private boolean has_profile_media;
    private boolean is_actioned;
    private ProfileMedia profile_media;
    private ArrayList<String> highlights;
    private MetaData meta_data;
    private boolean request_sent;
    private boolean following;

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public void setNotification_type(int notification_type) {
        this.notification_type = notification_type;
    }

    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setHas_profile_media(boolean has_profile_media) {
        this.has_profile_media = has_profile_media;
    }

    public void setIs_actioned(boolean is_actioned) {
        this.is_actioned = is_actioned;
    }

    public void setProfile_media(ProfileMedia profile_media) {
        this.profile_media = profile_media;
    }

    public void setHighlights(ArrayList<String> highlights) {
        this.highlights = highlights;
    }

    public void setMeta_data(MetaData meta_data) {
        this.meta_data = meta_data;
    }

    public void setRequest_sent(boolean request_sent) {
        this.request_sent = request_sent;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public void setRequestRecieved(boolean requestRecieved) {
        isRequestRecieved = requestRecieved;
    }

    private boolean isRequestRecieved;



    protected Notification(Parcel in) {
        notification_id = in.readInt();
        notification_type = in.readInt();
        source_id = in.readInt();
        account_type = in.readInt();
        title = in.readString();
        message = in.readString();
        created_at = in.readString();
        has_profile_media = in.readByte() != 0;
        is_actioned = in.readByte() != 0;
        profile_media = in.readParcelable(ProfileMedia.class.getClassLoader());
        highlights = in.createStringArrayList();
        meta_data = in.readParcelable(MetaData.class.getClassLoader());
        request_sent = in.readByte() != 0;
        following = in.readByte() != 0;
    }

    public Notification(int notification_id, int notification_type, int source_id, int account_type,
                        String title, String message, String created_at, boolean has_profile_media, boolean is_actioned,
                        ProfileMedia profile_media, ArrayList<String> highlights, MetaData meta_data, boolean isActioned,
                        boolean request_sent, boolean following) {
        this.notification_id = notification_id;
        this.notification_type = notification_type;
        this.source_id = source_id;
        this.account_type = account_type;
        this.title = title;
        this.message = message;
        this.created_at = created_at;
        this.has_profile_media = has_profile_media;
        this.is_actioned = is_actioned;
        this.profile_media = profile_media;
        this.highlights = highlights;
        this.meta_data = meta_data;
        this.request_sent = request_sent;
        this.following = following;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(notification_id);
        dest.writeInt(notification_type);
        dest.writeInt(source_id);
        dest.writeInt(account_type);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(created_at);
        dest.writeByte((byte) (has_profile_media ? 1 : 0));
        dest.writeByte((byte) (is_actioned ? 1 : 0));
        dest.writeParcelable(profile_media, flags);
        dest.writeStringList(highlights);
        dest.writeParcelable(meta_data, flags);
        dest.writeByte((byte) (request_sent ? 1 : 0));
        dest.writeByte((byte) (following ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public int getNotificationId() {
        return notification_id;
    }

    public int getNotificationType() {
        return notification_type;
    }

    public int getSourceId() {
        return source_id;
    }

    public int getAccountType() {
        return account_type;
    }

    public String getTitle() {
        return title;
    }

    public boolean isActioned() {
        return is_actioned;
    }

    public boolean isFollowing() {
        return following;
    }
    public boolean isRequestSent() {
        return request_sent;
    }
    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public boolean hasProfileMedia() {
        return has_profile_media;
    }

    public ProfileMedia getProfileMedia() {
        return profile_media;
    }

    public ArrayList<String> getHighlights() {
        return highlights;
    }

    public MetaData getMetaData() {
        return meta_data;
    }

}
package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostUploadResult implements Parcelable {
    private boolean status;
    private String message;
    private PostDetails post_details;
    private Throwable error;

    public PostUploadResult(boolean status, String message, PostDetails post_details) {
        this.status = status;
        this.message = message;
        this.post_details = post_details;
    }

    public PostUploadResult(Throwable error) {
        this.error = error;
    }

    protected PostUploadResult(Parcel in) {
        status = in.readByte() != 0;
        message = in.readString();
        post_details = in.readParcelable(PostDetails.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeString(message);
        dest.writeParcelable(post_details, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostUploadResult> CREATOR = new Creator<PostUploadResult>() {
        @Override
        public PostUploadResult createFromParcel(Parcel in) {
            return new PostUploadResult(in);
        }

        @Override
        public PostUploadResult[] newArray(int size) {
            return new PostUploadResult[size];
        }
    };

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getError() {
        return error;
    }

    public PostDetails getPostDetails() {
        return post_details;
    }
}
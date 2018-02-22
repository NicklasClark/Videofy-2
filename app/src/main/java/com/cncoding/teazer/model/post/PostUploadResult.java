package com.cncoding.teazer.model.post;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.utilities.Annotations.CallType;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostUploadResult extends BaseModel implements Parcelable {
    private boolean status;
    private String message;
    private PostDetails post_details;

    public PostUploadResult(boolean status, String message, PostDetails post_details) {
        this.status = status;
        this.message = message;
        this.post_details = post_details;
    }

    public PostUploadResult(Throwable error) {
        this.error = error;
    }

    public PostUploadResult setCallType(@CallType int callType) {
        setCall(callType);
        return this;
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

    public PostDetails getPostDetails() {
        return post_details;
    }
}
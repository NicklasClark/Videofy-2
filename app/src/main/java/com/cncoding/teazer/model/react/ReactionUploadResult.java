package com.cncoding.teazer.model.react;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.utilities.Annotations.CallType;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReactionUploadResult extends BaseModel implements Parcelable {
    private boolean status;
    private int react_id;
    private String message;
    private PostReaction post_react_detail;

    public ReactionUploadResult(boolean status, int react_id, String message, PostReaction post_react_detail) {
        this.status = status;
        this.react_id = react_id;
        this.message = message;
        this.post_react_detail = post_react_detail;
    }

    public ReactionUploadResult(Throwable error) {
        this.error = error;
    }

    public ReactionUploadResult setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    protected ReactionUploadResult(Parcel in) {
        status = in.readByte() != 0;
        react_id = in.readInt();
        message = in.readString();
        post_react_detail = in.readParcelable(PostReaction.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeInt(react_id);
        dest.writeString(message);
        dest.writeParcelable(post_react_detail, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReactionUploadResult> CREATOR = new Creator<ReactionUploadResult>() {
        @Override
        public ReactionUploadResult createFromParcel(Parcel in) {
            return new ReactionUploadResult(in);
        }

        @Override
        public ReactionUploadResult[] newArray(int size) {
            return new ReactionUploadResult[size];
        }
    };

    public boolean getStatus() {
        return status;
    }

    public int getReactId() {
        return react_id;
    }

    public String getMessage() {
        return message;
    }

    public PostReaction getPostReactDetail() {
        return post_react_detail;
    }
}
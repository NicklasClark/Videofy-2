package com.cncoding.teazer.home.post;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 10/24/2017.
 */

class PostObject implements Parcelable {

    private static final String PROFILE_PIC_NOT_AVAILABLE = "notAvailable";
    private String thumbnailUrl;
    private String caption;
    private String category;
    private String name;
    private int viewCount;
    private int reactionCount;
    private String profilePicUrl;

    private PostObject(Parcel in) {
        thumbnailUrl = in.readString();
        caption = in.readString();
        category = in.readString();
        name = in.readString();
        viewCount = in.readInt();
        reactionCount = in.readInt();
        profilePicUrl = in.readString();
    }

    public static final Creator<PostObject> CREATOR = new Creator<PostObject>() {
        @Override
        public PostObject createFromParcel(Parcel in) {
            return new PostObject(in);
        }

        @Override
        public PostObject[] newArray(int size) {
            return new PostObject[size];
        }
    };

    /**
     * Constructor including all values
     * */
    public PostObject(String thumbnailUrl, String caption, String category, String name,
                      int viewCount, int reactionCount, String profilePicUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.caption = caption;
        this.category = category;
        this.name = name;
        this.viewCount = viewCount;
        this.reactionCount = reactionCount;
        this.profilePicUrl = profilePicUrl;
    }

    /**
     * Constructor in case profile pic is not available
     * */
    public PostObject(String thumbnailUrl, String caption, String category, String name, int viewCount, int reactionCount) {
        this.thumbnailUrl = thumbnailUrl;
        this.caption = caption;
        this.category = category;
        this.name = name;
        this.viewCount = viewCount;
        this.reactionCount = reactionCount;
        profilePicUrl = PROFILE_PIC_NOT_AVAILABLE;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(thumbnailUrl);
        parcel.writeString(caption);
        parcel.writeString(category);
        parcel.writeString(name);
        parcel.writeInt(viewCount);
        parcel.writeInt(reactionCount);
        parcel.writeString(profilePicUrl);
    }
}

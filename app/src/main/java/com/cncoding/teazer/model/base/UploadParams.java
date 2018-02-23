package com.cncoding.teazer.model.base;

import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.model.post.PostDetails;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UploadParams implements Parcelable {

    private boolean isGiphy;
    private String videoPath;
    public boolean isReaction;
    private String title;
    private String location;
    private double latitude;
    private double longitude;
    private String tags;
    private String categories;
    private PostDetails postDetails;
    private boolean isGallery;

    public UploadParams(boolean isGallery, String videoPath, String title, String location,
                        double latitude, double longitude, String tags, String categories, PostDetails postDetails, boolean isGiphy) {
        this.isGallery = isGallery;
        this.videoPath = videoPath;
        this.title = title;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
        this.categories = categories;
        this.postDetails = postDetails;
        this.isGiphy = isGiphy;
    }

    public UploadParams(boolean isGallery, String videoPath, String title, String location,
                        double latitude, double longitude, PostDetails postDetails, boolean isGiphy) {
        this.isGallery = isGallery;
        this.videoPath = videoPath;
        this.title = title;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postDetails = postDetails;
        this.isGiphy = isGiphy;
    }

    public UploadParams(String videoPath, boolean isReaction, String title, PostDetails postDetails) {
        this.videoPath = videoPath;
        this.isReaction = isReaction;
        this.title = title;
        this.postDetails = postDetails;
    }

    public UploadParams(String videoPath) {
        this.videoPath = videoPath;
    }

    protected UploadParams(Parcel in) {
        isGiphy = in.readByte() != 0;
        videoPath = in.readString();
        isReaction = in.readByte() != 0;
        title = in.readString();
        location = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        tags = in.readString();
        categories = in.readString();
        postDetails = in.readParcelable(PostDetails.class.getClassLoader());
        isGallery = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isGiphy ? 1 : 0));
        dest.writeString(videoPath);
        dest.writeByte((byte) (isReaction ? 1 : 0));
        dest.writeString(title);
        dest.writeString(location);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(tags);
        dest.writeString(categories);
        dest.writeParcelable(postDetails, flags);
        dest.writeByte((byte) (isGallery ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UploadParams> CREATOR = new Creator<UploadParams>() {
        @Override
        public UploadParams createFromParcel(Parcel in) {
            return new UploadParams(in);
        }

        @Override
        public UploadParams[] newArray(int size) {
            return new UploadParams[size];
        }
    };

    public String getVideoPath() {
        return videoPath;
    }

    public boolean isReaction() {
        return isReaction;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTags() {
        return tags;
    }

    public String getCategories() {
        return categories;
    }

    public PostDetails getPostDetails() {
        return postDetails;
    }

    public boolean isGallery() {
        return isGallery;
    }

    public void setGallery(boolean gallery) {
        isGallery = gallery;
    }

    public boolean isGiphy() {
        return isGiphy;
    }

    public void setGiphy(boolean giphy) {
        isGiphy = giphy;
    }
}
package com.cncoding.teazer.data.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UploadParams implements Parcelable {

    private boolean isGiphy;
    private String videoPath;
    private boolean isReaction;
    private String title;
    private String location;
    private double latitude;
    private double longitude;
    private String selectedTagsToSend;
    private String selectedCategoriesToSend;
    private String tags;
    private String categories;
    private int postId;
    private boolean isGallery;

    public UploadParams(boolean isGallery, String videoPath, String title, String location,
                        double latitude, double longitude, String tags, String categories, int postId, boolean isGiphy) {
        this.isGallery = isGallery;
        this.videoPath = videoPath;
        this.title = title;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
        this.categories = categories;
        this.postId = postId;
        this.isGiphy = isGiphy;
    }

    public UploadParams(boolean isGallery, String videoPath, String title, String location,
                        double latitude, double longitude, int postId, boolean isGiphy) {
        this.isGallery = isGallery;
        this.videoPath = videoPath;
        this.title = title;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postId = postId;
        this.isGiphy = isGiphy;
    }

    public UploadParams(String videoPath, boolean isReaction, String title, int postId) {
        this.videoPath = videoPath;
        this.isReaction = isReaction;
        this.title = title;
        this.postId = postId;
    }

    public UploadParams(String videoPath) {
        this.videoPath = videoPath;
    }

    public UploadParams(int postId, boolean isGallery, String videoPath, String title, String location,
                        double latitude, double longitude, String selectedTagsToSend, String selectedCategoriesToSend, boolean isGiphy) {
        this.postId = postId;
        this.isGallery = isGallery;
        this.videoPath = videoPath;
        this.title = title;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.selectedTagsToSend = selectedTagsToSend;
        this.selectedCategoriesToSend = selectedCategoriesToSend;
        this.isGiphy = isGiphy;
    }

    public UploadParams(int postId, boolean isGallery, String videoPath, String title, String location,
                        double latitude, double longitude, boolean isGiphy) {
        this.postId = postId;
        this.isGallery = isGallery;
        this.videoPath = videoPath;
        this.title = title;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isGiphy = isGiphy;
    }

    protected UploadParams(Parcel in) {
        isGiphy = in.readByte() != 0;
        videoPath = in.readString();
        isReaction = in.readByte() != 0;
        title = in.readString();
        location = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        selectedTagsToSend = in.readString();
        selectedCategoriesToSend = in.readString();
        tags = in.readString();
        categories = in.readString();
        postId = in.readInt();
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
        dest.writeString(selectedTagsToSend);
        dest.writeString(selectedCategoriesToSend);
        dest.writeString(tags);
        dest.writeString(categories);
        dest.writeInt(postId);
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

    public int getPostId() {
        return postId;
    }

    public boolean isGallery() {
        return isGallery;
    }

    public void setGallery(boolean gallery) {
        isGallery = gallery;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setReaction(boolean reaction) {
        isReaction = reaction;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSelectedTagsToSend() {
        return selectedTagsToSend;
    }

    public void setSelectedTagsToSend(String selectedTagsToSend) {
        this.selectedTagsToSend = selectedTagsToSend;
    }

    public String getSelectedCategoriesToSend() {
        return selectedCategoriesToSend;
    }

    public void setSelectedCategoriesToSend(String selectedCategoriesToSend) {
        this.selectedCategoriesToSend = selectedCategoriesToSend;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public boolean isGiphy() {
        return isGiphy;
    }

    public void setGiphy(boolean giphy) {
        isGiphy = giphy;
    }
}
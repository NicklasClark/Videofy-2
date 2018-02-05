package com.cncoding.teazer.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 03/12/17.
 */

public class UpdatePostRequest {
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("tags")
    @Expose
    private String tags;
    @SerializedName("categories")
    @Expose
    private String categories;

    public UpdatePostRequest(Integer postId, String location, String title, Double latitude, Double longitude, String tags, String categories) {
        this.postId = postId;
        this.location = location;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
        this.categories = categories;
    }

    public Integer getPostId() {

        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

}

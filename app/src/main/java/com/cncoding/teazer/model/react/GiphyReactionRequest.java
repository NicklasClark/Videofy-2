package com.cncoding.teazer.model.react;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 1/2/18.
 */

public class GiphyReactionRequest {
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("gif_info")
    @Expose
    private String gifInfo;

    public GiphyReactionRequest(Integer postId, String title, String gifInfo) {
        this.postId = postId;
        this.title = title;
        this.gifInfo = gifInfo;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGifInfo() {
        return gifInfo;
    }

    public void setGifInfo(String gifInfo) {
        this.gifInfo = gifInfo;
    }
}

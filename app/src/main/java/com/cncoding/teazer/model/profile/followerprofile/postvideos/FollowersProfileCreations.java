package com.cncoding.teazer.model.profile.followerprofile.postvideos;

import com.cncoding.teazer.utilities.Pojos;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by farazhabib on 11/11/17.
 */

public class FollowersProfileCreations {

    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("posts")
    @Expose
    private List<Pojos.Post.PostDetails> posts = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<Pojos.Post.PostDetails> getPosts() {
        return posts;
    }

    public void setPosts(List<Pojos.Post.PostDetails> posts) {
        this.posts = posts;
    }

}
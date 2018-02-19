package com.cncoding.teazer.model.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by farazhabib on 29/12/17.
 */

public class LikedUserPost {


    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("liked_users")
    @Expose
    private List<LikedUser> likedUsers = null;
    private Throwable error;

    public LikedUserPost(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<LikedUser> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(List<LikedUser> likedUsers) {
        this.likedUsers = likedUsers;
    }

}


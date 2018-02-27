package com.cncoding.teazer.model.friends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by farazhabib on 24/02/18.
 */

public class UserFollowerList {
    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("followers")
    @Expose
    private List<NewUserInfo> followers = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<NewUserInfo> getFollowers() {
        return followers;
    }

    public void setFollowers(List<NewUserInfo> followers) {
        this.followers = followers;
    }

}

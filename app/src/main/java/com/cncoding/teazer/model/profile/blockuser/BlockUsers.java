package com.cncoding.teazer.model.profile.blockuser;

/**
 * Created by farazhabib on 15/11/17.
 */

import java.util.List;

import com.cncoding.teazer.model.profile.followers.Follower;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockUsers {

    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("followers")
    @Expose
    private List<Followers> followers = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<Followers> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Followers> followers) {
        this.followers = followers;
    }

}
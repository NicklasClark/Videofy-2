package com.cncoding.teazer.model.profile.followers;

import com.cncoding.teazer.model.profile.followers.Follower;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by farazhabib on 10/11/17.
 */

public class ProfileMyFollowers {

    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("followers")
    @Expose
    private List<Follower> followers = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

}
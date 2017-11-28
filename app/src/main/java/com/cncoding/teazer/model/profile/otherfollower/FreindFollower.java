package com.cncoding.teazer.model.profile.otherfollower;

import com.cncoding.teazer.model.profile.followers.Follower;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by farazhabib on 15/11/17.
 */

public class FreindFollower {



    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("followers")
    @Expose
    private List<OtherFollowers> followers = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<OtherFollowers> getFollowers() {
        return followers;
    }

    public void setFollowers(List<OtherFollowers> followers) {
        this.followers = followers;
    }
}


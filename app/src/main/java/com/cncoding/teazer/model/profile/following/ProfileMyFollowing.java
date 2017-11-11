package com.cncoding.teazer.model.profile.following;

/**
 * Created by farazhabib on 10/11/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileMyFollowing {

    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("followings")
    @Expose
    private List<Following> followings = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<Following> getFollowings() {
        return followings;
    }

    public void setFollowings(List<Following> followings) {
        this.followings = followings;
    }

}

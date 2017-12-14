package com.cncoding.teazer.model.friends;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowingsList {

    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("followings")
    @Expose
    private List<UserInfo> followings = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<UserInfo> getFollowings() {
        return followings;
    }

    public void setFollowings(List<UserInfo> followings) {
        this.followings = followings;
    }

}

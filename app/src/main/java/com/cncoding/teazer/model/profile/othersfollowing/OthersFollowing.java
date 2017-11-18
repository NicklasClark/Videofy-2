package com.cncoding.teazer.model.profile.othersfollowing;


import java.util.List;

import com.cncoding.teazer.model.profile.following.Following;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OthersFollowing {

    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("followings")
    @Expose
    private List<OtherUserFollowings> followings = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<OtherUserFollowings> getFollowings() {
        return followings;
    }

    public void setFollowings(List<OtherUserFollowings> followings) {
        this.followings = followings;
    }

}
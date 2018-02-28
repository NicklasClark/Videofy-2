package com.cncoding.teazer.model.friends;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.utilities.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowingsList extends BaseModel {

    @SerializedName("next_page") @Expose private Boolean nextPage;
    @SerializedName("followings") @Expose private List<UserInfo> followings = null;

    public FollowingsList(Throwable error) {
        this.error = error;
    }

    public FollowingsList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

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

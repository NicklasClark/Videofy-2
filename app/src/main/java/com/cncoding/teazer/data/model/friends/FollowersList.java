package com.cncoding.teazer.data.model.friends;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowersList extends BaseModel {

    @SerializedName("next_page") @Expose private Boolean nextPage;
    @SerializedName("followers") @Expose private List<UserInfo> userInfos = null;

    public FollowersList(Throwable error) {
        this.error = error;
    }

    public FollowersList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<UserInfo> getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(List<UserInfo> userInfos) {
        this.userInfos = userInfos;
    }

}
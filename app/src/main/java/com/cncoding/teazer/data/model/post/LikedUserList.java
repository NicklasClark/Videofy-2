package com.cncoding.teazer.data.model.post;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by farazhabib on 29/12/17.
 */

public class LikedUserList extends BaseModel {

    @SerializedName("next_page") @Expose private Boolean nextPage;
    @SerializedName("liked_users") @Expose private List<LikedUser> likedUsers;

    public LikedUserList(Throwable error) {
        this.error = error;
    }

    public LikedUserList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
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

package com.cncoding.teazer.model.profile.blockuser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by farazhabib on 28/11/17.
 */

public class BlockUserResponse {



    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("blocked_users")
    @Expose
    private List<BlockedUser> blockedUsers = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<BlockedUser> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(List<BlockedUser> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

}

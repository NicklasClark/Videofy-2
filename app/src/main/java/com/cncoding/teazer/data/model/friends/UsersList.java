package com.cncoding.teazer.data.model.friends;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.utilities.common.Annotations.CallType;

import java.util.List;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UsersList extends BaseModel {
    private boolean next_page;
    private List<MiniProfile> users;

    public UsersList(boolean next_page, List<MiniProfile> users) {
        this.next_page = next_page;
        this.users = users;
    }

    public UsersList(Throwable error) {
        this.error = error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public List<MiniProfile> getUsers() {
        return users;
    }

    public UsersList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }
}
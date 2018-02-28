package com.cncoding.teazer.model.friends;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.utilities.Annotations.CallType;

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
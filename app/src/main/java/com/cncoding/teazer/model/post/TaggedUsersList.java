package com.cncoding.teazer.model.post;

import com.cncoding.teazer.model.BaseModel;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.utilities.Annotations.CallType;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class TaggedUsersList extends BaseModel {
    private boolean next_page;
    private ArrayList<TaggedUser> tagged_users;

    public TaggedUsersList(boolean next_page, ArrayList<TaggedUser> tagged_users) {
        this.next_page = next_page;
        this.tagged_users = tagged_users;
    }

    public TaggedUsersList(Throwable error) {
        this.error = error;
    }

    public TaggedUsersList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<TaggedUser> getTaggedUsers() {
        return tagged_users;
    }
}
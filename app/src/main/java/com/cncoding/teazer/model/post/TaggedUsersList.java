package com.cncoding.teazer.model.post;

import com.cncoding.teazer.model.base.TaggedUser;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class TaggedUsersList {
    private boolean next_page;
    private ArrayList<TaggedUser> tagged_users;
    private Throwable error;

    public TaggedUsersList(boolean next_page, ArrayList<TaggedUser> tagged_users) {
        this.next_page = next_page;
        this.tagged_users = tagged_users;
    }

    public TaggedUsersList(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<TaggedUser> getTaggedUsers() {
        return tagged_users;
    }
}
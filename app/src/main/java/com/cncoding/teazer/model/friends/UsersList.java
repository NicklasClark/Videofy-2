package com.cncoding.teazer.model.friends;

import com.cncoding.teazer.model.base.MiniProfile;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class UsersList {
    private boolean next_page;
    private ArrayList<MiniProfile> users;

    public UsersList(boolean next_page, ArrayList<MiniProfile> users) {
        this.next_page = next_page;
        this.users = users;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<MiniProfile> getUsers() {
        return users;
    }
}
package com.cncoding.teazer.model.react;

import com.cncoding.teazer.model.post.PostReaction;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class HiddenReactionsList {
    private boolean next_page;
    private ArrayList<PostReaction> reactions;

    public HiddenReactionsList(boolean next_page, ArrayList<PostReaction> reactions) {
        this.next_page = next_page;
        this.reactions = reactions;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<PostReaction> getReactions() {
        return reactions;
    }
}
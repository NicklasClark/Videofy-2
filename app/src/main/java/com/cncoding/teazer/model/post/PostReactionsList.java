package com.cncoding.teazer.model.post;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class PostReactionsList {
    private boolean next_page;
    private ArrayList<PostReaction> reactions;
    private Throwable error;

    public PostReactionsList(boolean next_page, ArrayList<PostReaction> reactions) {
        this.next_page = next_page;
        this.reactions = reactions;
    }

    public PostReactionsList(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<PostReaction> getReactions() {
        return reactions;
    }
}
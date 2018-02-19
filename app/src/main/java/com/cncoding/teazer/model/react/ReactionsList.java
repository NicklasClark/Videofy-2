package com.cncoding.teazer.model.react;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReactionsList {
    private boolean next_page;
    private ArrayList<Reactions> reactions;
    private Throwable error;

    public ReactionsList(boolean next_page, ArrayList<Reactions> reactions) {
        this.next_page = next_page;
        this.reactions = reactions;
    }

    public ReactionsList(Throwable error) {
        this.error = error;
    }

    public Throwable getError() {
        return error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<Reactions> getReactions() {
        return reactions;
    }
}
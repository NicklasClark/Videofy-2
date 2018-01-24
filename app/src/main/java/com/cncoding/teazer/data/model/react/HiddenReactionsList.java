package com.cncoding.teazer.data.model.react;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class HiddenReactionsList {
    private boolean next_page;
    private ArrayList<ReactionDetails> reactions;

    public HiddenReactionsList(boolean next_page, ArrayList<ReactionDetails> reactions) {
        this.next_page = next_page;
        this.reactions = reactions;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<ReactionDetails> getReactions() {
        return reactions;
    }
}
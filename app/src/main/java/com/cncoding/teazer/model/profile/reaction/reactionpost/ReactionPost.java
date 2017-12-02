package com.cncoding.teazer.model.profile.reaction.reactionpost;

import com.cncoding.teazer.model.profile.reaction.Reaction;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by farazhabib on 22/11/17.
 */

public class ReactionPost {
    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("reactions")
    @Expose
    private List<Reaction> reactions = null;

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }


}

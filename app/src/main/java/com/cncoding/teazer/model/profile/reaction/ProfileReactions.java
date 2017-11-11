package com.cncoding.teazer.model.profile.reaction;

/**
 * Created by farazhabib on 10/11/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileReactions {

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

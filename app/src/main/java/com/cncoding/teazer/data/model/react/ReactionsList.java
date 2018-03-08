package com.cncoding.teazer.data.model.react;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReactionsList extends BaseModel {

    @SerializedName("next_page") @Expose private boolean nextPage;
    @SerializedName("reactions") @Expose private ArrayList<PostReaction> reactions;
    @SerializedName("can_show_reactions") @Expose private boolean canShowReactions;

    public ReactionsList(Throwable error)
    {
        this.error = error;
    }

    public ReactionsList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public ArrayList<PostReaction> getReactions() {
        return reactions;
    }

    public boolean canShowReactions() {
        return canShowReactions;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public void setReactions(ArrayList<PostReaction> reactions) {
        this.reactions = reactions;
    }

    public void setCanShowReactions(boolean canShowReactions) {
        this.canShowReactions = canShowReactions;
    }
}
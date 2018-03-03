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

    private boolean next_page;
    private ArrayList<PostReaction> reactions;
    @SerializedName("can_show_reactions")
    @Expose
    private Boolean canShowReactions;


    public Boolean getCanShowReactions() {
        return canShowReactions;
    }

    public ReactionsList(Throwable error)
    {
        this.error = error;
    }

    public ReactionsList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<PostReaction> getReactions() {
        return reactions;
    }
}
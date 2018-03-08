package com.cncoding.teazer.data.model.profile;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by farazhabib on 03/03/18.
 */

public class DefaultCoverImageResponse extends BaseModel {
    @SerializedName("next_page")
    @Expose
    private Boolean nextPage;
    @SerializedName("default_cover_medias")
    @Expose
    private List<DefaultCoverMedia> defaultCoverMedias = null;

    public DefaultCoverImageResponse(Throwable error) {
        this.error = error;
    }

    public DefaultCoverImageResponse setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public Boolean getNextPage() {
        return nextPage;
    }

    public void setNextPage(Boolean nextPage) {
        this.nextPage = nextPage;
    }

    public List<DefaultCoverMedia> getDefaultCoverMedias() {
        return defaultCoverMedias;
    }

    public void setDefaultCoverMedias(List<DefaultCoverMedia> defaultCoverMedias) {
        this.defaultCoverMedias = defaultCoverMedias;
    }

}


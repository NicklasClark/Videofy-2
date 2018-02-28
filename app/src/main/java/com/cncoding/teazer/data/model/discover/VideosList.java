package com.cncoding.teazer.data.model.discover;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class VideosList extends BaseModel {
    private boolean next_page;
    private ArrayList<Videos> videos;

    public VideosList(boolean next_page, ArrayList<Videos> videos) {
        this.next_page = next_page;
        this.videos = videos;
    }

    public VideosList(Throwable error) {
        this.error = error;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<Videos> getVideos() {
        return videos;
    }

    public VideosList setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }
}
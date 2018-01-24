package com.cncoding.teazer.data.model.discover;

import java.util.ArrayList;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class VideosList {
    private boolean next_page;
    private ArrayList<Videos> videos;

    public VideosList(boolean next_page, ArrayList<Videos> videos) {
        this.next_page = next_page;
        this.videos = videos;
    }

    public boolean isNextPage() {
        return next_page;
    }

    public ArrayList<Videos> getVideos() {
        return videos;
    }
}
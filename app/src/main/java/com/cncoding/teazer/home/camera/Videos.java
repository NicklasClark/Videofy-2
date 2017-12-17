package com.cncoding.teazer.home.camera;

/**
 *
 * Created by Prem $ on 10/12/2017.
 */

public class Videos {
    private String path;
//    private String thumbnail;
    private long duration;

    Videos(String path, long duration) {
        this.path = path;
//        this.thumbnail = thumbnail;
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

//    String getThumbnail() {
//        return thumbnail;
//    }

    public long getDuration() {
        return duration;
    }


//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
}

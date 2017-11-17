package com.cncoding.teazer.home.camera;

/**
 *
 * Created by Prem $ on 10/12/2017.
 */

public class Videos {
    private String path;
    private String thumbnail;

    Videos(String path, String thumbnail) {
        this.path = path;
        this.thumbnail = thumbnail;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    String getThumbnail() {
        return thumbnail;
    }

//    public void setThumbnail(String thumbnail) {
//        this.thumbnail = thumbnail;
//    }
}

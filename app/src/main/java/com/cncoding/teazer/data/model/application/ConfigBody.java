package com.cncoding.teazer.data.model.application;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public class ConfigBody {

    private int platform;
    private String version;

    public ConfigBody(int platform, String version) {
        this.platform = platform;
        this.version = version;
    }

    public int getPlatform() {
        return platform;
    }

    public String getVersion() {
        return version;
    }
}
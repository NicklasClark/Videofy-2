package com.cncoding.teazer.ui.home.camera.nearbyPlaces;

/**
 *
 * Created by Prem $ on 10/18/2017.
 */

public class SelectedPlace {

    private String placeName;
    private double latitude;
    private double longitude;

    public SelectedPlace(String placeName, double latitude, double longitude) {
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

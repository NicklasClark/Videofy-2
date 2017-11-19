package com.cncoding.teazer.home.camera.nearbyPlaces;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Prem $ on 10/18/2017.
 */

public class DataParser {

    public static ArrayList<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.e("Places", "Parse Error: " + e.getMessage());
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private static ArrayList<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        ArrayList<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap;

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);

            } catch (JSONException e) {
                Log.e("getPlaces", "Parse Error: " + e.getMessage());
            }
        }
        return placesList;
    }

    private static HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude;
        String longitude;
        String icon;
//        String nextPageToken;
//        String reference;

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            icon = googlePlaceJson.getString("icon");
//            nextPageToken = googlePlaceJson.getString("next_page_token");
//            reference = googlePlaceJson.getString("reference");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("icon", icon);
//            googlePlaceMap.put("next_page_token", nextPageToken);
//            googlePlaceMap.put("reference", reference);
        } catch (JSONException e) {
            Log.d("getPlace", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}

package com.cncoding.teazer.home.camera.nearbyPlaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * Created by Prem $ on 10/18/2017.
 */

public class NearbyPlacesAdapter extends RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder> {

    private ArrayList<HashMap<String, String>> places;
    private NearbyPlacesInteractionListener mListener;

    NearbyPlacesAdapter(ArrayList<HashMap<String, String>> places, Context context) {
        this.places = places;

        if (context instanceof NearbyPlacesInteractionListener) {
            mListener = (NearbyPlacesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RecyclerViewInteractionListener");
        }
    }

    @Override
    public NearbyPlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        HashMap<String, String> googlePlace = places.get(position);
        final String placeName = googlePlace.get("place_name");
        final double latitude = Double.parseDouble(googlePlace.get("lat"));
        final double longitude = Double.parseDouble(googlePlace.get("lng"));

//        new ImageLoader().load(googlePlace.get("icon")).into(holder.image);
        holder.placeName.setText(placeName);
        holder.placeAddress.setText(googlePlace.get("vicinity"));

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onNearbyPlacesInteraction(new SelectedPlace(placeName, latitude, longitude));
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rootLayout;
//        private ImageView image;
        private ProximaNovaRegularTextView placeName;
        private ProximaNovaRegularTextView placeAddress;

        ViewHolder(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.nearby_place_item_layout);
//            image = view.findViewById(R.id.nearby_place_side_image);
            placeName = view.findViewById(R.id.nearby_place_name);
            placeAddress = view.findViewById(R.id.nearby_place_address);
        }
    }

    public interface NearbyPlacesInteractionListener {
        void onNearbyPlacesInteraction(SelectedPlace selectedPlace);
    }
}
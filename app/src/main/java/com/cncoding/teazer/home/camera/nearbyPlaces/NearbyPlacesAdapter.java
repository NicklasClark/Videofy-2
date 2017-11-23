package com.cncoding.teazer.home.camera.nearbyPlaces;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.TypeFactory;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 10/18/2017.
 */

public class NearbyPlacesAdapter extends RecyclerView.Adapter<NearbyPlacesAdapter.ViewHolder> implements Filterable {

    static final int TYPE_NEARBY_PLACES = 1;
    static final int TYPE_AUTOCOMPLETE = 2;

    private ArrayList<HashMap<String, String>> places;
    private NearbyPlacesInteractionListener mListener;

    private Context context;
    private static final String TAG = "NearbyPlacesAdapter";
    private static CharacterStyle STYLE_BOLD;
    private static CharacterStyle STYLE_REGULAR;
    private ArrayList<PlaceAutocomplete> resultList;
    private GoogleApiClient googleApiClient;
    private LatLngBounds bounds;
    private AutocompleteFilter placeFilter;

    NearbyPlacesAdapter(Context context, ArrayList<HashMap<String, String>> places,
                        GoogleApiClient googleApiClient, LatLngBounds bounds, AutocompleteFilter placeFilter) {
        this.places = places;
        this.context = context;
        this.googleApiClient = googleApiClient;
        this.bounds = bounds;
        this.placeFilter = placeFilter;
        STYLE_BOLD = new StyleSpan(new TypeFactory(context).bold.getStyle());
        STYLE_REGULAR = new StyleSpan(new TypeFactory(context).regular.getStyle());
        initializeListener(context);
    }

    private void initializeListener(Context context) {
        if (mListener == null) {
            if (context instanceof NearbyPlacesInteractionListener) {
                mListener = (NearbyPlacesInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement RecyclerViewInteractionListener");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return googleApiClient != null ? TYPE_AUTOCOMPLETE : TYPE_NEARBY_PLACES;
    }

    /**
     * Clear List items
     */
    void clearList(){
        if(resultList!=null && resultList.size()>0){
            resultList.clear();
        }
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public boolean isAutoCompleteAdapter() {
        return googleApiClient != null;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    resultList = getAutocomplete(constraint);
                    if (resultList != null) {
                        // The API successfully returned results.
                        results.values = resultList;
                        results.count = resultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    if (places != null && places.size() > 0) {
                        places.clear();
                        notifyItemRangeRemoved(0, places.size());
                    }
                    notifyDataSetChanged();
                }
//                else {
//                    // The API did not return any results, invalidate the data set.
//                    //notifyDataSetInvalidated();
//                }
            }
        };
    }

    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        if (googleApiClient.isConnected()) {
            Log.i("", "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results = Places.GeoDataApi.getAutocompletePredictions(
                    googleApiClient, constraint.toString(), bounds, placeFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results.await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
//                Toast.makeText(mContext, "Error contacting API: " + status.toString(),
//                        Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount() + " predictions.");

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList<PlaceAutocomplete> resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getFullText(STYLE_BOLD), prediction.getPrimaryText(STYLE_REGULAR)));
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();

            return resultList;
        }
        Log.e(TAG, "Google API client is not connected for autocomplete query.");
        return null;
    }

    @Override
    public NearbyPlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nearby_place, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_NEARBY_PLACES:
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
                        if (mListener != null)
                            mListener.onNearbyPlacesAdapterInteraction(new SelectedPlace(placeName, latitude, longitude));
                        else
                            Toast.makeText(context, "mListener is null!\nImplement NearbyPlacesInteractionListener", Toast.LENGTH_SHORT)
                                    .show();
                    }
                });
                break;
            case TYPE_AUTOCOMPLETE:
                holder.placeAddress.setText(resultList.get(position).description);
                holder.placeName.setText(resultList.get(position).name);

                holder.rootLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null)
                            mListener.onPlaceClick(resultList, holder.getAdapterPosition());
                        else
                            Toast.makeText(context, "mListener is null!\nImplement NearbyPlacesInteractionListener", Toast.LENGTH_SHORT)
                                    .show();
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (googleApiClient != null) {
            if(resultList != null)
                return resultList.size();
            else
                return 0;
        } else
            return places.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nearby_place_item_layout) RelativeLayout rootLayout;
        @BindView(R.id.nearby_place_name) ProximaNovaRegularTextView placeName;
        @BindView(R.id.nearby_place_address) ProximaNovaRegularTextView placeAddress;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    public class PlaceAutocomplete {

        public CharSequence placeId;
        CharSequence description;
        private CharSequence name;

        PlaceAutocomplete(CharSequence placeId, CharSequence description, CharSequence name) {
            this.placeId = placeId;
            this.description = description;
            this.name = name;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }

    public interface NearbyPlacesInteractionListener {
        void onNearbyPlacesAdapterInteraction(SelectedPlace selectedPlace);
        void onPlaceClick(ArrayList<PlaceAutocomplete> mResultList, int position);
    }
}
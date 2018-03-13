package com.cncoding.teazer.ui.home.camera.nearbyPlaces;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.ui.home.base.BaseHomeFragment;
import com.cncoding.teazer.ui.home.camera.CameraActivity;
import com.cncoding.teazer.ui.home.camera.adapters.NearbyPlacesAdapter;
import com.cncoding.teazer.ui.home.profile.activity.EditPost;
import com.cncoding.teazer.utilities.common.ViewUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

import static com.cncoding.teazer.ui.home.camera.adapters.NearbyPlacesAdapter.TYPE_AUTOCOMPLETE;
import static com.cncoding.teazer.ui.home.camera.adapters.NearbyPlacesAdapter.TYPE_NEARBY_PLACES;

public class NearbyPlacesFragment extends BaseHomeFragment {

    private static final String NEARBY_PLACES = "nearbyPlaces";
    public static final int TURN_ON_LOCATION_ACTION = 454;
    public static final int NEARBY_PLACE_AUTOCOMPLETE_ACTION = 453;
    private static final LatLngBounds BOUNDS = new LatLngBounds(new LatLng(-0, 0), new LatLng(0, 0));

    @BindView(R.id.nearby_places_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.location_not_available_layout) LinearLayout locationNotAvailableLayout;
    @BindView(R.id.turn_on_location_btn) ProximaNovaSemiboldButton turnOnLocationBtn;
    @BindView(R.id.nearby_places_search) ProximaNovaRegularAutoCompleteTextView searchBar;
    @BindView(R.id.clear) AppCompatImageView clearBtn;

    private ArrayList<HashMap<String, String>> nearbyPlaces;
    private Handler handler;
    private GoogleApiClient googleApiClient;
    private OnNearbyPlacesListInteractionListener mListener;

    public static NearbyPlacesFragment newInstance(@Nullable ArrayList<HashMap<String, String>> nearbyPlaces) {
        NearbyPlacesFragment fragment = new NearbyPlacesFragment();
        Bundle args = new Bundle();
        args.putSerializable(NEARBY_PLACES, nearbyPlaces);
        fragment.setArguments(args);
        return fragment;
    }

    public NearbyPlacesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //noinspection unchecked
            nearbyPlaces = (ArrayList<HashMap<String, String>>) getArguments().getSerializable(NEARBY_PLACES);
        }
        if (getActivity() != null) {
            if(getActivity() instanceof CameraActivity)
               googleApiClient = ((CameraActivity) getActivity()).getGoogleApiClient();
            else
                googleApiClient = ((EditPost) getActivity()).getGoogleApiClient();
        }
        if (googleApiClient != null && !googleApiClient.isConnected())
            googleApiClient.connect();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nearby_places_list, container, false);
        ButterKnife.bind(this, rootView);

        if (nearbyPlaces != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getTheContext()));
            recyclerView.setAdapter(getNearbyPlacesAdapter(TYPE_NEARBY_PLACES));
        }
        else {
            recyclerView.setVisibility(View.INVISIBLE);
            locationNotAvailableLayout.startAnimation(AnimationUtils.loadAnimation(getTheContext(), android.R.anim.fade_in));
            locationNotAvailableLayout.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getActivity() instanceof CameraActivity) {
            ((CameraActivity) getActivity()).updateBackButton(R.drawable.ic_arrow_back_dark);
        }
    }

    @OnClick(R.id.clear) public void clearSearch() {
        searchBar.setText("");
//        searchBar.clearFocus();
        ((NearbyPlacesAdapter) recyclerView.getAdapter()).clearList();
    }

    @OnClick(R.id.turn_on_location_btn) public void onTurnOnLocationBtnClicked() {
        mListener.onNearbyPlacesListInteraction(TURN_ON_LOCATION_ACTION);
    }

    @OnEditorAction(R.id.nearby_places_search) public boolean actionFromKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
            } else {
                handler = new Handler();
            }
            performNearbySearch(searchBar.getText());
            ViewUtils.hideKeyboard(getActivity(), searchBar);
            return true;
        }
        return false;
    }

//    @OnFocusChange(R.id.nearby_places_search) public void swapAdapters(boolean isFocused) {
//        if (isFocused) {
//            recyclerView.setAdapter(getNearbyPlacesAdapter(TYPE_AUTOCOMPLETE));
//        }
////        else {
////            nearbyPlacesAdapter = new NearbyPlacesAdapter(nearbyPlaces, getTheContext());
////            recyclerView.getAdapter().notifyDataSetChanged();
////        }
//    }

    @OnTextChanged(R.id.nearby_places_search) public void searchNearby(final CharSequence charSequence) {
        try {
            if (!((NearbyPlacesAdapter) recyclerView.getAdapter()).isAutoCompleteAdapter()) {
                recyclerView.setAdapter(getNearbyPlacesAdapter(TYPE_AUTOCOMPLETE));
            }
            if (charSequence.length() > 0) {
                if (clearBtn.getVisibility() != View.VISIBLE)
                    clearBtn.setVisibility(View.VISIBLE);
    //            recyclerView.swapAdapter(placeAutoCompleteAdapter, false);
    //            if (placeAutoCompleteAdapter != null) {
    //                recyclerView.setAdapter(placeAutoCompleteAdapter);
    //            }
            } else {
                if (clearBtn.getVisibility() == View.VISIBLE)
                    clearBtn.setVisibility(View.GONE);
                recyclerView.setAdapter(getNearbyPlacesAdapter(TYPE_NEARBY_PLACES));
    //            recyclerView.swapAdapter(nearbyPlacesAdapter, true);
            }

            if (charSequence.length() > 2 && googleApiClient.isConnected()) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        performNearbySearch(charSequence);
                    }
                };
                // only canceling the network calls will not help, you need to remove all callbacks as well
                // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                } else {
                    handler = new Handler();
                }
                handler.postDelayed(runnable, 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NearbyPlacesAdapter getNearbyPlacesAdapter (int type) {
        switch (type) {
            case TYPE_NEARBY_PLACES:
                return new NearbyPlacesAdapter(getTheContext(), nearbyPlaces, null, null, null);
            case TYPE_AUTOCOMPLETE:
                return new NearbyPlacesAdapter(getTheContext(), null, googleApiClient, BOUNDS, null);
            default:
                return null;
        }
    }

    private void performNearbySearch(CharSequence charSequence) {
        Log.d("performNearbySearch", "called on " + charSequence);
        if (recyclerView.getAdapter() != null) {
            ((NearbyPlacesAdapter) recyclerView.getAdapter()).getFilter().filter(charSequence);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
//        placeAutoCompleteAdapter.getFilter().filter(charSequence);
//        placeAutoCompleteAdapter.notifyDataSetChanged();
    }

//    /*
//    * Create a get url to fetch results from google place autocomplete api.
//    * Append the input received from autocomplete edittext
//    * Append your current location
//    * Append radius you want to search results within
//    * Choose a language you want to fetch data in
//    * Append your google API Browser key
// */
//    public String getPlaceAutoCompleteUrl(String input) {
//        StringBuilder urlString = new StringBuilder();
//        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json")
//                .append("?input=");
//        try {
//            urlString.append(URLEncoder.encode(input, "utf8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        if (location != null) {
//            urlString.append("&location=")
//                    .append(location.getLatitude())
//                    .append(",").append(location.getLongitude());   // append lat long of current location
//        }
////        urlString.append("&radius=4000&language=en");
//        urlString.append("&radius=4000")
//                .append("&key=")
//                .append(getString(R.string.google_places_api_key));
//
//        return urlString.toString();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNearbyPlacesListInteractionListener) {
            mListener = (OnNearbyPlacesListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNearbyPlacesListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNearbyPlacesListInteractionListener {
        void onNearbyPlacesListInteraction(int action);
    }
}

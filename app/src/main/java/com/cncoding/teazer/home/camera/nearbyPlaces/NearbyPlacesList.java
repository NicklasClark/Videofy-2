package com.cncoding.teazer.home.camera.nearbyPlaces;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NearbyPlacesList extends Fragment {
    private static final String NEARBY_PLACES = "nearbyPlaces";
    public static final int TURN_ON_LOCATION_ACTION = 454;
    public static final int NEARBY_PLACE_AUTOCOMPLETE_ACTION = 453;

    @BindView(R.id.nearby_places_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.location_not_available_layout) LinearLayout locationNotAvailableLayout;
    @BindView(R.id.turn_on_location_btn) ProximaNovaSemiboldButton turnOnLocationBtn;
    @BindView(R.id.nearby_places_app_bar_layout) FrameLayout appBarLayout;
    @BindView(R.id.search_nearby_places) AppCompatImageView searchNearbyPlaces;

    private ArrayList<HashMap<String, String>> nearbyPlaces;
    private OnNearbyPlacesInteractionListener mListener;

    public static NearbyPlacesList newInstance(@Nullable ArrayList<HashMap<String, String>> nearbyPlaces) {
        NearbyPlacesList fragment = new NearbyPlacesList();
        Bundle args = new Bundle();
        args.putSerializable(NEARBY_PLACES, nearbyPlaces);
        fragment.setArguments(args);
        return fragment;
    }

    public NearbyPlacesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //noinspection unchecked
            nearbyPlaces = (ArrayList<HashMap<String, String>>) getArguments().getSerializable(NEARBY_PLACES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_nearby_places_list, container, false);
        ButterKnife.bind(this, rootView);
        if (nearbyPlaces != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new NearbyPlacesAdapter(nearbyPlaces, getActivity()));
        }
        else {
            recyclerView.setVisibility(View.INVISIBLE);
            locationNotAvailableLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
            locationNotAvailableLayout.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @OnClick(R.id.turn_on_location_btn) public void onTurnOnLocationBtnClicked() {
        mListener.onNearbyPlacesInteraction(TURN_ON_LOCATION_ACTION);
    }

    @OnClick(R.id.search_nearby_places) public void launchNearbyPlaceSearch() {
        mListener.onNearbyPlacesInteraction(NEARBY_PLACE_AUTOCOMPLETE_ACTION);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNearbyPlacesInteractionListener) {
            mListener = (OnNearbyPlacesInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNearbyPlacesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNearbyPlacesInteractionListener {
        void onNearbyPlacesInteraction(int action);
    }
}

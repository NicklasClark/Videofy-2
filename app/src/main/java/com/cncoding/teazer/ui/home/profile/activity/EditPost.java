package com.cncoding.teazer.ui.home.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.base.UploadParams;
import com.cncoding.teazer.data.model.camera.SelectedPlace;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment;
import com.cncoding.teazer.ui.home.camera.CameraFragment;
import com.cncoding.teazer.ui.home.camera.adapters.NearbyPlacesAdapter;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.NearbyPlacesFragment;
import com.cncoding.teazer.ui.home.profile.fragment.EditPostFragment;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentEditPostUpdateCtaegories;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import butterknife.ButterKnife;

import static android.R.anim.fade_in;
import static android.R.anim.fade_out;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;

public class EditPost extends AppCompatActivity implements CameraFragment.OnCameraFragmentInteractionListener, EditPostFragment.OnUploadFragmentInteractionListener,
        TagsAndCategoryFragment.TagsAndCategoriesInteractionListener, NearbyPlacesFragment.OnNearbyPlacesListInteractionListener,
        NearbyPlacesAdapter.NearbyPlacesInteractionListener, GoogleApiClient.OnConnectionFailedListener, FragmentEditPostUpdateCtaegories.UpdateCategoriesResponse{
    
    private Context context;
    EditPostFragment editPostFragment;
    private GoogleApiClient googleApiClient;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        ButterKnife.bind(this);
        context = this;
        fragmentManager=getSupportFragmentManager();

        Intent intent=getIntent();
        PostDetails postDetails=intent.getExtras().getParcelable("PostDetail");
        editPostFragment=EditPostFragment.newInstance(postDetails);

        fragmentManager.beginTransaction()
                .replace(R.id.container, editPostFragment, "editPostFragment")
                .commit();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (googleApiClient!= null && !googleApiClient.isConnected())
            googleApiClient.connect();
    }

    @Override
    public void onUploadInteraction(boolean isBackToCamera, Fragment fragment, String tag) {
        if (!isBackToCamera) {
            if (fragment != null && tag != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(fade_in, fade_out, fade_in, fade_out)
                        .replace(R.id.helper_uploading_container, fragment, tag)
                        .addToBackStack(tag)
                        .commit();
            }
            else
                {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            onBackPressed();

        }
    }
    @Override
    public void updateCategories(PostDetails postDetails) {

        FragmentEditPostUpdateCtaegories updateCtaegories=FragmentEditPostUpdateCtaegories.newInstance(postDetails);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.addToBackStack("updateCtaegories");
        ft.replace(R.id.container, updateCtaegories);
        ft.commit();

    }

    @Override
    public void onNearbyPlacesListInteraction(int action) {
        editPostFragment.onNearbyPlacesListInteraction(action);

    }


    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onTagsAndCategoriesInteraction(String action, String resultToShow, String resultToSend, SparseBooleanArray selectedTagsArray, int count) {
        editPostFragment.onTagsAndCategoriesInteraction(action, resultToShow, resultToSend);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onNearbyPlacesAdapterInteraction(SelectedPlace selectedPlace) {
        hideKeyboard(this, findViewById(R.id.container));
        editPostFragment.onNearbyPlacesAdapterInteraction(selectedPlace);
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onPlaceClick(CharSequence placeId) {
        hideKeyboard(this, findViewById(R.id.container));
        try {
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(googleApiClient, String.valueOf(placeId));
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if(places.getCount()==1){
                        //Do the things here on Click.....
                        editPostFragment.onNearbyPlacesAdapterInteraction(new SelectedPlace(
                                places.get(0).getName().toString(),
                                places.get(0).getLatLng().latitude,
                                places.get(0).getLatLng().longitude
                        ));
                        getSupportFragmentManager().popBackStack();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e){
            Log.e("onPlaceClick()", e.getMessage());
        }
    }

    @Override
    public void onCurrentLocationClick() {
        hideKeyboard(this, findViewById(R.id.container));
    }

    @Override
    public void onCameraInteraction(int action, UploadParams uploadParams) {
    }

    @Override
    public void updatedCategoriesResponse(final String categ,final String categName) {

        fragmentManager.popBackStack();
        editPostFragment.getCategoriesResult(categ,categName);


    }
}
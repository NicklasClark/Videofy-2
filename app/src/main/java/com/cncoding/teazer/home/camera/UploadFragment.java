package com.cncoding.teazer.home.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextInputEditText;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldTextView;
import com.cncoding.teazer.home.camera.nearbyPlaces.DataParser;
import com.cncoding.teazer.home.camera.nearbyPlaces.DownloadUrl;
import com.cncoding.teazer.home.camera.nearbyPlaces.SelectedPlace;
import com.cncoding.teazer.utilities.Pojos;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTouch;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList.NEARBY_PLACE_AUTOCOMPLETE_ACTION;
import static com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList.TURN_ON_LOCATION_ACTION;
import static com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.ACTION_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.ACTION_TAGS_FRAGMENT;
import static com.cncoding.teazer.utilities.ViewUtils.IS_GALLERY;
import static com.cncoding.teazer.utilities.ViewUtils.IS_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.POST_DETAILS;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.performUpload;
import static com.cncoding.teazer.utilities.ViewUtils.playVideo;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class UploadFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    public static final String VIDEO_PATH = "videoPath";
    public static final String TAG_NEARBY_PLACES = "nearbyPlaces";
    public static final String TAG_NULL_NEARBY_PLACES = "nullNearbyPlaces";
    public static final String TAG_CATEGORIES_FRAGMENT = "interestsFragment";
    public static final String TAG_TAGS_FRAGMENT = "tagsFragment";
    //    private static final int REQUEST_PLACE_PICKER = 212;
    private static final int REQUEST_LOCATION_PERMISSIONS = 211;
//    private static final int REQUEST_CODE_PLACE_AUTOCOMPLETE = 210;
    private static final int REQUEST_CODE_CHECK_SETTINGS = 312;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "locationUpdates";
    private static final String KEY_LOCATION = "location";
    private static final int RC_LOCATION_PERM = 123;

    @BindView(R.id.share_on_facebook) ProximaNovaRegularCheckedTextView facebookShareBtn;
    @BindView(R.id.share_on_twitter) ProximaNovaRegularCheckedTextView twitterShareBtn;
    @BindView(R.id.video_preview_thumbnail_container) RelativeLayout thumbnailViewContainer;
    @BindView(R.id.video_preview_thumbnail) ImageView thumbnailView;
    @BindView(R.id.video_duration) ProximaNovaRegularTextView videoDurationTextView;
    @BindView(R.id.thumbnail_progress_bar) ProgressBar thumbnailProgressBar;
    @BindView(R.id.progress_bar) ProgressBar topProgressBar;
    @BindView(R.id.video_upload_check_btn) Button uploadBtn;
    @BindView(R.id.video_upload_title) ProximaNovaRegularTextInputEditText videoTitle;
    @BindView(R.id.video_upload_location) ProximaNovaRegularTextInputEditText addLocationBtn;
    @BindView(R.id.video_upload_tag_friends) ProximaNovaRegularTextInputEditText tagFriendsBtn;
    @BindView(R.id.tag_friends_badge) ProximaNovaSemiboldTextView tagFriendsBadge;
    @BindView(R.id.video_upload_categories) ProximaNovaRegularTextInputEditText uploadCategoriesBtn;
    @BindView(R.id.categories_badge) ProximaNovaSemiboldTextView uploadCategoriesBadge;

    public static boolean checkFacebookButtonPressed = false;
    public String videoPath;
    public boolean isReaction;
    private int tagCount;
    private int categoryCount;
    String selectedCategoriesToSend = null;
    String selectedTagsToSend = null;
    String selectedCategoriesToShow = null;
    String selectedTagsToShow = null;
    private boolean isRequestingLocationUpdates;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private Pojos.Post.PostDetails postDetails;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private SelectedPlace selectedPlace;
    private Context context;
    private Activity activity;

    private OnUploadFragmentInteractionListener mListener;
    private boolean isGallery;

    public UploadFragment() {
        // Required empty public constructor
    }

    public static UploadFragment newInstance(String videoPath, boolean isReaction, Pojos.Post.PostDetails postDetails, boolean isGallery) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putString(VIDEO_PATH, videoPath);
        args.putBoolean(IS_REACTION, isReaction);
        args.putBoolean(IS_GALLERY, isGallery);
        args.putParcelable(POST_DETAILS, postDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            videoPath = bundle.getString(VIDEO_PATH);
            isReaction = bundle.getBoolean(IS_REACTION);
            if (isReaction)
                postDetails = getArguments().getParcelable(POST_DETAILS);
            isGallery = bundle.getBoolean(IS_GALLERY);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, isRequestingLocationUpdates);
        super.onSaveInstanceState(outState);
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        // Update the value of mRequestingLocationUpdates from the Bundle.
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                isRequestingLocationUpdates = savedInstanceState.getBoolean(REQUESTING_LOCATION_UPDATES_KEY);
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Inflate the layout for this fragment
        View rootView;
        if (!isReaction)
            rootView = inflater.inflate(R.layout.fragment_upload_post, container, false);
        else
            rootView = inflater.inflate(R.layout.fragment_upload_reaction, container, false);
        ButterKnife.bind(this, rootView);

        topProgressBar.setVisibility(View.GONE);
        context = getContext();
        activity = getActivity();

        setBadge(uploadCategoriesBadge, categoryCount);
        setBadge(tagFriendsBadge, tagCount);

        new GetThumbnail(this).execute();

        new SetVideoDuration(this).execute();

        isRequestingLocationUpdates = false;
        updateValuesFromBundle(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        getLastLocation(false);
        createLocationCallback();
        createLocationRequest();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRequestingLocationUpdates)
            startLocationUpdates();
    }

    private void getLastLocation(final boolean firstTime) {
        if (arePermissionsAllowed(context)) {
            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = location;
                                if (firstTime) {
                                    new GetNearbyPlacesData(UploadFragment.this).execute(getNearbySearchUrl(currentLocation));
                                }
                            }
                        }
                    });
        }
    }

    private void createLocationCallback() {
        if (currentLocation == null) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    currentLocation = locationResult.getLastLocation();
                    stopLocationUpdates();
                }
            };
        }
    }

    void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())
                .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        startLocationUpdates();
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case CommonStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied, but this can be fixed
                                // by showing the user a dialog.
                                try {
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    ResolvableApiException resolvable = (ResolvableApiException) e;
                                    resolvable.startResolutionForResult(activity, REQUEST_CODE_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sendEx) {
                                    // Ignore the error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way
                                // to fix the settings so we won't show the dialog.
                                break;
                        }
                    }
                });
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private static class GetThumbnail extends AsyncTask<Void, Void, Bitmap> {

        WeakReference<UploadFragment> reference;

        GetThumbnail(UploadFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                Bitmap bitmap;
                bitmap = ThumbnailUtils.createVideoThumbnail(reference.get().videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
                return bitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                if (bitmap != null) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(reference.get())
                            .load(stream.toByteArray())
                            .asBitmap()
                            //                        .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable())
                            .animate(R.anim.fast_fade_in)
                            .listener(new RequestListener<byte[], Bitmap>() {
                                @Override
                                public boolean onException(Exception e, byte[] model, Target<Bitmap> target, boolean isFirstResource) {
                                    reference.get().thumbnailProgressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Bitmap resource, byte[] model, Target<Bitmap> target,
                                                               boolean isFromMemoryCache, boolean isFirstResource) {
                                    reference.get().thumbnailProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(reference.get().thumbnailView);
                } else {
                    Glide.with(reference.get())
                            .load(R.drawable.material_flat)
                            .crossFade()
                            .listener(new RequestListener<Integer, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    reference.get().thumbnailProgressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target,
                                                               boolean isFromMemoryCache, boolean isFirstResource) {
                                    reference.get().thumbnailProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(reference.get().thumbnailView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    void launchPlacePicker() {
//        try {
//            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(activity);
//            startActivityForResult(intent, REQUEST_CODE_PLACE_AUTOCOMPLETE);
//        } catch (GooglePlayServicesRepairableException e) {
//            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.getConnectionStatusCode(), REQUEST_CODE_PLACE_AUTOCOMPLETE);
//        } catch (GooglePlayServicesNotAvailableException e) {
//            Snackbar.make(addLocationBtn, "Google Play Services is not available.", Snackbar.LENGTH_LONG).show();
//        }
////        try {
////            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
////            Intent intent = intentBuilder.build(this);
////            // Start the Intent by requesting a result, identified by a request code.
////            startActivityForResult(intent, REQUEST_PLACE_PICKER);
////        } catch (GooglePlayServicesRepairableException e) {
////            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), REQUEST_PLACE_PICKER);
////        } catch (GooglePlayServicesNotAvailableException e) {
////            Snackbar.make(addLocationBtn, "Google Play Services is not available.", Snackbar.LENGTH_LONG).show();
////        }
//    }

    private String getNearbySearchUrl(Location location) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(location.getLatitude()).append(",").append(location.getLongitude());
        googlePlacesUrl.append("&radius=" + 2000);
        googlePlacesUrl.append("&type=")
                .append("airport|amusement_park|aquarium|art_gallery|bakery|bar|beauty_salon" +
                        "|book_store|bowling_alley|cafe|casino|clothing_store|gym" +
                        "|hair_care|jewelry_store|library|night_club|park|shopping_mall|stadium" +
                        "|spa|subway_station|university|zoo");

        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=").append(getString(R.string.google_places_api_key));
        Log.d("getNearbySearchUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @OnEditorAction(R.id.video_upload_title) public boolean titleDone(TextView view, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard(activity, view);
            return true;
        }
        return false;
    }

    @OnClick(R.id.share_on_facebook) public void shareOnFacebookAction() {
        checkAction("facebook", facebookShareBtn);
        if (!checkFacebookButtonPressed) {
            checkFacebookButtonPressed = true;
            facebookShareBtn.setChecked(true);
            facebookShareBtn.setBackgroundTintList(null);
        } else {
            checkFacebookButtonPressed = false;
            facebookShareBtn.setChecked(false);
            facebookShareBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
        }
    }

    @OnClick(R.id.share_on_twitter) public void shareOnTwitterAction() {
        checkAction("twitter", twitterShareBtn);
    }

    @OnClick(R.id.video_preview_thumbnail) public void playVideoPreview() {
        hideKeyboard(activity, videoTitle);
        playVideo(context, videoPath, false);
    }

    @OnTouch(R.id.video_upload_location) public boolean addLocation(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            hideKeyboard(activity, view);
            if (arePermissionsAllowed(context)) {
                if (currentLocation != null)
                    new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
            } else requestPermissions();
            return true;
        }
        return false;
    }

    @OnTouch(R.id.video_upload_tag_friends) public boolean getMyFollowings(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            hideKeyboard(activity, view);
            mListener.onUploadInteraction(TAG_TAGS_FRAGMENT, null, selectedTagsToShow);
            tagFriendsBtn.clearFocus();
            return true;
        }
        return false;
    }

    @OnTouch(R.id.video_upload_categories) public boolean getCategories(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            hideKeyboard(activity, view);
            mListener.onUploadInteraction(TAG_CATEGORIES_FRAGMENT, null, selectedCategoriesToShow);
            uploadCategoriesBtn.clearFocus();
            return true;
        }
        return false;
    }

    @OnClick(R.id.video_upload_check_btn) public void onUploadBtnClick() {
        if (validateFields()) {
            toggleInteraction(false);
            String title = videoTitle.getText().toString().equals("") ? null : videoTitle.getText().toString();
            String location = null;
            double latitude = 0;
            double longitude = 0;
            if (selectedPlace != null) {
                location = selectedPlace.getPlaceName().equals("") ? null : selectedPlace.getPlaceName();
                latitude = selectedPlace.getLatitude();
                longitude = selectedPlace.getLongitude();
            }
            String tags = tagFriendsBtn.getText().toString().equals("") ? null : tagFriendsBtn.getText().toString();
            DecimalFormat df = new DecimalFormat("#.#######");
            performUpload(activity, new Pojos.UploadParams(isGallery, videoPath, isReaction, title, location,
                    Double.parseDouble(df.format(latitude)), Double.parseDouble(df.format(longitude)),
                    tags, selectedCategoriesToSend, postDetails));
        }
    }

    private void checkAction(String type, ProximaNovaRegularCheckedTextView view) {
        if (!view.isChecked()) {
            view.setChecked(true);
            view.setCompoundDrawablesWithIntrinsicBounds(type.equals("facebook") ? R.drawable.ic_facebook
                            : R.drawable.ic_twitter,
                    0, R.drawable.ic_tick_circle_large, 0);
        } else {
            view.setChecked(false);
            view.setCompoundDrawablesWithIntrinsicBounds(type.equals("facebook") ? R.drawable.ic_facebook :
                            R.drawable.ic_twitter,
                    0, R.drawable.ic_unchecked, 0);
        }
    }

    private boolean validateFields() {
        if (!isReaction && TextUtils.isEmpty(videoTitle.getText())) {
            videoTitle.setError(getString(R.string.required));
            videoTitle.requestFocus();
            return false;
        } else if (!isReaction && (selectedCategoriesToSend == null || selectedCategoriesToSend.length() == 0)) {
            Toast.makeText(context, getString(R.string.required_categories), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @AfterPermissionGranted(RC_LOCATION_PERM) private void startLocationService() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(context, perms)) {
            new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_LOCATION_PERM, perms);
        }
    }

    public void onNearbyPlacesListInteraction(int action) {
        switch (action) {
            case TURN_ON_LOCATION_ACTION:
                createLocationRequest();
                break;
            case NEARBY_PLACE_AUTOCOMPLETE_ACTION:
//                launchPlacePicker();
                break;
            default:
                break;
        }
    }

    public void onNearbyPlacesAdapterInteraction(SelectedPlace selectedPlace) {
        this.selectedPlace = selectedPlace;
        addLocationBtn.setText(selectedPlace.getPlaceName());
    }

    public void onTagsAndCategoriesInteraction(String action, String resultToShow, String resultToSend, int count) {
        switch (action) {
            case ACTION_TAGS_FRAGMENT:
                tagCount = count;
                selectedTagsToShow = resultToShow;
                selectedTagsToSend = resultToSend;
                tagFriendsBtn.setText(resultToShow);
                setBadge(tagFriendsBadge, count);
                break;
            case ACTION_CATEGORIES_FRAGMENT:
                categoryCount = count;
                selectedCategoriesToShow = resultToShow;
                selectedCategoriesToSend = resultToSend;
                uploadCategoriesBtn.setText(resultToShow);
                setBadge(uploadCategoriesBadge, count);
                break;
        }
    }

    private void setBadge(ProximaNovaSemiboldTextView view, int count) {
        view.setVisibility(count == 0 ? View.GONE : VISIBLE);
        if (view.getVisibility() == VISIBLE) {
            String countText = String.valueOf(count);
            if (count <= 9)
                countText = "0" + countText;
            view.setText(countText);
        }
    }

    public void toggleInteraction(boolean isEnabled) {
        if (isEnabled)
            topProgressBar.setVisibility(INVISIBLE);
        else
            topProgressBar.setVisibility(VISIBLE);
        if (isEnabled) {
            enableView(videoTitle);
            enableView(addLocationBtn);
            enableView(tagFriendsBtn);
            enableView(uploadCategoriesBtn);
            enableView(uploadBtn);
        } else {
            disableView(videoTitle, true);
            disableView(addLocationBtn, true);
            disableView(tagFriendsBtn, true);
            disableView(uploadCategoriesBtn, true);
            disableView(uploadBtn, false);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("HomeFragment", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
//        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
////            new AppSettingsDialog.Builder(this).build().show();
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        // All required changes were successfully made
                        isRequestingLocationUpdates = true;
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Snackbar.make(uploadBtn, "Location services are required to get your nearby locations.", Snackbar.LENGTH_SHORT)
                                .show();
                        isRequestingLocationUpdates = false;
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private static class GetNearbyPlacesData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

        private String googlePlacesJsonData;
        private WeakReference<UploadFragment> reference;

        GetNearbyPlacesData(UploadFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            reference.get().toggleInteraction(false);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... strings) {
            try {
                String url = strings[0];
                DownloadUrl downloadUrl = new DownloadUrl();
                googlePlacesJsonData = downloadUrl.readUrl(url);
            } catch (Exception e) {
                Log.e("GooglePlacesReadTask", e.toString());
            }
            return DataParser.parse(googlePlacesJsonData);
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> googlePlaces) {
            reference.get().toggleInteraction(true);
            reference.get().mListener.onUploadInteraction(TAG_NEARBY_PLACES, googlePlaces, null);
        }
    }

    private static class SetVideoDuration extends AsyncTask<Void, Void, String> {

        private WeakReference<UploadFragment> reference;

        SetVideoDuration(UploadFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... voids) {
            File file = new File(reference.get().videoPath);
            if (file.exists()) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(reference.get().videoPath);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long duration;
                try {
                    duration = Long.parseLong(time);
                    retriever.release();
                    return "Duration " + String.format(Locale.UK, "%02d:%02d",
                            MILLISECONDS.toMinutes(duration),
                            MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
                } catch (NumberFormatException e) {
                    Log.e("ErrorParsingDuration", e.getMessage() != null ? e.getMessage() : "FAILED!");
                    return "";
                }
            } else
                return "";
        }

        @Override
        protected void onPostExecute(String videoDuration) {
            reference.get().videoDurationTextView.setText(videoDuration);
            super.onPostExecute(videoDuration);
        }
    }

    /**
     * PERMISSIONS AND SHIT
     */
    private boolean arePermissionsAllowed(Context applicationContext) {
        int fineLocationResult = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION);
        int coarseLocationResult = ContextCompat.checkSelfPermission(applicationContext, ACCESS_COARSE_LOCATION);
        int internetResult = ContextCompat.checkSelfPermission(applicationContext, INTERNET);

        return fineLocationResult == PackageManager.PERMISSION_GRANTED &&
                coarseLocationResult == PackageManager.PERMISSION_GRANTED &&
                internetResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, INTERNET}, REQUEST_LOCATION_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean fineLocationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coarseLocationAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean internetAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (fineLocationAccepted && coarseLocationAccepted && internetAccepted) {
//                        permissions are granted
//                        launchPlacePicker();
                        getLastLocation(true);
//                        if (currentLocation != null)
//                            new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
                        if (isRequestingLocationUpdates) {
                            startLocationUpdates();
                        }
                    } else {
//                        permissions are denied, show empty list
                        showEmptyList();
                    }
                } else {
//                        permissions are denied, show empty list
                    showEmptyList();
                }
                break;
            default:
                break;
        }
    }

    private void showEmptyList() {
        mListener.onUploadInteraction(TAG_NULL_NEARBY_PLACES, null, null);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        isRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUploadFragmentInteractionListener) {
            mListener = (OnUploadFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUploadFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnUploadFragmentInteractionListener {
        void onUploadInteraction(String tag, ArrayList<HashMap<String, String>> googlePlaces, String selectedData);
    }

}
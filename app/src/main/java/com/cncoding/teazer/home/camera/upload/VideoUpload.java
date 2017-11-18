package com.cncoding.teazer.home.camera.upload;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.camera.nearbyPlaces.DataParser;
import com.cncoding.teazer.home.camera.nearbyPlaces.DownloadUrl;
import com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesAdapter;
import com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList;
import com.cncoding.teazer.home.camera.nearbyPlaces.SelectedPlace;
import com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment;
import com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.TagsAndCategoriesInteractionListener;
import com.cncoding.teazer.utilities.PlaceHolderDrawableHelper;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Category;
import com.cncoding.teazer.utilities.Pojos.Friends.CircleList;
import com.cncoding.teazer.utilities.Pojos.MiniProfile;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.apiCalls.ApiCallingService.Friends.isResponseOk;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_FALSE;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_TRUE;
import static com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList.NEARBY_PLACE_AUTOCOMPLETE_ACTION;
import static com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList.TURN_ON_LOCATION_ACTION;
import static com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.ACTION_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.ACTION_TAGS_FRAGMENT;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.launchVideoUploadCamera;
import static com.cncoding.teazer.utilities.ViewUtils.performUpload;
import static com.cncoding.teazer.utilities.ViewUtils.playVideo;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class VideoUpload extends AppCompatActivity
        implements NearbyPlacesList.OnNearbyPlacesInteractionListener,
        NearbyPlacesAdapter.NearbyPlacesInteractionListener,
        TagsAndCategoriesInteractionListener {

    public static final String VIDEO_PATH = "videoPath";
    private static final String TAG_VIDEO_PREVIEW = "videoPreview";
    private static final String TAG_NEARBY_PLACES = "nearbyPlaces";
    private static final String TAG_INTERESTS_FRAGMENT = "interestsFragment";
    private static final String TAG_TAGS_FRAGMENT = "tagsFragment";
//    private static final int REQUEST_PLACE_PICKER = 212;
    private static final int REQUEST_LOCATION_PERMISSIONS = 211;
    private static final int REQUEST_CODE_PLACE_AUTOCOMPLETE = 210;
    private static final int REQUEST_CODE_CHECK_SETTINGS = 312;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "locationUpdates";
    private static final String KEY_LOCATION = "location";

    @BindView(R.id.video_preview_thumbnail_container) RelativeLayout thumbnailViewContainer;
    @BindView(R.id.video_preview_thumbnail) ImageView thumbnailView;
    @BindView(R.id.fragment_container) FrameLayout fragmentContainer;
    @BindView(R.id.video_duration) ProximaNovaRegularTextView videoDurationTextView;
    @BindView(R.id.progress_bar) ProgressBar thumbnailProgressBar;
    @BindView(R.id.video_upload_cancel_btn) Button cancelBtn;
    @BindView(R.id.video_upload_check_btn) Button uploadBtn;
    @BindView(R.id.video_upload_title) ProximaNovaRegularAutoCompleteTextView videoTitle;
    @BindView(R.id.video_upload_location) ProximaNovaBoldButton addLocationBtn;
    @BindView(R.id.video_upload_location_text) ProximaNovaRegularTextView addLocationText;
    @BindView(R.id.video_upload_tag_friends) ProximaNovaBoldButton tagFriendsBtn;
    @BindView(R.id.video_upload_tag_friends_text) ProximaNovaRegularTextView tagFriendsText;
    @BindView(R.id.video_upload_categories) ProximaNovaBoldButton uploadCategoriesBtn;
    @BindView(R.id.video_upload_categories_text) ProximaNovaRegularTextView uploadCategoriesText;
    @BindView(R.id.up_btn) AppCompatImageView upBtn;

    private String videoPath;
    private String selectedCategoriesToSend;
    private boolean isRequestingLocationUpdates;
    private ArrayList<MiniProfile> myFollowingsList = new ArrayList<>();

    private FragmentManager fragmentManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
//    private Animator animator;
    private SelectedPlace selectedPlace;
//    private SelectedPlace selectedPlace;

    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            videoPath = bundle.getString(VIDEO_PATH);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, isRequestingLocationUpdates);
        outState.putParcelable(KEY_LOCATION, currentLocation);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_upload);
        getBundleExtras();
        ButterKnife.bind(this);

        new SetVideoDuration(this).execute();

        fragmentManager = getSupportFragmentManager();

        isRequestingLocationUpdates = false;
        updateValuesFromBundle(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        createLocationCallback();
        createLocationRequest();

//        Point point = new Point();
//        getWindowManager().getDefaultDisplay().getSize(point);
//        thumbnailViewContainer.setLayoutParams(new LinearLayout.LayoutParams(
//                point.x,
//                (point.x - (point.x / 3))));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        thumbnailProgressBar.setVisibility(VISIBLE);
        Glide.with(this)
                .load(getThumbnail(videoPath).toByteArray())
                .asBitmap()
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable())
                .animate(R.anim.fast_fade_in)
                .listener(new RequestListener<byte[], Bitmap>() {
                    @Override
                    public boolean onException(Exception e, byte[] model, Target<Bitmap> target, boolean isFirstResource) {
                        thumbnailProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, byte[] model, Target<Bitmap> target,
                                                   boolean isFromMemoryCache, boolean isFirstResource) {
                        thumbnailProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(thumbnailView);
    }

    private void getLastLocation() {
        if (arePermissionsAllowed(this)) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                currentLocation = location;
                                Toast.makeText(VideoUpload.this,
                                        "FusedLocationProvider: "
                                                + currentLocation.getLatitude() + " : " + currentLocation.getLongitude(),
                                        Toast.LENGTH_SHORT)
                                        .show();
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
                    Toast.makeText(VideoUpload.this,
                            "LocationCallback\n" + currentLocation.getLatitude() + " : " + currentLocation.getLongitude(),
                            Toast.LENGTH_SHORT).show();
                    stopLocationUpdates();
                }
            };
        }
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(30000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        startLocationUpdates();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
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
                                    resolvable.startResolutionForResult(VideoUpload.this, REQUEST_CODE_CHECK_SETTINGS);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private static class SetVideoDuration extends AsyncTask<Void, Void, String> {

        private WeakReference<VideoUpload> reference;

        SetVideoDuration(VideoUpload context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... voids) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(reference.get().videoPath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(time );
            retriever.release();
            return String.format(Locale.UK, "%02d:%02d",
                    MILLISECONDS.toMinutes(duration),
                    MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
        }

        @Override
        protected void onPostExecute(String videoDuration) {
            reference.get().videoDurationTextView.setText(videoDuration);
            super.onPostExecute(videoDuration);
        }
    }

    private ByteArrayOutputStream getThumbnail(String videoPath) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        if (thumbnail.getHeight() > thumbnail.getWidth()) {
            thumbnail = ThumbnailUtils.extractThumbnail(thumbnail,
                    thumbnail.getWidth(),
                    (thumbnail.getWidth() - (thumbnail.getWidth() / 3)));
        }
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream;
    }

//    private void dynamicToolbarColor(Bitmap bitmap) {
//        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                Palette.Swatch vibrant;
//                int bgColor = Color.parseColor("#26C6DA");
//                vibrant = palette.getVibrantSwatch();
//                if (vibrant != null) {
//                    bgColor = vibrant.getRgb();
//                } else {
//                    vibrant = palette.getDominantSwatch();
//                    if (vibrant != null) {
//                        bgColor = vibrant.getRgb();
//                    }
//                }
//                thumbnailViewContainer.setBackgroundColor(bgColor);
//            }
//        });
//    }

    private void launchPlacePicker() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            startActivityForResult(intent, REQUEST_CODE_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), REQUEST_CODE_PLACE_AUTOCOMPLETE);
        } catch (GooglePlayServicesNotAvailableException e) {
            Snackbar.make(addLocationBtn, "Google Play Services is not available.", Snackbar.LENGTH_LONG).show();
        }
//        try {
//            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
//            Intent intent = intentBuilder.build(this);
//            // Start the Intent by requesting a result, identified by a request code.
//            startActivityForResult(intent, REQUEST_PLACE_PICKER);
//        } catch (GooglePlayServicesRepairableException e) {
//            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(), REQUEST_PLACE_PICKER);
//        } catch (GooglePlayServicesNotAvailableException e) {
//            Snackbar.make(addLocationBtn, "Google Play Services is not available.", Snackbar.LENGTH_LONG).show();
//        }
    }

    private static class GetNearbyPlacesData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

        private String googlePlacesJsonData;
        private WeakReference<VideoUpload> reference;

        GetNearbyPlacesData(VideoUpload context) {
            reference = new WeakReference<>(context);
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
            reference.get().toggleUpBtnVisibility(VISIBLE);
            reference.get().fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fast_fade_in, R.anim.fast_fade_out, R.anim.fast_fade_in, R.anim.fast_fade_out)
                    .add(R.id.fragment_container, NearbyPlacesList.newInstance(googlePlaces), TAG_NEARBY_PLACES)
                    .addToBackStack(TAG_NEARBY_PLACES)
                    .commit();
        }
    }

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

//    private String getAutoCompleteUrl(String input, Location location) {
//        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
////        googlePlacesUrl.append("&types=").append(nearbyPlaceTypes);
//        googlePlacesUrl.append("input=").append(input);
//        googlePlacesUrl.append("&location=").append(location.getLatitude()).append(",").append(location.getLongitude());
//        googlePlacesUrl.append("&radius=" + 20000);
//        googlePlacesUrl.append("&key=").append(getString(R.string.google_places_api_key));
//        Log.d("getAutoCompleteUrl", googlePlacesUrl.toString());
//        return (googlePlacesUrl.toString());
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PLACE_AUTOCOMPLETE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    onNearbyPlacesInteraction(
                            new SelectedPlace(
                                    place.getName().toString(),
                                    place.getLatLng().latitude,
                                    place.getLatLng().longitude));
                }
                else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    String message = status.getStatusMessage();
                    if (message != null) {
                        Snackbar.make(addLocationBtn, message, Snackbar.LENGTH_SHORT).show();
                    } else
                        Snackbar.make(addLocationBtn, "Oops! Something went wrong, please retry.", Snackbar.LENGTH_SHORT).show();

                }
                break;
            case REQUEST_CODE_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
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
//            case REQUEST_PLACE_PICKER:
////            // This result is from the PlacePicker dialog.
//                if (resultCode == Activity.RESULT_OK) {
//                /* User has picked a place, extract data.
//                   Data is extracted from the returned intent by retrieving a Place object from
//                   the PlacePicker.
//                 */
//                    final Place place = PlacePicker.getPlace(this, data);
//
//                /* A Place object contains details about that place, such as its name, address
//                and phone number. Extract the name, address, phone number, place ID and place types.
//                 */
//                    final CharSequence name = place.getName();
//                    final CharSequence address = place.getAddress();
//                    final CharSequence phone = place.getPhoneNumber();
//                    final String placeId = place.getUserId();
//                    String attribution = place.getAttributions().toString();
//
//                    String toastMsg = String.format("Place: %s", name);
//                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//                }
//                break;
            default:
                break;
        }
    }

    @OnEditorAction(R.id.video_upload_title) public boolean titleDone(TextView view, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard(this, view);
            return true;
        }
        return false;
    }

    @OnClick(R.id.video_preview_thumbnail) public void playVideoPreview() {
        playVideo(this, videoPath, false);
    }

    @OnClick(R.id.video_upload_location) public void addLocation() {
        if (arePermissionsAllowed(this)) {
            if (currentLocation != null)
                new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
        } else requestPermissions();
    }

    @OnClick(R.id.video_upload_categories) public void getCategories() {
        ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        toggleUpBtnVisibility(VISIBLE);
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.fast_fade_in, R.anim.fast_fade_out, R.anim.fast_fade_in, R.anim.fast_fade_out)
                                .add(R.id.fragment_container,
                                        TagsAndCategoryFragment.newInstance(ACTION_CATEGORIES_FRAGMENT, response.body()),
//                                Interests.newInstance(Interests.LAUNCH_TYPE_UPLOAD),
                                        TAG_INTERESTS_FRAGMENT)
                                .addToBackStack(TAG_INTERESTS_FRAGMENT)
                                .commit();
                    } else
                        Snackbar.make(uploadCategoriesBtn, "There was an error fetching categories, please try again.",
                                Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(uploadCategoriesBtn, response.code() + " : " + response.message(),
                            Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.e("getCategories", t.getMessage());
            }
        });
    }

    @OnClick(R.id.video_upload_tag_friends) public void getMyFollowings() {
        getMyFollowingsList(1);
    }

    private void getMyFollowingsList(final int page) {
        ApiCallingService.Friends.getMyFollowings(page, this).enqueue(new Callback<CircleList>() {
            @Override
            public void onResponse(Call<CircleList> call, Response<CircleList> response) {
                if (response.body().getCircles() != null) {
                    switch (isResponseOk(response)) {
                        case SUCCESS_OK_TRUE:
                            myFollowingsList.addAll(response.body().getCircles());
                            getMyFollowingsList(page + 1);
                            break;
                        case SUCCESS_OK_FALSE:
                            myFollowingsList.addAll(response.body().getCircles());
                            toggleUpBtnVisibility(VISIBLE);
                            fragmentManager.beginTransaction()
                                    .setCustomAnimations(R.anim.fast_fade_in, R.anim.fast_fade_out,
                                            R.anim.fast_fade_in, R.anim.fast_fade_out)
                                    .replace(R.id.fragment_container,
                                            TagsAndCategoryFragment.newInstance(ACTION_TAGS_FRAGMENT, myFollowingsList), TAG_TAGS_FRAGMENT)
                                    .addToBackStack(TAG_TAGS_FRAGMENT)
                                    .commit();
                            break;
                        default:
                            Log.e("getMyFollowingsList", response.message());
                            break;
                    }
                } else Toast.makeText(VideoUpload.this, "Sorry, no friends found!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CircleList> call, Throwable t) {
                Log.e("getMyFollowingsList", t.getMessage());
            }
        });
    }

    @OnClick(R.id.video_upload_check_btn) public void onUploadBtnClick() {
        if (selectedPlace != null) {
            String title = videoTitle.getText().toString().equals("")? null : videoTitle.getText().toString();
            String location = selectedPlace.getPlaceName().equals("")? null : selectedPlace.getPlaceName();
            String taggedFriends = tagFriendsText.getText().toString().equals("")? null : tagFriendsText.getText().toString();
            String categories = selectedCategoriesToSend.equals("")? null : selectedCategoriesToSend;
            String tags = tagFriendsText.getText().toString().equals("")? null : tagFriendsText.getText().toString();
            DecimalFormat df = new DecimalFormat("#.#######");
            if (location != null) {
                performUpload(this, new Pojos.UploadParams(videoPath, false, title, location,
                        Double.parseDouble(df.format(selectedPlace.getLatitude())),
                        Double.parseDouble(df.format(selectedPlace.getLongitude())), tags, selectedCategoriesToSend));
            } else {
                Toast.makeText(this, "Location is required for now", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Location is required for now", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.video_upload_cancel_btn) public void retakeVideo() {
        launchVideoUploadCamera(this);
        finish();
    }

    @OnClick(R.id.up_btn) public void goBack() {
        onBackPressed();
    }

    private void toggleUpBtnVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                if (upBtn.getVisibility() != VISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));
                    upBtn.setVisibility(VISIBLE);
                }
                break;
            case INVISIBLE:
                if (upBtn.getVisibility() != INVISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_out));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            upBtn.setVisibility(INVISIBLE);
                        }
                    }, 400);
                }
                break;
            default:
                break;
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
        ActivityCompat.requestPermissions(this,
                new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, INTERNET},
                REQUEST_LOCATION_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
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
                        if (currentLocation != null)
                            new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
                        if (isRequestingLocationUpdates) {
                            startLocationUpdates();
                        }
                    }
                    else {
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
        toggleUpBtnVisibility(VISIBLE);
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fast_fade_in, R.anim.fast_fade_out, R.anim.fast_fade_in, R.anim.fast_fade_out)
                .add(R.id.fragment_container, NearbyPlacesList.newInstance(null), TAG_NEARBY_PLACES)
                .addToBackStack(TAG_NEARBY_PLACES)
                .commit();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        isRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequestingLocationUpdates)
            startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onNearbyPlacesInteraction(int action) {
        switch (action) {
            case TURN_ON_LOCATION_ACTION:
                createLocationRequest();
                break;
            case NEARBY_PLACE_AUTOCOMPLETE_ACTION:
                launchPlacePicker();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNearbyPlacesInteraction(SelectedPlace selectedPlace) {
        this.selectedPlace = selectedPlace;
        addLocationText.setText(selectedPlace.getPlaceName());
        onBackPressed();
    }

    @Override
    public void onTagsAndCategoriesInteraction(String action, String resultToShow, String resultToSend) {
        switch (action){
            case ACTION_TAGS_FRAGMENT:
                tagFriendsText.setText(resultToShow);
                break;
            case ACTION_CATEGORIES_FRAGMENT:
                selectedCategoriesToSend = resultToSend;
                uploadCategoriesText.setText(resultToShow);
                break;
        }
        fragmentManager.popBackStack();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        videoViewPreview.stopPlayback();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            if (fragmentManager.getBackStackEntryCount() == 0) {
                toggleUpBtnVisibility(View.INVISIBLE);
            }
        }
        else {
            startActivity(new Intent(this, BaseBottomBarActivity.class));
            finish();
        }
    }
}

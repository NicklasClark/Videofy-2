package com.cncoding.teazer.camera.upload;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.camera.nearbyPlaces.DataParser;
import com.cncoding.teazer.camera.nearbyPlaces.DownloadUrl;
import com.cncoding.teazer.camera.nearbyPlaces.NearbyPlacesAdapter;
import com.cncoding.teazer.camera.nearbyPlaces.NearbyPlacesList;
import com.cncoding.teazer.camera.nearbyPlaces.SelectedPlace;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.home.Interests;
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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static com.cncoding.teazer.apiCalls.ApiCallingService.Friends.isResponseOk;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_FALSE;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_TRUE;
import static com.cncoding.teazer.camera.nearbyPlaces.NearbyPlacesList.NEARBY_PLACE_AUTOCOMPLETE_ACTION;
import static com.cncoding.teazer.camera.nearbyPlaces.NearbyPlacesList.TURN_ON_LOCATION_ACTION;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class VideoUpload extends AppCompatActivity
        implements ProgressRequestBody.UploadCallbacks,
        NearbyPlacesList.OnNearbyPlacesInteractionListener,
        NearbyPlacesAdapter.NearbyPlacesInteractionListener,
        TagsAndCategoryFragment.TagsAndCategoriesInteractionListener, Interests.OnInterestsInteractionListener{

    public static final String VIDEO_PATH = "videoPath";
    private static final String TAG_VIDEO_PREVIEW = "videoPreview";
    private static final String TAG_NEARBY_PLACES = "nearbyPlaces";
    private static final String TAG_INTERESTS_FRAGMENT = "interestsFragment";
//    private static final int REQUEST_PLACE_PICKER = 212;
    private static final int REQUEST_LOCATION_PERMISSIONS = 211;
    private static final int REQUEST_CODE_PLACE_AUTOCOMPLETE = 210;
    private static final int REQUEST_CODE_CHECK_SETTINGS = 312;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "locationUpdates";
    private static final String KEY_LOCATION = "location";

    //    @BindView(R.id.video_view_preview) VideoView videoViewPreview;
//    @BindView(R.id.video_upload_coordinator_layout) CoordinatorLayout rootLayout;
//    @BindView(R.id.app_bar) AppBarLayout appBarLayout;
//    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.video_preview_thumbnail_container) RelativeLayout thumbnailViewContainer;
    @BindView(R.id.video_preview_thumbnail) ImageView thumbnailView;
    @BindView(R.id.video_upload_fragment_container) FrameLayout fragmentContainer;
    @BindView(R.id.video_duration) ProximaNovaRegularTextView videoDurationTextView;
    @BindView(R.id.video_progress_bar) ProgressBar progressBar;
    @BindView(R.id.video_upload_cancel_btn) Button cancelBtn;
    @BindView(R.id.video_upload_check_btn) Button uploadBtn;
    @BindView(R.id.uploading_notification) ProximaNovaRegularTextView uploadingNotificationTextView;
    @BindView(R.id.video_upload_title) ProximaNovaRegularAutoCompleteTextView videoTitle;
    @BindView(R.id.video_upload_location) ProximaNovaSemiboldButton addLocationBtn;
    @BindView(R.id.video_upload_location_text) ProximaNovaRegularTextView addLocationText;
    @BindView(R.id.video_upload_tag_friends) ProximaNovaSemiboldButton tagFriendsBtn;
    @BindView(R.id.video_upload_tag_friends_text) ProximaNovaRegularTextView tagFriendsText;
    @BindView(R.id.video_upload_categories) ProximaNovaSemiboldButton uploadCategories;
    @BindView(R.id.video_upload_categories_text) ProximaNovaRegularTextView uploadCategoriesText;

    private String videoPath;
    private boolean isRequestingLocationUpdates;
    private int page = 0;
    private List<MiniProfile> postOwnerList;

    private FragmentManager fragmentManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
//    private SelectedPlace selectedPlace;

    private void getBundleExtras() {
        Bundle bundle = getIntent().getExtras();
        videoPath = bundle.getString(VIDEO_PATH);
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
        setVideoDuration();

        fragmentManager = getSupportFragmentManager();

        isRequestingLocationUpdates = false;
        updateValuesFromBundle(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
//        checkGps();
        createLocationCallback();
        createLocationRequest();
//        startLocationUpdates();
        videoTitle.setFilters(new InputFilter[] {new InputFilter.LengthFilter(30)});
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        thumbnailViewContainer.setLayoutParams(new LinearLayout.LayoutParams(
                point.x,
                (point.x - (point.x / 3))));
//        videoViewPreview.setVideoPath(videoPath);
//        videoViewPreview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                videoViewPreview.start();
//                if (mediaPlayer.getReact_duration() > 0) {
//                    int duration = mediaPlayer.getReact_duration();
//                    videoDuration = String.format(Locale.UK, "%02d:%02d",
//                            MILLISECONDS.toMinutes(duration),
//                            MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
//                }
//                videoDurationTextView.setText(videoDuration);
//            }
//        });
    }

    private void getLastLocation() {
        if (arePermissionsAllowed(this)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
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
                                        "FusedLocationProvider: " +currentLocation.getLatitude() + " : " + currentLocation.getLongitude(),
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    });
        }
    }

//    private void checkGps() {
//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        if (!enabled) {
//            new AlertDialog.Builder(this)
//                    .setMessage("Please enable location services in order to get your nearby places.")
//                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    })
//                    .show();
//        }
//    }

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

    private void createLocationCallback() {
        if (currentLocation == null) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    currentLocation = locationResult.getLastLocation();
                    Toast.makeText(VideoUpload.this,
                            "LocationCallback: " + currentLocation.getLatitude() + " : " + currentLocation.getLongitude(),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            };
        }
    }

    private void setVideoDuration() {
        new AsyncTask<Void, Void, Void>() {

            private String videoDuration;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(videoPath);
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long duration = Long.parseLong(time );
                retriever.release();
                videoDuration = String.format(Locale.UK, "%02d:%02d",
                        MILLISECONDS.toMinutes(duration),
                        MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                videoDurationTextView.setText(videoDuration);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

//    public void expandOrCollapseToolbar(int fromOffset, int toOffset, int duration){
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
//        final AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
//        if (behavior != null) {
//            ValueAnimator valueAnimator = ValueAnimator.ofInt();
//            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    behavior.setTopAndBottomOffset((Integer) animation.getAnimatedValue());
//                    appBarLayout.requestLayout();
//                }
//            });
//            valueAnimator.setIntValues(fromOffset, toOffset);
//            valueAnimator.setDuration(duration);
//            valueAnimator.start();
//        }
//    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new AsyncTask<String, Void, Void>() {

            Bitmap bitmap;
            byte[] bytes;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
//                expandOrCollapseToolbar(appBarLayout.getHeight(), 0, 600);
            }

            @Override
            protected Void doInBackground(String... strings) {
                bytes = getThumbnail(strings[0]);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                thumbnailView.setImageBitmap(bitmap);
                progressBar.setVisibility(View.INVISIBLE);
                dynamicToolbarColor(bitmap);
//                ViewTransform.collapse(thumbnailViewContainer);
//                expandOrCollapseToolbar(thumbnailView.getHeight(), (int) -(thumbnailView.getHeight() / 1.5), 800);
            }
        }.execute(videoPath);
    }

    @OnClick(R.id.video_preview_thumbnail) public void playVideoPreview() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.float_down, R.anim.sink_up, R.anim.float_down, R.anim.sink_up)
                .add(R.id.video_upload_fragment_container,
                        VideoPreview.newInstance(videoPath),
                        TAG_VIDEO_PREVIEW)
                .commit();
    }

//    @OnClick(R.id.video_duration) public void collapseLayout() {
//        appBarLayout.setExpanded(true, true);
//        expandOrCollapseToolbar(0, (int) -(thumbnailView.getHeight() / 1.5), 800);
//    }

    private byte[] getThumbnail(String videoPath) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        if (thumbnail.getHeight() > thumbnail.getWidth()) {
            thumbnail = ThumbnailUtils.extractThumbnail(thumbnail,
                    thumbnail.getWidth(),
                    (thumbnail.getWidth() - (thumbnail.getWidth() / 3)));
        }
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void dynamicToolbarColor(Bitmap bitmap) {
        new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant;
                int bgColor = Color.parseColor("#26C6DA");
                vibrant = palette.getVibrantSwatch();
                if (vibrant != null) {
                    bgColor = vibrant.getRgb();
                } else {
                    vibrant = palette.getDominantSwatch();
                    if (vibrant != null) {
                        bgColor = vibrant.getRgb();
                    }
                }
                thumbnailViewContainer.setBackgroundColor(bgColor);
            }
        });
    }

    @OnClick(R.id.video_upload_location) public void addLocation() {
        if (arePermissionsAllowed(this)) {
            if (currentLocation != null)
                getNearbyPlacesData(getNearbySearchUrl(currentLocation));
//            launchPlacePicker();
        } else requestPermissions();
    }

    @OnClick(R.id.video_upload_categories) public void getCategories() {
        ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                        .add(R.id.video_upload_fragment_container,
                                Interests.newInstance(Interests.LAUNCH_TYPE_UPLOAD),
                                TAG_INTERESTS_FRAGMENT)
                        .commit();
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                Log.e("getCategories", t.getMessage());
            }
        });
    }

    @OnClick(R.id.video_upload_tag_friends) public void getMyFollowings() {
        if (this.page == 0) this.page = 1;

        ApiCallingService.Friends.getMyFollowings(page).enqueue(new Callback<CircleList>() {
            @Override
            public void onResponse(Call<CircleList> call, Response<CircleList> response) {
                switch (isResponseOk(response)) {
                    case SUCCESS_OK_TRUE:
                        page++;
                        postOwnerList = response.body().getCircles();
                        getMyFollowings();
                        break;
                    case SUCCESS_OK_FALSE:
                        postOwnerList.addAll(response.body().getCircles());
                        break;
                    default:
                        Log.e("getMyFollowings", response.message());
                        break;
                }
            }

            @Override
            public void onFailure(Call<CircleList> call, Throwable t) {
                Log.e("getMyFollowings", t.getMessage());
            }
        });
    }

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

    private void getNearbyPlacesData(String url) {
        new AsyncTask<String, String, ArrayList<HashMap<String, String>>>() {

            private String googlePlacesJsonData;

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(String... strings) {
                try {
//            mMap = (GoogleMap) params[0];
                    String url = strings[0];
                    DownloadUrl downloadUrl = new DownloadUrl();
                    googlePlacesJsonData = downloadUrl.readUrl(url);
                } catch (Exception e) {
                    Log.d("GooglePlacesReadTask", e.toString());
                }
                return DataParser.parse(googlePlacesJsonData);
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> googlePlaces) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                R.anim.slide_in_left, R.anim.slide_out_right)
                        .add(R.id.video_upload_fragment_container, NearbyPlacesList.newInstance(googlePlaces), TAG_NEARBY_PLACES)
                        .commit();
            }
        }.execute(url);
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
                    onNearbyPlacesInteraction(new SelectedPlace(
                            place.getName().toString(),
                            place.getLatLng().latitude,
                            place.getLatLng().longitude));
//                    final CharSequence name = place.getName();
//                    final CharSequence address = place.getAddress();
//                    final CharSequence phone = place.getPhoneNumber();
//                    final String placeId = place.getUserId();
//                    String attribution = place.getAttributions().toString();

//                    String toastMsg = String.format("Place: %s", name);
//                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
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

    @OnClick(R.id.video_upload_check_btn) public void onUploadBtnClick() {
        showProgressLayout();
        File videoFile = new File(videoPath);
        ProgressRequestBody videoBody = new ProgressRequestBody(videoFile, this);
//        RequestBody videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
        MultipartBody.Part videoPartFile = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
        ApiCallingService.Posts.uploadVideo(videoPartFile, videoTitle.getText().toString()).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                ResultObject result = new ResultObject(response.code(), response.message(), response.body().getAuthToken(), false);
                if (result.getCode() == 201 && result.getMessage().equals("Created")) {
                    onUploadFinish();
                } else {
                    Snackbar.make(uploadBtn, "Error " + response.code() + ": " + response.message(), Snackbar.LENGTH_SHORT).show();
                    onUploadError();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                onUploadError();
            }
        });
    }

    private void showProgressLayout() {
//        appBarLayout.setExpanded(true, true);
        progressBar.setIndeterminate(false);
        uploadingNotificationTextView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        uploadingNotificationTextView.setVisibility(View.VISIBLE);
    }

    private void hideProgressLayout() {
        progressBar.setIndeterminate(false);
//        expandOrCollapseToolbar(thumbnailView.getHeight(), (int) -(thumbnailView.getHeight() / 1.5), 800);
        uploadingNotificationTextView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
        uploadingNotificationTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setProgress(percentage);
    }

    @Override
    public void onUploadError() {
        hideProgressLayout();
        new AlertDialog.Builder(this)
                .setTitle("Oops!")
                .setMessage("Videos upload encountered an error, please try again.")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onUploadFinish() {
        hideProgressLayout();
        new AlertDialog.Builder(this)
                .setTitle("Yay!")
                .setMessage("Your video was successfully uploaded.")
                .setCancelable(false)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
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
                            getNearbyPlacesData(getNearbySearchUrl(currentLocation));
                        if (isRequestingLocationUpdates) {
                            startLocationUpdates();
                        }
                    }
                    else {
//                        permissions are denied, show empty list
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                        R.anim.slide_in_left, R.anim.slide_out_right)
                                .add(R.id.video_upload_fragment_container, NearbyPlacesList.newInstance(null), TAG_NEARBY_PLACES)
                                .commit();
                    }
                } else {
//                        permissions are denied, show empty list
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left,
                                    R.anim.slide_in_left, R.anim.slide_out_right)
                            .add(R.id.video_upload_fragment_container, NearbyPlacesList.newInstance(null), TAG_NEARBY_PLACES)
                            .commit();
                }
                break;
            default:
                break;
        }
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
//        this.selectedPlace = selectedPlace;
        addLocationText.setText(selectedPlace.getPlaceName());
        removeFragment(TAG_NEARBY_PLACES);
    }

    @Override
    public void onTagsAndCategoriesInteraction(String result) {
        tagFriendsText.setText(result);
    }

    @Override
    public void onInterestsInteraction(String result) {
        uploadCategoriesText.setText(result);
        if (isFragmentActive(TAG_INTERESTS_FRAGMENT))
            removeFragment(TAG_INTERESTS_FRAGMENT);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        videoViewPreview.stopPlayback();
    }

    private boolean isFragmentActive(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag) != null;
    }

    private void removeFragment(String tag) {
        fragmentManager.beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag(tag))
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (isFragmentActive(TAG_VIDEO_PREVIEW)) {
            removeFragment(TAG_VIDEO_PREVIEW);
        } else if (isFragmentActive(TAG_NEARBY_PLACES)) {
            removeFragment(TAG_NEARBY_PLACES);
        }
        else if (isFragmentActive(TAG_INTERESTS_FRAGMENT))
            removeFragment(TAG_INTERESTS_FRAGMENT);
        else {
            super.onBackPressed();
        }
    }
}

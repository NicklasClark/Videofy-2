package com.cncoding.teazer.ui.home.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.base.UploadParams;
import com.cncoding.teazer.data.model.giphy.Images;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextInputEditText;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.DataParser;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.DownloadUrl;
import com.cncoding.teazer.ui.home.camera.nearbyPlaces.SelectedPlace;
import com.cncoding.teazer.utilities.asynctasks.AddWaterMarkAsyncTask;
import com.cncoding.teazer.utilities.asynctasks.CompressVideoAsyncTask;
import com.cncoding.teazer.utilities.asynctasks.GifConvertAsyncTask;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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
import com.inmobi.sdk.InMobiSdk;
import com.google.gson.Gson;

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
import static com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment.ACTION_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.ui.common.tagsAndCategories.TagsAndCategoryFragment.ACTION_TAGS_FRAGMENT;
import static com.cncoding.teazer.ui.home.camera.nearbyPlaces.NearbyPlacesList.TURN_ON_LOCATION_ACTION;
import static com.cncoding.teazer.utilities.common.CommonUtilities.encodeUnicodeString;
import static com.cncoding.teazer.utilities.common.ViewUtils.IS_GALLERY;
import static com.cncoding.teazer.utilities.common.ViewUtils.IS_REACTION;
import static com.cncoding.teazer.utilities.common.ViewUtils.POST_ID;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.common.ViewUtils.performReactionUpload;
import static com.cncoding.teazer.utilities.common.ViewUtils.performVideoUpload;
import static com.cncoding.teazer.utilities.common.ViewUtils.playVideoInExoPlayer;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class UploadFragment extends Fragment implements EasyPermissions.PermissionCallbacks,
        CompressVideoAsyncTask.AsyncResponse,
        AddWaterMarkAsyncTask.WatermarkAsyncResponse,
        GifConvertAsyncTask.GifConvertAsyncResponse {

    public static final String VIDEO_PATH = "videoPath";
    public static final String TAG_NEARBY_PLACES = "nearbyPlaces";
    public static final String TAG_NULL_NEARBY_PLACES = "nullNearbyPlaces";
    public static final String TAG_CATEGORIES_FRAGMENT = "interestsFragment";
    public static final String TAG_TAGS_FRAGMENT = "tagsFragment";
    private static final int REQUEST_LOCATION_PERMISSIONS = 211;
    private static final int REQUEST_CODE_CHECK_SETTINGS = 312;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "locationUpdates";
    private static final String KEY_LOCATION = "location";
    private static final int RC_LOCATION_PERM = 123;
    private static final int TAGGED_CATEGORIES = 2;
    private static final int TAGGED_FRIENDS = 1;

    private static final String VIDEO_DURATION = "video_duration";
    private static final String IS_GIPHY = "is_giphy";

    @BindView(R.id.share_on_facebook) ProximaNovaRegularCheckedTextView facebookShareBtn;
    @BindView(R.id.share_on_twitter) ProximaNovaRegularCheckedTextView twitterShareBtn;
    @BindView(R.id.video_preview_thumbnail_container) RelativeLayout thumbnailViewContainer;
    @BindView(R.id.video_preview_thumbnail) ImageView thumbnailView;
    @BindView(R.id.video_duration) ProximaNovaRegularTextView videoDurationTextView;
    @BindView(R.id.thumbnail_progress_bar) ProgressBar thumbnailProgressBar;
    @BindView(R.id.progress_bar) ProgressBar topProgressBar;
    @BindView(R.id.video_upload_btn) Button uploadBtn;
    @BindView(R.id.video_upload_title) ProximaNovaRegularTextInputEditText videoTitle;
    @BindView(R.id.video_upload_location) ProximaNovaRegularTextInputEditText addLocationBtn;
    @BindView(R.id.video_upload_tag_friends) ProximaNovaRegularTextInputEditText tagFriendsBtn;
    @BindView(R.id.tag_friends_badge) ProximaNovaRegularTextView tagFriendsBadge;
    @BindView(R.id.video_upload_categories) ProximaNovaRegularTextInputEditText uploadCategoriesBtn;
    @BindView(R.id.categories_badge) ProximaNovaRegularTextView uploadCategoriesBadge;
    @BindView(R.id.gifSwitch) Switch gifSwitch;
    @BindView(R.id.erro_msg) ProximaNovaRegularTextView errorMessage;
    @BindView(R.id.playBtn) AppCompatImageView playBtn;

    public static boolean checkFacebookButtonPressed = false;
    public static boolean checkedTwitterButton = false;
    private boolean isGallery;
    public boolean isReaction;
    private boolean isRequestingLocationUpdates;
    private int tagCount;
    private int categoryCount;
    private String videoPath;
    private String selectedCategoriesToSend = null;
    private String selectedTagsToSend = null;
    private String selectedCategoriesToShow = null;
    private String selectedTagsToShow = null;

    public Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private SelectedPlace selectedPlace;
    private Context context;
    private Activity activity;

    private static boolean isCompressing = false;
    private long initialSize;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private OnUploadFragmentInteractionListener mListener;
    private boolean convertToGif = false;
    private boolean convertingToGif = false;
    private String gifPath;
    private int videoDuration;
    private String oldVideoPath;
    private boolean isGiphy;
    private int postId;

    public UploadFragment() {
        // Required empty public constructor
    }

    public static UploadFragment newInstance(int postId, String videoPath, boolean isReaction,
                                             boolean isGallery, int videoDuration, boolean isGiphy) {
        UploadFragment fragment = new UploadFragment();
        Bundle args = new Bundle();
        args.putInt(POST_ID, postId);
        args.putString(VIDEO_PATH, videoPath);
        args.putBoolean(IS_REACTION, isReaction);
        args.putBoolean(IS_GALLERY, isGallery);
        args.putInt(VIDEO_DURATION, videoDuration);
        args.putBoolean(IS_GIPHY, isGiphy);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        context = getContext();
        if (bundle != null) {
            postId = bundle.getInt(POST_ID);
            videoPath = bundle.getString(VIDEO_PATH);
            isReaction = bundle.getBoolean(IS_REACTION);
            isGallery = bundle.getBoolean(IS_GALLERY);
            videoDuration = bundle.getInt(VIDEO_DURATION);
            isGiphy = bundle.getBoolean(IS_GIPHY);
        }

        if (!isGiphy) {
            CompressVideoAsyncTask compressVideoAsyncTask = new CompressVideoAsyncTask(getContext(), isGallery);
            compressVideoAsyncTask.delegate = this;
            compressVideoAsyncTask.execute(videoPath);
            isCompressing = true;
            initialSize = new File(videoPath).length();
        }

        checkFacebookButtonPressed = false;
        checkedTwitterButton = false;
    }

    @Override
    public void waterMarkProcessFinish(String output, String s) {
        Log.d("Watermark", output);
        enableView(uploadBtn);
        videoPath = output;
    }

    @Override
    public void compressionProcessFinish(String output) {
        videoPath = output;
        try {
            enableView(uploadBtn);
            uploadBtn.setText(R.string.upload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        isCompressing = false;
//        long compressedSize = new File(videoPath).length();
//        Log.d("SIZE", "Before: "+initialSize/1024+" After:"+compressedSize/1024);
    }

    @Override
    public void gifConvertProcessFinish(String output) {
        Log.d("GifConvert", output);
        oldVideoPath = videoPath;
        thumbnailProgressBar.setVisibility(View.GONE);
        convertingToGif = false;
        gifPath = output;
        videoPath = gifPath;

        Glide.with(context)
                .load(gifPath)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(thumbnailView);

        enableView(uploadBtn);
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
        activity = getActivity();

        setBadge(uploadCategoriesBadge, categoryCount, TAGGED_CATEGORIES);
        setBadge(tagFriendsBadge, tagCount, TAGGED_FRIENDS);

        isRequestingLocationUpdates = false;
        updateValuesFromBundle(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        getLastLocation(false);
        createLocationCallback();
        createLocationRequest();

        gifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                convertToGif = isChecked;

                if (convertToGif) {
                    if(videoDuration < 8) {
                        thumbnailView.setClickable(false);
                        playBtn.setVisibility(View.GONE);
                        disableView(uploadBtn, true);
                        convertingToGif = true;

                        if (null == gifPath) {
                            thumbnailProgressBar.setVisibility(VISIBLE);
                            GifConvertAsyncTask gifConvertAsyncTask = new GifConvertAsyncTask(getContext());
                            gifConvertAsyncTask.delegate = UploadFragment.this;
                            gifConvertAsyncTask.execute(videoPath);
                        } else {
                            videoPath = gifPath;
                            Glide.with(context)
                                    .load(gifPath)
//                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(thumbnailView);
                            thumbnailProgressBar.setVisibility(View.GONE);

                            enableView(uploadBtn);
                            convertingToGif = false;
                        }
                    }
                    else {
                        Toast.makeText(context, "Duration can not be greater than 8 seconds", Toast.LENGTH_SHORT).show();
                        gifSwitch.setChecked(false);
                    }
                }
                else {
                    thumbnailView.setClickable(true);
                    playBtn.setVisibility(View.VISIBLE);

                    videoPath = oldVideoPath;
                    if (oldVideoPath != null) {
                        Glide.with(context)
                                .load(Uri.fromFile(new File(oldVideoPath)))
                                .into(thumbnailView);
                    } else {
                        Glide.with(context)
                                .load(Uri.fromFile(new File(videoPath)))
                                .into(thumbnailView);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        if (addingWatermark) {
//            disableView(uploadBtn, true);
//        }
        if(isCompressing) {
            disableView(uploadBtn, true);
            uploadBtn.setText("Processing...");
        }

//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                Glide.with(UploadFragment.this)
//                        .load(Uri.fromFile(new File(videoPath)))
//                        .into(thumbnailView);
//            }
//        }, 1000);

        if (!isGiphy) {
            if (convertingToGif) {
                disableView(uploadBtn, true);
            }

            Glide.with(context)
                    .load(Uri.fromFile(new File(videoPath)))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                    Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                       DataSource dataSource, boolean isFirstResource) {
                            thumbnailProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(thumbnailView);

            if (videoDuration > 0) {
                String durationText = "Duration " + String.format(Locale.UK, "%02d:%02d",
                        MILLISECONDS.toMinutes(videoDuration*1000),
                        MILLISECONDS.toSeconds(videoDuration*1000) - MINUTES.toSeconds(MILLISECONDS.toMinutes(videoDuration*1000)));
                videoDurationTextView.setText(durationText);
            } else {
                new SetVideoDuration(this).execute();
            }
        } else {
            videoDurationTextView.setVisibility(View.GONE);
            gifSwitch.setVisibility(View.GONE);
            thumbnailView.setClickable(false);
            playBtn.setVisibility(View.GONE);

            Gson gson = new Gson();
            Images images = gson.fromJson(videoPath, Images.class);
            Glide.with(context)
                    .load(images.getDownsized().getUrl())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(thumbnailView);
        }

        if (getActivity() != null && getActivity() instanceof CameraActivity) {
            ((CameraActivity) getActivity()).updateBackButton(R.drawable.ic_arrow_back_white_with_shadow);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressWarnings({"ConstantConditions", "deprecation", "unused"})
    public void setupFacebookShareIntent() {
        try {
            ShareDialog shareDialog;
            shareDialog = new ShareDialog(getActivity());
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(videoTitle.getText().toString())
                    .setContentDescription("")
                    .setContentUrl(Uri.parse(videoPath))
                    .build();

            shareDialog.show(linkContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRequestingLocationUpdates)
            startLocationUpdates();

        if (checkFacebookButtonPressed) {
//            checkAction("facebook", facebookShareBtn);
            facebookShareBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_facebook,
                    0, R.drawable.btn_checked, 0);
            facebookShareBtn.setChecked(true);
            facebookShareBtn.setBackgroundTintList(null);
        }

        if (checkedTwitterButton) {
            twitterShareBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_twitter,
                    0, R.drawable.btn_checked, 0);
            twitterShareBtn.setChecked(true);
            twitterShareBtn.setBackgroundTintList(null);
        }
    }

    private void getLastLocation(final boolean firstTime) {
        if (arePermissionsAllowed(activity)) {
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

                                //location for inmobi
                                InMobiSdk.setLocation(currentLocation);

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

                    //location for inmobi
//                    InMobiSdk.setLocation(currentLocation);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String getNearbySearchUrl(Location location) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=").append(location.getLatitude()).append(",").append(location.getLongitude());
        googlePlacesUrl.append("&radius=" + 2000);
//        googlePlacesUrl.append("&type=");
//        googlePlacesUrl.append("airport|amusement_park|aquarium|art_gallery|bakery|bar|beauty_salon" +
//                "|book_store|bowling_alley|cafe|casino|clothing_store|gym" +
//                "|hair_care|jewelry_store|library|night_club|park|shopping_mall|stadium" +
//                "|spa|subway_station|university|zoo");
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
        if (!checkFacebookButtonPressed) {
            checkAction("facebook", facebookShareBtn);
            checkFacebookButtonPressed = true;
            facebookShareBtn.setChecked(true);
            facebookShareBtn.setBackgroundTintList(null);
        } else {
            checkAction("facebook", facebookShareBtn);
            checkFacebookButtonPressed = false;
            facebookShareBtn.setChecked(false);
            facebookShareBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
        }
    }

    @OnClick(R.id.share_on_twitter) public void shareOnTwitterAction() {
        if (!checkedTwitterButton) {
            checkAction("twitter", twitterShareBtn);
            checkedTwitterButton = true;
            twitterShareBtn.setChecked(true);
            twitterShareBtn.setBackgroundTintList(null);
        } else {
            checkAction("twitter", twitterShareBtn);
            checkedTwitterButton = false;
            twitterShareBtn.setChecked(false);
            twitterShareBtn.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDisabled)));
        }
    }

    @OnClick(R.id.video_preview_thumbnail) public void playVideoPreview() {
        hideKeyboard(activity, videoTitle);
        playVideoInExoPlayer(activity, videoPath);
    }

    @OnTouch(R.id.video_upload_location) public boolean addLocation(View view, MotionEvent motionEvent) {
        Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        if (motionEvent.getAction() == MotionEvent.ACTION_UP &&
                rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())) {
//            addLocationBtn.requestFocus();
            hideKeyboard(activity, view);
            if (arePermissionsAllowed(activity)) {
                if (currentLocation != null)
                    new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
            } else requestPermissions();
            return true;
        }
        return false;
    }

    @OnTouch(R.id.video_upload_tag_friends) public boolean getMyFollowings(View view, MotionEvent motionEvent) {
        Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        if (motionEvent.getAction() == MotionEvent.ACTION_UP &&
                rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())) {
//            tagFriendsBtn.requestFocus();
            hideKeyboard(activity, view);
            mListener.onUploadInteraction(TAG_TAGS_FRAGMENT, null, selectedTagsToShow);
            tagFriendsBtn.clearFocus();
            return true;
        }
        return false;
    }

    @OnTouch(R.id.video_upload_categories) public boolean getCategories(View view, MotionEvent motionEvent) {
        Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        if (motionEvent.getAction() == MotionEvent.ACTION_UP &&
                rect.contains(view.getLeft() + (int) motionEvent.getX(), view.getTop() + (int) motionEvent.getY())) {
//            uploadCategoriesBtn.requestFocus();
            hideKeyboard(activity, view);
            mListener.onUploadInteraction(TAG_CATEGORIES_FRAGMENT, null, selectedCategoriesToShow);
            uploadCategoriesBtn.clearFocus();
//            motionEvent.setAction(MotionEvent.ACTION_DOWN);
            return true;
        }
        return false;
    }

    @OnClick(R.id.video_upload_btn) public void onUploadBtnClick() {
        if (validateFields()) {
            toggleInteraction(false);
            String title = encodeUnicodeString(videoTitle.getText().toString().equals("") ? null : videoTitle.getText().toString());
            String location = null;
            double latitude = 0;
            double longitude = 0;
            if (selectedPlace != null) {
                if (selectedPlace.getPlaceName() != null) {
                    location = selectedPlace.getPlaceName().equals("") ? null : selectedPlace.getPlaceName();
                }
                latitude = selectedPlace.getLatitude();
                longitude = selectedPlace.getLongitude();
            }
            DecimalFormat df = new DecimalFormat("#.#####");
            latitude = Double.parseDouble(df.format(latitude));
            longitude = Double.parseDouble(df.format(longitude));

            if (getActivity() instanceof CameraActivity) {
                if (!isReaction) {
                    performVideoUpload(activity, new UploadParams(postId, isGallery, videoPath, title, location,
                            latitude, longitude, selectedTagsToSend, selectedCategoriesToSend, isGiphy));
                } else {
                    performReactionUpload(activity, new UploadParams(postId, isGallery,
                            videoPath, title, location, latitude, longitude, isGiphy));
                }
            }
        }
    }

    private void checkAction(String type, ProximaNovaRegularCheckedTextView view) {
        if (!view.isChecked()) {
            view.setChecked(true);
            view.setCompoundDrawablesWithIntrinsicBounds(type.equals("facebook") ? R.drawable.ic_facebook
                            : R.drawable.ic_twitter,
                    0, R.drawable.btn_checked, 0);
        } else {
            view.setChecked(false);
            view.setCompoundDrawablesWithIntrinsicBounds(type.equals("facebook") ? R.drawable.ic_facebook :
                            R.drawable.ic_twitter,
                    0, R.drawable.btn_unchecked, 0);
        }
    }

    private boolean validateFields() {
        if (!isReaction && TextUtils.isEmpty(videoTitle.getText())) {
           // videoTitle.setError(getString(R.string.required));
            videoTitle.requestFocus();
            errorMessage.setVisibility(View.VISIBLE);
            return false;
        } else if (!isReaction && (selectedCategoriesToSend == null || selectedCategoriesToSend.length() == 0)) {
            Toast.makeText(activity, getString(R.string.required_categories), Toast.LENGTH_SHORT).show();
            errorMessage.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    @AfterPermissionGranted(RC_LOCATION_PERM) private void startLocationService() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(activity, perms)) {
            new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_LOCATION_PERM, perms);
        }
    }

    public void onNearbyPlacesListInteraction(int action) {
        if (action == TURN_ON_LOCATION_ACTION) createLocationRequest();
    }

    public void onNearbyPlacesAdapterInteraction(SelectedPlace selectedPlace) {
        this.selectedPlace = selectedPlace;
        final String placeName = selectedPlace.getPlaceName();
        if (!placeName.contains("null")) {
            addLocationBtn.requestFocus();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addLocationBtn.setText(placeName);
                }
            }, 500);
        } else {
            addLocationBtn.setText(null);
            Toast.makeText(activity, R.string.could_not_find_location, Toast.LENGTH_SHORT).show();
        }
    }

    public void onTagsAndCategoriesInteraction(String action, final String resultToShow, String resultToSend, final int count) {
        switch (action) {
            case ACTION_TAGS_FRAGMENT:
                tagCount = count;
                selectedTagsToShow = resultToShow;
                selectedTagsToSend = resultToSend;
                if (resultToShow.trim().isEmpty()) {
                    tagFriendsBtn.setText(null);
                } else {
                    tagFriendsBtn.requestFocus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tagFriendsBtn.setText(resultToShow);
                        }
                    }, 500);
                }
                setBadge(tagFriendsBadge, count, TAGGED_FRIENDS);
                break;
            case ACTION_CATEGORIES_FRAGMENT:
                categoryCount = count;
                selectedCategoriesToShow = resultToShow;
                selectedCategoriesToSend = resultToSend;
                if (resultToShow.trim().isEmpty() || resultToShow.equals(""))
                    uploadCategoriesBtn.setText(null);
                else {
                    uploadCategoriesBtn.requestFocus();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadCategoriesBtn.setText(resultToShow);
                            setBadge(uploadCategoriesBadge, count, TAGGED_CATEGORIES);
                        }
                    }, 500);
                }
                setBadge(uploadCategoriesBadge, count, TAGGED_CATEGORIES);
                break;
        }
    }

    private void setBadge(ProximaNovaRegularTextView view, int count, int check) {
        view.setVisibility(count == 0 ? View.GONE : VISIBLE);
        if (view.getVisibility() == VISIBLE) {
            String countText = String.valueOf(count);
            if (count <= 9) countText = "0" + countText;
            view.setText(check == TAGGED_FRIENDS ? countText + " Tagged Friends" : countText + " Tagged Categories");
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
        } else {
            disableView(videoTitle, false);
            disableView(addLocationBtn, false);
            disableView(tagFriendsBtn, false);
            disableView(uploadCategoriesBtn, false);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        new GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("HomeFragment", "onPermissionsDenied:" + requestCode + ":" + perms.size());

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
                        Snackbar.make(uploadBtn, R.string.location_services_required, Snackbar.LENGTH_SHORT).show();
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
//            reference.get().toggleInteraction(true);
            if (reference != null) {
                reference.get().mListener.onUploadInteraction(TAG_NEARBY_PLACES, googlePlaces, null);
            }
        }
    }

    private static class SetVideoDuration extends AsyncTask<Void, Void, String> {

        private WeakReference<UploadFragment> reference;

        SetVideoDuration(UploadFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        protected void onPostExecute(String videoDuration) {
            reference.get().videoDurationTextView.setText(videoDuration);
            reference.get().thumbnailProgressBar.setVisibility(View.GONE);
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
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null && getActivity() instanceof CameraActivity) {
            ((CameraActivity) getActivity()).updateBackButton(-1);
        }
        checkFacebookButtonPressed = false;
        checkedTwitterButton = false;
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
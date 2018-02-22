package com.cncoding.teazer.ui.fragment.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaBoldButton;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextInputEditText;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.camera.nearbyPlaces.DataParser;
import com.cncoding.teazer.home.camera.nearbyPlaces.DownloadUrl;
import com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList;
import com.cncoding.teazer.home.camera.nearbyPlaces.SelectedPlace;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.home.tagsAndCategories.TagsAndCategoryFragment;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.model.friends.CircleList;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.post.UpdatePostRequest;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;
import static android.app.Activity.RESULT_OK;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.apiCalls.ApiCallingService.Friends.isResponseOk;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_FALSE;
import static com.cncoding.teazer.apiCalls.ApiCallingService.SUCCESS_OK_TRUE;
import static com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList.NEARBY_PLACE_AUTOCOMPLETE_ACTION;
import static com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList.TURN_ON_LOCATION_ACTION;
import static com.cncoding.teazer.home.tagsAndCategories.TagsAndCategoryFragment.ACTION_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.home.tagsAndCategories.TagsAndCategoryFragment.ACTION_TAGS_FRAGMENT;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;
import static com.cncoding.teazer.utilities.CommonUtilities.encodeUnicodeString;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;

/**
 * 
 * Created by farazhabib on 29/11/17.
 */

public class EditPostFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    public static final String VIDEO_PATH = "videoPath";
    public static final String POST_DETAILS = "postdetails";
    private static final String TAG_NEARBY_PLACES = "nearbyPlaces";
    private static final String TAG_INTERESTS_FRAGMENT = "interestsFragment";
    private static final String TAG_TAGS_FRAGMENT = "tagsFragment";
    //    private static final int REQUEST_PLACE_PICKER = 212;
    private static final int REQUEST_LOCATION_PERMISSIONS = 211;
    private static final int REQUEST_CODE_PLACE_AUTOCOMPLETE = 210;
    private static final int REQUEST_CODE_CHECK_SETTINGS = 312;
    private static final String REQUESTING_LOCATION_UPDATES_KEY = "locationUpdates";
    private static final String KEY_LOCATION = "location";
    private static final int RC_LOCATION_PERM = 123;

    public static boolean checkefacebookeButtonPressed = false;

    @BindView(R.id.spacer)
    Space spacer;
    @BindView(R.id.spacer1)
    Space spacer1;
    @BindView(R.id.video_actions)
    RelativeLayout videoActions;
    @BindView(R.id.video_preview_thumbnail_container)
    RelativeLayout thumbnailViewContainer;
    @BindView(R.id.video_preview_thumbnail)
    ImageView thumbnailView;
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.video_duration)
    ProximaNovaRegularTextView videoDurationTextView;
    @BindView(R.id.progress_bar)
    ProgressBar thumbnailProgressBar;
    @BindView(R.id.top_progress_bar)
    ProgressBar topProgressBar;
    @BindView(R.id.video_upload_retake_btn)
    Button cancelBtn;
    @BindView(R.id.video_upload_btn)
    Button uploadBtn;
    @BindView(R.id.video_upload_title)
    ProximaNovaRegularTextInputEditText videoTitle;
    @BindView(R.id.video_upload_location)
    ProximaNovaBoldButton addLocationBtn;
    @BindView(R.id.video_upload_location_text)
    ProximaNovaRegularTextView addLocationText;
    @BindView(R.id.video_upload_tag_friends)
    ProximaNovaBoldButton tagFriendsBtn;
    @BindView(R.id.video_upload_tag_friends_text)
    ProximaNovaRegularTextView tagFriendsText;
    @BindView(R.id.video_upload_categories)
    ProximaNovaBoldButton uploadCategoriesBtn;
    @BindView(R.id.video_upload_categories_text)
    ProximaNovaRegularTextView uploadCategoriesText;
    @BindView(R.id.up_btn)
    AppCompatImageView upBtn;
    @BindView(R.id.save)
    FloatingActionButton save;

    public String videoPath;
    public boolean isReaction;
    String selectedCategoriesToSend = null;
    String selectedTagsToSend = null;
    private boolean isRequestingLocationUpdates;
    private ArrayList<MiniProfile> myFollowingsList = new ArrayList<>();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private SelectedPlace selectedPlace;
    private Context context;
    private Activity activity;
    String categories;
    private OnUploadFragmentInteractionListener mListener;
    private boolean isGallery;
    PostDetails postDetails;
    StringBuilder categoryId;
    StringBuilder categoryName;
    String location;
    private String selectedCategories;
    List<TaggedUser> taggedUsers;
    StringBuilder stringBuilder;
    StringBuilder selectTagIdBuilder;
    private String selectedTags;
    private String sTags;
    private String selectedTagIDToSend;
    private  String categoriesResultName;
    private  String categoriesResultId;

    public EditPostFragment() {
    }

    public static EditPostFragment newInstance(PostDetails postDetails) {
        EditPostFragment fragment = new EditPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(POST_DETAILS, postDetails);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            postDetails = bundle.getParcelable(POST_DETAILS);
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, isRequestingLocationUpdates);
        super.onSaveInstanceState(outState);
    }
    private void updateValuesFromBundle(Bundle savedInstanceState) {
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_editpost, container, false);
        ButterKnife.bind(this, rootView);

        context = getContext();
        activity = getActivity();
        //  new GetThumbnail(this).execute();
        new SetVideoDuration(this).execute();
        isRequestingLocationUpdates = false;

        updateValuesFromBundle(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        getLastLocation(false);

        createLocationCallback();
        createLocationRequest();

        videoTitle.setText(decodeUnicodeString(postDetails.getTitle()));
        if (postDetails.getMedias().get(0).getThumbUrl() != null) {
            Glide.with(context)
                    .load(postDetails.getMedias().get(0).getThumbUrl())
                    .into(thumbnailView);
        } else {
            Glide.with(context)
                    .load(R.drawable.material_flat)
                    .into(thumbnailView);
        }
        if (postDetails.getCheckIn() != null) {
            addLocationText.setText(postDetails.getCheckIn().getLocation());
        }

        List<Category> list = postDetails.getCategories();
        categoryName = new StringBuilder();
        categoryId = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            categoryName.append(list.get(i).getCategoryName());
            categoryId.append(list.get(i).getCategoryId());

            if (i != list.size() - 1) {
                categoryName.append(",");
                categoryId.append(",");
            }
        }

        selectedCategories = categoryName.toString();
        if(categoriesResultName==null) {
            uploadCategoriesText.setText(selectedCategories);
        }
        else
        {
            uploadCategoriesText.setText(categoriesResultName);
        }
        getTagFriends(postDetails.getPostId());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = 0;
                double longitude = 0;

                if (selectedPlace != null) {
                    location = selectedPlace.getPlaceName().equals("") ? null : selectedPlace.getPlaceName();
                    latitude = selectedPlace.getLatitude();
                    longitude = selectedPlace.getLongitude();
                }

                if (categoriesResultId != null) {
                    categories = categoriesResultId;
                } else {
                    categories = categoryId.toString();
                }

                if(selectedTagsToSend==null)
                {
                    sTags=selectedTagIDToSend;
                }
                else
                {
                    sTags=selectedTagsToSend;

                }
                boolean valid=true;
                String title = videoTitle.getText().toString();
                title = encodeUnicodeString(title);
                int postId = postDetails.getPostId();

                if (title==null|| title.equals("")||title.isEmpty()) {
                    videoTitle.setError("Enter Video Title ");
                    videoTitle.requestFocus();
                    valid = false;

                } else {
                    videoTitle.setError(null);
                }

                if(valid) {
                    UpdatePostRequest updatePostRequest = new UpdatePostRequest(postId, location, title, latitude, longitude, sTags, categories);
                    updatePost(updatePostRequest);
                }
            }
        });
        return rootView;
    }

    private void updatePost(UpdatePostRequest updatePostRequest) {
        ApiCallingService.Posts.updatePost(updatePostRequest, context).enqueue(new Callback<PostUploadResult>() {
            @Override
            public void onResponse(Call<PostUploadResult> call, Response<PostUploadResult> response) {

                if (response.code() == 200) {
                    try {
                        Toast.makeText(context, "Your post has been updated sucessfully", Toast.LENGTH_SHORT).show();
                        ProfileFragment.checkpostupdated=true;
                        getActivity().onBackPressed();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Exception", e.getMessage());
                    }
                }
                else {

                }
            }

            @Override
            public void onFailure(Call<PostUploadResult> call, Throwable t) {
                t.printStackTrace();
            }
        });


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
                                    new GetNearbyPlacesData(EditPostFragment.this).execute(getNearbySearchUrl(currentLocation));
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    @OnEditorAction(R.id.video_upload_title)
    public boolean titleDone(TextView view, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            hideKeyboard(activity, view);
            return true;
        }
        return false;
    }

    @OnClick(R.id.video_preview_thumbnail)
    public void playVideoPreview() {
        //  playVideo(context, videoPath, false);

    }

    @OnClick(R.id.video_upload_location)
    public void addLocation() {
        if (arePermissionsAllowed(context)) {
            if (currentLocation != null)
                new EditPostFragment.GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
        } else requestPermissions();
//        startLocationService();
    }

    @OnClick(R.id.video_upload_categories)
    public void getCategories() {

        mListener.updateCategories(postDetails);

//        toggleInteraction(false);
//        ApiCallingService.Application.getCategories().enqueue(new Callback<ArrayList<RealmCategory>>() {
//            @Override
//            public void onResponse(Call<ArrayList<RealmCategory>> call, Response<ArrayList<RealmCategory>> response) {
//                if (response.code() == 200) {
//                    if (response.body() != null) {
//                        toggleUpBtnVisibility(VISIBLE);
//                        mListener.onUploadInteraction(false,
//                                TagsAndCategoryFragment.newInstance(ACTION_CATEGORIES_FRAGMENT, selectedCategories),
//                                TAG_INTERESTS_FRAGMENT);
//                    } else
//                        Snackbar.make(uploadCategoriesBtn, "There was an error fetching categories, please try again.",
//                                Snackbar.LENGTH_SHORT).show();
//                } else
//                    Snackbar.make(uploadCategoriesBtn, response.code() + " : " + response.message(),
//                            Snackbar.LENGTH_SHORT).show();
//                toggleInteraction(true);
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<RealmCategory>> call, Throwable t) {
//                Log.e("getCategories", t.getMessage());
//                toggleInteraction(true);
//            }
//        });
    }

    public void getCategoriesResult(String categoriesResultId,String categoriesResultName)
    {
        this.categoriesResultName=categoriesResultName;
        this.categoriesResultId=categoriesResultId;

    }

    @OnClick(R.id.video_upload_tag_friends)
    public void getMyFollowings() {
        toggleInteraction(false);
        getMyCircle(1);
    }

    private void getMyCircle(final int page) {
        ApiCallingService.Friends.getMyCircle(page, context).enqueue(new Callback<CircleList>() {
            @Override
            public void onResponse(Call<CircleList> call, Response<CircleList> response) {
                if (response.body().getCircles() != null) {
                    switch (isResponseOk(response)) {
                        case SUCCESS_OK_TRUE:
                            myFollowingsList.addAll(response.body().getCircles());
                            getMyCircle(page + 1);
                            break;
                        case SUCCESS_OK_FALSE:
                            myFollowingsList.addAll(response.body().getCircles());
                            toggleUpBtnVisibility(VISIBLE);
                            mListener.onUploadInteraction(false,
                                    TagsAndCategoryFragment.newInstance(ACTION_TAGS_FRAGMENT, selectedTags),
                                    TAG_TAGS_FRAGMENT);
                            break;
                        default:
                            Log.e("getMyCircle", response.message());
                            break;
                    }
                } else Toast.makeText(context, "No friends yet!", Toast.LENGTH_SHORT).show();
                toggleInteraction(true);
            }

            @Override
            public void onFailure(Call<CircleList> call, Throwable t) {
                Log.e("getMyCircle", t.getMessage());
                toggleInteraction(true);
            }
        });
    }


    private String getSelectedTagsToShow(ArrayList<MiniProfile> sparseArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            stringBuilder.append(sparseArray.get(i).getFirstName());
            if (i < sparseArray.size() - 1) {
                stringBuilder.append(", ");

            }
        }
        return stringBuilder.toString();
    }

    private String getSelectedCategoriesToShow(ArrayList<Category> sparseArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < sparseArray.size(); i++) {
            stringBuilder.append(sparseArray.get(i).getCategoryName());
            if (i < sparseArray.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    public void getTagFriends(final int postId) {
        ApiCallingService.Posts.getPostDetails(postId, context).enqueue(new Callback<PostDetails>() {
            @Override
            public void onResponse(Call<PostDetails> call, Response<PostDetails> response) {
                if (response.code() == 200) {
                    try {

                        taggedUsers = response.body().getTaggedUsers();
                        stringBuilder = new StringBuilder();
                        selectTagIdBuilder=new StringBuilder();
                        if(taggedUsers.size()==0||taggedUsers==null) {
                            selectedTags="";
                            selectedTagIDToSend="";
                        }
                        else
                        {
                            for (int i = 0; i < taggedUsers.size(); i++)
                            {
                                stringBuilder.append(taggedUsers.get(i).getFirstName());
                                selectTagIdBuilder.append(taggedUsers.get(i).getUserId());
                                if (i != taggedUsers.size() - 1) {
                                    stringBuilder.append(",");
                                    selectTagIdBuilder.append(",");

                                }
                            }
                            selectedTags = stringBuilder.toString();
                            selectedTagIDToSend=selectTagIdBuilder.toString();
                            tagFriendsText.setText(selectedTags);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }

            }
            @Override
            public void onFailure(Call<PostDetails> call, Throwable t) {
                toggleInteraction(true);
            }
        });
    }


//    @OnClick(R.id.video_upload_check_btn)
//    public void onUploadBtnClick() {
//        if (validateFields()) {
//            toggleInteraction(false);
//            String title = videoTitle.getText().toString().equals("") ? null : videoTitle.getText().toString();
//            String location = null;
//            double latitude = 0;
//            double longitude = 0;
//            if (selectedPlace != null) {
//                location = selectedPlace.getPlaceName().equals("") ? null : selectedPlace.getPlaceName();
//                latitude = selectedPlace.getLatitude();
//                longitude = selectedPlace.getLongitude();
//            }
//            String tags = tagFriendsText.getText().toString().equals("") ? null : tagFriendsText.getText().toString();
//            DecimalFormat df = new DecimalFormat("#.#######");
//            performVideoUpload(activity, new Pojos.UploadParams(isGallery, videoPath, false, title, location,
//                    Double.parseDouble(df.format(latitude)), Double.parseDouble(df.format(longitude)), tags, selectedCategoriesToSend, null));
//        }
//    }

    private boolean validateFields() {
        if (TextUtils.isEmpty(videoTitle.getText())) {
            videoTitle.setError(getString(R.string.required));
            videoTitle.requestFocus();
            return false;
        } else if (selectedCategoriesToSend == null || selectedCategoriesToSend.length() == 0) {
            Toast.makeText(context, getString(R.string.required_categories), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @OnClick(R.id.video_upload_retake_btn)
    public void retakeVideo() {
        mListener.onUploadInteraction(true, null, null);
    }

    @AfterPermissionGranted(RC_LOCATION_PERM)
    private void startLocationService() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(context, perms)) {
            new EditPostFragment.GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale),
                    RC_LOCATION_PERM, perms);
        }
    }

    public void toggleUpBtnVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                if (upBtn.getVisibility() != VISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
                    upBtn.setVisibility(VISIBLE);
                }
                break;
            case INVISIBLE:
                if (upBtn.getVisibility() != INVISIBLE) {
                    upBtn.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_out));
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
        addLocationText.setText(selectedPlace.getPlaceName());
    }

    public void onTagsAndCategoriesInteraction(String action, final String resultToShow, final String resultToSend) {
        switch (action) {
            case ACTION_TAGS_FRAGMENT:
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tagFriendsText.setText(resultToShow);
                        selectedTags = resultToShow;
                        if(resultToSend==null)
                        {
                            selectedTagsToSend="";
                        }
                        else
                        {
                            selectedTagsToSend = resultToSend;
                        }
                        tagFriendsText.setText(selectedTags);
                    }
                });
                break;

            case ACTION_CATEGORIES_FRAGMENT:
                if(resultToSend==null)
                {
                    selectedCategoriesToSend = "";
                }
                else
                {
                    selectedCategoriesToSend = resultToSend;
                }

                selectedCategories = resultToShow;
                uploadCategoriesText.setText(resultToShow);
                break;
        }
    }

    public void toggleInteraction(boolean isEnabled) {
        videoTitle.setEnabled(isEnabled);
        addLocationBtn.setEnabled(isEnabled);
        tagFriendsBtn.setEnabled(isEnabled);
        uploadCategoriesBtn.setEnabled(isEnabled);
        cancelBtn.setEnabled(isEnabled);
        uploadBtn.setEnabled(isEnabled);
        if (isEnabled)
            topProgressBar.setVisibility(INVISIBLE);
        else
            topProgressBar.setVisibility(VISIBLE);
        if (isEnabled) {
            videoTitle.setAlpha(1);
            addLocationBtn.setAlpha(1);
            tagFriendsBtn.setAlpha(1);
            uploadCategoriesBtn.setAlpha(1);
            cancelBtn.setAlpha(1);
            uploadBtn.setAlpha(1);
        } else {
            videoTitle.setAlpha(0.5f);
            addLocationBtn.setAlpha(0.5f);
            tagFriendsBtn.setAlpha(0.5f);
            uploadCategoriesBtn.setAlpha(0.5f);
            cancelBtn.setAlpha(0.5f);
            uploadBtn.setAlpha(0.5f);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        new EditPostFragment.GetNearbyPlacesData(this).execute(getNearbySearchUrl(currentLocation));
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
//            case REQUEST_CODE_PLACE_AUTOCOMPLETE:
//                if (resultCode == RESULT_OK) {
//                    Place place = PlaceAutocomplete.getPlace(activity, data);
//                    onNearbyPlacesAdapterInteraction(
//                            new SelectedPlace(
//                                    place.getName().toString(),
//                                    place.getLatLng().latitude,
//                                    place.getLatLng().longitude));
//                    mListener.onUploadInteraction(false, null, null);
//                }
//                else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
//                    Status status = PlaceAutocomplete.getStatus(context, data);
//                    String message = status.getStatusMessage();
//                    if (message != null) {
//                        Snackbar.make(addLocationBtn, message, Snackbar.LENGTH_SHORT).show();
//                    } else
//                        Snackbar.make(addLocationBtn, "Oops! Something went wrong, please retry.", Snackbar.LENGTH_SHORT).show();
//
//                }
//                break;
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

    private static class GetNearbyPlacesData extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

        private String googlePlacesJsonData;
        private WeakReference<EditPostFragment> reference;

        GetNearbyPlacesData(EditPostFragment context) {
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
            reference.get().toggleUpBtnVisibility(VISIBLE);
            reference.get().mListener.onUploadInteraction(false,
                    NearbyPlacesList.newInstance(googlePlaces), TAG_NEARBY_PLACES);
        }
    }

    private static class SetVideoDuration extends AsyncTask<Void, Void, String> {

        private WeakReference<EditPostFragment> reference;

        SetVideoDuration(EditPostFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
//                File file = new File(reference.get().videoPath);
//
//                if (file.exists()) {
//                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                    retriever.setDataSource(reference.get().videoPath);
//                    String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//                    long duration;
//                    try {
//                        duration = Long.parseLong(time);
//                        retriever.release();
//                        return String.format(Locale.UK, "%02d:%02d",
//                                MILLISECONDS.toMinutes(duration),
//                                MILLISECONDS.toSeconds(duration) - MINUTES.toSeconds(MILLISECONDS.toMinutes(duration)));
//                    } catch (NumberFormatException e) {
//                        if (e.getMessage() != null)
//                            Log.e("ErrorParsingDuration", e.getMessage());
//                        return "";
//                    }
//                } else
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
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
//                        permissions are denied, show empty myReactions
                        showEmptyList();
                    }
                } else {
//                        permissions are denied, show empty myReactions
                    showEmptyList();
                }
                break;
            default:
                break;
        }
    }

    private void showEmptyList() {
        toggleUpBtnVisibility(VISIBLE);
        mListener.onUploadInteraction(false,
                NearbyPlacesList.newInstance(null), TAG_NEARBY_PLACES);
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
        if (context instanceof EditPostFragment.OnUploadFragmentInteractionListener) {
            mListener = (EditPostFragment.OnUploadFragmentInteractionListener) context;
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
        void onUploadInteraction(boolean isBackToCamera, Fragment fragment, String tag);
        void updateCategories(PostDetails postDetails);
    }
}
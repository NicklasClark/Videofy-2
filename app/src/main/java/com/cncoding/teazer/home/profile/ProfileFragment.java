package com.cncoding.teazer.home.profile;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.SignPainterTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.Settings;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.SharedPrefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import jp.wasabeef.blurry.Blurry;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.cncoding.teazer.utilities.SharedPrefs.getBlurredProfilePic;
import static com.cncoding.teazer.utilities.SharedPrefs.isBlurredProfilePicSaved;

public class ProfileFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ImageView profile_image;
    ImageView bgImage;
    ImageView settings;
    ImageView small_profile_icon;
    LinearLayout mContainerView;
    Context context;
    AppBarLayout appBarLayout;
    ProximaNovaRegularCheckedTextView _name;
    SignPainterTextView _username;
    TextView _creations;
    TextView _followers;
    TextView _following;
    ViewPager viewPager;
    TabLayout tabLayout;
    ProximaNovaRegularCheckedTextView _detail;
    ImageView backgroundProfile;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    Pojos.User.UserProfile userprofile;
    Button btnedit;
    Button btnshare;
    int totalfollowers;
    int totalfollowing;
    int totalvideos;
    String firstname;
    String userId;
    String lastname;
    String username;
    String email;
    int accountType;
    boolean hasProfleMedia;
    Long mobilenumber;
    int gender;
    int countrycode;
    String detail;
    CoordinatorLayout coordinatorLayout;
    ProgressBar progressbar;
    CircularAppCompatImageView profile_id;
    private FollowerListListener mListener;
    private String imageUri;
    private static final int RC_REQUEST_STORAGE = 1001;
    private String userProfileThumbnail;
    private String userProfileUrl;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        previousTitle = getParentActivity().getToolbarTitle();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = getContext();


        _name = view.findViewById(R.id.username);
        _username = view.findViewById(R.id.username_title);
        _creations = view.findViewById(R.id.creations);
        _followers = view.findViewById(R.id.followers);
        _following = view.findViewById(R.id.following);
        _detail = view.findViewById(R.id.hobby);
        backgroundProfile = view.findViewById(R.id.background_profile);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        btnedit = view.findViewById(R.id.btnedit);
        btnshare = view.findViewById(R.id.btnshare);
        coordinatorLayout = view.findViewById(R.id.layout);
        progressbar = view.findViewById(R.id.progress_bar);
        profile_id = view.findViewById(R.id.profile_id);


        btnedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProfile.class);
                intent.putExtra("UserName", username);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("LastName", lastname);
                intent.putExtra("EmailId", email);
                intent.putExtra("MobileNumber", String.valueOf(mobilenumber));
                intent.putExtra("Gender", String.valueOf(gender));
                intent.putExtra("CountryCode", String.valueOf(countrycode));
                intent.putExtra("ProfileThumb", userProfileThumbnail);
                intent.putExtra("ProfileMedia", userProfileUrl);
                if (detail == null)
                    intent.putExtra("Detail", "");
                else {
                    intent.putExtra("Detail", detail);
                }
                startActivity(intent);
            }
        });
        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onFollowerListListener(String.valueOf(0),"User");
            }
        });
        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mListener.onFollowingListListener(String.valueOf(0),"User");
            }
        });
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "https://play.google.com/store/apps/details?id=com.app_towertwtm.layout";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Teazer App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));

            }
        });

        tabLayout = view.findViewById(R.id.sliding_tabs);
        viewPager = view.findViewById(R.id.viewpager);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfileDetail();
        getParentActivity().updateToolbarTitle("Profile");
        getParentActivity().showAppBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //  super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_user_profile,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.action_settings:
              Intent intent=new Intent(context, Settings.class);
                        intent.putExtra("AccountType",String.valueOf(accountType));
              startActivity(intent);
            case R.id.action_profile_block:
               // openBlockUser(followerfollowingid);
        }
        return true;
    }

    public void getProfileDetail() {

        progressbar.setVisibility(View.VISIBLE);
        coordinatorLayout.setVisibility(View.GONE);

        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<Pojos.User.UserProfile>() {
            @Override
            public void onResponse(Call<Pojos.User.UserProfile> call, Response<Pojos.User.UserProfile> response) {

                try {
                    PublicProfile userProfile = response.body().getUserProfile();
                    firstname = userProfile.getFirstName();
                    lastname = userProfile.getLastName();
                    username = userProfile.getUserName();
                    email = userProfile.getEmail();
                    accountType = userProfile.getAccountType();
                    hasProfleMedia = userProfile.getHasProfileMedia();
                    totalfollowers = response.body().getFollowers();
                    totalfollowing = response.body().getFollowings();
                    totalvideos = response.body().getTotalVideos();
                    userId = String.valueOf(userProfile.getUserId());
                    gender = userProfile.getGender();
                    Long mobilno = userProfile.getPhoneNumber();
                    if (mobilno == null) {
                        mobilenumber = 0L;
                    } else {
                        mobilenumber = mobilno;
                    }
                    countrycode = userProfile.getCountryCode();
                    detail = userProfile.getDescription();
                    if (userProfile.getHasProfileMedia()) {
                        userProfileThumbnail = userProfile.getProfileMedia().getThumbUrl();
                        userProfileUrl = userProfile.getProfileMedia().getMediaUrl();
                    }
                    _detail.setText(detail);
                    _name.setText(firstname);
                    _username.setText(username);
                    _followers.setText(String.valueOf(totalfollowers) + " Follower");
                    _following.setText(String.valueOf(totalfollowing + " Following"));
                    _creations.setText(String.valueOf(totalvideos + " Creations"));
                    coordinatorLayout.setVisibility(View.VISIBLE);

                    if (userProfileThumbnail != null) {
                        Glide.with(context)
                                .load(Uri.parse(userProfileThumbnail))
                                .into(profile_id);
                        profileBlur(userProfileUrl);
                    }

                    progressbar.setVisibility(View.GONE);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Pojos.User.UserProfile> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }

    @AfterPermissionGranted(RC_REQUEST_STORAGE) public void profileBlur(final String pic) {
        String[] perm = new String[] {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(context, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_REQUEST_STORAGE, perm);
        } else {
            progressbar.setVisibility(View.VISIBLE);
            coordinatorLayout.setVisibility(View.GONE);

            new SetBackgroundBlurPic(this).execute(pic);

            progressbar.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
        }
    }

    private static class SetBackgroundBlurPic extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<ProfileFragment> reference;
        private boolean isSavedLocally;

        SetBackgroundBlurPic(ProfileFragment context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            if (isBlurredProfilePicSaved(reference.get().getParentActivity().getApplicationContext())) {
                File file = new File(getBlurredProfilePic(reference.get().getParentActivity().getApplicationContext()));
                isSavedLocally = true;
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            } else {
                try {
                    final URL url = new URL(strings[0]);
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    isSavedLocally = false;
                    return bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        private String saveBlurredPic(Bitmap bitmapImage) {
            ContextWrapper cw = new ContextWrapper(reference.get().getParentActivity().getApplicationContext());
            File directory = cw.getDir("dp", Context.MODE_PRIVATE);
            File imagePath = new File(directory,"profile.png");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(imagePath);

                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imagePath.getAbsolutePath();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            reference.get().progressbar.setVisibility(View.VISIBLE);
            reference.get().coordinatorLayout.setVisibility(View.GONE);

            try {
                if (isSavedLocally) {
                    reference.get().backgroundProfile.setImageBitmap(bitmap);
                } else {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    while (width > 500 && height > 500) {
                        width = width / 2;
                        height = height / 2;
                    }
                    Blurry.with(reference.get().context)
                            .async(new Blurry.ImageComposer.ImageComposerListener() {
                                @Override
                                public void onImageReady(BitmapDrawable drawable) {
                                    SharedPrefs.saveBlurredProfilePic(reference.get().context, saveBlurredPic(drawable.getBitmap()));
                                    reference.get().backgroundProfile.setImageDrawable(drawable);
                                }
                            })
                            .from(Bitmap.createScaledBitmap(bitmap, width, height, false))
                            .into(reference.get().backgroundProfile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            reference.get().progressbar.setVisibility(View.GONE);
            reference.get().coordinatorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FollowerListListener) {
            mListener = (FollowerListListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
       getParentActivity().updateToolbarTitle(previousTitle);
    }

    public interface FollowerListListener {
        void onFollowerListListener(String id,String identifier);
        void onFollowingListListener(String id,String identifier);
    }

    private void dynamicToolbarColor() {
        if (!hasProfleMedia) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                    R.drawable.arif_image);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryDark);
                }
            });
        } else {
        }
    }
}

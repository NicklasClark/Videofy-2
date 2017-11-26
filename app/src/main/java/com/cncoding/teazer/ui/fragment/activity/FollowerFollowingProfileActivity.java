package com.cncoding.teazer.ui.fragment.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowersCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.model.profile.blockuser.BlockUnBlockUser;
import com.cncoding.teazer.model.profile.followerprofile.FollowersProfile;
import com.cncoding.teazer.model.profile.followerprofile.PrivateProfile;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.FollowersProfileCreations;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.Post;
import com.cncoding.teazer.ui.fragment.fragment.ReportUserDialogFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerFollowingProfileActivity extends AppCompatActivity {

    @BindView(R.id.username)
    TextView _usernameTitle;
    @BindView(R.id.creations)
    TextView _creations;

    @BindView(R.id.layoutDetail)
    RelativeLayout layoutDetail;
    ;
    @BindView(R.id.name)
    TextView _name;
    @BindView(R.id.following)
    TextView _following;
    @BindView(R.id.followers)
    TextView _followers;
    @BindView(R.id.recycler_view)
    RecyclerView _recycler_view;
    @BindView(R.id.btnfollow)
    Button _btnfollow;
    @BindView(R.id.layout)
    CoordinatorLayout layout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.nomessagetxt)
    TextView nomessagetxt;
    @BindView(R.id.background_profile)
    ImageView background_profile;
    @BindView(R.id.profile_id)
    ImageView profile_id;
    @BindView(R.id.hobby)
    ProximaNovaRegularCheckedTextView hobby;

    Context context;
    FollowersProfile followersProfile;
    public static final int PRIVATE_ACCOUNT = 1;
    public static final int PUBLIC_ACCOUNT = 2;
    public static final boolean FRIENDS = true;
    public static final boolean NOTFRIEND = false;
    CircularAppCompatImageView menu;
    List<Post> list=new ArrayList<>();
    FollowersCreationAdapter followerCreationAdapter;
    RecyclerView.LayoutManager layoutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private static final int BLOCK_STATUS = 1;
    int accountType;
    boolean requestRecieved;
    String status;
    boolean hassentrequest;
    boolean isBlockedyou;
    boolean isfollower;
    boolean isfollowing;
    private String userProfileThumbnail;
    private String userProfileUrl;
    int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_profile);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }

        context = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrowwhite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();
            }
        });

      //  _btnfollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_follow));
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        menu = findViewById(R.id.menu);
        Intent intent = getIntent();
        final int followerfollowingid = Integer.parseInt(getIntent().getStringExtra("FollowId"));
        String username = intent.getStringExtra("Username");
        String userType = intent.getStringExtra("UserType");
        _btnfollow.setText(userType);

        _btnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_btnfollow.getText().equals("Follow")) {

                    followUser(followerfollowingid, context);

                } else if (_btnfollow.getText().equals("Unfollow") || _btnfollow.getText().equals("Following")) {

                    unFollowUser(followerfollowingid, context);

                } else if (_btnfollow.getText().equals("Requested")) {

                    Toast.makeText(context, "You have already sent the request", Toast.LENGTH_SHORT).show();
                }
            }
        });

        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (accountType == 1) {

                    if (hassentrequest == true) {


                        if (requestRecieved == true) {
                            Intent intent = new Intent(getApplicationContext(), FollowersListActivity.class);
                            intent.putExtra("FollowerId", String.valueOf(followerfollowingid));
                            intent.putExtra("Identifier", "Other");
                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "You can not view following List now", Toast.LENGTH_SHORT).show();
                        }

                    } else
                        Toast.makeText(context, "You can not view following List now", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), FollowingListActivities.class);
                    intent.putExtra("FollowerId", String.valueOf(followerfollowingid));
                    intent.putExtra("Identifier", "Other");
                    startActivity(intent);
                }
            }

        });

        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (accountType == 1) {

                    if (hassentrequest == true) {


                        if (requestRecieved == true) {

                            Intent intent = new Intent(getApplicationContext(), FollowersListActivity.class);
                            intent.putExtra("FollowerId", String.valueOf(followerfollowingid));
                            intent.putExtra("Identifier", "Other");

                            startActivity(intent);
                        } else {
                            Toast.makeText(context, "You can not view follower List now", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "You can not view follower List", Toast.LENGTH_SHORT).show();
                    }

                } else {


                    Intent intent = new Intent(getApplicationContext(), FollowersListActivity.class);
                    intent.putExtra("FollowerId", String.valueOf(followerfollowingid));
                    intent.putExtra("Identifier", "Other");
                    startActivity(intent);
                }

            }
        });
        menu.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, menu);
                popup.inflate(R.menu.menu_other_profile);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_profile_block:
                                openBlockUser(followerfollowingid);
                                break;
                            case R.id.action_profile_report:
                            {
                                FragmentManager fm = getSupportFragmentManager();
                                ReportUserDialogFragment reportUserDialogFragment = ReportUserDialogFragment.newInstance(followerfollowingid);
                                reportUserDialogFragment.show(fm, "fragment_report_user");

                            }

                        }
                        return false;
                    }
                });
                popup.show();
            }

        });

        getProfileInformation(followerfollowingid);


    }

    public void getProfileInformation(final int followersid) {


        ApiCallingService.Friends.getOthersProfileInfo(followersid, context).enqueue(new Callback<FollowersProfile>() {
            @Override
            public void onResponse(Call<FollowersProfile> call, Response<FollowersProfile> response) {
                if (response.code() == 200) {
                    try {

                        FollowersProfile followersProfile = response.body();
                        int follower = followersProfile.getFollowers();
                        int following = followersProfile.getFollowings();
                        int totalvideos = followersProfile.getTotalVideos();
                        hassentrequest = followersProfile.getFollowInfo().getRequestSent();
                        requestRecieved = followersProfile.getFollowInfo().getRequestReceived();
                        isBlockedyou = followersProfile.getFollowInfo().getIsBlockedYou();
                        isfollower = followersProfile.getFollowInfo().getFollower();
                        isfollowing = followersProfile.getFollowInfo().getFollowing();

                        _followers.setText(follower + " Followers");
                        _following.setText(following + " Following");
                        _creations.setText(totalvideos + " Creations");
                        if (response.body().getPrivateProfile() == null) {

                            PublicProfile publicProfile = response.body().getPublicProfile();
                            String username = publicProfile.getUserName();
                            String firstName = publicProfile.getFirstName();
                            String lastName = publicProfile.getLastName();
                            String details=publicProfile.getDescription();
                            int gender = publicProfile.getGender();
                            accountType = publicProfile.getAccountType();
                            _usernameTitle.setText(username);
                            _name.setText(firstName);
                            RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)_usernameTitle.getLayoutParams();
                            if(details==null||details.equals(""))
                            {
                                hobby.setText("");

                            }
                            else {
                                hobby.setText(details);
                            }
                            Boolean hasProfileMedia = publicProfile.getHasProfileMedia();
                            if (hasProfileMedia == true) {

                                userProfileThumbnail = publicProfile.getProfileMedia().getThumbUrl();
                                userProfileUrl = publicProfile.getProfileMedia().getMediaUrl();

                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                                        R.drawable.material_flat);
                                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @SuppressWarnings("ResourceType")
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        int vibrantColor = palette.getVibrantColor(R.color.colorPrimary);
                                        collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                                        collapsingToolbarLayout.setStatusBarScrimColor(R.color.colorPrimaryDark);
                                    }
                                });
                            }
                            if (userProfileThumbnail == null) {
                                final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";

                                Glide.with(context)
                                        .load(pic)
                                        .into(profile_id);
                                profileBlur(pic);
                            } else {

                                Picasso.with(context)
                                        .load(Uri.parse(userProfileThumbnail))
                                        .into(profile_id);
                                profileBlur(userProfileUrl);
                            }


                            getProfileVideos(followersid);


                        } else if (response.body().getPublicProfile() == null) {

                            PrivateProfile privateProfile = response.body().getPrivateProfile();
                            accountType = privateProfile.getAccountType();
                            String username = privateProfile.getUserName();
                            String firstName = privateProfile.getFirstName();
                            String lastName = privateProfile.getLastName();
                            int gender = privateProfile.getGender();
                            Boolean hasProfileMedia = privateProfile.getHasProfileMedia();
                            if (hasProfileMedia) {
                                userProfileThumbnail = privateProfile.getProfileMedia().getMediaUrl();
                                userProfileUrl = privateProfile.getProfileMedia().getThumbUrl();
                            }

                            if (userProfileThumbnail == null) {
                                final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";

                                Glide.with(context)
                                        .load(pic)
                                        .into(profile_id);
                                profileBlur(pic);
                            } else {

                                Picasso.with(context)
                                        .load(Uri.parse(userProfileThumbnail))
                                        .into(profile_id);
                                profileBlur(userProfileUrl);
                            }
                            _usernameTitle.setText(username);
                            _name.setText(firstName);
                            RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)_usernameTitle.getLayoutParams();

                                hobby.setText("");
                                lp.setMargins(0,0,0,20);

                            if (hassentrequest == true) {

                                if (requestRecieved == true) {
                                    //    _btnfollow.getText().equals("Following");
                                } else {

                                    //  _btnfollow.getText().equals("Requested");
                                }
                            } else {


                                // _btnfollow.getText().equals("Follow");

                            }
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        }

                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowersProfile> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });

        layout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);


    }


    public void profileBlur(final String pic) {

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(final Void... params) {
                Bitmap bitmap = null;
                try {
                    final URL url = new URL(pic);
                    try {
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(final Bitmap result) {


                try {
                    Blurry.with(context).from(result).into(background_profile);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }.execute();


//            progressbar.setVisibility(View.GONE);
//            coordinatorLayout.setVisibility(View.VISIBLE);
    }


    public void getProfileVideos(final int followerId) {



        layoutManager = new LinearLayoutManager(context);
        _recycler_view.setLayoutManager(layoutManager);
        Log.d("getProfileVideos", "hello");
        ApiCallingService.Posts.getVideosPostedByFriend(page, followerId, context).enqueue(new Callback<FollowersProfileCreations>() {
            @Override
            public void onResponse(Call<FollowersProfileCreations> call, Response<FollowersProfileCreations> response) {
                Log.d("getProfileVideos", String.valueOf(response));
                if (response.code() == 200) {
                    try {

                        boolean next=response.body().getNextPage();
                        list.addAll(response.body().getPosts());
                        followerCreationAdapter = new FollowersCreationAdapter(context, list);
                        _recycler_view.setAdapter(followerCreationAdapter);

                        if(next)
                        {
                            page++;
                            getProfileVideos(followerId);
                        }
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<FollowersProfileCreations> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void unFollowUser(int userId, Context context)

    {
        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.unfollowUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "User has been unfollowed", Toast.LENGTH_LONG).show();
                            _btnfollow.setText("Follow");

                        } else {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            _btnfollow.setText("Follow");
                            Toast.makeText(getApplicationContext(), "You have already unfollowed", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void followUser(int userId, Context context) {
        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "You have started following", Toast.LENGTH_LONG).show();
                           if(accountType==1)
                           {
                               _btnfollow.setText("Requested");
                               _btnfollow.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_follow));

                           }
                           else
                           {
                               _btnfollow.setText("Unfollow");
                           }


                        } else {
                            layout.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            _btnfollow.setText("Unfollow");
                            Toast.makeText(getApplicationContext(), "You are aleady following", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void openBlockUser(final int blockuserId) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Confirm Block...");
        alertDialog.setMessage("Are you sure you want to block this user");
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                blockunBlock(blockuserId, BLOCK_STATUS);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void openReportUser(final int blockuserId) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Confirm Report...");
        alertDialog.setMessage("Are you sure you want to report this user");
        alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                blockunBlock(blockuserId, BLOCK_STATUS);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void blockunBlock(int userId, int status) {

        layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ApiCallingService.Friends.blockUnblockUser(userId, status, context).enqueue(new Callback<BlockUnBlockUser>() {

            @Override
            public void onResponse(Call<BlockUnBlockUser> call, Response<BlockUnBlockUser> response) {
                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "You have blocked this user", Toast.LENGTH_LONG).show();

                    } else {
                        layout.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Already blocked this user", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BlockUnBlockUser> call, Throwable t) {
                layout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  list.clear();
    }
}

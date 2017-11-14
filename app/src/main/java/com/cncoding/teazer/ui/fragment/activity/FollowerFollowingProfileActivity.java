package com.cncoding.teazer.ui.fragment.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cncoding.teazer.R;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowersCreationAdapter;
import com.cncoding.teazer.adapter.FollowingAdapter;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;

import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.profile.followerprofile.FollowersProfile;
import com.cncoding.teazer.model.profile.followerprofile.PrivateProfile;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.FollowersProfileCreations;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.Post;
import com.cncoding.teazer.model.profile.followerprofile.FollowersProfile;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.FollowersProfileCreations;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.Post;
import com.cncoding.teazer.model.profile.following.ProfileMyFollowing;
import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowerFollowingProfileActivity extends AppCompatActivity {

    @BindView(R.id.username_title)
    TextView _usernameTitle;
    @BindView(R.id.username)
    TextView _username;
    @BindView(R.id.following)
    TextView _following;
    @BindView(R.id.followers)
    TextView _followers;
    @BindView(R.id.recycler_view)
    RecyclerView _recycler_view;
    @BindView(R.id.btnfollow)
    Button _btnfollow;
    Context context;
    FollowersProfile followersProfile;
    public static final int PRIVATE_ACCOUNT = 1;
    public static final int PUBLIC_ACCOUNT = 2;
    public static final boolean FRIENDS = true;
    public static final boolean NOTFRIEND = false;
    CircularAppCompatImageView menu;
    List<Post> list;
    FollowersCreationAdapter followerCreationAdapter;
    RecyclerView.LayoutManager layoutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_profile);
        ButterKnife.bind(this);
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

        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        menu = findViewById(R.id.menu);
        Intent intent = getIntent();
        final int followersid = Integer.parseInt(getIntent().getStringExtra("FollowerId"));
        String username = intent.getStringExtra("username");
        String  userType= intent.getStringExtra("UserType");

        _username.setText(username);
        if(userType.equals("Follower"))
        {
            _btnfollow.setText("Follow");

        }
        else{
            _btnfollow.setText("Unfollow");
        }


        _btnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_btnfollow.getText().equals("Follow"))
                {
                    Toast.makeText(getApplicationContext(), "FollowButton", Toast.LENGTH_LONG).show();
                    followUser(followersid,context);
                }
                else if (_btnfollow.getText().equals("Unfollow"))
                {
                    unFollowUser(followersid,context);

                }
            }
        });

        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FollowingListActivities.class);
                intent.putExtra("FollowerId", String.valueOf(followersid));
                intent.putExtra("Identifier", "Other");
                startActivity(intent);

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, menu);
                popup.inflate(R.menu.menu_other_profile);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_report_block:
                                blockunBlock(followersid);
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        getProfilInformation(followersid);
    }



    public void getProfilInformation(final int followersid) {
        ApiCallingService.Friends.getOthersProfileInfo(followersid, context).enqueue(new Callback<FollowersProfile>() {
            @Override
            public void onResponse(Call<FollowersProfile> call, Response<FollowersProfile> response) {
                if (response.code() == 200) {
                    try {

                        int i = response.body().getAccountType();
                        boolean b = response.body().getCanJoin();
                        int following = response.body().getFollowers();
                        int follower = response.body().getFollowings();

                        _followers.setText(follower + " Followers");
                        _following.setText(following + " Following");

                        if (i == PUBLIC_ACCOUNT) {

                            PublicProfile publicProfile = response.body().getPublicProfile();

                            String username = publicProfile.getUserName();
                            String firstName = publicProfile.getFirstName();
                            String lastName = publicProfile.getLastName();
                            int gender = publicProfile.getGender();
                            _usernameTitle.setText(firstName);
                            _username.setText(username);
                            Boolean hasProfileMedia = publicProfile.getHasProfileMedia();

                            if (!hasProfileMedia) {
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

                            getProfileVideos(followersid);

                        }
                        else if (i == PRIVATE_ACCOUNT) {
                            Toast.makeText(getApplicationContext(), "Private", Toast.LENGTH_LONG).show();
                            PrivateProfile privateProfile = response.body().getPrivateProfile();
                            String username = privateProfile.getUserName();
                            String firstName = privateProfile.getFirstName();
                            String lastName = privateProfile.getLastName();
                            int gender = privateProfile.getGender();
                            _usernameTitle.setText(username);
                            Boolean hasProfileMedia = privateProfile.getHasProfileMedia();

                            if (hasProfileMedia) {
                            } else {
                            }

                            if (b == FRIENDS) {
                                Toast.makeText(getApplicationContext(), "Friends", Toast.LENGTH_LONG).show();
                            } else if (b == NOTFRIEND) {
                                Toast.makeText(getApplicationContext(), "NoTFriend", Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<FollowersProfile> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                Log.d("ShutDown", t.getMessage());
            }

        });
    }

    public void getProfileVideos(int followerId) {
        int page = 1;
        layoutManager = new LinearLayoutManager(context);
        _recycler_view.setLayoutManager(layoutManager);

        ApiCallingService.Posts.getVideosPostedByFriend(page, followerId, context).enqueue(new Callback<FollowersProfileCreations>() {
            @Override
            public void onResponse(Call<FollowersProfileCreations> call, Response<FollowersProfileCreations> response) {
                if (response.code() == 200) {
                    try {
                        list = response.body().getPosts();
                        followerCreationAdapter = new FollowersCreationAdapter(context, list);
                        _recycler_view.setAdapter(followerCreationAdapter);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<FollowersProfileCreations> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                Log.d("ShutDown", t.getMessage());
            }
        });
    }

    // To Block and Unblock the user
    public void blockunBlock(int userId) {

    }

    public void unFollowUser(int userId,Context context)
    {
        ApiCallingService.Friends.unfollowUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true)
                        {
                            Toast.makeText(getApplicationContext(), "User has been unfollowed", Toast.LENGTH_LONG).show();
                            _btnfollow.setText("Follow");
                        }
                        else
                            {
                                Toast.makeText(getApplicationContext(), "User has not been unfollowed,Please try again", Toast.LENGTH_LONG).show();
                            }

                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                Log.d("ShutDown", t.getMessage());
            }
        });

    }

    public void followUser(int userId,Context context)
    {
        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true)
                        {
                            Toast.makeText(getApplicationContext(), "You have started following", Toast.LENGTH_LONG).show();
                            _btnfollow.setText("UnFollow");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "some error, Please try again", Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
                Log.d("ShutDown", t.getMessage());
            }
        });




    }

}

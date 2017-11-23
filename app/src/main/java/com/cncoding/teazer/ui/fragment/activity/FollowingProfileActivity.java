package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.FollowersCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.followerprofile.FollowersProfile;
import com.cncoding.teazer.model.profile.followerprofile.PrivateProfile;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.FollowersProfileCreations;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.Post;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingProfileActivity extends AppCompatActivity {

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

    Context context;
    FollowersProfile followersProfile;
    public static final int PRIVATE_ACCOUNT = 1;
    public static final int PUBLIC_ACCOUNT = 2;
    public static final boolean FRIENDS = true;
    public static final boolean NOTFRIEND = false;

    List<Post> list;
    FollowersCreationAdapter followerCreationAdapter;
    RecyclerView.LayoutManager layoutManager;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_profile);
        ButterKnife.bind(this);
        context = this;
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        Intent intent = getIntent();
        int followersid = Integer.parseInt(getIntent().getStringExtra("FollowerId"));
        String username = intent.getStringExtra("username");
        _username.setText(username);
        getProfilInformation(followersid);

    }

    public void getProfilInformation(final int followersid) {
        ApiCallingService.Friends.getOthersProfileInfo(followersid, context).enqueue(new Callback<FollowersProfile>() {
            @Override
            public void onResponse(Call<FollowersProfile> call, Response<FollowersProfile> response) {
                if (response.code() == 200) {
                    try {

                        int i = response.body().getAccountType();
                        boolean b =false;
                       // boolean b = response.body().getCanJoin();
                        int following = response.body().getFollowers();
                        int follower = response.body().getFollowings();
                        _followers.setText(follower + " Followers");
                        _following.setText(following + " Followers");


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

                        } else if (i == PRIVATE_ACCOUNT) {
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
                        Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<FollowersProfile> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Exception", Toast.LENGTH_LONG).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<FollowersProfileCreations> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
                Log.d("ShutDown", t.getMessage());
            }

        });

    }
}

package com.cncoding.teazer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.customViews.CircularImageView;
import com.cncoding.teazer.authentication.UserProfile;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeScreenActivity extends AppCompatActivity {

    @BindView(R.id.navigation) SpaceNavigationView navigation;
    @BindView(R.id.logout_btn) Button logoutBtn;
    @BindView(R.id.profile_dp) CircularImageView profilePic;
    @BindView(R.id.profile_id) TextView profileId;
    @BindView(R.id.profile_name) TextView profileName;
    @BindView(R.id.profile_email) TextView profileEmail;

    private SpaceOnClickListener spaceOnClickListener = new SpaceOnClickListener() {
        @Override
        public void onCentreButtonClick() {
            Toast.makeText(HomeScreenActivity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClick(int itemIndex, String itemName) {
            Toast.makeText(HomeScreenActivity.this, itemIndex + ": " + itemName, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemReselected(int itemIndex, String itemName) {
            Toast.makeText(HomeScreenActivity.this, itemIndex + ": " + itemName, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);

        prepareUserProfile();

        prepareNavigationItems();

        navigation.setSpaceOnClickListener(spaceOnClickListener);
    }

    private void prepareUserProfile() {
        UserProfile userProfile = new UserProfile(this);
//        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            if (firebaseUser.getEmail() != null && !userProfile.getEmail().equals(""))
//                userProfile.setEmail(firebaseUser.getEmail());
//        }
        profileId.setText(userProfile.getId());
        profileName.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
        profileEmail.setText(userProfile.getEmail());
        if (userProfile.getProfilePicUri() != null) {
            new LoadProfilePic().execute(userProfile.getProfilePicUri());
        }
    }

    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap profilePicture = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                profilePicture = BitmapFactory.decodeStream(in_stream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return profilePicture;
        }

        protected void onPostExecute(Bitmap bitmap) {
            profilePic.setImageBitmap(bitmap);
        }
    }

//    private String[] getFirstAndLastNames(GoogleSignInAccount googleAccount) {
//        String[] name = new String[2];
//        String username;
//        username = googleAccount.getDisplayName();
//        if (username == null) {
//            username = googleAccount.getGivenName();
//        }
//        if (username != null) {
//            name = username.split(" ");
//        }
//        return name;
//    }

    @OnClick(R.id.logout_btn) public void logout() {
        FirebaseAuth.getInstance().signOut();
        logOutOfFacebook();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void logOutOfFacebook() {
        LoginManager loginManager = LoginManager.getInstance();
        if (loginManager != null)
            loginManager.logOut();
    }

    private void prepareNavigationItems() {
//        navigation.initWithSaveInstanceState(savedInstanceState);
        navigation.showIconOnly();
        navigation.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        navigation.addSpaceItem(new SpaceItem("Search", R.drawable.ic_search));
        navigation.addSpaceItem(new SpaceItem("Notifications", R.drawable.ic_notification));
        navigation.addSpaceItem(new SpaceItem("History", R.drawable.ic_history));
    }
}

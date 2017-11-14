package com.cncoding.teazer.ui.fragment.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdate;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdateRequest;
import com.cncoding.teazer.utilities.Pojos;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {

    Context context;
    ImageView bgImage;
    CircularAppCompatImageView profile_image;
    AppBarLayout appBarLayout;
    EditText _username;
    EditText _firstname;
    EditText _lastName;
    EditText _email;
    EditText _mobileNumber;
    EditText _bio;
    RadioButton _male;
    RadioButton _female;
    String username;
    String firstname;
    String lastname;
    int countrycode;
    String emailId;
    long mobilenumber;
    int gender;
    String detail;

    FloatingActionButton fab;
    ProgressBar simpleProgressBar;
    ScrollView layoutdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();
            }
        });

        context = EditProfile.this;
        bgImage = findViewById(R.id.profile_id2);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        layoutdetail = findViewById(R.id.layoutdetail);
        _male = findViewById(R.id.male);
        _female = findViewById(R.id.female);

        _username = findViewById(R.id.username);
        _firstname = findViewById(R.id.firstname);
        profile_image = findViewById(R.id.profile_id);
        _email = findViewById(R.id.email);
        _mobileNumber = findViewById(R.id.mobile);
        _bio = findViewById(R.id.bio);
        fab = findViewById(R.id.fab);
        layoutdetail.setVisibility(View.GONE);

        Intent intent = getIntent();
        username = intent.getStringExtra("UserName");
        firstname = intent.getStringExtra("FirstName");
        lastname = intent.getStringExtra("LastName");
        mobilenumber = Long.parseLong(intent.getStringExtra("MobileNumber"));
        gender = Integer.parseInt(intent.getStringExtra("Gender"));
        emailId = intent.getStringExtra("EmailId");
        countrycode = Integer.parseInt(intent.getStringExtra("CountryCode"));
        detail = intent.getStringExtra("Detail");
        _username.setText(username);
        _firstname.setText(firstname);
        //  _lastName.setText(lastname);
        _bio.setText(detail);
        _email.setText(emailId);
        _mobileNumber.setText(String.valueOf(mobilenumber));


        if (gender == 1) {
            _male.setChecked(true);
        } else {
            _female.setChecked(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String usernames = _username.getText().toString();
                String firstname = _firstname.getText().toString();
                String lastnames = "ABCde";
                int countrycodes = countrycode;
                long mobilenumber = Long.valueOf(_mobileNumber.getText().toString());
                String emailid = _email.getText().toString();
                String details = _bio.getText().toString();
                ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(firstname, lastnames, usernames, emailid, mobilenumber, countrycodes, gender, detail);
                ProfileUpdate(profileUpdateRequest);

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.male:
                if (checked)
                    gender = 1;
                break;
            case R.id.female:
                if (checked)
                    gender = 2;

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";
        Glide.with(context)
                .load(pic)
                .into(profile_image);

        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(final Void... params) {

                Bitmap bitmap = null;
                try {

//             try {
//                        //Drawable pic = ContextCompat.getDrawable(context, R.drawable.arifimage);
//
////                    String pic="https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";
////                     final URL url = new URL(pic);
//                        try {
////                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
////                         bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), pic);
//                            bitmap = BitmapFactory.decodeResource(context.getResources(),
//                                    R.drawable.arifimage);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    catch (Exception e)
//                    {
//
//                    }

                    final URL url = new URL(pic);

                    try {
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {

                }

                return bitmap;
            }

            @Override
            protected void onPostExecute(final Bitmap result) {
                Blurry.with(context).from(result).into(bgImage);
                layoutdetail.setVisibility(View.VISIBLE);
                simpleProgressBar.setVisibility(View.GONE);
            }
        }.execute();


    }


    public void ProfileUpdate(ProfileUpdateRequest profileUpdateRequest) {
        simpleProgressBar.setVisibility(View.VISIBLE);
        layoutdetail.setVisibility(View.GONE);

        ApiCallingService.User.updateUserProfiles(profileUpdateRequest, getApplicationContext()).enqueue(new Callback<ProfileUpdate>() {
            @Override
            public void onResponse(Call<ProfileUpdate> call, Response<ProfileUpdate> response) {


                Log.d("ResponseCode", String.valueOf(response.code()));

                if (response.code() == 200) {

                    try {
                        if (response.body().getStatus()) {

                            Toast.makeText(getApplicationContext(), "Your Profile has been updated", Toast.LENGTH_LONG).show();
                            simpleProgressBar.setVisibility(View.GONE);
                            layoutdetail.setVisibility(View.VISIBLE);
                        } else {

                            Toast.makeText(getApplicationContext(), "Your Profile has not been updated yet", Toast.LENGTH_LONG).show();
                            simpleProgressBar.setVisibility(View.GONE);
                            layoutdetail.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                        Toast.makeText(getApplicationContext(), "Something went wrong Please try again", Toast.LENGTH_LONG).show();
                        simpleProgressBar.setVisibility(View.GONE);
                        layoutdetail.setVisibility(View.VISIBLE);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your data is correct", Toast.LENGTH_LONG).show();
                    simpleProgressBar.setVisibility(View.GONE);
                    layoutdetail.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ProfileUpdate> call, Throwable t) {
                Log.d("Failure", t.getMessage());
                Toast.makeText(getApplicationContext(), "Network Issue Please check once again ", Toast.LENGTH_LONG).show();
                simpleProgressBar.setVisibility(View.GONE);
                layoutdetail.setVisibility(View.VISIBLE);
            }
        });
    }


}







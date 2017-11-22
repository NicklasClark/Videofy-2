package com.cncoding.teazer.ui.fragment.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdate;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdateRequest;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.CommonUtilities.getBitmapFromURL;
import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;

public class EditProfile extends AppCompatActivity implements IPickResult, EasyPermissions.PermissionCallbacks, ProgressRequestBody.UploadCallbacks {

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
    String username;
    String firstname;
    String lastname;
    int countrycode;
    String emailId;
    Long mobilenumber;
    int gender;
    String detail;
    @BindView(R.id.male)
    CircularAppCompatImageView male;
    @BindView(R.id.female)
    CircularAppCompatImageView female;

    @BindView(R.id.maletxt)
    ProximaNovaRegularCheckedTextView maletext;
    @BindView(R.id.femaletxt)
    ProximaNovaRegularCheckedTextView femaletxt;
    FloatingActionButton fab;
    ProgressBar simpleProgressBar;
    ScrollView layoutdetail;

    private static final int RC_REQUEST_STORAGE = 1001;
    private static final int LIMIT = 1;
    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final String TAG = "Edit Profile";
    boolean flag = false;
    private String userProfileThumbnail;
    private String userProfileUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
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
        String mobileno = intent.getStringExtra("MobileNumber");
        userProfileThumbnail =intent.getStringExtra("ProfileThumb");
        userProfileUrl =intent.getStringExtra("ProfileMedia");

        if(mobileno==null) {}
        else
        {
            mobilenumber=Long.parseLong(mobileno);
        }
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
            male.setBackgroundResource(R.drawable.ic_male_sel);
            female.setBackgroundResource(R.drawable.ic_female_white);
            maletext.setTextColor(Color.parseColor("#2196F3"));
            femaletxt.setTextColor(Color.parseColor("#333333"));
        } else {
            female.setBackgroundResource(R.drawable.ic_female_sel);
            male.setBackgroundResource(R.drawable.ic_male_white);
            femaletxt.setTextColor(Color.parseColor("#2196F3"));
            maletext.setTextColor(Color.parseColor("#333333"));
        }

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.ic_male_sel);
                female.setBackgroundResource(R.drawable.ic_female_white);
                maletext.setTextColor(Color.parseColor("#2196F3"));
                femaletxt.setTextColor(Color.parseColor("#333333"));
                gender = 1;
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackgroundResource(R.drawable.ic_female_sel);
                male.setBackgroundResource(R.drawable.ic_male_white);
                femaletxt.setTextColor(Color.parseColor("#2196F3"));
                maletext.setTextColor(Color.parseColor("#333333"));
                gender = 2;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernames = _username.getText().toString();
                String firstname = _firstname.getText().toString();
                String lastnames = "abcdee";
                Integer countrycodes = countrycode;
                Long mobilenumber = Long.valueOf(_mobileNumber.getText().toString());
                String emailid = _email.getText().toString();
                String details = _bio.getText().toString();
                ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(firstname, lastnames, usernames, emailid, mobilenumber, countrycodes, gender, details);
                ProfileUpdate(profileUpdateRequest);
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PickImageDialog.build(new PickSetup()).show(EditProfile.this);
            }
        });


        initProfileImage();

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("MYIMAGES", r.getUri().toString());
            editor.apply();

            SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            final String imageUri =  prfs.getString("MYIMAGES", "");

            try {


                //layoutdetail.setVisibility(View.VISIBLE);
               // simpleProgressBar.setVisibility(View.VISIBLE);

                File profileImage= new File(r.getPath());
                Log.d("Exception1",r.getPath());

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profileImage+".jpg");
                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", profileImage.getName(), reqFile);
                saveDataToDatabase(body);

            }
            catch (Exception e) {
                Log.d("Exception2",e.getMessage());
            }
        } else {

            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_REQUEST_STORAGE)
    public void initProfileImage() {
        String perm = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_REQUEST_STORAGE, perm);
        } else {

            if (userProfileThumbnail == null) {
                final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";

                Glide.with(context)
                        .load(pic)
                        .into(profile_image);
                        profileBlur(pic);
            } else {

                Picasso.with(context)
                        .load(Uri.parse(userProfileThumbnail))
                        .into(profile_image);
//                        profileBlur();
            }

            }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
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
        layoutdetail.setVisibility(View.VISIBLE);
        simpleProgressBar.setVisibility(View.GONE);

    }


    public void ProfileUpdate(ProfileUpdateRequest profileUpdateRequest) {
        simpleProgressBar.setVisibility(View.VISIBLE);
        layoutdetail.setVisibility(View.GONE);

        ApiCallingService.User.updateUserProfiles(profileUpdateRequest, getApplicationContext()).enqueue(new Callback<ProfileUpdate>() {
            @Override
            public void onResponse(Call<ProfileUpdate> call, Response<ProfileUpdate> response) {

                Log.d("on response", String.valueOf(response.toString()));

                if (response.code() == 200) {

                    try {
                        if (response.body().getStatus()) {
                            Toast.makeText(getApplicationContext(), "Your Profile has been updated", Toast.LENGTH_LONG).show();
                            flag = true;
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


    public void saveDataToDatabase(MultipartBody.Part body)

    {

        ApiCallingService.User.updateUserProfileMedia(body, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    Log.d("Response message", response.toString());
                    Log.d("Response", String.valueOf(response.code()));
                    if (response.code() == 200) {

                        String imageUri = "";
                        Log.d("Response", String.valueOf(response.body().getStatus()));
                        Picasso.with(EditProfile.this)
                                .load(imageUri)
                                .into(profile_image);

                        layoutdetail.setVisibility(View.VISIBLE);
                        simpleProgressBar.setVisibility(View.GONE);
                        Blurry.with(EditProfile.this).radius(1).sampling(1).from(getBitmapFromURL(imageUri)).into(bgImage);
                        simpleProgressBar.setVisibility(View.GONE);

                    } else if (response.code() == 400) {
                        Log.d("Response2 ", String.valueOf(response.body().getMessage()));
                    }


                    // Log.d("Response", String.valueOf(response.body().getStatus()));
                    // Log.d("Response", String.valueOf(response.body().getMessage()));


                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Log.d("errror", t.getMessage());
            }
        });

    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onUploadError(Throwable throwable) {


    }

    @Override
    public void onUploadFinish() {
        finishVideoUploadSession(this);

    }


@AfterPermissionGranted(RC_REQUEST_STORAGE)
public void profileBlur(final String pic)
{

    String perm = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    if (!EasyPermissions.hasPermissions(this, perm)) {
        EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                RC_REQUEST_STORAGE, perm);
    }

    else {
        simpleProgressBar.setVisibility(View.VISIBLE);
        layoutdetail.setVisibility(View.GONE);
//            final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";

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
//                Blurry.with(context).from(result).into(backgroundprofile);
                try {
                    Blurry.with(EditProfile.this).radius(1).sampling(1).from(result).into(bgImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                simpleProgressBar.setVisibility(View.GONE);
                layoutdetail.setVisibility(View.VISIBLE);
            }
        }.execute();


        simpleProgressBar.setVisibility(View.GONE);
        layoutdetail.setVisibility(View.VISIBLE);
    }

}
}







package com.cncoding.teazer.ui.fragment.activity;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.Manifest;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.profile.followerprofile.PublicProfile;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdate;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdateRequest;
import com.cncoding.teazer.utilities.FileUtils;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Utility;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;
import com.vansuita.pickimage.listeners.IPickResult;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;
import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;

public class EditProfile extends AppCompatActivity implements IPickResult, EasyPermissions.PermissionCallbacks,ProgressRequestBody.UploadCallbacks{

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
    Long mobilenumber;
    int gender;
    String detail;


    FloatingActionButton fab;
    ProgressBar simpleProgressBar;
    ScrollView layoutdetail;

    private static final int RC_REQUEST_STORAGE = 1001;


    private static final int LIMIT = 1;
    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final String TAG = "Edit Profile";
    boolean flag=false;


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
        String mobileno =intent.getStringExtra("MobileNumber");
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
            _male.setChecked(true);
        } else {
            _female.setChecked(true);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernames = _username.getText().toString();
                String firstname = _firstname.getText().toString();
                String lastnames = "";
                int countrycodes = countrycode;
                long mobilenumber = Long.valueOf(_mobileNumber.getText().toString());
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
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {


            try {


                layoutdetail.setVisibility(View.GONE);
                simpleProgressBar.setVisibility(View.VISIBLE);


//                File file = new File(getPath(r.getUri()));
//
//
//
//
//                File videoFile = new File(String.valueOf(r.getUri()));
//                ProgressRequestBody videoBody = new ProgressRequestBody(videoFile, this);
//                MultipartBody.Part media = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);
//
//                File files= FileUtils.getFile(this,r.getUri());
//                Log.d("Exception111",files.getName());
//                RequestBody requestBody=RequestBody.create(MediaType.parse(getContentResolver().getType(r.getUri())),files);
//                MultipartBody.Part body=MultipartBody.Part.createFormData( "photo",files.getName(),requestBody);
//                saveDataToDatabase(body);

            }
            catch (Exception e) {
               Log.d("Exception12",e.getMessage());
            }
            SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("MYIMAGES", r.getUri().toString());
            editor.apply();

            //SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            final String imageUri =  preferences.getString("MYIMAGES", null);
            Picasso.with(this)
                    .load(Uri.parse(imageUri))
                    .into(profile_image);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imageUri));
                Blurry.with(this).radius(1).sampling(1).from(bitmap).into(bgImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            layoutdetail.setVisibility(View.VISIBLE);
            simpleProgressBar.setVisibility(View.GONE);
        } else {

            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public byte[] getBytes(InputStream inputStream) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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
        }

        else {
            SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            String imageUri =  prfs.getString("MYIMAGES", null);
            if(imageUri==null)
            {

                final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";
                Glide.with(context)
                        .load(pic)
                        .into(profile_image);
            }
            else
            {

                Picasso.with(this)
                        .load(Uri.parse(imageUri))
                        .into(profile_image);


//
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(imageUri));
//                    Blurry.with(this).radius(1).sampling(1).from(bitmap).into(bgImage);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }




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


//        new AsyncTask<Void, Void, Bitmap>() {
//            @Override
//            protected Bitmap doInBackground(final Void... params) {
//
//                Bitmap bitmap = null;
//                try {
//
//
//                    final URL url = new URL(pic);
//
//                    try {
//                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (Exception e) {
//
//                }
//
//                return bitmap;
//            }
//
//            @Override
//            protected void onPostExecute(final Bitmap result) {
//                Blurry.with(context).from(result).into(bgImage);
//                layoutdetail.setVisibility(View.VISIBLE);
//                simpleProgressBar.setVisibility(View.GONE);
//            }
//        }.execute();
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
                Log.d("ResponseCode", String.valueOf(response.toString()));

                if (response.code() == 200) {

                    try {
                        if (response.body().getStatus()) {

                            Toast.makeText(getApplicationContext(), "Your Profile has been updated", Toast.LENGTH_LONG).show();

                            flag=true;
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
                    if(response.code()==200)
                    {
                        Log.d("Response", String.valueOf(response.body().getStatus()));


                    }
                    else if(response.code()==400)
                    {
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


//    public void saveDataToServer(Uri uri) {
//
//        String filePath = getRealPathFromUri(uri);
//        if (filePath != null && !filePath.isEmpty()) {
//            File file = new File(filePath);
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            // MultipartBody.Part is used to send also the actual filename
//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//            // adds another part within the multipart request
//            String descriptionString = "Sample description";
//            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
//
//
//            ApiCallingService.User.updateUserProfileMedia(body, context).enqueue(new Callback<ResultObject>() {
//                @Override
//                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
//                    try {
//                        Log.d("Response", response.toString());
//
//                        Log.d("Response", String.valueOf(response.code()));
//                        if(response.code()==200)
//                        {
//                             Log.d("Response", String.valueOf(response.body().getStatus()));
//
//
//                        }
//                        else if(response.code()==400)
//                        {
//                            Log.d("Response2 ", String.valueOf(response.body().getMessage()));
//                        }
//
//
//                       // Log.d("Response", String.valueOf(response.body().getStatus()));
//                       // Log.d("Response", String.valueOf(response.body().getMessage()));
//
//
//                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
//
//                    } catch (Exception e) {
//                        Log.d("Exception", e.getMessage());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResultObject> call, Throwable t) {
//
//                    Log.d("errror", t.getMessage());
//                }
//            });
//
//        }
//    }
//
//
//        public String getRealPathFromUri(final Uri uri) {
//        // DocumentProvider
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
//            }
//            // DownloadsProvider
//            else if (isDownloadsDocument(uri)) {
//
//                final String id = DocumentsContract.getDocumentId(uri);
//                final Uri contentUri = ContentUris.withAppendedId(
//                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                return getDataColumn(context, contentUri, null, null);
//            }
//            // MediaProvider
//            else if (isMediaDocument(uri)) {
//                final String docId = DocumentsContract.getDocumentId(uri);
//                final String[] split = docId.split(":");
//                final String type = split[0];
//
//                Uri contentUri = null;
//                if ("image".equals(type)) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                } else if ("video".equals(type)) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                } else if ("audio".equals(type)) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                }
//
//                final String selection = "_id=?";
//                final String[] selectionArgs = new String[]{
//                        split[1]
//                };
//
//                return getDataColumn(context, contentUri, selection, selectionArgs);
//            }
//        }
//        // MediaStore (and general)
//        else if ("content".equalsIgnoreCase(uri.getScheme())) {
//
//            // Return the remote address
//            if (isGooglePhotosUri(uri))
//                return uri.getLastPathSegment();
//
//            return getDataColumn(context, uri, null, null);
//        }
//        // File
//        else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//
//        return null;
//    }
//
//
//    private String getDataColumn(Context context, Uri uri, String selection,
//                                 String[] selectionArgs) {
//
//        Cursor cursor = null;
//        final String column = "_data";
//        final String[] projection = {
//                column
//        };
//
//        try {
//            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                    null);
//            if (cursor != null && cursor.moveToFirst()) {
//                final int index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(index);
//            }
//        } finally {
//            if (cursor != null)
//                cursor.close();
//        }
//        return null;
//    }
//
//    private boolean isExternalStorageDocument(Uri uri) {
//        return "com.android.externalstorage.documents".equals(uri.getAuthority());
//    }
//
//    private boolean isDownloadsDocument(Uri uri) {
//        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//    }
//
//    private boolean isMediaDocument(Uri uri) {
//        return "com.android.providers.media.documents".equals(uri.getAuthority());
//    }
//
//    private boolean isGooglePhotosUri(Uri uri) {
//        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
//    }

    }







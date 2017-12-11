package com.cncoding.teazer.ui.fragment.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.bumptech.glide.Glide;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdate;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdateRequest;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
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

import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;


public class EditProfile extends AppCompatActivity implements IPickResult, EasyPermissions.PermissionCallbacks, ProgressRequestBody.UploadCallbacks {

    private static final int RC_CAMERA = 11;
    private static final Object IMAGE_DIRECTORY = 22;
    Context context;
    ImageView bgImage;
    CircularAppCompatImageView profile_image;
    AppBarLayout appBarLayout;
    TextInputEditText _username;
    TextInputEditText _firstname;
    TextInputEditText _email;
    TextInputEditText _mobileNumber;
    TextInputEditText _bio;
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

    Button fab;
    ProgressBar simpleProgressBar;
    RelativeLayout layoutdetail;

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
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);

                onBackPressed();
            }
        });


        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>Edit Profile</font>"));
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
        userProfileThumbnail = intent.getStringExtra("ProfileThumb");
        userProfileUrl = intent.getStringExtra("ProfileMedia");

        if (mobileno == null) {
        } else {
            mobilenumber = Long.parseLong(mobileno);
        }

        gender = Integer.parseInt(intent.getStringExtra("Gender"));
        emailId = intent.getStringExtra("EmailId");
        countrycode = Integer.parseInt(intent.getStringExtra("CountryCode"));
        detail = intent.getStringExtra("Detail");
        _username.setText(username);
        _firstname.setText(firstname);
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
            femaletxt.setTextColor(Color.parseColor("#F48fb1"));
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
                femaletxt.setTextColor(Color.parseColor("#F48fb1"));
                maletext.setTextColor(Color.parseColor("#333333"));
                gender = 2;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment.checkprofileupdated = true;
                PickImageDialog.build(new PickSetup()).show(EditProfile.this);
                //showPictureDialog();


            }
        });

        initProfileImage();

    }


    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            try {

                ProfileFragment.checkprofileupdated = true;

//                SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
//                final String imageUri = prfs.getString("MYIMAGES", "");
//               Picasso.with(EditProfile.this)
//                        .load(r.getUri())
//                        .into(profile_image);


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(EditProfile.this.getContentResolver(), r.getUri());
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);


//                Bitmap photobitmap = Bitmap.createBitmap(scaledBitmap, scaledBitmap.getWidth() / 2, scaledBitmap.getHeight() / 2, 100, 100);
//                Bitmap dstBmp;
//                int dimension = getSquareCropDimensionForBitmap(photobitmap);
//                dstBmp = ThumbnailUtils.extractThumbnail(bitmap, 200, 200);


                Bitmap b = getCorrectlyOrientedImage(context, r.getUri(), 400);
                profile_image.setImageBitmap(b);
                bgImage.setImageBitmap(b);
                byte[] bte = bitmaptoByte(b);
                Blurry.with(EditProfile.this).from(b).into(bgImage);

                SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("MYIMAGES", r.getUri().toString());
                editor.apply();

                File profileImage = new File(r.getPath());
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), bte);
                MultipartBody.Part body = MultipartBody.Part.createFormData("media", "profile_image.jpg", reqFile);
                saveDataToDatabase(body);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "oops something went wrong, please try again", Toast.LENGTH_LONG).show();
        }
    }


//    public void choosePhotoFromGallary() {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(galleryIntent, 0);
//    }
//
//    @AfterPermissionGranted(RC_CAMERA)
//    private void takePhotoFromCamera() {
//        String[] perms = {Manifest.permission.CAMERA};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, 1);
//
//
//        } else {
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(this, context.getString(R.string.camera_rationale),
//                    RC_CAMERA, perms);
//        }
//    }
//
//    private void showPictureDialog() {
//        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
//        pictureDialog.setTitle("Select Action");
//        String[] pictureDialogItems = {
//                "Select photo from gallery",
//                "Capture photo from camera"};
//        pictureDialog.setItems(pictureDialogItems,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        switch (i) {
//                            case 0:
//                                choosePhotoFromGallary();
//                                break;
//                            case 1:
//                                takePhotoFromCamera();
//                                break;
//                        }
//                    }
//                });
//
//        pictureDialog.show();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == this.RESULT_CANCELED) {
//            return;
//        }
//        if (requestCode == 0) {
//            if (data != null) {
//                Uri contentURI = data.getData();
//                try {
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
//                    Toast.makeText(EditProfile.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//
//                    Bitmap b = getCorrectlyOrientedImage(context, contentURI, 200);
//                    profile_image.setImageBitmap(b);
//                    //  bgImage.setImageBitmap(b);
//
//
//                    byte[] bte = bitmaptoByte(b);
//                    Blurry.with(EditProfile.this).from(b).into(bgImage);
//
//                    SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("MYIMAGES", contentURI.toString());
//                    editor.apply();
//
//                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), bte);
//                    MultipartBody.Part body = MultipartBody.Part.createFormData("media", "profile_image.jpg", reqFile);
//                    saveDataToDatabase(body);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        } else if (requestCode == 1) {
//
//
//            Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
//            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//            byte[] b = bitmaptoByte(thumbnail);
//            //Uri imageur = getImageUri(context, thumbnail);
//
//
//            // Toast.makeText(context, String.valueOf(imageur), Toast.LENGTH_LONG).show();
//            String s = saveImage(thumbnail);
//
//            try {
//
//                Bitmap bitmap = getCorrectlyOrientedImage(context, Uri.parse(s), 200);
//                profile_image.setImageBitmap(bitmap);
//                bgImage.setImageBitmap(bitmap);
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//
//
//            Toast.makeText(EditProfile.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//
//        }
//    }





    public static int getOrientation(Context context, Uri photoUri) {

        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{MediaStore.Images.ImageColumns.ORIENTATION}, null, null, null);

        if (cursor == null || cursor.getCount() != 1) {
            return 90;  //Assuming it was taken portrait
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    /**
     * Rotates and shrinks as needed
     */
    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri, int maxWidth)
            throws IOException {

        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();


        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            Log.d("ImageUtil", "Will be rotated");
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        Log.d("ImageUtil", String.format("rotatedWidth=%s, rotatedHeight=%s, maxWidth=%s",
                rotatedWidth, rotatedHeight, maxWidth));
        if (rotatedWidth > maxWidth || rotatedHeight > maxWidth) {
            float widthRatio = ((float) rotatedWidth) / ((float) maxWidth);
            float heightRatio = ((float) rotatedHeight) / ((float) maxWidth);
            float maxRatio = Math.max(widthRatio, heightRatio);
            Log.d("ImageUtil", String.format("Shrinking. maxRatio=%s",
                    maxRatio));

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            Log.d("ImageUtil", String.format("No need for Shrinking. maxRatio=%s",
                    1));

            srcBitmap = BitmapFactory.decodeStream(is);
            Log.d("ImageUtil", String.format("Decoded bitmap successful"));
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }


    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public static byte[] bitmaptoByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        return data;
    }

//
//    public Uri getImageUri(Context inContext, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
//
//    public String saveImage(Bitmap myBitmap) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//        File wallpaperDirectory = new File(
//                String.valueOf(Environment.getExternalStorageDirectory()));
//        // have the object build the directory structure, if needed.
//        if (!wallpaperDirectory.exists()) {
//            wallpaperDirectory.mkdirs();
//        }
//
//        try {
//            File f = new File(wallpaperDirectory, Calendar.getInstance()
//                    .getTimeInMillis() + ".jpg");
//            f.createNewFile();
//            FileOutputStream fo = new FileOutputStream(f);
//            fo.write(bytes.toByteArray());
//            MediaScannerConnection.scanFile(this,
//                    new String[]{f.getPath()},
//                    new String[]{"image/jpeg"}, null);
//            fo.close();
//            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
//
//            return f.getAbsolutePath();
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        return "";
//    }


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

            if (userProfileUrl == null) {

            } else {

                Glide.with(context)
                        .load(Uri.parse(userProfileUrl))
                        .into(profile_image);
                profileBlur(userProfileUrl);
            }

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
       // takePhotoFromCamera();
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
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


                if (response.code() == 200) {

                    try {
                        if (response.body().getStatus()) {
                            Toast.makeText(getApplicationContext(), "Your Profile has been updated", Toast.LENGTH_LONG).show();
                            simpleProgressBar.setVisibility(View.GONE);
                            layoutdetail.setVisibility(View.VISIBLE);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Your Profile has not been updated yet", Toast.LENGTH_LONG).show();
                            simpleProgressBar.setVisibility(View.GONE);
                            layoutdetail.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network Issue Please check once again ", Toast.LENGTH_LONG).show();
                simpleProgressBar.setVisibility(View.GONE);
                layoutdetail.setVisibility(View.VISIBLE);
            }
        });
    }


    public void saveDataToDatabase(MultipartBody.Part body) {
        simpleProgressBar.setVisibility(View.VISIBLE);
        // layoutdetail.setVisibility(View.GONE);
        profile_image.setClickable(false);

        ApiCallingService.User.updateUserProfileMedia(body, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {

                    simpleProgressBar.setVisibility(View.GONE);
                    //layoutdetail.setVisibility(View.VISIBLE);
                    profile_image.setClickable(true);
                    if (response.code() == 400) {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                simpleProgressBar.setVisibility(View.GONE);
                layoutdetail.setVisibility(View.VISIBLE);
                t.printStackTrace();
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
    public void profileBlur(final String pic) {

        String perm = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_REQUEST_STORAGE, perm);
        } else {
            simpleProgressBar.setVisibility(View.VISIBLE);
            layoutdetail.setVisibility(View.GONE);
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

                        Bitmap photobitmap = Bitmap.createScaledBitmap(result,
                                300, 300, false);
                        Blurry.with(EditProfile.this).from(photobitmap).into(bgImage);


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


    public void validate() {
        boolean valid = true;
        String usernames = _username.getText().toString();
        String firstname = _firstname.getText().toString();
        String lastnames = lastname;
        Integer countrycodes = countrycode;
        String mobilenumber = _mobileNumber.getText().toString();
        String emailid = _email.getText().toString();
        String details = _bio.getText().toString();

        if (usernames.isEmpty()) {
            _username.setError("enter username");
            _username.requestFocus();
            valid = false;

        } else {
            _username.setError(null);
        }
        if (firstname.isEmpty()) {
            _firstname.setError("enter your name");
            _firstname.requestFocus();
            valid = false;

        } else {
            _firstname.setError(null);
        }
        if (emailid.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailid).matches() || emailid.isEmpty()) {
            _email.setError("enter a email address");
            _email.requestFocus();
            valid = false;

        } else {
            _email.setError(null);
        }


        if (mobilenumber.isEmpty() || mobilenumber.length() < 10 || mobilenumber.length() > 10) {
            _mobileNumber.setError("enter valid 10 digit mobile number");
            _mobileNumber.requestFocus();
            valid = false;

        } else {
            _mobileNumber.setError(null);

        }

        if (details.isEmpty()) {
            _bio.setError("Bio is required");
            _bio.requestFocus();
            valid = false;

        } else {
            _bio.setError(null);
        }


        if (valid) {
            ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(firstname, lastnames, usernames, emailid, Long.parseLong(mobilenumber), countrycodes, gender, details);
            ProfileUpdate(profileUpdateRequest);
        } else {

        }


    }


}









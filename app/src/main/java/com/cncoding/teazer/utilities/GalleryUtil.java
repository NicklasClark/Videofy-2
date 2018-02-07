package com.cncoding.teazer.utilities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.cncoding.teazer.R;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 *
 * Created by Prem $ on 12/5/2017.
 */

public class GalleryUtil extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private final static int RESULT_SELECT_IMAGE = 100;
//    private static final String TAG = "GalleryUtil";
    private static final int PERMISSION_REQUEST = 101;

    private String[] PERMISSIONS = new String[] {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA};
//    String mCurrentPhotoPath;
//    File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            //Pick Image From Gallery or camera
            startImagePickerIntent();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.no_app_found_for_intent, Toast.LENGTH_SHORT).show();
        }
    }

    @AfterPermissionGranted(PERMISSION_REQUEST) private void startImagePickerIntent() {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            startActivityForResult(
                    Intent.createChooser(
                            new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                            "Select profile pic"), RESULT_SELECT_IMAGE);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    PERMISSION_REQUEST, PERMISSIONS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case RESULT_SELECT_IMAGE:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    try{
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA };
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            cursor.close();

                            //return Image Path to the Main Activity
                            Intent returnFromGalleryIntent = new Intent();
                            returnFromGalleryIntent.putExtra("picturePath", picturePath);
                            setResult(RESULT_OK, returnFromGalleryIntent);
                            finish();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        Intent returnFromGalleryIntent = new Intent();
                        setResult(RESULT_CANCELED, returnFromGalleryIntent);
                        finish();
                    }
                }
                else{
//                    Log.i(TAG,"RESULT_CANCELED");
                    Intent returnFromGalleryIntent = new Intent();
                    setResult(RESULT_CANCELED, returnFromGalleryIntent);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == PERMISSION_REQUEST) {
            startImagePickerIntent();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
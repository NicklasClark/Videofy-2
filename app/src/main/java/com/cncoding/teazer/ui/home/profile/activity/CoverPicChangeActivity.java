package com.cncoding.teazer.ui.home.profile.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.model.profile.CoverImageResponse;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.model.profile.DefaultCoverMedia;
import com.cncoding.teazer.ui.customviews.common.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.ui.home.profile.adapter.ChangeCoverPhotoAdapter;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoverPicChangeActivity extends AppCompatActivity {


    Context context;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progress_bar;
    ChangeCoverPhotoAdapter changeCoverPhotoAdapter;
    boolean next = false;
    List<Object> coverPicList;
    List<DefaultCoverMedia> defaultcoverpiclist;
    @BindView(R.id.backbutton)
    ImageView backbutton;
    public static String coverPicUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover_pic_change);
        ButterKnife.bind(this);
        context=this;
        recyclerView = findViewById(R.id.recycler_view);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });




        layoutManager = new GridLayoutManager(CoverPicChangeActivity.this,2);
        recyclerView.setLayoutManager(layoutManager);


        getDefaultCoverPic(1);





        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (next) {
                    getDefaultCoverPic(page);

                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

    }

    public void changeCoverPic()
    {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setFixAspectRatio(true)
                .start(CoverPicChangeActivity.this);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(CoverPicChangeActivity.this.getContentResolver(), resultUri);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
                    byte[] bte = bitmaptoByte(scaledBitmap);

                    SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("MYIMAGES", resultUri.toString());
                    editor.apply();

                    // File profileImage = new File(r.getPath());
                    RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), bte);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("media", "cover_image.jpg", reqFile);

                    saveDataToDatabase(body);


                }
                catch (Exception e)
                {

                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }

    public static byte[] bitmaptoByte(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }
        return baos.toByteArray();
    }


    public void saveDataToDatabase(MultipartBody.Part body) {

        ApiCallingService.User.updateUserProfileCoverMedia(body, context).enqueue(new Callback<CoverImageResponse>() {
            @Override
            public void onResponse(Call<CoverImageResponse> call, Response<CoverImageResponse> response) {
                try {
                    Toast.makeText(context,"Your cover pic has been uploaded successfully",Toast.LENGTH_SHORT).show();
                    FragmentNewProfile2.checkprofileupdated=true;
                    coverPicUrl =response.body().getProfileCoverImage().getMediaUrl();
                    finish();




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Profile pic uploading failed, please try again",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CoverImageResponse> call, Throwable t) {

                Toast.makeText(context,"Profile pic uploading failed, please try again",Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }


    public void getDefaultCoverPic(final int page)
    {

        ApiCallingService.Application.getDefaultcoverImages(context,page).enqueue(new Callback<DefaultCoverImageResponse>() {
            @Override
            public void onResponse(Call<DefaultCoverImageResponse> call, Response<DefaultCoverImageResponse> response) {
                try {
                 //   FragmentNewProfile2.checkprofileupdated=true;

                    next = response.body().getNextPage();
                    defaultcoverpiclist =response.body().getDefaultCoverMedias();
                    defaultcoverpiclist.add(new DefaultCoverMedia(-101,"","","",null));
                    changeCoverPhotoAdapter=new ChangeCoverPhotoAdapter(defaultcoverpiclist,context,CoverPicChangeActivity.this);
                    recyclerView.setAdapter(changeCoverPhotoAdapter);
                    changeCoverPhotoAdapter.notifyDataSetChanged();
                    changeCoverPhotoAdapter.notifyItemRangeInserted(changeCoverPhotoAdapter.getItemCount(), defaultcoverpiclist.size() - 1);

                    //    FragmentNewProfile2.checkprofileupdated=true;
                //    finish();




                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context,"Profile pic uploading failed, please try again",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DefaultCoverImageResponse> call, Throwable t) {

                Toast.makeText(context,"Profile pic uploading failed, please try again",Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void finishActivity()
    {
        finish();

    }


}

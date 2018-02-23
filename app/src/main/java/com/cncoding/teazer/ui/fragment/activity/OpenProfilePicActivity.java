package com.cncoding.teazer.ui.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.profile.ProfileFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenProfilePicActivity extends AppCompatActivity {

    @BindView(R.id.media_controller_dp)
    CircularAppCompatImageView media_controller_dp;
    @BindView(R.id.remove_image_btn)
    ProximaNovaSemiBoldTextView remove_image_btn;
    int gender;

    @BindView(R.id.close)
    CircularAppCompatImageView close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_profile_pic);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
           getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        ButterKnife.bind(this);
        Intent intent=getIntent();
        String url=intent.getStringExtra("Image");
        boolean ismyself=intent.getExtras().getBoolean("candelete");
         gender=intent.getExtras().getInt("gender");


        if(ismyself&&url!=null)
        {
            remove_image_btn.setVisibility(View.VISIBLE);
        }

        if(url!=null){
        Glide.with(getApplicationContext())
                .load(url)
                .into(media_controller_dp);
        }
        else
        {

            if(gender==1)
            {
                Glide.with(getApplicationContext())
                        .load(R.drawable.ic_user_male_dp)
                        .into(media_controller_dp);

            }
            else if(gender==2)
            {
                Glide.with(getApplicationContext())
                        .load(R.drawable.ic_user_female_dp)
                        .into(media_controller_dp);

            }
            else
            {
                Glide.with(getApplicationContext())
                        .load(R.drawable.ic_user_male_dp)
                        .into(media_controller_dp);

            }

        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        remove_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                removeProfilePic();


            }
        });


    }
    public void removeProfilePic()

    {


        ApiCallingService.User.removeProfilePicture(getApplicationContext()).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {

                    try {
                        if (response.body().getStatus()) {
                            Toast.makeText(getApplicationContext(), "Your Profile pic has been removed", Toast.LENGTH_SHORT).show();
                            ProfileFragment.checkprofileupdated = true;
                            ProfileFragment. checkpicUpdated = true;

                            if(gender==1)
                            {
                                Glide.with(getApplicationContext())
                                        .load(R.drawable.ic_user_male_dp)
                                        .into(media_controller_dp);

                            }
                            else if(gender==2)
                            {
                                Glide.with(getApplicationContext())
                                        .load(R.drawable.ic_user_female_dp)
                                        .into(media_controller_dp);

                            }
                            else
                            {
                                Glide.with(getApplicationContext())
                                        .load(R.drawable.ic_user_male_dp)
                                        .into(media_controller_dp);

                            }

                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong Please try again", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your data is correct", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network Issue Please check once again ", Toast.LENGTH_LONG).show();

            }
        });
    }

}

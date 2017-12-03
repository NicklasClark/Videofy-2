package com.cncoding.teazer.ui.fragment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.logout;

public class Settings extends AppCompatActivity {

    @BindView(R.id.text_block_layout)
    LinearLayout text_block;
    @BindView(R.id.simpleSwitch)
    Switch simpleSwitch;

    @BindView(R.id.logoutlayout)
    LinearLayout logout;
    @BindView(R.id.changePassword)
    TextView changePassword;

    @BindView(R.id.invitefriendslayout)
    LinearLayout invitefriendslayout;
    Context context;
    private static final  int PRIVATE_STATUS=1;
    private static final  int PUBLIC_STATUS=2;
    boolean flag =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        toolbar.setNavigationIcon(R.drawable.ic_previous);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);

                    onBackPressed();
            }
        });

        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Settings</font>"));
        Intent intent=getIntent();
        int accoutType=Integer.parseInt(intent.getStringExtra("AccountType"));
        if(accoutType==1)
        {
            simpleSwitch.setChecked(true);
        }
        else
        {
            simpleSwitch.setChecked(false);
        }

        text_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),BlockUserList.class);
                startActivity(intent);
            }
        });

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    flag=true;
                    publicprivateProfile(PRIVATE_STATUS);
                } else {
                    flag=true;
                    publicprivateProfile(PUBLIC_STATUS);
                }
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(getApplicationContext(), Settings.this);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.this,PasswordChange.class);
                startActivity(intent);
            }
        });
        invitefriendslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.this,InviteFriend.class);
                startActivity(intent);
            }
        });
    }
    public void publicprivateProfile(final int status)
    {
        ApiCallingService.User.setAccountVisibility(status, context).enqueue(new Callback<ResultObject>() {

            @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                        try {
                            boolean b = response.body().getStatus();
                            if (b == true)
                            {

                                if(status==1)
                                 Toast.makeText(getApplicationContext(), "Your account has become private", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getApplicationContext(), "Your account has become public", Toast.LENGTH_LONG).show();

                            }
                            else
                            {

                                Toast.makeText(getApplicationContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (Exception e) {

                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                        }

                    }



                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

                }
            });


    }

}

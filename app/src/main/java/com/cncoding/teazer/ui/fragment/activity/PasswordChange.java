package com.cncoding.teazer.ui.fragment.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cncoding.teazer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordChange extends AppCompatActivity {

    @BindView(R.id.currentPassword)
    EditText currentPassword;
    @BindView(R.id.newPassword)
    EditText newPassword;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.save)
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#FFFFFF'>Change Password</font>"));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();

            }
        });


    }



    public boolean validate() {
        boolean valid = true;

        String currentPass = currentPassword.getText().toString();
        String newPass = newPassword.getText().toString();
        String confirmpass = confirmPassword.getText().toString();

        if (currentPass.isEmpty()||currentPass==null ||currentPass.length()==0) {
            currentPassword.setError("You have not entered your current password");
            currentPassword.requestFocus();
            valid = false;
            return valid;
        } else {

            if(currentPass.equals("mrajsingh"))
            {
                currentPassword.setError(null);
            }
            else
            {
                currentPassword.setError("Password is correct password");
                currentPassword.requestFocus();
                valid = false;
                return valid;
            }
        }
        if (newPass.isEmpty() || newPass==null || newPass.length()==0  ||  newPass.length()< 8) {
            newPassword.setError("Your password should be more than 8 charecters");
            newPassword.requestFocus();
            valid = false;
        }
        else {
            newPassword.setError(null);
        }

        if (confirmpass.isEmpty() || confirmpass==null || confirmpass.length()==0) {
            confirmPassword.setError("Confirm pass word can not be empty");
            confirmPassword.requestFocus();
            valid = false;
        }
        else {

            if (confirmpass.equals(newPass)) {
                confirmPassword.setError(null);
            }
            else
            {
                confirmPassword.setError("Confirm password and New Password should be same");
                confirmPassword.requestFocus();
                valid = false;
            }

        }


        return valid;
    }
}

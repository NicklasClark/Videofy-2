package com.cncoding.teazer.ui.fragment.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextInputEditText;
import com.cncoding.teazer.data.model.user.SetPasswordRequest;
import com.cncoding.teazer.data.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.data.model.user.UserProfile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.SharedPrefs.getCurrentPassword;
import static com.cncoding.teazer.utilities.SharedPrefs.setCurrentPassword;

public class PasswordChange extends AppCompatActivity {

    @BindView(R.id.currentPassword)
    ProximaNovaRegularTextInputEditText currentPassword;
    @BindView(R.id.newPassword)
    ProximaNovaRegularTextInputEditText newPassword;
    @BindView(R.id.confirmPassword)
    ProximaNovaRegularTextInputEditText confirmPassword;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.currentPasswordLayout)
    LinearLayout currentPasswordLayout;
    private boolean canChangePassword;
    private String confirmPass;
    private String newPass;
    private String currentPass;
    private String oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Change Password</font>"));

        currentPasswordLayout.setVisibility(View.GONE);
        getProfileDetail();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    if (canChangePassword) {
                        updateNewPassword(new UpdatePasswordRequest(currentPass, newPass));
                    } else {
                        setNewPassword(new SetPasswordRequest(newPass));
                    }
                }

            }
        });
        save.setEnabled(false);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPassword.requestFocus();
                currentPassword.getText().clear();
                newPassword.getText().clear();
                confirmPassword.getText().clear();


            }
        });
    }



    public boolean validate() {
        boolean valid = true;

        currentPass = currentPassword.getText().toString();
        newPass = newPassword.getText().toString();
        confirmPass = confirmPassword.getText().toString();

        if (canChangePassword) {
            if (currentPass.isEmpty() || currentPass.length() == 0) {
                currentPassword.setError("You have not entered your current password");
                currentPassword.requestFocus();
                valid = false;
                return valid;
            }
            if (!currentPass.equals(oldPassword)) {
                currentPassword.setError("Incorrect password");
                currentPassword.requestFocus();
                valid = false;
                return valid;
            }
        }
        if (newPass.isEmpty() || newPass.length() == 0 || newPass.length() < 8) {
            newPassword.setError("Your password should be more than 8 characters");
            newPassword.requestFocus();
            valid = false;
            return valid;
        } else {
            newPassword.setError(null);
        }

        if (confirmPass.isEmpty() || confirmPass.length() == 0) {
            confirmPassword.setError("Confirm pass word can not be empty");
            confirmPassword.requestFocus();
            valid = false;
            return valid;
        }

        if (confirmPass.equals(newPass)) {
            confirmPassword.setError(null);
        } else {
            confirmPassword.setError("Confirm password and New Password should be same");
            confirmPassword.requestFocus();
            valid = false;
            return valid;
        }
        return valid;
    }

    public void getProfileDetail() {
        currentPasswordLayout.setVisibility(View.GONE);
        ApiCallingService.User.getUserProfile(this).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {
                    UserProfile userProfile = response.body();
                    if (userProfile != null) {
                        canChangePassword = userProfile.canChangePassword();
                        if (canChangePassword) {
                            currentPasswordLayout.setVisibility(View.VISIBLE);
                            oldPassword = getCurrentPassword(PasswordChange.this);
                            currentPassword.requestFocus();
                        } else {
                            currentPasswordLayout.setVisibility(View.GONE);
                            newPassword.requestFocus();
                        }

                        save.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void setNewPassword(final SetPasswordRequest setPasswordRequest) {
        ApiCallingService.User.setPassword(setPasswordRequest, this).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    if (response.code() == 200) {
                        setCurrentPassword(PasswordChange.this, setPasswordRequest.getNewPassword());
                        Toast.makeText(PasswordChange.this, "Password has been created", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PasswordChange.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(PasswordChange.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                finish();
            }
        });
    }

    public void updateNewPassword(final UpdatePasswordRequest updatePasswordRequest) {
        ApiCallingService.User.updatePassword(updatePasswordRequest, this).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    if (response.code() == 200) {
                        Toast.makeText(PasswordChange.this, "Password changed", Toast.LENGTH_SHORT).show();
                        setCurrentPassword(PasswordChange.this, updatePasswordRequest.getNewPassword());
                    }
                } catch (Exception e) {
                    Toast.makeText(PasswordChange.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                finish();
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(PasswordChange.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @OnClick(R.id.forgot_password_btn)
    public void onViewClicked() {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}

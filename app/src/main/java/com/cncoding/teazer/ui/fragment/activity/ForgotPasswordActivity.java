package com.cncoding.teazer.ui.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.authentication.ForgotPasswordFragment;
import com.cncoding.teazer.authentication.ResetPasswordFragment;
import com.cncoding.teazer.authentication.LoginFragment;
import com.cncoding.teazer.model.auth.BaseAuth;
import com.cncoding.teazer.model.auth.Login;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.branch.referral.Branch;

import static com.cncoding.teazer.MainActivity.FORGOT_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_OTP_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.TAG_FORGOT_PASSWORD_FRAGMENT;
import static com.cncoding.teazer.MainActivity.TAG_RESET_PASSWORD_FRAGMENT;
import static com.cncoding.teazer.MainActivity.TAG_LOGIN_FRAGMENT;
import static com.cncoding.teazer.MainActivity.TAG_OTP_FRAGMENT;
import static com.cncoding.teazer.utilities.AuthUtils.logout;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordFragment.OnForgotPasswordInteractionListener,
        ResetPasswordFragment.OnResetForgotPasswordInteractionListener,
        LoginFragment.LoginInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ButterKnife.bind(this);
        if (null != toolbar) {
            this.setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.black));
            toolbar.setTitle(getString(R.string.change_password));
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        transaction.replace(R.id.main_fragment_container,
                ForgotPasswordFragment.newInstance(null), TAG_FORGOT_PASSWORD_FRAGMENT);
        transaction.commit();

    }

    @Override
    public void onForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_RESET_PASSWORD_FRAGMENT, true, new Object[] {enteredText, countryCode, isEmail});
    }
    private void setFragment(String tag, boolean addToBackStack, Object[] args) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        switch (tag) {
            case TAG_LOGIN_FRAGMENT:
                logout(this, ForgotPasswordActivity.this);
                Branch.getInstance(getApplicationContext()).logout();
                break;
            case TAG_FORGOT_PASSWORD_FRAGMENT:
                transaction.replace(R.id.main_fragment_container,
                        ForgotPasswordFragment.newInstance((String) args[0]), TAG_FORGOT_PASSWORD_FRAGMENT);
                break;
            case TAG_RESET_PASSWORD_FRAGMENT:
                if (args.length >= 3) {
                    transaction.replace(R.id.main_fragment_container,
                            ResetPasswordFragment.newInstance(((String) args[0]), ((int) args[1]), ((boolean) args[2])),
                            TAG_FORGOT_PASSWORD_FRAGMENT);
                }
                break;
            default:
                break;
        }
        if (addToBackStack)
            transaction.addToBackStack(tag);
        transaction.commit();
    }

    @Override
    public void onResetForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail) {
        setFragment(TAG_LOGIN_FRAGMENT, true, new Object[]{enteredText, countryCode, isEmail});
    }

    @Override
    public void onLoginFragmentInteraction(int action, BaseAuth baseAuth) {
        try {
            switch (action) {
                case LOGIN_WITH_PASSWORD_ACTION:
                    successfullyLoggedIn();
                    break;
                case LOGIN_WITH_OTP_ACTION:
//                    InitiateLoginWithOtp initiateLoginWithOtp = (InitiateLoginWithOtp) baseAuth;
                    setFragment(TAG_OTP_FRAGMENT, true, new Object[]{baseAuth, LOGIN_WITH_OTP_ACTION});
                    break;
                case FORGOT_PASSWORD_ACTION:
                    Login login = (Login) baseAuth;
                    setFragment(TAG_FORGOT_PASSWORD_FRAGMENT, true, new Object[] {login.getUserName()});
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void successfullyLoggedIn() {
        startActivity(new Intent(ForgotPasswordActivity.this, BaseBottomBarActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            default:
                return true;
        }
    }
}

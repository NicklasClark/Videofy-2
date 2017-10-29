package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Authorize;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static com.cncoding.teazer.MainActivity.FORGOT_PASSWORD_ACTION;
import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.loginWithOtp;
import static com.cncoding.teazer.utilities.AuthUtils.loginWithUsernameAndPassword;
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;
import static com.cncoding.teazer.utilities.AuthUtils.togglePasswordVisibility;
import static com.cncoding.teazer.utilities.AuthUtils.validateUsername;

public class LoginFragment extends Fragment {

    private static final int LOGIN_STATE_PASSWORD = 1;
    private static final int LOGIN_STATE_OTP = 2;
    public static final int PHONE_NUMBER_FORMAT = 10;
    public static final int EMAIL_FORMAT = 11;
    public static final int USERNAME_FORMAT = 12;

    @BindView(R.id.login_btn) ProximaNovaSemiboldButton loginBtn;
    @BindView(R.id.forgot_password_btn) ProximaNovaSemiboldButton forgotPasswordBtn;
    @BindView(R.id.login_username) ProximaNovaRegularAutoCompleteTextView usernameView;
    @BindView(R.id.login_password) ProximaNovaRegularAutoCompleteTextView passwordView;
    @BindView(R.id.login_through_otp) ProximaNovaSemiboldButton loginThroughOtpBtn;
    @BindView(R.id.login_through_password) ProximaNovaSemiboldButton loginThroughPasswordBtn;
    @BindView(R.id.login_country_code) CountryCodePicker countryCodePicker;
    @BindView(R.id.login_options_layout) RelativeLayout loginOptionsLayout;

    private String username;
    private int countryCode = -1;

    private LoginInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);
        setOnCountryChangeListener();
        passwordView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(32)});
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        countryCode = getCountryCode(countryCodePicker, getActivity());
        countryCodePicker.setCountryForPhoneCode(countryCode);
    }

    private void setOnCountryChangeListener() {
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCodeAsInt();
                setCountryCode(getActivity().getApplicationContext(), countryCode);
            }
        });
    }

    @OnTextChanged(R.id.login_username) public void usernameTextChanged(CharSequence charSequence) {
        username = charSequence.toString();
        if (usernameView.getCompoundDrawables()[2] != null) {
            clearDrawables(usernameView);
        }
    }

    @OnTextChanged(R.id.login_password) public void passwordTextChanged(CharSequence charSequence) {
        if (!charSequence.toString().equals("")) {
            if (passwordView.getCompoundDrawables()[2] == null)
                passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
        } else {
            clearDrawables(passwordView);
        }
    }

    @OnFocusChange(R.id.login_username) public void onUsernameFocusChanged(boolean isFocused) {
        if (!isFocused) {
            validateUsername(usernameView, getActivity());
        }
    }

    @OnTouch(R.id.login_password) public boolean onPasswordShow(MotionEvent event) {
        return togglePasswordVisibility(passwordView, event);
    }

    @OnEditorAction(R.id.login_password)
    public boolean onLoginByKeyboard(TextView v, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
            onLoginBtnClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.login_btn) public void onLoginBtnClick() {
        loginBtn.setEnabled(false);
        switch (getLoginState()) {
            case LOGIN_STATE_PASSWORD:
                String password = passwordView.getText().toString();
                if (username != null && !username.isEmpty() && !password.isEmpty()) {
                    loginWithUsernameAndPassword(username, passwordView, countryCode, countryCodePicker, mListener, loginBtn);
                }
                else Snackbar.make(loginBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
                break;
            case LOGIN_STATE_OTP:
                if (!username.isEmpty()) {
                    loginWithOtp(getContext(), username, countryCode, mListener, loginBtn, null, null, false);
                }
                else setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.forgot_password_btn)
    public void onForgotPasswordClick() {
        mListener.onLoginFragmentInteraction(FORGOT_PASSWORD_ACTION, new Authorize(username), null);
    }

    @OnClick(R.id.login_through_otp) public void onLoginThroughOtpClicked() {
//            Toggle login through OTP
        passwordView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sink_up));
        loginOptionsLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sink_up));
        forgotPasswordBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        usernameView.setInputType(InputType.TYPE_CLASS_NUMBER);
        usernameView.setHint(R.string.phone_number);
        //noinspection deprecation
        usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
        countryCodePicker.setVisibility(View.VISIBLE);
        if (countryCode == -1) {
            countryCodePicker.launchCountrySelectionDialog();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                passwordView.setVisibility(View.GONE);
                loginOptionsLayout.setVisibility(View.INVISIBLE);
                loginBtn.setText(getString(R.string.request_otp));
                loginThroughPasswordBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.float_up));
                loginThroughPasswordBtn.setVisibility(View.VISIBLE);
            }
        }, 250);
        switch (getLoginState()) {
            case LOGIN_STATE_PASSWORD:
                break;
            case LOGIN_STATE_OTP:
                break;
            default:break;

        }
    }

    @OnClick(R.id.login_through_password) public void onLoginThroughPasswordClicked() {
//            Toggle login through password
        loginThroughOtpBtn.setText(getString(R.string.login_through_otp));
        passwordView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.float_down));
        loginOptionsLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.float_down));
        forgotPasswordBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in));
        passwordView.setVisibility(View.VISIBLE);
        loginOptionsLayout.setVisibility(View.VISIBLE);
        usernameView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        usernameView.setHint(R.string.username_email_mobile);
        //noinspection deprecation
        usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
        countryCodePicker.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginBtn.setText(getString(R.string.login));
                loginThroughPasswordBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sink_down));
                loginThroughPasswordBtn.setVisibility(View.INVISIBLE);
            }
        }, 250);
    }

    private int getLoginState() {
        String string = loginBtn.getText().toString();
        if (string.equals(getString(R.string.login)))
            return LOGIN_STATE_PASSWORD;
        else if (string.equals(getString(R.string.request_otp)))
            return LOGIN_STATE_OTP;
        else return -1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginInteractionListener) {
            mListener = (LoginInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface LoginInteractionListener {
        void onLoginFragmentInteraction(int action, Authorize authorize, Pojos.User.UserProfile userProfile);
    }
}
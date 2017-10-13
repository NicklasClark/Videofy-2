package com.cncoding.teazer.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
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
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.apiCalls.UserAuth;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.hbb20.CountryCodePicker;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.FORGOT_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_THROUGH_OTP_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_THROUGH_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.getDeviceId;
import static com.cncoding.teazer.MainActivity.getFcmToken;
import static com.cncoding.teazer.authentication.SignupFragment.COUNTRY_CODE;
import static com.cncoding.teazer.authentication.UserProfile.TEAZER;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOGIN_STATE_PASSWORD = 1;
    private static final int LOGIN_STATE_OTP = 2;
    private static final int PHONE_NUMBER_FORMAT = 10;
    private static final int EMAIL_FORMAT = 11;
    private static final int USERNAME_FORMAT = 12;

    @BindView(R.id.login_btn) ProximaNovaSemiboldButton loginBtn;
    @BindView(R.id.forgot_password_btn) ProximaNovaSemiboldButton forgotPasswordBtn;
    @BindView(R.id.login_username) ProximaNovaRegularAutoCompleteTextView usernameView;
    @BindView(R.id.login_password) ProximaNovaRegularAutoCompleteTextView passwordView;
    @BindView(R.id.login_through_otp) ProximaNovaSemiboldButton loginThroughOtpBtn;
    @BindView(R.id.login_through_password) ProximaNovaSemiboldButton loginThroughPasswordBtn;
    @BindView(R.id.login_country_code) CountryCodePicker countryCodePicker;
    @BindView(R.id.login_options_layout) RelativeLayout loginOptionsLayout;

    private String mParam1;
    private String mParam2;
    private String username;
    private int countryCode = -1;
    private boolean isRemember;

    private LoginInteractionListener mListener;
    private SharedPreferences sharedPreferences;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        getCountryCode();
//        getContext().getTheme().applyStyle(R.style.AppTheme_LoginFragment, true);
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
        username = sharedPreferences.getString("username", null);
        if (username != null) {
            usernameView.setText(username);
            passwordView.setText(sharedPreferences.getString("password", null));
        }
    }

    private void setOnCountryChangeListener() {
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCodeAsInt();
                getActivity().getApplicationContext()
                        .getSharedPreferences(TEAZER, Context.MODE_PRIVATE)
                        .edit()
                        .putInt(COUNTRY_CODE, countryCode)
                        .apply();
            }
        });
    }

    @OnTextChanged(R.id.login_username) public void usernameTextChanged(CharSequence charSequence) {
        username = charSequence.toString();
    }

    @OnTextChanged(R.id.login_password) public void passwordTextChanged(CharSequence charSequence) {
        if (charSequence.toString().equals(""))
            passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
            if (passwordView.getCompoundDrawables()[2] == null)
                passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
    }

    private void getCountryCode() {
        countryCode = getActivity().getApplicationContext()
                .getSharedPreferences(TEAZER, Context.MODE_PRIVATE)
                .getInt(COUNTRY_CODE, -1);
    }

    @OnFocusChange(R.id.login_username) public void onUsernameFocusChanged(boolean isFocused) {
        if (!isFocused) {
            switch (getEnteredFormat()) {
                case PHONE_NUMBER_FORMAT:
                    getCountryCode();
                    if (countryCode == -1)
                        countryCodePicker.launchCountrySelectionDialog();
                    else
                        ApiCallingService.checkPhoneNumber(countryCode, usernameView, false);
                    break;
                case EMAIL_FORMAT:
                    ApiCallingService.checkEmail(usernameView, false);
                    break;
                case USERNAME_FORMAT:
                    ApiCallingService.checkUsername(usernameView, false);
                    break;
                default:
                    break;
            }
        }
    }

    private int getEnteredFormat() {
        String enteredText = usernameView.getText().toString();
        if (!enteredText.isEmpty()) {
            if (TextUtils.isDigitsOnly(enteredText)) {
//                    Phone number is entered
                return PHONE_NUMBER_FORMAT;
            }
            else if (isValidEmailAddress(enteredText)) {
//                    Email is entered
                return EMAIL_FORMAT;
            }
            else {
//                    Username is entered
                return USERNAME_FORMAT;
            }
        } else return -1;
    }

    @OnTouch(R.id.login_password) public boolean onPasswordShow(MotionEvent event) {
        if (passwordView.getCompoundDrawables()[2] != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getRawX() >= (passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width())) {
                        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        return true;
                    }
                    return false;
                case MotionEvent.ACTION_UP:
                    if (event.getRawX() >= (passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width())) {
                        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } return false;
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
        switch (getLoginState()) {
            case LOGIN_STATE_PASSWORD:
                final String password = passwordView.getText().toString();
                if (username != null && !username.isEmpty() && !password.isEmpty()) {
                    if (TextUtils.isDigitsOnly(usernameView.getText().toString()) && countryCode == -1) {
                        countryCodePicker.launchCountrySelectionDialog();
                        return;
                    }
                    if (isPasswordValid()) {
                        ApiCallingService.loginWithPassword(
                                new UserAuth.LoginWithPassword(
                                        username,
                                        password,
                                        getFcmToken(),
                                        getDeviceId(),
                                        DEVICE_TYPE_ANDROID))

                                .enqueue(new Callback<ResultObject>() {
                                    @Override
                                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                        if (response.code() == 200 && response.body().getStatus()) {
                                            mListener.onLoginFragmentInteraction(
                                                    LOGIN_THROUGH_PASSWORD_ACTION,
                                                    new UserAuth.SignUp(username, password));
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResultObject> call, Throwable t) {
                                    }
                                });
                    } else
                        Snackbar.make(loginBtn, "Password must be at least 5 characters", Snackbar.LENGTH_SHORT).show();
                } else Snackbar.make(loginBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
                break;
            case LOGIN_STATE_OTP:
                if (!username.isEmpty()) {
                    ApiCallingService.loginWithOtp(new UserAuth.PhoneNumberDetails(Long.parseLong(username), countryCode))
                            .enqueue(new Callback<ResultObject>() {
                                @Override
                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                    if (response.code() == 200) {
                                        if (response.body().getStatus()) {
                                            if (countryCode == -1) {
                                                countryCodePicker.launchCountrySelectionDialog();
                                            } else {
                                                mListener.onLoginFragmentInteraction(
                                                        LOGIN_THROUGH_OTP_ACTION,
                                                        new UserAuth.SignUp(Long.parseLong(username), countryCode));
                                            }
                                        } else Snackbar.make(loginBtn, R.string.login_through_otp_error, Snackbar.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                }
                            });
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.forgot_password_btn)
    public void onForgotPasswordClick() {
        mListener.onLoginFragmentInteraction(FORGOT_PASSWORD_ACTION, new UserAuth.SignUp(username));
    }

    @OnClick(R.id.login_through_otp)
    public void onLoginThroughOtpClicked() {
//            Toggle login through OTP
        passwordView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sink_up));
        loginOptionsLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.sink_up));
        forgotPasswordBtn.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
        usernameView.setInputType(InputType.TYPE_CLASS_NUMBER);
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
        String string = loginThroughOtpBtn.getText().toString();
        if (string.equals(getString(R.string.login_through_otp)))
            return LOGIN_STATE_PASSWORD;
        else if (string.equals(getString(R.string.login_through_password)))
            return LOGIN_STATE_OTP;
        else return -1;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private boolean isPasswordValid() {
        return passwordView.getText().toString().length() >= 5;
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
        void onLoginFragmentInteraction(int action, UserAuth.SignUp userDetails);
    }
}

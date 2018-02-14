package com.cncoding.teazer.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.auth.BaseAuth;
import com.cncoding.teazer.model.auth.ForgotPassword;
import com.cncoding.teazer.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.model.auth.Login;
import com.cncoding.teazer.utilities.AuthUtils;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.cncoding.teazer.utilities.ViewUtils;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static android.text.TextUtils.isDigitsOnly;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.FORGOT_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_OTP_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_PASSWORD_ACTION;
import static com.cncoding.teazer.authentication.ResetPasswordFragment.COUNTRY_CODE;
import static com.cncoding.teazer.authentication.ResetPasswordFragment.ENTERED_TEXT;
import static com.cncoding.teazer.authentication.ResetPasswordFragment.IS_EMAIL;
import static com.cncoding.teazer.data.remote.api.calls.authentication.AuthenticationRepositoryImpl.CHECK_EMAIL_AVAILABILITY;
import static com.cncoding.teazer.data.remote.api.calls.authentication.AuthenticationRepositoryImpl.CHECK_PHONE_NUMBER_AVAILABILITY;
import static com.cncoding.teazer.data.remote.api.calls.authentication.AuthenticationRepositoryImpl.CHECK_USERNAME_AVAILABILITY;
import static com.cncoding.teazer.data.remote.api.calls.authentication.AuthenticationRepositoryImpl.LOGIN_WITH_OTP;
import static com.cncoding.teazer.data.remote.api.calls.authentication.AuthenticationRepositoryImpl.LOGIN_WITH_PASSWORD;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getEnteredUserFormat;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;
import static com.cncoding.teazer.utilities.AuthUtils.isValidPhoneNumber;
import static com.cncoding.teazer.utilities.AuthUtils.removeView;
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;
import static com.cncoding.teazer.utilities.FabricAnalyticsUtil.logLoginEvent;
import static com.cncoding.teazer.utilities.SharedPrefs.setCurrentPassword;
import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;

@SuppressLint("SwitchIntDef")
public class LoginFragment extends AuthFragment {

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
    @BindView(R.id.already_have_otp) ProximaNovaSemiboldButton alreadyHaveOtpBtn;
    @BindView(R.id.login_through_password) ProximaNovaSemiboldButton loginThroughPasswordBtn;
    @BindView(R.id.country_code_picker) CountryCodePicker countryCodePicker;
    @BindView(R.id.login_options_layout) RelativeLayout loginOptionsLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;

    private int countryCode = -1;
    private String enteredText;
    private boolean isComingFromResetPassword = false;
    public  static boolean isAlreadyOTP = false;

    private Login login;
    private LoginInteractionListener mListener;
    private boolean loginBtnClicked;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String enteredText, int countryCode, boolean isEmail) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ENTERED_TEXT, enteredText);
        args.putInt(COUNTRY_CODE, countryCode);
        args.putBoolean(IS_EMAIL, isEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            enteredText = getArguments().getString(ENTERED_TEXT);
            countryCode = getArguments().getInt(COUNTRY_CODE);
            isComingFromResetPassword = true;
        }
        if (login == null) {
            login = new Login()
                    .setFcmToken(getFcmToken(context))
                    .setDeviceId(getDeviceId(context))
                    .setDeviceType(DEVICE_TYPE_ANDROID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAlreadyOTP) {
            isAlreadyOTP = false;
            onLoginThroughOTP();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);
        setOnCountryChangeListener();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isComingFromResetPassword) {
            usernameView.setText(enteredText);
            passwordView.requestFocus();
        } else {
            countryCode = getCountryCode(countryCodePicker, getActivity());
        }
        countryCodePicker.setCountryForPhoneCode(countryCode);
    }

    private void setOnCountryChangeListener() {
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCodeAsInt();
                setCountryCode(getActivity().getApplicationContext(), countryCode);
            }
        });
    }

    @OnTextChanged(R.id.login_username) public void usernameTextChanged(CharSequence charSequence) {
        login.setUserNameOnly(charSequence.toString());
        if (usernameView.getCompoundDrawables()[2] != null) {
            clearDrawables(usernameView);
        }
    }

    @OnFocusChange(R.id.login_username) public void onUsernameFocusChanged(boolean isFocused) {
        if (!isFocused) {
            validateUsername();
        }
    }

    @OnTextChanged(R.id.login_password) public void passwordTextChanged(CharSequence charSequence) {
        login.setPasswordOnly(charSequence.toString());
        if (!charSequence.toString().equals("")) {
            if (passwordView.getCompoundDrawables()[2] == null)
                passwordView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
        } else {
            clearDrawables(passwordView);
        }
    }

    @OnTouch(R.id.login_password) public boolean onPasswordShow(MotionEvent event) {
        if (passwordView.getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                if(isPasswordShown) {
                    passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled_cross, 0);
                    passwordView.setSelection(passwordView.getText().length());
                    passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordView.setTypeface(new TypeFactory(context).regular);
                    isPasswordShown = false;
                }
                else {
                    passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
                    passwordView.setSelection(passwordView.getText().length());
                    passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    passwordView.setTypeface(new TypeFactory(context).regular);
                    isPasswordShown = true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @OnEditorAction(R.id.login_password) public boolean onLoginByKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            onLoginBtnClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.login_btn) public void onLoginBtnClick() {
        ViewUtils.hideKeyboard(getActivity(), loginBtn);
        if (isConnected) {
            switch (getLoginState()) {
                case LOGIN_STATE_PASSWORD:
                    if (isFieldFilled(CHECK_USERNAME) && isFieldFilled(CHECK_PASSWORD)) {
                        if (isDigitsOnly(login.getUserName()) && countryCode == -1) {
                            countryCodePicker.launchCountrySelectionDialog();
                            return;
                        }
                        if (isFieldValidated(CHECK_USERNAME)) {
                            if (isFieldValidated(CHECK_PASSWORD)) {
                                loginBtn.setEnabled(false);
                                startProgressBar();
                                loginBtnClicked = true;
                                loginWithPassword(login);
                            } else
                                Snackbar.make(loginBtn, "Password must be 8 to 32 characters", Snackbar.LENGTH_SHORT).show();
                        } else
                            Snackbar.make(loginBtn, "Username or phone number not valid", Snackbar.LENGTH_SHORT).show();
                    } else
                        Snackbar.make(loginBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
                    break;
                case LOGIN_STATE_OTP:
                    if (isFieldFilled(CHECK_USERNAME) && isDigitsOnly(login.getUserName())) {
                        loginBtn.setEnabled(false);
                        startProgressBar();
                        loginBtnClicked = true;
                        loginWithOtp(new InitiateLoginWithOtp(Long.parseLong(login.getUserName()), countryCode));
                    } else {
                        setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                        Snackbar.make(usernameView, R.string.enter_phone_number, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        } else {
            notifyNoInternetConnection();
        }
    }

    @OnClick(R.id.forgot_password_btn) public void onForgotPasswordClick() {
        mListener.onLoginFragmentInteraction(FORGOT_PASSWORD_ACTION, new ForgotPassword(login.getUserName()));
    }

    @OnClick(R.id.login_through_otp) public void onLoginThroughOtpClicked() {                       //Toggle login through OTP
        onLoginThroughOTP();
    }

    public void onLoginThroughOTP() {
        usernameView.setInputType(InputType.TYPE_CLASS_NUMBER);
        //setting padding
        float scale = getResources().getDisplayMetrics().density;
        int trbPadding = (int) (14*scale + 0.5f);
        int leftPadding = (int) (0*scale + 0.5f);
        usernameView.setPadding(leftPadding, trbPadding, trbPadding, trbPadding);
        usernameView.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15) });

        usernameView.setHint(R.string.phone_number);
        usernameView.setText("");
        usernameView.setTextAppearance(context, R.style.AppTheme_PhoneNumberEditText);
        usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
        countryCodePicker.setVisibility(VISIBLE);
        if (countryCode == -1) {
            countryCodePicker.launchCountrySelectionDialog();
        }
        passwordView.setVisibility(View.GONE);
        loginOptionsLayout.setVisibility(View.GONE);
        loginBtn.setText(getString(R.string.request_otp));
        alreadyHaveOtpBtn.setVisibility(VISIBLE);
        loginThroughPasswordBtn.setVisibility(VISIBLE);
    }

    @OnClick(R.id.login_through_password) public void onLoginThroughPasswordClicked() {             //Toggle login through password
        loginThroughOtpBtn.setText(getString(R.string.login_through_otp));
        passwordView.setVisibility(VISIBLE);
        loginOptionsLayout.setVisibility(VISIBLE);
        usernameView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        usernameView.setHint(R.string.username_email_mobile);

        float scale = getResources().getDisplayMetrics().density;
        int trbPadding = (int) (14*scale + 0.5f);
        usernameView.setPadding(trbPadding, trbPadding, trbPadding, trbPadding);
        usernameView.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });

        //noinspection deprecation
        usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
        countryCodePicker.setVisibility(View.GONE);
        alreadyHaveOtpBtn.setVisibility(View.GONE);
        loginThroughPasswordBtn.setVisibility(View.GONE);
        loginBtn.setText(getString(R.string.login));
    }

    @OnClick(R.id.already_have_otp) public void alreadyHaveOtp() {
        ViewUtils.hideKeyboard(getActivity(), loginBtn);


        if (!login.getUserName().isEmpty() && isDigitsOnly(login.getUserName())) {
            mListener.onLoginFragmentInteraction(LOGIN_WITH_OTP_ACTION,
                    new InitiateLoginWithOtp(Long.parseLong(login.getUserName()), countryCode));
        } else {
            setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
            Snackbar.make(usernameView, R.string.enter_phone_number, Snackbar.LENGTH_SHORT).show();
        }
    }

    private int getLoginState() {
        String string = loginBtn.getText().toString();
        if (string.equals(getString(R.string.login)))
            return LOGIN_STATE_PASSWORD;
        else if (string.equals(getString(R.string.request_otp)))
            return LOGIN_STATE_OTP;
        else return -1;
    }

    private void startProgressBar() {
        progressBar.animate().scaleX(1).scaleY(1).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
        progressBar.setVisibility(VISIBLE);
    }

    private void markValidity(boolean status) {
        setEditTextDrawableEnd(usernameView, !status ? R.drawable.ic_cross :
                R.drawable.ic_tick_circle);
    }

    @Override protected void handleResponse(ResultObject resultObject) {
        switch (resultObject.getCallType()) {
            case CHECK_USERNAME_AVAILABILITY:
                markValidity(resultObject.getStatus());
                break;
            case CHECK_EMAIL_AVAILABILITY:
                markValidity(resultObject.getStatus());
                break;
            case CHECK_PHONE_NUMBER_AVAILABILITY:
                markValidity(resultObject.getStatus());
                break;
            case LOGIN_WITH_PASSWORD:
                try {
                    SharedPrefs.saveAuthToken(getParentActivity().getApplicationContext(), resultObject.getAuthToken());
                    SharedPrefs.saveUserId(getParentActivity().getApplicationContext(), resultObject.getUserId());
                    setCurrentPassword(context ,passwordView.getText().toString());

                    logLoginEvent("Email", true, login.getUserName());

                    mListener.onLoginFragmentInteraction(LOGIN_WITH_PASSWORD_ACTION, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    logLoginEvent("Email", false, login.getUserName());
                }
                removeView(progressBar);
                loginBtnClicked = false;
                loginBtn.setEnabled(true);
                break;
            case LOGIN_WITH_OTP:
                try {
                    logLoginEvent("OTP", true, login.getUserName());

                    mListener.onLoginFragmentInteraction(LOGIN_WITH_OTP_ACTION,
                            new InitiateLoginWithOtp(Long.parseLong(login.getUserName()), countryCode));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    logLoginEvent("OTP", false, login.getUserName());
                }
                removeView(progressBar);
                loginBtnClicked = false;
                loginBtn.setEnabled(true);
                break;
            default:
                break;
        }
    }

    @Override protected void handleError(Throwable throwable) {
        if (loginBtnClicked) {
            loginBtnClicked = false;
            @NonNull String message = throwable.getMessage() != null ? throwable.getMessage() : getString(R.string.something_went_wrong);
            Snackbar.make(loginBtn, message, Snackbar.LENGTH_LONG).show();
            switch (getLoginState()) {
                case LOGIN_STATE_PASSWORD:
                    logLoginEvent("Email", false, login.getUserName());
                    break;
                case LOGIN_STATE_OTP:
                    logLoginEvent("OTP", false, login.getUserName());
                    break;
                default:
                    break;
            }
            removeView(progressBar);
            loginBtn.setEnabled(true);
        }
    }

    @Override protected void notifyNoInternetConnection() {
        Snackbar.make(loginBtn, R.string.no_internet_connection, Snackbar.LENGTH_SHORT).show();
        removeView(progressBar);
        loginBtn.setEnabled(true);
    }

    @Override protected boolean isFieldValidated(@ValidationType int whichType) {
        switch (whichType) {
            case CHECK_USERNAME:
                return login.getUserName() != null &&
                        login.getUserName().length() >= 4 && login.getUserName().length() <= 50;
            case CHECK_PASSWORD:
                return login.getPassword() != null &&
                        login.getPassword().length() >= 8 && login.getPassword().length() <= 32;
            default:
                return false;
        }
    }

    @Override
    protected boolean isFieldFilled(int whichType) {
        switch (whichType) {
            case CHECK_USERNAME:
                return login.getUserName() != null && !login.getUserName().isEmpty();
            case CHECK_PASSWORD:
                return login.getPassword() != null && !login.getPassword().isEmpty();
            default:
                return false;
        }
    }

    private void validateUsername() {
        if (!usernameView.getText().toString().isEmpty()) {
            switch (getEnteredUserFormat(usernameView.getText().toString())) {
                case PHONE_NUMBER_FORMAT:
                    if (isValidPhoneNumber(usernameView.getText().toString()))
                        checkPhoneNumberAvailability(getCountryCode(null, getParentActivity()), Long.parseLong(usernameView.getText().toString()));
                    else
                        ViewUtils.setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                    break;
                case EMAIL_FORMAT:
                    if (AuthUtils.isValidEmailAddress(usernameView.getText().toString()))
                        checkEmailAvailability(usernameView.getText().toString());
                    else
                        ViewUtils.setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                    break;
                case USERNAME_FORMAT:
                    checkUsernameAvailability(usernameView.getText().toString());
                    break;
                default:
                    break;
            }
        } else {
            ViewUtils.setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
        }
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
    public void onDestroy() {
        super.onDestroy();
        ViewUtils.hideKeyboard(getActivity(), loginBtn);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
//        login = null;
    }

    public interface LoginInteractionListener {
        void onLoginFragmentInteraction(int action, BaseAuth baseAuth);
    }
}
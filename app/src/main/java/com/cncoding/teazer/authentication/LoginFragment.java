package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.data.model.base.Authorize;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.VISIBLE;
import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.FORGOT_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_OTP_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_PASSWORD_ACTION;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.COUNTRY_CODE;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.ENTERED_TEXT;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.IS_EMAIL;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getErrorMessage;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;
import static com.cncoding.teazer.utilities.AuthUtils.loginWithOtp;
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.stopCircularReveal;
import static com.cncoding.teazer.utilities.AuthUtils.validateUsername;
import static com.cncoding.teazer.utilities.FabricAnalyticsUtil.logLoginEvent;
import static com.cncoding.teazer.utilities.SharedPrefs.setCurrentPassword;
import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

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

    private String username;
    private int countryCode = -1;
    private String enteredText;
//    private boolean isEmail;
    private boolean isComingFromResetPassword = false;
    public  static boolean isAlreadyOTP=false;

    private LoginInteractionListener mListener;

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
//            isEmail = getArguments().getBoolean(IS_EMAIL);
            isComingFromResetPassword = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAlreadyOTP)
        {
            isAlreadyOTP=false;
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
            countryCodePicker.setCountryForPhoneCode(countryCode);
            passwordView.requestFocus();
        } else {
            countryCode = getCountryCode(countryCodePicker, getActivity());
        }
        countryCodePicker.setCountryForPhoneCode(countryCode);

//        if (isEmail) {
//            usernameView.setText(enteredText);
//            countryCodePicker.setCountryForPhoneCode(countryCode);
//        }
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
        username = charSequence.toString();
        if (usernameView.getCompoundDrawables()[2] != null) {
            clearDrawables(usernameView);
        }
        if (charSequence.length() > 0) {
            if (TextUtils.isDigitsOnly(charSequence)) {
                if (countryCodePicker.getVisibility() != VISIBLE) {
                    countryCodePicker.setVisibility(View.GONE);
                    usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
                    float scale = getResources().getDisplayMetrics().density;
                    int trbPadding = (int) (14*scale + 0.5f);
                    int leftPadding = (int) (0*scale + 0.5f);
                    usernameView.setPadding(leftPadding, trbPadding, trbPadding, trbPadding);
                    usernameView.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15) });
                }
            } else {
                if (countryCodePicker.getVisibility() == VISIBLE) {
                    countryCodePicker.setVisibility(View.GONE);
                    usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
                    float scale = getResources().getDisplayMetrics().density;
                    int trbPadding = (int) (14*scale + 0.5f);
                    usernameView.setPadding(trbPadding, trbPadding, trbPadding, trbPadding);
                    usernameView.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });
                }
            }
        } else {
            if (countryCodePicker.getVisibility() == VISIBLE) {
                countryCodePicker.setVisibility(View.GONE);
                usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
                float scale = getResources().getDisplayMetrics().density;
                int trbPadding = (int) (14*scale + 0.5f);
                usernameView.setPadding(trbPadding, trbPadding, trbPadding, trbPadding);
                usernameView.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });
            }
        }
    }

    @OnFocusChange(R.id.login_username) public void onUsernameFocusChanged(boolean isFocused) {
        if (!isFocused) {
            validateUsername(usernameView, getActivity());
        }
    }

    @OnTextChanged(R.id.login_password)
    public void passwordTextChanged(CharSequence charSequence) {
        if (!charSequence.toString().equals("")) {
            if (passwordView.getCompoundDrawables()[2] == null)
                passwordView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
        } else {
            clearDrawables(passwordView);
        }
    }

    @OnTouch(R.id.login_password)
    public boolean onPasswordShow(MotionEvent event) {
        if (passwordView.getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                if(isPasswodShown) {
                    passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled_cross, 0);
                    passwordView.setSelection(passwordView.getText().length());
                    passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=false;
                }
                else
                {
                    passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
                    passwordView.setSelection(passwordView.getText().length());
                    passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    passwordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=true;

                }
                return true;
    //            return togglePasswordVisibility(passwordView, context);
            }
            return false;
        }
        return false;
    }



//    @OnTouchClick(R.id.login_password) public void onPasswordShow() {
//        if(isPasswodShown) {
//
//            passwordView.setTypeface(new TypeFactory(context).regular);
//            passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_viewfilled_cross, 0);
//            passwordView.setTypeface(new TypeFactory(context).regular);
//            passwordView.setTypeface(new TypeFactory(context).regular);
//            passwordView.setSelection(passwordView.getText().length());
//            passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//            isPasswodShown=false;
//        }
//        else
//        {
//            passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_viewfilled, 0);
//            passwordView.setTypeface(new TypeFactory(context).regular);
//            passwordView.setTypeface(new TypeFactory(context).regular);
//            passwordView.setSelection(passwordView.getText().length());
//            passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
//            isPasswodShown=true;
//
//        }
//        togglePasswordVisibility(passwordView, context);
//    }
    @OnEditorAction(R.id.login_password) public boolean onLoginByKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            onLoginBtnClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.login_btn) public void onLoginBtnClick() {
        ViewUtils.hideKeyboard(getActivity(), loginBtn);
        switch (getLoginState()) {
            case LOGIN_STATE_PASSWORD:
                String password = passwordView.getText().toString();
                if (username != null && !username.isEmpty() && !password.isEmpty()) {
                    loginWithUsernameAndPassword();
                }
                else
                    Snackbar.make(loginBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
                break;
            case LOGIN_STATE_OTP:
                if (!username.isEmpty() && TextUtils.isDigitsOnly(username)) {
                    loginBtn.setEnabled(false);
                    startProgressBar();
                    if (isConnected) {
                        loginWithOtp(context, username, countryCode, mListener, loginBtn, progressBar,
                                null, null, false);
                    } else {
                        Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                    Snackbar.make(usernameView, R.string.enter_phone_number, Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.forgot_password_btn) public void onForgotPasswordClick() {
        mListener.onLoginFragmentInteraction(FORGOT_PASSWORD_ACTION, new Authorize(username));
    }

    @OnClick(R.id.login_through_otp)
    public void onLoginThroughOtpClicked() {
//            Toggle login through OTP

        onLoginThroughOTP();

    }
    public void onLoginThroughOTP()
    {
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

    @OnClick(R.id.login_through_password) public void onLoginThroughPasswordClicked() {
//            Toggle login through password
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
        if (!username.isEmpty() && TextUtils.isDigitsOnly(username)) {
            mListener.onLoginFragmentInteraction(LOGIN_WITH_OTP_ACTION, new Authorize(Long.parseLong(username), countryCode));
        } else {
            setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
            Snackbar.make(usernameView, R.string.enter_phone_number, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    public void loginWithUsernameAndPassword() {
        if (TextUtils.isDigitsOnly(username) && countryCode == -1) {
            countryCodePicker.launchCountrySelectionDialog();
            return;
        }
        if (AuthUtils.isPasswordValid(passwordView)) {
            loginBtn.setEnabled(false);
            startProgressBar();
            final Authorize authorize = new Authorize(
                    getFcmToken(context),
                    getDeviceId(context),
                    DEVICE_TYPE_ANDROID,
                    username,
                    passwordView.getText().toString());
            if (isConnected) {
                ApiCallingService.Auth.loginWithPassword(context, authorize)
                        .enqueue(new Callback<ResultObject>() {
                            @SuppressWarnings("ConstantConditions")
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                try {
                                    if (response.code() == 200) {
                                        if (response.body().getStatus()) {
                                            SharedPrefs.saveAuthToken(getActivity().getApplicationContext(), response.body().getAuthToken());
                                            SharedPrefs.saveUserId(getActivity().getApplicationContext(), response.body().getUser_id());//1
                                            setCurrentPassword(context ,passwordView.getText().toString());

                                            //fabric event
                                            logLoginEvent("Email", true, username);

                                            mListener.onLoginFragmentInteraction(LOGIN_WITH_PASSWORD_ACTION, authorize);
                                        } else {
                                            ViewUtils.showSnackBar(loginBtn, response.body().getMessage());
                                        }
                                    } else
                                        showSnackBar(loginBtn, getErrorMessage(response.errorBody()));

                                    stopCircularReveal(progressBar);
                                    loginBtn.setEnabled(true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    //fabric event
                                    logLoginEvent("Email", false, username);

                                }
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                stopCircularReveal(progressBar);
                                loginBtn.setEnabled(true);
                                t.printStackTrace();

                                //fabric event
                                logLoginEvent("Email", false, username);
                            }
                        });
            } else {
                Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                stopCircularReveal(progressBar);
            }
        } else {
            loginBtn.setEnabled(true);
            Snackbar.make(loginBtn, "Password must be 5 to 32 characters", Snackbar.LENGTH_SHORT).show();
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
    }

    public interface LoginInteractionListener {
        void onLoginFragmentInteraction(int action, Authorize authorize);
    }
}
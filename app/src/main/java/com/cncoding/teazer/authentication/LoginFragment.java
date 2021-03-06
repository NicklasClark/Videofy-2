package com.cncoding.teazer.authentication;

import android.animation.Animator;
import android.content.Context;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cncoding.teazer.MainActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.AuthUtils;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Authorize;
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

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.FORGOT_PASSWORD_ACTION;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_PASSWORD_ACTION;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.COUNTRY_CODE;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.ENTERED_TEXT;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.IS_EMAIL;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;
import static com.cncoding.teazer.utilities.AuthUtils.loginWithOtp;
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.togglePasswordVisibility;
import static com.cncoding.teazer.utilities.AuthUtils.validateUsername;
import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;

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
    @BindView(R.id.country_code_picker) CountryCodePicker countryCodePicker;
    @BindView(R.id.login_options_layout) RelativeLayout loginOptionsLayout;
    @BindView(R.id.reveal_layout) LinearLayout revealLayout;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.uploading_notification) ProximaNovaRegularTextView uploadingNotification;

    private String username;
    private int countryCode = -1;
    private String enteredText;
    private boolean isEmail;
    private boolean isComingFromResetPassword = false;

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
            isEmail = getArguments().getBoolean(IS_EMAIL);
            isComingFromResetPassword = true;
        }
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
        if (isComingFromResetPassword) {
            usernameView.setText(enteredText);
            countryCodePicker.setCountryForPhoneCode(countryCode);
            passwordView.requestFocus();
        } else {
            countryCode = getCountryCode(countryCodePicker, getActivity());
        }
        countryCodePicker.setCountryForPhoneCode(countryCode);

        usernameView.setText("chaitanya");
        passwordView.setText("chaitanya");
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
        if (charSequence.length() > 0) {
            if (TextUtils.isDigitsOnly(charSequence)) {
                if (countryCodePicker.getVisibility() != VISIBLE) {
                    countryCodePicker.setVisibility(VISIBLE);
                    usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
                }
            } else {
                if (countryCodePicker.getVisibility() == VISIBLE) {
                    countryCodePicker.setVisibility(View.GONE);
                    usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
                }
            }
        } else {
            if (countryCodePicker.getVisibility() == VISIBLE) {
                countryCodePicker.setVisibility(View.GONE);
                usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
            }
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

    @OnEditorAction(R.id.login_password) public boolean onLoginByKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            onLoginBtnClick();
            return true;
        }
        return false;
    }

    @OnClick(R.id.login_btn) public void onLoginBtnClick() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).toggleUpBtnVisibility(INVISIBLE);
        }
        ViewUtils.hideKeyboard(getActivity(), loginBtn);
        loginBtn.setEnabled(false);
        switch (getLoginState()) {
            case LOGIN_STATE_PASSWORD:
                String password = passwordView.getText().toString();
                if (username != null && !username.isEmpty() && !password.isEmpty()) {
                    startCircularReveal();
                    loginWithUsernameAndPassword();
                }
                else Snackbar.make(loginBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
                break;
            case LOGIN_STATE_OTP:
                if (!username.isEmpty()) {
                    startCircularReveal();
                    loginWithOtp(getContext(), username, countryCode, mListener, loginBtn, revealLayout,
                            null, null, false);
                }
                else setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.forgot_password_btn)
    public void onForgotPasswordClick() {
        mListener.onLoginFragmentInteraction(FORGOT_PASSWORD_ACTION, new Authorize(username));
    }

    @OnClick(R.id.login_through_otp) public void onLoginThroughOtpClicked() {
//            Toggle login through OTP
        usernameView.setInputType(InputType.TYPE_CLASS_NUMBER);
        usernameView.setHint(R.string.phone_number);
        //noinspection deprecation
        usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
        countryCodePicker.setVisibility(VISIBLE);
        if (countryCode == -1) {
            countryCodePicker.launchCountrySelectionDialog();
        }
        passwordView.setVisibility(View.GONE);
        loginOptionsLayout.setVisibility(View.GONE);
        loginBtn.setText(getString(R.string.request_otp));
        loginThroughPasswordBtn.setVisibility(VISIBLE);
    }

    @OnClick(R.id.login_through_password) public void onLoginThroughPasswordClicked() {
//            Toggle login through password
        loginThroughOtpBtn.setText(getString(R.string.login_through_otp));
        passwordView.setVisibility(VISIBLE);
        loginOptionsLayout.setVisibility(VISIBLE);
        usernameView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        usernameView.setHint(R.string.username_email_mobile);
        //noinspection deprecation
        usernameView.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
        countryCodePicker.setVisibility(View.GONE);
        loginThroughPasswordBtn.setVisibility(View.GONE);
        loginBtn.setText(getString(R.string.login));
    }

    public void loginWithUsernameAndPassword() {
        if (TextUtils.isDigitsOnly(username) && countryCode == -1) {
            countryCodePicker.launchCountrySelectionDialog();
            return;
        }
        if (AuthUtils.isPasswordValid(passwordView)) {
            final Pojos.Authorize authorize = new Pojos.Authorize(
                    getFcmToken(getContext()),
                    getDeviceId(getContext()),
                    DEVICE_TYPE_ANDROID,
                    username,
                    passwordView.getText().toString());
            ApiCallingService.Auth.loginWithPassword(authorize)
                    .enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            if (response.code() == 200) {
                                if (response.body().getStatus()) {
                                    SharedPrefs.saveAuthToken(getContext(), response.body().getAuthToken());
                                    mListener.onLoginFragmentInteraction(LOGIN_WITH_PASSWORD_ACTION, authorize);
                                } else {
                                    ViewUtils.showSnackBar(loginBtn, response.body().getMessage());
                                }
                            } else
                                ViewUtils.showSnackBar(loginBtn, response.code() + " : " + response.message());

                            stopCircularReveal();
                            loginBtn.setEnabled(true);
                        }

                        void stopCircularReveal() {
                            if (getActivity() != null) {
                                ((MainActivity) getActivity()).toggleUpBtnVisibility(VISIBLE);
                            }
                            Animator animator = ViewAnimationUtils.createCircularReveal(revealLayout,
                                    (int) loginBtn.getX() + (loginBtn.getWidth() / 2),
                                    (int) loginBtn.getY() + (loginBtn.getHeight() / 2),
                                    (float) Math.hypot(revealLayout.getWidth(), revealLayout.getHeight()), 0);
                            animator.setDuration(500);
                            animator.setInterpolator(new DecelerateInterpolator());
                            animator.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    revealLayout.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {
                                }
                            });
                            animator.start();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.animate().scaleX(1).scaleY(1).setDuration(250)
                                            .setInterpolator(new DecelerateInterpolator()).start();
                                    progressBar.setVisibility(VISIBLE);
                                }
                            }, 680);
                        }

                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                            stopCircularReveal();
                            ViewUtils.showSnackBar(loginBtn, t.getMessage());
                            loginBtn.setEnabled(true);
                        }
                    });
        } else
            Snackbar.make(loginBtn, "Password must be 5 to 32 characters", Snackbar.LENGTH_SHORT).show();
    }

    private int getLoginState() {
        String string = loginBtn.getText().toString();
        if (string.equals(getString(R.string.login)))
            return LOGIN_STATE_PASSWORD;
        else if (string.equals(getString(R.string.request_otp)))
            return LOGIN_STATE_OTP;
        else return -1;
    }

    private void startCircularReveal() {
        revealLayout.setVisibility(VISIBLE);
        uploadingNotification.setText(R.string.logging_you_in);
        Animator animator = ViewAnimationUtils.createCircularReveal(revealLayout,
                (int) loginBtn.getX() + (loginBtn.getWidth() / 2), (int) loginBtn.getY() + (loginBtn.getHeight() / 2),
                0, (float) Math.hypot(revealLayout.getWidth(), revealLayout.getHeight()));
        animator.setDuration(500);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.animate().scaleX(1).scaleY(1).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
                progressBar.setVisibility(VISIBLE);
            }
        }, 680);
    }

//    public void stopCircularReveal() {
//        Animator animator = ViewAnimationUtils.createCircularReveal(revealLayout,
//                (int) loginBtn.getX() + (loginBtn.getWidth() / 2), (int) loginBtn.getY() + (loginBtn.getHeight() / 2),
//                (float) Math.hypot(revealLayout.getWidth(), revealLayout.getHeight()), 0);
//        animator.setDuration(500);
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                revealLayout.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//            }
//        });
//        animator.start();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.animate().scaleX(1).scaleY(1).setDuration(250).setInterpolator(new DecelerateInterpolator()).start();
//                progressBar.setVisibility(View.VISIBLE);
//            }
//        }, 680);
//    }

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
        void onLoginFragmentInteraction(int action, Authorize authorize);
    }
}
package com.cncoding.teazer.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.auth.InitiateLoginWithOtp;
import com.cncoding.teazer.model.auth.InitiateSignup;
import com.cncoding.teazer.model.auth.VerifyLoginWithOtp;
import com.cncoding.teazer.model.auth.VerifySignUp;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.cncoding.teazer.utilities.ViewUtils;
import com.cncoding.teazer.utilities.smscatcher.OnSmsCatchListener;
import com.cncoding.teazer.utilities.smscatcher.SmsVerifyCatcher;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_OTP_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_EMAIL_ACTION;
import static com.cncoding.teazer.authentication.SignupFragment2.ARG_PICTURE_PATH;
import static com.cncoding.teazer.utilities.Annotations.LOGIN_WITH_OTP;
import static com.cncoding.teazer.utilities.Annotations.SIGNUP;
import static com.cncoding.teazer.utilities.Annotations.VERIFY_LOGIN_WITH_OTP;
import static com.cncoding.teazer.utilities.Annotations.VERIFY_SIGNUP;
import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

@SuppressLint("SwitchIntDef")
public class ConfirmOtpFragment extends AuthFragment {

    private static final String ARG_INITIAL_SIGNUP_DETAILS = "userDetails";
    private static final String LAUNCH_ACTION = "launchAction";
    private static final int REQUEST_PERMISSIONS = 123;

    @BindView(R.id.otp_sent_text_view) ProximaNovaRegularTextView otpSentTextView;
    @BindView(R.id.otp_1) ProximaNovaRegularAutoCompleteTextView otp1EditText;
    @BindView(R.id.otp_2) ProximaNovaRegularAutoCompleteTextView otp2EditText;
    @BindView(R.id.otp_3) ProximaNovaRegularAutoCompleteTextView otp3EditText;
    @BindView(R.id.otp_4) ProximaNovaRegularAutoCompleteTextView otp4EditText;
    @BindView(R.id.otp_verified_view) ProximaNovaRegularTextView otpVerifiedTextView;
    @BindView(R.id.otp_resend_btn) ProximaNovaSemiboldButton otpResendBtn;

    private VerifySignUp verifySignUp;
    private InitiateLoginWithOtp initiateLoginWithOtp;
    int launchAction;
    private String picturePath;

    private SmsVerifyCatcher smsVerifyCatcher;
    private OnOtpInteractionListener mListener;
    private CountDownTimer countDownTimer;

    public ConfirmOtpFragment() {
        // Required empty public constructor
    }

    public static ConfirmOtpFragment newInstance(Object[] verifySignup) {
        ConfirmOtpFragment fragment = new ConfirmOtpFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_INITIAL_SIGNUP_DETAILS, (Parcelable) verifySignup[0]);
        args.putInt(LAUNCH_ACTION, (Integer) verifySignup[1]);
//        args.putString(ARG_PICTURE_PATH, (String) signUpDetails[2]); not available in case of otp login
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            launchAction = bundle.getInt(LAUNCH_ACTION);
            switch (launchAction) {
                case SIGNUP_WITH_EMAIL_ACTION:
                    verifySignUp = bundle.getParcelable(ARG_INITIAL_SIGNUP_DETAILS);
                    break;
                case LOGIN_WITH_OTP_ACTION:
                    initiateLoginWithOtp = bundle.getParcelable(ARG_INITIAL_SIGNUP_DETAILS);
                    break;
                default:
                    break;
            }
            picturePath = bundle.getString(ARG_PICTURE_PATH);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_confirm_otp, container, false);
        ButterKnife.bind(this, rootView);
        LoginFragment.isAlreadyOTP = true;
        smsVerifyCatcher = new SmsVerifyCatcher(getParentActivity(), this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                if (code.length() == 4) {
                    otp1EditText.setText(String.valueOf(code.charAt(0)));
                    otp2EditText.setText(String.valueOf(code.charAt(1)));
                    otp3EditText.setText(String.valueOf(code.charAt(2)));
                    otp4EditText.setText(String.valueOf(code.charAt(3)));
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startCountDownTimer();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            String sent;
            switch (launchAction) {
                case SIGNUP_WITH_EMAIL_ACTION:
                    sent = getString(R.string.otp_sent_1) + " " + verifySignUp.getPhoneNumber();
                    break;
                case LOGIN_WITH_OTP_ACTION:
                    sent = getString(R.string.otp_sent_1) + " " + initiateLoginWithOtp.getPhoneNumber();
                    break;
                default:
                    sent = getString(R.string.otp_sent_1) + " your phone number";
                    break;
            }
            otpSentTextView.setText(sent);
            if (!arePermissionsAllowed(context.getApplicationContext()))
                requestPermissions();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @OnClick(R.id.otp_resend_btn) public void resendOtp() {
        switch (launchAction) {
            case SIGNUP_WITH_EMAIL_ACTION:
                if (isConnected) {
                    signUp(new InitiateSignup().extractFromVerifySignUp(verifySignUp));
                } else
                    notifyNoInternetConnection();
                break;
            case LOGIN_WITH_OTP_ACTION:
                if (isConnected) {
                    otp1EditText.getText().clear();
                    otp2EditText.getText().clear();
                    otp3EditText.getText().clear();
                    otp4EditText.getText().clear();
                    otp1EditText.requestFocus();
                    loginWithOtp(initiateLoginWithOtp);
                } else {
                    notifyNoInternetConnection();
                }
                break;
            default:
                break;
        }
    }

    @OnTextChanged(R.id.otp_1) public void Otp1TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            otp2EditText.requestFocus();
        }
    }

    @OnTextChanged(R.id.otp_2) public void Otp2TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            otp3EditText.requestFocus();
        }
        else if (charSequence.toString().isEmpty())
            otp1EditText.requestFocus();
    }

    @OnTextChanged(R.id.otp_3) public void Otp3TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            otp4EditText.requestFocus();
        }
        else if (charSequence.toString().isEmpty())
            otp2EditText.requestFocus();
    }

    @OnTextChanged(R.id.otp_4) public void Otp4TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            verifyOtp();
        }
        else if (charSequence.toString().isEmpty())
            otp3EditText.requestFocus();
    }

    @SuppressWarnings("ConstantConditions")
    private void verifyOtp() {
        otpVerifiedTextView.setText("");
        ViewUtils.hideKeyboard(getParentActivity(), otp4EditText);
        if (isConnected) {
            switch (launchAction) {
                case SIGNUP_WITH_EMAIL_ACTION:
                    verifySignUp(verifySignUp
                            .setFcmToken(getFcmToken(context))
                            .setDeviceId(getDeviceId(context))
                            .setDeviceType(DEVICE_TYPE_ANDROID).setOtp(getOtp()));
                    break;
                case LOGIN_WITH_OTP_ACTION:
                    verifyLoginWithOtp(
                            new VerifyLoginWithOtp()
                                    .setFcmToken(getFcmToken(context))
                                    .setDeviceId(getDeviceId(context))
                                    .setDeviceType(DEVICE_TYPE_ANDROID)
                                    .setPhoneNumber(initiateLoginWithOtp.getPhoneNumber())
                                    .setCountryCode(initiateLoginWithOtp.getCountryCode())
                                    .setOtp(getOtp()));
                    break;
                default:
                    break;
            }
        } else notifyNoInternetConnection();
    }

    @Override
    protected void handleResponse(ResultObject resultObject) {
        switch (resultObject.getAuthCallType()) {
            case SIGNUP:
                showSnackBar(otpResendBtn, "New otp sent to " + verifySignUp.getPhoneNumber());
                otpResendBtn.setEnabled(false);
                otpResendBtn.setAlpha(0.5f);
                startCountDownTimer();
                break;
            case VERIFY_LOGIN_WITH_OTP:
                if (resultObject.getStatus()) {
                    SharedPrefs.saveAuthToken(context, resultObject.getAuthToken());
                    SharedPrefs.saveUserId(context, resultObject.getUserId());//1
                    countDownTimer.cancel();
                    otpVerifiedTextView.setText(context.getString(R.string.verified));
                    ViewUtils.setTextViewDrawableEnd(otpVerifiedTextView, R.drawable.ic_check_24dp);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null)
                                mListener.onOtpInteraction(null, null);
                        }
                    }, 1000);
                } else {
                    Snackbar.make(otpVerifiedTextView, R.string.wrong_otp, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case VERIFY_SIGNUP:
                switch (resultObject.getCode()) {
//                    Authorize successful
                    case 201:
                        countDownTimer.cancel();
                        otpVerifiedTextView.setText(context.getString(R.string.verified));
                        SharedPrefs.saveAuthToken(context, resultObject.getAuthToken());
                        ViewUtils.setTextViewDrawableEnd(otpVerifiedTextView, R.drawable.ic_check_24dp);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mListener.onOtpInteraction(verifySignUp, picturePath);
                            }
                        }, 1000);
                        break;
//                    Username, Email or Phone Number already exists
                    case 200:
                        countDownTimer.cancel();
                        otpVerifiedTextView.setText(R.string.wrong_otp);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                otpVerifiedTextView.setText("");
                            }
                        }, 2800);
                        otpResendBtn.setEnabled(true);
                        otpResendBtn.setAlpha(1);
                        break;
//                    Failed, Invalid JSON or validation failed
                    default:
                        showSnackBar(otpResendBtn, getString(R.string.signup_failed));
                        break;
                }
                break;
            case LOGIN_WITH_OTP:
                startCountDownTimer();
                showSnackBar(otpResendBtn, "New otp sent to " + initiateLoginWithOtp.getPhoneNumber());
                break;
            default:
                break;
        }
        otpResendBtn.setEnabled(true);
        otpResendBtn.setAlpha(1);
    }

    @Override
    protected void handleError(Throwable throwable) {
        otpResendBtn.setEnabled(true);
        otpResendBtn.setAlpha(1);
        showSnackBar(otpResendBtn, getString(R.string.something_went_wrong));
    }

    @Override
    protected void notifyNoInternetConnection() {
        showSnackBar(otpResendBtn, getString(R.string.no_internet_connection));
    }

    @Override
    protected boolean isFieldValidated(int whichType) {
        return false;
    }

    @Override
    protected boolean isFieldFilled(int whichType) {
        return false;
    }

    public int getOtp() {
        return Integer.parseInt(
                otp1EditText.getText().toString() +
                otp2EditText.getText().toString() +
                otp3EditText.getText().toString() +
                otp4EditText.getText().toString());
    }

    public void startCountDownTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String remainingTime = context.getString(R.string.retry_in) + " " + String.format(Locale.UK, "%02d:%02d",
                        MILLISECONDS.toMinutes(millisUntilFinished),
                        MILLISECONDS.toSeconds(millisUntilFinished) -
                                MINUTES.toSeconds(MILLISECONDS.toMinutes(millisUntilFinished)));
                otpVerifiedTextView.setText(remainingTime);
            }

            @Override
            public void onFinish() {
                otpVerifiedTextView.setText(R.string.you_can_try_again);
                otpResendBtn.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOtpInteractionListener) {
            mListener = (OnOtpInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOtpInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * PERMISSIONS AND SHIT
     */
    private boolean arePermissionsAllowed(Context applicationContext) {
        int receiveSmsResult = ContextCompat.checkSelfPermission(applicationContext, RECEIVE_SMS);
        int readSmsResult = ContextCompat.checkSelfPermission(applicationContext, READ_SMS);
        int sendSmsResult = ContextCompat.checkSelfPermission(applicationContext, SEND_SMS);

        return receiveSmsResult == PackageManager.PERMISSION_GRANTED &&
                readSmsResult == PackageManager.PERMISSION_GRANTED &&
                sendSmsResult == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getParentActivity(), new String[]{RECEIVE_SMS, READ_SMS, SEND_SMS}, REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean receiveSmsAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readSmsAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean sendSmsAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (!receiveSmsAccepted || !readSmsAccepted || !sendSmsAccepted) {
//                        permissions are denied
                        showExplanationDialog();
                    }
//                    else {
////                        permissions are granted
//                    }
                } else showExplanationDialog();
                break;
            default:
                break;
        }
    }

    private void showExplanationDialog() {
        new AlertDialog.Builder(context)
                .setTitle(R.string.whoa)
                .setMessage(R.string.please_allow_all_permissions_message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }

    public interface OnOtpInteractionListener {
        void onOtpInteraction(VerifySignUp verificationDetails, String picturePath);
    }
}

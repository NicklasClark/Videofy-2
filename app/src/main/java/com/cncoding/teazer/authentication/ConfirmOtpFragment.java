package com.cncoding.teazer.authentication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.Pojos.Authorize;
import com.cncoding.teazer.utilities.Pojos.User.UserProfile;
import com.cncoding.teazer.utilities.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.SEND_SMS;
import static com.cncoding.teazer.MainActivity.DEVICE_TYPE_ANDROID;
import static com.cncoding.teazer.MainActivity.LOGIN_WITH_OTP_ACTION;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_EMAIL_ACTION;
import static com.cncoding.teazer.utilities.AuthUtils.getDeviceId;
import static com.cncoding.teazer.utilities.AuthUtils.getFcmToken;
import static com.cncoding.teazer.utilities.AuthUtils.loginWithOtp;
import static com.cncoding.teazer.utilities.AuthUtils.performFinalSignup;
import static com.cncoding.teazer.utilities.AuthUtils.verifyOtpLogin;

public class ConfirmOtpFragment extends Fragment {

    private static final String USER_DETAILS = "userDetails";
    private static final String LAUNCH_ACTION = "launchAction";
    private static final int REQUEST_PERMISSIONS = 123;

    @BindView(R.id.otp_sent_text_view) ProximaNovaRegularTextView otpSentTextView;
    @BindView(R.id.otp_1) ProximaNovaRegularAutoCompleteTextView otp1EditText;
    @BindView(R.id.otp_2) ProximaNovaRegularAutoCompleteTextView otp2EditText;
    @BindView(R.id.otp_3) ProximaNovaRegularAutoCompleteTextView otp3EditText;
    @BindView(R.id.otp_4) ProximaNovaRegularAutoCompleteTextView otp4EditText;
    @BindView(R.id.otp_verified_view) ProximaNovaRegularTextView otpVerifiedTextView;
    @BindView(R.id.otp_resend_btn) ProximaNovaSemiboldButton otpResendBtn;

    private Authorize userSignUpDetails;
    int launchAction;

    private OnOtpInteractionListener mListener;
    private CountDownTimer countDownTimer;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                //Do whatever you want with the code here
            }
        }
    };

    public ConfirmOtpFragment() {
        // Required empty public constructor
    }

    public static ConfirmOtpFragment newInstance(Object[] signUpDetails) {
        ConfirmOtpFragment fragment = new ConfirmOtpFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER_DETAILS, (Parcelable) signUpDetails[0]);
        args.putInt(LAUNCH_ACTION, (Integer) signUpDetails[1]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userSignUpDetails = getArguments().getParcelable(USER_DETAILS);
            launchAction = getArguments().getInt(LAUNCH_ACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_confirm_otp, container, false);
        ButterKnife.bind(this, rootView);
        setTextLimits();
        otpSentTextView.setText(getString(R.string.otp_sent_1) + " " + userSignUpDetails.getPhoneNumber());

        countDownTimer = ViewUtils.startCountDownTimer(getContext(), otpVerifiedTextView, otpResendBtn);

        if (!arePermissionsAllowed(getActivity().getApplicationContext()))
            requestPermissions();

        return rootView;
    }

    @OnClick(R.id.otp_resend_btn) public void resendOtp() {
        switch (launchAction) {
            case SIGNUP_WITH_EMAIL_ACTION:
                ApiCallingService.Auth.performSignUp(userSignUpDetails).enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus()) {
                                Snackbar.make(otpResendBtn,
                                        "New otp sent to " + userSignUpDetails.getPhoneNumber(),
                                        Snackbar.LENGTH_LONG).show();
                                otpResendBtn.setEnabled(false);
                                countDownTimer = ViewUtils.startCountDownTimer(getContext(), otpVerifiedTextView, otpResendBtn).start();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        Snackbar.make(otpResendBtn, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                        otpResendBtn.setEnabled(true);
                    }
                });
                break;
            case LOGIN_WITH_OTP_ACTION:
                loginWithOtp(getContext(), String.valueOf(userSignUpDetails.getPhoneNumber()), userSignUpDetails.getCountryCode(),
                        null, otpResendBtn, otpVerifiedTextView, new CountDownTimer[]{countDownTimer}, true);
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

    private void verifyOtp() {
        switch (launchAction) {
            case SIGNUP_WITH_EMAIL_ACTION:
                final Authorize verify = new Authorize(
                        userSignUpDetails.getUsername(),
                        userSignUpDetails.getFirstName(),
                        userSignUpDetails.getLastName(),
                        userSignUpDetails.getEmail(),
                        userSignUpDetails.getPassword(),
                        userSignUpDetails.getPhoneNumber(),
                        userSignUpDetails.getCountryCode(),
                        getOtp(),
                        getFcmToken(),
                        getDeviceId(),
                        DEVICE_TYPE_ANDROID);
                performFinalSignup(getContext(), verify, countDownTimer, otpVerifiedTextView, mListener);
                break;
            case LOGIN_WITH_OTP_ACTION:
                verifyOtpLogin(getContext(), userSignUpDetails, getOtp(), countDownTimer, otpVerifiedTextView, mListener, otpResendBtn);
                break;
            default:
                break;
        }
    }

    public int getOtp() {
        return Integer.parseInt(
                otp1EditText.getText().toString() +
                otp2EditText.getText().toString() +
                otp3EditText.getText().toString() +
                otp4EditText.getText().toString());
    }

    private void setTextLimits() {
        InputFilter[] inputFilters = new InputFilter[] {new InputFilter.LengthFilter(1)};
        otp1EditText.setFilters(inputFilters);
        otp2EditText.setFilters(inputFilters);
        otp3EditText.setFilters(inputFilters);
        otp4EditText.setFilters(inputFilters);
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
        if (countDownTimer!= null)
            countDownTimer.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        countDownTimer.cancel();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
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
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{RECEIVE_SMS, READ_SMS, SEND_SMS},
                REQUEST_PERMISSIONS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0) {
                    boolean receiveSmsAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readSmsAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean sendSmsAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (receiveSmsAccepted && readSmsAccepted && sendSmsAccepted) {
//                        permissions are granted
                    }
                    else {
//                        permissions are denied
                        showExplanationDialog();
                    }
                } else showExplanationDialog();
                break;
            default:
                break;
        }
    }

    private void showExplanationDialog() {
        new AlertDialog.Builder(getContext())
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
        void onOtpInteraction(int action, Authorize verificationDetails, UserProfile userProfile, boolean isSignUp, String authToken);
    }
}

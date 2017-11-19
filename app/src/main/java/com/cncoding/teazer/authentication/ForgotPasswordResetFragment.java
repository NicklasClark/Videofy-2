package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.utilities.Pojos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ForgotPasswordResetFragment extends Fragment {
    public static final String ENTERED_TEXT = "enteredText";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String IS_EMAIL = "isEmail";
    public static final int RESET_PASSWORD_SUCCESSFUL = 101;

    @BindView(R.id.forgot_pwd_reset_otp) ProximaNovaRegularAutoCompleteTextView resetOtpView;
    @BindView(R.id.forgot_pwd_reset_new_password) ProximaNovaRegularAutoCompleteTextView resetNewPasswordView;
    @BindView(R.id.forgot_pwd_reset_confirm_password) ProximaNovaRegularAutoCompleteTextView resetConfirmPasswordView;
    @BindView(R.id.password_reset_status_view) ProximaNovaRegularTextView resetPasswordStatusView;

    private String enteredText;
    private int countryCode;
    private boolean isEmail;

    private OnResetForgotPasswordInteractionListener mListener;

    public ForgotPasswordResetFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordResetFragment newInstance(String enteredText, int countryCode, boolean isEmail) {
        ForgotPasswordResetFragment fragment = new ForgotPasswordResetFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @OnEditorAction(R.id.forgot_pwd_reset_confirm_password) public boolean resetByKeyboard(TextView v, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
            }
            resetPassword();
            return true;
        }
        return false;
    }

    @OnClick(R.id.reset_pwd_btn) public void resetPassword() {
        Pojos.Authorize authorize;
        if (isEmail) {
            authorize = new Pojos.Authorize(
                    resetConfirmPasswordView.getText().toString(),
                    enteredText,
                    -1,
                    countryCode,
                    Integer.parseInt(resetOtpView.getText().toString()));
        } else {
            authorize = new Pojos.Authorize(
                    resetConfirmPasswordView.getText().toString(),
                    null,
                    Long.parseLong(enteredText),
                    countryCode,
                    Integer.parseInt(resetOtpView.getText().toString()));
        }
        ApiCallingService.Auth.changePassword(authorize).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    if (response.body().getStatus()) {
                        resetPasswordStatusView.setText(R.string.password_successfully_reset);
                        resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0, 0, R.drawable.ic_tick_circle, 0);

                        changeFirebasePassword(isEmail);
                    } else {
                        resetPasswordStatusView.setText(
                                "Resetting password failed!\n" + response.body().getMessage());
                        resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                0, R.drawable.ic_error, 0, 0);
                    }
                } else {
                    resetPasswordStatusView.setText(
                            "Resetting password failed!\n" + response.code() + " : " + response.body().getMessage());
                    resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0, R.drawable.ic_error, 0, 0);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListener.onResetForgotPasswordInteraction(enteredText, countryCode, isEmail);
                    }
                }, 1000);
//                dismissMessage();
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                resetPasswordStatusView.setText("Resetting password failed!\n" + t.getMessage());
                resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_error, 0, 0);
                dismissMessage();
            }
        });
    }

    private void dismissMessage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetPasswordStatusView.setText("");
                resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }
        }, 4000);
    }

    private void changeFirebasePassword(boolean isEmail) {
        if (isEmail) {

        } else {

            AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(resetConfirmPasswordView.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Password updated");
                                                    } else {
                                                        Log.d(TAG, "Error password not updated");
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d(TAG, "Error auth failed");
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResetForgotPasswordInteractionListener) {
            mListener = (OnResetForgotPasswordInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResetForgotPasswordInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnResetForgotPasswordInteractionListener {
        void onResetForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail);
    }
}

package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.data.model.base.Authorize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.getErrorMessage;
import static com.cncoding.teazer.utilities.AuthUtils.togglePasswordVisibility;
import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

public class ForgotPasswordResetFragment extends AuthFragment {
    public static final String ENTERED_TEXT = "enteredText";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String IS_EMAIL = "isEmail";
//    public static final int RESET_PASSWORD_SUCCESSFUL = 101;

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
    @OnTouch(R.id.forgot_pwd_reset_confirm_password)
    public boolean onPasswordShow(MotionEvent event) {
        if (resetConfirmPasswordView.getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= resetConfirmPasswordView.getRight() - resetConfirmPasswordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                if(isPasswodShown) {
                    resetConfirmPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled_cross, 0);
                    resetConfirmPasswordView.setSelection(resetConfirmPasswordView.getText().length());
                    resetConfirmPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    resetConfirmPasswordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=false;
                }
                else
                {
                    resetConfirmPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
                    resetConfirmPasswordView.setSelection(resetConfirmPasswordView.getText().length());
                    resetConfirmPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    resetConfirmPasswordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @OnTouch(R.id.forgot_pwd_reset_new_password)
    public boolean onPasswordShowConfirm(MotionEvent event) {
        if (resetNewPasswordView.getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= resetNewPasswordView.getRight() - resetNewPasswordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                if(isPasswodShown) {
                    resetNewPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled_cross, 0);
                    resetNewPasswordView.setSelection(resetNewPasswordView.getText().length());
                    resetNewPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    resetNewPasswordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=false;
                }
                else
                {
                    resetNewPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
                    resetNewPasswordView.setSelection(resetNewPasswordView.getText().length());
                    resetNewPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    resetNewPasswordView.setTypeface(new TypeFactory(context).regular);
                    isPasswodShown=true;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnTextChanged(R.id.forgot_pwd_reset_new_password) public void newPasswordTextChanged(CharSequence charSequence) {
        if (!charSequence.toString().equals("")) {
            if (resetNewPasswordView.getCompoundDrawables()[2] == null)
                resetNewPasswordView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
        } else {
            clearDrawables(resetNewPasswordView);
        }
    }

    @OnClick(R.id.forgot_pwd_reset_new_password) public void onNewPasswordShow() {
        togglePasswordVisibility(resetNewPasswordView, context);
    }

    @OnTextChanged(R.id.forgot_pwd_reset_confirm_password) public void confirmPasswordTextChanged(CharSequence charSequence) {
        if (!charSequence.toString().equals("")) {
            if (resetConfirmPasswordView.getCompoundDrawables()[2] == null)
                resetConfirmPasswordView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
        } else {
            clearDrawables(resetConfirmPasswordView);
        }
    }

    @OnClick(R.id.forgot_pwd_reset_confirm_password) public void onConfirmPasswordShow() {
        togglePasswordVisibility(resetConfirmPasswordView, context);
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
        hideKeyboard(getParentActivity(), resetPasswordStatusView);
        if (!resetOtpView.getText().toString().isEmpty()) {
            if (resetNewPasswordView.getText().toString().equals(resetConfirmPasswordView.getText().toString())) {
                Authorize authorize;
                if (isEmail) {
                    authorize = new Authorize(
                            resetConfirmPasswordView.getText().toString(),
                            enteredText,
                            -1,
                            countryCode,
                            Integer.parseInt(resetOtpView.getText().toString()));
                } else {
                    authorize = new Authorize(
                            resetConfirmPasswordView.getText().toString(),
                            null,
                            Long.parseLong(enteredText),
                            countryCode,
                            Integer.parseInt(resetOtpView.getText().toString()));
                }
                if (isConnected) {
                    ApiCallingService.Auth.changePassword(context, authorize).enqueue(new Callback<ResultObject>() {
                        @Override
                        public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                            if (response.code() == 200) {
                                if (response.body().getStatus()) {
                                    resetPasswordStatusView.setText(R.string.password_successfully_reset);
                                    resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            0, 0, R.drawable.ic_tick_circle, 0);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isAdded()) {
                                                mListener.onResetForgotPasswordInteraction(enteredText, countryCode, isEmail);
                                                Toast.makeText(context, "Please login with the new password",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }, 2000);
                                } else {
                                    String resetPasswordStatus = getString(R.string.resetting_password_failed);
                                    resetPasswordStatusView.setText(resetPasswordStatus);
                                    resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                            0, R.drawable.ic_error, 0, 0);
                                }
                            } else {
                                        resetPasswordStatusView.setText(getString(R.string.resetting_password_failed));
                                resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                        0, R.drawable.ic_error, 0, 0);
                            }
            //                dismissMessage();
                        }
    
                        @Override
                        public void onFailure(Call<ResultObject> call, Throwable t) {
                            t.printStackTrace();
                            resetPasswordStatusView.setText(getErrorMessage(null));
                            resetPasswordStatusView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0, R.drawable.ic_error, 0, 0);
                            dismissMessage();
                        }
                    });
                } else {
                    Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                }
            } else {
                showSnackBar(resetOtpView, "Passwords don't match");
            }
        } else {
            showSnackBar(resetOtpView, "Otp is required");
        }
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

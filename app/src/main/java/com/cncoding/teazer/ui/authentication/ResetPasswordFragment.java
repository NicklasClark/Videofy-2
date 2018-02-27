package com.cncoding.teazer.ui.authentication;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.TypeFactory;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.auth.ResetPasswordByOtp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

public class ResetPasswordFragment extends BaseAuthFragment {
    public static final String ENTERED_TEXT = "enteredText";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String IS_EMAIL = "isEmail";

    @BindView(R.id.forgot_pwd_reset_otp) ProximaNovaRegularAutoCompleteTextView resetOtpView;
    @BindView(R.id.forgot_pwd_reset_new_password) ProximaNovaRegularAutoCompleteTextView resetNewPasswordView;
    @BindView(R.id.forgot_pwd_reset_confirm_password) ProximaNovaRegularAutoCompleteTextView resetConfirmPasswordView;
    @BindView(R.id.password_reset_status_view) ProximaNovaRegularTextView statusView;

    private String enteredText;
    private int countryCode;
    private boolean isEmail;
    private String newPass;
    private String confirmPass;
    private int otp;

    private OnResetForgotPasswordInteractionListener mListener;

    public ResetPasswordFragment() {
    }

    public static ResetPasswordFragment newInstance(String enteredText, int countryCode, boolean isEmail) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void toggleViewPasswordVisibility(EditText view) {
        if(isPasswordShown) {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled_cross, 0);
            view.setSelection(view.getText().length());
            view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            view.setTypeface(new TypeFactory(context).regular);
            isPasswordShown =false;
        }
        else {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
            view.setSelection(view.getText().length());
            view.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
            view.setTypeface(new TypeFactory(context).regular);
            isPasswordShown =true;
        }
    }

    @OnTouch(R.id.forgot_pwd_reset_confirm_password) public boolean onPasswordShow(MotionEvent event) {
        if (resetConfirmPasswordView.getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= resetConfirmPasswordView.getRight() - resetConfirmPasswordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                toggleViewPasswordVisibility(resetConfirmPasswordView);
                return true;
            }
            return false;
        }
        return false;
    }

    @OnTouch(R.id.forgot_pwd_reset_new_password) public boolean onPasswordShowConfirm(MotionEvent event) {
        if (resetNewPasswordView.getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP &&
                    event.getRawX() >= resetNewPasswordView.getRight() - resetNewPasswordView.getCompoundDrawables()[2].getBounds().width() * 1.5) {
                toggleViewPasswordVisibility(resetNewPasswordView);
                return true;
            }
            return false;
        }
        return false;
    }

    @OnTextChanged(R.id.forgot_pwd_reset_new_password) public void newPasswordTextChanged(CharSequence charSequence) {
        newPass = charSequence.toString();
        if (!charSequence.toString().equals("")) {
            if (resetNewPasswordView.getCompoundDrawables()[2] == null)
                resetNewPasswordView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
        } else {
            clearDrawables(resetNewPasswordView);
        }
    }

    @OnTextChanged(R.id.forgot_pwd_reset_confirm_password) public void confirmPasswordTextChanged(CharSequence charSequence) {
        confirmPass = charSequence.toString();
        if (!charSequence.toString().equals("")) {
            if (resetConfirmPasswordView.getCompoundDrawables()[2] == null)
                resetConfirmPasswordView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_view_filled, 0);
        } else {
            clearDrawables(resetConfirmPasswordView);
            if (!newPass.equals(confirmPass)) statusView.setText(R.string.passwords_dont_match);
            else statusView.setText(null);
        }
    }

    @OnTextChanged(R.id.forgot_pwd_reset_otp) public void otpEntered(CharSequence charSequence) {
        otp = Integer.parseInt(charSequence.toString());
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
        hideKeyboard(getParentActivity(), statusView);
        if (isConnected) {
            if (isFieldFilled(CHECK_PASSWORD)) {
                if (newPass.equals(confirmPass)) {
                    ResetPasswordByOtp resetPasswordDetails;
                    resetPasswordDetails = isEmail ? new ResetPasswordByOtp(confirmPass, enteredText, null, countryCode, otp) :
                            new ResetPasswordByOtp(confirmPass, null, Long.parseLong(enteredText), countryCode, otp);
                    resetPasswordByOtp(resetPasswordDetails);
                }
                else showSnackBar(resetOtpView, getString(R.string.passwords_dont_match));
            }
            else showSnackBar(resetOtpView, getString(R.string.otp_is_required));
        }
        else notifyNoInternetConnection();
    }

    @Override
    protected void handleResponse(ResultObject resultObject) {
        if (resultObject.getStatus()) {
            statusView.setText(R.string.password_successfully_reset);
            statusView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_tick_circle, 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null) {
                        mListener.onResetForgotPasswordInteraction(enteredText, countryCode, isEmail);
                        Toast.makeText(context, "Please login with the new password", Toast.LENGTH_LONG).show();
                    }
                }
            }, 2000);
        } else {
            String resetPasswordStatus = getString(R.string.resetting_password_failed);
            statusView.setText(resetPasswordStatus);
            statusView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        }
    }

    @Override
    protected void handleError(Throwable throwable) {
        throwable.printStackTrace();
        statusView.setText(R.string.something_went_wrong);
        statusView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
        dismissMessage();
    }

    @Override
    protected void notifyNoInternetConnection() {
        showSnackBar(statusView, getString(R.string.no_internet_connection));
    }

    @Override
    protected boolean isFieldValidated(int whichType) {
        return false;
    }

    @Override
    protected boolean isFieldFilled(int whichType) {
        return !resetOtpView.getText().toString().isEmpty() && !newPass.isEmpty() && !confirmPass.isEmpty();
    }

    private void dismissMessage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                statusView.setText("");
                statusView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
            }
        }, 4000);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResetForgotPasswordInteractionListener) {
            mListener = (OnResetForgotPasswordInteractionListener) context;
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
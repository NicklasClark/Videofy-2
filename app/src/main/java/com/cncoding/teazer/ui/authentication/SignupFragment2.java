package com.cncoding.teazer.ui.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.auth.InitiateSignup;
import com.cncoding.teazer.data.model.auth.ProceedSignup;
import com.cncoding.teazer.data.model.auth.VerifySignUp;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.ui.authentication.base.BaseAuthFragment;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.common.Annotations;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.STATUS_FALSE;
import static com.cncoding.teazer.ui.authentication.ResetPasswordFragment.COUNTRY_CODE;
import static com.cncoding.teazer.utilities.common.Annotations.CHECK_EMAIL_AVAILABILITY;
import static com.cncoding.teazer.utilities.common.Annotations.CHECK_PHONE_NUMBER_AVAILABILITY;
import static com.cncoding.teazer.utilities.common.Annotations.SIGNUP;
import static com.cncoding.teazer.utilities.common.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.common.AuthUtils.isConnected;
import static com.cncoding.teazer.utilities.common.AuthUtils.isValidEmailAddress;
import static com.cncoding.teazer.utilities.common.AuthUtils.isValidPhoneNumber;
import static com.cncoding.teazer.utilities.common.SharedPrefs.TEAZER;
import static com.cncoding.teazer.utilities.common.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.common.ViewUtils.setEditTextDrawableEnd;
import static com.cncoding.teazer.utilities.common.ViewUtils.showSnackBar;

public class SignupFragment2 extends BaseAuthFragment {

    private static final String ARG_SIGNUP_INFO = "info";
    public static final String ARG_PICTURE_PATH = "picturePath";

    @BindView(R.id.signup_name) ProximaNovaRegularAutoCompleteTextView nameView;
    @BindView(R.id.signup_email) ProximaNovaRegularAutoCompleteTextView emailView;
    @BindView(R.id.signup_country_code) CountryCodePicker countryCodeView;
    @BindView(R.id.signup_phone_number) ProximaNovaRegularAutoCompleteTextView phoneNumberView;
    @BindView(R.id.signup_btn) ProximaNovaSemiboldButton signupBtn;

    private ProceedSignup proceedSignup;
    private String picturePath;
    private InitiateSignup initiateSignup;

    private OnFinalSignupInteractionListener mListener;

    public SignupFragment2() {
        // Required empty public constructor
    }

    public static SignupFragment2 newInstance(ProceedSignup proceedSignup, String picturePath) {
        SignupFragment2 fragment = new SignupFragment2();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SIGNUP_INFO, proceedSignup);
        args.putString(ARG_PICTURE_PATH, picturePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            proceedSignup = bundle.getParcelable(ARG_SIGNUP_INFO);
            picturePath = bundle.getString(ARG_PICTURE_PATH);
        }
        if (initiateSignup == null && proceedSignup != null)
            initiateSignup = new InitiateSignup()
                .setUserName(proceedSignup.getUserName())
                .setPassword(proceedSignup.getPassword());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_signup_2, container, false);
        ButterKnife.bind(this, rootView);
//        setTextFilters();
        setOnCountryChangeListener();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiateSignup.setCountryCodeOnly(getCountryCode(countryCodeView, getParentActivity()));
    }

    private void setOnCountryChangeListener() {
        countryCodeView.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                //noinspection ConstantConditions
                getParentActivity().getApplicationContext()
                        .getSharedPreferences(TEAZER, Context.MODE_PRIVATE)
                        .edit()
                        .putInt(COUNTRY_CODE, countryCodeView.getSelectedCountryCodeAsInt())
                        .apply();
            }
        });
    }

    @OnTextChanged(R.id.signup_email) public void emailEntered(CharSequence charSequence) {
        initiateSignup.setEmailOnly(charSequence.toString());
    }

    @OnTextChanged(R.id.signup_phone_number) public void phoneNumberEntered(CharSequence charSequence) {
        initiateSignup.setPhoneNumberOnly(charSequence.toString());
    }

    @OnEditorAction(R.id.signup_email) public boolean goToPhoneNumberField(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_NEXT) {
            phoneNumberView.requestFocus();
            return true;
        }
        return false;
    }

    @OnEditorAction(R.id.signup_phone_number) public boolean signupProceedByKeyboard(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            hideKeyboard(getParentActivity(), phoneNumberView);
            performSignup();
            return true;
        }
        return false;
    }

    @OnFocusChange(R.id.signup_email) public void onEmailFocusChanged(boolean isFocused) {
        if (!isFocused) {
            String email = emailView.getText().toString();
            if (!email.isEmpty() && isValidEmailAddress(emailView.getText().toString())) {
                viewModel.checkEmailAvailability(initiateSignup.getEmail());
            } else {
                setEditTextDrawableEnd(emailView, R.drawable.ic_error);
            }
        }
    }

    @OnFocusChange(R.id.signup_phone_number) public void onPhoneNumberFocusChanged(boolean isFocused) {
        if (!isFocused) {
            if (isValidPhoneNumber(phoneNumberView.getText().toString()))
                viewModel.checkPhoneNumberAvailability(countryCodeView.getDefaultCountryCodeAsInt(), initiateSignup.getPhoneNumber());
            else
                setEditTextDrawableEnd(phoneNumberView, R.drawable.ic_error);
        }
    }

    @OnFocusChange(R.id.signup_name) public void getNameEntered() {
        initiateSignup.setFirstAndLastNames(getFirstAndLastNames(nameView.getText().toString()));
    }

    @OnClick(R.id.signup_btn) public void performSignup() {
        try {
            hideKeyboard(getParentActivity(), signupBtn);
            if (isConnected(context)) {
                if (isFieldFilled(Annotations.CHECK_USERNAME)) {
                    if (getFirstAndLastNames(nameView.getText().toString()).length < 2) {
                        if (isValidEmailAddress(emailView.getText().toString())) {
                            signupBtn.setEnabled(false);
                            viewModel.signUp(initiateSignup);
                        }
                        else showSnackBar(signupBtn, "Please provide a valid email address.");
                    }
                    else showSnackBar(signupBtn, "Please provide both first and last names, separated by blank space");
                }
                else showSnackBar(signupBtn, "All fields are required");
            }
            else notifyNoInternetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String[] getFirstAndLastNames(String name) {
        String[] names = name.split(" ");
        if (names.length > 1)
            return new String[] {names[0], names[names.length - 1]};
        else
            return names;
    }

    @SuppressLint("SwitchIntDef") @Override
    protected void handleResponse(ResultObject resultObject) {
        try {
            switch (resultObject.getCallType()) {
                case CHECK_EMAIL_AVAILABILITY:
                    markValidity(resultObject.getStatus(), emailView);
                    break;
                case CHECK_PHONE_NUMBER_AVAILABILITY:
                    markValidity(resultObject.getStatus(), phoneNumberView);
                    break;
                case SIGNUP:
                    if (resultObject.getError() != null) {
                        //noinspection ThrowableNotThrown
                        switch (resultObject.getError().getMessage()) {
                            case NOT_SUCCESSFUL:
                                showSnackBar(signupBtn, getString(R.string.something_is_not_right));
                                break;
                            case STATUS_FALSE:
                                showSnackBar(signupBtn, context.getString(R.string.already_exists_signup));
                                break;
                            default:
                                showSnackBar(signupBtn, getString(R.string.signup_failed));
                                break;
                        }
                    } else if (mListener != null) {
                        mListener.onFinalEmailSignupInteraction(new VerifySignUp().setInitialSignup(initiateSignup), picturePath);
                    }
                    break;
            }
            signupBtn.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleError(ResultObject resultObject) {
        try {
            showSnackBar(signupBtn, Objects.equals(resultObject.getMessage(), FAILED) ?
                    getString(R.string.something_went_wrong) :getString(R.string.signup_failed));
            signupBtn.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void notifyNoInternetConnection() {
        Snackbar.make(signupBtn, R.string.not_connected_message, Snackbar.LENGTH_SHORT).show();
    }

    private void markValidity(boolean status, TextView view) {
        setEditTextDrawableEnd(view,
                status ?
                        R.drawable.ic_cross :
                        R.drawable.ic_tick_circle);
    }

    @Override
    protected boolean isFieldValidated(int whichType) {
        return false;
    }

    @Override
    protected boolean isFieldFilled(int whichType) {
        return !nameView.getText().toString().isEmpty() &&
                !emailView.getText().toString().isEmpty() &&
                !countryCodeView.getSelectedCountryCode().isEmpty() &&
                !phoneNumberView.getText().toString().isEmpty();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFinalSignupInteractionListener) {
            mListener = (OnFinalSignupInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFinalSignupInteractionListener {
        void onFinalEmailSignupInteraction(VerifySignUp verifySignUp, String picturePath);
    }
}
package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.model.auth.InitiateSignup;
import com.cncoding.teazer.model.auth.ProceedSignup;
import com.cncoding.teazer.model.auth.VerifySignUp;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

import static com.cncoding.teazer.authentication.ResetPasswordFragment.COUNTRY_CODE;
import static com.cncoding.teazer.data.api.calls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.api.calls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;
import static com.cncoding.teazer.data.api.calls.authentication.AuthenticationRepositoryImpl.STATUS_FALSE;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.isValidEmailAddress;
import static com.cncoding.teazer.utilities.AuthUtils.isValidPhoneNumber;
import static com.cncoding.teazer.utilities.SharedPrefs.TEAZER;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

public class SignupFragment2 extends AuthFragment {

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
                ApiCallingService.Auth.checkEmail(getContext(), emailView, true);
            } else {
                setEditTextDrawableEnd(emailView, R.drawable.ic_error);
            }
        }
    }

    @OnFocusChange(R.id.signup_phone_number) public void onPhoneNumberFocusChanged(boolean isFocused) {
        if (!isFocused) {
            if (isValidPhoneNumber(phoneNumberView.getText().toString()))
                ApiCallingService.Auth.checkPhoneNumber(getContext(), countryCodeView.getDefaultCountryCodeAsInt(), phoneNumberView, true);
            else
                setEditTextDrawableEnd(phoneNumberView, R.drawable.ic_error);
        }
    }

    @OnFocusChange(R.id.signup_name) public void getNameEntered() {
        initiateSignup.setFirstAndLastNames(getFirstAndLastNames(nameView.getText().toString()));
    }

    @OnClick(R.id.signup_btn) public void performSignup() {
        hideKeyboard(getParentActivity(), signupBtn);
        if (isConnected) {
            if (isFieldFilled(CHECK_USERNAME)) {
                if (getFirstAndLastNames(nameView.getText().toString()).length < 2) {
                    if (isValidEmailAddress(emailView.getText().toString())) {
                        signupBtn.setEnabled(false);
                        signUp(initiateSignup);
                    }
                    else showSnackBar(signupBtn, "Please provide a valid email address.");
                }
                else showSnackBar(signupBtn, "Please provide both first and last names, separated by blank space");
            }
            else showSnackBar(signupBtn, "All fields are required");
        }
        else notifyNoInternetConnection();
    }

    public static String[] getFirstAndLastNames(String name) {
        String[] names = name.split(" ");
        if (names.length > 1)
            return new String[] {names[0], names[names.length - 1]};
        else
            return names;
    }

    @Override
    protected void handleResponse(ResultObject resultObject) {
        signupBtn.setEnabled(true);
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
        } else
            mListener.onFinalEmailSignupInteraction(new VerifySignUp().setInitialSignup(initiateSignup), picturePath);
        signupBtn.setEnabled(true);
    }

    @Override
    protected void handleError(Throwable throwable) {
        showSnackBar(signupBtn, throwable.getMessage().equals(FAILED) ?
                getString(R.string.something_went_wrong) :getString(R.string.signup_failed));
        signupBtn.setEnabled(true);
    }

    @Override
    protected void notifyNoInternetConnection() {
        Snackbar.make(signupBtn, R.string.not_connected_message, Snackbar.LENGTH_SHORT).show();
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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFinalSignupInteractionListener");
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
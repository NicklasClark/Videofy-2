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
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.model.base.Authorize;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;

import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.COUNTRY_CODE;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.isValidEmailAddress;
import static com.cncoding.teazer.utilities.AuthUtils.isValidPhoneNumber;
import static com.cncoding.teazer.utilities.AuthUtils.performInitialSignUp;
import static com.cncoding.teazer.utilities.SharedPrefs.TEAZER;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;

public class SignupFragment2 extends AuthFragment {
    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASS = "pass";
    public static final String ARG_PICTURE_PATH = "picturePath";

    @BindView(R.id.signup_name) ProximaNovaRegularAutoCompleteTextView nameView;
    @BindView(R.id.signup_email) ProximaNovaRegularAutoCompleteTextView emailView;
    @BindView(R.id.signup_country_code) CountryCodePicker countryCodeView;
    @BindView(R.id.signup_phone_number) ProximaNovaRegularAutoCompleteTextView phoneNumberView;
    @BindView(R.id.signup_btn) ProximaNovaSemiboldButton signupBtn;

    private String username;
    private String pass;
    private String picturePath;

    private OnFinalSignupInteractionListener mListener;

    public SignupFragment2() {
        // Required empty public constructor
    }

    public static SignupFragment2 newInstance(String username, String pass, String picturePath) {
        SignupFragment2 fragment = new SignupFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASS, pass);
        args.putString(ARG_PICTURE_PATH, picturePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString(ARG_USERNAME);
            pass = bundle.getString(ARG_PASS);
            picturePath = bundle.getString(ARG_PICTURE_PATH);
        }
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
        getCountryCode(countryCodeView, getParentActivity());
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

    @OnClick(R.id.signup_btn) public void performSignup() {
        hideKeyboard(getParentActivity(), signupBtn);
        if (isConnected) {
            if (areAllViewsFilled()) {
                if (getFirstAndLastNames(nameView.getText().toString()).length >= 2) {
                    if (isValidEmailAddress(emailView.getText().toString())) {
                        String[] names = getFirstAndLastNames(nameView.getText().toString());
                        final Authorize authorize = new Authorize(
                                username,
                                names[0],
                                names[1],
                                emailView.getText().toString(),
                                pass,
                                Long.parseLong(phoneNumberView.getText().toString()),
                                countryCodeView.getSelectedCountryCodeAsInt()
                        );
                        signupBtn.setEnabled(false);
                        performInitialSignUp(getContext(), mListener, authorize, signupBtn, picturePath);
                    } else Snackbar.make(signupBtn, "Please provide a valid email address.", Snackbar.LENGTH_SHORT).show();
                } else Snackbar.make(signupBtn, "Please provide both first and last names, separated by blank space", Snackbar.LENGTH_SHORT).show();
            } else
                Snackbar.make(signupBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
        } else
            Snackbar.make(signupBtn, R.string.not_connected_message, Snackbar.LENGTH_SHORT).show();
    }

    public static String[] getFirstAndLastNames(String name) {
        String[] names = name.split(" ");
        if (names.length > 1)
            return new String[] {names[0], names[names.length - 1]};
        else
            return names;
    }

    private boolean areAllViewsFilled() {
        return !nameView.getText().toString().isEmpty() &&
                !emailView.getText().toString().isEmpty() &&
                !countryCodeView.getSelectedCountryCode().isEmpty() &&
                !phoneNumberView.getText().toString().isEmpty();
    }

//    private void setTextFilters() {
////        InputFilter[] inputFilters = new InputFilter[] {FilterFactory.passwordFilter};
////        passwordView.setFilters(inputFilters);
////        confirmPasswordView.setFilters(inputFilters);
//    }

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
        void onFinalEmailSignupInteraction(Authorize signUpDetails, String picturePath);
    }
}
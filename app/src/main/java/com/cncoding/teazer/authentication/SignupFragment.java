package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.utilities.AuthUtils;
import com.cncoding.teazer.utilities.Pojos.Authorize;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.cncoding.teazer.MainActivity.EMAIL_SIGNUP_PROCEED_ACTION;
import static com.cncoding.teazer.authentication.ForgotPasswordResetFragment.COUNTRY_CODE;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.isValidPhoneNumber;
import static com.cncoding.teazer.utilities.OfflineUserProfile.TEAZER;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;

public class SignupFragment extends Fragment {

//    private String email;

    @BindView(R.id.signup_name) ProximaNovaRegularAutoCompleteTextView nameView;
    @BindView(R.id.signup_email) ProximaNovaRegularAutoCompleteTextView emailView;
    @BindView(R.id.signup_country_code) CountryCodePicker countryCodeView;
    @BindView(R.id.signup_phone_number) ProximaNovaRegularAutoCompleteTextView phoneNumberView;
    @BindView(R.id.signup__proceed_btn) AppCompatButton signupProceedBtn;

    private OnInitialSignupInteractionListener mListener;
    //    private Context context;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, rootView);
        setTextLimits();
        setOnCountryChangeListener();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCountryCode(countryCodeView, getActivity());
    }

    private void setOnCountryChangeListener() {
        countryCodeView.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                getActivity().getApplicationContext()
                        .getSharedPreferences(TEAZER, Context.MODE_PRIVATE)
                        .edit()
                        .putInt(COUNTRY_CODE, countryCodeView.getSelectedCountryCodeAsInt())
                        .apply();
            }
        });
    }

    @OnClick(R.id.signup__proceed_btn) public void signupProceed() {
        if (areAllViewsFilled()) {
            if (getFirstAndLastNames(nameView.getText().toString()).length >= 2) {
                String[] names = getFirstAndLastNames(nameView.getText().toString());
                mListener.onInitialEmailSignupInteraction(EMAIL_SIGNUP_PROCEED_ACTION,
                        new Authorize(names[0], names[1],
                                emailView.getText().toString(),
                                countryCodeView.getSelectedCountryCodeAsInt(),
                                Long.parseLong(phoneNumberView.getText().toString())));
            } else Snackbar.make(signupProceedBtn, "Please provide both first and last names", Snackbar.LENGTH_SHORT).show();
        } else Snackbar.make(signupProceedBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
    }

    @OnFocusChange(R.id.signup_email) public void onEmailFocusChanged(boolean isFocused) {
        if (!isFocused) {
            String email = emailView.getText().toString();
            if (!email.isEmpty() && AuthUtils.isValidEmailAddress(emailView.getText().toString())) {
                ApiCallingService.Auth.checkEmail(emailView, true);
            } else {
                setEditTextDrawableEnd(emailView, R.drawable.ic_error);
            }
        }
    }

    @OnFocusChange(R.id.signup_phone_number) public void onPhoneNumberFocusChanged(boolean isFocused) {
        if (!isFocused) {
            if (isValidPhoneNumber(phoneNumberView.getText().toString()))
                ApiCallingService.Auth.checkPhoneNumber(countryCodeView.getDefaultCountryCodeAsInt(), phoneNumberView, true);
            else
                setEditTextDrawableEnd(phoneNumberView, R.drawable.ic_error);
        }
    }

    public static String[] getFirstAndLastNames(String name) {
        String[] names = name.split(" ");
        return new String[] {names[0], names[names.length - 1]};
    }

    private void setTextLimits() {
        phoneNumberView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
    }

    private boolean areAllViewsFilled() {
        return !nameView.getText().toString().isEmpty() &&
                !emailView.getText().toString().isEmpty() &&
                !countryCodeView.getSelectedCountryCode().isEmpty() &&
                !phoneNumberView.getText().toString().isEmpty();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnInitialSignupInteractionListener) {
            mListener = (OnInitialSignupInteractionListener) context;
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

    public interface OnInitialSignupInteractionListener {
        void onInitialEmailSignupInteraction(int action, Authorize signUpDetails);
    }
}
package com.cncoding.teazer.authentication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.UserAuth;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.cncoding.teazer.MainActivity.SIGNUP_WITH_EMAIL_ACTION;
import static com.cncoding.teazer.authentication.UserProfile.TEAZER;

public class SignupFragment extends Fragment {
    public static final String COUNTRY_CODE = "countryCode";

//    private String email;

    @BindView(R.id.signup_first_name) ProximaNovaRegularAutoCompleteTextView firstNameView;
    @BindView(R.id.signup_last_name) ProximaNovaRegularAutoCompleteTextView lastNameView;
    @BindView(R.id.signup_username) ProximaNovaRegularAutoCompleteTextView usernameView;
    @BindView(R.id.signup_email) ProximaNovaRegularAutoCompleteTextView emailView;
    @BindView(R.id.signup_country_code) CountryCodePicker countryCodeView;
    @BindView(R.id.signup_phone_number) ProximaNovaRegularAutoCompleteTextView phoneNumberView;
    @BindView(R.id.signup_password) ProximaNovaRegularAutoCompleteTextView passwordView;
    @BindView(R.id.signup_confirm_password) ProximaNovaRegularAutoCompleteTextView confirmPasswordView;
    @BindView(R.id.signup_btn) AppCompatButton signupBtn;

    private OnEmailSignupInteractionListener mListener;
    private FragmentActivity fragmentActivity;
//    private Context context;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentActivity = getActivity();
//        context = getContext();
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

    private boolean isConnected() {
        ConnectivityManager conman = (ConnectivityManager) fragmentActivity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conman.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @OnEditorAction(R.id.signup_confirm_password) public boolean onLoginByKeyboard(TextView v, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
            performSignup();
            return true;
        }
        return false;
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

    @OnClick(R.id.signup_btn) public void performSignup() {
        String password = passwordView.getText().toString();
        if (isConnected()) {
            if (areAllViewsFilled()) {
                if (isPasswordValid(password)) {
                    if (password.equals(confirmPasswordView.getText().toString())) {
                        mListener.onEmailSignupInteraction(SIGNUP_WITH_EMAIL_ACTION,
                                new UserAuth.SignUp(
                                        usernameView.getText().toString(),
                                        firstNameView.getText().toString(),
                                        lastNameView.getText().toString(),
                                        emailView.getText().toString(),
                                        passwordView.getText().toString(),
                                        Long.parseLong(phoneNumberView.getText().toString()),
                                        countryCodeView.getSelectedCountryCodeAsInt()
                                )
                        );
                    } else
                        Snackbar.make(signupBtn, "Passwords don't match!", Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(signupBtn, "Password must be at least 5 characters", Snackbar.LENGTH_SHORT).show();
            } else
                Snackbar.make(signupBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
        } else
            Snackbar.make(signupBtn, R.string.not_connected_message, Snackbar.LENGTH_SHORT).show();
    }

//    @OnTextChanged(R.id.signup_email) public void emailTextChanged(CharSequence charSequence) {
//        email = charSequence.toString();
//    }

    @OnTextChanged(R.id.signup_password) public void signupPasswordTextChanged(CharSequence charSequence) {
        if (charSequence.toString().equals(""))
            passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
        if (passwordView.getCompoundDrawables()[2] == null)
            passwordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
    }

    @OnTextChanged(R.id.signup_confirm_password) public void confirmPasswordTextChanged(CharSequence charSequence) {
        if (charSequence.toString().equals(""))
            confirmPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        else
        if (confirmPasswordView.getCompoundDrawables()[2] == null)
            confirmPasswordView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_view, 0);
    }

    @OnTouch(R.id.signup_password) public boolean onSignupPasswordShow(MotionEvent event) {
        if (passwordView.getCompoundDrawables()[2] != null) {
            passwordView.post(new Runnable() {
                @Override
                public void run() {
                    passwordView.setSelection(passwordView.getText().length());
                }
            });
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getRawX() >= (passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width())) {
                        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        return true;
                    }
                    return false;
                case MotionEvent.ACTION_UP:
                    if (event.getRawX() >= (passwordView.getRight() - passwordView.getCompoundDrawables()[2].getBounds().width())) {
                        passwordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } return false;
    }

    @OnTouch(R.id.signup_confirm_password) public boolean onConfirmPasswordShow(MotionEvent event) {
        if (confirmPasswordView.getCompoundDrawables()[2] != null) {
            confirmPasswordView.post(new Runnable() {
                @Override
                public void run() {
                    confirmPasswordView.setSelection(passwordView.getText().length());
                }
            });
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (event.getRawX() >=
                            (confirmPasswordView.getRight() - confirmPasswordView.getCompoundDrawables()[2].getBounds().width())) {
                        confirmPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        return true;
                    }
                    return false;
                case MotionEvent.ACTION_UP:
                    if (event.getRawX() >=
                            (confirmPasswordView.getRight() - confirmPasswordView.getCompoundDrawables()[2].getBounds().width())) {
                        confirmPasswordView.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } return false;
    }

    @OnFocusChange(R.id.signup_username) public void onUsernameFocusChanged(boolean isFocused) {
        if (!isFocused) {
            String username = usernameView.getText().toString();
            if (!username.isEmpty()) {
                ApiCallingService.checkUsername(usernameView, true);
            }
        }
    }

    @OnFocusChange(R.id.signup_email) public void onEmailFocusChanged(boolean isFocused) {
        if (!isFocused) {
            String email = emailView.getText().toString();
            if (!email.isEmpty()) {
                ApiCallingService.checkEmail(emailView, true);
            }
        }
    }

    @OnFocusChange(R.id.signup_phone_number) public void onPhoneNumberFocusChanged(boolean isFocused) {
        if (!isFocused) {
            ApiCallingService.checkPhoneNumber(countryCodeView.getDefaultCountryCodeAsInt(), phoneNumberView, true);
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 5;
    }

    private boolean areAllViewsFilled() {
        return !firstNameView.getText().toString().isEmpty() &&
                !lastNameView.getText().toString().isEmpty() &&
                !usernameView.getText().toString().isEmpty() &&
                !emailView.getText().toString().isEmpty() &&
                !countryCodeView.getSelectedCountryCode().isEmpty() &&
                !phoneNumberView.getText().toString().isEmpty() &&
                !passwordView.getText().toString().isEmpty() &&
                !confirmPasswordView.getText().toString().isEmpty();
    }

    private void setTextLimits() {
        phoneNumberView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});
        InputFilter[] inputFilters = new InputFilter[] {new InputFilter.LengthFilter(32)};
        passwordView.setFilters(inputFilters);
        confirmPasswordView.setFilters(inputFilters);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEmailSignupInteractionListener) {
            mListener = (OnEmailSignupInteractionListener) context;
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

    public interface OnEmailSignupInteractionListener {
        void onEmailSignupInteraction(int action, UserAuth.SignUp signUpDetails);
    }
}

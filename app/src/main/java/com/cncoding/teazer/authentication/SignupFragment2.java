package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.utilities.NetworkStateReceiver;
import com.cncoding.teazer.utilities.Pojos.Authorize;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;

import static com.cncoding.teazer.utilities.AuthUtils.performInitialSignup;
import static com.cncoding.teazer.utilities.AuthUtils.togglePasswordVisibility;
import static com.cncoding.teazer.utilities.ViewUtils.clearDrawables;
import static com.cncoding.teazer.utilities.ViewUtils.hideKeyboard;
import static com.cncoding.teazer.utilities.ViewUtils.setEditTextDrawableEnd;

public class SignupFragment2 extends Fragment {
    private static final String SIGNUP_DETAILS = "signupDetails";

    private Authorize signUpDetails;

    @BindView(R.id.signup_page_header) ProximaNovaRegularTextView headerTextView;
    @BindView(R.id.signup_username) ProximaNovaRegularAutoCompleteTextView usernameView;
    @BindView(R.id.signup_password) ProximaNovaRegularAutoCompleteTextView passwordView;
    @BindView(R.id.signup_confirm_password) ProximaNovaRegularAutoCompleteTextView confirmPasswordView;
    @BindView(R.id.signup_btn) ProximaNovaSemiboldButton signupBtn;

    private OnFinalSignupInteractionListener mListener;

    public SignupFragment2() {
        // Required empty public constructor
    }

    public static SignupFragment2 newInstance(Authorize signUpDetails) {
        SignupFragment2 fragment = new SignupFragment2();
        Bundle args = new Bundle();
        args.putParcelable(SIGNUP_DETAILS, signUpDetails);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            signUpDetails = getArguments().getParcelable(SIGNUP_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_signup_2, container, false);
        ButterKnife.bind(this, rootView);
        setTextLimits();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        headerTextView.setText("Hey " + signUpDetails.getFirstName() + ", " + getString(R.string.you_are_almost_there));
    }

    @OnEditorAction(R.id.signup_confirm_password) public boolean onLoginByKeyboard(TextView v, int actionId) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            performSignup();
            return true;
        }
        return false;
    }

    @OnTextChanged(R.id.signup_password) public void signupPasswordTextChanged(CharSequence charSequence) {
        if (charSequence.toString().equals(""))
            clearDrawables(passwordView);
        else
        if (passwordView.getCompoundDrawables()[2] == null)
            setEditTextDrawableEnd(passwordView, R.drawable.ic_view_filled);
    }

    @OnTextChanged(R.id.signup_confirm_password) public void confirmPasswordTextChanged(CharSequence charSequence) {
        if (charSequence.toString().equals(""))
            clearDrawables(confirmPasswordView);
        else
        if (confirmPasswordView.getCompoundDrawables()[2] == null)
            setEditTextDrawableEnd(confirmPasswordView, R.drawable.ic_view_filled);
    }

    @OnTouch(R.id.signup_password) public boolean onSignupPasswordShow(MotionEvent event) {
        return togglePasswordVisibility(passwordView, event);
    }

    @OnTouch(R.id.signup_confirm_password) public boolean onConfirmPasswordShow(MotionEvent event) {
        return togglePasswordVisibility(confirmPasswordView, event);
    }

    @OnFocusChange(R.id.signup_username) public void onUsernameFocusChanged(boolean isFocused) {
        if (!isFocused) {
            String username = usernameView.getText().toString();
            if (!username.isEmpty()) {
                ApiCallingService.Auth.checkUsername(usernameView, true);
            } else {
                setEditTextDrawableEnd(usernameView, R.drawable.ic_error);
            }
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8 && password.length() <= 32;
    }

    private boolean areAllViewsFilled() {
        return !usernameView.getText().toString().isEmpty() &&
                !passwordView.getText().toString().isEmpty() &&
                !confirmPasswordView.getText().toString().isEmpty();
    }

    @OnClick(R.id.signup_btn) public void performSignup() {
        hideKeyboard(getActivity(), signupBtn);
        String password = passwordView.getText().toString();
        if (NetworkStateReceiver.isConnected(getActivity())) {
            if (areAllViewsFilled()) {
                if (isPasswordValid(password)) {
                    if (password.equals(confirmPasswordView.getText().toString())) {
                        final Authorize authorize = new Authorize(
                                usernameView.getText().toString(),
                                signUpDetails.getFirstName(),
                                signUpDetails.getLastName(),
                                signUpDetails.getEmail(),
                                passwordView.getText().toString(),
                                signUpDetails.getPhoneNumber(),
                                signUpDetails.getCountryCode()
                        );
                        signupBtn.setEnabled(false);
                        performInitialSignup(mListener, authorize, signupBtn);
                    } else
                        Snackbar.make(signupBtn, "Passwords don't match!", Snackbar.LENGTH_SHORT).show();
                } else
                    Snackbar.make(signupBtn, "Password must be 8 to 32 characters", Snackbar.LENGTH_SHORT).show();
            } else
                Snackbar.make(signupBtn, "All fields are required", Snackbar.LENGTH_SHORT).show();
        } else
            Snackbar.make(signupBtn, R.string.not_connected_message, Snackbar.LENGTH_SHORT).show();
    }

    private void setTextLimits() {
        InputFilter[] inputFilters = new InputFilter[] {new InputFilter.LengthFilter(32)};
        passwordView.setFilters(inputFilters);
        confirmPasswordView.setFilters(inputFilters);
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
        void onFinalSignupInteraction(int action, Authorize signUpDetails);
    }
}
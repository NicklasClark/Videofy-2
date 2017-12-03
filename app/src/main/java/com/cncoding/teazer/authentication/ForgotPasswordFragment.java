package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.utilities.AuthUtils;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.getErrorMessage;
import static com.cncoding.teazer.utilities.AuthUtils.logTheError;
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

public class ForgotPasswordFragment extends Fragment {

    private static final String USERNAME = "username";

    @BindView(R.id.forgot_pwd_email_mobile) ProximaNovaRegularAutoCompleteTextView forgotPasswordEditText;
    @BindView(R.id.country_code_picker) CountryCodePicker countryCodePicker;

    private String username;
    private int countryCode;

    private OnForgotPasswordInteractionListener mListener;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordFragment newInstance(String username) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, rootView);

//        countryCode = getCountryCode(countryCodePicker, getActivity());
        if (countryCode != -1) {
            countryCodePicker.setCountryForPhoneCode(countryCode = getCountryCode(countryCodePicker, getActivity()));
        }
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryCode = countryCodePicker.getSelectedCountryCodeAsInt();
                //noinspection ConstantConditions
                setCountryCode(getActivity().getApplicationContext(), countryCode);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forgotPasswordEditText.setText(username);
    }

    @OnTextChanged(R.id.forgot_pwd_email_mobile) public void onTextEntered(CharSequence charSequence) {
        if (charSequence.length() > 0) {
            if (TextUtils.isDigitsOnly(charSequence)) {
                if (countryCodePicker.getVisibility() != View.VISIBLE) {
                    countryCodePicker.setVisibility(View.VISIBLE);
                    forgotPasswordEditText.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
                }
            } else {
                if (countryCodePicker.getVisibility() == View.VISIBLE) {
                    countryCodePicker.setVisibility(View.GONE);
                    forgotPasswordEditText.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
                }
            }
        } else {
            if (countryCodePicker.getVisibility() == View.VISIBLE) {
                countryCodePicker.setVisibility(View.GONE);
                forgotPasswordEditText.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
            }
        }
    }

    @OnClick(R.id.reset_pwd_btn) public void resetPassword() {
        final String enteredText = forgotPasswordEditText.getText().toString();
        if (!enteredText.isEmpty()) {
            if (TextUtils.isDigitsOnly(enteredText)) {
//                Phone number is entered
                ApiCallingService.Auth.requestResetPasswordByPhone(Long.parseLong(enteredText), countryCode)
                        .enqueue(new Callback<ResultObject>() {
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                if (response.code() == 200) {
                                    if (response.body().getStatus()) {
                                        mListener.onForgotPasswordInteraction(enteredText, countryCode, false);
                                    } else {
                                        showSnackBar(forgotPasswordEditText, getErrorMessage(response.errorBody()));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                logTheError("resetPasswordByPhone", t.getMessage());
                            }
                        });
            } else {
//                Email is entered
                if (AuthUtils.isValidEmailAddress(enteredText)) {
                    ApiCallingService.Auth.requestResetPasswordByEmail(enteredText)
                            .enqueue(new Callback<ResultObject>() {
                                @Override
                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                    if (response.code() == 200) {
                                        if (response.body().getStatus()) {
                                            mListener.onForgotPasswordInteraction(enteredText, countryCode, true);
                                        } else {
                                            showSnackBar(forgotPasswordEditText, getErrorMessage(response.errorBody()));
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                    logTheError("resetPasswordByEmail", t.getMessage());
                                }
                            });
                } else {
                    showSnackBar(forgotPasswordEditText, getString(R.string.error_invalid_email));
                }
            }
        } else {
            showSnackBar(forgotPasswordEditText, getString(R.string.enter_email_or_mobile_number_first));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnForgotPasswordInteractionListener) {
            mListener = (OnForgotPasswordInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnForgotPasswordInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnForgotPasswordInteractionListener {
        void onForgotPasswordInteraction(String enteredText, int countryCode, boolean isEmail);
    }
}
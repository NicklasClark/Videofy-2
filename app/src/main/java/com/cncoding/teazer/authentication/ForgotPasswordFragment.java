package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                setCountryCode(getActivity().getApplicationContext(), countryCode);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
                                        Snackbar.make(forgotPasswordEditText, "An error occurred! Please try again later.",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {
                                Snackbar.make(forgotPasswordEditText, t.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                        });
            } else {
//                Email is entered
                ApiCallingService.Auth.requestResetPasswordByEmail(enteredText)
                        .enqueue(new Callback<ResultObject>() {
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                if (response.code() == 200) {
                                    if (response.body().getStatus()) {
                                        mListener.onForgotPasswordInteraction(enteredText, countryCode, true);
                                    } else {
                                        Snackbar.make(forgotPasswordEditText, "An error occurred! Please try again later.",
                                                Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {
                            }
                        });
            }
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
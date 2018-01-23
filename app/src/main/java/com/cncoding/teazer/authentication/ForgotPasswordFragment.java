package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

public class ForgotPasswordFragment extends AuthFragment {

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
                setCountryCode(context, countryCode);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (username != null) {
            if (validateEmailOrPhone(username)) {
                forgotPasswordEditText.setText(username);
            }
        }
    }

    private boolean validateEmailOrPhone(String text) {
            if(isValidEmailAddress(text) || isValidPhone(text))
                return true;
            else
                return false;
    }
    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
    public static boolean isValidPhone(String phone) {
        String ePattern = "\\d+";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(phone);
        return m.matches();
    }

    @OnTextChanged(R.id.forgot_pwd_email_mobile) public void onTextEntered(CharSequence charSequence) {
        if (charSequence.length() > 0) {
            if (TextUtils.isDigitsOnly(charSequence)) {
                if (countryCodePicker.getVisibility() != View.VISIBLE) {
                    countryCodePicker.setVisibility(View.VISIBLE);
                    forgotPasswordEditText.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
                    //setting padding
                    float scale = getResources().getDisplayMetrics().density;
                    int trbPadding = (int) (14*scale + 0.5f);
                    int leftPadding = (int) (0*scale + 0.5f);
                    forgotPasswordEditText.setPadding(leftPadding, trbPadding, trbPadding, trbPadding);
                    forgotPasswordEditText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(15) });
                }
            } else {
                if (countryCodePicker.getVisibility() == View.VISIBLE) {
                    countryCodePicker.setVisibility(View.GONE);
                    forgotPasswordEditText.setBackground(getResources().getDrawable(R.drawable.bg_button_white));
                    //setting padding
                    float scale = getResources().getDisplayMetrics().density;
                    int trbPadding = (int) (14*scale + 0.5f);
                    int leftPadding = (int) (14*scale + 0.5f);
                    forgotPasswordEditText.setPadding(leftPadding, trbPadding, trbPadding, trbPadding);
                    forgotPasswordEditText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });
                }
            }
        } else {
            if (countryCodePicker.getVisibility() == View.VISIBLE) {
                countryCodePicker.setVisibility(View.GONE);
                forgotPasswordEditText.setBackground(getResources().getDrawable(R.drawable.bg_button_right_curved));
                //setting padding
                float scale = getResources().getDisplayMetrics().density;
                int trbPadding = (int) (14*scale + 0.5f);
                int leftPadding = (int) (14*scale + 0.5f);
                forgotPasswordEditText.setPadding(leftPadding, trbPadding, trbPadding, trbPadding);
                forgotPasswordEditText.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(100) });
            }
        }
    }

    @OnClick(R.id.reset_pwd_btn) public void resetPassword() {
        try {
            final String enteredText = forgotPasswordEditText.getText().toString();
            if (!enteredText.isEmpty()) {
                if (TextUtils.isDigitsOnly(enteredText)) {
    //                Phone number is entered
                    if (isConnected) {
                        ApiCallingService.Auth.requestResetPasswordByPhone(context, Long.parseLong(enteredText), countryCode)
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
                                        t.printStackTrace();
                                    }
                                });
                    } else {
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    }
                } else {
    //                Email is entered
                    if (AuthUtils.isValidEmailAddress(enteredText)) {
                        if (isConnected) {
                            ApiCallingService.Auth.requestResetPasswordByEmail(context, enteredText)
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
                                            t.printStackTrace();
                                        }
                                    });
                        } else {
                            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showSnackBar(forgotPasswordEditText, getString(R.string.error_invalid_email));
                    }
                }
            } else {
                showSnackBar(forgotPasswordEditText, getString(R.string.enter_email_or_mobile_number_first));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
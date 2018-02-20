package com.cncoding.teazer.authentication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.model.auth.ResetPasswordByPhoneNumber;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.text.TextUtils.isDigitsOnly;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.REQUEST_RESET_PASSWORD_BY_EMAIL;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.REQUEST_RESET_PASSWORD_BY_PHONE;
import static com.cncoding.teazer.utilities.AuthUtils.getCountryCode;
import static com.cncoding.teazer.utilities.AuthUtils.setCountryCode;
import static com.cncoding.teazer.utilities.ViewUtils.showSnackBar;

@SuppressLint("SwitchIntDef")
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

        countryCode = getCountryCode(countryCodePicker, getParentActivity());
        if (countryCode != -1) {
            countryCodePicker.setCountryForPhoneCode(countryCode);
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
            if (isFieldValidated(CHECK_EMAIL) || isFieldValidated(CHECK_PHONE_NUMBER)) {
                forgotPasswordEditText.setText(username);
            }
        }
    }

    @OnTextChanged(R.id.forgot_pwd_email_mobile) public void onTextEntered(CharSequence charSequence) {
        username = charSequence.toString();
        if (charSequence.length() > 0) {
            if (isDigitsOnly(charSequence)) {
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
            if (isConnected) {
                if (isFieldFilled(CHECK_USERNAME) || isFieldFilled(CHECK_EMAIL)) {
                    if (isDigitsOnly(username)) {
//                        Phone number is entered
                        requestResetPasswordByPhone(new ResetPasswordByPhoneNumber(Long.parseLong(username), countryCode));
                    } else {
//                        Email is entered
                        if (isFieldValidated(CHECK_EMAIL)) {
                            requestResetPasswordByEmail(username);
                        }
                        else showSnackBar(forgotPasswordEditText, getString(R.string.error_invalid_email));
                    }
                } else showSnackBar(forgotPasswordEditText, getString(R.string.enter_email_or_mobile_number_first));
            }
            else notifyNoInternetConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleResponse(ResultObject resultObject) {
        try {
            switch (resultObject.getCallType()) {
                case REQUEST_RESET_PASSWORD_BY_EMAIL:
                    if (resultObject.getStatus()) {
                        mListener.onForgotPasswordInteraction(username, countryCode, true);
                    } else {
                        showSnackBar(forgotPasswordEditText, resultObject.getErrorBody().getMessage());
                    }
                    break;
                case REQUEST_RESET_PASSWORD_BY_PHONE:
                    if (resultObject.getStatus()) {
                        mListener.onForgotPasswordInteraction(username, countryCode, false);
                    } else {
                        showSnackBar(forgotPasswordEditText, resultObject.getErrorBody().getMessage());
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleError(Throwable throwable) {
        throwable.printStackTrace();
        showSnackBar(forgotPasswordEditText, throwable.getMessage() != null ?
                throwable.getMessage() : getString(R.string.something_went_wrong));
    }

    @Override
    protected void notifyNoInternetConnection() {
        showSnackBar(forgotPasswordEditText, getString(R.string.no_internet_connection));
    }

    @Override
    protected boolean isFieldValidated(int whichType) {
        switch (whichType) {
            case AuthFragment.CHECK_EMAIL:
                String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\." +
                        "[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                java.util.regex.Matcher m = p.matcher(username);
                return m.matches();
            case AuthFragment.CHECK_PHONE_NUMBER:
                String ePattern1 = "\\d+";
                java.util.regex.Pattern p1 = java.util.regex.Pattern.compile(ePattern1);
                java.util.regex.Matcher m1 = p1.matcher(username);
                return m1.matches();
            default:
                return false;
        }
    }

    @Override
    protected boolean isFieldFilled(int whichType) {
        switch (whichType) {
            case AuthFragment.CHECK_EMAIL:
                return !forgotPasswordEditText.getText().toString().isEmpty();
            case AuthFragment.CHECK_USERNAME:
                return username != null && !username.isEmpty();
            default:
                return false;
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
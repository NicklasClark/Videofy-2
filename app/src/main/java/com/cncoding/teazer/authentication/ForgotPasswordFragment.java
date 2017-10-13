package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {

    private static final String USERNAME = "username";

    private String username;

    private OnForgotPasswordInteractionListener mListener;
    @BindView(R.id.forgot_pwd_email_mobile) ProximaNovaRegularAutoCompleteTextView forgotPasswordEditText;

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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forgotPasswordEditText.setText(username);
    }

    @OnClick(R.id.reset_pwd_btn) public void resetPassword() {
        String enteredText = forgotPasswordEditText.getText().toString();
        if (!enteredText.isEmpty()) {
            if (TextUtils.isDigitsOnly(enteredText)) {
//                Phone number is entered
                ApiCallingService.requestResetPasswordByPhone(Long.parseLong(enteredText), 91)
                        .enqueue(new Callback<ResultObject>() {
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                
                            }

                            @Override
                            public void onFailure(Call<ResultObject> call, Throwable t) {

                            }
                        });
            } else {
//                Email is entered
                ApiCallingService.requestResetPasswordByEmail(enteredText)
                        .enqueue(new Callback<ResultObject>() {
                            @Override
                            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

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
        void onForgotPasswordInteraction(int action);
    }
}
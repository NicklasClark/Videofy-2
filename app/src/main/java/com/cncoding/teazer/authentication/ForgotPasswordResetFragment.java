package com.cncoding.teazer.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;

import butterknife.ButterKnife;

public class ForgotPasswordResetFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnResetForgotPasswordInteractionListener mListener;

    public ForgotPasswordResetFragment() {
        // Required empty public constructor
    }

    public static ForgotPasswordResetFragment newInstance(String param1, String param2) {
        ForgotPasswordResetFragment fragment = new ForgotPasswordResetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResetForgotPasswordInteractionListener) {
            mListener = (OnResetForgotPasswordInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResetForgotPasswordInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnResetForgotPasswordInteractionListener {
        void onResetForgotPasswordInteraction(int action);
    }
}

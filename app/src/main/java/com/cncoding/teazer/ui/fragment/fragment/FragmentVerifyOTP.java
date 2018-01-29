package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ReportPostTitleAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaRegularAutoCompleteTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.data.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.data.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.UpdateMobileNumber;
import com.cncoding.teazer.utilities.ViewUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import jp.wasabeef.blurry.Blurry;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 16/12/17.
 */

public class FragmentVerifyOTP extends DialogFragment {



    @BindView(R.id.otp_sent_text_view)
    ProximaNovaRegularTextView otpSentTextView;
    @BindView(R.id.otp_1)
    ProximaNovaRegularAutoCompleteTextView otp1EditText;
    @BindView(R.id.otp_2) ProximaNovaRegularAutoCompleteTextView otp2EditText;
    @BindView(R.id.otp_3) ProximaNovaRegularAutoCompleteTextView otp3EditText;
    @BindView(R.id.otp_4) ProximaNovaRegularAutoCompleteTextView otp4EditText;
    @BindView(R.id.otp_verified_view) ProximaNovaRegularTextView otpVerifiedTextView;
    @BindView(R.id.otp_resend_btn) ProximaNovaSemiboldButton otpResendBtn;
    @BindView(R.id.btnClose) AppCompatImageView btnClose;


    Context context;
    private Integer selectedReportId;
    private long mobilenumber;
    private int countrycode;
    private String  firsName;
    private String  lastName;
    private String  userName;
    private String  details;
    private String  email;
    private int   gender;
    private static final int RC_REQUEST_STORAGE = 1001;

    public FragmentVerifyOTP() {

    }



    public static FragmentVerifyOTP newInstance(long mobilenumber, int countrycode, String firsname,String lastname,String username,String email,String details,int gender) {
        FragmentVerifyOTP frag = new FragmentVerifyOTP();
        Bundle args = new Bundle();
        args.putLong("mobilenumber", mobilenumber);
        args.putInt("countrycode", countrycode);
        args.putString("firstname", firsname);
        args.putString("lastname", lastname);
        args.putString("username", username);
        args.putString("details", details);
        args.putString("email", email);
        args.putInt("gender", gender);
        frag.setArguments(args);
        return frag;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mobilenumber = getArguments().getLong("mobilenumber");
        countrycode = getArguments().getInt("countrycode");
        firsName = getArguments().getString("firstname");
        lastName = getArguments().getString("lastname");
        userName = getArguments().getString("username");
        email = getArguments().getString("email");
        details = getArguments().getString("details");
        gender = getArguments().getInt("gender");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_otp, container);
        ButterKnife.bind(this, rootView);
        context=getContext();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        otpResendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reSendOTP(mobilenumber,countrycode);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String sent = getString(R.string.otp_sent_1) + " " + mobilenumber;
        otpSentTextView.setText(sent);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnTextChanged(R.id.otp_1) public void Otp1TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            otp2EditText.requestFocus();
        }
    }

    @OnTextChanged(R.id.otp_2) public void Otp2TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            otp3EditText.requestFocus();
        }
        else if (charSequence.toString().isEmpty())
            otp1EditText.requestFocus();
    }

    @OnTextChanged(R.id.otp_3) public void Otp3TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            otp4EditText.requestFocus();
        }
        else if (charSequence.toString().isEmpty())
            otp2EditText.requestFocus();
    }

    @OnTextChanged(R.id.otp_4) public void Otp4TextChanged(CharSequence charSequence) {
        if (charSequence.toString().length() >= 1) {
            verifyOTP();
        }
        else if (charSequence.toString().isEmpty())
            otp3EditText.requestFocus();
    }



    public void verifyOTP()
    {
        otpVerifiedTextView.setText("");
        ViewUtils.hideKeyboard(getActivity(), otp4EditText);
        String otp= otp1EditText.getText().toString()+otp2EditText.getText().toString()+otp3EditText.getText().toString()+otp4EditText.getText().toString();
        //Toast.makeText(getContext(),otp,Toast.LENGTH_SHORT).show();
        UpdateMobileNumber updateMobileNumber=new UpdateMobileNumber(mobilenumber,countrycode,Integer.parseInt(otp));

        ApiCallingService.User.updateMobileNumber(context,updateMobileNumber)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus()) {

                             //   Toast.makeText(context,"OTP verified",Toast.LENGTH_LONG).show();
                                ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(firsName, lastName, userName, details, email, gender);
                                ProfileUpdate(profileUpdateRequest);
                            } else {

                                Toast.makeText(context,response.body().getMessage()+" Enter correct OTP",Toast.LENGTH_LONG).show();
                                otp1EditText.setText(null);
                                otp2EditText.setText(null);
                                otp3EditText.setText(null);
                                otp4EditText.setText(null);
                                otp1EditText.requestFocus();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    public void ProfileUpdate(ProfileUpdateRequest profileUpdateRequest) {

        ApiCallingService.User.updateUserProfiles(profileUpdateRequest, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {

                    try {
                        if (response.body().getStatus()) {
                            Toast.makeText(context, "Your Profile has been updated", Toast.LENGTH_SHORT).show();
                            ProfileFragment.checkprofileupdated = true;
                            getDialog().dismiss();
                            getActivity().finish();

                        } else {
                            Toast.makeText(context, "Your Profile has not been updated yet,Please try again", Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Something went wrong Please try again", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(context, "Please check your data is correct", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Network Issue Please check once again ", Toast.LENGTH_LONG).show();

            }
        });
    }





    void reSendOTP(final long mobilenumber,final  int countrycode)

    {


        Toast.makeText(context,"OTP has sent to your number",Toast.LENGTH_LONG).show();
        ChangeMobileNumber changeMobileNumber=new ChangeMobileNumber(mobilenumber,countrycode);

        ApiCallingService.User.changeMobileNumber(context,changeMobileNumber)
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        if (response.code() == 200) {
                            if (response.body().getStatus()) {

                                Toast.makeText(context,"OTP has sent to your number",Toast.LENGTH_LONG).show();
                                verifyOTP();

                            } else {

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

}

package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.profile.Preference;
import com.cncoding.teazer.ui.fragment.activity.BlockUserList;
import com.cncoding.teazer.ui.fragment.activity.InviteFriend;
import com.cncoding.teazer.ui.fragment.activity.PasswordChange;
import com.cncoding.teazer.utilities.SharedPrefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.branch.referral.Branch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.logout;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by farazhabib on 03/12/17.
 */

public class FragmentSettings extends BaseFragment {

//    @BindView(R.id.text_block_layout) ProximaNovaRegularTextView text_block;
//    @BindView(R.id.recentlyDeleted) ProximaNovaRegularTextView recentlyDeleted;
//    @BindView(R.id.logoutLayout) ProximaNovaRegularTextView logout;
//    @BindView(R.id.changePassword) TextView changePassword;
//    @BindView(R.id.changeCategoriesLayout) ProximaNovaRegularTextView changeCategoriesLayout;
//    @BindView(R.id.invite_friends) ProximaNovaSemiBoldTextView inviteFriendsLayout;
//    @BindView(R.id.deactivateAccountLayout) ProximaNovaRegularTextView deactivateAccountLayout;

    Context context;
    private static final int PRIVATE_STATUS = 1;
    private static final int PUBLIC_STATUS = 2;
    boolean flag = false;
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String PREFRENCES = "prefrences";
    int accountType;
    ArrayList<Preference> preferencesList;
    ChangeCategoriesListener mListener;
    Switch simpleSwitch;
    @BindView(R.id.saveVideosSwitch)
    Switch saveVideosSwitch;
    @BindView(R.id.simpleSwitchshowingreactions)
    Switch simpleSwitchshowingreactions;
    @BindView(R.id.text_hide_layout)
    ProximaNovaRegularTextView hidevideos;

    public static FragmentSettings newInstance(String accountType, ArrayList<Preference> list) {
        FragmentSettings fragment = new FragmentSettings();
        Bundle args = new Bundle();
        args.putString(ACCOUNT_TYPE, accountType);
        args.putParcelableArrayList(PREFRENCES,list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        if (bundle != null) {
            accountType = Integer.parseInt(bundle.getString(ACCOUNT_TYPE));
            preferencesList=bundle.getParcelableArrayList(PREFRENCES);


        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        simpleSwitch = view.findViewById(R.id.simpleSwitch);
        context = container.getContext();
        ButterKnife.bind(this, view);

        hidevideos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.hideVideoList();

            }
        });
        simpleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simpleSwitch.isChecked()) {
                    publicprivateProfile(PRIVATE_STATUS);
                } else {
                    publicprivateProfile(PUBLIC_STATUS);
                }

            }
        });
        simpleSwitchshowingreactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simpleSwitchshowingreactions.isChecked()) {
                    
                    resetPrefrences(1,"show reactions to other",1);


                } else {
                    resetPrefrences(1,"hide reactions to other",0);



                }

            }
        });

        //save video to gallery switch button
        saveVideosSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveVideosSwitch.isChecked() && saveVideosSwitch.isPressed()) {
                    SharedPrefs.setSaveVideoFlag(getContext(), true);
                } else if(!saveVideosSwitch.isChecked() && saveVideosSwitch.isPressed()){
                    SharedPrefs.setSaveVideoFlag(getContext(), false);
                }
            }
        });

        if(SharedPrefs.getSaveVideoFlag(getContext()))
        {
            saveVideosSwitch.setChecked(true);
        }
        else
            saveVideosSwitch.setChecked(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (accountType == 1) {
            simpleSwitch.setChecked(true);
        } else {
            simpleSwitch.setChecked(false);
        }
        simpleSwitchshowingreactions.setChecked(false);
    }


    @OnClick(R.id.text_block_layout)
    public void blockListClicked() {

        startActivity(new Intent(context, BlockUserList.class));
    }

    @OnClick(R.id.deactivateAccountLayout)
    public void deactivateClicked() {
        mListener.deactivateAccountListener();
    }

//    @OnClick(R.id.recentlyDeleted) public void recentlyDeletedClicked() {
//    }

    @OnClick(R.id.changePassword)
    public void changePasswordClicked() {

        startActivity(new Intent(context, PasswordChange.class));
    }

    @OnClick(R.id.invite_friends)
    public void inviteFriendsClicked() {

        startActivity(new Intent(context, InviteFriend.class));
    }

    @OnClick(R.id.changeCategoriesLayout)
    public void changeCategoriesClicked() {

        mListener.changeCategoriesListener();
    }

    @OnClick(R.id.logoutLayout)
    public void logoutClicked() {
        logout(context, getActivity());
        Branch.getInstance(getApplicationContext()).logout();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ChangeCategoriesListener) context;
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void publicprivateProfile(final int status) {
        ApiCallingService.User.setAccountVisibility(status, context).enqueue(new Callback<ResultObject>() {

            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {

                        if (status == 1) {
                            ProfileFragment.checkprofileupdated = true;

                            Toast.makeText(context, "Your account has become private", Toast.LENGTH_SHORT).show();
                        } else {

                            ProfileFragment.checkprofileupdated = true;
                            Toast.makeText(context, "Your account has become public", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });
    }


    public void resetPrefrences(int prefrenceId, String prefrencesname, int prefrenceValue)
    {
        ArrayList<Preference>list=new ArrayList<>();

        list.add(new Preference(prefrenceId,prefrencesname,prefrenceValue));

        ApiCallingService.User.resetPrefrences(context,preferencesList).enqueue(new Callback<ResultObject>() {

            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {

                        if (true) {

                            Toast.makeText(context, "Your reacition  is visible", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Your reaction is invisible", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.facebookLink, R.id.instagramLink, R.id.helpCenterLink, R.id.legalLink, R.id.privacyPolicyLink, R.id.tncLink, R.id.licenceLink})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.facebookLink:
                Intent fbBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/teaZer.social.media.application/"));
                startActivity(fbBrowserIntent);
                break;
            case R.id.instagramLink:
                Intent instaBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/cnapplications/"));
                startActivity(instaBrowserIntent);
                break;
            case R.id.helpCenterLink:
                Intent hcBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                startActivity(hcBrowserIntent);
                break;
            case R.id.legalLink:
                Intent legalBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                startActivity(legalBrowserIntent);
                break;
            case R.id.privacyPolicyLink:
                Intent privacyBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                startActivity(privacyBrowserIntent);
                break;
            case R.id.tncLink:
                Intent tncBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                startActivity(tncBrowserIntent);
                break;
            case R.id.licenceLink:
                Intent licenceBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                startActivity(licenceBrowserIntent);
                break;
        }
    }


    public interface ChangeCategoriesListener {
        void changeCategoriesListener();
        void deactivateAccountListener();
        void hideVideoList();

    }
}

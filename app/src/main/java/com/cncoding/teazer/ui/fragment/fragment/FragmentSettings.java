package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.ui.fragment.activity.BlockUserList;
import com.cncoding.teazer.ui.fragment.activity.InviteFriend;
import com.cncoding.teazer.ui.fragment.activity.PasswordChange;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
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

public class FragmentSettings extends Fragment {

    @BindView(R.id.simpleSwitch)
    Switch simpleSwitch;
//    @BindView(R.id.text_block_layout) ProximaNovaRegularTextView text_block;
//    @BindView(R.id.recentlyDeleted) ProximaNovaRegularTextView recentlyDeleted;
//    @BindView(R.id.logoutLayout) ProximaNovaRegularTextView logout;
//    @BindView(R.id.changePassword) TextView changePassword;
//    @BindView(R.id.changeCategoriesLayout) ProximaNovaRegularTextView changeCategoriesLayout;
//    @BindView(R.id.invite_friends) ProximaNovaSemiboldTextView inviteFriendsLayout;
//    @BindView(R.id.deactivateAccountLayout) ProximaNovaRegularTextView deactivateAccountLayout;

    Context context;
    private static final int PRIVATE_STATUS = 1;
    private static final int PUBLIC_STATUS = 2;
    boolean flag = false;
    public static final String ACCOUNT_TYPE = "accountType";
    int accountType;
    ChangeCategoriesListener mListener;

    public static FragmentSettings newInstance(String accountType) {
        FragmentSettings fragment = new FragmentSettings();
        Bundle args = new Bundle();
        args.putString(ACCOUNT_TYPE, accountType);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            accountType = Integer.parseInt(bundle.getString(ACCOUNT_TYPE));
        }
    }

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        context = container.getContext();
        ButterKnife.bind(this, view);
        if (!flag) {
            simpleSwitch.setChecked(accountType == 1);
            flag = true;
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @OnClick(R.id.text_block_layout)
    public void blockListClicked() {
        startActivity(new Intent(context, BlockUserList.class));
    }

    @OnCheckedChanged(R.id.simpleSwitch)
    public void switchClicked(boolean isChecked) {
        if (flag) {
            publicPrivateProfile(isChecked ? PRIVATE_STATUS : PUBLIC_STATUS);
        }
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
    public void onResume() {
        super.onResume();
    }

    public void publicPrivateProfile(final int status) {
        ApiCallingService.User.setAccountVisibility(status, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                try {
                    boolean b = response.body().getStatus();
                    if (b) {
                        if (status == 1)
                            Toast.makeText(context, "Your account has become private", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "Your account has become public", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
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
                Intent instaBrowserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/teaZer.social.media.application/"));
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
    }
}

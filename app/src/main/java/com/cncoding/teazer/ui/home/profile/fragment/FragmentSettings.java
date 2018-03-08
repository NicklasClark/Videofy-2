package com.cncoding.teazer.ui.home.profile.fragment;

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
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.profile.Preference;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.home.profile.activity.BlockUserList;
import com.cncoding.teazer.ui.home.profile.activity.InviteFriend;
import com.cncoding.teazer.ui.home.profile.activity.PasswordChange;
import com.cncoding.teazer.utilities.common.SharedPrefs;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.branch.referral.Branch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.AuthUtils.logout;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getCanSaveMediaOnlyOnWiFi;
import static com.cncoding.teazer.utilities.common.SharedPrefs.getSaveVideoFlag;
import static com.cncoding.teazer.utilities.common.SharedPrefs.setCanSaveMediaOnlyOnWiFi;
import static com.cncoding.teazer.utilities.common.SharedPrefs.setSaveVideoFlag;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 *
 * Created by farazhabib on 03/12/17.
 */

public class FragmentSettings extends BaseFragment {

    public static final String ACCOUNT_TYPE = "accountType";
    public static final String PREFERENCES = "preferences";
    private static final int PRIVATE_STATUS = 1;
    private static final int PUBLIC_STATUS = 2;

    @BindView(R.id.save_videos_switch) Switch saveVideosSwitch;
    @BindView(R.id.private_account_switch) Switch privateAccountSwitch;
    @BindView(R.id.prefetch_videos_switch) Switch prefetchVideosSwitch;
    @BindView(R.id.text_hide_layout) ProximaNovaRegularTextView hideVideos;
    @BindView(R.id.prefetch_media_detail) ProximaNovaRegularTextView prefetchMediaDetail;
    @BindView(R.id.simpleSwitchshowingreactions) Switch simpleSwitchShowingReactions;

    int accountType;
    ArrayList<Preference> preferencesList;
    ChangeCategoriesListener mListener;

    public static FragmentSettings newInstance(String accountType, ArrayList<Preference> list) {
        FragmentSettings fragment = new FragmentSettings();
        Bundle args = new Bundle();
        args.putString(ACCOUNT_TYPE, accountType);
        args.putParcelableArrayList(PREFERENCES,list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            accountType = Integer.parseInt(bundle.getString(ACCOUNT_TYPE));
            preferencesList=bundle.getParcelableArrayList(PREFERENCES);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        privateAccountSwitch = view.findViewById(R.id.private_account_switch);
        ButterKnife.bind(this, view);
        //save video to gallery switch button
        saveVideosSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveVideosSwitch.isChecked() && saveVideosSwitch.isPressed()) {
                    SharedPrefs.setSaveVideoFlag(getContext(), true);
                } else if(!saveVideosSwitch.isChecked() && saveVideosSwitch.isPressed()) {
                    SharedPrefs.setSaveVideoFlag(getContext(), false);
                }
            }
        });
        simpleSwitchShowingReactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (simpleSwitchShowingReactions.isChecked()) {


                    resetPrefrences(1,"show reactions to other",1);



                } else{

                    resetPrefrences(1,"hide reactions to other",0);


                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            int valueofPrefrencese=preferencesList.get(0).getValue();

            if(valueofPrefrencese==0){

                simpleSwitchShowingReactions.setChecked(false);
            }
            else
            {
                simpleSwitchShowingReactions.setChecked(true);

            }
            privateAccountSwitch.setChecked(accountType == 1);
            saveVideosSwitch.setChecked(getSaveVideoFlag(getContext()));
            prefetchVideosSwitch.setChecked(getCanSaveMediaOnlyOnWiFi(context));
            saveVideosSwitch.setChecked(SharedPrefs.getSaveVideoFlag(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.text_hide_layout) public void hideVideosClicked() {
        mListener.hideVideoList();
    }

    @OnClick(R.id.text_block_layout) public void blockListClicked() {
        startActivity(new Intent(context, BlockUserList.class));
    }

    @OnClick(R.id.deactivateAccountLayout) public void deactivateClicked() {
        mListener.deactivateAccountListener();
    }

//    @OnClick(R.id.recentlyDeleted) public void recentlyDeletedClicked() {}

    @OnClick(R.id.changePassword) public void changePasswordClicked() {
        startActivity(new Intent(context, PasswordChange.class));
    }

    @OnClick(R.id.invite_friends) public void inviteFriendsClicked() {
        startActivity(new Intent(context, InviteFriend.class));
    }

    @OnClick(R.id.changeCategoriesLayout) public void changeCategoriesClicked() {
        mListener.changeCategoriesListener();
    }

    @OnClick(R.id.logoutLayout) public void logoutClicked() {
        logout(context, getActivity());
        Branch.getInstance(getApplicationContext()).logout();
    }

    /**
     * Save video to gallery switch button.
     */
    @OnCheckedChanged(R.id.save_videos_switch) public void setSaveVideosSwitch (boolean isChecked) {
        setSaveVideoFlag(context, isChecked);
    }

    @OnCheckedChanged(R.id.private_account_switch) public void setPrivateAccountSwitch (boolean isChecked) {
        publicPrivateProfile(isChecked ?
                accountType == 2 ? PRIVATE_STATUS : null :
                accountType == 1 ? PUBLIC_STATUS : null);
    }

    /**
     * Prefetch videos only on wifi switch button.
     */
    @OnCheckedChanged(R.id.prefetch_videos_switch) public void setPrefetchVideosSwitch (boolean isChecked) {
        setCanSaveMediaOnlyOnWiFi(context, isChecked);
        prefetchMediaDetail.setText(isChecked ? R.string.prefetch_media_on_detail : R.string.prefetch_media_off_detail);
    }

    @OnClick({R.id.facebookLink, R.id.instagramLink, R.id.helpCenterLink, R.id.legalLink, R.id.privacyPolicyLink, R.id.tncLink, R.id.licenceLink})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.facebookLink:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/teaZer.social.media.application/"));
                break;
            case R.id.instagramLink:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/cnapplications/"));
                break;
            case R.id.helpCenterLink:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                break;
            case R.id.legalLink:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                break;
            case R.id.privacyPolicyLink:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                break;
            case R.id.tncLink:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                break;
            case R.id.licenceLink:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.teazer_support_url)));
                break;
        }
        if (intent != null) startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ChangeCategoriesListener) context;
    }

    public void publicPrivateProfile(final Integer status) {
        if (status != null) {
            ApiCallingService.User.setAccountVisibility(status, context).enqueue(new Callback<ResultObject>() {

                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                    try {
                        boolean b = response.body().getStatus();
                        if (b) {
                            if (status == 1) {
                                FragmentNewProfile2.checkprofileupdated = true;

                                Toast.makeText(context, "Your account has become private", Toast.LENGTH_SHORT).show();
                            } else {

                                FragmentNewProfile2.checkprofileupdated = true;
                                Toast.makeText(context, "Your account has become public", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                            Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
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
    }

    public void resetPrefrences(int prefrenceId, String prefrencesname,final int prefrenceValue) {
        ArrayList<Preference>list=new ArrayList<>();
        list.add(new Preference(prefrenceId,prefrencesname,prefrenceValue));
        ApiCallingService.User.resetPrefrences(context,list).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {
                       FragmentNewProfile2.checkprofileupdated = true;

                        if (prefrenceValue==0) {

                            Toast.makeText(context, "Your reaction is hidden", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context, "Your reacition  is visible", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface ChangeCategoriesListener {
        void changeCategoriesListener();
        void deactivateAccountListener();
        void hideVideoList();
    }
}

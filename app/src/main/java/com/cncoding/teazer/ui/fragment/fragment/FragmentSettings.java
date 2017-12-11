package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.ui.fragment.activity.BlockUserList;
import com.cncoding.teazer.ui.fragment.activity.InviteFriend;
import com.cncoding.teazer.ui.fragment.activity.PasswordChange;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.branch.referral.Branch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.logout;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by farazhabib on 03/12/17.
 */

public class FragmentSettings extends Fragment{

    @BindView(R.id.text_block_layout)
    LinearLayout text_block;
    @BindView(R.id.simpleSwitch)
    Switch simpleSwitch;

    @BindView(R.id.recentelyDeleted)
    LinearLayout recentelyDeleted;

    @BindView(R.id.logoutlayout)
    LinearLayout logout;
    @BindView(R.id.changePassword)
    TextView changePassword;

    @BindView(R.id.changecategoriesLayout)
    LinearLayout changecategoriesLayout;

    @BindView(R.id.invitefriendslayout)
    LinearLayout invitefriendslayout;

    @BindView(R.id.dectivateAccountLayout)
    LinearLayout dectivateAccountLayout;
    Context context;
    private static final  int PRIVATE_STATUS=1;
    private static final  int PUBLIC_STATUS=2;
    boolean flag =false;
    public static final String   ACCOUNT_TYPE="accountType";
    int accountType;
    ChangeCategoriesListener mlistener;





    public static FragmentSettings newInstance( String accountType) {
        FragmentSettings fragment=new FragmentSettings();
        Bundle args = new Bundle();
        args.putString(ACCOUNT_TYPE, accountType);
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            accountType = Integer.parseInt(bundle.getString(ACCOUNT_TYPE));
        }
    }
    @Override


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        context=container.getContext();
        ButterKnife.bind(this,view);
        if(accountType==1)
        {
            simpleSwitch.setChecked(true);
        }
        else
        {
            simpleSwitch.setChecked(false);
        }

        text_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,BlockUserList.class);
                startActivity(intent);
            }
        });

        simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    flag=true;
                    publicprivateProfile(PRIVATE_STATUS);
                } else {
                    flag=true;
                    publicprivateProfile(PUBLIC_STATUS);
                }
            }
        });

        dectivateAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.deactivateAccountListener();

            }
        });
        recentelyDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(context, getActivity());
                Branch.getInstance(getApplicationContext()).logout();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,PasswordChange.class);
                startActivity(intent);
            }
        });
        invitefriendslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,InviteFriend.class);
                startActivity(intent);
            }
        });

        changecategoriesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mlistener.changeCategoriesListener();


            }
        });
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mlistener=(ChangeCategoriesListener)context;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void publicprivateProfile(final int status)
    {
        ApiCallingService.User.setAccountVisibility(status, context).enqueue(new Callback<ResultObject>() {

            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {

                try {
                    boolean b = response.body().getStatus();
                    if (b == true)
                    {

                        if(status==1)
                            Toast.makeText(context, "Your account has become private", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(context, "Your account has become public", Toast.LENGTH_LONG).show();}
                    else
                    {

                        Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG).show();
                    }

                }
                catch (Exception e) {

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

    public interface ChangeCategoriesListener
    {
        public void changeCategoriesListener();
        public void deactivateAccountListener();

    }


}

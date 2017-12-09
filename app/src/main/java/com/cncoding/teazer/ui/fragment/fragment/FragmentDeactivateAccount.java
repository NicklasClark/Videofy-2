package com.cncoding.teazer.ui.fragment.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.BlockUserListAdapter;
import com.cncoding.teazer.adapter.DeactivateAccountUserAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.model.profile.deactivateaccount.DeactivateAccountRequest;
import com.cncoding.teazer.model.profile.deactivateaccount.DeactivateReasonList;
import com.cncoding.teazer.ui.fragment.activity.BlockUserList;
import com.cncoding.teazer.ui.fragment.activity.InviteFriend;
import com.cncoding.teazer.ui.fragment.activity.PasswordChange;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.AuthUtils.logout;

/**
 * Created by farazhabib on 06/12/17.
 */

public class FragmentDeactivateAccount extends Fragment {


    Context context;
    private static final int PRIVATE_STATUS = 1;
    private static final int PUBLIC_STATUS = 2;
    public static final String ACCOUNT_TYPE = "accountType";
    int accountType;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;
    @BindView(R.id.commentLayout)
    LinearLayout commentLayout;

    @BindView(R.id.layout)
    RelativeLayout layout;

    @BindView(R.id.deactivationreason)
    EditText deactivation_reason;
    int deactivation_id;

    @BindView(R.id.deactivateAccount)
    Button deactivateAccount;
    List<DeactivateReasonList> list;
    RecyclerView recyclerView;
    DeactivateAccountUserAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    public static FragmentDeactivateAccount newInstance() {
        FragmentDeactivateAccount fragment = new FragmentDeactivateAccount();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
//        if (bundle != null) {
//            accountType = Integer.parseInt(bundle.getString(ACCOUNT_TYPE));
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deactivate_account, container, false);
        context = container.getContext();
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        deactivateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            String deactivateReason=deactivation_reason.getText().toString();

                if (deactivateReason.isEmpty()) {
                    deactivation_reason.setError("Enter Reason");
                    deactivation_reason.requestFocus();
                    return;

                }
            DeactivateAccountRequest deactivateAccountRequest=new DeactivateAccountRequest(deactivation_id,deactivateReason);
            deactivateID(deactivateAccountRequest);


            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        layout.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
        getDeactivationResonList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mlistener=(DeactivateAccountListener)context;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void getDeactivationResonList() {
        ApiCallingService.Application.getDeactivationTypesList().enqueue(new Callback<List<DeactivateReasonList>>() {

            @Override
            public void onResponse(Call<List<DeactivateReasonList>> call, Response<List<DeactivateReasonList>> response) {

                if (response.code() == 200) {
                    try {
                        list = response.body();
                        adapter = new DeactivateAccountUserAdapter(context, list, FragmentDeactivateAccount.this);
                        recyclerView.setAdapter(adapter);
                        progress_bar.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);


                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }

            }


            @Override
            public void onFailure(Call<List<DeactivateReasonList>> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });

    }

    public void visibleCommentLayout(boolean flag,int deactivationid)
    {
        if (flag) {
            deactivation_id = deactivationid;
            commentLayout.setVisibility(View.VISIBLE);
        }
        else
            commentLayout.setVisibility(View.GONE);
    }


    public void deactivateID(DeactivateAccountRequest deactivateAccountRequest)
    {
        ApiCallingService.User.deactivateAccount(context,deactivateAccountRequest).enqueue(new Callback<ResultObject>() {

            @Override
            public void onResponse(Call<ResultObject>call, Response<ResultObject> response) {

                if (response.code()==200) {
                    try {
                        if(response.body().getStatus())
                        {
                            logout(context, (Activity) context);
                            //Toast.makeText(context,"Your account has been deactivated",Toast.LENGTH_SHORT).show();

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();

            }
        });

    }
}

package com.cncoding.teazer.ui.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ReportUserTitleAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.model.profile.reportPost.ReportPostSubTitleResponse;
import com.cncoding.teazer.model.profile.reportPost.ReportPostTitlesResponse;
import com.cncoding.teazer.model.profile.reportuser.ReportUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amit on 24/11/17.
 */

public class ReportUserDialogFragment extends DialogFragment implements ReportUserTitleAdapter.TitleSelectedInterface {

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportUserTitleAdapter reportuserTitleAdapter = null;
    private Integer selectedReportId;
    private int userId;
    private boolean canReact;

    public ReportUserDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReportUserDialogFragment newInstance(Integer userId) {
        ReportUserDialogFragment frag = new ReportUserDialogFragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getInt("userId");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_user, container);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reportTitlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getPostReportTypes();
    }

    private void getPostReportTypes()
    {
        ApiCallingService.Application.getProfileReportTypes().enqueue(new Callback<List<ReportPostTitlesResponse>>() {
            @Override
            public void onResponse(Call<List<ReportPostTitlesResponse>> call, Response<List<ReportPostTitlesResponse>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        reportuserTitleAdapter = new ReportUserTitleAdapter(response.body().get(0).getSubReports(), getContext(), ReportUserDialogFragment.this);
                        reportTitlesRecyclerView.setAdapter(reportuserTitleAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ReportPostTitlesResponse>> call, Throwable t) {
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void titleSelected(ReportPostSubTitleResponse value) {
        selectedReportId = value.getReportTypeId();
        reportUserServiceCall();
    }

    private void reportUserServiceCall() {

        ApiCallingService.User.reportUsers(new ReportUser(userId, selectedReportId), getContext()).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "User reported", Toast.LENGTH_SHORT).show();
                    ReportUserDialogFragment.this.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                ReportUserDialogFragment.this.dismiss();
            }
        });
    }
}

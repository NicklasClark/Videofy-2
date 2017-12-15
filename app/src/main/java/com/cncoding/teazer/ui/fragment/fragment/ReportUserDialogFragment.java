package com.cncoding.teazer.ui.fragment.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ReportUserTitleAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.model.profile.reportPost.ReportPostSubTitleResponse;
import com.cncoding.teazer.model.profile.reportPost.ReportPostTitlesResponse;
import com.cncoding.teazer.model.profile.reportuser.ReportUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;

/**
 * Created by amit on 24/11/17.
 */

public class ReportUserDialogFragment extends DialogFragment implements ReportUserTitleAdapter.TitleSelectedInterface {

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportUserTitleAdapter reportuserTitleAdapter = null;
    @BindView(R.id.report_remark)
    EditText reportRemark;
    @BindView(R.id.submitReport)
    ProximaNovaSemiboldButton submitReport;
    private Integer selectedReportId;
    private int userId;
    private boolean canReact;
    private boolean userReportSelected = false;

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
        if(!userReportSelected)
            disableView(submitReport, true);
        getPostReportTypes();
    }

    private void getPostReportTypes() {
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
        enableView(submitReport);
    }

    private void reportUserServiceCall() {

        ApiCallingService.User.reportUsers(new ReportUser(userId, selectedReportId, reportRemark.getText().toString()), getContext()).enqueue(new Callback<ResultObject>() {
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

    private void showReportUserAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getString(R.string.repot_post_dialog_title));
        builder.setMessage(getContext().getString(R.string.report_post_dialog_message));

        String positiveText = getContext().getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            reportUserServiceCall();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        String negativeText = getContext().getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick(R.id.submitReport)
    public void onViewClicked() {
        showReportUserAlertDialog();
    }
}

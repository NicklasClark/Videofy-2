package com.cncoding.teazer.ui.home.profile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.user.ReportUser;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.ui.home.profile.adapter.ReportUserTitleAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;

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
    @BindView(R.id.loader)
    DynamicProgress loader;
    private Integer selectedReportId;
    private int userId;
    private boolean canReact;
    private boolean userReportSelected = false;
    private String userName;

    public ReportUserDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReportUserDialogFragment newInstance(Integer userId, String username) {
        ReportUserDialogFragment frag = new ReportUserDialogFragment();
        Bundle args = new Bundle();
        args.putInt("userId", userId);
        args.putString("userName", username);
        args.putString("blah1", username);
        args.putString("blah2", username);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getInt("userId");
        userName = getArguments().getString("userName");
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

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        reportTitlesRecyclerView.addItemDecoration(decoration);
        reportTitlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (!userReportSelected)
            disableView(submitReport, true);
        getPostReportTypes();
    }

    private void getPostReportTypes() {
        loader.setVisibility(View.VISIBLE);
        ApiCallingService.Application.getProfileReportTypes().enqueue(new Callback<List<ReportTypes>>() {
            @Override
            public void onResponse(Call<List<ReportTypes>> call, Response<List<ReportTypes>> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            reportuserTitleAdapter = new ReportUserTitleAdapter(response.body().get(0).getSubReports(), getContext(), ReportUserDialogFragment.this, userName);
                            reportTitlesRecyclerView.setAdapter(reportuserTitleAdapter);
                        }
                        loader.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ReportTypes>> call, Throwable t) {
                loader.setVisibility(View.GONE);
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
                    showReportUserSuccessDialog();
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
        builder.setTitle(getContext().getString(R.string.report_user_dialog_title));
        builder.setMessage(getContext().getString(R.string.report_user_dialog_message));

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

    private void showReportUserSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getString(R.string.report_user_success_dialog_title));
        builder.setMessage(getContext().getString(R.string.report_user_success_dialog_message));

        String positiveText = getContext().getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReportUserDialogFragment.this.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @OnClick({R.id.btnClose, R.id.submitReport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                this.dismiss();
                break;
            case R.id.submitReport:
                showReportUserAlertDialog();
                break;
        }
    }
}

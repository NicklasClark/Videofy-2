package com.cncoding.teazer.ui.home.profile.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.post.ReportPost;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.ui.home.profile.adapter.ReportPostTitleAdapter;

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

public class ReportPostDialogFragment extends DialogFragment implements ReportPostSubtitleFragment.ReportSubTitleSelected,
        ReportPostTitleAdapter.TitleSelectedInterface {

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportPostTitleAdapter reportPostTitleAdapter = null;
    @BindView(R.id.report_remark)
    TextInputEditText reportRemark;
    @BindView(R.id.submitReport)
    ProximaNovaSemiboldButton submitReport;
    @BindView(R.id.loader)
    DynamicProgress loader;
    private Integer selectedReportId;
    private int postId;
    private boolean canReact;
    public static boolean postReportOptionSelected = false;
    private String userName;

    public ReportPostDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReportPostDialogFragment newInstance(Integer postId, boolean canReact, String userName) {
        ReportPostDialogFragment frag = new ReportPostDialogFragment();
        Bundle args = new Bundle();
        args.putInt("postId", postId);
        args.putBoolean("canReact", canReact);
        args.putString("userName", userName);
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
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        postId = getArguments().getInt("postId");
        canReact = getArguments().getBoolean("canReact");
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
        if (!postReportOptionSelected)
            disableView(submitReport, true);
        getPostReportTypes();
    }

    private void getPostReportTypes() {
        loader.setVisibility(View.VISIBLE);
        ApiCallingService.Application.getPostReportTypes().enqueue(new Callback<List<ReportTypes>>() {
            @Override
            public void onResponse(Call<List<ReportTypes>> call, Response<List<ReportTypes>> response) {
                try {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            reportPostTitleAdapter = new ReportPostTitleAdapter(response.body(), getContext(), ReportPostDialogFragment.this, userName);
                            reportTitlesRecyclerView.setAdapter(reportPostTitleAdapter);
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
    public void onSubTitleSelected(ReportPostSubTitleResponse reportPostSubTitleResponse, String reportRemark) {
        selectedReportId = reportPostSubTitleResponse.getReportTypeId();
        postReportOptionSelected = true;
        enableView(submitReport);
        showReportPostAlertDialog(reportRemark);
    }


    @Override
    public void titleSelected(ReportTypes value) {
        selectedReportId = value.getReportTypeId();
        postReportOptionSelected = true;
        enableView(submitReport);
    }

    private void reportPostServiceCall(String reportRemark) {

        ApiCallingService.Posts.reportPost(new ReportPost(postId, selectedReportId, reportRemark.equals("") ? null : reportRemark), getContext()).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Post reported", Toast.LENGTH_SHORT).show();
                    postReportOptionSelected = false;
                    ReportPostDialogFragment.this.dismiss();
                } else
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                postReportOptionSelected = false;
                ReportPostDialogFragment.this.dismiss();
            }
        });
    }

    private void showReportPostAlertDialog(final String reportRemark) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getContext().getString(R.string.report_post_dialog_title));
        builder.setMessage(getContext().getString(R.string.report_post_dialog_message));
        String positiveText = getContext().getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {
                            reportPostServiceCall(reportRemark);
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

    @Override
    public void onResume() {
        super.onResume();
        if (!postReportOptionSelected)
            disableView(submitReport, true);
    }

    @OnClick({R.id.btnClose, R.id.submitReport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                this.dismiss();
                break;
            case R.id.submitReport:
                showReportPostAlertDialog(reportRemark.getText().toString());
                break;
        }
    }
}

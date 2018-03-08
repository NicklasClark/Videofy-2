package com.cncoding.teazer.ui.home.profile.fragment;

import android.app.Dialog;
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

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.ui.customviews.common.DynamicProgress;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.ui.home.profile.adapter.ReportPostSubtitleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.cncoding.teazer.ui.home.profile.fragment.ReportPostDialogFragment.postReportOptionSelected;
import static com.cncoding.teazer.utilities.common.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.common.ViewUtils.enableView;

/**
 * Created by amit on 24/11/17.
 */

public class ReportPostSubtitleFragment extends DialogFragment implements ReportPostSubtitleAdapter.SubTitleSelectedInterface {

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportPostSubtitleAdapter reportPostSubtitleAdapter = null;
    @BindView(R.id.submitReport)
    ProximaNovaSemiboldButton submitReport;
    @BindView(R.id.loader)
    DynamicProgress loader;
    @BindView(R.id.report_remark)
    TextInputEditText reportRemark;
    private ReportTypes reportTypes;
    private View rootView;
    private ReportPostSubTitleResponse selectedReportType;
    private String userName;

    public ReportPostSubtitleFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReportPostSubtitleFragment newInstance(ReportTypes reportTypes, String userName) {
        ReportPostSubtitleFragment frag = new ReportPostSubtitleFragment();
        Bundle args = new Bundle();
        args.putParcelable("report", reportTypes);
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
        reportTypes = getArguments().getParcelable("report");
        userName = getArguments().getString("userName");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_report_user, container);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!postReportOptionSelected)
            disableView(submitReport, true);

        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
        reportTitlesRecyclerView.addItemDecoration(decoration);
        reportTitlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getPostSubReportTypes();
    }

    private void getPostSubReportTypes() {
        loader.setVisibility(View.VISIBLE);
        reportPostSubtitleAdapter = new ReportPostSubtitleAdapter(reportTypes.getSubReports(), getContext(), ReportPostSubtitleFragment.this, userName);
        reportTitlesRecyclerView.setAdapter(reportPostSubtitleAdapter);
        loader.setVisibility(View.GONE);
    }

    @OnClick({R.id.btnClose, R.id.submitReport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnClose:
                this.dismiss();
                break;
            case R.id.submitReport:
                ReportSubTitleSelected listener = (ReportSubTitleSelected) getTargetFragment();
                listener.onSubTitleSelected(selectedReportType, reportRemark.getText().toString());
                dismiss();
                break;
        }
    }

    public interface ReportSubTitleSelected {
        void onSubTitleSelected(ReportPostSubTitleResponse reportPostSubTitleResponse, String reportRemark);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void subtitleSelected(ReportPostSubTitleResponse value) {
        selectedReportType = value;
        postReportOptionSelected = true;
        if (postReportOptionSelected)
            enableView(submitReport);
    }
}

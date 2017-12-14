package com.cncoding.teazer.ui.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ReportPostSubtitleAdapter;
import com.cncoding.teazer.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 24/11/17.
 */

public class ReportPostSubtitleFragment extends DialogFragment implements ReportPostSubtitleAdapter.SubTitleSelectedInterface {

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportPostSubtitleAdapter reportPostSubtitleAdapter = null;
    private ReportPostTitlesResponse reportPostTitlesResponse;
    private View rootView;

    public ReportPostSubtitleFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReportPostSubtitleFragment newInstance(ReportPostTitlesResponse reportPostTitlesResponse) {
        ReportPostSubtitleFragment frag = new ReportPostSubtitleFragment();
        Bundle args = new Bundle();
        args.putParcelable("report", reportPostTitlesResponse);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportPostTitlesResponse = getArguments().getParcelable("report");
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
        reportTitlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getPostSubReportTypes();
    }

    private void getPostSubReportTypes()
    {
        reportPostSubtitleAdapter = new ReportPostSubtitleAdapter(reportPostTitlesResponse.getSubReports(), getContext(), ReportPostSubtitleFragment.this);
        reportTitlesRecyclerView.setAdapter(reportPostSubtitleAdapter);
    }



    public interface ReportSubTitleSelected {
        void onSubTitleSelected(ReportPostSubTitleResponse reportPostSubTitleResponse);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void subtitleSelected(ReportPostSubTitleResponse value) {
        ReportSubTitleSelected listener = (ReportSubTitleSelected) getTargetFragment();
        listener.onSubTitleSelected(value);
        dismiss();
    }
}

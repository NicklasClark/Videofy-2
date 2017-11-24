package com.cncoding.teazer.ui.fragment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ReportPostTitleAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.model.profile.reportPost.ReportPostTitlesResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by amit on 24/11/17.
 */

public class ReportPostDialogFragment extends DialogFragment {

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportPostTitleAdapter reportPostTitleAdapter = null;

    public ReportPostDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReportPostDialogFragment newInstance(String title) {
        ReportPostDialogFragment frag = new ReportPostDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
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

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        reportTitlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getPostReportTypes();
    }

    private void getPostReportTypes()
    {
        ApiCallingService.Application.getPostReportTypes().enqueue(new Callback<List<ReportPostTitlesResponse>>() {
            @Override
            public void onResponse(Call<List<ReportPostTitlesResponse>> call, Response<List<ReportPostTitlesResponse>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        reportPostTitleAdapter = new ReportPostTitleAdapter(response.body(), getContext());
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
}

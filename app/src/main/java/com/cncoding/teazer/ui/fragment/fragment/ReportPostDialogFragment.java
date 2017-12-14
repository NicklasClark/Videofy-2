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
import com.cncoding.teazer.adapter.ReportPostTitleAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.model.post.ReportPost;
import com.cncoding.teazer.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * Created by amit on 24/11/17.
 */

public class ReportPostDialogFragment extends DialogFragment implements ReportPostSubtitleFragment.ReportSubTitleSelected,
        ReportPostTitleAdapter.TitleSelectedInterface{

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportPostTitleAdapter reportPostTitleAdapter = null;
    private Integer selectedReportId;
    private int postId;
    private boolean canReact;

    public ReportPostDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReportPostDialogFragment newInstance(Integer postId, boolean canReact) {
        ReportPostDialogFragment frag = new ReportPostDialogFragment();
        Bundle args = new Bundle();
        args.putInt("postId", postId);
        args.putBoolean("canReact", canReact);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postId = getArguments().getInt("postId");
        canReact = getArguments().getBoolean("canReact");
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
        ApiCallingService.Application.getPostReportTypes().enqueue(new Callback<List<ReportPostTitlesResponse>>() {
            @Override
            public void onResponse(Call<List<ReportPostTitlesResponse>> call, Response<List<ReportPostTitlesResponse>> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        reportPostTitleAdapter = new ReportPostTitleAdapter(response.body(), getContext(), ReportPostDialogFragment.this);
                        reportTitlesRecyclerView.setAdapter(reportPostTitleAdapter);
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
    public void onSubTitleSelected(ReportPostSubTitleResponse reportPostSubTitleResponse) {
        selectedReportId = reportPostSubTitleResponse.getReportTypeId();
        reportPostServiceCall();
    }


    @Override
    public void titleSelected(ReportPostTitlesResponse value) {
        selectedReportId = value.getReportTypeId();
        reportPostServiceCall();
    }

    private void reportPostServiceCall() {

        ApiCallingService.Posts.reportPost(new ReportPost(postId, selectedReportId), getContext()).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Post reported", Toast.LENGTH_SHORT).show();
                    ReportPostDialogFragment.this.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                ReportPostDialogFragment.this.dismiss();
            }
        });
    }
}

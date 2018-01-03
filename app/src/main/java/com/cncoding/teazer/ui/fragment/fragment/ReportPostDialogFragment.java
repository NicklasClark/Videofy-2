package com.cncoding.teazer.ui.fragment.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ReportPostTitleAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.ProximaNovaSemiboldButton;
import com.cncoding.teazer.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.model.post.ReportPost;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.cncoding.teazer.utilities.ViewUtils.disableView;
import static com.cncoding.teazer.utilities.ViewUtils.enableView;

/**
 * Created by amit on 24/11/17.
 */

public class ReportPostDialogFragment extends DialogFragment implements ReportPostSubtitleFragment.ReportSubTitleSelected,
        ReportPostTitleAdapter.TitleSelectedInterface {

    @BindView(R.id.reportTitlesRecyclerView)
    RecyclerView reportTitlesRecyclerView;
    ReportPostTitleAdapter reportPostTitleAdapter = null;
    @BindView(R.id.report_remark)
    EditText reportRemark;
    @BindView(R.id.submitReport)
    ProximaNovaSemiboldButton submitReport;
    @BindView(R.id.loader)
    GifTextView loader;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ApiCallingService.Application.getPostReportTypes(getContext()).enqueue(new Callback<List<ReportPostTitlesResponse>>() {
            @Override
            public void onResponse(Call<List<ReportPostTitlesResponse>> call, Response<List<ReportPostTitlesResponse>> response) {
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
            public void onFailure(Call<List<ReportPostTitlesResponse>> call, Throwable t) {
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
    public void titleSelected(ReportPostTitlesResponse value) {
        selectedReportId = value.getReportTypeId();
        postReportOptionSelected = true;
        enableView(submitReport);
    }

    private void reportPostServiceCall(String reportRemark) {

        ApiCallingService.Posts.reportPost(new ReportPost(postId, selectedReportId, reportRemark.equals("")? null:reportRemark), getContext()).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    Toast.makeText(getContext(), "Post reported", Toast.LENGTH_SHORT).show();
                    postReportOptionSelected = false;
                    ReportPostDialogFragment.this.dismiss();
                }
                else
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

    @OnClick(R.id.submitReport)
    public void onViewClicked() {
        showReportPostAlertDialog(reportRemark.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!postReportOptionSelected)
            disableView(submitReport, true);
    }
}

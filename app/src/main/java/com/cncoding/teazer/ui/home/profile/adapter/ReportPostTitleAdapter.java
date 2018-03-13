package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.ui.home.post.homepage.PostsListFragment;
import com.cncoding.teazer.ui.home.profile.fragment.ReportPostDialogFragment;
import com.cncoding.teazer.ui.home.profile.fragment.ReportPostSubtitleFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by amit on 24/11/17.
 */

public class ReportPostTitleAdapter extends RecyclerView.Adapter<ReportPostTitleAdapter.ViewHolder> {

    private final List<ReportTypes> reportsType;
    private final ReportPostDialogFragment fragmentContext;
    private Context context;
    private PostsListFragment postsListFragment;
    private TitleSelectedInterface mAdapterCallback;
    private int lastSelectedRow = -1;
    private String userName;

    public ReportPostTitleAdapter(List<ReportTypes> reportsType, Context context, ReportPostDialogFragment reportPostDialogFragment, String userName) {
        this.reportsType = reportsType;
        this.context = context;
        this.fragmentContext = reportPostDialogFragment;
        this.userName = userName;
        try {
            this.mAdapterCallback = reportPostDialogFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ReportPostTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_post, parent, false);
        return new ReportPostTitleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportPostTitleAdapter.ViewHolder holder, final int position) {
        final ReportTypes report = reportsType.get(position);

        if(position == lastSelectedRow)
            holder.tickView.setVisibility(View.VISIBLE);
        else
            holder.tickView.setVisibility(View.GONE);

        String title = report.getTitle();
        if(title.contains("####"))
        {
            title = title.substring(0, title.indexOf("#")-1) +" "+ userName;
            holder.reportTitle.setText(title);
        }
        else
            holder.reportTitle.setText(report.getTitle());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (report.getSubReports().size()>0) {
                    ReportPostDialogFragment.postReportOptionSelected = false;
                    lastSelectedRow = -1;
                    FragmentManager fm = fragmentContext.getFragmentManager();
                    ReportPostSubtitleFragment reportPostSubTitleDialogFragment = ReportPostSubtitleFragment.newInstance(report, userName);
                    // SETS the target fragment for use later when sending results
                    reportPostSubTitleDialogFragment.setTargetFragment(fragmentContext, 300);
                    reportPostSubTitleDialogFragment.show(fm, "fragment_report_post");
                } else {
                    ReportPostDialogFragment.postReportOptionSelected = false;
                    lastSelectedRow = position;
                    holder.tickView.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                    mAdapterCallback.titleSelected(report);
                }

            }
        });

    }

//    private byte[] getImage(ImageView imageView) {
//        Bitmap bitmap = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//        return outputStream.toByteArray();
//    }

    @Override
    public int getItemCount() {
        return reportsType.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reportTitleLayout) RelativeLayout layout;
        @BindView(R.id.reportTitle)
        ProximaNovaRegularTextView reportTitle;
        @BindView(R.id.tickView) ImageView tickView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface TitleSelectedInterface
    {
        void titleSelected(ReportTypes value);
    }
}
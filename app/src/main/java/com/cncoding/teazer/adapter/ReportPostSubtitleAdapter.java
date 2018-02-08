package com.cncoding.teazer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.post.homepage.PostsListFragment;
import com.cncoding.teazer.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.ui.fragment.fragment.ReportPostSubtitleFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by amit on 24/11/17.
 */

public class ReportPostSubtitleAdapter extends RecyclerView.Adapter<ReportPostSubtitleAdapter.ViewHolder> {

    private final List<ReportPostSubTitleResponse> subReportsType;
    private final ReportPostSubtitleFragment fragmentContext;
    private Context context;
    private PostsListFragment postsListFragment;
    private SubTitleSelectedInterface mAdapterCallback;
    private int lastSelectedRow = -1;
    private String userName;

    public ReportPostSubtitleAdapter(List<ReportPostSubTitleResponse> reportsType, Context context, ReportPostSubtitleFragment reportPostSubtitleFragment, String userName) {
        this.subReportsType = reportsType;
        this.context = context;
        this.fragmentContext = reportPostSubtitleFragment;
        this.userName = userName;
        try {
            this.mAdapterCallback = reportPostSubtitleFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ReportPostSubtitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_post, parent, false);
        return new ReportPostSubtitleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportPostSubtitleAdapter.ViewHolder holder, final int position) {
        final ReportPostSubTitleResponse report = subReportsType.get(position);
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
                lastSelectedRow = position;
                holder.tickView.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
                mAdapterCallback.subtitleSelected(report);
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
        return subReportsType.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reportTitleLayout)
        RelativeLayout layout;
        @BindView(R.id.reportTitle)
        ProximaNovaRegularTextView reportTitle;
        @BindView(R.id.tickView) ImageView tickView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface SubTitleSelectedInterface
    {
        void subtitleSelected(ReportPostSubTitleResponse value);
    }

}

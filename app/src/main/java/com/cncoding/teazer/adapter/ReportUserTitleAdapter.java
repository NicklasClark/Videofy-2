package com.cncoding.teazer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.home.post.homepage.PostsListFragment;
import com.cncoding.teazer.model.application.ReportPostSubTitleResponse;
import com.cncoding.teazer.ui.fragment.fragment.ReportUserDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by amit on 24/11/17.
 */

public class ReportUserTitleAdapter extends RecyclerView.Adapter<ReportUserTitleAdapter.ViewHolder> {

    private final List<ReportPostSubTitleResponse> reportsType;
    private final ReportUserDialogFragment fragmentContext;
    private final String userName;
    private Context context;
    private PostsListFragment postsListFragment;
    private TitleSelectedInterface mAdapterCallback;
    private int lastSelectedRow = -1;

    public ReportUserTitleAdapter(List<ReportPostSubTitleResponse> reportsType, Context context, ReportUserDialogFragment reportUserDialogFragment, String userName) {
        this.reportsType = reportsType;
        this.context = context;
        this.fragmentContext = reportUserDialogFragment;
        this.userName = userName;
        try {
            this.mAdapterCallback = reportUserDialogFragment;
        } catch (ClassCastException e) {
            throw new ClassCastException("Fragment must implement AdapterCallback.");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public ReportUserTitleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_report_post, parent, false);
        return new ReportUserTitleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportUserTitleAdapter.ViewHolder holder, final int position) {
        final ReportPostSubTitleResponse report = reportsType.get(position);

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

                mAdapterCallback.titleSelected(report);
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

        @BindView(R.id.reportTitleLayout)
        RelativeLayout layout;
        @BindView(R.id.reportTitle)
        TextView reportTitle;
        @BindView(R.id.tickView)
        ImageView tickView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public interface TitleSelectedInterface
    {
        void titleSelected(ReportPostSubTitleResponse value);
    }
}

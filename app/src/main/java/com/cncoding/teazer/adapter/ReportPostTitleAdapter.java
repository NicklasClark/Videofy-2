package com.cncoding.teazer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.cncoding.teazer.R;
import com.cncoding.teazer.home.post.PostsListFragment;
import com.cncoding.teazer.model.profile.reportPost.ReportPostTitlesResponse;

import java.io.ByteArrayOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by amit on 24/11/17.
 */

public class ReportPostTitleAdapter extends RecyclerView.Adapter<ReportPostTitleAdapter.ViewHolder> {

    private final List<ReportPostTitlesResponse> reportsType;
    private Context context;
    private PostsListFragment postsListFragment;

    public ReportPostTitleAdapter(List<ReportPostTitlesResponse> reportsType, Context context) {
        this.reportsType = reportsType;
        this.context = context;
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
    public void onBindViewHolder(final ReportPostTitleAdapter.ViewHolder holder, int position) {
        ReportPostTitlesResponse report = reportsType.get(position);
        holder.reportTitle.setText(report.getTitle());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private byte[] getImage(ImageView imageView) {
        Bitmap bitmap = ((GlideBitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public int getItemCount() {
        return reportsType.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.reportTitleLayout) RelativeLayout layout;
        @BindView(R.id.reportTitle)
        TextView reportTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}

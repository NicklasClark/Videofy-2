package com.cncoding.teazer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.profile.delete.DeleteMyVideos;
import com.cncoding.teazer.model.profile.reaction.Reaction;
import com.cncoding.teazer.ui.fragment.activity.ProfileCreationVideos;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by farazhabib on 10/11/17.
 */

public class ProfileMyReactionAdapter extends RecyclerView.Adapter<ProfileMyReactionAdapter.ViewHolder> {

    private List<Reaction> list;
    private Context context;


    public ProfileMyReactionAdapter(Context context, List<Reaction> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public ProfileMyReactionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_myreactions, viewGroup, false);
        return new ProfileMyReactionAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ProfileMyReactionAdapter.ViewHolder viewHolder,  final int i) {

        final Reaction cont = list.get(i);
        final int reactId = cont.getReactId();
        final String videotitle = cont.getReactTitle();
        final int likes = cont.getLikes();
        final int views = cont.getViews();
        final String reactduration = cont.getMediaDetail().getReactDuration();

        final String videourl = cont.getMediaDetail().getReactMediaUrl();
        final String thumb_url = cont.getMediaDetail().getReactThumbUrl();
        final String postowner = cont.getPostOwner().getUserName();
        final int reaction = cont.getReactedBy();

        if (videotitle.equals("") || videotitle == null) {
            viewHolder.videoTitle.setText("No Title");
        } else {
            viewHolder.videoTitle.setText(videotitle);
        }

        viewHolder.post_owner.setText(postowner);
        viewHolder.txtlikes.setText(String.valueOf(likes));
        viewHolder.txtview.setText(String.valueOf(views));
        viewHolder.duration.setText(reactduration + " secs");
        viewHolder.reaction_id.setText(String.valueOf("+" + reaction + "R"));

        Glide.with(context).load(thumb_url)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.material_flat))
                .into(viewHolder.thumbimage);

        viewHolder.playvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileCreationVideos.class);
                intent.putExtra("VideoURL", videourl);
                intent.putExtra("Likes", String.valueOf(likes));
                intent.putExtra("Views",String.valueOf( views));
                intent.putExtra("Title", videotitle);
                context.startActivity(intent);
            }
        });
        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, viewHolder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_profile);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:


                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setTitle("Confirm Deletion...");
                                alertDialog.setMessage("Are you sure you want to delete this video.");
                                alertDialog.setIcon(R.drawable.ic_warning_black_24dp);
                                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog,int which) {
                                        deleteVideos(reactId);
                                        viewHolder.cardView.setVisibility(View.GONE);
                                    }
                                });
                                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.cancel();
                                    }
                                });
                                alertDialog.show();

                                break;

                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    private void deleteVideos(int deleteId) {
        ApiCallingService.React.deleteReaction(deleteId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    if (response.code() == 200) {
                        boolean status = response.body().getStatus();
                        Toast.makeText(context, "Reaction has been deleted", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, "Reaction has not been deleted", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }

        });

    }
    @Override
    public int getItemCount() {
        return list.size() ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTitle;
        private TextView txtlikes;
        private TextView txtview;
        private TextView reaction_id;
        VideoView videoviewContainer;
        ImageView thumbimage;
        CardView cardView;
        View line;
        TextView post_owner;
        TextView duration;
        CircularAppCompatImageView menu;
        ImageView playvideo;
        public ViewHolder(View view) {
            super(view);

            videoTitle = view.findViewById(R.id.videodetails);
            videoviewContainer = view.findViewById(R.id.flContainer);
            thumbimage = view.findViewById(R.id.demoimage);
            playvideo = view.findViewById(R.id.playvideo);
            menu = view.findViewById(R.id.menu);
            post_owner = view.findViewById(R.id.post_owner);
            txtlikes = view.findViewById(R.id.txtlikes);
            txtview = view.findViewById(R.id.txtview);
            duration = view.findViewById(R.id.duration);
            reaction_id = view.findViewById(R.id.reaction_id);
            cardView = view.findViewById(R.id.cardview);

        }
    }
}

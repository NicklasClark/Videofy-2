package com.cncoding.teazer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.giphy.Images;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.react.MyReactions;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.ui.fragment.fragment.FragmentReactionPlayer.OPENED_FROM_PROFILE;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIF;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_GIFHY;
import static com.cncoding.teazer.utilities.CommonUtilities.MEDIA_TYPE_VIDEO;
import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class ProfileMyReactionAdapter extends RecyclerView.Adapter<ProfileMyReactionAdapter.ViewHolder> {

    private List<MyReactions> list;
    private Context context;
    ReactionPlayerListener reactionPlayerListener;
    private boolean isPostClicked = false;

    public ProfileMyReactionAdapter(Context context, List<MyReactions> list) {
        this.context = context;
        this.list = list;
        reactionPlayerListener=( ReactionPlayerListener)context;
    }
    @Override
    public ProfileMyReactionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_myreactions, viewGroup, false);
        return new ProfileMyReactionAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ProfileMyReactionAdapter.ViewHolder viewHolder,  final int i) {
        final MyReactions reactions = list.get(i);
        final int reactId = reactions.getReactId();
        String videoTitle = reactions.getReactTitle();
        final int likes = reactions.getLikes();
        final int views = reactions.getViews();
        final String reactDuration = reactions.getMediaDetail().getReactDuration();
        final String thumb_url = reactions.getMediaDetail().getReactThumbUrl();
        final String postOwner = reactions.getPostOwner().getFirstName();
        final int reaction = reactions.getReactedBy();


        if (videoTitle.equals("")) {
            viewHolder.videoTitle.setText("No Title");
        } else {
            viewHolder.videoTitle.setText(decodeUnicodeString(videoTitle));
        }
        viewHolder.post_owner.setText(postOwner);
        viewHolder.txtlikes.setText(String.valueOf(likes));
        viewHolder.txtview.setText(String.valueOf(views));
        viewHolder.duration.setText(reactDuration);
        viewHolder.reaction_id.setText(String.valueOf("+" + reaction + "R"));

        if (reactions.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
            Glide.with(context)
                    .load(reactions.getMediaDetail().getReactMediaUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.material_flat).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(viewHolder.thumbimage);
            viewHolder.duration.setVisibility(View.GONE);
        }
        else if (reactions.getMediaDetail().getMediaType() == MEDIA_TYPE_VIDEO){
            Glide.with(context).load(thumb_url)
                    .apply(new RequestOptions().placeholder(R.drawable.material_flat))
                    .into(viewHolder.thumbimage);
            viewHolder.duration.setVisibility(View.VISIBLE);
        } else if (reactions.getMediaDetail().getMediaType() == MEDIA_TYPE_GIFHY) {

            Gson gson = new Gson();
            Images images = gson.fromJson(reactions.getMediaDetail().getExternalMeta(), Images.class);
            Glide.with(context)
                    .load(images.getDownsized().getUrl())
                    .apply(new RequestOptions().placeholder(R.drawable.material_flat).diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.bitmapTransform(new FitCenter()))
                    .into(viewHolder.thumbimage);
        }
        viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPostClicked) {
                    isPostClicked = true;
                    if (reactions.getMediaDetail().getMediaType() == MEDIA_TYPE_GIF) {
                        reactionPlayerListener.reactionPlayer(OPENED_FROM_PROFILE,null,reactions, true);
                    } else if(reactions.getMediaDetail().getMediaType() == MEDIA_TYPE_VIDEO){
                        reactionPlayerListener.reactionPlayer(OPENED_FROM_PROFILE,null,reactions, true);
                    } else if(reactions.getMediaDetail().getMediaType() == MEDIA_TYPE_GIFHY){
                        reactionPlayerListener.reactionPlayer(OPENED_FROM_PROFILE,null,reactions, true);
                    }
                    isPostClicked = false;
                }
            }
        });
        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, viewHolder.menu);
                popup.inflate(R.menu.menu_profile_reaction);
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
                                        list.remove(i);
                                        notifyItemRemoved(i);
                                        notifyItemRangeChanged(i,list.size());
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
                }catch (Exception e) {
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
        RelativeLayout rootLayout;

        public ViewHolder(View view) {
            super(view);

            videoTitle = view.findViewById(R.id.video_details);
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
            rootLayout = view.findViewById(R.id.rootLayout);
        }
    }

    public interface ReactionPlayerListener {
        void reactionPlayer(int selfReaction, PostReaction postReaction, MyReactions reaction, boolean isGif);
    }
}

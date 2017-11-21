package com.cncoding.teazer.adapter;

import android.content.Context;
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
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.profile.delete.DeleteMyVideos;
import com.cncoding.teazer.ui.fragment.activity.ProfileCreationVideos;
import com.cncoding.teazer.utilities.Pojos;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by farazhabib on 09/11/17.
 */

public class ProfileMyCreationAdapter extends RecyclerView.Adapter<ProfileMyCreationAdapter.ViewHolder> {

    private ArrayList<Pojos.Post.PostDetails> addressdetail_list;
    private Context context;
    myCreationListener listener;

    public ProfileMyCreationAdapter(Context context, ArrayList<Pojos.Post.PostDetails> addressdetail_list) {
        this.context = context;
        this.addressdetail_list = addressdetail_list;
        listener=(myCreationListener)context;

    }

    @Override
    public ProfileMyCreationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_mycreations, viewGroup, false);
        return new ProfileMyCreationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProfileMyCreationAdapter.ViewHolder viewHolder, final int i) {
        final Pojos.Post.PostDetails cont = addressdetail_list.get(i);
        final String videotitle = cont.getTitle();
        final String videourl = cont.getMedias().get(0).getMediaUrl();
        final int videopostId = cont.getPostId();
        final String thumb_url = cont.getMedias().get(0).getThumbUrl();
        final String duration = cont.getMedias().get(0).getDuration();
        final String views = String.valueOf(cont.getMedias().get(0).getViews());
        final String likes= String.valueOf(cont.getLikes());
        final String reaction= String.valueOf(cont.getTotalReactions());
//        final String location=cont.getCheckIn().getLocation();

        viewHolder.videoTitle.setText(videotitle);
        viewHolder.txtlikes.setText(likes);
        viewHolder.duration.setText(duration);
        viewHolder.txtview.setText(views);
        viewHolder.reactions.setText(reaction);
       // viewHolder.location.setText(location);


        Glide.with(context).load(thumb_url)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.material_flat))
                .into(viewHolder.thumbimage);


        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ProfileCreationVideos.class);
//                intent.putExtra("VideoURL", videourl);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
                listener.myCreationVideos(2,cont);
               // Toast.makeText(context,"Hello",Toast.LENGTH_LONG).show();
            }
        });


        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, viewHolder.menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.menu_profile);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                deleteVideos(videopostId);
                                // notifyItemRemoved(i);
                                viewHolder.cardView.setVisibility(View.GONE);
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

    @Override
    public int getItemCount() {
        return addressdetail_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTitle;
        private TextView duration;
        private TextView txtlikes;
        private TextView txtview;
        private TextView reactions;
        private TextView location;
        VideoView videoviewContainer;
        ImageView thumbimage;
        CardView cardView;
        View line;
        ImageView playvideo;
        CircularAppCompatImageView menu;
        public ViewHolder(View view) {
            super(view);
            videoTitle = view.findViewById(R.id.videodetails);
            duration = view.findViewById(R.id.duration);
            txtlikes = view.findViewById(R.id.txtlikes);
            txtview = view.findViewById(R.id.txtview);
            reactions = view.findViewById(R.id.reactions);
            thumbimage = view.findViewById(R.id.demoimage);
            playvideo = view.findViewById(R.id.playvideo);
            cardView = view.findViewById(R.id.cardview);
            location = view.findViewById(R.id.location);
            menu = view.findViewById(R.id.menu);

        }
    }

    private void deleteVideos(int deleteId) {
        ApiCallingService.Posts.deletePosts(deleteId, context).enqueue(new Callback<DeleteMyVideos>() {
            @Override
            public void onResponse(Call<DeleteMyVideos> call, Response<DeleteMyVideos> response) {
                try {
                    if (response.code() == 200) {
                        boolean status = response.body().getStatus();
                        Toast.makeText(context, "Video has been deleted", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(context, "Video has not been deleted", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<DeleteMyVideos> call, Throwable t) {

                Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public  interface myCreationListener
    {

        public void  myCreationVideos ( int i, Pojos.Post.PostDetails postDetails);
    }

}

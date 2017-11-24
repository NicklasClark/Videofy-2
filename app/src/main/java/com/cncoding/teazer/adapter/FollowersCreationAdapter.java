package com.cncoding.teazer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.cncoding.teazer.model.profile.followerprofile.postvideos.Post;
import com.cncoding.teazer.model.profile.followerprofile.postvideos.Post;
import com.cncoding.teazer.ui.fragment.activity.ProfileCreationVideos;
import com.cncoding.teazer.utilities.Pojos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by farazhabib on 11/11/17.
 */

public class FollowersCreationAdapter extends RecyclerView.Adapter<FollowersCreationAdapter.ViewHolder> {

    private List<Post> _list;
    Context context;
    private ArrayList<Pojos.Post.PostReaction> reactiolist;

    public FollowersCreationAdapter(Context context, List<Post> _list) {
        this.context = context;
        this._list = _list;

    }

    @Override
    public FollowersCreationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_follower_profile_creation, viewGroup, false);
        return new FollowersCreationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FollowersCreationAdapter.ViewHolder viewHolder, final int i) {


        try {
            final Post cont = _list.get(i);
            final String videotitle = cont.getTitle();
            final int postId = cont.getPostId();
            final String videourl = cont.getMedias().get(0).getMediaUrl();
            final int videopostId = cont.getPostId();
            final String thumb_url = cont.getMedias().get(0).getThumbUrl();
            final String duration = cont.getMedias().get(0).getDuration();
            final Integer  txtview = cont.getMedias().get(0).getViews();
            final boolean hasCheckIn = cont.getHasCheckin();
            final Integer likes = cont.getLikes();
            if(hasCheckIn)
            {
                final String location=cont.getCheckIn().getLocation();
                viewHolder.location.setText(location);
            }
            else
            {
                viewHolder.location.setText("");
            }
            Glide.with(context).load(thumb_url)
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.material_flat))
                    .into(viewHolder.thumbimage);
            viewHolder.videoTitle.setText(videotitle);
            viewHolder.duration.setText(duration);
            viewHolder.totalLikes.setText(String.valueOf(likes));
            viewHolder.txtview.setText(String.valueOf(txtview));
            getPostReaction(viewHolder, postId);
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileCreationVideos.class);
                    intent.putExtra("VideoURL", videourl);
                    intent.putExtra("Likes", String.valueOf(likes));
                    intent.putExtra("Views", String.valueOf(txtview));
                    intent.putExtra("Title", videotitle);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"Something went wrong please try again",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTitle, address;
        VideoView videoviewContainer;
        ImageView thumbimage;
        CardView cardView;
        TextView toatalReaction;
        TextView hascheckin;
        TextView totalLikes;
        TextView location;
        TextView duration;
        TextView txtview;
        CircularAppCompatImageView userReactionImage;
        View line;
        ImageView playvideo;
        CircularAppCompatImageView menu;
        public ViewHolder(View view) {
            super(view);
            videoTitle = view.findViewById(R.id.videodetails);
            address = view.findViewById(R.id.videodetails);
            videoviewContainer = view.findViewById(R.id.flContainer);
            thumbimage = view.findViewById(R.id.demoimage);
            duration = view.findViewById(R.id.duration);
            playvideo = view.findViewById(R.id.playvideo);
            cardView = view.findViewById(R.id.cardview);
            location = view.findViewById(R.id.location);
            totalLikes = view.findViewById(R.id.txtlikes);
            txtview = view.findViewById(R.id.txtview);
            userReactionImage = view.findViewById(R.id.userReactionImage);
            menu = view.findViewById(R.id.menu);
        }
    }

    public void deleteVideos(int deleteid) {
        ApiCallingService.Posts.deletePosts(deleteid, context).enqueue(new Callback<DeleteMyVideos>() {
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


    public void getPostReaction(final FollowersCreationAdapter.ViewHolder viewHolder, int postId) {
        int page = 1;
        ApiCallingService.Posts.getReactionsOfPost(postId, page, context).enqueue(new Callback<Pojos.Post.PostReactionsList>() {
            @Override
            public void onResponse(Call<Pojos.Post.PostReactionsList> call, Response<Pojos.Post.PostReactionsList> response) {

                Log.d("Tesponsecheck",response.message());
                Log.d("Tesponsecheck",String.valueOf(response.code()));
                if (response.code() == 200) {

                    try {
                        reactiolist = response.body().getReactions();
                     for(int i=0;i<reactiolist.size();i++) {
                         Integer totalviews = reactiolist.get(i).getViews();
                         viewHolder.txtview.setText(String.valueOf(totalviews));
                     }
                        if (reactiolist.size() > 1) {
                            // int counter=reactions-3;
                            //  viewHolder.reactions.setText("+" + String.valueOf(counter) + " R");
                            for (int i = 0; i < 1; i++)
                            {
                                Pojos.MiniProfile miniProfile = reactiolist.get(i).getReactOwner();
                                if (miniProfile.hasProfileMedia()) {
                                    String profileurl = miniProfile.getProfileMedia().getThumbUrl();
                                    switch (i) {
                                        case 0:
                                            Picasso.with(context)
                                                    .load(profileurl)
                                                    .into(viewHolder.userReactionImage);
                                           // viewHolder.imagelayout1.setVisibility(View.VISIBLE);

                                            break;
                                        case 1:
//                                            Picasso.with(context)
//                                                    .load(profileurl)
//                                                    .into(viewHolder.image2);
                                            //viewHolder.imagelayout2.setVisibility(View.VISIBLE);


                                            break;

                                        case 2:
//                                            Picasso.with(context)
//                                                    .load(profileurl)
//                                                    .into(viewHolder.image3);
                                          //  viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            break;

                                        default:


                                    }

                                } else {

                                    switch (0) {
                                        case 0:

                                        //    viewHolder.imagelayout1.setVisibility(View.VISIBLE);
//                                            Picasso.with(context)
//                                                    .load(pic)
//                                                    .into(viewHolder.image1);
                                            break;
                                        case 1:

                                        //    viewHolder.imagelayout2.setVisibility(View.VISIBLE);
//                                            Picasso.with(context)
//                                                    .load(pic)
//                                                    .into(viewHolder.image3);
                                            break;
                                        case 2:

                                         //   viewHolder.imagelayout3.setVisibility(View.VISIBLE);
//                                            Picasso.with(context)
//                                                    .load(pic)
//                                                    .into(viewHolder.image3);
                                            break;
                                        default:


                                    }
                                }


                            }
                        }
                    }

                    catch(Exception e)
                    {

                    }


                }
            }
            @Override
            public void onFailure (Call < Pojos.Post.PostReactionsList > call, Throwable t){
            }
        });
    }


}
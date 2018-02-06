package com.cncoding.teazer.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.base.MiniProfile;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostReaction;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.ui.fragment.fragment.ReportPostDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.CommonUtilities.decodeUnicodeString;

/**
 * 
 * Created by farazhabib on 11/11/17.
 */

public class FollowersCreationAdapter extends RecyclerView.Adapter<FollowersCreationAdapter.ViewHolder> {

    private List<PostDetails> _list;
    Context context;
    private ArrayList<PostReaction> reactionList;
    FollowerCreationListener listener;
    Activity othersProfileFragment;

    public FollowersCreationAdapter(Context context, List<PostDetails> _list, Activity othersProfileFragment) {
        this.context = context;
        this._list = _list;
        listener = (FollowerCreationListener) context;
        this.othersProfileFragment = othersProfileFragment;

    }

    @Override
    public FollowersCreationAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_follower_profile_creation, viewGroup, false);
        return new FollowersCreationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FollowersCreationAdapter.ViewHolder viewHolder, final int i) {


        try {

            final PostDetails postDetails = _list.get(i);
            final String videoTitle = postDetails.getTitle();
            final int postId = postDetails.getPostId();
            final boolean is_hidden=postDetails.getHided();
            if(is_hidden)
            {

                viewHolder.hidden_layout.setVisibility(View.VISIBLE);
                viewHolder.unhide_video.setVisibility(View.VISIBLE);
                viewHolder.playvideo.setVisibility(View.GONE);
                viewHolder.cardView.setEnabled(false);
                viewHolder.menu.setEnabled(false);
            } else {
                viewHolder.hidden_layout.setVisibility(View.GONE);
                viewHolder.unhide_video.setVisibility(View.GONE);
                viewHolder.playvideo.setVisibility(View.VISIBLE);
                viewHolder.menu.setEnabled(true);
                viewHolder.cardView.setEnabled(true);
            }

            final String videoUrl = postDetails.getMedias().get(0).getMediaUrl();
            String postUser = postDetails.getPostOwner().getUserName();
            boolean hasProfileMedia = postDetails.getPostOwner().hasProfileMedia();
            if(hasProfileMedia) {
                String userDp = postDetails.getPostOwner().getProfileMedia().getMediaUrl();
                if (userDp!=null)
                {
                    Glide.with(context).load(userDp)
                            .into(viewHolder.userReactionImage);
                }
            }
          else
            {
                Glide.with(context).load(R.drawable.ic_user_male_dp_small)
                        .into(viewHolder.userReactionImage);
            }

            final String thumb_url = postDetails.getMedias().get(0).getThumbUrl();
            Log.d("Video UrL",videoUrl);
            final String duration = postDetails.getMedias().get(0).getDuration();
            final Integer  txtview = postDetails.getMedias().get(0).getViews();
            final boolean hasCheckIn = postDetails.hasCheckin();
            final Integer likes = postDetails.getLikes();
            if(hasCheckIn)
            {
                final String location=postDetails.getCheckIn().getLocation();
                viewHolder.location.setText(location);

            }
            else
            {
                viewHolder.location.setText("");
            }

            Glide.with(context).load(thumb_url)
                    .into(viewHolder.thumbimage);
            viewHolder.location.setText(postUser);
            viewHolder.videoTitle.setText(decodeUnicodeString(videoTitle));
            viewHolder.duration.setText(duration);
            viewHolder.totalLikes.setText(String.valueOf(likes));
            viewHolder.txtview.setText(String.valueOf(txtview));

            //getPostReaction(viewHolder, postId);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.myCreationVideos(2, postDetails);
                    viewHolder.txtview.setText(String.valueOf(txtview+1));


                }
            });

            viewHolder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, viewHolder.menu);
                    popup.inflate(R.menu.menu_other_profile_post);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_delete:
                                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                                    ReportPostDialogFragment reportPostDialogFragment = ReportPostDialogFragment.
                                            newInstance(postDetails.getPostId(), postDetails.canReact(), postDetails.getPostOwner().getUserName());
                                    if (fm != null) {
                                        reportPostDialogFragment.show(fm, "fragment_report_post");
                                    }
                                    break;
                                case R.id.action_hide:


                                    new AlertDialog.Builder(context)
                                            .setTitle(R.string.hiding_post)
                                            .setMessage(R.string.hide_post_confirm)
                                            .setPositiveButton(context.getString(R.string.yes_hide), new DialogInterface.OnClickListener()
                                            {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ApiCallingService.Posts.hideOrShowPost(postDetails.getPostId(), 1, context)
                                                            .enqueue(new Callback<ResultObject>() {
                                                                @Override
                                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                                    if (response.body().getStatus()) {
                                                                        Toast.makeText(context,
                                                                                R.string.video_hide_successful,
                                                                                Toast.LENGTH_SHORT).show();
                                                                        viewHolder.hidden_layout.setVisibility(View.VISIBLE);
                                                                        viewHolder.unhide_video.setVisibility(View.VISIBLE);
                                                                        viewHolder.playvideo.setVisibility(View.GONE);
                                                                        viewHolder.cardView.setEnabled(false);
                                                                        viewHolder.menu.setEnabled(false);


                                                                    } else {
                                                                        Toast.makeText(context,
                                                                                R.string.something_went_wrong,
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                @Override
                                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                                    t.printStackTrace();
                                                                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .show();


                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });



            viewHolder.unhide_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new android.support.v7.app.AlertDialog.Builder(context)
                            .setTitle(R.string.unhiding_post)
                            .setMessage(R.string.unhide_post_confirm)
                            .setPositiveButton(context.getString(R.string.yes_unhide), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {
                                    ApiCallingService.Posts.hideOrShowPost(postId, 2, context)
                                            .enqueue(new Callback<ResultObject>() {
                                                @Override
                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                    if (response.body().getStatus()) {
                                                        Toast.makeText(context,
                                                                R.string.video_shown_successful,
                                                                Toast.LENGTH_SHORT).show();

                                                        viewHolder.hidden_layout.setVisibility(View.GONE);
                                                        viewHolder.unhide_video.setVisibility(View.GONE);
                                                        viewHolder.playvideo.setVisibility(View.VISIBLE);
                                                        viewHolder.menu.setEnabled(true);
                                                        viewHolder.cardView.setEnabled(true);

                                                    } else {
                                                        Toast.makeText(context,
                                                                R.string.something_went_wrong,
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<ResultObject> call, Throwable t) {
                                                    t.printStackTrace();
                                                    Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();



                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error Message",e.getMessage());
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
        ImageView hidden_layout;
        CircularAppCompatImageView userReactionImage;
        View line;
        ImageView playvideo;
        Button unhide_video;

        CircularAppCompatImageView menu;
        public ViewHolder(View view) {
            super(view);
            videoTitle = view.findViewById(R.id.video_details);
            address = view.findViewById(R.id.video_details);
            videoviewContainer = view.findViewById(R.id.flContainer);
            thumbimage = view.findViewById(R.id.demoimage);
            duration = view.findViewById(R.id.duration);
            playvideo = view.findViewById(R.id.playvideo);
            cardView = view.findViewById(R.id.cardview);
            location = view.findViewById(R.id.location);
            totalLikes = view.findViewById(R.id.txtlikes);
            txtview = view.findViewById(R.id.txtview);
            userReactionImage = view.findViewById(R.id.userReactionImage);
            hidden_layout = view.findViewById(R.id.hidden_layput);
            unhide_video = view.findViewById(R.id.unhide_video);
            menu = view.findViewById(R.id.menu);
        }
    }




    public void getPostReaction(final FollowersCreationAdapter.ViewHolder viewHolder, int postId) {
        int page = 1;
        ApiCallingService.Posts.getReactionsOfPost(postId, page, context).enqueue(new Callback<PostReactionsList>() {
            @Override
            public void onResponse(Call<PostReactionsList> call, Response<PostReactionsList> response) {

                if (response.code() == 200) {

                    try {
                        reactionList = response.body().getReactions();
                        for(int i = 0; i< reactionList.size(); i++) {
                       //  Integer totalviews = reactionList.get(i).getViews();
                        // viewHolder.txtview.setText(String.valueOf(totalviews));
                     }
                        if (reactionList.size() > 1) {
                            // int counter=reactions-3;
                            //  viewHolder.reactions.setText("+" + String.valueOf(counter) + " R");
                            for (int i = 0; i < 1; i++) {
                                MiniProfile miniProfile = reactionList.get(i).getReactOwner();
                                if (miniProfile.hasProfileMedia()) {
                                    String profileurl = miniProfile.getProfileMedia().getThumbUrl();
                                    switch (i) {
                                        case 0:
//                                            Picasso.with(context)
//                                                    .load(profileurl)
//                                                    .into(viewHolder.userReactionImage);
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
            public void onFailure (Call < PostReactionsList > call, Throwable t){
            }
        });
    }

    public interface FollowerCreationListener

    {
        public void myCreationVideos(int i, PostDetails postDetails);


    }
}
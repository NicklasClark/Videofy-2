package com.cncoding.teazer.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import static com.cncoding.teazer.home.post.PostsListFragment.postDetails;

/**
 * 
 * Created by farazhabib on 11/11/17.
 */

public class FollowersCreationAdapter extends RecyclerView.Adapter<FollowersCreationAdapter.ViewHolder> {

    private List<PostDetails> _list;
    Context context;
    private ArrayList<PostReaction> reactiolist;
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

            final PostDetails cont = _list.get(i);
            final String videotitle = cont.getTitle();
            final int postId = cont.getPostId();
            final String videourl = cont.getMedias().get(0).getMediaUrl();
            String postUser=cont.getPostOwner().getUserName();
            boolean hasProfilemedia=cont.getPostOwner().hasProfileMedia();
            if(hasProfilemedia) {
                String userDp = cont.getPostOwner().getProfileMedia().getMediaUrl();
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



            final int videopostId = cont.getPostId();
            final String thumb_url = cont.getMedias().get(0).getThumbUrl();
            Log.d("Video UrL",videourl);
            final String duration = cont.getMedias().get(0).getDuration();
            final Integer  txtview = cont.getMedias().get(0).getViews();
            final boolean hasCheckIn = cont.hasCheckin();
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
                    .into(viewHolder.thumbimage);

            viewHolder.location.setText(postUser);
            viewHolder.videoTitle.setText(videotitle);
            viewHolder.duration.setText(duration);
            viewHolder.totalLikes.setText(String.valueOf(likes));
            viewHolder.txtview.setText(String.valueOf(txtview));

            getPostReaction(viewHolder, postId);
            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.myCreationVideos(2, cont);

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
                                            newInstance(cont.getPostId(), cont.canReact(), postDetails.getPostOwner().getUserName());
                                    if (fm != null) {
                                        reportPostDialogFragment.show(fm, "fragment_report_post");
                                    }
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
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




    public void getPostReaction(final FollowersCreationAdapter.ViewHolder viewHolder, int postId) {
        int page = 1;
        ApiCallingService.Posts.getReactionsOfPost(postId, page, context).enqueue(new Callback<PostReactionsList>() {
            @Override
            public void onResponse(Call<PostReactionsList> call, Response<PostReactionsList> response) {

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
                            for (int i = 0; i < 1; i++) {
                                MiniProfile miniProfile = reactiolist.get(i).getReactOwner();
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
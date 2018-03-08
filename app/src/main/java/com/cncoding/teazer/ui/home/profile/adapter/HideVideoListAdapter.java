package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostReaction;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.ui.home.profile.activity.EditPost;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.CommonUtilities.decodeUnicodeString;

/**
 * 
 * Created by farazhabib on 15/01/18.
 */

public class HideVideoListAdapter  extends RecyclerView.Adapter<HideVideoListAdapter.ViewHolder> {
    private ArrayList<PostDetails> list;
    private ArrayList<PostReaction> reactiolist;
    private Context context;
    Fragment fragment;
    ProfileMyCreationAdapter.OnChildFragmentUpdateVideos onChildFragmentUpdateVideosllistrener;

    public HideVideoListAdapter(Context context, ArrayList<PostDetails> list, Fragment fragment) {
        this.context = context;
        this.list = list;
        this.fragment = fragment;
    }

    @Override
    public HideVideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_hide_videos, viewGroup, false);
        return new HideVideoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HideVideoListAdapter.ViewHolder viewHolder, final int i) {

        final PostDetails cont;
        String videoTitle;
        final int videoPostId;
        final String thumb_url;
        final String duration;
        final String views;
        final String likes;
        final int reactions;
        final int postId;
        final String username;
        final boolean hasProfileMedia;
        final String postownerDp;
        try {

            cont = list.get(i);
            videoTitle = cont.getTitle();
            postId = cont.getPostId();
            final String videourl = cont.getMedias().get(0).getMediaUrl();
            videoPostId = cont.getPostId();
            thumb_url = cont.getMedias().get(0).getThumbUrl();
            duration = cont.getMedias().get(0).getDuration();
            views = String.valueOf(cont.getMedias().get(0).getViews());
            likes = String.valueOf(cont.getLikes());
            reactions = cont.getTotalReactions();
            username=cont.getPostOwner().getUserName();
            hasProfileMedia=cont.getPostOwner().hasProfileMedia();
            if(hasProfileMedia) {
                postownerDp = cont.getPostOwner().getProfileMedia().getThumbUrl();
                Glide.with(context).load(postownerDp)
                        .into(viewHolder.media_controller_dp);

            }
            else {
                Glide.with(context).load(R.drawable.ic_user_male_dp_small)
                        .into(viewHolder.media_controller_dp);
            }


            boolean hascheckin = cont.hasCheckin();
            if (hascheckin) {
                String location2 = cont.getCheckIn().getLocation();

                if (location2.equals("") || location2 == null) {
                    viewHolder.location.setText("");
                    viewHolder.locationimage.setVisibility(View.GONE);
                } else {
                    viewHolder.location.setText(location2);
                    viewHolder.locationimage.setVisibility(View.VISIBLE);
                }
            } else {
                viewHolder.location.setText("");
                viewHolder.locationimage.setVisibility(View.GONE);
            }

            if (reactions == 0) {
                viewHolder.reactions.setVisibility(View.GONE);
                viewHolder.imagelayout1.setVisibility(View.INVISIBLE);
                viewHolder.imagelayout2.setVisibility(View.INVISIBLE);
                viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
            } else if (reactions > 3)
                getPostReactionFour(viewHolder, postId, reactions);
            else {
                viewHolder.reactions.setText(String.valueOf(reactions) + " R");
                viewHolder.reactions.setVisibility(View.GONE);
                getPostReactionThree(viewHolder, postId, reactions);
            }

            viewHolder.videoTitle.setText(decodeUnicodeString(videoTitle));
            viewHolder.txtlikes.setText(likes);
            viewHolder.duration.setText(duration);
            viewHolder.txtview.setText(views);
            viewHolder.username.setText(username);

            Glide.with(context).load(thumb_url)
                    .into(viewHolder.thumbimage);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //listener.myCreationVideos(2, cont);

                }
            });

            viewHolder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(context, viewHolder.menu);
                    popup.inflate(R.menu.menu_unhide_videos);
//
//                    MenuItem item = menu.getItem(0); //here we are getting our menu item.
//
//                    SpannableString s = new SpannableString(.getTitle()); //get text from our menu item.
//                    s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0); //here I am just setting a custom color to the menu item. leave this out if you want it left at black.
//                    s.setSpan(new RelativeSizeSpan(.7f),0,s.length(),0); //here is where we are actually setting the size with a float (proportion).
//                    item.setTitle(s); //Then just set the menu item to our SpannableString.

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_unhide:

                                  //here we are getting our menu item.



                                    new android.support.v7.app.AlertDialog.Builder(context)
                                            .setTitle(R.string.unhiding_post)
                                            .setMessage(R.string.unhide_post_confirm)
                                            .setPositiveButton(context.getString(R.string.yes_unhide), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int j) {
                                                    ApiCallingService.Posts.hideOrShowPost(videoPostId, 2, context)
                                                            .enqueue(new Callback<ResultObject>() {
                                                                @Override
                                                                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                                                                    if (response.body().getStatus()) {
                                                                        Toast.makeText(context,
                                                                                R.string.video_shown_successful,
                                                                                Toast.LENGTH_SHORT).show();

                                                                        list.remove(i);
                                                                        notifyItemRemoved(i);
                                                                        notifyItemRangeChanged(i, list.size());
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
                                case R.id.edit_post:
                                    Intent intent = new Intent(context, EditPost.class);
                                    intent.putExtra("PostDetail", cont);
                                    context.startActivity(intent);
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
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ProximaNovaRegularCheckedTextView videoTitle;
        private ProximaNovaRegularCheckedTextView duration;
        private ProximaNovaRegularCheckedTextView txtlikes;
        private ProximaNovaRegularCheckedTextView txtview;
        private ProximaNovaRegularCheckedTextView reactions;
        private ProximaNovaRegularCheckedTextView location;
        private ProximaNovaRegularCheckedTextView username;
        VideoView videoviewContainer;
        ImageView thumbimage;
        RelativeLayout imagelayout1;
        RelativeLayout imagelayout2;
        RelativeLayout imagelayout3;
        CircularAppCompatImageView image1, image2, image3, locationimage,media_controller_dp;
        CardView cardView;
        View line;
        ImageView playvideo;
        CircularAppCompatImageView menu;

        public ViewHolder(View view) {
            super(view);

            videoTitle = view.findViewById(R.id.video_details);
            duration = view.findViewById(R.id.duration);
            txtlikes = view.findViewById(R.id.txtlikes);
            txtview = view.findViewById(R.id.txtview);
            reactions = view.findViewById(R.id.reactions);
            thumbimage = view.findViewById(R.id.demoimage);
            playvideo = view.findViewById(R.id.playvideo);
            cardView = view.findViewById(R.id.cardview);
            location = view.findViewById(R.id.location);
            image1 = view.findViewById(R.id.image1);
            image2 = view.findViewById(R.id.image2);
            image3 = view.findViewById(R.id.image3);
            imagelayout1 = view.findViewById(R.id.image1_layout);
            imagelayout2 = view.findViewById(R.id.image2_layout);
            imagelayout3 = view.findViewById(R.id.image3_layout);
            locationimage = view.findViewById(R.id.locationimage);
            username = view.findViewById(R.id.name);
            media_controller_dp = view.findViewById(R.id.media_controller_dp);
            menu = view.findViewById(R.id.menu);
        }
    }

    private void deleteVideos(int deleteId) {

        ApiCallingService.Posts.deletePosts(deleteId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    if (response.code() == 200) {

                        boolean status = response.body().getStatus();

                        Toast.makeText(context, "Video has been deleted", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, "Some issue has been occurred please try again", Toast.LENGTH_SHORT).show();
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

    public void getPostReactionFour(final HideVideoListAdapter.ViewHolder viewHolder, int postId, final int reactions) {
        int page = 1;
        ApiCallingService.Posts.getReactionsOfPost(postId, page, context).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {

                if (response.code() == 200) {

                    try {
                        reactiolist = response.body().getReactions();

                        if (reactiolist.size() > 3) {
                            int counter = reactions - 3;
                          //  viewHolder.reactions.setText("+" + String.valueOf(counter) + " R");
                            for (int i = 0; i < 3; i++) {

                                MiniProfile miniProfile = reactiolist.get(i).getReactOwner();
                                if (miniProfile.hasProfileMedia()) {
                                    String profileurl = miniProfile.getProfileMedia().getMediaUrl();
                                    switch (i) {
                                        case 0:
                                            Glide.with(context)
                                                    .load(profileurl)
                                                    .into(viewHolder.image1);
                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
                                            break;
                                        case 1:
                                            Glide.with(context)
                                                    .load(profileurl)
                                                    .into(viewHolder.image2);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            break;

                                        case 2:
                                            Glide.with(context)
                                                    .load(profileurl)
                                                    .into(viewHolder.image3);
                                            viewHolder.imagelayout3.setVisibility(View.VISIBLE);
                                            break;

                                        default:
                                    }

                                } else {

                                    switch (i) {
                                        case 0:

                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
                                            Glide.with(context)
                                                    .load(R.drawable.ic_user_male_dp_small)
                                                    .into(viewHolder.image1);
                                            break;
                                        case 1:

                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
                                            Glide.with(context)
                                                    .load(R.drawable.ic_user_male_dp_small)
                                                    .into(viewHolder.image3);
                                            break;
                                        case 2:

                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.VISIBLE);
                                            Glide.with(context)
                                                    .load(R.drawable.ic_user_male_dp_small)
                                                    .into(viewHolder.image3);
                                            break;
                                        default:


                                    }
                                }


                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t) {
            }
        });
    }

    public void getPostReactionThree(final HideVideoListAdapter.ViewHolder viewHolder, int postId, final int reactions) {
        int page = 1;
        ApiCallingService.Posts.getReactionsOfPost(postId, page, context).enqueue(new Callback<ReactionsList>() {
            @Override
            public void onResponse(Call<ReactionsList> call, Response<ReactionsList> response) {

                if (response.code() == 200) {

                    try {
                        reactiolist = response.body().getReactions();

                        if (reactiolist.size() <= 3) {

                            for (int i = 0; i < reactiolist.size(); i++) {

                                MiniProfile miniProfile = reactiolist.get(i).getReactOwner();
                                if (miniProfile.hasProfileMedia()) {

                                    String profileurl = miniProfile.getProfileMedia().getMediaUrl();
                                    switch (i) {
                                        case 0:
                                            Glide.with(context)
                                                    .load(profileurl)
                                                    .into(viewHolder.image1);
                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.INVISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
                                            break;
                                        case 1:
                                            Glide.with(context)
                                                    .load(profileurl)
                                                    .into(viewHolder.image2);
                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
                                            break;

                                        case 2:

                                            Glide.with(context)
                                                    .load(profileurl)
                                                    .into(viewHolder.image3);
                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.VISIBLE);
                                            break;

                                        default:
                                    }

                                } else {

                                    switch (i) {
                                        case 0:

                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.INVISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
                                            Glide.with(context)
                                                    .load(R.drawable.ic_user_male_dp_small)
                                                    .into(viewHolder.image1);
                                            break;
                                        case 1:

                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.INVISIBLE);
                                            Glide.with(context)
                                                    .load(R.drawable.ic_user_male_dp_small)
                                                    .into(viewHolder.image3);
                                            break;
                                        case 2:

                                            viewHolder.imagelayout1.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout2.setVisibility(View.VISIBLE);
                                            viewHolder.imagelayout3.setVisibility(View.VISIBLE);
                                            Glide.with(context)
                                                    .load(R.drawable.ic_user_male_dp_small)
                                                    .into(viewHolder.image3);
                                            break;
                                        default:


                                    }
                                }


                            }
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReactionsList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}
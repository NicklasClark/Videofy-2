package com.cncoding.teazer.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.model.profile.reaction.Reaction;

import java.util.List;

/**
 * Created by farazhabib on 10/11/17.
 */

public class ProfileMyReactionAdapter extends RecyclerView.Adapter<ProfileMyReactionAdapter.ViewHolder> {

    private List<Reaction> addressdetail_list;
    Context context;

    public ProfileMyReactionAdapter(Context context, List<Reaction> addressdetail_list) {
        this.context = context;
        //this.addressdetail_list = addressdetail_list;

        }
    @Override
    public ProfileMyReactionAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_myreactions, viewGroup, false);
        return new ProfileMyReactionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProfileMyReactionAdapter.ViewHolder viewHolder, int i) {
//        Toast.makeText(context,"onbind",Toast.LENGTH_LONG).show();
//        if(addressdetail_list.size()>0) {
//            final Reaction cont = addressdetail_list.get(i);
//            if (cont != null) {
//                Toast.makeText(context,"if ",Toast.LENGTH_LONG).show();
//
//                String videotitle = cont.getReactTitle();
//                viewHolder.videoTitle.setText(videotitle);
//            }
//        }
////
//        else
//        {
//            Toast.makeText(context,"no reaction found ",Toast.LENGTH_LONG).show();
//        }
// final String videourl = cont.getMedias().get(0).getMediaUrl();
//        String thumb_url = cont.getMedias().get(0).getThumbUrl();
//        Picasso.with(context).load(thumb_url)
//                .placeholder(ContextCompat.getDrawable(context, R.drawable.material_flat))
//                .into(viewHolder.thumbimage);
//

//        viewHolder.playvideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ProfileCreationVideos.class);
//                intent.putExtra("VideoURL", videourl);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView videoTitle, address;
        VideoView videoviewContainer;
        ImageView thumbimage;
        CardView cardView;
        View line;
        CircularAppCompatImageView menu;


        ImageView playvideo;

        public ViewHolder(View view) {
            super(view);
            videoTitle = view.findViewById(R.id.videodetails);
            address = view.findViewById(R.id.videodetails);
            videoviewContainer = view.findViewById(R.id.flContainer);
            thumbimage = view.findViewById(R.id.demoimage);
            playvideo = view.findViewById(R.id.playvideo);
            menu = view.findViewById(R.id.menu);

        }
    }
}

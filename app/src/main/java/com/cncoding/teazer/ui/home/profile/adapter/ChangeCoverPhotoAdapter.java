package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.ui.home.profile.activity.CoverPicChangeActivity;

import java.util.ArrayList;

/**
 * Created by farazhabib on 14/02/18.
 */

public class ChangeCoverPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Object> categories;
    private final int DefaultImages = 0;
    private final int UserSelectedImages = 1;
    CoverPicChangeActivity fragmentChangeCoverPhoto;


    public ChangeCoverPhotoAdapter(ArrayList<Object> categories, Context context, CoverPicChangeActivity fragmentChangeCoverPhoto) {
        this.categories = categories;
        this.context = context;
        this.fragmentChangeCoverPhoto=fragmentChangeCoverPhoto;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case DefaultImages:
                View v1 = inflater.inflate(R.layout.cardview_change_cover_photo, parent, false);
                viewHolder = new ViewHolder1(v1);
                break;

            case UserSelectedImages:

                View v2 = inflater.inflate(R.layout.cardview_change_cover_addphoto, parent, false);
                viewHolder = new ViewHolder2(v2);
                break;

            default:
                View v3 = inflater.inflate(R.layout.cardview_change_cover_photo, parent, false);
                viewHolder = new ViewHolder1(v3);
                break;
        }

        ///    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_change_cover_photo, parent, false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {

            switch (holder.getItemViewType()) {
                case DefaultImages:
                    ViewHolder1 vh1 = (ViewHolder1) holder;
                    final int category = (int) this.categories.get(position);
                    vh1.profile_id2.setImageResource(category);
                    break;
                case UserSelectedImages:
                    ViewHolder2 vh2 = (ViewHolder2) holder;
                    vh2.cardview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            ((CoverPicChangeActivity)context).changeCoverPic();

                        }
                    });
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_LONG).show();

        }
    }
    @Override
    public int getItemCount() {
        return categories.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (categories.get(position) instanceof Integer) {
            return DefaultImages;
        }
        else {
            return UserSelectedImages;
        }
    }
    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private CardView cardview;

        public ViewHolder2(View view) {
            super(view);
            cardview = view.findViewById(R.id.cardview);

        }
    }
    public class ViewHolder1 extends RecyclerView.ViewHolder {
        private RelativeLayout rootLayout;
        ImageView profile_id2;
        private ProximaNovaRegularCheckedTextView nameView;
        public ViewHolder1(View view) {
            super(view);
            rootLayout = view.findViewById(R.id.categories_item_layout);
            profile_id2 = view.findViewById(R.id.profile_id2);
            nameView = view.findViewById(R.id.chip);
        }
    }
}
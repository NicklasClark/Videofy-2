package com.cncoding.teazer.home.post.detailspage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.home.BaseRecyclerView;
import com.cncoding.teazer.home.profile.ProfileFragment;
import com.cncoding.teazer.model.base.TaggedUser;
import com.cncoding.teazer.ui.fragment.activity.OthersProfileFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cncoding.teazer.utilities.ViewUtils.getGenderSpecificDpSmall;

/**
 *
 * Created by Prem $ on 11/17/2017.
 */

public class TagListAdapter extends BaseRecyclerView.Adapter {

    private List<TaggedUser> taggedUsers;
    private PostDetailsFragment fragment;

    TagListAdapter(PostDetailsFragment fragment) {
        this.fragment = fragment;
        taggedUsers = new ArrayList<>();
    }

    @Override
    public TaggedViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tagged_user, viewGroup, false);
        return new TaggedViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return taggedUsers.size();
    }

    @Override
    public void release() {}

    @Override
    public void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void addPosts(final int page, final List<TaggedUser> taggedUserList) {
        try {
            if (page == 1) {
                taggedUsers.clear();
                taggedUsers.addAll(taggedUserList);
                notifyDataChanged();
            } else {
                taggedUsers.addAll(taggedUserList);
                fragment.getParentActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyItemRangeInserted((page - 1) * 10, taggedUserList.size());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TaggedViewHolder extends BaseRecyclerView.ViewHolder {

        @BindView(R.id.tagged_user_dp) CircularAppCompatImageView dp;
        TaggedUser taggedUser;

        TaggedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind() {
            String image = null;
            taggedUser = taggedUsers.get(getAdapterPosition());
            if (taggedUser.getProfileMedia() != null)
                image = taggedUser.getProfileMedia().getThumbUrl();
            Glide.with(fragment)
                    .load(image != null ? image : getGenderSpecificDpSmall(taggedUser.getGender()))
                    .apply(new RequestOptions()
                            .placeholder(getGenderSpecificDpSmall(taggedUser.getGender()))
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                    .into(dp);
        }

        @OnClick(R.id.tagged_user_dp) void openProfile() {
            fragment.navigation.pushFragment(taggedUsers.get(getAdapterPosition()).isMySelf() ?
                    ProfileFragment.newInstance() :
                    OthersProfileFragment.newInstance(
                            String.valueOf(taggedUsers.get(getAdapterPosition()).getUserId()), "", ""));
        }
    }
}
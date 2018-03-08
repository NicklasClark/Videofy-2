package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.data.model.base.ProfileMedia;
import com.cncoding.teazer.data.model.friends.FollowInfo;
import com.cncoding.teazer.data.model.post.LikedUser;

import java.util.List;
import java.util.Objects;

import static com.cncoding.teazer.utilities.diffutil.MyUserInfoDiffCallback.DIFF_FOLLOW_INFO;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_ACCOUNT_TYPE;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_FIRST_NAME;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_GENDER;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_HAS_PROFILE_MEDIA;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_IS_BLOCKED_YOU;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_IS_MYSELF;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_IS_YOU_BLOCKED;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_LAST_NAME;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_PROFILE_MEDIA;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_USERNAME;

/**
 * 
 * Created by Prem$ on 3/8/2018.
 */

public class LikedUserDiffCallback extends DiffUtil.Callback {

    public static final String DIFF_LIKED_USER = "likedUser";

    private List<LikedUser> oldLikedUserList;
    private List<LikedUser> newLikedUserList;

    public LikedUserDiffCallback(List<LikedUser> oldLikedUserList, List<LikedUser> newLikedUserList) {
        this.oldLikedUserList = oldLikedUserList;
        this.newLikedUserList = newLikedUserList;
    }

    @Override
    public int getOldListSize() {
        return oldLikedUserList.size();
    }

    @Override
    public int getNewListSize() {
        return newLikedUserList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newLikedUserList.get(newItemPosition).getUserId(), oldLikedUserList.get(oldItemPosition).getUserId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newLikedUserList.get(newItemPosition), oldLikedUserList.get(oldItemPosition));
    }

    @Nullable @Override public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        LikedUser oldLikedUser = oldLikedUserList.get(oldItemPosition);
        LikedUser newLikedUser = newLikedUserList.get(newItemPosition);

        if (oldLikedUser != null && newLikedUser != null) {
            Bundle diffBundle = new Bundle();
            try {
                if (!Objects.equals(oldLikedUser.getUserId(), newLikedUser.getUserId())) {
                    diffBundle.putParcelable(DIFF_LIKED_USER, newLikedUser);
                    return diffBundle;
                } else {
                    return getDiffBundle(oldLikedUser, newLikedUser, diffBundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getDiffBundle(oldLikedUser, newLikedUser, diffBundle);
            }
        } else {
            return null;
        }
    }

    private Object getDiffBundle(LikedUser oldLikedUser, LikedUser newLikedUser, Bundle diffBundle) {
        if (!Objects.equals(oldLikedUser.getUserName(), newLikedUser.getUserName()))
            diffBundle.putString(DIFF_USERNAME, newLikedUser.getUserName());
        if (!Objects.equals(oldLikedUser.getFirstName(), newLikedUser.getFirstName()))
            diffBundle.putString(DIFF_FIRST_NAME, newLikedUser.getFirstName());
        if (!Objects.equals(oldLikedUser.getLastName(), newLikedUser.getLastName()))
            diffBundle.putString(DIFF_LAST_NAME, newLikedUser.getLastName());
        if (!Objects.equals(oldLikedUser.getGender(), newLikedUser.getGender()))
            diffBundle.putInt(DIFF_GENDER, newLikedUser.getGender());
        if (!Objects.equals(oldLikedUser.isBlockedYou(), newLikedUser.isBlockedYou()))
            diffBundle.putBoolean(DIFF_IS_BLOCKED_YOU, newLikedUser.isBlockedYou());
        if (!Objects.equals(oldLikedUser.isYouBlocked(), newLikedUser.isYouBlocked()))
            diffBundle.putBoolean(DIFF_IS_YOU_BLOCKED, newLikedUser.isYouBlocked());
        if (!Objects.equals(oldLikedUser.isMySelf(), newLikedUser.isMySelf()))
            diffBundle.putBoolean(DIFF_IS_MYSELF, newLikedUser.isMySelf());
        if (!Objects.equals(oldLikedUser.getAccountType(), newLikedUser.getAccountType()))
            diffBundle.putInt(DIFF_ACCOUNT_TYPE, newLikedUser.getAccountType());
        if (!Objects.equals(oldLikedUser.hasProfileMedia(), newLikedUser.hasProfileMedia()))
            diffBundle.putBoolean(DIFF_HAS_PROFILE_MEDIA, newLikedUser.hasProfileMedia());
        if (!Objects.equals(oldLikedUser.getProfileMedia(), newLikedUser.getProfileMedia()))
            diffBundle.putParcelable(DIFF_PROFILE_MEDIA, newLikedUser.getProfileMedia());
        if (!Objects.equals(oldLikedUser.getFollowInfo(), newLikedUser.getFollowInfo()))
            diffBundle.putParcelable(DIFF_FOLLOW_INFO, newLikedUser.getFollowInfo());
        return diffBundle;
    }

    public static void updateLikedUserAccordingToDiffBundle(LikedUser likedUser, Bundle bundle) {
        for (String key : bundle.keySet()) {
            try {
                switch (key) {
                    case DIFF_USERNAME:
                        likedUser.setUserName(bundle.getString(DIFF_USERNAME));
                        break;
                    case DIFF_FIRST_NAME:
                        likedUser.setFirstName(bundle.getString(DIFF_FIRST_NAME));
                        break;
                    case DIFF_LAST_NAME:
                        likedUser.setLastName(bundle.getString(DIFF_LAST_NAME));
                        break;
                    case DIFF_GENDER:
                        likedUser.setGender(bundle.getInt(DIFF_GENDER));
                        break;
                    case DIFF_IS_BLOCKED_YOU:
                        likedUser.setIsBlockedYou(bundle.getBoolean(DIFF_IS_BLOCKED_YOU));
                        break;
                    case DIFF_IS_YOU_BLOCKED:
                        likedUser.setYouBlocked(bundle.getBoolean(DIFF_IS_YOU_BLOCKED));
                        break;
                    case DIFF_IS_MYSELF:
                        likedUser.setMySelf(bundle.getBoolean(DIFF_IS_MYSELF));
                        break;
                    case DIFF_ACCOUNT_TYPE:
                        likedUser.setAccountType(bundle.getInt(DIFF_ACCOUNT_TYPE));
                        break;
                    case DIFF_HAS_PROFILE_MEDIA:
                        likedUser.setHasProfileMedia(bundle.getBoolean(DIFF_HAS_PROFILE_MEDIA));
                        break;
                    case DIFF_PROFILE_MEDIA:
                        likedUser.setProfileMedia((ProfileMedia) bundle.getParcelable(DIFF_PROFILE_MEDIA));
                        break;
                    case DIFF_FOLLOW_INFO:
                        likedUser.setFollowInfo((FollowInfo) bundle.getParcelable(DIFF_FOLLOW_INFO));
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
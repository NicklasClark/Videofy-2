package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.data.model.base.ProfileMedia;
import com.cncoding.teazer.data.model.friends.FollowInfo;
import com.cncoding.teazer.data.model.friends.MyUserInfo;

import java.util.List;
import java.util.Objects;

import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_ACCOUNT_TYPE;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_FIRST_NAME;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_GENDER;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_HAS_PROFILE_MEDIA;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_IS_FOLLOWER;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_IS_FOLLOWING;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_IS_REQUEST_SENT;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_LAST_NAME;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_PROFILE_MEDIA;
import static com.cncoding.teazer.utilities.diffutil.UserInfoDiffCallback.DIFF_USERNAME;

/**
 * 
 * Created by Prem$ on 3/6/2018.
 */

public class MyUserInfoDiffCallback extends DiffUtil.Callback {

    static final String DIFF_FOLLOW_INFO = "followInfo";
    public static final String DIFF_MY_USER_INFO = "myUserInfo";

    private List<MyUserInfo> oldMyUserInfoList;
    private List<MyUserInfo> newMyUserInfoList;

    public MyUserInfoDiffCallback(List<MyUserInfo> oldMyUserInfoList, List<MyUserInfo> newMyUserInfoList) {
        this.oldMyUserInfoList = oldMyUserInfoList;
        this.newMyUserInfoList = newMyUserInfoList;
    }

    @Override
    public int getOldListSize() {
        return oldMyUserInfoList.size();
    }

    @Override
    public int getNewListSize() {
        return newMyUserInfoList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newMyUserInfoList.get(newItemPosition).getUserId(), oldMyUserInfoList.get(oldItemPosition).getUserId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newMyUserInfoList.get(newItemPosition), oldMyUserInfoList.get(oldItemPosition));
    }

    @Nullable @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        MyUserInfo oldMyUserInfo = oldMyUserInfoList.get(oldItemPosition);
        MyUserInfo newMyUserInfo = newMyUserInfoList.get(newItemPosition);

        if (oldMyUserInfo != null && newMyUserInfo != null) {
            Bundle diffBundle = new Bundle();
            try {
                if (!Objects.equals(oldMyUserInfo.getUserId(), newMyUserInfo.getUserId())) {
                    diffBundle.putParcelable(DIFF_MY_USER_INFO, newMyUserInfo);
                    return diffBundle;
                } else {
                    return getDiffBundle(oldMyUserInfo, newMyUserInfo, diffBundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getDiffBundle(oldMyUserInfo, newMyUserInfo, diffBundle);
            }
        } else {
            return null;
        }
    }

    private Object getDiffBundle(MyUserInfo oldMyUserInfo, MyUserInfo newMyUserInfo, Bundle diffBundle) {
        if (!Objects.equals(oldMyUserInfo.getUserName(), newMyUserInfo.getUserName()))
            diffBundle.putString(DIFF_USERNAME, newMyUserInfo.getUserName());
        if (!Objects.equals(oldMyUserInfo.getFirstName(), newMyUserInfo.getFirstName()))
            diffBundle.putString(DIFF_FIRST_NAME, newMyUserInfo.getFirstName());
        if (!Objects.equals(oldMyUserInfo.getLastName(), newMyUserInfo.getLastName()))
            diffBundle.putString(DIFF_LAST_NAME, newMyUserInfo.getLastName());
        if (!Objects.equals(oldMyUserInfo.getGender(), newMyUserInfo.getGender()))
            diffBundle.putInt(DIFF_GENDER, newMyUserInfo.getGender());
        if (!Objects.equals(oldMyUserInfo.getAccountType(), newMyUserInfo.getAccountType()))
            diffBundle.putInt(DIFF_ACCOUNT_TYPE, newMyUserInfo.getAccountType());
        if (!Objects.equals(oldMyUserInfo.hasProfileMedia(), newMyUserInfo.hasProfileMedia()))
            diffBundle.putBoolean(DIFF_HAS_PROFILE_MEDIA, newMyUserInfo.hasProfileMedia());
        if (!Objects.equals(oldMyUserInfo.getProfileMedia(), newMyUserInfo.getProfileMedia()))
            diffBundle.putParcelable(DIFF_PROFILE_MEDIA, newMyUserInfo.getProfileMedia());
        if (!Objects.equals(oldMyUserInfo.isFollowing(), newMyUserInfo.isFollowing()))
            diffBundle.putBoolean(DIFF_IS_FOLLOWING, newMyUserInfo.isFollowing());
        if (!Objects.equals(oldMyUserInfo.isFollower(), newMyUserInfo.isFollower()))
            diffBundle.putBoolean(DIFF_IS_FOLLOWER, newMyUserInfo.isFollower());
        if (!Objects.equals(oldMyUserInfo.isRequestSent(), newMyUserInfo.isRequestSent()))
            diffBundle.putBoolean(DIFF_IS_REQUEST_SENT, newMyUserInfo.isRequestSent());
        if (!Objects.equals(oldMyUserInfo.getFollowInfo(), newMyUserInfo.getFollowInfo()))
            diffBundle.putParcelable(DIFF_FOLLOW_INFO, newMyUserInfo.getFollowInfo());
        return diffBundle;
    }

    public static void updateMyUserInfoAccordingToDiffBundle(MyUserInfo myUserInfo, Bundle bundle) {
        for (String key : bundle.keySet()) {
            try {
                switch (key) {
                    case DIFF_USERNAME:
                        myUserInfo.setUserName(bundle.getString(DIFF_USERNAME));
                        break;
                    case DIFF_FIRST_NAME:
                        myUserInfo.setFirstName(bundle.getString(DIFF_FIRST_NAME));
                        break;
                    case DIFF_LAST_NAME:
                        myUserInfo.setLastName(bundle.getString(DIFF_LAST_NAME));
                        break;
                    case DIFF_GENDER:
                        myUserInfo.setGender(bundle.getInt(DIFF_GENDER));
                        break;
                    case DIFF_ACCOUNT_TYPE:
                        myUserInfo.setAccountType(bundle.getInt(DIFF_ACCOUNT_TYPE));
                        break;
                    case DIFF_HAS_PROFILE_MEDIA:
                        myUserInfo.setHasProfileMedia(bundle.getBoolean(DIFF_HAS_PROFILE_MEDIA));
                        break;
                    case DIFF_PROFILE_MEDIA:
                        myUserInfo.setProfileMedia((ProfileMedia) bundle.getParcelable(DIFF_PROFILE_MEDIA));
                        break;
                    case DIFF_IS_FOLLOWING:
                        myUserInfo.setFollowing(bundle.getBoolean(DIFF_IS_FOLLOWING));
                        break;
                    case DIFF_IS_FOLLOWER:
                        myUserInfo.setFollower(bundle.getBoolean(DIFF_IS_FOLLOWER));
                        break;
                    case DIFF_IS_REQUEST_SENT:
                        myUserInfo.setRequestSent(bundle.getBoolean(DIFF_IS_REQUEST_SENT));
                        break;
                    case DIFF_FOLLOW_INFO:
                        myUserInfo.setFollowInfo((FollowInfo) bundle.getParcelable(DIFF_FOLLOW_INFO));
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
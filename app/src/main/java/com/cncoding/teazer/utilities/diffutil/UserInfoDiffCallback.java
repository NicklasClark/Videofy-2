package com.cncoding.teazer.utilities.diffutil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.data.model.friends.UserInfo;

import java.util.List;
import java.util.Objects;

/**
 * 
 * Created by Prem$ on 3/6/2018.
 */

public class UserInfoDiffCallback extends DiffUtil.Callback {

    private static final String DIFF_USER_INFO = "userInfo";
    private static final String DIFF_USERNAME = "username";
    private static final String DIFF_FIRST_NAME = "firstName";
    private static final String DIFF_LAST_NAME = "lastName";
    private static final String DIFF_GENDER = "gender";
    private static final String DIFF_IS_BLOCKED_YOU = "isBlockedYou";
    private static final String DIFF_IS_YOU_BLOCKED = "youBlocked";
    private static final String DIFF_IS_MYSELF = "mySelf";
    private static final String DIFF_ACCOUNT_TYPE = "accountType";
    private static final String DIFF_HAS_PROFILE_MEDIA = "hasProfileMedia";
    private static final String DIFF_PROFILE_MEDIA = "profileMedia";
    private static final String DIFF_IS_FOLLOWING = "following";
    private static final String DIFF_IS_FOLLOWER = "follower";
    private static final String DIFF_IS_REQUEST_SENT = "requestSent";
    private static final String DIFF_REQUEST_ID = "requestId";
    private static final String DIFF_IS_REQUEST_RECEIVED = "requestRecieved";

    private List<UserInfo> oldUserInfoList;
    private List<UserInfo> newUserInfoList;

    public UserInfoDiffCallback(List<UserInfo> oldUserInfoList, List<UserInfo> newUserInfoList) {
        this.oldUserInfoList = oldUserInfoList;
        this.newUserInfoList = newUserInfoList;
    }

    @Override
    public int getOldListSize() {
        return oldUserInfoList.size();
    }

    @Override
    public int getNewListSize() {
        return newUserInfoList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newUserInfoList.get(newItemPosition).getUserId(), oldUserInfoList.get(oldItemPosition).getUserId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return Objects.equals(newUserInfoList.get(newItemPosition), oldUserInfoList.get(oldItemPosition));
    }

    @Nullable @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        UserInfo oldUserInfo = oldUserInfoList.get(oldItemPosition);
        UserInfo newUserInfo = newUserInfoList.get(newItemPosition);

        if (oldUserInfo != null && newUserInfo != null) {
            Bundle diffBundle = new Bundle();
            try {
                if (!Objects.equals(oldUserInfo.getUserId(), newUserInfo.getUserId())) {
                    diffBundle.putParcelable(DIFF_USER_INFO, newUserInfo);
                    return diffBundle;
                } else {
                    return getDiffBundle(oldUserInfo, newUserInfo, diffBundle);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return getDiffBundle(oldUserInfo, newUserInfo, diffBundle);
            }
        } else {
            return null;
        }
    }

    private Object getDiffBundle(UserInfo oldUserInfo, UserInfo newUserInfo, Bundle diffBundle) {
        if (!Objects.equals(oldUserInfo.getUserName(), newUserInfo.getUserName()))
            diffBundle.putString(DIFF_USERNAME, newUserInfo.getUserName());
        if (!Objects.equals(oldUserInfo.getFirstName(), newUserInfo.getFirstName()))
            diffBundle.putString(DIFF_FIRST_NAME, newUserInfo.getFirstName());
        if (!Objects.equals(oldUserInfo.getLastName(), newUserInfo.getLastName()))
            diffBundle.putString(DIFF_LAST_NAME, newUserInfo.getLastName());
        if (!Objects.equals(oldUserInfo.getGender(), newUserInfo.getGender()))
            diffBundle.putInt(DIFF_GENDER, newUserInfo.getGender());
        if (!Objects.equals(oldUserInfo.isBlockedYou(), newUserInfo.isBlockedYou()))
            diffBundle.putBoolean(DIFF_IS_BLOCKED_YOU, newUserInfo.isBlockedYou());
        if (!Objects.equals(oldUserInfo.isYouBlocked(), newUserInfo.isYouBlocked()))
            diffBundle.putBoolean(DIFF_IS_YOU_BLOCKED, newUserInfo.isYouBlocked());
        if (!Objects.equals(oldUserInfo.isMySelf(), newUserInfo.isMySelf()))
            diffBundle.putBoolean(DIFF_IS_MYSELF, newUserInfo.isMySelf());
        if (!Objects.equals(oldUserInfo.getAccountType(), newUserInfo.getAccountType()))
            diffBundle.putInt(DIFF_ACCOUNT_TYPE, newUserInfo.getAccountType());
        if (!Objects.equals(oldUserInfo.hasProfileMedia(), newUserInfo.hasProfileMedia()))
            diffBundle.putBoolean(DIFF_HAS_PROFILE_MEDIA, newUserInfo.hasProfileMedia());
        if (!Objects.equals(oldUserInfo.getProfileMedia(), newUserInfo.getProfileMedia()))
            diffBundle.putParcelable(DIFF_PROFILE_MEDIA, newUserInfo.getProfileMedia());
        if (!Objects.equals(oldUserInfo.isFollowing(), newUserInfo.isFollowing()))
            diffBundle.putBoolean(DIFF_IS_FOLLOWING, newUserInfo.isFollowing());
        if (!Objects.equals(oldUserInfo.isFollower(), newUserInfo.isFollower()))
            diffBundle.putBoolean(DIFF_IS_FOLLOWER, newUserInfo.isFollower());
        if (!Objects.equals(oldUserInfo.isRequestSent(), newUserInfo.isRequestSent()))
            diffBundle.putBoolean(DIFF_IS_REQUEST_SENT, newUserInfo.isRequestSent());
        if (!Objects.equals(oldUserInfo.getRequestId(), newUserInfo.getRequestId()))
            diffBundle.putInt(DIFF_REQUEST_ID, newUserInfo.getRequestId());
        if (!Objects.equals(oldUserInfo.isRequestRecieved(), newUserInfo.isRequestRecieved()))
            diffBundle.putBoolean(DIFF_IS_REQUEST_RECEIVED, newUserInfo.isRequestRecieved());
        return diffBundle;
    }

    public static void updatePostDetailsAccordingToDiffBundle(UserInfo userInfo, Bundle bundle) {
        for (String key : bundle.keySet()) {
            try {
                switch (key) {
                    case DIFF_USERNAME:
                        userInfo.setUserName(bundle.getString(DIFF_USERNAME));
                        break;
                    case DIFF_FIRST_NAME:
                        userInfo.setFirstName(bundle.getString(DIFF_FIRST_NAME));
                        break;
                    case DIFF_LAST_NAME:
                        userInfo.setLastName(bundle.getString(DIFF_LAST_NAME));
                        break;
                    case DIFF_GENDER:
                        userInfo.setGender(bundle.getInt(DIFF_GENDER));
                        break;
                    case DIFF_IS_BLOCKED_YOU:
                        userInfo.setIsBlockedYou(bundle.getBoolean(DIFF_IS_BLOCKED_YOU));
                        break;
                    case DIFF_IS_YOU_BLOCKED:
                        userInfo.setYouBlocked(bundle.getBoolean(DIFF_IS_YOU_BLOCKED));
                        break;
                    case DIFF_IS_MYSELF:
                        userInfo.setMySelf(bundle.getBoolean(DIFF_IS_MYSELF));
                        break;
                    case DIFF_ACCOUNT_TYPE:
                        userInfo.setAccountType(bundle.getInt(DIFF_ACCOUNT_TYPE));
                        break;
                    case DIFF_HAS_PROFILE_MEDIA:
                        userInfo.setHasProfileMedia(bundle.getBoolean(DIFF_HAS_PROFILE_MEDIA));
                        break;
                    case DIFF_PROFILE_MEDIA:
                        userInfo.setProfileMedia((Medias) bundle.getParcelable(DIFF_PROFILE_MEDIA));
                        break;
                    case DIFF_IS_FOLLOWING:
                        userInfo.setFollowing(bundle.getBoolean(DIFF_IS_FOLLOWING));
                        break;
                    case DIFF_IS_FOLLOWER:
                        userInfo.setFollower(bundle.getBoolean(DIFF_IS_FOLLOWER));
                        break;
                    case DIFF_IS_REQUEST_SENT:
                        userInfo.setRequestSent(bundle.getBoolean(DIFF_IS_REQUEST_SENT));
                        break;
                    case DIFF_REQUEST_ID:
                        userInfo.setRequestId(bundle.getInt(DIFF_REQUEST_ID));
                        break;
                    case DIFF_IS_REQUEST_RECEIVED:
                        userInfo.setRequestRecieved(bundle.getBoolean(DIFF_IS_REQUEST_RECEIVED));
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
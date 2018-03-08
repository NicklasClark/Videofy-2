package com.cncoding.teazer.injection.module.mainmodule.local;

import android.arch.lifecycle.MediatorLiveData;

import com.cncoding.teazer.data.model.application.ConfigDetails;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.discover.LandingPostsV2;
import com.cncoding.teazer.data.model.discover.VideosList;
import com.cncoding.teazer.data.model.friends.CircleList;
import com.cncoding.teazer.data.model.friends.FollowersList;
import com.cncoding.teazer.data.model.friends.FollowingsList;
import com.cncoding.teazer.data.model.friends.ProfileInfo;
import com.cncoding.teazer.data.model.friends.UsersList;
import com.cncoding.teazer.data.model.post.LikedUserList;
import com.cncoding.teazer.data.model.post.PostDetails;
import com.cncoding.teazer.data.model.post.PostList;
import com.cncoding.teazer.data.model.post.PostUploadResult;
import com.cncoding.teazer.data.model.post.TaggedUsersList;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.model.react.HiddenReactionsList;
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.utilities.common.Annotations;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module
public class LiveDataModule {

    @Provides @Singleton public MediatorLiveData<ResultObject> resultObjectLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<ConfigDetails> configDetailsLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<List<ReportTypes>> reportTypesListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<List<DeactivateTypes>> deactivateTypesListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<List<Category>> categoriesListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<LandingPostsV2> landingPostsLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton @Named(Annotations.POST_LIST) public MediatorLiveData<PostList> postListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton @Named(Annotations.MOST_POPULAR) public MediatorLiveData<PostList> mostPopularLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<UsersList> usersListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<VideosList> videosLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<CircleList> circleListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<FollowingsList> followingsListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<FollowersList> followersListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<ProfileInfo> profileInfoLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<BlockedUsersList> blockedUsersListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<LikedUserList> likedUserListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<PostDetails> postDetailsLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<PostUploadResult> postUploadResultLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<TaggedUsersList> taggedUsersListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<ReactionResponse> reactionResponseLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<ReactionsList> reactionsListLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<HiddenReactionsList> hiddenReactionsLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<UserProfile> userProfileLiveData() {
        return new MediatorLiveData<>();
    }

    @Provides @Singleton public MediatorLiveData<NotificationsList> notificationsLiveData() {
        return new MediatorLiveData<>();
    }
}
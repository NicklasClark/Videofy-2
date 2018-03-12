package com.cncoding.teazer.injection.module.base.local;

import com.cncoding.teazer.data.BrokerLiveData;
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
import com.cncoding.teazer.data.model.react.ReactionResponse;
import com.cncoding.teazer.data.model.react.ReactionsList;
import com.cncoding.teazer.data.model.user.BlockedUsersList;
import com.cncoding.teazer.data.model.user.NotificationsList;
import com.cncoding.teazer.data.model.user.UserProfile;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.injection.scope.BaseScope;
import com.cncoding.teazer.utilities.common.Annotations;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module
public class LiveDataModule {

    @Provides @BaseScope
    public BrokerLiveData<ResultObject> resultObjectLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<ConfigDetails> configDetailsLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<List<ReportTypes>> reportTypesListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<List<DeactivateTypes>> deactivateTypesListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<List<Category>> categoriesListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<LandingPostsV2> landingPostsLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    @Named(Annotations.POST_LIST) public BrokerLiveData<PostList> postListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    @Named(Annotations.MOST_POPULAR) public BrokerLiveData<PostList> mostPopularLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<UsersList> usersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<VideosList> videosListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<CircleList> circleListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<FollowingsList> followingsListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<FollowersList> followersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<ProfileInfo> profileInfoLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<BlockedUsersList> blockedUsersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<LikedUserList> likedUserListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<PostDetails> postDetailsLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<PostUploadResult> postUploadResultLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<TaggedUsersList> taggedUsersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<ReactionResponse> reactionResponseLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<ReactionsList> reactionsListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<UserProfile> userProfileLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    public BrokerLiveData<NotificationsList> notificationsLiveData() {
        return new BrokerLiveData<>();
    }
}
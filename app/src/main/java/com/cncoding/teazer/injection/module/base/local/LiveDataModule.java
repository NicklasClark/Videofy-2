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

@BaseScope
@Module
public class LiveDataModule {

    @Provides @BaseScope
    BrokerLiveData<ResultObject> resultObjectLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<ConfigDetails> configDetailsLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<List<ReportTypes>> reportTypesListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<List<DeactivateTypes>> deactivateTypesListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<List<Category>> categoriesListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<LandingPostsV2> landingPostsLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    @Named(Annotations.POST_LIST) BrokerLiveData<PostList> postListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    @Named(Annotations.MOST_POPULAR) BrokerLiveData<PostList> mostPopularLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<UsersList> usersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<VideosList> videosListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<CircleList> circleListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<FollowingsList> followingsListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<FollowersList> followersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<ProfileInfo> profileInfoLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<BlockedUsersList> blockedUsersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<LikedUserList> likedUserListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<PostDetails> postDetailsLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<PostUploadResult> postUploadResultLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<TaggedUsersList> taggedUsersListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<ReactionResponse> reactionResponseLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<ReactionsList> reactionsListLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<UserProfile> userProfileLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @BaseScope
    BrokerLiveData<NotificationsList> notificationsLiveData() {
        return new BrokerLiveData<>();
    }
}
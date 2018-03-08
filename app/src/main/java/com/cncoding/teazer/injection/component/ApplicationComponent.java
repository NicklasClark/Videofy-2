/*
 * *
 *  * Copyright (C) 2017 Ryan Kay Open Source Project
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.cncoding.teazer.injection.component;

import android.arch.lifecycle.MediatorLiveData;

import com.cncoding.teazer.data.local.database.TeazerDB;
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
import com.cncoding.teazer.data.remote.apicalls.application.ApplicationRepository;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepository;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepository;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsRepository;
import com.cncoding.teazer.data.remote.apicalls.giphy.GiphyRepository;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepository;
import com.cncoding.teazer.data.remote.apicalls.react.ReactRepository;
import com.cncoding.teazer.data.remote.apicalls.user.UserRepository;
import com.cncoding.teazer.injection.module.mainmodule.local.LiveDataModule;
import com.cncoding.teazer.injection.module.mainmodule.local.RoomModule;
import com.cncoding.teazer.injection.module.mainmodule.remote.RepositoryModule;
import com.cncoding.teazer.utilities.common.Annotations;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Annotated as a Singleton since we don't want to have multiple instances of a Single Database,
 * <p>
 * Created by Prem$ on 8/15/2017.
 */

@Singleton
@Component(modules = {
        RoomModule.class, RepositoryModule.class, LiveDataModule.class,
})
public interface ApplicationComponent {

    MediatorLiveData<ResultObject> resultObjectLiveData();
    MediatorLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData();
    MediatorLiveData<List<ReportTypes>> reportTypesListLiveData();
    MediatorLiveData<List<DeactivateTypes>> deactivateTypesListLiveData();
    MediatorLiveData<List<Category>> categoriesListLiveData();
    MediatorLiveData<LandingPostsV2> landingPostsLiveData();
    @Named(Annotations.POST_LIST) MediatorLiveData<PostList> postListLiveData();
    @Named(Annotations.MOST_POPULAR) MediatorLiveData<PostList> mostPopularLiveData();
    MediatorLiveData<UsersList> usersListLiveData();
    MediatorLiveData<VideosList> videosLiveData();
    MediatorLiveData<CircleList> circleListLiveData();
    MediatorLiveData<FollowingsList> followingsListLiveData();
    MediatorLiveData<FollowersList> followersListLiveData();
    MediatorLiveData<ProfileInfo> profileInfoLiveData();
    MediatorLiveData<BlockedUsersList> blockedUsersListLiveData();
    MediatorLiveData<LikedUserList> likedUserListLiveData();
    MediatorLiveData<PostDetails> postDetailsLiveData();
    MediatorLiveData<PostUploadResult> postUploadResultLiveData();
    MediatorLiveData<TaggedUsersList> taggedUsersListLiveData();
    MediatorLiveData<ReactionResponse> reactionResponseLiveData();
    MediatorLiveData<ReactionsList> reactionsListLiveData();
    MediatorLiveData<HiddenReactionsList> hiddenReactionsLiveData();
    MediatorLiveData<UserProfile> userProfileLiveData();
    MediatorLiveData<NotificationsList> notificationsLiveData();

    ApplicationRepository applicationRepository();
    AuthenticationRepository authenticationRepository();
    DiscoverRepository discoverRepository();
    FriendsRepository friendsRepository();
    PostsRepository postsRepository();
    ReactRepository reactRepository();
    UserRepository userRepository();
    GiphyRepository giphyRepository();
    TeazerDB database();
}
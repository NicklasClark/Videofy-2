package com.cncoding.teazer.injection.home.base.module.remote;

import com.cncoding.teazer.data.remote.apicalls.application.ApplicationRepository;
import com.cncoding.teazer.data.remote.apicalls.application.ApplicationRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.application.ApplicationService;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepository;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverService;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsRepository;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsService;
import com.cncoding.teazer.data.remote.apicalls.giphy.GiphyRepository;
import com.cncoding.teazer.data.remote.apicalls.giphy.GiphyRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.giphy.GiphyService;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepository;
import com.cncoding.teazer.data.remote.apicalls.post.PostsRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.post.PostsService;
import com.cncoding.teazer.data.remote.apicalls.react.ReactRepository;
import com.cncoding.teazer.data.remote.apicalls.react.ReactRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.react.ReactService;
import com.cncoding.teazer.data.remote.apicalls.user.UserRepository;
import com.cncoding.teazer.data.remote.apicalls.user.UserRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.user.UserService;
import com.cncoding.teazer.injection.scope.BaseScope;

import dagger.Module;
import dagger.Provides;

/**
 * 
 * Created by Prem$ on 3/7/2018.
 */

@Module(includes = ServiceModule.class)
public class RepositoryModule {

    @Provides @BaseScope
    ApplicationRepository applicationRepository(ApplicationService applicationService) {
        return new ApplicationRepositoryImpl(applicationService);
    }

    @Provides @BaseScope
    DiscoverRepository discoverRepository(DiscoverService discoverService) {
        return new DiscoverRepositoryImpl(discoverService);
    }

    @Provides @BaseScope
    FriendsRepository friendsRepository(FriendsService friendsService) {
        return new FriendsRepositoryImpl(friendsService);
    }

    @Provides @BaseScope
    PostsRepository postsRepository(PostsService postsService) {
        return new PostsRepositoryImpl(postsService);
    }

    @Provides @BaseScope
    ReactRepository reactRepository(ReactService reactService) {
        return new ReactRepositoryImpl(reactService);
    }

    @Provides @BaseScope
    UserRepository userRepository(UserService userService) {
        return new UserRepositoryImpl(userService);
    }

    @Provides @BaseScope
    GiphyRepository giphyRepository(GiphyService giphyService) {
        return new GiphyRepositoryImpl(giphyService);
    }
}
package com.cncoding.teazer.injection.module.mainmodule.remote;

import com.cncoding.teazer.data.remote.apicalls.application.ApplicationService;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationService;
import com.cncoding.teazer.data.remote.apicalls.discover.DiscoverService;
import com.cncoding.teazer.data.remote.apicalls.friends.FriendsService;
import com.cncoding.teazer.data.remote.apicalls.giphy.GiphyService;
import com.cncoding.teazer.data.remote.apicalls.post.PostsService;
import com.cncoding.teazer.data.remote.apicalls.react.ReactService;
import com.cncoding.teazer.data.remote.apicalls.user.UserService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import static com.cncoding.teazer.utilities.common.Annotations.GIPHY;
import static com.cncoding.teazer.utilities.common.Annotations.WITHOUT_AUTH_TOKEN;
import static com.cncoding.teazer.utilities.common.Annotations.WITH_AUTH_TOKEN;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module(includes = RetrofitModule.class)
public class ServiceModule {

    @Provides @Singleton DiscoverService getDiscoverService(@Named(WITH_AUTH_TOKEN) Retrofit retrofitWithAuthToken) {
        return retrofitWithAuthToken.create(DiscoverService.class);
    }

    @Provides @Singleton FriendsService getFriendsService(@Named(WITH_AUTH_TOKEN) Retrofit retrofitWithAuthToken) {
        return retrofitWithAuthToken.create(FriendsService.class);
    }

    @Provides @Singleton PostsService getPostsService(@Named(WITH_AUTH_TOKEN) Retrofit retrofitWithAuthToken) {
        return retrofitWithAuthToken.create(PostsService.class);
    }

    @Provides @Singleton ReactService getReactService(@Named(WITH_AUTH_TOKEN) Retrofit retrofitWithAuthToken) {
        return retrofitWithAuthToken.create(ReactService.class);
    }

    @Provides @Singleton UserService getUserService(@Named(WITH_AUTH_TOKEN) Retrofit retrofitWithAuthToken) {
        return retrofitWithAuthToken.create(UserService.class);
    }

    @Provides @Singleton ApplicationService getApplicationService(@Named(WITHOUT_AUTH_TOKEN) Retrofit retrofitWithoutAuthToken) {
        return retrofitWithoutAuthToken.create(ApplicationService.class);
    }

    @Provides @Singleton AuthenticationService getAuthenticationService(@Named(WITHOUT_AUTH_TOKEN) Retrofit retrofitWithoutAuthToken) {
        return retrofitWithoutAuthToken.create(AuthenticationService.class);
    }

    @Provides @Singleton GiphyService getGiphyService(@Named(GIPHY) Retrofit giphyRetrofit) {
        return giphyRetrofit.create(GiphyService.class);
    }
}
package com.cncoding.teazer.injection.module.auth;

import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationService;
import com.cncoding.teazer.injection.scope.AuthScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

import static com.cncoding.teazer.utilities.common.Annotations.WITHOUT_AUTH_TOKEN;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

@Module(includes = AuthNetworkModule.class)
public class AuthServiceModule {

    @Provides @AuthScope
    AuthenticationService getAuthenticationService(@Named(WITHOUT_AUTH_TOKEN) Retrofit retrofitWithoutAuthToken) {
        return retrofitWithoutAuthToken.create(AuthenticationService.class);
    }
}
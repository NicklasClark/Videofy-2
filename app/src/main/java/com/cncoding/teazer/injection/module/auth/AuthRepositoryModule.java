package com.cncoding.teazer.injection.module.auth;

import com.cncoding.teazer.data.BrokerLiveData;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepository;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl;
import com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationService;
import com.cncoding.teazer.injection.scope.AuthScope;

import dagger.Module;
import dagger.Provides;

/**
 * 
 * Created by Prem$ on 3/7/2018.
 */

@Module(includes = {AuthServiceModule.class})
public class AuthRepositoryModule {

    @Provides @AuthScope
    BrokerLiveData<ResultObject> resultObjectLiveData() {
        return new BrokerLiveData<>();
    }

    @Provides @AuthScope
    AuthenticationRepository authenticationRepository(AuthenticationService authenticationService) {
        return new AuthenticationRepositoryImpl(authenticationService);
    }
}
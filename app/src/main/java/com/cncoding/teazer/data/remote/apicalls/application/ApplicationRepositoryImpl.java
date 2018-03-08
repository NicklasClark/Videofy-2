package com.cncoding.teazer.data.remote.apicalls.application;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.model.application.ConfigBody;
import com.cncoding.teazer.data.model.application.ConfigDetails;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.PushNotificationBody;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.remote.ResultObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.reportTypesCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.resultObjectCallback;
import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.setListCallType;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithoutAuthToken;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.FAILED;
import static com.cncoding.teazer.data.remote.apicalls.authentication.AuthenticationRepositoryImpl.NOT_SUCCESSFUL;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_CATEGORIES;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_DEACTIVATION_TYPES_LIST;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_DEFAULT_COVER_IMAGES;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_POST_REPORT_TYPES;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_PROFILE_REPORT_TYPES;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_GET_REACTION_REPORT_TYPES;
import static com.cncoding.teazer.utilities.common.Annotations.CALL_SEND_PUSH_NOTIFICATION_TO_ALL;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class ApplicationRepositoryImpl implements ApplicationRepository {

    private ApplicationService applicationService;

    @Inject public ApplicationRepositoryImpl(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public ApplicationRepositoryImpl() {
        applicationService = getRetrofitWithoutAuthToken().create(ApplicationService.class);
    }

    @Override public LiveData<ResultObject> sendPushNotificationToAll(PushNotificationBody pushNotificationBody) {
        MutableLiveData<ResultObject> liveData = new MutableLiveData<>();
        applicationService.sendPushNotificationToAll(pushNotificationBody)
                .enqueue(resultObjectCallback(liveData, CALL_SEND_PUSH_NOTIFICATION_TO_ALL));
        return liveData;
    }

    @Override public LiveData<DefaultCoverImageResponse> getDefaultCoverImages(int page) {
        final MutableLiveData<DefaultCoverImageResponse> liveData = new MutableLiveData<>();
        applicationService.getDefaultCoverImages(page)
                .enqueue(new Callback<DefaultCoverImageResponse>() {
                    @Override
                    public void onResponse(Call<DefaultCoverImageResponse> call, Response<DefaultCoverImageResponse> response) {
                        liveData.setValue(response.isSuccessful() ?
                                response.body().setCallType(CALL_GET_DEFAULT_COVER_IMAGES) :
                                new DefaultCoverImageResponse(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_DEFAULT_COVER_IMAGES));
                    }

                    @Override
                    public void onFailure(Call<DefaultCoverImageResponse> call, Throwable t) {
                        t.printStackTrace();
                        liveData.setValue(new DefaultCoverImageResponse(new Throwable(FAILED)).setCallType(CALL_GET_DEFAULT_COVER_IMAGES));
                    }
                });
        return liveData;
    }

    @Override public LiveData<ConfigDetails> getUpdateAndConfigDetails(ConfigBody configBody) {
        final MutableLiveData<ConfigDetails> liveData = new MutableLiveData<>();
        applicationService.getUpdateAndConfigDetails(configBody)
                .enqueue(new Callback<ConfigDetails>() {
                    @Override
                    public void onResponse(Call<ConfigDetails> call, Response<ConfigDetails> response) {
                        liveData.setValue(response.isSuccessful() ?
                                response.body().setCallType(CALL_GET_DEFAULT_COVER_IMAGES) :
                                new ConfigDetails(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_DEFAULT_COVER_IMAGES));
                    }

                    @Override
                    public void onFailure(Call<ConfigDetails> call, Throwable t) {
                        t.printStackTrace();
                        liveData.setValue(new ConfigDetails(new Throwable(FAILED)).setCallType(CALL_GET_DEFAULT_COVER_IMAGES));
                    }
                });
        return liveData;
    }

    @Override public LiveData<List<ReportTypes>> getPostReportTypes() {
        final MutableLiveData<List<ReportTypes>> liveData = new MutableLiveData<>();
        applicationService.getPostReportTypes().enqueue(reportTypesCallback(liveData, CALL_GET_POST_REPORT_TYPES));
        return liveData;
    }

    @Override public LiveData<List<ReportTypes>> getReactionReportTypes() {
        final MutableLiveData<List<ReportTypes>> liveData = new MutableLiveData<>();
        applicationService.getReactionReportTypes().enqueue(reportTypesCallback(liveData, CALL_GET_REACTION_REPORT_TYPES));
        return liveData;
    }

    @Override public LiveData<List<ReportTypes>> getProfileReportTypes() {
        final MutableLiveData<List<ReportTypes>> liveData = new MutableLiveData<>();
        applicationService.getProfileReportTypes().enqueue(reportTypesCallback(liveData, CALL_GET_PROFILE_REPORT_TYPES));
        return liveData;
    }

    @Override public LiveData<List<DeactivateTypes>> getDeactivationTypesList() {
        final MutableLiveData<List<DeactivateTypes>> liveData = new MutableLiveData<>();
        applicationService.getDeactivationTypesList().enqueue(new Callback<List<DeactivateTypes>>() {
            @Override
            public void onResponse(Call<List<DeactivateTypes>> call, Response<List<DeactivateTypes>> response) {
                if (response.isSuccessful()) {
                    List<DeactivateTypes> reportTypesList = response.body();
                    setListCallType(reportTypesList, CALL_GET_DEACTIVATION_TYPES_LIST);
                    liveData.setValue(reportTypesList);
                } else {
                    DeactivateTypes response1 = new DeactivateTypes(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_DEACTIVATION_TYPES_LIST);
                    List<DeactivateTypes> list = new ArrayList<>();
                    list.add(response1);
                    liveData.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<List<DeactivateTypes>> call, Throwable t) {
                t.printStackTrace();
                DeactivateTypes response1 = new DeactivateTypes(new Throwable(FAILED)).setCallType(CALL_GET_DEACTIVATION_TYPES_LIST);
                List<DeactivateTypes> list = new ArrayList<>();
                list.add(response1);
                liveData.setValue(list);
            }
        });
        return liveData;
    }

    @Override public LiveData<List<Category>> getCategories() {
        final MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        applicationService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> reportTypesList = response.body();
                    setListCallType(reportTypesList, CALL_GET_CATEGORIES);
                    liveData.setValue(reportTypesList);
                } else {
                    Category response1 = new Category(new Throwable(NOT_SUCCESSFUL)).setCallType(CALL_GET_CATEGORIES);
                    List<Category> list = new ArrayList<>();
                    list.add(response1);
                    liveData.setValue(list);
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                t.printStackTrace();
                Category response1 = new Category(new Throwable(FAILED)).setCallType(CALL_GET_CATEGORIES);
                List<Category> list = new ArrayList<>();
                list.add(response1);
                liveData.setValue(list);
            }
        });
        return liveData;
    }
}
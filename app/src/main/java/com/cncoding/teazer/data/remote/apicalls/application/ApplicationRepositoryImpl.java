package com.cncoding.teazer.data.remote.apicalls.application;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.data.model.base.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.data.remote.apicalls.CallbackFactory.reportPostTitlesResponse;
import static com.cncoding.teazer.data.remote.apicalls.ClientProvider.getRetrofitWithoutAuthToken;

/**
 *
 * Created by Prem$ on 2/5/2018.
 */

public class ApplicationRepositoryImpl implements ApplicationRepository {

    private ApplicationService applicationService;

    public ApplicationRepositoryImpl() {
        applicationService = getRetrofitWithoutAuthToken().create(ApplicationService.class);
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getPostReportTypes() {
        final MutableLiveData<List<ReportPostTitlesResponse>> liveData = new MutableLiveData<>();
        applicationService.getPostReportTypes().enqueue(reportPostTitlesResponse(liveData));
        return liveData;
    }

    @Override
    public LiveData<List<ReportPostTitlesResponse>> getProfileReportTypes() {
        final MutableLiveData<List<ReportPostTitlesResponse>> liveData = new MutableLiveData<>();
        applicationService.getProfileReportTypes().enqueue(reportPostTitlesResponse(liveData));
        return liveData;
    }

    @Override
    public LiveData<List<DeactivateTypes>> getDeactivationTypesList() {
        final MutableLiveData<List<DeactivateTypes>> liveData = new MutableLiveData<>();
        applicationService.getDeactivationTypesList().enqueue(new Callback<List<DeactivateTypes>>() {
            @Override
            public void onResponse(Call<List<DeactivateTypes>> call, Response<List<DeactivateTypes>> response) {
                if (response.isSuccessful()) liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<DeactivateTypes>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return liveData;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        final MutableLiveData<List<Category>> liveData = new MutableLiveData<>();
        applicationService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return liveData;
    }
}

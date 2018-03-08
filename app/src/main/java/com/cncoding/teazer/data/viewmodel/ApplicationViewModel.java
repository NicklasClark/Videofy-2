package com.cncoding.teazer.data.viewmodel;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.cncoding.teazer.data.model.application.ConfigBody;
import com.cncoding.teazer.data.model.application.ConfigDetails;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.PushNotificationBody;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.remote.ResultObject;
import com.cncoding.teazer.data.remote.apicalls.application.ApplicationRepository;
import com.cncoding.teazer.data.remote.apicalls.application.ApplicationRepositoryImpl;

import java.util.List;

import javax.inject.Inject;

/**
 *
 * Created by Prem$ on 3/7/2018.
 */

public class ApplicationViewModel extends ViewModel {

    private MediatorLiveData<ResultObject> resultObjectLiveData;
    private MediatorLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData;
    private MediatorLiveData<ConfigDetails> configDetailsLiveData;
    private MediatorLiveData<List<ReportTypes>> reportTypesListLiveData;
    private MediatorLiveData<List<DeactivateTypes>> deactivateTypesListLiveData;
    private MediatorLiveData<List<Category>> categoriesListLiveData;
    private ApplicationRepository applicationRepository;

    @Inject public ApplicationViewModel(MediatorLiveData<ResultObject> resultObjectLiveData,
                                MediatorLiveData<DefaultCoverImageResponse> defaultCoverImageLiveData,
                                MediatorLiveData<ConfigDetails> configDetailsLiveData, MediatorLiveData<List<ReportTypes>> reportTypesListLiveData,
                                MediatorLiveData<List<DeactivateTypes>> deactivateTypesListLiveData,
                                MediatorLiveData<List<Category>> categoriesListLiveData,
                                ApplicationRepository applicationRepository) {
        this.resultObjectLiveData = resultObjectLiveData;
        this.defaultCoverImageLiveData = defaultCoverImageLiveData;
        this.configDetailsLiveData = configDetailsLiveData;
        this.reportTypesListLiveData = reportTypesListLiveData;
        this.deactivateTypesListLiveData = deactivateTypesListLiveData;
        this.categoriesListLiveData = categoriesListLiveData;
        this.applicationRepository = applicationRepository;
    }

    public ApplicationViewModel() {
        this.resultObjectLiveData = new MediatorLiveData<>();
        this.defaultCoverImageLiveData = new MediatorLiveData<>();
        this.configDetailsLiveData = new MediatorLiveData<>();
        this.reportTypesListLiveData = new MediatorLiveData<>();
        this.deactivateTypesListLiveData = new MediatorLiveData<>();
        this.categoriesListLiveData = new MediatorLiveData<>();
        this.applicationRepository = new ApplicationRepositoryImpl();
    }

    //region Getters
    public MediatorLiveData<ResultObject> getResultObjectLiveData() {
        return resultObjectLiveData;
    }

    public MediatorLiveData<DefaultCoverImageResponse> getDefaultCoverImageLiveData() {
        return defaultCoverImageLiveData;
    }

    public MediatorLiveData<ConfigDetails> getConfigDetailsLiveData() {
        return configDetailsLiveData;
    }

    public MediatorLiveData<List<ReportTypes>> getReportTypesListLiveData() {
        return reportTypesListLiveData;
    }

    public MediatorLiveData<List<DeactivateTypes>> getDeactivateTypesListLiveData() {
        return deactivateTypesListLiveData;
    }

    public MediatorLiveData<List<Category>> getCategoriesListLiveData() {
        return categoriesListLiveData;
    }
    //endregion

    //region API Calls
    public void sendPushNotificationToAll(PushNotificationBody pushNotificationBody){
        resultObjectLiveData.addSource(
                applicationRepository.sendPushNotificationToAll(pushNotificationBody),
                new Observer<ResultObject>() {
                    @Override
                    public void onChanged(@Nullable ResultObject resultObject) {
                        resultObjectLiveData.setValue(resultObject);
                    }
                }
        );
    }

    public void getDefaultCoverImages(int page){
        defaultCoverImageLiveData.addSource(
                applicationRepository.getDefaultCoverImages(page),
                new Observer<DefaultCoverImageResponse>() {
                    @Override
                    public void onChanged(@Nullable DefaultCoverImageResponse defaultCoverImageResponse) {
                        defaultCoverImageLiveData.setValue(defaultCoverImageResponse);
                    }
                }
        );
    }

//    public void addDefaultCoverMedia(DefaultCoverMedia defaultCoverMedia){}

    public void getUpdateAndConfigDetails(ConfigBody configBody){
        configDetailsLiveData.addSource(
                applicationRepository.getUpdateAndConfigDetails(configBody),
                new Observer<ConfigDetails>() {
                    @Override
                    public void onChanged(@Nullable ConfigDetails configDetails) {
                        configDetailsLiveData.setValue(configDetails);
                    }
                }
        );
    }

    public void getPostReportTypes(){
        reportTypesListLiveData.addSource(
                applicationRepository.getPostReportTypes(),
                new Observer<List<ReportTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<ReportTypes> reportTypes) {
                        reportTypesListLiveData.setValue(reportTypes);
                    }
                }
        );
    }

    public void getReactionReportTypes(){
        reportTypesListLiveData.addSource(
                applicationRepository.getReactionReportTypes(),
                new Observer<List<ReportTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<ReportTypes> reportTypes) {
                        reportTypesListLiveData.setValue(reportTypes);
                    }
                }
        );
    }

    public void getProfileReportTypes(){
        reportTypesListLiveData.addSource(
                applicationRepository.getReactionReportTypes(),
                new Observer<List<ReportTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<ReportTypes> reportTypes) {
                        reportTypesListLiveData.setValue(reportTypes);
                    }
                }
        );
    }

    public void getDeactivationTypesList(){
        deactivateTypesListLiveData.addSource(
                applicationRepository.getDeactivationTypesList(),
                new Observer<List<DeactivateTypes>>() {
                    @Override
                    public void onChanged(@Nullable List<DeactivateTypes> deactivateTypes) {
                        deactivateTypesListLiveData.setValue(deactivateTypes);
                    }
                }
        );
    }

    public void getCategories(){
        categoriesListLiveData.addSource(
                applicationRepository.getCategories(),
                new Observer<List<Category>>() {
                    @Override
                    public void onChanged(@Nullable List<Category> categories) {
                        categoriesListLiveData.setValue(categories);
                    }
                }
        );
    }
    //endregion
}
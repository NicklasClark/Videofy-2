package com.cncoding.teazer.data.remote.apicalls.application;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.data.model.application.ConfigBody;
import com.cncoding.teazer.data.model.application.ConfigDetails;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.PushNotificationBody;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.remote.ResultObject;

import java.util.List;

/**
 *
 * Created by Prem$ on 1/23/2018.
 */

public interface ApplicationRepository {

    LiveData<ResultObject> sendPushNotificationToAll(PushNotificationBody pushNotificationBody);

    LiveData<DefaultCoverImageResponse> getDefaultCoverImages(int page);

//    LiveData<ResultObject> addDefaultCoverMedia(DefaultCoverMedia defaultCoverMedia);

    LiveData<ConfigDetails> getUpdateAndConfigDetails(ConfigBody configBody);

    LiveData<List<ReportTypes>> getPostReportTypes();

    LiveData<List<ReportTypes>> getReactionReportTypes();

    LiveData<List<ReportTypes>> getProfileReportTypes();

    LiveData<List<DeactivateTypes>> getDeactivationTypesList();

    LiveData<List<Category>> getCategories();
}
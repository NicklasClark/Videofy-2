package com.cncoding.teazer.data.remote.apicalls.application;

import com.cncoding.teazer.data.model.application.ConfigBody;
import com.cncoding.teazer.data.model.application.ConfigDetails;
import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.PushNotificationBody;
import com.cncoding.teazer.data.model.application.ReportTypes;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;
import com.cncoding.teazer.data.remote.ResultObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Prem$ on 2/5/2018.
 **/

public interface ApplicationService {

    /**
     * Call this service to send a push notification all Teazer Users
     * */
    @POST("/api/v1/application/publish/notifications")
    Call<ResultObject> sendPushNotificationToAll(PushNotificationBody pushNotificationBody);

    /**
     * Call this service to get the list of default cover images
     * */
    @GET("/api/v1/application/default/cover/medias/{page}")
    Call<DefaultCoverImageResponse> getDefaultCoverImages(@Path("page") int page);

//    /**
//     * Call this service to add a default profile cover media
//     * */
//    @POST("/api/v1/application/add/default/cover/media")
//    Call<ResultObject> addDefaultCoverMedia(DefaultCoverMedia defaultCoverMedia);

    /**
     * Call this service to get update and config details
     * */
    @POST("/api/v1/application/config")
    Call<ConfigDetails> getUpdateAndConfigDetails(ConfigBody configBody);

    /**
     * Call this service to get the post report types
     * */
    @GET("/api/v1/application/post/report/types")
    Call<List<ReportTypes>> getPostReportTypes();

    /**
     * Call this service to get the reaction report types
     * */
    @GET("/api/v1/application/post/report/types")
    Call<List<ReportTypes>> getReactionReportTypes();

    /**
     * Call this service to get the profile report types
     * */
    @GET("/api/v1/application/profile/report/types")
    Call<List<ReportTypes>> getProfileReportTypes();

    /**
     * Call this service to get the deactivate types
     * */
    @GET("/api/v1/application/deactivate/types")
    Call<List<DeactivateTypes>> getDeactivationTypesList();

    /**
     * Call this service to get the categories list
     * */
    @GET("/api/v1/application/categories")
    Call<List<Category>> getCategories();
}
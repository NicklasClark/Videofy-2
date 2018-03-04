package com.cncoding.teazer.data.remote.apicalls.application;

import com.cncoding.teazer.data.model.application.DeactivateTypes;
import com.cncoding.teazer.data.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.profile.DefaultCoverImageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Prem$ on 2/5/2018.
 **/

public interface ApplicationService {

    /**
     * To get the post report types
     * */
    @GET("/api/v1/application/post/report/types")
    Call<List<ReportPostTitlesResponse>> getPostReportTypes();

    /**
     * To get the profile report types
     * */
    @GET("/api/v1/application/profile/report/types")
    Call<List<ReportPostTitlesResponse>> getProfileReportTypes();

    @GET("/api/v1/application/deactivate/types")
    Call<List<DeactivateTypes>> getDeactivationTypesList();

    /**
     * To get the categories list
     * */
    @GET("/api/v1/application/categories")
    Call<List<Category>> getCategories();

    // New Api for getting cover Images

    @GET("/api/v1/application/default/cover/medias/{page}")
    Call<DefaultCoverImageResponse> getDefaultcoverImages(@Path("page") int page);

}
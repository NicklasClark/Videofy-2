package com.cncoding.teazer.model.profile.reportPost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 24/11/17.
 */

public class ReportPostRequest {
    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("report_type_id")
    @Expose
    private Integer reportTypeId;
    @SerializedName("other_reason")
    @Expose
    private String reportRemark;

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(Integer reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getReportRemark() {
        return reportRemark;
    }

    public void setReportRemark(String reportRemark) {
        this.reportRemark = reportRemark;
    }

    public ReportPostRequest(Integer postId, Integer reportTypeId, String reportRemark) {
        this.postId = postId;
        this.reportTypeId = reportTypeId;
        this.reportRemark = reportRemark;
    }
}

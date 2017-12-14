package com.cncoding.teazer.model.post;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */

public class ReportPost {
    private int post_id;
    private int report_type_id;

    public ReportPost(int post_id, int report_type_id) {
        this.post_id = post_id;
        this.report_type_id = report_type_id;
    }

    public int getPostId() {
        return post_id;
    }

    public int getReportTypeId() {
        return report_type_id;
    }
}
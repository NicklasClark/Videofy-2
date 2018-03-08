
package com.cncoding.teazer.data.model.giphy;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendingGiphy extends BaseModel {

    @SerializedName("data") @Expose private List<Datum> data = null;
    @SerializedName("pagination") @Expose private Pagination pagination;
    @SerializedName("meta") @Expose private Meta meta;

    public TrendingGiphy(Throwable error) {
        this.error = error;
    }

    public TrendingGiphy setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

}

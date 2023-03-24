package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateActivityCategoryModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("lookupType")
    @Expose
    private String lookupType;

    @SerializedName("lookupName")
    @Expose
    private String lookupName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupName() {
        return lookupName;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

}

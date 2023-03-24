package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuModelMeal {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("EmpId")
    @Expose
    private String empid;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("lookupsId")
    @Expose
    private String lookupId;

    @SerializedName("lookupName")
    @Expose
    private String lookupName;

    @SerializedName("lookupType")
    @Expose
    private String lookupType;

    @SerializedName("lookupCategory")
    @Expose
    private String lookupCategory;

    public String getEmpid() {
        return empid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getLookupId() {
        return lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getLookupName() {
        return lookupName;
    }

    public void setLookupName(String lookupName) {
        this.lookupName = lookupName;
    }

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupCategory() {
        return lookupCategory;
    }

    public void setLookupCategory(String lookupCategory) {
        this.lookupCategory = lookupCategory;
    }
}

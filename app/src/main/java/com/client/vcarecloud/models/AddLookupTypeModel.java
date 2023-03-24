package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddLookupTypeModel {
    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("lookupsId")
    @Expose
    private String lookupsId;

    @SerializedName("lookupType")
    @Expose
    private String lookupType;

    @SerializedName("lookupName")
    @Expose
    private String lookupName;

    @SerializedName("EmpId")
    @Expose
    private String empId;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getLookupsId() {
        return lookupsId;
    }

    public void setLookupsId(String lookupsId) {
        this.lookupsId = lookupsId;
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

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}

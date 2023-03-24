package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LookupTypeModel implements Serializable {
    String empid,lookupsId,custId,lookupType,lookupName,lookupCategory;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getLookupsId() {
        return lookupsId;
    }

    public void setLookupsId(String lookupsId) {
        this.lookupsId = lookupsId;
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

    public String getLookupCategory() {
        return lookupCategory;
    }

    public void setLookupCategory(String lookupCategory) {
        this.lookupCategory = lookupCategory;
    }
}

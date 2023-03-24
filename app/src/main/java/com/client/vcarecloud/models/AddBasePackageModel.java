package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddBasePackageModel {

    @SerializedName("empid")
    @Expose
    private String empid;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("packageName")
    @Expose
    private String packageName;

    @SerializedName("packageDescription")
    @Expose
    private String description;

    @SerializedName("packageAmount")
    @Expose
    private String amount;

    @SerializedName("taxesId")
    @Expose
    private String taxid;

    @SerializedName("classId")
    @Expose
    private String classId;

    @SerializedName("packageStatus")
    @Expose
    private String status;

    public String getEmpid() {
        return empid;
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

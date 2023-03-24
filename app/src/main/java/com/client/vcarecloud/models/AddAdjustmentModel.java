package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAdjustmentModel {

    @SerializedName("empId")
    @Expose
    private String empId;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("adjustType")
    @Expose
    private String adjustType;

    @SerializedName("adjustDescription")
    @Expose
    private String description;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("adjustAmount")
    @Expose
    private String adjustAmount;

    @SerializedName("adjustDate")
    @Expose
    private String date;

    @SerializedName("applicableType")
    @Expose
    private String applicableType;

    @SerializedName("refApplicableID")
    @Expose
    private String applicableID;

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getAdjustType() {
        return adjustType;
    }

    public void setAdjustType(String adjustType) {
        this.adjustType = adjustType;
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

    public String getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(String adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApplicableType() {
        return applicableType;
    }

    public void setApplicableType(String applicableType) {
        this.applicableType = applicableType;
    }

    public String getApplicableID() {
        return applicableID;
    }

    public void setApplicableID(String applicableID) {
        this.applicableID = applicableID;
    }
}

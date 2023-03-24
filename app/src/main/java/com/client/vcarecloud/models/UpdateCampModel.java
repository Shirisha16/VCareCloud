package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCampModel {
    @SerializedName("campId")
    @Expose
    private  String id;

    @SerializedName("custId")
    @Expose
    private  String custId;

    @SerializedName("campName")
    @Expose
    private  String campName;

    @SerializedName("campDetails")
    @Expose
    private  String campDetails;

    @SerializedName("className")
    @Expose
    private  String className;

    @SerializedName("campStartDate")
    @Expose
    private  String startDate;

    @SerializedName("campEndDate")
    @Expose
    private  String endDate;

    @SerializedName("classId")
    @Expose
    private  String classId;

    @SerializedName("campCharge")
    @Expose
    private  String campCharge;

    @SerializedName("taxesId")
    @Expose
    private  String taxesId;

    @SerializedName("taxName")
    @Expose
    private  String taxname;

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

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getCampDetails() {
        return campDetails;
    }

    public void setCampDetails(String campDetails) {
        this.campDetails = campDetails;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCampCharge() {
        return campCharge;
    }

    public void setCampCharge(String campCharge) {
        this.campCharge = campCharge;
    }

    public String getTaxesId() {
        return taxesId;
    }

    public void setTaxesId(String taxesId) {
        this.taxesId = taxesId;
    }

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }
}

package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCampModel {

    @SerializedName("empID")
    @Expose
    private String empID;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("campName")
    @Expose
    private String campName;

    @SerializedName("campDetails")
    @Expose
    private String campDetails;

    @SerializedName("className")
    @Expose
    private String className;

    @SerializedName("campStartDate")
    @Expose
    private String campStartDate;

    @SerializedName("campEndDate")
    @Expose
    private String campEndDate;

    @SerializedName("classId")
    @Expose
    private String classId;

    @SerializedName("campCharge")
    @Expose
    private String campCharge;

    @SerializedName("taxesId")
    @Expose
    private String taxesId;

    @SerializedName("taxName")
    @Expose
    private String taxName;

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
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

    public String getCampStartDate() {
        return campStartDate;
    }

    public void setCampStartDate(String campStartDate) {
        this.campStartDate = campStartDate;
    }

    public String getCampEndDate() {
        return campEndDate;
    }

    public void setCampEndDate(String campEndDate) {
        this.campEndDate = campEndDate;
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

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }
}


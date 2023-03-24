package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;

public class AdditionalChargesModel {

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("chargeName")
    @Expose
    private String chargeName;

    @SerializedName("ChargeDescription")
    @Expose
    private String description;

    @SerializedName("chargeAmount")
    @Expose
    private String amount;

    @SerializedName("taxesId")
    @Expose
    private String taxId;

    @SerializedName("chargeDate")
    @Expose
    private String date;

    @SerializedName("applicableType")
    @Expose
    private String applicableType;

    @SerializedName("Applicableonclass")
    @Expose
    private String classid;

    @SerializedName("Applicableonchild")
    @Expose
    private String childId;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
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

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
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

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

}

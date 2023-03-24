package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateAdditionalChargesModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("refApplicableID")
    @Expose
    private String refApplicableID;

    @SerializedName("EmpId")
    @Expose
    private String empid;

    @SerializedName("AdditionalChargeId")
    @Expose
    private String chargeId;

    @SerializedName("CustId")
    @Expose
    private String custId;

    @SerializedName("ChargeName")
    @Expose
    private String chargeName;

    @SerializedName("ChargeDescription")
    @Expose
    private String description;

    @SerializedName("ChargeAmount")
    @Expose
    private String amount;

    @SerializedName("TaxesId")
    @Expose
    private String taxId;

    @SerializedName("taxName")
    @Expose
    private String taxName;

    @SerializedName("chargeDate")
    @Expose
    private String date;

    @SerializedName("ApplicableType")
    @Expose
    private String applicableType;

    @SerializedName("Applicableonclass")
    @Expose
    private String classid;

    @SerializedName("className")
    @Expose
    private String classname;

    @SerializedName("Applicableonchild")
    @Expose
    private String childId;

    @SerializedName("childName")
    @Expose
    private String childName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefApplicableID() {
        return refApplicableID;
    }

    public void setRefApplicableID(String refApplicableID) {
        this.refApplicableID = refApplicableID;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

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

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}

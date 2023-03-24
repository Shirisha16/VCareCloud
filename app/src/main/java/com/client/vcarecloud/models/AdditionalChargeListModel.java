package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdditionalChargeListModel implements Serializable {
    String ChargeId,custId,date,chargename,amount,refApplicableID,className,childName,
            applicableType,description,taxesId,taxname,classid,childid,empid;

    public String getChargeId() {
        return ChargeId;
    }

    public void setChargeId(String chargeId) {
        ChargeId = chargeId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChargename() {
        return chargename;
    }

    public void setChargename(String chargename) {
        this.chargename = chargename;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRefApplicableID() {
        return refApplicableID;
    }

    public void setRefApplicableID(String refApplicableID) {
        this.refApplicableID = refApplicableID;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getApplicableType() {
        return applicableType;
    }

    public void setApplicableType(String applicableType) {
        this.applicableType = applicableType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getChildid() {
        return childid;
    }

    public void setChildid(String childid) {
        this.childid = childid;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

}

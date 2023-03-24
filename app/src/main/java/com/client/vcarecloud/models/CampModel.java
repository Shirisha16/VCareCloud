package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CampModel implements Serializable {

    String campId,custId,campName,campDetails,campStartDate,campEndDate,className,classId,
            campCharge, taxesId,taxname,createdOn, lastChangedOn,status;

    String message,error;

    public String getCampId() {
        return campId;
    }

    public void setCampId(String campId) {
        this.campId = campId;
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

    public String getCampStartDate() {
        return campStartDate;
    }

    public String getCampDetails() {
        return campDetails;
    }

    public void setCampDetails(String campDetails) {
        this.campDetails = campDetails;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastChangedOn() {
        return lastChangedOn;
    }

    public void setLastChangedOn(String lastChangedOn) {
        this.lastChangedOn = lastChangedOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

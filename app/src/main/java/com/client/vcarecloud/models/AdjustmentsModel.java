package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AdjustmentsModel implements Serializable {
    String adjustmentId, custId, adjustType, description, refApplicableID, amount, adjustAmount,
            adjustDate, applicableType, child;

    public String getAdjustmentId() {
        return adjustmentId;
    }

    public void setAdjustmentId(String adjustmentId) {
        this.adjustmentId = adjustmentId;
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

    public String getRefApplicableID() {
        return refApplicableID;
    }

    public void setRefApplicableID(String refApplicableID) {
        this.refApplicableID = refApplicableID;
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

    public String getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(String adjustDate) {
        this.adjustDate = adjustDate;
    }

    public String getApplicableType() {
        return applicableType;
    }

    public void setApplicableType(String applicableType) {
        this.applicableType = applicableType;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }
}

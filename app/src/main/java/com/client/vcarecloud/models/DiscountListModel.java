package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DiscountListModel implements Serializable {
    String discountId, custId, discountName, description, discountType, discountValue, status,
            checkLimitedPeriod, fromDate, toDate, basePackage, additionalCharge,camp,
            applicableActivity, applicableAll,empId;

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCheckLimitedPeriod() {
        return checkLimitedPeriod;
    }

    public void setCheckLimitedPeriod(String checkLimitedPeriod) {
        this.checkLimitedPeriod = checkLimitedPeriod;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getAdditionalCharge() {
        return additionalCharge;
    }

    public void setAdditionalCharge(String additionalCharge) {
        this.additionalCharge = additionalCharge;
    }

    public String getCamp() {
        return camp;
    }

    public void setCamp(String camp) {
        this.camp = camp;
    }

    public String getApplicableActivity() {
        return applicableActivity;
    }

    public void setApplicableActivity(String applicableActivity) {
        this.applicableActivity = applicableActivity;
    }

    public String getApplicableAll() {
        return applicableAll;
    }

    public void setApplicableAll(String applicableAll) {
        this.applicableAll = applicableAll;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}

package com.client.vcarecloud.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddDiscountModel {

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("discountName")
    @Expose
    private String discountName;

    @SerializedName("discountDescription")
    @Expose
    private String description;

    @SerializedName("discountType")
    @Expose
    private String discountType;

    @SerializedName("discountValue")
    @Expose
    private String value;

    @SerializedName("discountStatus")
    @Expose
    private String status;

    @SerializedName("checkLimitedPeriod")
    @Expose
    private String limited;

    @SerializedName("limitedPeriodFromDate")
    @Expose
    private String from;

    @SerializedName("limitedPeriodToDate")
    @Expose
    private String to;

    @SerializedName("applicable_BasePackage")
    @Expose
    private String basePackage;

    @SerializedName("applicable_AdditionalCharge")
    @Expose
    private String additionalCharge;

    @SerializedName("applicable_Camp")
    @Expose
    private String camp;

    @SerializedName("applicable_Activity")
    @Expose
    private String activity;

    @SerializedName("applicable_All")
    @Expose
    private String all;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLimited() {
        return limited;
    }

    public void setLimited(String limited) {
        this.limited = limited;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public String getCamp() { return camp; }

    public void setCamp(String camp) {
        this.camp = camp;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }
}

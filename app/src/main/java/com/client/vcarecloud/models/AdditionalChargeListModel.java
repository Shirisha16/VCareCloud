package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdditionalChargeListModel {

    @SerializedName("model")
    @Expose
    private List<Model> model;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("didError")
    @Expose
    private Boolean didError;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;

    public List<Model> getModel() {
        return model;
    }

    public void setModel(List<Model> model) {
        this.model = model;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDidError() {
        return didError;
    }

    public void setDidError(Boolean didError) {
        this.didError = didError;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class Model {

        @SerializedName("additionalChargeId")
        @Expose
        private Integer additionalChargeId;
        @SerializedName("custId")
        @Expose
        private Integer custId;
        @SerializedName("chargeDate")
        @Expose
        private String chargeDate;
        @SerializedName("chargeName")
        @Expose
        private String chargeName;
        @SerializedName("chargeAmount")
        @Expose
        private Double chargeAmount;
        @SerializedName("refApplicableID")
        @Expose
        private Integer refApplicableID;
        @SerializedName("className")
        @Expose
        private String className;
        @SerializedName("childName")
        @Expose
        private String childName;
        @SerializedName("applicableType")
        @Expose
        private String applicableType;
        @SerializedName("chargeDescription")
        @Expose
        private String chargeDescription;
        @SerializedName("taxesId")
        @Expose
        private Integer taxesId;
        @SerializedName("taxName")
        @Expose
        private String taxName;

        public Integer getAdditionalChargeId() {
            return additionalChargeId;
        }

        public void setAdditionalChargeId(Integer additionalChargeId) {
            this.additionalChargeId = additionalChargeId;
        }

        public Integer getCustId() {
            return custId;
        }

        public void setCustId(Integer custId) {
            this.custId = custId;
        }

        public String getChargeDate() {
            return chargeDate;
        }

        public void setChargeDate(String chargeDate) {
            this.chargeDate = chargeDate;
        }

        public String getChargeName() {
            return chargeName;
        }

        public void setChargeName(String chargeName) {
            this.chargeName = chargeName;
        }

        public Double getChargeAmount() {
            return chargeAmount;
        }

        public void setChargeAmount(Double chargeAmount) {
            this.chargeAmount = chargeAmount;
        }

        public Integer getRefApplicableID() {
            return refApplicableID;
        }

        public void setRefApplicableID(Integer refApplicableID) {
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

        public String getChargeDescription() {
            return chargeDescription;
        }

        public void setChargeDescription(String chargeDescription) {
            this.chargeDescription = chargeDescription;
        }

        public Integer getTaxesId() {
            return taxesId;
        }

        public void setTaxesId(Integer taxesId) {
            this.taxesId = taxesId;
        }

        public String getTaxName() {
            return taxName;
        }

        public void setTaxName(String taxName) {
            this.taxName = taxName;
        }
    }
}

package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BasePackagesModel {
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

        @SerializedName("packageId")
        @Expose
        private Integer packageId;
        @SerializedName("custId")
        @Expose
        private Integer custId;
        @SerializedName("packageName")
        @Expose
        private String packageName;
        @SerializedName("packageDescription")
        @Expose
        private String packageDescription;
        @SerializedName("packageAmount")
        @Expose
        private Double packageAmount;
        @SerializedName("tax")
        @Expose
        private String tax;
        @SerializedName("taxid")
        @Expose
        private Integer taxid;
        @SerializedName("classes")
        @Expose
        private String classes;
        @SerializedName("packageStatus")
        @Expose
        private String packageStatus;
        @SerializedName("classId")
        @Expose
        private Integer classId;

        public Integer getPackageId() {
            return packageId;
        }

        public void setPackageId(Integer packageId) {
            this.packageId = packageId;
        }

        public Integer getCustId() {
            return custId;
        }

        public void setCustId(Integer custId) {
            this.custId = custId;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getPackageDescription() {
            return packageDescription;
        }

        public void setPackageDescription(String packageDescription) {
            this.packageDescription = packageDescription;
        }

        public Double getPackageAmount() {
            return packageAmount;
        }

        public void setPackageAmount(Double packageAmount) {
            this.packageAmount = packageAmount;
        }

        public String getTax() {
            return tax;
        }

        public void setTax(String tax) {
            this.tax = tax;
        }

        public Integer getTaxid() {
            return taxid;
        }

        public void setTaxid(Integer taxid) {
            this.taxid = taxid;
        }

        public String getClasses() {
            return classes;
        }

        public void setClasses(String classes) {
            this.classes = classes;
        }

        public String getPackageStatus() {
            return packageStatus;
        }

        public void setPackageStatus(String packageStatus) {
            this.packageStatus = packageStatus;
        }

        public Integer getClassId() {
            return classId;
        }

        public void setClassId(Integer classId) {
            this.classId = classId;
        }
    }
}

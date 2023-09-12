package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class AddTaxModel {

        @SerializedName("EmpId")
        @Expose
        private String empid;

//        @SerializedName("taxesId")
//        @Expose
//        private String taxId;

        @SerializedName("custId")
        @Expose
        private String custId;

        @SerializedName("taxName")
        @Expose
        private String taxName;

        @SerializedName("taxRate")
        @Expose
        private String taxRate;

        @SerializedName("taxStatus")
        @Expose
        private Boolean taxStatus;

        @SerializedName("createdBy")
        @Expose
        private String createdBy;

        @SerializedName("createdOn")
        @Expose
        private String createdOn;

        @SerializedName("modifiedBy")
        @Expose
        private String modifiedBy;

        @SerializedName("modifiedOn")
        @Expose
        private String modifiedOn;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("customer")
        @Expose
        private String customer;

        public String getEmpid() {
            return empid;
        }

        public void setEmpid(String empid) {
            this.empid = empid;
        }

//        public String getTaxId() {
//            return taxId;
//        }
//
//        public void setTaxId(String taxId) {
//            this.taxId = taxId;
//        }

        public String getCustId() {
            return custId;
        }

        public void setCustId(String custId) {
            this.custId = custId;
        }

        public String getTaxName() {
            return taxName;
        }

        public void setTaxName(String taxName) {
            this.taxName = taxName;
        }

        public String getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(String taxRate) {
            this.taxRate = taxRate;
        }

        public Boolean getTaxStatus() {
            return taxStatus;
        }

        public void setTaxStatus(Boolean taxStatus) {
            this.taxStatus = taxStatus;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }
}

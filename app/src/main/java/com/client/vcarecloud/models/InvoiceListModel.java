package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InvoiceListModel {
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

        @SerializedName("headerId")
        @Expose
        private Integer headerId;
        @SerializedName("custId")
        @Expose
        private Integer custId;
        @SerializedName("childId")
        @Expose
        private Integer childId;
        @SerializedName("classname")
        @Expose
        private String classname;
        @SerializedName("invoiceNo")
        @Expose
        private String invoiceNo;
        @SerializedName("invoiceMonth")
        @Expose
        private Integer invoiceMonth;
        @SerializedName("invoiceYear")
        @Expose
        private Integer invoiceYear;
        @SerializedName("invoicePeriod_From")
        @Expose
        private String invoicePeriodFrom;
        @SerializedName("invoicePeriod_To")
        @Expose
        private String invoicePeriodTo;
        @SerializedName("invoiceDate")
        @Expose
        private String invoiceDate;
        @SerializedName("invoiceDueDate")
        @Expose
        private String invoiceDueDate;
        @SerializedName("paymentDate")
        @Expose
        private String paymentDate;
        @SerializedName("childname")
        @Expose
        private String childname;
        @SerializedName("totalChargeAmount")
        @Expose
        private Double totalChargeAmount;
        @SerializedName("totalDiscountAmount")
        @Expose
        private Double totalDiscountAmount;
        @SerializedName("totalTaxAmount")
        @Expose
        private Double totalTaxAmount;
        @SerializedName("totalAdjustmentAmount")
        @Expose
        private Double totalAdjustmentAmount;
        @SerializedName("invoiceAmount")
        @Expose
        private Double invoiceAmount;
        @SerializedName("payAmount")
        @Expose
        private Double payAmount;
        @SerializedName("latePayment")
        @Expose
        private Double latePayment;

        public Integer getHeaderId() {
            return headerId;
        }

        public void setHeaderId(Integer headerId) {
            this.headerId = headerId;
        }

        public Integer getCustId() {
            return custId;
        }

        public void setCustId(Integer custId) {
            this.custId = custId;
        }

        public Integer getChildId() {
            return childId;
        }

        public void setChildId(Integer childId) {
            this.childId = childId;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public Integer getInvoiceMonth() {
            return invoiceMonth;
        }

        public void setInvoiceMonth(Integer invoiceMonth) {
            this.invoiceMonth = invoiceMonth;
        }

        public Integer getInvoiceYear() {
            return invoiceYear;
        }

        public void setInvoiceYear(Integer invoiceYear) {
            this.invoiceYear = invoiceYear;
        }

        public String getInvoicePeriodFrom() {
            return invoicePeriodFrom;
        }

        public void setInvoicePeriodFrom(String invoicePeriodFrom) {
            this.invoicePeriodFrom = invoicePeriodFrom;
        }

        public String getInvoicePeriodTo() {
            return invoicePeriodTo;
        }

        public void setInvoicePeriodTo(String invoicePeriodTo) {
            this.invoicePeriodTo = invoicePeriodTo;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
            this.invoiceDate = invoiceDate;
        }

        public String getInvoiceDueDate() {
            return invoiceDueDate;
        }

        public void setInvoiceDueDate(String invoiceDueDate) {
            this.invoiceDueDate = invoiceDueDate;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public String getChildname() {
            return childname;
        }

        public void setChildname(String childname) {
            this.childname = childname;
        }

        public Double getTotalChargeAmount() {
            return totalChargeAmount;
        }

        public void setTotalChargeAmount(Double totalChargeAmount) {
            this.totalChargeAmount = totalChargeAmount;
        }

        public Double getTotalDiscountAmount() {
            return totalDiscountAmount;
        }

        public void setTotalDiscountAmount(Double totalDiscountAmount) {
            this.totalDiscountAmount = totalDiscountAmount;
        }

        public Double getTotalTaxAmount() {
            return totalTaxAmount;
        }

        public void setTotalTaxAmount(Double totalTaxAmount) {
            this.totalTaxAmount = totalTaxAmount;
        }

        public Double getTotalAdjustmentAmount() {
            return totalAdjustmentAmount;
        }

        public void setTotalAdjustmentAmount(Double totalAdjustmentAmount) {
            this.totalAdjustmentAmount = totalAdjustmentAmount;
        }

        public Double getInvoiceAmount() {
            return invoiceAmount;
        }

        public void setInvoiceAmount(Double invoiceAmount) {
            this.invoiceAmount = invoiceAmount;
        }

        public Double getPayAmount() {
            return payAmount;
        }

        public void setPayAmount(Double payAmount) {
            this.payAmount = payAmount;
        }

        public Double getLatePayment() {
            return latePayment;
        }

        public void setLatePayment(Double latePayment) {
            this.latePayment = latePayment;
        }

    }
}

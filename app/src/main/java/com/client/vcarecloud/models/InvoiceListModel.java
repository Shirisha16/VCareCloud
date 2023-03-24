package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InvoiceListModel implements Serializable {
    String headerId,custId,classId,invoiceNo,invoiceMonth,invoiceYear,invoicePeriod_From,
           invoicePeriod_To,invoiceDate,invoiceDueDate,paymentDate,childId,totalChargeAmount,
           totalDiscountAmount,totalTaxAmount,totalAdjustmentAmount,invoiceAmount,payAmount,
           latePayment;

    String className,childName;

    String message,error;

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceMonth() {
        return invoiceMonth;
    }

    public void setInvoiceMonth(String invoiceMonth) {
        this.invoiceMonth = invoiceMonth;
    }

    public String getInvoiceYear() {
        return invoiceYear;
    }

    public void setInvoiceYear(String invoiceYear) {
        this.invoiceYear = invoiceYear;
    }

    public String getInvoicePeriod_From() {
        return invoicePeriod_From;
    }

    public void setInvoicePeriod_From(String invoicePeriod_From) {
        this.invoicePeriod_From = invoicePeriod_From;
    }

    public String getInvoicePeriod_To() {
        return invoicePeriod_To;
    }

    public void setInvoicePeriod_To(String invoicePeriod_To) {
        this.invoicePeriod_To = invoicePeriod_To;
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

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getTotalChargeAmount() {
        return totalChargeAmount;
    }

    public void setTotalChargeAmount(String totalChargeAmount) {
        this.totalChargeAmount = totalChargeAmount;
    }

    public String getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(String totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public String getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(String totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public String getTotalAdjustmentAmount() {
        return totalAdjustmentAmount;
    }

    public void setTotalAdjustmentAmount(String totalAdjustmentAmount) {
        this.totalAdjustmentAmount = totalAdjustmentAmount;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getLatePayment() {
        return latePayment;
    }

    public void setLatePayment(String latePayment) {
        this.latePayment = latePayment;
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

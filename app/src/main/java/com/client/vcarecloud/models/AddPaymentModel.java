package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPaymentModel {
    @SerializedName("paymentId")
    @Expose
    private String paymentId;

    @SerializedName("EmpId")
    @Expose
    private String EmpId;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("paymentType")
    @Expose
    private String paymentType;

    @SerializedName("paymentDate")
    @Expose
    private String date;

    @SerializedName("childId")
    @Expose
    private String childId;

    @SerializedName("InvoiceId")
    @Expose
    private String InvoiceId;

    @SerializedName("RefInvoiceNo")
    @Expose
    private String invoiceNo;

    @SerializedName("paymentAmount")
    @Expose
    private String amount;

    @SerializedName("displayName")
    @Expose
    private String childName;

    @SerializedName("paymentNotes")
    @Expose
    private String note;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        InvoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

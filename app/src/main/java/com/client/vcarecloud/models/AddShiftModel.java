package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddShiftModel {

    @SerializedName("EmpId")
    @Expose
    private String EmpId;

    @SerializedName("ShiftId")
    @Expose
    private String shiftId;

    @SerializedName("CustId")
    @Expose
    private Integer custId;

    @SerializedName("ShiftName")
    @Expose
    private String ShiftName;

    @SerializedName("StartTime")
    @Expose
    private String startTime;

    @SerializedName("EndTime")
    @Expose
    private String EndTime;

    @SerializedName("ShiftDuration")
    @Expose
    private String shiftDuration;

    @SerializedName("ShiftStatus")
    @Expose
    private String shiftStatus;

    @SerializedName("CreatedOn")
    @Expose
    private String createdOn;

    @SerializedName("CreatedBy")
    @Expose
    private String createdBy;

    @SerializedName("LastChangedOn")
    @Expose
    private String lastChangedOn;

    @SerializedName("LastChangedBy")
    @Expose
    private String lastChangedBy;

    @SerializedName("Status")
    @Expose
    private String status;

    @SerializedName("Customer")
    @Expose
    private String customer;

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public String getShiftName() {
        return ShiftName;
    }

    public void setShiftName(String shiftName) {
        ShiftName = shiftName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getShiftDuration() {
        return shiftDuration;
    }

    public void setShiftDuration(String shiftDuration) {
        this.shiftDuration = shiftDuration;
    }

    public String getShiftStatus() {
        return shiftStatus;
    }

    public void setShiftStatus(String shiftStatus) {
        this.shiftStatus = shiftStatus;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastChangedOn() {
        return lastChangedOn;
    }

    public void setLastChangedOn(String lastChangedOn) {
        this.lastChangedOn = lastChangedOn;
    }

    public String getLastChangedBy() {
        return lastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
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

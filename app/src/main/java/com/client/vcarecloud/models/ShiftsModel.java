package com.client.vcarecloud.models;

public class ShiftsModel {
    String shift_id, cust_id, shift, timeStart, endTime, shift_duration,
            shift_status, created_by, created_on, last_changedBy,
            last_changedOn, status, customer, id, empId;

    public String getShift_id() {
        return shift_id;
    }

    public void setShift_id(String shift_id) {
        this.shift_id = shift_id;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getShift_duration() {
        return shift_duration;
    }

    public void setShift_duration(String shift_duration) {
        this.shift_duration = shift_duration;
    }

    public String getShift_status() {
        return shift_status;
    }

    public void setShift_status(String shift_status) {
        this.shift_status = shift_status;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getLast_changedBy() {
        return last_changedBy;
    }

    public void setLast_changedBy(String last_changedBy) {
        this.last_changedBy = last_changedBy;
    }

    public String getLast_changedOn() {
        return last_changedOn;
    }

    public void setLast_changedOn(String last_changedOn) {
        this.last_changedOn = last_changedOn;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}

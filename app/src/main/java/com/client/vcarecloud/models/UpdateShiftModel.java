package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateShiftModel {
    @SerializedName("id")
    @Expose
    private  String id;

    @SerializedName("EmpId")
    @Expose
    private  String empid;

    @SerializedName("shiftId")
    @Expose
    private  String shift_id;

    @SerializedName("custId")
    @Expose
    private  String cust_id;

    @SerializedName("shiftName")
    @Expose
    private  String shift_name;

    @SerializedName("startTime")
    @Expose
    private  String start_time;

    @SerializedName("endTime")
    @Expose
    private  String end_time;

    @SerializedName("shiftDuration")
    @Expose
    private  String shift_duration;

    @SerializedName("ShiftStatus")
    @Expose
    private  String shift_status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

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

    public String getShift_name() {
        return shift_name;
    }

    public void setShift_name(String shift_name) {
        this.shift_name = shift_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    @Override
    public String toString() {
        return "UpdateShiftModel{" +
                "id=" + id +
                "EmpId=" + empid +
                "shiftId" + shift_id +
                "custId" + cust_id + '\'' +
                "shiftName" + shift_name + '\'' +
                "startTime" + start_time + '\'' +
                "endTime" + end_time + '\'' +
                "shiftDuration" + shift_duration + '\'' +
                "shiftStatus" + shift_status + '\'' +
                '}';
    }
}

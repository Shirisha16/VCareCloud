package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckInRequest {

    @SerializedName("ChildId")
    @Expose
    private String ChildId;

    @SerializedName("Name")
    @Expose
    private String Name;

    @SerializedName("dates")
    @Expose
    private String dates;

    @SerializedName("ChildAttendanceId")
    @Expose
    private String ChildAttendanceId;

    @SerializedName("Empid")
    @Expose
    private String Empid;

    @SerializedName("CustId")
    @Expose
    private String CustId;

    public String getChildId() {
        return ChildId;
    }

    public void setChildId(String childId) {
        ChildId = childId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getChildAttendanceId() {
        return ChildAttendanceId;
    }

    public void setChildAttendanceId(String childAttendanceId) {
        ChildAttendanceId = childAttendanceId;
    }

    public String getEmpid() {
        return Empid;
    }

    public void setEmpid(String empid) {
        Empid = empid;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }
}

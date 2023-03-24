package com.client.vcarecloud.models;

public class CheckInModel {
    String childId,name,dates,ChildAttendanceId,Empid,CustId,checkIn;

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }
}

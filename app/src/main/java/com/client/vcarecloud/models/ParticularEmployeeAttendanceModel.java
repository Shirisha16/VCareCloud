package com.client.vcarecloud.models;

public class ParticularEmployeeAttendanceModel {
    String employeeAttendanceId,employeeId,employeeName,custId,shiftID,shiftName,dayStart,
            dayEnd,shiftStartDate,shiftEndDate,createdBy,createdOn,lastChangedBy,
            lastChangedOn,empId;
//String shiftStartTime,shiftEndTime;
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmployeeAttendanceId() {
        return employeeAttendanceId;
    }

    public void setEmployeeAttendanceId(String employeeAttendanceId) {
        this.employeeAttendanceId = employeeAttendanceId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getShiftID() {
        return shiftID;
    }

    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }

    public String getDayStart() {
        return dayStart;
    }

    public void setDayStart(String dayStart) {
        this.dayStart = dayStart;
    }

    public String getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(String dayEnd) {
        this.dayEnd = dayEnd;
    }

    public String getShiftStartDate() {
        return shiftStartDate;
    }

    public void setShiftStartDate(String shiftStartDate) {
        this.shiftStartDate = shiftStartDate;
    }

    public String getShiftEndDate() {
        return shiftEndDate;
    }

    public void setShiftEndDate(String shiftEndDate) {
        this.shiftEndDate = shiftEndDate;
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

    public String getLastChangedBy() {
        return lastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
    }

    public String getLastChangedOn() {
        return lastChangedOn;
    }

    public void setLastChangedOn(String lastChangedOn) {
        this.lastChangedOn = lastChangedOn;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
}

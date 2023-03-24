package com.client.vcarecloud.models;

public class EmpAttendanceModel {
    String dayStart,dayEnd,shiftStartDate,shiftEndDate,employees,shifts;

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

    public String getEmployees() {
        return employees;
    }

    public void setEmployees(String employees) {
        this.employees = employees;
    }

    public String getShifts() {
        return shifts;
    }

    public void setShifts(String shifts) {
        this.shifts = shifts;
    }
}

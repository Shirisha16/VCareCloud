package com.client.vcarecloud.models;

public class EmpCheckInRequest {
    private String EmpId;
    private String EmployeeId;
    private String CustId;
    private String dates;

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }
    @Override
    public String toString() {
        return "EmpCheckInRequest{" +
                "EmpId=" + EmpId +
                "EmployeeId" + EmployeeId +
                "CustId" + CustId +
                ", dates='" + dates + '\'' +
                '}';
    }
}

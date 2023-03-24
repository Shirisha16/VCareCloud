package com.client.vcarecloud.models;

import com.client.vcarecloud.Login;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Login_model implements Serializable {
    String userID,empID,custId,firstName,lastName,email,userType,daycarename,
            securityid,islogged;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDaycarename() {
        return daycarename;
    }

    public void setDaycarename(String daycarename) {
        this.daycarename = daycarename;
    }

    public String getSecurityid() {
        return securityid;
    }

    public void setSecurityid(String securityid) {
        this.securityid = securityid;
    }
}

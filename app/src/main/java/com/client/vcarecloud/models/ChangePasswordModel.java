package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePasswordModel {
//    String oldPass,newPass,confirmPass,empId,custId,userId;

    private String Old;
    private String New;
    private String Confirm;
    private String Empid;
    private String custId;
    private String UserId;
    private String message;
    private String didError;
    private String errorMessage;

    public String getOld() {
        return Old;
    }

    public void setOld(String old) {
        Old = old;
    }

    public String getNew() {
        return New;
    }

    public void setNew(String aNew) {
        New = aNew;
    }

    public String getConfirm() {
        return Confirm;
    }

    public void setConfirm(String confirm) {
        Confirm = confirm;
    }

    public String getEmpid() {
        return Empid;
    }

    public void setEmpid(String empid) {
        Empid = empid;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDidError() {
        return didError;
    }

    public void setDidError(String didError) {
        this.didError = didError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ChangePassword{" +
                "Old=" + Old +
                "New" + New +
                "Confirm" + Confirm +
                ", Empid='" + Empid + '\'' +
                ", custId='" + custId + '\'' +
                ", UserId='" + UserId + '\'' +
                ", message='" + message + '\'' +
                ", didError='" + didError + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

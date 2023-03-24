package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateAbsentModel  {
    @SerializedName("id")
    @Expose
    String id;

    @SerializedName("EmpId")
    @Expose
    String EmpId;

    @SerializedName("ChildId")
    @Expose
    String ChildId;

    @SerializedName("ClassId")
    @Expose
    String ClassId;

    @SerializedName("CustId")
    @Expose
    String CustId;

    @SerializedName("AbsentFrom")
    @Expose
    String from;

    @SerializedName("AbsentTo")
    @Expose
    String to;

    @SerializedName("AbsentType")
    @Expose
    String AbsentType;

    @SerializedName("AbsentNotes")
    @Expose
    String AbsentNotes;

    @SerializedName("CreatedBy")
    @Expose
    String CreatedBy;

    @SerializedName("CreatedOn")
    @Expose
    String CreatedOn;

    @SerializedName("LastChangedBy")
    @Expose
    String LastChangedBy;

    @SerializedName("LastChangedOn")
    @Expose
    String LastChangedOn;

    @SerializedName("Status")
    @Expose
    String Status;

    @SerializedName("className")
    @Expose
    String className;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }

    public String getChildId() {
        return ChildId;
    }

    public void setChildId(String childId) {
        ChildId = childId;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getCustId() {
        return CustId;
    }

    public void setCustId(String custId) {
        CustId = custId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getAbsentType() {
        return AbsentType;
    }

    public void setAbsentType(String absentType) {
        AbsentType = absentType;
    }

    public String getAbsentNotes() {
        return AbsentNotes;
    }

    public void setAbsentNotes(String absentNotes) {
        AbsentNotes = absentNotes;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getLastChangedBy() {
        return LastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        LastChangedBy = lastChangedBy;
    }

    public String getLastChangedOn() {
        return LastChangedOn;
    }

    public void setLastChangedOn(String lastChangedOn) {
        LastChangedOn = lastChangedOn;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "UpdateAbsentModel{" +
                "id=" + id +
                "EmpId=" + EmpId +
                "ChildId" + ChildId +
                "ClassId" + ClassId +
                ", CustId='" + CustId + '\'' +
                ", AbsentFrom='" + from + '\'' +
                ", AbsentTo='" + to + '\'' +
                ", AbsentType='" + AbsentType + '\'' +
                ", AbsentNotes='" + AbsentNotes + '\'' +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", CreatedOn='" + CreatedOn + '\'' +
                ", LastChangedBy='" + LastChangedBy + '\'' +
                ", LastChangedOn='" + LastChangedOn + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }

}



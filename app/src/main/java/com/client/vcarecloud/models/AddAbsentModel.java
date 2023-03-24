package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddAbsentModel {
    @SerializedName("EmpId")
    @Expose
    private String EmpId;
    @SerializedName("ChildId")
    @Expose
    private String ChildId;
    @SerializedName("ClassId")
    @Expose
    private String ClassId;
    @SerializedName("CustId")
    @Expose
    private String CustId;
    @SerializedName("AbsentFrom")
    @Expose
    private String AbsentFrom;
    @SerializedName("AbsentTo")
    @Expose
    private String AbsentTo;
    @SerializedName("AbsentType")
    @Expose
    private String AbsentType;
    @SerializedName("AbsentNotes")
    @Expose
    private String AbsentNotes;
    @SerializedName("CreatedOn")
    @Expose
    private String CreatedOn;
    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;
    @SerializedName("LastChangedOn")
    @Expose
    private String lastChangedOn;
    @SerializedName("lastChangedBy")
    @Expose
    private String lastChangedBy;
    @SerializedName("Status")
    @Expose
    private String Status;
    @SerializedName("ClassName")
    @Expose
    private String className;
    @SerializedName("ChildName")
    @Expose
    private String childName;


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

    public String getAbsentFrom() {
        return AbsentFrom;
    }

    public void setAbsentFrom(String absentFrom) {
        AbsentFrom = absentFrom;
    }

    public String getAbsentTo() {
        return AbsentTo;
    }

    public void setAbsentTo(String absentTo) {
        AbsentTo = absentTo;
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


    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getLastChangedOn() {
        return lastChangedOn;
    }

    public void setLastChangedOn(String lastChangedOn) {
        this.lastChangedOn = lastChangedOn;
    }

    public String getLastChangedBy() {
        return lastChangedBy;
    }

    public void setLastChangedBy(String lastChangedBy) {
        this.lastChangedBy = lastChangedBy;
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

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }
}


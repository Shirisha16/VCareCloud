package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChildAbsentModel implements Serializable{
  String absentId,ChildId,ClassId,CustId,AbsentFrom,AbsentTo,AbsentType,AbsentNotes,childName,
          className,CreatedBy,CreatedOn,LastChangedBy,LastChangedOn,Status;

    public String getAbsentId() {
        return absentId;
    }

    public void setAbsentId(String absentId) {
        this.absentId = absentId;
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

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    @Override
    public String toString() {
        return "ChildAbsentModel{" +
                "absentId='" + absentId + '\'' +
                ", ChildId='" + ChildId + '\'' +
                ", ClassId='" + ClassId + '\'' +
                ", CustId='" + CustId + '\'' +
                ", AbsentFrom='" + AbsentFrom + '\'' +
                ", AbsentTo='" + AbsentTo + '\'' +
                ", AbsentType='" + AbsentType + '\'' +
                ", AbsentNotes='" + AbsentNotes + '\'' +
                ", CreatedBy='" + CreatedBy + '\'' +
                ", CreatedOn='" + CreatedOn + '\'' +
                ", LastChangedBy='" + LastChangedBy + '\'' +
                ", LastChangedOn='" + LastChangedOn + '\'' +
                ", Status='" + Status + '\'' +
                ", childName='" + childName + '\'' +
                ", className='" + className + '\'' +

                '}';
    }
}

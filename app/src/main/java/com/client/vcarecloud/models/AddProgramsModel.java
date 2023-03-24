package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProgramsModel {

    @SerializedName("EmpId")
    @Expose
    private String empid;

    @SerializedName("classId")
    @Expose
    private String classId;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("ClassName")
    @Expose
    private String className;

    @SerializedName("MinAgeLimit")
    @Expose
    private String minAge;

    @SerializedName("MaxAgeLimit")
    @Expose
    private String maxAge;

    @SerializedName("Capacity")
    @Expose
    private String capacity;

    @SerializedName("Photo")
    @Expose
    private String photo;

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
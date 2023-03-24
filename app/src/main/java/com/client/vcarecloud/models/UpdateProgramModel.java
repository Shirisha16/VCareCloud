package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProgramModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("empId")
    @Expose
    private String empid;

    @SerializedName("classId")
    @Expose
    private String classid;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("className")
    @Expose
    private String className;

    @SerializedName("minAge")
    @Expose
    private String minAge;

    @SerializedName("maxAge")
    @Expose
    private String maxAge;

    @SerializedName("capacity")
    @Expose
    private String capacity;

    @SerializedName("photo")
    @Expose
    private String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
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

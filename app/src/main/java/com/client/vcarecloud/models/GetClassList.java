package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetClassList {
    @SerializedName("classId")
    @Expose

    private String classId;
    @SerializedName("className")
    @Expose

    private String className;
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "GetClassList{" +
                "classId='" + classId + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}

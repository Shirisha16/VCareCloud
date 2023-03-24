package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddEventModel {

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("typeId")
    @Expose
    private String typeId;

    @SerializedName("eventName")
    @Expose
    private String eventName;

    @SerializedName("eventLocation")
    @Expose
    private String location;

    @SerializedName("eventDetails")
    @Expose
    private String details;

//    @SerializedName("eventtype")
//    @Expose
//    private String eventtype;

    @SerializedName("fromDate")
    @Expose
    private String fromDate;

    @SerializedName("toDate")
    @Expose
    private String toDate;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

//    public String getEventtype() {
//        return eventtype;
//    }
//
//    public void setEventtype(String eventtype) {
//        this.eventtype = eventtype;
//    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}

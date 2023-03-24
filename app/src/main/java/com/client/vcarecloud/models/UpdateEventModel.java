package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateEventModel {

    @SerializedName("eventID")
    @Expose
    private String eventId;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("eventName")
    @Expose
    private String eventName;

    @SerializedName("eventLocation")
    @Expose
    private String location;

    @SerializedName("eventDetails")
    @Expose
    private String details;

    @SerializedName("typeId")
    @Expose
    private String typeId;

    @SerializedName("fromDate")
    @Expose
    private String fromDate;

    @SerializedName("toDate")
    @Expose
    private String toDate;

    @SerializedName("eventtype")
    @Expose
    private String typeName;


    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
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

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}

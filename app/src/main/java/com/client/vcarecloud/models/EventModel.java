package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventModel{
    @SerializedName("model")
    @Expose
    private List<Model> model;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("didError")
    @Expose
    private Boolean didError;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;

    public List<Model> getModel() {
        return model;
    }

    public void setModel(List<Model> model) {
        this.model = model;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDidError() {
        return didError;
    }

    public void setDidError(Boolean didError) {
        this.didError = didError;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public class Model {

        @SerializedName("eventID")
        @Expose
        private Integer eventID;
        @SerializedName("eventtype")
        @Expose
        private String eventtype;
        @SerializedName("custId")
        @Expose
        private Integer custId;
        @SerializedName("eventName")
        @Expose
        private String eventName;
        @SerializedName("eventLocation")
        @Expose
        private String eventLocation;
        @SerializedName("eventDetails")
        @Expose
        private String eventDetails;
        @SerializedName("fromDate")
        @Expose
        private String fromDate;
        @SerializedName("toDate")
        @Expose
        private String toDate;
        @SerializedName("createdBy")
        @Expose
        private Integer createdBy;
        @SerializedName("createdOn")
        @Expose
        private String createdOn;
        @SerializedName("lastChangedBy")
        @Expose
        private Integer lastChangedBy;
        @SerializedName("lastChangedOn")
        @Expose
        private String lastChangedOn;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("typeId")
        @Expose
        private Integer typeId;

        public Integer getEventID() {
            return eventID;
        }

        public void setEventID(Integer eventID) {
            this.eventID = eventID;
        }

        public String getEventtype() {
            return eventtype;
        }

        public void setEventtype(String eventtype) {
            this.eventtype = eventtype;
        }

        public Integer getCustId() {
            return custId;
        }

        public void setCustId(Integer custId) {
            this.custId = custId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventLocation() {
            return eventLocation;
        }

        public void setEventLocation(String eventLocation) {
            this.eventLocation = eventLocation;
        }

        public String getEventDetails() {
            return eventDetails;
        }

        public void setEventDetails(String eventDetails) {
            this.eventDetails = eventDetails;
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

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public Integer getLastChangedBy() {
            return lastChangedBy;
        }

        public void setLastChangedBy(Integer lastChangedBy) {
            this.lastChangedBy = lastChangedBy;
        }

        public String getLastChangedOn() {
            return lastChangedOn;
        }

        public void setLastChangedOn(String lastChangedOn) {
            this.lastChangedOn = lastChangedOn;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getTypeId() {
            return typeId;
        }

        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }
    }
}


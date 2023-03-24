package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChildCheckInModel {
// implements Serializable
//        @SerializedName("message")
//        @Expose
//        private Object message;
//        @SerializedName("didError")
//        @Expose
//        private Boolean didError;
//        @SerializedName("errorMessage")
//        @Expose
//        private String errorMessage;
//
//        List<CheckInRequest> model;
//
//        private final static long serialVersionUID = -3714931405509910001L;
//
//        public Object getMessage() {
//        return message;
//    }
//
//        public void setMessage(Object message) {
//        this.message = message;
//    }
//
//        public Boolean getDidError() {
//        return didError;
//    }
//
//        public void setDidError(Boolean didError) {
//        this.didError = didError;
//    }
//
//        public String getErrorMessage() {
//        return errorMessage;
//    }
//
//        public void setErrorMessage(String errorMessage) {
//        this.errorMessage = errorMessage;
//    }
//
//    public List<CheckInRequest> getModel() {
//        return model;
//    }
//
//    public void setModel(List<CheckInRequest> model) {
//        this.model = model;
//    }

    private String message,errorMessage;
    private boolean didError;

    List<CheckInRequest> model;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isDidError() {
        return didError;
    }

    public void setDidError(boolean didError) {
        this.didError = didError;
    }

    public List<CheckInRequest> getModel() {
        return model;
    }

    public void setModel(List<CheckInRequest> model) {
        this.model = model;
    }
}

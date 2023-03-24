package com.client.vcarecloud.models;

import java.util.List;

public class CheckInResponse {
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

    @Override
        public String toString() {
            return "CheckInResponse{" +
                    "message='" + message + '\'' +
                    ", didError='" + didError + '\'' +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", model=" + model +
                    '}';
        }
    }



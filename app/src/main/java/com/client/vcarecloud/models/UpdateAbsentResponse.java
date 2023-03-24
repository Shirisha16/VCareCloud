package com.client.vcarecloud.models;

import java.util.List;

public class UpdateAbsentResponse {
    private String message,didError,errorMessage;
    List<UpdateAbsentModel> model;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDidError() {
        return didError;
    }

    public void setDidError(String didError) {
        this.didError = didError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<UpdateAbsentModel> getModel() {
        return model;
    }

    public void setModel(List<UpdateAbsentModel> model) {
        this.model = model;
    }
    @Override
    public String toString() {
        return "UpdateAbsentResponse{" +
                "message='" + message + '\'' +
                ", didError='" + didError + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", model=" + model +
                '}';
    }
}

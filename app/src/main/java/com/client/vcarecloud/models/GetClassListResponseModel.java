package com.client.vcarecloud.models;

import java.util.List;

public class GetClassListResponseModel {
    String message,errorMessage,didError;
    List<GetClassList> model;

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

    public String getDidError() {
        return didError;
    }

    public void setDidError(String didError) {
        this.didError = didError;
    }

    public List<GetClassList> getModel() {
        return model;
    }

    public void setModel(List<GetClassList> model) {
        this.model = model;
    }
    @Override
    public String toString() {
        return "GetChildListResponseModel{" +
                ", model=" + model +
                "message='" + message + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", didError=" + didError +
                '}';
    }
}

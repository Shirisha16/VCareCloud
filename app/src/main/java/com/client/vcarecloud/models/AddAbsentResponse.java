package com.client.vcarecloud.models;

import java.util.List;

public class AddAbsentResponse {
    private String message,didError,errorMessage;
    List<AddAbsentModel> model;

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

    public List<AddAbsentModel> getModel() {
        return model;
    }

    public void setModel(List<AddAbsentModel> model) {
        this.model = model;
    }

}

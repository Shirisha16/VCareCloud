package com.client.vcarecloud.models;

import java.util.List;

public class AddSecurityProfileResponse {
    private String message,didError,errorMessage;
    List<AddSecurityProfileModel> model;

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

    public List<AddSecurityProfileModel> getModel() {
        return model;
    }

    public void setModel(List<AddSecurityProfileModel> model) {
        this.model = model;
    }
}

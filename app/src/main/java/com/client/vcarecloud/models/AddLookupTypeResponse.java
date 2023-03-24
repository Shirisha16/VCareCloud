package com.client.vcarecloud.models;

import java.util.List;

public class AddLookupTypeResponse {
    private String message,didError,errorMessage;
    List<AddLookupTypeModel> model;

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

    public List<AddLookupTypeModel> getModel() {
        return model;
    }

    public void setModel(List<AddLookupTypeModel> model) {
        this.model = model;
    }
}

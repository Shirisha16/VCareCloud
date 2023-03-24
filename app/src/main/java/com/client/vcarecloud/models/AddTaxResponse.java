package com.client.vcarecloud.models;

import java.util.List;

public class AddTaxResponse {
    private String message,didError,errorMessage;
    List<AddTaxModel> model;

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

    public List<AddTaxModel> getModel() {
        return model;
    }

    public void setModel(List<AddTaxModel> model) {
        this.model = model;
    }
}

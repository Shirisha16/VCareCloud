package com.client.vcarecloud.models;

import com.client.vcarecloud.models.AddShiftModel;

import java.util.List;

public class AddShiftResponse {
    private String message,didError,errorMessage;
    List<AddShiftModel> model;

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

    public List<AddShiftModel> getModel() {
        return model;
    }

    public void setModel(List<AddShiftModel> model) {
        this.model = model;
    }
}

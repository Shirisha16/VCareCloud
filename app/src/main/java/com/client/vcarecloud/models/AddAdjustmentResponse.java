package com.client.vcarecloud.models;

import java.util.List;

public class AddAdjustmentResponse {
    private String message,didError,errorMessage;
    List<AddAdjustmentModel> model;

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

    public List<AddAdjustmentModel> getModel() {
        return model;
    }

    public void setModel(List<AddAdjustmentModel> model) {
        this.model = model;
    }
}

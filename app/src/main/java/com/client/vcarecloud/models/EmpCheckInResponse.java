package com.client.vcarecloud.models;

import java.util.List;

public class EmpCheckInResponse {
    private String message,errorMessage;
    private boolean didError;
    List<EmpCheckInRequest> model;

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

    public List<EmpCheckInRequest> getModel() {
        return model;
    }

    public void setModel(List<EmpCheckInRequest> model) {
        this.model = model;
    }
}

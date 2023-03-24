package com.client.vcarecloud.models;

import java.util.List;

public class PasswordResponce {
    private String message,errorMessage;
    private boolean didError;
    List<ChangePasswordModel> model;

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

    public List<ChangePasswordModel> getModel() {
        return model;
    }

    public void setModel(List<ChangePasswordModel> model) {
        this.model = model;
    }

    @Override
        public String toString() {
            return "PasswordResponse{" +
                    "message='" + message + '\'' +
                    ", didError='" + didError + '\'' +
                    ", errorMessage='" + errorMessage + '\'' +
                    ", model=" + model +
                    '}';
        }
    }



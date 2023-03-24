package com.client.vcarecloud.models;

public class ChildEnrollModel {
    String message,didError,errorMessage;

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

    @Override
    public String toString() {
        return "ChildEnrollModel{" +
                "message='" + message + '\'' +
                ", didError='" + didError + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

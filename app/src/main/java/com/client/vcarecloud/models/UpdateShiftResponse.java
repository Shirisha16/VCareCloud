package com.client.vcarecloud.models;

import java.util.List;

public class UpdateShiftResponse {
    String message,didError,errorMessage;
    List<UpdateShiftModel> updateShiftModelList;

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

    public List<UpdateShiftModel> getUpdateShiftModelList() {
        return updateShiftModelList;
    }

    public void setUpdateShiftModelList(List<UpdateShiftModel> updateShiftModelList) {
        this.updateShiftModelList = updateShiftModelList;
    }

    @Override
    public String toString() {
        return "UpdateShiftResponse{" +
                "message='" + message + '\'' +
                ", didError='" + didError + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", model=" + updateShiftModelList +
                '}';
    }
}

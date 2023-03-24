package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChildNameResponse {
    @SerializedName("model")
    @Expose
    private List<GetChildList> model = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("didError")
    @Expose
    private Boolean didError;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;

    public List<GetChildList> getModel() {
        return model;
    }

    public void setModel(List<GetChildList> model) {
        this.model = model;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getDidError() {
        return didError;
    }

    public void setDidError(Boolean didError) {
        this.didError = didError;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }
}

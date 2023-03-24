package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SentMsgModel implements Serializable {
    String communicationId,custId,sendOn,sendToCategory,sendTo,messageSubject,messageDescription,
            attachments;

    String message,error;

    public String getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(String communicationId) {
        this.communicationId = communicationId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getSendOn() {
        return sendOn;
    }

    public void setSendOn(String sendOn) {
        this.sendOn = sendOn;
    }

    public String getSendToCategory() {
        return sendToCategory;
    }

    public void setSendToCategory(String sendToCategory) {
        this.sendToCategory = sendToCategory;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public void setMessageSubject(String messageSubject) {
        this.messageSubject = messageSubject;
    }

    public String getMessageDescription() {
        return messageDescription;
    }

    public void setMessageDescription(String messageDescription) {
        this.messageDescription = messageDescription;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

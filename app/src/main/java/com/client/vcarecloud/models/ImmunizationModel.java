package com.client.vcarecloud.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ImmunizationModel implements Serializable {
    String immunizationId,custId,immunizationName,immunizationCode,dose1,dose2,dose3,dose4,dose5,
            dose6,isOptional;

    public String getImmunizationId() {
        return immunizationId;
    }

    public void setImmunizationId(String immunizationId) {
        this.immunizationId = immunizationId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getImmunizationName() {
        return immunizationName;
    }

    public void setImmunizationName(String immunizationName) {
        this.immunizationName = immunizationName;
    }

    public String getImmunizationCode() {
        return immunizationCode;
    }

    public void setImmunizationCode(String immunizationCode) {
        this.immunizationCode = immunizationCode;
    }

    public String getDose1() {
        return dose1;
    }

    public void setDose1(String dose1) {
        this.dose1 = dose1;
    }

    public String getDose2() {
        return dose2;
    }

    public void setDose2(String dose2) {
        this.dose2 = dose2;
    }

    public String getDose3() {
        return dose3;
    }

    public void setDose3(String dose3) {
        this.dose3 = dose3;
    }

    public String getDose4() {
        return dose4;
    }

    public void setDose4(String dose4) {
        this.dose4 = dose4;
    }

    public String getDose5() {
        return dose5;
    }

    public void setDose5(String dose5) {
        this.dose5 = dose5;
    }

    public String getDose6() {
        return dose6;
    }

    public void setDose6(String dose6) {
        this.dose6 = dose6;
    }

    public String getIsOptional() {
        return isOptional;
    }

    public void setIsOptional(String isOptional) {
        this.isOptional = isOptional;
    }
}

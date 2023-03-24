package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateTaxModel {
    @SerializedName("taxesId")
    @Expose
    private  String taxid;

    @SerializedName("custId")
    @Expose
    private  String custid;

    @SerializedName("taxName")
    @Expose
    private  String tax_name;

    @SerializedName("taxRate")
    @Expose
    private  String tax_rate;

    @SerializedName("taxStatus")
    @Expose
    private  String tax_status;

    @SerializedName("createdOn")
    @Expose
    private  String created_on;

    @SerializedName("modifiedOn")
    @Expose
    private  String modified_on;

    @SerializedName("status")
    @Expose
    private  String status;

    public String getTaxid() {
        return taxid;
    }

    public void setTaxid(String taxid) {
        this.taxid = taxid;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }

    public String getTax_rate() {
        return tax_rate;
    }

    public void setTax_rate(String tax_rate) {
        this.tax_rate = tax_rate;
    }

    public String getTax_status() {
        return tax_status;
    }

    public void setTax_status(String tax_status) {
        this.tax_status = tax_status;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getModified_on() {
        return modified_on;
    }

    public void setModified_on(String modified_on) {
        this.modified_on = modified_on;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

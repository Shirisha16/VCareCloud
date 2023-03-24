package com.client.vcarecloud.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateSecurityProfileModel {

    @SerializedName("securityId")
    @Expose
    private String securityId;

    @SerializedName("custId")
    @Expose
    private String custId;

    @SerializedName("securityProfileName")
    @Expose
    private String name;

    @SerializedName("access_ChildCheckInOut")
    @Expose
    private String checkInOut;

    @SerializedName("access_ChildAbsent")
    @Expose
    private String absent;

    @SerializedName("access_ViewChild")
    @Expose
    private String viewChild;

    @SerializedName("access_ModifyChild")
    @Expose
    private String modifyChild;

    @SerializedName("access_Waitlist")
    @Expose
    private String waitlist;

    @SerializedName("access_ModifyWaitlist")
    @Expose
    private String modifyWaitlist;

    @SerializedName("access_Employee")
    @Expose
    private String emp;

    @SerializedName("access_ModifyEmployee")
    @Expose
    private String modifyemp;

    @SerializedName("access_Shift")
    @Expose
    private String shift;

    @SerializedName("access_ModifyShift")
    @Expose
    private String modifyshift;

    @SerializedName("access_ActivityPlanner")
    @Expose
    private String activityPlanner;

    @SerializedName("access_ModifyActivityPlanner")
    @Expose
    private String modifyActivityPlanner;

    @SerializedName("access_DailyActivity")
    @Expose
    private String dailyActivity;

    @SerializedName("access_ModifyDailyActivity")
    @Expose
    private String modifydailyActivity;

    @SerializedName("access_ActivityTheme")
    @Expose
    private String activityTheme;

    @SerializedName("access_ModifyActivityTheme")
    @Expose
    private String modifyActivityTheme;

    @SerializedName("access_ActivityCategory")
    @Expose
    private String activityCategory;

    @SerializedName("access_Camp")
    @Expose
    private String camp;

    @SerializedName("access_ModifyCamp")
    @Expose
    private String modifyCamp;

    @SerializedName("access_MealPlanner")
    @Expose
    private String mealPlanner;

    @SerializedName("access_ModifyMealPlanner")
    @Expose
    private String modifymealPlanner;

    @SerializedName("access_DailyMeal")
    @Expose
    private String dailyMeal;

    @SerializedName("access_ModifyDailyMeal")
    @Expose
    private String modifyDailyMeal;

    @SerializedName("access_Menu")
    @Expose
    private String menu;

    @SerializedName("access_MealPortion")
    @Expose
    private String mealPortion;

    @SerializedName("access_BasePackages")
    @Expose
    private String basePackages;

    @SerializedName("access_ModifyBasePackages")
    @Expose
    private String modifyBasePackages;

    @SerializedName("access_AdditionalCharges")
    @Expose
    private String additionalCharges;

    @SerializedName("access_ModifyAdditionalCharges")
    @Expose
    private String modifyadditionalCharges;

    @SerializedName("access_Discounts")
    @Expose
    private String discounts;

    @SerializedName("access_ModifyDiscounts")
    @Expose
    private String modifyDiscounts;

    @SerializedName("access_Adjustments")
    @Expose
    private String adjustments;

    @SerializedName("access_ModifyAdjustments")
    @Expose
    private String modifyAdjustments;

    @SerializedName("access_GenerateInvoice")
    @Expose
    private String generateInvoice;

    @SerializedName("access_ChildCloseout")
    @Expose
    private String childCloseout;

    @SerializedName("access_PaymentTypes")
    @Expose
    private String paymentTypes;

    @SerializedName("access_Payments")
    @Expose
    private String payment;

    @SerializedName("access_ModifyPayments")
    @Expose
    private String ModifyPayments;

    @SerializedName("access_GeneralSetup")
    @Expose
    private String generalSetup;

    @SerializedName("access_LookupSetup")
    @Expose
    private String lookup;

    @SerializedName("access_ClassSetup")
    @Expose
    private String classSetup;

    @SerializedName("access_ImmunizationSetup")
    @Expose
    private String immunization;

    @SerializedName("access_Security")
    @Expose
    private String security;

    @SerializedName("access_Report")
    @Expose
    private String report;

    @SerializedName("access_Messaging")
    @Expose
    private String message;

    @SerializedName("access_SendMessages")
    @Expose
    private String sendmessage;

    public String getSecurityId() {
        return securityId;
    }

    public void setSecurityId(String securityId) {
        this.securityId = securityId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckInOut() {
        return checkInOut;
    }

    public void setCheckInOut(String checkInOut) {
        this.checkInOut = checkInOut;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getViewChild() {
        return viewChild;
    }

    public void setViewChild(String viewChild) {
        this.viewChild = viewChild;
    }

    public String getModifyChild() {
        return modifyChild;
    }

    public void setModifyChild(String modifyChild) {
        this.modifyChild = modifyChild;
    }

    public String getWaitlist() {
        return waitlist;
    }

    public void setWaitlist(String waitlist) {
        this.waitlist = waitlist;
    }

    public String getModifyWaitlist() {
        return modifyWaitlist;
    }

    public void setModifyWaitlist(String modifyWaitlist) {
        this.modifyWaitlist = modifyWaitlist;
    }

    public String getEmp() {
        return emp;
    }

    public void setEmp(String emp) {
        this.emp = emp;
    }

    public String getModifyemp() {
        return modifyemp;
    }

    public void setModifyemp(String modifyemp) {
        this.modifyemp = modifyemp;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getModifyshift() {
        return modifyshift;
    }

    public void setModifyshift(String modifyshift) {
        this.modifyshift = modifyshift;
    }

    public String getActivityPlanner() {
        return activityPlanner;
    }

    public void setActivityPlanner(String activityPlanner) {
        this.activityPlanner = activityPlanner;
    }

    public String getModifyActivityPlanner() {
        return modifyActivityPlanner;
    }

    public void setModifyActivityPlanner(String modifyActivityPlanner) {
        this.modifyActivityPlanner = modifyActivityPlanner;
    }

    public String getDailyActivity() {
        return dailyActivity;
    }

    public void setDailyActivity(String dailyActivity) {
        this.dailyActivity = dailyActivity;
    }

    public String getModifydailyActivity() {
        return modifydailyActivity;
    }

    public void setModifydailyActivity(String modifydailyActivity) {
        this.modifydailyActivity = modifydailyActivity;
    }

    public String getActivityTheme() {
        return activityTheme;
    }

    public void setActivityTheme(String activityTheme) {
        this.activityTheme = activityTheme;
    }

    public String getModifyActivityTheme() {
        return modifyActivityTheme;
    }

    public void setModifyActivityTheme(String modifyActivityTheme) {
        this.modifyActivityTheme = modifyActivityTheme;
    }

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    public String getCamp() {
        return camp;
    }

    public void setCamp(String camp) {
        this.camp = camp;
    }

    public String getModifyCamp() {
        return modifyCamp;
    }

    public void setModifyCamp(String modifyCamp) {
        this.modifyCamp = modifyCamp;
    }

    public String getMealPlanner() {
        return mealPlanner;
    }

    public void setMealPlanner(String mealPlanner) {
        this.mealPlanner = mealPlanner;
    }

    public String getModifymealPlanner() {
        return modifymealPlanner;
    }

    public void setModifymealPlanner(String modifymealPlanner) {
        this.modifymealPlanner = modifymealPlanner;
    }

    public String getDailyMeal() {
        return dailyMeal;
    }

    public void setDailyMeal(String dailyMeal) {
        this.dailyMeal = dailyMeal;
    }

    public String getModifyDailyMeal() {
        return modifyDailyMeal;
    }

    public void setModifyDailyMeal(String modifyDailyMeal) {
        this.modifyDailyMeal = modifyDailyMeal;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getMealPortion() {
        return mealPortion;
    }

    public void setMealPortion(String mealPortion) {
        this.mealPortion = mealPortion;
    }

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    public String getModifyBasePackages() {
        return modifyBasePackages;
    }

    public void setModifyBasePackages(String modifyBasePackages) {
        this.modifyBasePackages = modifyBasePackages;
    }

    public String getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getModifyadditionalCharges() {
        return modifyadditionalCharges;
    }

    public void setModifyadditionalCharges(String modifyadditionalCharges) {
        this.modifyadditionalCharges = modifyadditionalCharges;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getModifyDiscounts() {
        return modifyDiscounts;
    }

    public void setModifyDiscounts(String modifyDiscounts) {
        this.modifyDiscounts = modifyDiscounts;
    }

    public String getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(String adjustments) {
        this.adjustments = adjustments;
    }

    public String getModifyAdjustments() {
        return modifyAdjustments;
    }

    public void setModifyAdjustments(String modifyAdjustments) {
        this.modifyAdjustments = modifyAdjustments;
    }

    public String getGenerateInvoice() {
        return generateInvoice;
    }

    public void setGenerateInvoice(String generateInvoice) {
        this.generateInvoice = generateInvoice;
    }

    public String getChildCloseout() {
        return childCloseout;
    }

    public void setChildCloseout(String childCloseout) {
        this.childCloseout = childCloseout;
    }

    public String getPaymentTypes() {
        return paymentTypes;
    }

    public void setPaymentTypes(String paymentTypes) {
        this.paymentTypes = paymentTypes;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getModifyPayments() {
        return ModifyPayments;
    }

    public void setModifyPayments(String modifyPayments) {
        ModifyPayments = modifyPayments;
    }

    public String getGeneralSetup() {
        return generalSetup;
    }

    public void setGeneralSetup(String generalSetup) {
        this.generalSetup = generalSetup;
    }

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public String getClassSetup() {
        return classSetup;
    }

    public void setClassSetup(String classSetup) {
        this.classSetup = classSetup;
    }

    public String getImmunization() {
        return immunization;
    }

    public void setImmunization(String immunization) {
        this.immunization = immunization;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendmessage() {
        return sendmessage;
    }

    public void setSendmessage(String sendmessage) {
        this.sendmessage = sendmessage;
    }
}

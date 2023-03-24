package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.DiscountListModel;
import com.client.vcarecloud.models.SecurityProfileModel;
import com.client.vcarecloud.models.UpdateDiscountModel;
import com.client.vcarecloud.models.UpdateDiscountResponse;
import com.client.vcarecloud.models.UpdateSecurityProfileModel;
import com.client.vcarecloud.models.UpdateSecurityProfileResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateSecurityProfile extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText profile;
    AppCompatButton editProfile;

    MaterialCheckBox selectall,checkIn,absent,childView,modifyChild,viewWaitList,modifyWait,
            employeeView, modifyemp,shiftsView,modifyShifts,activityView,eventsView,dailyActView,
            campsView,modifyActPlanner, eventmodify,modifyDailyAct,campsModify,actCategories,
            mealView,dailyMealsView,menu,mealPortion, modifyMeal,dailyMealsModify,packageView,
            addChargesView,adjustmentView,discountView, modifyPackage,addChargesModify,
            adjustmentModify,modifyDiscount,childCloseout,generateInvoice, viewpayments,
            modifyPayments,paymentType,generalSetting,securityProfile,lookup,programs,
            immunization,viewMsgs,sendMsgs,viewReport;

    String  securityId,custId,profileName,checkInOut,childAbsent,viewChild,modify_child,wait_list,
            modify_waitList,emp,modifyEmp,shift,modifyShift,activityPlanner,modify_activityPlanner,
            dailyActivity,modifyDailyActivity,activityTheme,modify_actTheme,activityCategory,camp,
            modifyCamp,mealPlanner,modify_mealPlanner,dailyMeal,modify_dailyMeal,menu1,mealPortion1,
            basePackage,modify_basePackage,addCharges,modify_addCharges,discount,modify_discount,
            adjustment,modify_adjustment,generate_Invoice,child_closeOut,payment_type,payments,
            modify_payments,general,lookupSetup,classSetUp,immunizationSetup,security,report,
            messaging,sendMsg;

    UserDetails userDetails;
    String message,error;

    SecurityProfileModel securityProfileModel;
    UpdateSecurityProfileModel updateSecurityProfileModel;
    UpdateSecurityProfileResponse updateSecurityProfileResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_security_profile);

        back=findViewById(R.id.back);
        profile=findViewById(R.id.profileName1);
        editProfile=findViewById(R.id.edit);
        progress_layout=findViewById(R.id.progress);

        selectall=findViewById(R.id.selectAll);
        checkIn=findViewById(R.id.childCheckIn);
        absent=findViewById(R.id.absent);
        childView=findViewById(R.id.viewChild);
        modifyChild=findViewById(R.id.modify);
        viewWaitList=findViewById(R.id.waitList);
        modifyWait=findViewById(R.id.modifywaitList);
        employeeView=findViewById(R.id.viewEmp);
        modifyemp=findViewById(R.id.modifyemp);
        shiftsView=findViewById(R.id.viewShifts);
        modifyShifts=findViewById(R.id.modifyShift);
        activityView=findViewById(R.id.viewActPlanner);
        eventsView=findViewById(R.id.viewEvents);
        dailyActView=findViewById(R.id.dailyActivity);
        campsView=findViewById(R.id.camps);
        modifyActPlanner=findViewById(R.id.modifyActPlanner);
        eventmodify=findViewById(R.id.modifyevents);
        modifyDailyAct=findViewById(R.id.modifyActivities);
        campsModify=findViewById(R.id.modifyCamps);
        actCategories=findViewById(R.id.activityCategories);
        mealView=findViewById(R.id.viewMealPlanner);
        dailyMealsView=findViewById(R.id.viewDailyMeals);
        menu=findViewById(R.id.menu);
        mealPortion=findViewById(R.id.mealPortion);
        modifyMeal=findViewById(R.id.modifyMealPlanner);
        dailyMealsModify=findViewById(R.id.modifyDailyMeals);
        packageView=findViewById(R.id.viewBasePackage);
        addChargesView=findViewById(R.id.viewaddCharges);
        adjustmentView=findViewById(R.id.viewAdjustment);
        discountView=findViewById(R.id.viewDiscount);
        modifyPackage=findViewById(R.id.modifyBasePackage);
        addChargesModify=findViewById(R.id.modifyCharges);
        adjustmentModify=findViewById(R.id.modifyAdjustment);
        modifyDiscount=findViewById(R.id.modifyDiscount);
        childCloseout=findViewById(R.id.childCloseOut);
        generateInvoice=findViewById(R.id.generateInvoice);
        viewpayments=findViewById(R.id.viewPayments);
        modifyPayments=findViewById(R.id.modifyPayment);
        paymentType=findViewById(R.id.paymentType);
        generalSetting=findViewById(R.id.generalSetup);
        securityProfile=findViewById(R.id.securityProfile);
        lookup=findViewById(R.id.lookup);
        programs=findViewById(R.id.programs);
        immunization=findViewById(R.id.immunization);
        viewMsgs=findViewById(R.id.viewMsg);
        sendMsgs=findViewById(R.id.sendMsg);
        viewReport=findViewById(R.id.viewReport);

        securityProfileModel=new SecurityProfileModel();
        updateSecurityProfileModel=new UpdateSecurityProfileModel();
        updateSecurityProfileResponse=new UpdateSecurityProfileResponse();

        userDetails=new UserDetails(UpdateSecurityProfile.this);

        securityProfileModel=(SecurityProfileModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateSecurityProfile.this,SecurityProfile.class);
                intent.putExtra("list",securityProfileModel);
                startActivity(intent);
                finish();
            }
        });

        securityId=getIntent().getStringExtra("securityId");
        custId=getIntent().getStringExtra("custId");
        profileName=getIntent().getStringExtra("securityProfileName");
        checkInOut=getIntent().getStringExtra("access_ChildCheckInOut");
        childAbsent=getIntent().getStringExtra("access_ChildAbsent");
        viewChild=getIntent().getStringExtra("access_ViewChild");
        modify_child=getIntent().getStringExtra("access_ModifyChild");
        wait_list=getIntent().getStringExtra("access_Waitlist");
        modify_waitList=getIntent().getStringExtra("access_ModifyWaitlist");
        emp=getIntent().getStringExtra("access_Employee");
        modifyEmp=getIntent().getStringExtra("access_ModifyEmployee");
        shift=getIntent().getStringExtra("access_Shift");
        modifyShift=getIntent().getStringExtra("access_ModifyShift");
        activityPlanner=getIntent().getStringExtra("access_ActivityPlanner");
        modify_activityPlanner=getIntent().getStringExtra("access_ModifyActivityPlanner");
        dailyActivity=getIntent().getStringExtra("access_DailyActivity");
        modifyDailyActivity=getIntent().getStringExtra("access_ModifyDailyActivity");
        activityTheme=getIntent().getStringExtra("access_ActivityTheme");
        modify_actTheme=getIntent().getStringExtra("access_ModifyActivityTheme");
        activityCategory=getIntent().getStringExtra("access_ActivityCategory");
        camp=getIntent().getStringExtra("access_Camp");
        modifyCamp=getIntent().getStringExtra("access_ModifyCamp");
        mealPlanner=getIntent().getStringExtra("access_MealPlanner");
        modify_mealPlanner=getIntent().getStringExtra("access_ModifyMealPlanner");
        dailyMeal=getIntent().getStringExtra("access_DailyMeal");
        modify_dailyMeal=getIntent().getStringExtra("access_ModifyDailyMeal");
        menu1=getIntent().getStringExtra("access_Menu");
        mealPortion1=getIntent().getStringExtra("access_MealPortion");
        basePackage=getIntent().getStringExtra("access_BasePackages");
        modify_basePackage=getIntent().getStringExtra("access_ModifyBasePackages");
        addCharges=getIntent().getStringExtra("access_AdditionalCharges");
        modify_addCharges=getIntent().getStringExtra("access_ModifyAdditionalCharges");
        discount=getIntent().getStringExtra("access_Discounts");
        modify_discount=getIntent().getStringExtra("access_ModifyDiscounts");
        adjustment=getIntent().getStringExtra("access_Adjustments");
        modify_adjustment=getIntent().getStringExtra("access_ModifyAdjustments");
        generate_Invoice=getIntent().getStringExtra("access_GenerateInvoice");
        child_closeOut=getIntent().getStringExtra("access_ChildCloseout");
        payment_type=getIntent().getStringExtra("access_PaymentTypes");
        payments=getIntent().getStringExtra("access_Payments");
        modify_payments=getIntent().getStringExtra("access_ModifyPayments");
        general=getIntent().getStringExtra("access_GeneralSetup");
        lookupSetup=getIntent().getStringExtra("access_LookupSetup");
        classSetUp=getIntent().getStringExtra("access_ClassSetup");
        immunizationSetup=getIntent().getStringExtra("access_ImmunizationSetup");
        security=getIntent().getStringExtra("access_Security");
        report=getIntent().getStringExtra("access_Report");
        messaging=getIntent().getStringExtra("access_Messaging");
        sendMsg=getIntent().getStringExtra("access_SendMessages");

        profile.setText(profileName);

        if(checkInOut.equalsIgnoreCase("y")){
            checkIn.setChecked(true);
        }else{
            checkIn.setChecked(false);
        }

        if(childAbsent.equalsIgnoreCase("y")){
            absent.setChecked(true);
        }else{
            absent.setChecked(false);
        }

        if(viewChild.equalsIgnoreCase("y")){
            childView.setChecked(true);
        }else{
            childView.setChecked(false);
        }

        if(modify_child.equalsIgnoreCase("y")){
            modifyChild.setChecked(true);
        }else{
            modifyChild.setChecked(false);
        }

        if(wait_list.equalsIgnoreCase("y")){
            viewWaitList.setChecked(true);
        }else{
            viewWaitList.setChecked(false);
        }

        if(modify_waitList.equalsIgnoreCase("y")){
            modifyWait.setChecked(true);
        }else{
            modifyWait.setChecked(false);
        }

        if(emp.equalsIgnoreCase("y")){
            employeeView.setChecked(true);
        }else{
            employeeView.setChecked(false);
        }

        if(modifyEmp.equalsIgnoreCase("y")){
            modifyemp.setChecked(true);
        }else{
            modifyemp.setChecked(false);
        }

        if(shift.equalsIgnoreCase("y")){
            shiftsView.setChecked(true);
        }else{
            shiftsView.setChecked(false);
        }

        if(modifyShift.equalsIgnoreCase("y")){
            modifyShifts.setChecked(true);
        }else{
            modifyShifts.setChecked(false);
        }

        if(activityPlanner.equalsIgnoreCase("y")){
            activityView.setChecked(true);
        }else{
            activityView.setChecked(false);
        }

        if(modify_activityPlanner.equalsIgnoreCase("y")){
            modifyActPlanner.setChecked(true);
        }else{
            modifyActPlanner.setChecked(false);
        }

        if(dailyActivity.equalsIgnoreCase("y")){
            dailyActView.setChecked(true);
        }else{
            dailyActView.setChecked(false);
        }

        if(modifyDailyActivity.equalsIgnoreCase("y")){
            modifyDailyAct.setChecked(true);
        }else{
            modifyDailyAct.setChecked(false);
        }

        if(activityTheme.equalsIgnoreCase("y")){
            eventsView.setChecked(true);
        }else{
            eventsView.setChecked(false);
        }

        if(modify_actTheme.equalsIgnoreCase("y")){
            eventmodify.setChecked(true);
        }else{
            eventmodify.setChecked(false);
        }

        if(activityCategory.equalsIgnoreCase("y")){
            actCategories.setChecked(true);
        }else{
            actCategories.setChecked(false);
        }

        if(camp.equalsIgnoreCase("y")){
            campsView.setChecked(true);
        }else{
            campsView.setChecked(false);
        }

        if(modifyCamp.equalsIgnoreCase("y")){
            campsModify.setChecked(true);
        }else{
            campsModify.setChecked(false);
        }

        if(mealPlanner.equalsIgnoreCase("y")){
            mealView.setChecked(true);
        }else{
            mealView.setChecked(false);
        }

        if(modify_mealPlanner.equalsIgnoreCase("y")){
            modifyMeal.setChecked(true);
        }else{
            modifyMeal.setChecked(false);
        }

        if(dailyMeal.equalsIgnoreCase("y")){
            dailyMealsView.setChecked(true);
        }else{
            dailyMealsView.setChecked(false);
        }

        if(modify_dailyMeal.equalsIgnoreCase("y")){
            dailyMealsModify.setChecked(true);
        }else{
            dailyMealsModify.setChecked(false);
        }

        if(menu1.equalsIgnoreCase("y")){
            menu.setChecked(true);
        }else{
            menu.setChecked(false);
        }

        if(mealPortion1.equalsIgnoreCase("y")){
            mealPortion.setChecked(true);
        }else{
            mealPortion.setChecked(false);
        }

        if(basePackage.equalsIgnoreCase("y")){
            packageView.setChecked(true);
        }else{
            packageView.setChecked(false);
        }

        if(modify_basePackage.equalsIgnoreCase("y")){
            modifyPackage.setChecked(true);
        }else{
            modifyPackage.setChecked(false);
        }

        if(addCharges.equalsIgnoreCase("y")){
            addChargesView.setChecked(true);
        }else{
            addChargesView.setChecked(false);
        }

        if(modify_addCharges.equalsIgnoreCase("y")){
            addChargesModify.setChecked(true);
        }else{
            addChargesModify.setChecked(false);
        }

        if(discount.equalsIgnoreCase("y")){
            discountView.setChecked(true);
        }else{
            discountView.setChecked(false);
        }

        if(modify_discount.equalsIgnoreCase("y")){
            modifyDiscount.setChecked(true);
        }else{
            modifyDiscount.setChecked(false);
        }

        if(adjustment.equalsIgnoreCase("y")){
            adjustmentView.setChecked(true);
        }else{
            adjustmentView.setChecked(false);
        }

        if(modify_adjustment.equalsIgnoreCase("y")){
            adjustmentModify.setChecked(true);
        }else{
            adjustmentModify.setChecked(false);
        }

        if(generate_Invoice.equalsIgnoreCase("y")){
            generateInvoice.setChecked(true);
        }else{
            generateInvoice.setChecked(false);
        }

        if(child_closeOut.equalsIgnoreCase("y")){
            childCloseout.setChecked(true);
        }else{
            childCloseout.setChecked(false);
        }

        if(payment_type.equalsIgnoreCase("y")){
            paymentType.setChecked(true);
        }else{
            paymentType.setChecked(false);
        }

        if(payments.equalsIgnoreCase("y")){
            viewpayments.setChecked(true);
        }else{
            viewpayments.setChecked(false);
        }

        if(modify_payments.equalsIgnoreCase("y")){
            modifyPayments.setChecked(true);
        }else{
            modifyPayments.setChecked(false);
        }

        if(general.equalsIgnoreCase("y")){
            generalSetting.setChecked(true);
        }else{
            generalSetting.setChecked(false);
        }

        if(lookupSetup.equalsIgnoreCase("y")){
            lookup.setChecked(true);
        }else{
            lookup.setChecked(false);
        }

        if(classSetUp.equalsIgnoreCase("y")){
            programs.setChecked(true);
        }else{
            programs.setChecked(false);
        }

        if(immunizationSetup.equalsIgnoreCase("y")){
            immunization.setChecked(true);
        }else{
            immunization.setChecked(false);
        }

        if(security.equalsIgnoreCase("y")){
            securityProfile.setChecked(true);
        }else{
            securityProfile.setChecked(false);
        }

        if(report.equalsIgnoreCase("y")){
            viewReport.setChecked(true);
        }else{
            viewReport.setChecked(false);
        }

        if(messaging.equalsIgnoreCase("y")){
            viewMsgs.setChecked(true);
        }else{
            viewMsgs.setChecked(false);
        }

        if(sendMsg.equalsIgnoreCase("y")){
            sendMsgs.setChecked(true);
        }else{
            sendMsgs.setChecked(false);
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileName=profile.getText().toString();
                checkInOut=checkIn.getText().toString();
                childAbsent=absent.getText().toString();
                viewChild=childView.getText().toString();
                modify_child=modifyChild.getText().toString();
                wait_list=viewWaitList.getText().toString();
                modify_waitList=modifyWait.getText().toString();
                emp=employeeView.getText().toString();
                modifyEmp=modifyemp.getText().toString();
                shift=shiftsView.getText().toString();
                modifyShift=modifyShifts.getText().toString();
                activityPlanner=activityView.getText().toString();
                modify_activityPlanner=modifyActPlanner.getText().toString();
                dailyActivity=dailyActView.getText().toString();
                modifyDailyActivity=modifyDailyAct.getText().toString();
                activityTheme=eventsView.getText().toString();
                modify_actTheme=eventmodify.getText().toString();
                activityCategory=actCategories.getText().toString();
                camp=campsView.getText().toString();
                modifyCamp=campsModify.getText().toString();
                mealPlanner=mealView.getText().toString();
                modify_mealPlanner=modifyMeal.getText().toString();
                dailyMeal=dailyMealsView.getText().toString();
                modify_dailyMeal=dailyMealsModify.getText().toString();
                menu1=menu.getText().toString();
                mealPortion1=mealPortion.getText().toString();
                basePackage=packageView.getText().toString();
                modify_basePackage=modifyPackage.getText().toString();
                addCharges=addChargesView.getText().toString();
                modify_addCharges=addChargesModify.getText().toString();
                discount=discountView.getText().toString();
                modify_discount=modifyDiscount.getText().toString();
                adjustment=adjustmentView.getText().toString();
                modify_adjustment=adjustmentModify.getText().toString();
                generate_Invoice=generateInvoice.getText().toString();
                child_closeOut=childCloseout.getText().toString();
                payment_type=paymentType.getText().toString();
                payments=viewpayments.getText().toString();
                modify_payments=modifyPayments.getText().toString();
                general=generalSetting.getText().toString();
                lookupSetup=lookup.getText().toString();
                classSetUp=programs.getText().toString();
                immunizationSetup=immunization.getText().toString();
                security=securityProfile.getText().toString();
                report=viewReport.getText().toString();
                messaging=viewMsgs.getText().toString();
                sendMsg=sendMsgs.getText().toString();

                if (Validation()) {
                    UpdatesecurityProfile();
                }
            }
        });
    }

    private void UpdatesecurityProfile() {
        progress_layout.setVisibility(View.VISIBLE);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        updateSecurityProfileModel.setSecurityId(securityId);
        updateSecurityProfileModel.setCustId(custId);
        updateSecurityProfileModel.setName(profileName);

        if(checkIn.isChecked()){
            updateSecurityProfileModel.setCheckInOut("Y");
        }else{
            updateSecurityProfileModel.setCheckInOut("N");
        }

        if(absent.isChecked()){
            updateSecurityProfileModel.setAbsent("Y");
        }else{
            updateSecurityProfileModel.setAbsent("N");
        }

        if(childView.isChecked()){
            updateSecurityProfileModel.setViewChild("Y");
        }else{
            updateSecurityProfileModel.setViewChild("N");
        }

        if(modifyChild.isChecked()){
            updateSecurityProfileModel.setModifyChild("Y");
        }else{
            updateSecurityProfileModel.setModifyChild("N");
        }

        if(viewWaitList.isChecked()){
            updateSecurityProfileModel.setWaitlist("Y");
        }else{
            updateSecurityProfileModel.setWaitlist("N");
        }

        if(modifyWait.isChecked()){
            updateSecurityProfileModel.setModifyWaitlist("Y");
        }else{
            updateSecurityProfileModel.setModifyWaitlist("N");
        }

        if(employeeView.isChecked()){
            updateSecurityProfileModel.setEmp("Y");
        }else{
            updateSecurityProfileModel.setEmp("N");
        }

        if(modifyemp.isChecked()){
            updateSecurityProfileModel.setModifyemp("Y");
        }else{
            updateSecurityProfileModel.setModifyemp("N");
        }

        if(shiftsView.isChecked()){
            updateSecurityProfileModel.setShift("Y");
        }else{
            updateSecurityProfileModel.setShift("N");
        }

        if(modifyShifts.isChecked()){
            updateSecurityProfileModel.setModifyshift("Y");
        }else{
            updateSecurityProfileModel.setModifyshift("N");
        }

        if(activityView.isChecked()){
            updateSecurityProfileModel.setActivityPlanner("Y");
        }else{
            updateSecurityProfileModel.setActivityPlanner("N");
        }

        if(eventsView.isChecked()){
            updateSecurityProfileModel.setActivityTheme("Y");
        }else{
            updateSecurityProfileModel.setActivityTheme("N");
        }

        if(dailyActView.isChecked()){
            updateSecurityProfileModel.setDailyActivity("Y");
        }else{
            updateSecurityProfileModel.setDailyActivity("N");
        }

        if(campsView.isChecked()){
            updateSecurityProfileModel.setCamp("Y");
        }else{
            updateSecurityProfileModel.setCamp("N");
        }

        if(modifyActPlanner.isChecked()){
            updateSecurityProfileModel.setModifyActivityPlanner("Y");
        }else{
            updateSecurityProfileModel.setModifyActivityPlanner("N");
        }

        if(eventmodify.isChecked()){
            updateSecurityProfileModel.setModifyActivityTheme("Y");
        }else{
            updateSecurityProfileModel.setModifyActivityTheme("N");
        }

        if(modifyDailyAct.isChecked()){
            updateSecurityProfileModel.setModifydailyActivity("Y");
        }else{
            updateSecurityProfileModel.setModifydailyActivity("N");
        }

        if(campsModify.isChecked()){
            updateSecurityProfileModel.setModifyCamp("Y");
        }else{
            updateSecurityProfileModel.setModifyCamp("N");
        }

        if(actCategories.isChecked()){
            updateSecurityProfileModel.setActivityCategory("Y");
        }else{
            updateSecurityProfileModel.setActivityCategory("N");
        }

        if(mealView.isChecked()){
            updateSecurityProfileModel.setMealPlanner("Y");
        }else{
            updateSecurityProfileModel.setMealPlanner("N");
        }

        if(dailyMealsView.isChecked()){
            updateSecurityProfileModel.setDailyMeal("Y");
        }else{
            updateSecurityProfileModel.setDailyMeal("N");
        }

        if(menu.isChecked()){
            updateSecurityProfileModel.setMenu("Y");
        }else{
            updateSecurityProfileModel.setMenu("N");
        }

        if(mealPortion.isChecked()){
            updateSecurityProfileModel.setMealPortion("Y");
        }else{
            updateSecurityProfileModel.setMealPortion("N");
        }

        if(modifyMeal.isChecked()){
            updateSecurityProfileModel.setModifymealPlanner("Y");
        }else{
            updateSecurityProfileModel.setModifymealPlanner("N");
        }

        if(dailyMealsModify.isChecked()){
            updateSecurityProfileModel.setModifyDailyMeal("Y");
        }else{
            updateSecurityProfileModel.setModifyDailyMeal("N");
        }

        if(packageView.isChecked()){
            updateSecurityProfileModel.setBasePackages("Y");
        }else{
            updateSecurityProfileModel.setBasePackages("N");
        }

        if(addChargesView.isChecked()){
            updateSecurityProfileModel.setAdditionalCharges("Y");
        }else{
            updateSecurityProfileModel.setAdditionalCharges("N");
        }

        if(adjustmentView.isChecked()){
            updateSecurityProfileModel.setAdjustments("Y");
        }else{
            updateSecurityProfileModel.setAdjustments("N");
        }

        if(discountView.isChecked()){
            updateSecurityProfileModel.setDiscounts("Y");
        }else{
            updateSecurityProfileModel.setDiscounts("N");
        }

        if(modifyPackage.isChecked()){
            updateSecurityProfileModel.setModifyBasePackages("Y");
        }else{
            updateSecurityProfileModel.setModifyBasePackages("N");
        }

        if(addChargesModify.isChecked()){
            updateSecurityProfileModel.setModifyadditionalCharges("Y");
        }else{
            updateSecurityProfileModel.setModifyadditionalCharges("N");
        }

        if(adjustmentModify.isChecked()){
            updateSecurityProfileModel.setModifyAdjustments("Y");
        }else{
            updateSecurityProfileModel.setModifyAdjustments("N");
        }

        if(modifyDiscount.isChecked()){
            updateSecurityProfileModel.setModifyDiscounts("Y");
        }else{
            updateSecurityProfileModel.setModifyDiscounts("N");
        }

        if(childCloseout.isChecked()){
            updateSecurityProfileModel.setChildCloseout("Y");
        }else{
            updateSecurityProfileModel.setChildCloseout("N");
        }

        if(generateInvoice.isChecked()){
            updateSecurityProfileModel.setGenerateInvoice("Y");
        }else{
            updateSecurityProfileModel.setGenerateInvoice("N");
        }

        if(viewpayments.isChecked()){
            updateSecurityProfileModel.setPayment("Y");
        }else{
            updateSecurityProfileModel.setPayment("N");
        }

        if(modifyPayments.isChecked()){
            updateSecurityProfileModel.setModifyPayments("Y");
        }else{
            updateSecurityProfileModel.setModifyPayments("N");
        }

        if(paymentType.isChecked()){
            updateSecurityProfileModel.setPaymentTypes("Y");
        }else{
            updateSecurityProfileModel.setPaymentTypes("N");
        }

        if(generalSetting.isChecked()){
            updateSecurityProfileModel.setGeneralSetup("Y");
        }else{
            updateSecurityProfileModel.setGeneralSetup("N");
        }

        if(securityProfile.isChecked()){
            updateSecurityProfileModel.setSecurity("Y");
        }else{
            updateSecurityProfileModel.setSecurity("N");
        }

        if(lookup.isChecked()){
            updateSecurityProfileModel.setLookup("Y");
        }else{
            updateSecurityProfileModel.setLookup("N");
        }

        if(programs.isChecked()){
            updateSecurityProfileModel.setClassSetup("Y");
        }else{
            updateSecurityProfileModel.setClassSetup("N");
        }

        if(immunization.isChecked()){
            updateSecurityProfileModel.setImmunization("Y");
        }else{
            updateSecurityProfileModel.setImmunization("N");
        }

        if(viewMsgs.isChecked()){
            updateSecurityProfileModel.setMessage("Y");
        }else{
            updateSecurityProfileModel.setMessage("N");
        }

        if(sendMsgs.isChecked()){
            updateSecurityProfileModel.setSendmessage("Y");
        }else{
            updateSecurityProfileModel.setSendmessage("N");
        }

        if(viewReport.isChecked()){
            updateSecurityProfileModel.setReport("Y");
        }else{
            updateSecurityProfileModel.setReport("N");
        }

        Call<UpdateSecurityProfileResponse> call=api.update_security_profile(securityId,"0",updateSecurityProfileModel);
        call.enqueue(new Callback<UpdateSecurityProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateSecurityProfileResponse> call, Response<UpdateSecurityProfileResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdateSecurityProfile.this != null && !UpdateSecurityProfile.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateSecurityProfile.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateSecurityProfile.this, SecurityProfile.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } else{
                        Utils.showAlertDialog(UpdateSecurityProfile.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateSecurityProfileResponse> call, Throwable t) {
                Toast.makeText(UpdateSecurityProfile.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean Validation() {
        if(!profilename()){
            return false;
        }else if(!applicationOn()){
            return false;
        }else{
            return true;
        }
    }

    private boolean profilename() {
        String str = profile.getText().toString();
        if (profile.getText().toString().isEmpty()) {
            profile.setError("Security Profile Name should not be empty");
            profile.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            profile.setText(profile.getText().toString().trim());
            profile.setSelection(profile.getText().length());
            return false;
        }else {
            return true;
        }
    }

    private boolean applicationOn() {
        if(checkIn.isChecked()==false && absent.isChecked()==false && childView.isChecked()==false &&
                modifyChild.isChecked()==false && viewWaitList.isChecked()==false && modifyWait.isChecked()==false
                && employeeView.isChecked()==false && modifyemp.isChecked()==false && shiftsView.isChecked()==false
                && modifyShifts.isChecked()==false && activityView.isChecked()==false && eventsView.isChecked()==false
                && dailyActView.isChecked()==false && campsView.isChecked()==false && modifyActPlanner.isChecked()==false
                && eventmodify.isChecked()==false && modifyDailyAct.isChecked()==false && campsModify.isChecked()==false
                && actCategories.isChecked()==false && mealView.isChecked()==false && dailyMealsView.isChecked()==false
                && menu.isChecked()==false && mealPortion.isChecked()==false && modifyMeal.isChecked()==false &&
                dailyMealsModify.isChecked()==false && packageView.isChecked()==false && addChargesView.isChecked()==false
                && adjustmentView.isChecked()==false && discountView.isChecked()==false && modifyPackage.isChecked()==false
                && addChargesModify.isChecked()==false && adjustmentModify.isChecked()==false && modifyDiscount.isChecked()==false
                && childCloseout.isChecked()==false && generateInvoice.isChecked()==false && viewpayments.isChecked()==false
                && modifyPayments.isChecked()==false && paymentType.isChecked()==false && generalSetting.isChecked()==false
                && securityProfile.isChecked()==false && lookup.isChecked()==false && programs.isChecked()==false &&
                immunization.isChecked()==false && viewMsgs.isChecked()==false && sendMsgs.isChecked()==false
                && viewReport.isChecked()==false) {

//            Toast.makeText(UpdateSecurityProfile.this, "Please select atleast one Attribute", Toast.LENGTH_SHORT).show();
           Utils.showAlertDialog(UpdateSecurityProfile.this,"Please select atleast one Attribute",false);
            return false;
        }else{
            return true;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateSecurityProfile.this,SecurityProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
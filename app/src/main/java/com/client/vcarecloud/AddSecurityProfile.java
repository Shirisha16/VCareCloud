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
import com.client.vcarecloud.models.AddDiscountResponse;
import com.client.vcarecloud.models.AddSecurityProfileModel;
import com.client.vcarecloud.models.AddSecurityProfileResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddSecurityProfile extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText profile;
    AppCompatButton addProfile;

    MaterialCheckBox selectall,checkIn,absent,childView,modifyChild,viewWaitList,modifyWait,
            employeeView, modifyemp,shiftsView,modifyShifts,activityView,eventsView,dailyActView,
            campsView,modifyActPlanner, eventmodify,modifyDailyAct,campsModify,actCategories,
            mealView,dailyMealsView,menu,mealPortion, modifyMeal,dailyMealsModify,packageView,
            addChargesView,adjustmentView,discountView, modifyPackage,addChargesModify,
            adjustmentModify,modifyDiscount,childCloseout,generateInvoice, viewpayments,
            modifyPayments,paymentType,generalSetting,securityProfile,lookup,programs,
            immunization,viewMsgs,sendMsgs,viewReport;

    String  custId,profileName,checkInOut,childAbsent,viewChild,modify_child,wait_list,
            modify_waitList,emp,modifyEmp,shift,modifyShift,activityPlanner,modify_activityPlanner,
            dailyActivity,modifyDailyActivity,activityTheme,modify_actTheme,activityCategory,camp,
            modifyCamp,mealPlanner,modify_mealPlanner,dailyMeal,modify_dailyMeal,menu1,mealPortion1,
            basePackage,modify_basePackage,addCharges,modify_addCharges,discount,modify_discount,
            adjustment,modify_adjustment,generate_Invoice,child_closeOut,payment_type,payments,
            modify_payments,general,lookupSetup,classSetUp,immunizationSetup,security,report,
            messaging,sendMsg,selectAll1;

    String message,errorMessage;
    UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_security_profile);

        back=findViewById(R.id.back);
        profile=findViewById(R.id.profileName1);
        addProfile=findViewById(R.id.add);
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

        userDetails = new UserDetails(AddSecurityProfile.this);

        custId = userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSecurityProfile.this, SecurityProfile.class);
                startActivity(intent);
                finish();
            }
        });

        selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectall.isChecked()){
                    checkIn.setChecked(true);
                    absent.setChecked(true);
                    childView.setChecked(true);
                    modifyChild.setChecked(true);
                    viewWaitList.setChecked(true);
                    modifyWait.setChecked(true);
                    employeeView.setChecked(true);
                    modifyemp.setChecked(true);
                    shiftsView.setChecked(true);
                    modifyShifts.setChecked(true);
                    activityView.setChecked(true);
                    eventsView.setChecked(true);
                    dailyActView.setChecked(true);
                    campsView.setChecked(true);
                    modifyActPlanner.setChecked(true);
                    eventmodify.setChecked(true);
                    modifyDailyAct.setChecked(true);
                    campsModify.setChecked(true);
                    actCategories.setChecked(true);
                    mealView.setChecked(true);
                    dailyMealsView.setChecked(true);
                    menu.setChecked(true);
                    mealPortion.setChecked(true);
                    modifyMeal.setChecked(true);
                    dailyMealsModify.setChecked(true);
                    packageView.setChecked(true);
                    addChargesView.setChecked(true);
                    adjustmentView.setChecked(true);
                    discountView.setChecked(true);
                    modifyPackage.setChecked(true);
                    addChargesModify.setChecked(true);
                    adjustmentModify.setChecked(true);
                    modifyDiscount.setChecked(true);
                    childCloseout.setChecked(true);
                    generateInvoice.setChecked(true);
                    viewpayments.setChecked(true);
                    modifyPayments.setChecked(true);
                    paymentType.setChecked(true);
                    generalSetting.setChecked(true);
                    securityProfile.setChecked(true);
                    lookup.setChecked(true);
                    programs.setChecked(true);
                    immunization.setChecked(true);
                    viewMsgs.setChecked(true);
                    sendMsgs.setChecked(true);
                    viewReport.setChecked(true);
                }else{
                    checkIn.setChecked(false);
                    absent.setChecked(false);
                    childView.setChecked(false);
                    modifyChild.setChecked(false);
                    viewWaitList.setChecked(false);
                    modifyWait.setChecked(false);
                    employeeView.setChecked(false);
                    modifyemp.setChecked(false);
                    shiftsView.setChecked(false);
                    modifyShifts.setChecked(false);
                    activityView.setChecked(false);
                    eventsView.setChecked(false);
                    dailyActView.setChecked(false);
                    campsView.setChecked(false);
                    modifyActPlanner.setChecked(false);
                    eventmodify.setChecked(false);
                    modifyDailyAct.setChecked(false);
                    campsModify.setChecked(false);
                    actCategories.setChecked(false);
                    mealView.setChecked(false);
                    dailyMealsView.setChecked(false);
                    menu.setChecked(false);
                    mealPortion.setChecked(false);
                    modifyMeal.setChecked(false);
                    dailyMealsModify.setChecked(false);
                    packageView.setChecked(false);
                    addChargesView.setChecked(false);
                    adjustmentView.setChecked(false);
                    discountView.setChecked(false);
                    modifyPackage.setChecked(false);
                    addChargesModify.setChecked(false);
                    adjustmentModify.setChecked(false);
                    modifyDiscount.setChecked(false);
                    childCloseout.setChecked(false);
                    generateInvoice.setChecked(false);
                    viewpayments.setChecked(false);
                    modifyPayments.setChecked(false);
                    paymentType.setChecked(false);
                    generalSetting.setChecked(false);
                    securityProfile.setChecked(false);
                    lookup.setChecked(false);
                    programs.setChecked(false);
                    immunization.setChecked(false);
                    viewMsgs.setChecked(false);
                    sendMsgs.setChecked(false);
                    viewReport.setChecked(false);
                }
            }
        });

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileName=profile.getText().toString();
                selectAll1=selectall.getText().toString();
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
                    addsecurityProfile();
                }
            }
        });
    }

    private void addsecurityProfile() {
        progress_layout.setVisibility(View.VISIBLE);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        AddSecurityProfileModel addSecurityProfileModel=new AddSecurityProfileModel();

        addSecurityProfileModel.setCustId(custId);
        addSecurityProfileModel.setName(profileName);

        if(checkIn.isChecked()){
            addSecurityProfileModel.setCheckInOut("Y");
        }else{
            addSecurityProfileModel.setCheckInOut("N");
        }

        if(absent.isChecked()){
            addSecurityProfileModel.setAbsent("Y");
        }else{
            addSecurityProfileModel.setAbsent("N");
        }

        if(childView.isChecked()){
            addSecurityProfileModel.setViewChild("Y");
        }else{
            addSecurityProfileModel.setViewChild("N");
        }

        if(modifyChild.isChecked()){
            addSecurityProfileModel.setModifyChild("Y");
        }else{
            addSecurityProfileModel.setModifyChild("N");
        }

        if(viewWaitList.isChecked()){
            addSecurityProfileModel.setWaitlist("Y");
        }else{
            addSecurityProfileModel.setWaitlist("N");
        }

        if(modifyWait.isChecked()){
            addSecurityProfileModel.setModifyWaitlist("Y");
        }else{
            addSecurityProfileModel.setModifyWaitlist("N");
        }

        if(employeeView.isChecked()){
            addSecurityProfileModel.setEmp("Y");
        }else{
            addSecurityProfileModel.setEmp("N");
        }

        if(modifyemp.isChecked()){
            addSecurityProfileModel.setModifyemp("Y");
        }else{
            addSecurityProfileModel.setModifyemp("N");
        }

        if(shiftsView.isChecked()){
            addSecurityProfileModel.setShift("Y");
        }else{
            addSecurityProfileModel.setShift("N");
        }

        if(modifyShifts.isChecked()){
            addSecurityProfileModel.setModifyshift("Y");
        }else{
            addSecurityProfileModel.setModifyshift("N");
        }

        if(activityView.isChecked()){
            addSecurityProfileModel.setActivityPlanner("Y");
        }else{
            addSecurityProfileModel.setActivityPlanner("N");
        }

        if(eventsView.isChecked()){
            addSecurityProfileModel.setActivityTheme("Y");
        }else{
            addSecurityProfileModel.setActivityTheme("N");
        }

        if(dailyActView.isChecked()){
            addSecurityProfileModel.setDailyActivity("Y");
        }else{
            addSecurityProfileModel.setDailyActivity("N");
        }

        if(campsView.isChecked()){
            addSecurityProfileModel.setCamp("Y");
        }else{
            addSecurityProfileModel.setCamp("N");
        }

        if(modifyActPlanner.isChecked()){
            addSecurityProfileModel.setModifyActivityPlanner("Y");
        }else{
            addSecurityProfileModel.setModifyActivityPlanner("N");
        }

        if(eventmodify.isChecked()){
            addSecurityProfileModel.setModifyActivityTheme("Y");
        }else{
            addSecurityProfileModel.setModifyActivityTheme("N");
        }

        if(modifyDailyAct.isChecked()){
            addSecurityProfileModel.setModifydailyActivity("Y");
        }else{
            addSecurityProfileModel.setModifydailyActivity("N");
        }

        if(campsModify.isChecked()){
            addSecurityProfileModel.setModifyCamp("Y");
        }else{
            addSecurityProfileModel.setModifyCamp("N");
        }

        if(actCategories.isChecked()){
            addSecurityProfileModel.setActivityCategory("Y");
        }else{
            addSecurityProfileModel.setActivityCategory("N");
        }

        if(mealView.isChecked()){
            addSecurityProfileModel.setMealPlanner("Y");
        }else{
            addSecurityProfileModel.setMealPlanner("N");
        }

        if(dailyMealsView.isChecked()){
            addSecurityProfileModel.setDailyMeal("Y");
        }else{
            addSecurityProfileModel.setDailyMeal("N");
        }

        if(menu.isChecked()){
            addSecurityProfileModel.setMenu("Y");
        }else{
            addSecurityProfileModel.setMenu("N");
        }

        if(mealPortion.isChecked()){
            addSecurityProfileModel.setMealPortion("Y");
        }else{
            addSecurityProfileModel.setMealPortion("N");
        }

        if(modifyMeal.isChecked()){
            addSecurityProfileModel.setModifymealPlanner("Y");
        }else{
            addSecurityProfileModel.setModifymealPlanner("N");
        }

        if(dailyMealsModify.isChecked()){
            addSecurityProfileModel.setModifyDailyMeal("Y");
        }else{
            addSecurityProfileModel.setModifyDailyMeal("N");
        }

        if(packageView.isChecked()){
            addSecurityProfileModel.setBasePackages("Y");
        }else{
            addSecurityProfileModel.setBasePackages("N");
        }

        if(addChargesView.isChecked()){
            addSecurityProfileModel.setAdditionalCharges("Y");
        }else{
            addSecurityProfileModel.setAdditionalCharges("N");
        }

        if(adjustmentView.isChecked()){
            addSecurityProfileModel.setAdjustments("Y");
        }else{
            addSecurityProfileModel.setAdjustments("N");
        }

        if(discountView.isChecked()){
            addSecurityProfileModel.setDiscounts("Y");
        }else{
            addSecurityProfileModel.setDiscounts("N");
        }

        if(modifyPackage.isChecked()){
            addSecurityProfileModel.setModifyBasePackages("Y");
        }else{
            addSecurityProfileModel.setModifyBasePackages("N");
        }

        if(addChargesModify.isChecked()){
            addSecurityProfileModel.setModifyadditionalCharges("Y");
        }else{
            addSecurityProfileModel.setModifyadditionalCharges("N");
        }

        if(adjustmentModify.isChecked()){
            addSecurityProfileModel.setModifyAdjustments("Y");
        }else{
            addSecurityProfileModel.setModifyAdjustments("N");
        }

        if(modifyDiscount.isChecked()){
            addSecurityProfileModel.setModifyDiscounts("Y");
        }else{
            addSecurityProfileModel.setModifyDiscounts("N");
        }

        if(childCloseout.isChecked()){
            addSecurityProfileModel.setChildCloseout("Y");
        }else{
            addSecurityProfileModel.setChildCloseout("N");
        }

        if(generateInvoice.isChecked()){
            addSecurityProfileModel.setGenerateInvoice("Y");
        }else{
            addSecurityProfileModel.setGenerateInvoice("N");
        }

        if(viewpayments.isChecked()){
            addSecurityProfileModel.setPayment("Y");
        }else{
            addSecurityProfileModel.setPayment("N");
        }

        if(modifyPayments.isChecked()){
            addSecurityProfileModel.setModifyPayments("Y");
        }else{
            addSecurityProfileModel.setModifyPayments("N");
        }

        if(paymentType.isChecked()){
            addSecurityProfileModel.setPaymentTypes("Y");
        }else{
            addSecurityProfileModel.setPaymentTypes("N");
        }

        if(generalSetting.isChecked()){
            addSecurityProfileModel.setGeneralSetup("Y");
        }else{
            addSecurityProfileModel.setGeneralSetup("N");
        }

        if(securityProfile.isChecked()){
            addSecurityProfileModel.setSecurity("Y");
        }else{
            addSecurityProfileModel.setSecurity("N");
        }

        if(lookup.isChecked()){
            addSecurityProfileModel.setLookup("Y");
        }else{
            addSecurityProfileModel.setLookup("N");
        }

        if(programs.isChecked()){
            addSecurityProfileModel.setClassSetup("Y");
        }else{
            addSecurityProfileModel.setClassSetup("N");
        }

        if(immunization.isChecked()){
            addSecurityProfileModel.setImmunization("Y");
        }else{
            addSecurityProfileModel.setImmunization("N");
        }

        if(viewMsgs.isChecked()){
            addSecurityProfileModel.setMessage("Y");
        }else{
            addSecurityProfileModel.setMessage("N");
        }

        if(sendMsgs.isChecked()){
            addSecurityProfileModel.setSendmessage("Y");
        }else{
            addSecurityProfileModel.setSendmessage("N");
        }

        if(viewReport.isChecked()){
            addSecurityProfileModel.setReport("Y");
        }else{
            addSecurityProfileModel.setReport("N");
        }

        Call<AddSecurityProfileResponse> call= api.add_security_profile("0",addSecurityProfileModel);
        call.enqueue(new Callback<AddSecurityProfileResponse>() {
            @Override
            public void onResponse(Call<AddSecurityProfileResponse> call, Response<AddSecurityProfileResponse> response) {
                if (response.code()==200) {
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddSecurityProfile.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddSecurityProfile.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddSecurityProfile.this, SecurityProfile.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }
                    else{
                        Utils.showAlertDialog(AddSecurityProfile.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddSecurityProfileResponse> call, Throwable t) {
                Toast.makeText(AddSecurityProfile.this, "Fail", Toast.LENGTH_SHORT).show();
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

//            Toast.makeText(AddSecurityProfile.this, "Please select atleast one Attribute", Toast.LENGTH_SHORT).show();
            Utils.showAlertDialog(AddSecurityProfile.this,"Please select atleast one Attribute", false);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(AddSecurityProfile.this,SecurityProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
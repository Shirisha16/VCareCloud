package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.BasePackagesModel;
import com.client.vcarecloud.models.DiscountListModel;
import com.client.vcarecloud.models.UpdateDiscountModel;
import com.client.vcarecloud.models.UpdateDiscountResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateDiscount extends AppCompatActivity {
    ImageView back;
    TextInputEditText discountText,valueText,descriptionText,startDateText,endDateText;
//    MaterialTextView startDateText,endDateText;
    MaterialCheckBox statusBox,limitedPeriod,basePackage,additionalCharge,activityCharge,
                     campCharge,all;
    AppCompatButton editButton;
    RelativeLayout progress_layout;

    UserDetails userDetails;
    String message,error;

    boolean dateChecked = false;

    String  id,empid,custId,discountId,discount,discountType,value,status,limited,from,to,
            basePackage1,additionalCharge1,activityCharge1,campCharge1,all1,description;

    DiscountListModel discountListModel;
    UpdateDiscountModel updateDiscountModel;
    UpdateDiscountResponse updateDiscountResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_discount);

        back = findViewById(R.id.back);
        discountText = findViewById(R.id.discount);
        valueText = findViewById(R.id.value);
        statusBox = findViewById(R.id.status);
        limitedPeriod = findViewById(R.id.limited);
        startDateText = findViewById(R.id.startDate);
        endDateText = findViewById(R.id.endDate);
        basePackage = findViewById(R.id.basePackage);
        additionalCharge = findViewById(R.id.additionalCharges);
        activityCharge = findViewById(R.id.activityCharge);
        campCharge = findViewById(R.id.campCharge);
        all = findViewById(R.id.allChildren);
        descriptionText = findViewById(R.id.description);
        editButton = findViewById(R.id.edit);
        progress_layout = findViewById(R.id.progress);

        discountListModel=new DiscountListModel();
        updateDiscountModel=new UpdateDiscountModel();
        updateDiscountResponse=new UpdateDiscountResponse();

        userDetails=new UserDetails(UpdateDiscount.this);

        discountListModel=(DiscountListModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateDiscount.this,Discounts.class);
                intent.putExtra("list",discountListModel);
                startActivity(intent);
                finish();
            }
        });

        id=getIntent().getStringExtra("Id");
        empid=getIntent().getStringExtra("empId");
        discountId=getIntent().getStringExtra("discountId");
        custId=getIntent().getStringExtra("custId");
        discount=getIntent().getStringExtra("discountName");
        description=getIntent().getStringExtra("discountDescription");
        discountType=getIntent().getStringExtra("discountType");
        value=getIntent().getStringExtra("discountValue");
        status=getIntent().getStringExtra("discountStatus");
        limited=getIntent().getStringExtra("checkLimitedPeriod");
        from=getIntent().getStringExtra("limitedPeriodFromDate");
        to=getIntent().getStringExtra("limitedPeriodToDate");
        basePackage1=getIntent().getStringExtra("applicable_BasePackage");
        additionalCharge1=getIntent().getStringExtra("applicable_AdditionalCharge");
        campCharge1=getIntent().getStringExtra("applicable_Camp");
        activityCharge1=getIntent().getStringExtra("applicable_Activity");
        all1=getIntent().getStringExtra("applicable_All");

        discountText.setText(discount);
        valueText.setText(value);

        if(status.equalsIgnoreCase("y")){
            statusBox.setChecked(true);
        }else{
            statusBox.setChecked(false);
        }

        if(limited.equalsIgnoreCase("y")){
            limitedPeriod.setChecked(true);
//            startDateText.setVisibility(View.VISIBLE);
//            endDateText.setVisibility(View.VISIBLE);
        }else{
            limitedPeriod.setChecked(false);
        }

        if(from.equalsIgnoreCase("null")){
            startDateText.setText("");
        }else{
            startDateText.setText(from);
        }

        if(to.equalsIgnoreCase("null")) {
            endDateText.setText("");
        }else {
            endDateText.setText(to);
        }

        if(basePackage1.equalsIgnoreCase("y")){
            basePackage.setChecked(true);
        }else{
            basePackage.setChecked(false);
        }

        if(additionalCharge1.equalsIgnoreCase("y")){
            additionalCharge.setChecked(true);
        }else{
            additionalCharge.setChecked(false);
        }

        if(activityCharge1.equalsIgnoreCase("y")){
            activityCharge.setChecked(true);
        }

        if(campCharge1.equalsIgnoreCase("y")){
            campCharge.setChecked(true);
        }

        if(all1.equalsIgnoreCase("y")){
            all.setChecked(true);
        }

        if(description.equalsIgnoreCase("null")){
            descriptionText.setText("");
        }else{
            descriptionText.setText(description);
        }

        if(limitedPeriod.isChecked()==true) {
            datesMethod();

            limitedPeriod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(limitedPeriod.isChecked()) {
                        datesMethod();
                    }else{
                        startDateText.getText().clear();
                        endDateText.getText().clear();
                    }
                }
            });
        }else{
            limitedPeriod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(limitedPeriod.isChecked()) {
                        datesMethod();
                    }else{
                        startDateText.getText().clear();
                        endDateText.getText().clear();
                    }
                }
            });
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discount=discountText.getText().toString();
                value=valueText.getText().toString();
                status=statusBox.getText().toString();
                limited=limitedPeriod.getText().toString();
                from=startDateText.getText().toString();
                to=endDateText.getText().toString();
                basePackage1=basePackage.getText().toString();
                additionalCharge1=additionalCharge.getText().toString();
                activityCharge1=activityCharge.getText().toString();
                campCharge1=campCharge.getText().toString();
                all1=all.getText().toString();
                description=descriptionText.getText().toString();

                if (Validation()) {
                    updateDiscount();
                }
            }
        });
    }

    private void datesMethod() {
        startDateText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateDiscount.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                startDateText.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        endDateText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateDiscount.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                String date = day1 + "-" + month1 + "-" + year1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                endDateText.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });
    }

    private void updateDiscount() {
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
        updateDiscountModel.setId(id);
        updateDiscountModel.setEmpid(empid);
        updateDiscountModel.setDiscountId(discountId);
        updateDiscountModel.setCustId(custId);
        updateDiscountModel.setDiscountName(discount);
        updateDiscountModel.setDescription(description);
        updateDiscountModel.setValue(value);

        if(statusBox.isChecked()){
            updateDiscountModel.setStatus("Y");
        }else{
            updateDiscountModel.setStatus("N");
        }

        if(limitedPeriod.isChecked()){
            updateDiscountModel.setCheckLimitedPeriod("Y");
//            String[] parts = from.split("T");
//            String daystartDate = parts[0];
//            if(from!=null){
                updateDiscountModel.setFrom(from);
//            }

//            String[] partsEnd = to.split("T");
//            String dayendDate = partsEnd[0];
//            if(to!=null){
                updateDiscountModel.setTo(to);
//            }
        }else{
            updateDiscountModel.setCheckLimitedPeriod("N");
        }

        if(basePackage.isChecked()){
            updateDiscountModel.setBasePackage("Y");
        }else{
            updateDiscountModel.setBasePackage("N");
        }

        if(additionalCharge.isChecked()){
            updateDiscountModel.setAdditionalCharge("Y");
        }else{
            updateDiscountModel.setAdditionalCharge("N");
        }

        if(campCharge.isChecked()){
            updateDiscountModel.setCamp("Y");
        }else{
            updateDiscountModel.setCamp("N");
        }

        if(activityCharge.isChecked()){
            updateDiscountModel.setActivity("Y");
        }else{
            updateDiscountModel.setActivity("N");
        }

        if(all.isChecked()){
            updateDiscountModel.setAll("Y");
        }else{
            updateDiscountModel.setAll("N");
        }

        Call<UpdateDiscountResponse> call=api.update_discount(id,"0",updateDiscountModel);
        call.enqueue(new Callback<UpdateDiscountResponse>() {
            @Override
            public void onResponse(Call<UpdateDiscountResponse> call, Response<UpdateDiscountResponse> response) {
               if (response.code()==200){
                    if (response.body().getMessage()!=null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdateDiscount.this != null && !UpdateDiscount.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateDiscount.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateDiscount.this, Discounts.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateDiscount.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateDiscountResponse> call, Throwable t) {
                Toast.makeText(UpdateDiscount.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateDiscount.this,Discounts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean Validation() {
        if(!discountname()){
            return false;
        }else if(!discountValue()){
            return false;
        }else if(!applicationOn()){
            return false;
        }else if(!limitedPeriod1()){
            return false;
        }else{
            return true;
        }
    }

    private boolean discountname() {
        String str = discountText.getText().toString();
        if (discountText.getText().toString().isEmpty()) {
            discountText.setError("Discount Name should not be empty");
            discountText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            discountText.setText(discountText.getText().toString().trim());
            discountText.setSelection(discountText.getText().length());
            return false;
        }else {
            return true;
        }
    }

    private boolean discountValue() {
        String str = valueText.getText().toString();
        if (valueText.getText().toString().isEmpty()) {
            valueText.setError("Discount value should not be empty");
            valueText.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            valueText.setText(valueText.getText().toString().trim());
            valueText.setSelection(valueText.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            valueText.setError("Discount should not start with point");
            valueText.setText(valueText.getText().toString().trim());
            valueText.setSelection(valueText.getText().length());
            return false;
        } else{
            return true;
        }
    }

    private boolean applicationOn() {
        if(basePackage.isChecked()==false && additionalCharge.isChecked()== false &&
                activityCharge.isChecked()==false && campCharge.isChecked()==false &&
                all.isChecked()==false){
            Utils.showAlertDialog(UpdateDiscount.this,"Please select atleast one attribute for applicable on",false);
            return false;
        }else {
            return true;
        }
    }
    private boolean limitedPeriod1() {
        if(limitedPeriod.isChecked() && startDateText.getText().toString().isEmpty() && endDateText.getText().toString().isEmpty()) {
//            if(startDateText.getText().toString().isEmpty() && endDateText.getText().toString().isEmpty()){
//            startDateText.setError("From date should not be empty");
//            startDateText.requestFocus();
            Utils.showAlertDialog(UpdateDiscount.this,"Please enter From and To dates",false);
            return false;
        }else if(limitedPeriod.isChecked() && startDateText.getText().toString().isEmpty()){
            Utils.showAlertDialog(UpdateDiscount.this,"Please select from date",false);
            return false;
        }else if(limitedPeriod.isChecked() && endDateText.getText().toString().isEmpty()){
            Utils.showAlertDialog(UpdateDiscount.this,"Please select to date",false);
            return false;
        }else if(limitedPeriod.isChecked()==false && startDateText.getText().toString().length()>0 && endDateText.getText().toString().length()>0){
            Utils.showAlertDialog(UpdateDiscount.this,"Please select Limited period",false);
            return false;
        }else if(limitedPeriod.isChecked()==false && startDateText.getText().toString().length()>0){
            Utils.showAlertDialog(UpdateDiscount.this,"Please select Limited period",false);
            return false;
        }else if(limitedPeriod.isChecked()==false && endDateText.getText().toString().length()>0){
            Utils.showAlertDialog(UpdateDiscount.this,"Please select Limited period",false);
            return false;
        } else {
            return true;
        }
    }
}
package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddDiscountModel;
import com.client.vcarecloud.models.AddDiscountResponse;
import com.client.vcarecloud.models.DiscountListModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddDiscounts extends AppCompatActivity {
    ImageView back;
    TextInputEditText discountText,valueText,descriptionText,startDateText,endDateText;
//    MaterialTextView startDateText,endDateText;
    MaterialCheckBox statusBox,limitedPeriod,basePackage,additionalCharge,activityCharge,
                     campCharge,all;

    AppCompatButton addButton;
    RelativeLayout progress_layout;

    String discount,discountType,value,status,limited,from,to,basePackage1,additionalCharge1,
            activityCharge1,campCharge1,all1,description,custId,message,errorMessage;

    UserDetails userDetails;
    AddDiscountModel addDiscountModel;
    DiscountListModel discountModel;

    boolean dateChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_discounts);

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
        addButton = findViewById(R.id.add);
        progress_layout = findViewById(R.id.progress);

        userDetails = new UserDetails(AddDiscounts.this);

        addDiscountModel = new AddDiscountModel();
        discountModel = new DiscountListModel();

        custId = userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDiscounts.this, Discounts.class);
                startActivity(intent);
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                discount = discountText.getText().toString();
                value = valueText.getText().toString();
                status = statusBox.getText().toString();
                limited = limitedPeriod.getText().toString();
                from = startDateText.getText().toString();
                to = endDateText.getText().toString();
                basePackage1 = basePackage.getText().toString();
                additionalCharge1 = additionalCharge.getText().toString();
                activityCharge1 = activityCharge.getText().toString();
                campCharge1 = campCharge.getText().toString();
                all1 = all.getText().toString();
                description = descriptionText.getText().toString();

                if (Validation()) {
                    addDiscounts();
                }
            }
        });

        limitedPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                if (limitedPeriod.isChecked()) {
//                    startDateText.setVisibility(View.VISIBLE);
//                    endDateText.setVisibility(View.VISIBLE);
//                }else{
//                    startDateText.setVisibility(View.GONE);
//                    endDateText.setVisibility(View.GONE);
//                }

                    startDateText.setOnClickListener(v -> {
                        Calendar calendar = Calendar.getInstance();
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);
                        final int month = calendar.get(Calendar.MONTH);
                        final int year = calendar.get(Calendar.YEAR);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscounts.this, (view, year1, month1, day1) -> {
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
//                        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                        datePickerDialog.getDatePicker();
                        datePickerDialog.show();
                    });

                    endDateText.setOnClickListener(v -> {
                        Calendar calendar = Calendar.getInstance();
                        final int day = calendar.get(Calendar.DAY_OF_MONTH);
                        final int month = calendar.get(Calendar.MONTH);
                        final int year = calendar.get(Calendar.YEAR);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(AddDiscounts.this, (view, year1, month1, day1) -> {
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
//                        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
                        datePickerDialog.getDatePicker();
                        datePickerDialog.show();
                    });
                }
            }
        });

    }

    private void addDiscounts() {
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
        AddDiscountModel addDiscountModel=new AddDiscountModel();

        addDiscountModel.setCustId(custId);
        addDiscountModel.setDiscountName(discount);
        addDiscountModel.setDescription(description);
        addDiscountModel.setDiscountType(discountType);
        addDiscountModel.setValue(value);

        if(statusBox.isChecked()){
            addDiscountModel.setStatus("Y");
        }else{
            addDiscountModel.setStatus("N");
        }

        if(limitedPeriod.isChecked()) {
            addDiscountModel.setLimited("Y");
            addDiscountModel.setFrom(from);
            addDiscountModel.setTo(to);

        }
        else{
            addDiscountModel.setLimited("N");
        }

        if(basePackage.isChecked()){
            addDiscountModel.setBasePackage("Y");
        }else{
            addDiscountModel.setBasePackage("N");
        }

        if(additionalCharge.isChecked()){
            addDiscountModel.setAdditionalCharge("Y");
        }else{
            addDiscountModel.setAdditionalCharge("N");
        }

        if(campCharge.isChecked()){
            addDiscountModel.setCamp("Y");
        }else{
            addDiscountModel.setCamp("N");
        }

        if(activityCharge.isChecked()){
            addDiscountModel.setActivity("Y");
        }else{
            addDiscountModel.setActivity("N");
        }

        if(all.isChecked()){
            addDiscountModel.setAll("Y");
        }else{
            addDiscountModel.setAll("N");
        }

        Call<AddDiscountResponse> call= api.add_discount("0",addDiscountModel);
        call.enqueue(new Callback<AddDiscountResponse>() {
            @Override
            public void onResponse(Call<AddDiscountResponse> call, Response<AddDiscountResponse> response) {
                if (response.code()==200) {
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddDiscounts.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddDiscounts.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddDiscounts.this, Discounts.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddDiscounts.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddDiscountResponse> call, Throwable t) {
                Toast.makeText(AddDiscounts.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean Validation() {
        if(!discountname()){
            return false;
        }else if(!discountValue()){
            return false;
        } else if(!limitedPeriod1()){
            return false;
        }
//        else if(!date()){
//            return false;
//        }
        else if(!applicationOn()){
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

    private boolean limitedPeriod1() {
        if(limitedPeriod.isChecked() && startDateText.getText().toString().isEmpty() && endDateText.getText().toString().isEmpty()) {
//            if(startDateText.getText().toString().isEmpty() && endDateText.getText().toString().isEmpty()){
//            startDateText.setError("From date should not be empty");
//            startDateText.requestFocus();
            Utils.showAlertDialog(AddDiscounts.this,"Please enter From and To dates",false);
            return false;
        }else if(limitedPeriod.isChecked() && startDateText.getText().toString().isEmpty()){
            Utils.showAlertDialog(AddDiscounts.this,"Please select from date",false);
            return false;
        }else if(limitedPeriod.isChecked() && endDateText.getText().toString().isEmpty()){
            Utils.showAlertDialog(AddDiscounts.this,"Please select to date",false);
            return false;
        }else if(limitedPeriod.isChecked()==false && startDateText.getText().toString().length()>0 && endDateText.getText().toString().length()>0){
            Utils.showAlertDialog(AddDiscounts.this,"Please select Limited period",false);
            return false;
        }else if(limitedPeriod.isChecked()==false && startDateText.getText().toString().length()>0){
            Utils.showAlertDialog(AddDiscounts.this,"Please select Limited period",false);
            return false;
        }else if(limitedPeriod.isChecked()==false && endDateText.getText().toString().length()>0){
            Utils.showAlertDialog(AddDiscounts.this,"Please select Limited period",false);
            return false;
        }  else {
                return true;
        }
    }

    private boolean  date(){
        if(startDateText.getText().toString().isEmpty() && endDateText.getText().toString().isEmpty()){
//            startDateText.setError("Date should not be empty");
            Utils.showAlertDialog(AddDiscounts.this,"Date should not be empty",false);
            return false;
        }else{
            return true;
        }
    }

    private boolean applicationOn() {
        if(basePackage.isChecked()==false && additionalCharge.isChecked()== false &&
           activityCharge.isChecked()==false && campCharge.isChecked()==false &&
           all.isChecked()==false){

         Utils.showAlertDialog(AddDiscounts.this,"Please select atleast one attribute for applicable on",false);
         return false;
        }else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(AddDiscounts.this,Discounts.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
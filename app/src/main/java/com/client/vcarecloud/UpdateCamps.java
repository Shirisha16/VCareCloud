package com.client.vcarecloud;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.CampModel;
import com.client.vcarecloud.models.UpdateCampModel;
import com.client.vcarecloud.models.UpdateCampResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateCamps extends AppCompatActivity {
    TextInputEditText camp,charges,campDetail,from,to;
    AutoCompleteTextView className_spin,tax_spin;
    AppCompatButton updateButton;
    ImageView back;
    RelativeLayout progressLayout;
    TextView clearTax;

    boolean dateChecked = false;

    String campid,custId,campName,fromDate,toDate,className,classId,charge,tax,taxid,
            taxStatus,taxID,taxName, campDetails,message,error;
    String classID;

    ArrayList<String> class_Name = new ArrayList<>();
    ArrayList<String> class_Id = new ArrayList<>();

    ArrayList<String> taxNameList = new ArrayList<>();
    ArrayList<String> taxIdList = new ArrayList<>();

    UserDetails userDetails;

    Camps camps;
    CampModel campModel;
    UpdateCampModel updateCampModel;
    UpdateCampResponse updateCampResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_camps);

        camp = findViewById(R.id.camp);
        from = findViewById(R.id.from);
        to = findViewById(R.id.to);
        className_spin = findViewById(R.id.class_spin);
        charges = findViewById(R.id.charge);
        tax_spin = findViewById(R.id.tax_spin);
        campDetail = findViewById(R.id.campDetails);
        updateButton = findViewById(R.id.update);
        back = findViewById(R.id.back);
        progressLayout = findViewById(R.id.progress);
        clearTax=findViewById(R.id.clear_text);

        camps=new Camps();
        campModel=new CampModel();

        updateCampResponse=new UpdateCampResponse();
        updateCampModel=new UpdateCampModel();

        userDetails=new UserDetails(UpdateCamps.this);
        campModel=(CampModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(UpdateCamps.this,Camps.class);
//                intent.putExtra("list",campModel);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        clearTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tax_spin.getText().clear();
            }
        });

        campid=getIntent().getStringExtra("campId");
        custId=getIntent().getStringExtra("custId");
        campName=getIntent().getStringExtra("campName");
        campDetails=getIntent().getStringExtra("campDetails");
        className=getIntent().getStringExtra("className");

        fromDate=getIntent().getStringExtra("campStartDate");
        if (fromDate!=null){
            fromDate=fromDate.replace("T00:00:00","");
        }

        toDate=getIntent().getStringExtra("campEndDate");
        if (toDate!=null){
            toDate=toDate.replace("T00:00:00","");
        }

        classId=getIntent().getStringExtra("classId");
        charge=getIntent().getStringExtra("campCharge");
        taxid=getIntent().getStringExtra("taxesId");
        tax=getIntent().getStringExtra("taxName");

        camp.setText(campName);
        from.setText(fromDate);
        to.setText(toDate);
        className_spin.setText(className);
        charges.setText(charge);

        if(taxid.equalsIgnoreCase("null")){
            tax_spin.setText("");
        }else {
            tax_spin.setText(tax);
        }

        if (campDetails.equalsIgnoreCase("null")){
            campDetail.setText("");
        }else{
            campDetail.setText(campDetails);
        }

        getClassList();
        getTaxList();

        from.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateCamps.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                from.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker();
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        to.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateCamps.this, (view,year1, month1, day1) -> {
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
                to.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker();
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campName=camp.getText().toString();
                fromDate=from.getText().toString();
                toDate=to.getText().toString();
                className=className_spin.getText().toString();
                charge=charges.getText().toString();
                tax=tax_spin.getText().toString();
                campDetails=campDetail.getText().toString();

                if (Validate()) {
                    updateCampDetails();
                }
            }
        });

    }

    private void getClassList() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        Call<String> call = api.class_dropdown(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressLayout.setVisibility(View.GONE);

                if (response.body()!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            classID = jsonObject1.getString("classId");
                            className = jsonObject1.getString("className");
                            class_Id.add(classID);
                            class_Name.add(className);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(UpdateCamps.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                        className_spin.setAdapter(adapter);

                        className_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                classId=class_Id.get(i);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressLayout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateCamps.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void getTaxList() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();
        VcareApi api = retrofit.create(VcareApi.class);
        Call<String> call=api.taxData(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
//                    taxNameList.add("--Select--");
//                    getString(R.string.prompt);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            taxID=jsonObject1.getString("taxesId");
                            taxName=jsonObject1.getString("taxName");
                            taxStatus=jsonObject1.getString("taxStatus");
                            if(taxStatus.equalsIgnoreCase("true")) {
                                taxIdList.add(taxID);
                                taxNameList.add(taxName);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressLayout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateCamps.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(UpdateCamps.this, android.R.layout.simple_dropdown_item_1line, taxNameList);
        tax_spin.setAdapter(adapter);

        tax_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                taxid = taxIdList.get(i);
            }
        });
    }

    private void updateCampDetails() {
        progressLayout.setVisibility(View.VISIBLE);

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
        updateCampModel.setId(campid);
        updateCampModel.setCustId(custId);
        updateCampModel.setCampName(campName);
        updateCampModel.setCampDetails(campDetails);
        updateCampModel.setStartDate(fromDate);
        updateCampModel.setEndDate(toDate);
        updateCampModel.setClassId(classId);
        updateCampModel.setClassName(className);
        updateCampModel.setCampCharge(charge);

        if(tax.equalsIgnoreCase("--Select--") || tax.equalsIgnoreCase("")){
            updateCampModel.setClassId(classId);
            updateCampModel.setTaxesId(null);
            updateCampModel.setTaxname(null);
//            Toast.makeText(UpdateCamps.this, ""+classId+taxid, Toast.LENGTH_SHORT).show();

        }else {
            updateCampModel.setTaxesId(taxid);
            updateCampModel.setTaxname(tax);
        }

        Call<UpdateCampResponse> call=api.update_camps(campid,"0",updateCampModel);
        call.enqueue(new Callback<UpdateCampResponse>() {
            @Override
            public void onResponse(Call<UpdateCampResponse> call, Response<UpdateCampResponse> response) {
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        error=response.body().getErrorMessage();
                        if (UpdateCamps.this!=null&&!UpdateCamps.this.isFinishing()){
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(UpdateCamps.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateCamps.this, Camps.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateCamps.this,response.body().getErrorMessage(),false);
                    }
                }
                progressLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateCampResponse> call, Throwable t) {
                Toast.makeText(UpdateCamps.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(UpdateCamps.this,Camps.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }
    private boolean Validate() {
        if(!camp_name()){
            return false;
        }else if (!from_date()) {
            return false;
        } else if (!to_date()){
            return false;
        } else if(!chargecamp()){
            return false;
        }else if (!classname()) {
            return false;
        }
//        else if(!tax_name()){
//            return false;
//        }
        else{
            return true;
        }
    }

    private boolean tax_name() {
        if (tax_spin.getText().toString().equalsIgnoreCase("--Select--")) {
            Utils.showAlertDialog(UpdateCamps.this,"Please select Tax",false);
            return false;
        } else {
            return true;
        }
    }

    private boolean to_date() {
        if (to.getText().toString().isEmpty()) {
            to.setError("To date should not be empty");
            to.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean camp_name() {
        String str = camp.getText().toString();
        if(camp.getText().toString().isEmpty()){
            camp.setError("Camp name should not be empty");
            camp.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            camp.setText(camp.getText().toString().trim());
            camp.setSelection(camp.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean from_date() {
        if (from.getText().toString().isEmpty()) {
            from.setError("From date should not be empty");
            from.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean chargecamp() {
        String str = charges.getText().toString();
        if(charges.getText().toString().isEmpty()){
            charges.setError("Charge should not be empty");
            charges.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            charges.setText(charges.getText().toString().trim());
            charges.setSelection(charges.getText().length());
            return false;
        } else if(str.length() > 0 && str.startsWith(".")) {
            charges.setError("Charge should not be point");
            charges.setText(charges.getText().toString().trim());
            charges.setSelection(charges.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean classname() {
        if(className_spin.getText().toString().isEmpty()){
            className_spin.setError("Class should not be empty");
            className_spin.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}

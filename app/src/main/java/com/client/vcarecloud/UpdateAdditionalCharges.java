package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AdditionalChargeListModel;
import com.client.vcarecloud.models.UpdateAdditionalChargesModel;
import com.client.vcarecloud.models.UpdateAdditionalChargesResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateAdditionalCharges extends AppCompatActivity {
    ImageView back;
    TextInputEditText chargeText, amountText, descriptionText, dateText;
    TextView clearTax;
    RadioGroup applicationOn;
    RadioButton classname, childName;
    AutoCompleteTextView tax_spin, applicationType_spin;
    AppCompatButton editButton;
    RelativeLayout progress_layout;

//    Spinner spinner;

    UserDetails userDetails;
    String message, error;

    String empid = "0", chargeId, custId, chargeName, description, chargeAmount, taxesId,
            taxname, chargeDate, applicableType, classid, className, childid, childname,
            refid,taxstatus, taxName,taxId, classID,classNameSpin,childID,childnameSpin;

    String[] tax={"--Select--"};
    UpdateAdditionalChargesModel updateAdditionalChargesModel;
    UpdateAdditionalChargesResponse updateAdditionalChargesResponse;
    AdditionalCharges additionalCharges;
    AdditionalChargeListModel additionalChargeListModel;

    boolean dateChecked = false;

    ArrayList<String> tax_id = new ArrayList<>();
    ArrayList<String> tax_name = new ArrayList<>();

    ArrayList<String> class_Name = new ArrayList<>();
    ArrayList<String> class_Id = new ArrayList<>();

    ArrayList<String> child_name = new ArrayList<>();
    ArrayList<String> child_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_additional_charges);

        back = findViewById(R.id.back);
        chargeText = findViewById(R.id.chargename1);
        dateText = findViewById(R.id.date1);
        amountText = findViewById(R.id.amount1);
        descriptionText = findViewById(R.id.note);
        classname = findViewById(R.id.class1);
        childName = findViewById(R.id.child);
        applicationOn = findViewById(R.id.radio);
        applicationType_spin = findViewById(R.id.class_spin);
        tax_spin = findViewById(R.id.tax_spin);
        editButton = findViewById(R.id.edit);
        progress_layout = findViewById(R.id.progress);
        clearTax=findViewById(R.id.clear_text);

//        spinner=findViewById(R.id.spinner);
//        spinner.setOnItemClickListener((AdapterView.OnItemClickListener) this);



        userDetails = new UserDetails(UpdateAdditionalCharges.this);

        additionalCharges = new AdditionalCharges();
        additionalChargeListModel = new AdditionalChargeListModel();
        updateAdditionalChargesModel = new UpdateAdditionalChargesModel();
        updateAdditionalChargesResponse = new UpdateAdditionalChargesResponse();

        additionalChargeListModel = (AdditionalChargeListModel) getIntent().getSerializableExtra("list");

        custId = userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UpdateAdditionalCharges.this, AdditionalCharges.class);
//                intent.putExtra("list", additionalChargeListModel);
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

        empid = getIntent().getStringExtra("0");
        chargeId = getIntent().getStringExtra("additionalChargeId");
        custId = getIntent().getStringExtra("custId");
        chargeName = getIntent().getStringExtra("chargeName");
        description = getIntent().getStringExtra("ChargeDescription");
        chargeAmount = getIntent().getStringExtra("chargeAmount");
        taxesId = getIntent().getStringExtra("taxesId");
        taxname = getIntent().getStringExtra("taxName");
        chargeDate = getIntent().getStringExtra("chargeDate");
        if (chargeDate != null) {
            chargeDate = chargeDate.replace("T00:00:00", "");
        }

        applicableType = getIntent().getStringExtra("applicableType");
        refid= getIntent().getStringExtra("refApplicableID");
        classid = getIntent().getStringExtra("Applicableonclass");
        className = getIntent().getStringExtra("className");
        childid = getIntent().getStringExtra("Applicableonchild");
        childname = getIntent().getStringExtra("childName");

        chargeText.setText(chargeName);
        amountText.setText(chargeAmount);

        if (taxname.equalsIgnoreCase("null")) {
            tax_spin.setText("");
        } else {
            tax_spin.setText(taxname);
        }

        if (description.equalsIgnoreCase("null") || description.equalsIgnoreCase("")){
            descriptionText.setText("");
        }else{
            descriptionText.setText(description);
        }

        dateText.setText(chargeDate);

        if (applicableType.equalsIgnoreCase("Child")) {
            childName.setChecked(true);
        } else {
            classname.setChecked(true);
        }

        if (childName.isChecked()) {
            applicationType_spin.setText(childname);
        } else {
            applicationType_spin.setText(className);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeName = chargeText.getText().toString();
                chargeDate = dateText.getText().toString();

                if (classname.isChecked()) {
                    applicableType = classname.getText().toString();
                } else {
                    applicableType = childName.getText().toString();
                }

                chargeAmount = amountText.getText().toString();
                taxname = tax_spin.getText().toString();
                description = descriptionText.getText().toString();

                if (Validate()) {
                    updateAdditionalCharges();
                }
            }
        });

        getTaxList();

        dateText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAdditionalCharges.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                dateText.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        childName.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                applicationType_spin.getText().clear();
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
                Call<String> call = api.child_dropdown(userDetails.getCustId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body() != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                JSONArray jsonArray = jsonObject.getJSONArray("model");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    childID = jsonObject1.getString("childId");
                                    childnameSpin = jsonObject1.getString("displayName");
                                    child_id.add(childID);
                                    child_name.add(childnameSpin);
                                }
                                ArrayAdapter adapter = new ArrayAdapter(UpdateAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, child_name);
                                applicationType_spin.setAdapter(adapter);

                                applicationType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String selected = (String) adapterView.getItemAtPosition(i);
                                        childid = child_id.get(i);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progress_layout.setVisibility(View.GONE);
                        String message = "";
                        if (t instanceof UnknownHostException) {
                            message = "No internet connection!";
                        } else {
                            message = "Something went wrong! try again";
                        }
                        Toast.makeText(UpdateAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        classname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applicationType_spin.getText().clear();
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
                        if (response.body() != null) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.body());
                                JSONArray jsonArray = jsonObject.getJSONArray("model");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    classID = jsonObject1.getString("classId");
                                    classNameSpin = jsonObject1.getString("className");
                                    class_Id.add(classID);
                                    class_Name.add(classNameSpin);
                                }

                                ArrayAdapter adapter = new ArrayAdapter(UpdateAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                                applicationType_spin.setAdapter(adapter);
                                applicationType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        String selected = (String) adapterView.getItemAtPosition(i);
                                        classid = class_Id.get(i);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progress_layout.setVisibility(View.GONE);
                        String message = "";
                        if (t instanceof UnknownHostException) {
                            message = "No internet connection!";
                        } else {
                            message = "Something went wrong! try again";
                        }
                        Toast.makeText(UpdateAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

   }

    private void updateAdditionalCharges() {
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
        updateAdditionalChargesModel.setEmpid("0");
        updateAdditionalChargesModel.setCustId(custId);
        updateAdditionalChargesModel.setChargeId(chargeId);
        updateAdditionalChargesModel.setChargeName(chargeName);
        updateAdditionalChargesModel.setDescription(description);
        updateAdditionalChargesModel.setDate(chargeDate);


        if(taxname.equalsIgnoreCase("--Select--") || tax_spin.getText().toString().isEmpty()){
            updateAdditionalChargesModel.setTaxId(null);
            updateAdditionalChargesModel.setTaxName(null);
        }else {
            updateAdditionalChargesModel.setTaxId(taxesId);
            updateAdditionalChargesModel.setTaxName(taxname);
        }

        updateAdditionalChargesModel.setApplicableType(applicableType);

        if(applicableType.equalsIgnoreCase("Child")){
            updateAdditionalChargesModel.setChildId(childid);
        }else {
            updateAdditionalChargesModel.setClassid(classid);
        }
        updateAdditionalChargesModel.setAmount(chargeAmount);

        Call<UpdateAdditionalChargesResponse> call=api.update_additionalCharge(chargeId,"0",updateAdditionalChargesModel);
        call.enqueue(new Callback<UpdateAdditionalChargesResponse>() {
            @Override
            public void onResponse(Call<UpdateAdditionalChargesResponse> call, Response<UpdateAdditionalChargesResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdateAdditionalCharges.this != null && !UpdateAdditionalCharges.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateAdditionalCharges.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateAdditionalCharges.this, AdditionalCharges.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateAdditionalCharges.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateAdditionalChargesResponse> call, Throwable t) {
                Toast.makeText(UpdateAdditionalCharges.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getClassList() {
        applicationType_spin.getText().clear();
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
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            classid = jsonObject1.getString("classId");
                            className = jsonObject1.getString("className");
                            class_Id.add(classid);
                            class_Name.add(className);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(UpdateAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                        applicationType_spin.setAdapter(adapter);
                        applicationType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                classid = class_Id.get(i);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getChildList() {
        applicationType_spin.getText().clear();
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
        Call<String> call = api.child_dropdown(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            childid = jsonObject1.getString("childId");
                            childname = jsonObject1.getString("displayName");
                            child_id.add(childid);
                            child_name.add(childname);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(UpdateAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                        applicationType_spin.setAdapter(adapter);
                        applicationType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                childname = child_name.get(i);
                            }
                        });
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                        Toast.makeText(UpdateAdditionalCharges.this, ""+childname+className, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
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
        Call<String> call = api.taxData(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
//                    tax_name.add("--Select--");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            taxId = jsonObject1.getString("taxesId");
                            taxName = jsonObject1.getString("taxName");
                            taxstatus=jsonObject1.getString("taxStatus");

                            if(taxstatus.equalsIgnoreCase("true")){
                                tax_id.add(taxId);
                                tax_name.add(taxName);
                            }
                        }

                        ArrayAdapter adapter = new ArrayAdapter(UpdateAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line,tax_name);
                        tax_spin.setAdapter(adapter);

                        tax_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                taxesId = tax_id.get(i);
                            }
                        });

//                        ArrayAdapter adapter = new ArrayAdapter(UpdateAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line,tax);
//                        spinner.setAdapter(adapter);
//
//                        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                String selected = (String) adapterView.getItemAtPosition(i);
//                                taxesId = tax_id.get(i);
//                            }
//                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(UpdateAdditionalCharges.this,AdditionalCharges.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }

    private boolean Validate() {
        if (!charge_name()) {
            return false;
        } else if (!date1()) {
            return false;
        }else if (!applicationon()) {
            return false;
        }else if (!amount()) {
            return false;
        }
//        else if (!tax()) {
//            return false;
//        }
        else {
            return true;
        }
    }

    private boolean charge_name() {
        String str = chargeText.getText().toString();
        if (chargeText.getText().toString().isEmpty()) {
            chargeText.setError("Charge Name should not be empty");
            chargeText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            chargeText.setText(chargeText.getText().toString().trim());
            chargeText.setSelection(chargeText.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean date1() {
        if(dateText.getText().toString().isEmpty()){
            dateText.setError("Date should not be empty");
            dateText.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean applicationon() {
        String str = applicationType_spin.getText().toString();
        if (applicationType_spin.getText().toString().isEmpty()) {
            applicationType_spin.setError("Application on should not be empty");
            applicationType_spin.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            applicationType_spin.setText(applicationType_spin.getText().toString().trim());
            applicationType_spin.setSelection(applicationType_spin.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean amount() {
        String str = amountText.getText().toString();
        if (amountText.getText().toString().isEmpty()) {
            amountText.setError("Amount should not be empty");
            amountText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            amountText.setText(amountText.getText().toString().trim());
            amountText.setSelection(amountText.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            amountText.setError("Amount should not be point");
            amountText.setText(amountText.getText().toString().trim());
            amountText.setSelection(amountText.getText().length());
            return false;
        } else {
            return true;
        }
    }

//    private boolean tax() {
//        String str = tax_spin.getText().toString();
//        if (tax_spin.getText().toString().isEmpty()) {
//            tax_spin.setError("Tax should not be empty");
//            tax_spin.requestFocus();
//            return false;
//        } else if(str.length() > 0 && str.startsWith(" ")) {
//            tax_spin.setText(tax_spin.getText().toString().trim());
//            tax_spin.setSelection(tax_spin.getText().length());
//            return false;
//        } else {
//            return true;
//        }
//    }
}
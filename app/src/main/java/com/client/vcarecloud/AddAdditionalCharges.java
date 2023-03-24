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
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AdditionalChargeListModel;
import com.client.vcarecloud.models.AdditionalChargeResponse;
import com.client.vcarecloud.models.AdditionalChargesModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddAdditionalCharges extends AppCompatActivity {
    ImageView back;
    TextInputEditText chargeText,amountText,descriptionText, dateText;
    RadioGroup applicationOn;
    RadioButton classname,childName;
    AutoCompleteTextView tax_spin,applicationType_spin;
    AppCompatButton addButton;
    RelativeLayout progress_layout;

    AdditionalChargeListModel additionalChargeListModel;
    UserDetails userDetails;
    AdditionalChargesModel additionalChargesModel;

    String custId,chargeName,description,amount,taxesId,taxName,taxstatus,date,applicationType,
            classid,className,childid,childname;

    String message,errorMessage;
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
        setContentView(R.layout.activity_add_additional_charges);

        back=findViewById(R.id.back);
        chargeText=findViewById(R.id.chargename1);
        dateText=findViewById(R.id.date1);
        amountText=findViewById(R.id.amount1);
        descriptionText=findViewById(R.id.note);
        classname=findViewById(R.id.class1);
        childName=findViewById(R.id.child);
        applicationOn=findViewById(R.id.radio);
        applicationType_spin=findViewById(R.id.class_spin);
        tax_spin=findViewById(R.id.tax_spin);
        addButton=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);

        additionalChargeListModel=new AdditionalChargeListModel();
        additionalChargesModel=new AdditionalChargesModel();

        userDetails=new UserDetails(AddAdditionalCharges.this);

        custId=userDetails.getCustId();
        taxesId=userDetails.getTaxId();

        getTaxList();
//        getChildList();
//        getClassList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddAdditionalCharges.this, AdditionalCharges.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargeName = chargeText.getText().toString();
                date = dateText.getText().toString();
                if (classname.isChecked()) {
                    applicationType = classname.getText().toString();
                } else {
                    applicationType = childName.getText().toString();
                }

                amount = amountText.getText().toString();
                taxName = tax_spin.getText().toString();
                description = descriptionText.getText().toString();

                if (Validate()) {
                addAdditionalCharge();
            }
            }
        });

        dateText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAdditionalCharges.this, (view, year1, month1, day1) -> {
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
                        Call<String> call=api.child_dropdown(userDetails.getCustId());
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
                                        ArrayAdapter adapter = new ArrayAdapter(AddAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, child_name);
                                        applicationType_spin.setAdapter(adapter);

                                        applicationType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                String selected = (String) adapterView.getItemAtPosition(i);
                                                childid=child_id.get(i);
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
                                Toast.makeText(AddAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
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
                                    classid = jsonObject1.getString("classId");
                                    className = jsonObject1.getString("className");
                                    class_Id.add(classid);
                                    class_Name.add(className);
                                }

                                ArrayAdapter adapter = new ArrayAdapter(AddAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, class_Name);
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
                        Toast.makeText(AddAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
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
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            taxesId=jsonObject1.getString("taxesId");
                            taxName=jsonObject1.getString("taxName");
                            taxstatus=jsonObject1.getString("taxStatus");
                            if(taxstatus.equalsIgnoreCase("true")){
                                tax_id.add(taxesId);
                                tax_name.add(taxName);
                            }
                        }

                        ArrayAdapter adapter = new ArrayAdapter(AddAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, tax_name);
                        tax_spin.setAdapter(adapter);

                        tax_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                    taxesId = tax_id.get(i);
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
                Toast.makeText(AddAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

//    private void getClassList() {
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                .callTimeout(2, TimeUnit.MINUTES)
//                .connectTimeout(90, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(VcareApi.JSONURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//
//        VcareApi api = retrofit.create(VcareApi.class);
//        Call<String> call = api.class_dropdown(userDetails.getCustId());
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body());
//                        JSONArray jsonArray = jsonObject.getJSONArray("model");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            classid = jsonObject1.getString("classId");
//                            className = jsonObject1.getString("className");
//                            class_Id.add(classid);
//                            class_Name.add(className);
//                        }
//
//                        ArrayAdapter adapter = new ArrayAdapter(AddAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, class_Name);
//                        applicationType_spin.setAdapter(adapter);
//                        applicationType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                String selected = (String) adapterView.getItemAtPosition(i);
//                                classid = class_Id.get(i);
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                progress_layout.setVisibility(View.GONE);
//                String message = "";
//                if (t instanceof UnknownHostException) {
//                    message = "No internet connection!";
//                } else {
//                    message = "Something went wrong! try again";
//                }
//                Toast.makeText(AddAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//    }
//
//    private void getChildList() {
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                .callTimeout(2, TimeUnit.MINUTES)
//                .connectTimeout(90, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(VcareApi.JSONURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//        VcareApi api = retrofit.create(VcareApi.class);
//        Call<String> call=api.child_dropdown(userDetails.getCustId());
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body());
//                        JSONArray jsonArray = jsonObject.getJSONArray("model");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                            childid = jsonObject1.getString("childId");
//                            childname = jsonObject1.getString("displayName");
//                            child_id.add(childid);
//                            child_name.add(childname);
//                        }
//                        ArrayAdapter adapter = new ArrayAdapter(AddAdditionalCharges.this, android.R.layout.simple_dropdown_item_1line, child_name);
//                        applicationType_spin.setAdapter(adapter);
//
//                        applicationType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                String selected = (String) adapterView.getItemAtPosition(i);
//                                childid=child_id.get(i);
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                progress_layout.setVisibility(View.GONE);
//                String message = "";
//                if (t instanceof UnknownHostException) {
//                    message = "No internet connection!";
//                } else {
//                    message = "Something went wrong! try again";
//                }
//                Toast.makeText(AddAdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//    }

    private void addAdditionalCharge() {
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
        AdditionalChargesModel additionalChargesModel=new AdditionalChargesModel();
        additionalChargesModel.setCustId(custId);
        additionalChargesModel.setChargeName(chargeName);
        additionalChargesModel.setDescription(description);
        additionalChargesModel.setAmount(amount);
        if(tax_spin.getText().toString().isEmpty()){
            additionalChargesModel.setTaxId("");
        }else {
            additionalChargesModel.setTaxId(taxesId);
        }
        additionalChargesModel.setDate(dateText.getText().toString());
//        if(app)
        additionalChargesModel.setApplicableType(applicationType);
        additionalChargesModel.setClassid(classid);
        additionalChargesModel.setChildId(childid);

        Call<AdditionalChargeResponse> call=api.add_additionalCharge("0",additionalChargesModel);
        call.enqueue(new Callback<AdditionalChargeResponse>() {
            @Override
            public void onResponse(Call<AdditionalChargeResponse> call, Response<AdditionalChargeResponse> response) {
                if (response.code()==200) {
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddAdditionalCharges.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddAdditionalCharges.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddAdditionalCharges.this, AdditionalCharges.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddAdditionalCharges.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AdditionalChargeResponse> call, Throwable t) {
                Toast.makeText(AddAdditionalCharges.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(AddAdditionalCharges.this,AdditionalCharges.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }
}
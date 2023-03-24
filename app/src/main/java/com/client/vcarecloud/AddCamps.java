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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddCampModel;
import com.client.vcarecloud.models.AddTaxModel;
import com.client.vcarecloud.models.AddcampResponse;
import com.client.vcarecloud.models.CampModel;
import com.client.vcarecloud.models.GetClassList;
import com.client.vcarecloud.models.GetClassListResponseModel;
import com.client.vcarecloud.models.TaxModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddCamps extends AppCompatActivity {
    TextInputEditText camp,charges,campDetail,from,to;
    AutoCompleteTextView className_spin,tax_spin;
//    AutoCompleteTextView className_spin
    AppCompatButton addCamp;
    ImageView back;
    RelativeLayout progressLayout;
//    Spinner className_spin;

    CampModel campModel;
    UserDetails userDetails;
    AddCampModel campAddModel;

    boolean dateChecked = false;

    List<AddTaxModel> taxModelList;
    List<GetClassList> classList;

    AdapterView<?> arg0;
    View arg1;
    int position;
    long id;
    String[] classArray;

    String custId,campId,campName,className,taxName,fromDate,toDate,charge,camp_detail,classId,
            taxId,taxstatus;
    String message,errorMessage;

    ArrayList<String> class_Name = new ArrayList<>();
    ArrayList<String> class_Id = new ArrayList<>();

    ArrayList<String> tax_id = new ArrayList<>();
    ArrayList<String> tax_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camps);

        camp = findViewById(R.id.camp);
        from = findViewById(R.id.from1);
        to = findViewById(R.id.to1);
        className_spin = findViewById(R.id.class_spin);
        charges = findViewById(R.id.charge);
        tax_spin = findViewById(R.id.tax_spin);
        campDetail = findViewById(R.id.campDetails);
        addCamp = findViewById(R.id.add);
        back = findViewById(R.id.back);
        progressLayout = findViewById(R.id.progress);

        userDetails = new UserDetails(AddCamps.this);

        classList = new ArrayList<>();
        taxModelList = new ArrayList<>();

        campModel=new CampModel();
        campAddModel=new AddCampModel();

        custId=userDetails.getCustId();
        classId=userDetails.getClassId();
        campId=userDetails.getCampId();
        taxId=userDetails.getTaxId();

        getClassList();
        getTaxList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCamps.this, Camps.class);
                startActivity(intent);
                finish();
            }
        });

        addCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                campName=camp.getText().toString();
                fromDate=from.getText().toString();
                toDate=to.getText().toString();
//                className=className_spin.getText().toString();
                className=className_spin.toString();
                charge=charges.getText().toString();
                taxName=tax_spin.getText().toString();
                camp_detail=campDetail.getText().toString();

                if (Validate()) {
                    addCampDetails();
                }
            }
        });

        from.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddCamps.this, (view, year1, month1, day1) -> {
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
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        to.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddCamps.this, (view, year1, month1, day1) -> {
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
    }

    private void addCampDetails() {
        progressLayout.setVisibility(View.VISIBLE);

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
        AddCampModel addCampModel=new AddCampModel();
//        addCampModel.setCampId(campId);
        addCampModel.setCustId(custId);
        addCampModel.setCampName(campName);
        addCampModel.setCampDetails(camp_detail);
        addCampModel.setClassName(className);
        addCampModel.setCampStartDate(fromDate);
        addCampModel.setCampEndDate(toDate);
        addCampModel.setClassId(classId);
        addCampModel.setCampCharge(charge);
        if(tax_spin.getText().toString().isEmpty()){
            addCampModel.setTaxesId("");
//            addCampModel.setTaxName("");
        }else {
            addCampModel.setTaxesId(taxId);
            addCampModel.setTaxName(taxName);
        }

        Call<AddcampResponse> call=api.add_camps("0",addCampModel);
        call.enqueue(new Callback<AddcampResponse>() {
            @Override
            public void onResponse(Call<AddcampResponse> call, Response<AddcampResponse> response) {
                if (response.code()==200) {
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddCamps.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddCamps.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddCamps.this, Camps.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddCamps.this,response.body().getErrorMessage(),false);
                    }
                    progressLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddcampResponse> call, Throwable t) {
                Toast.makeText(AddCamps.this, "Fail", Toast.LENGTH_SHORT).show();
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

                            taxId=jsonObject1.getString("taxesId");
                            taxName=jsonObject1.getString("taxName");
                            taxstatus=jsonObject1.getString("taxStatus");
                            if(taxstatus.equalsIgnoreCase("true")){
                                tax_id.add(taxId);
                                tax_name.add(taxName);
                            }
                        }

                        ArrayAdapter adapter = new ArrayAdapter(AddCamps.this, android.R.layout.simple_dropdown_item_1line, tax_name);
                        tax_spin.setAdapter(adapter);

                        tax_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                taxId = tax_id.get(i);
                            }
                        });
//                        Utils.dismissProgressDialog();
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
                Toast.makeText(AddCamps.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getClassList() {
//        Utils.showProgressDialog(AddCamps.this);
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
//                Utils.dismissProgressDialog();
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            classId = jsonObject1.getString("classId");
                            className = jsonObject1.getString("className");
                            class_Id.add(classId);
                            class_Name.add(className);

                        }

                        ArrayAdapter adapter = new ArrayAdapter(AddCamps.this, android.R.layout.simple_dropdown_item_1line, class_Name);
//                        adapter.setDropDownViewResource(
//                                android.R.layout
//                                        .simple_spinner_dropdown_item);
                        className_spin.setAdapter(adapter);
//                        onItemSelected(arg0,arg1,position,id);
                        className_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                classId=class_Id.get(i);
                            }
                        });

//                        Utils.dismissProgressDialog();
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
                Toast.makeText(AddCamps.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

//    public void onItemSelected(AdapterView<?> arg0,
//                               View arg1,
//                               int position,
//                               long id){
//        String selected =(String).getItemAtPosition(position);
//                                classId=class_Id.get(position);
//        classId=class_Id.get(position);
//    }

    private boolean Validate() {
        if (!campname()) {
            return false;
        } else if (!startDate()) {
            return false;
        } else if (!endDate()) {
            return false;
        } else if (!classname()) {
            return false;
        }
        else if (!chargeCamp()) {
            return false;
        }
//        else if (!tax()) {
//            return false;
//        }
        else {
            return true;
        }
    }

    private boolean campname() {
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

    private boolean startDate() {
        if(from.getText().toString().isEmpty()){
            from.setError("From date should not be empty");

//            Utils.showAlertDialog(AddCamps.this,"From date should not be empty",false);
            from.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean endDate() {
        if(to.getText().toString().isEmpty()){
            to.setError("To date should not be empty");
            to.requestFocus();
            return false;
        }else {
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

    private boolean chargeCamp() {
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

    private boolean tax() {
        if(tax_spin.getText().toString().isEmpty()){
            tax_spin.setError("Tax should not be empty");
            tax_spin.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(AddCamps.this,Camps.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
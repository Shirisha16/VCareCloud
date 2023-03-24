package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddAdjustmentModel;
import com.client.vcarecloud.models.AddAdjustmentResponse;
import com.client.vcarecloud.models.AdjustmentsModel;
import com.client.vcarecloud.models.UpdateAdjustmentModel;
import com.client.vcarecloud.models.UpdateAdjustmentResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateAdjustment extends AppCompatActivity {
    ImageView back;
    TextInputEditText dateText,amountText,noteText;
    AutoCompleteTextView adjust_spin,application_spin;
    AppCompatButton editButton;
    RelativeLayout progressLayout;

    UserDetails userDetails;
    String selected;

    String id,custId,adjustType,description,amount,adjustamount,date,applicableType,childname,
            refApplicableID,classID;

    String message, error;

    AdjustmentsModel adjustmentsModel;
    UpdateAdjustmentModel updateAdjustmentModel;
    UpdateAdjustmentResponse updateAdjustmentResponse;

    String[] adjustTypeNames = {"Deposit", "Wrong Posting"};

    boolean dateChecked = false;
    Dialog dialog;

    ArrayList<String> applicable_name = new ArrayList<>();
    ArrayList<String> applicable_id = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_adjustment);

        back=findViewById(R.id.back);
        progressLayout=findViewById(R.id.progress);
        dateText=findViewById(R.id.date);
        adjust_spin=findViewById(R.id.adjust_spin);
        application_spin=findViewById(R.id.application_spin);
        amountText=findViewById(R.id.amount1);
        noteText=findViewById(R.id.note);
        editButton=findViewById(R.id.edit);

        adjustmentsModel=new AdjustmentsModel();
        updateAdjustmentModel=new UpdateAdjustmentModel();
        updateAdjustmentResponse=new UpdateAdjustmentResponse();

        userDetails=new UserDetails(UpdateAdjustment.this);

        adjustmentsModel=(AdjustmentsModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(UpdateAdjustment.this,Adjustments.class);
//                intent.putExtra("list",adjustmentsModel);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        id=getIntent().getStringExtra("id");
        custId=getIntent().getStringExtra("custId");
        adjustType=getIntent().getStringExtra("adjustType");
        description=getIntent().getStringExtra("adjustDescription");
        amount=getIntent().getStringExtra("amount");
        adjustamount=getIntent().getStringExtra("adjustAmount");
        date=getIntent().getStringExtra("adjustDate");
        if (date!=null){
            date=date.replace("T00:00:00","");
        }

        applicableType=getIntent().getStringExtra("applicableType");
        refApplicableID=getIntent().getStringExtra("refApplicableID");
        childname=getIntent().getStringExtra("child");

        dateText.setText(date);
        adjust_spin.setText(adjustType);
        application_spin.setText(childname);
        amountText.setText(amount);

        if (description.equalsIgnoreCase("null")){
            noteText.setText("");
        }else{
            noteText.setText(description);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             date=dateText.getText().toString();
             adjustType=adjust_spin.getText().toString();
             childname=application_spin.getText().toString();
             amount=amountText.getText().toString();
             description=noteText.getText().toString();

                if (Validation()) {
                    updateAdjustments();
                }
            }
        });

        dateText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAdjustment.this, (view, year1, month1, day1) -> {
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
            datePickerDialog.getDatePicker();
//            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        ArrayAdapter adapter = new ArrayAdapter(UpdateAdjustment.this, android.R.layout.simple_list_item_1, adjustTypeNames);

        adjust_spin.setAdapter(adapter);
        adjust_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });
        dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.spinner_search_item_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        getChildList();
    }

    private void getChildList() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();
        VcareApi api=retrofit.create(VcareApi.class);
        Call<String> call=api.child_dropdown(custId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body()!=null){
                    try {
                        JSONObject  jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray=jsonObject.getJSONArray("model");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            classID=jsonObject1.getString("childId");
                            childname=jsonObject1.getString("displayName");
                            applicable_id.add(classID);
                            applicable_name.add(childname);

                        }
                        ArrayAdapter adapter = new ArrayAdapter(UpdateAdjustment.this,android.R.layout.simple_dropdown_item_1line,applicable_name);
                        application_spin.setAdapter(adapter);
                        application_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                refApplicableID=applicable_id.get(i);
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
                Toast.makeText(UpdateAdjustment.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void updateAdjustments() {
        progressLayout.setVisibility(View.VISIBLE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        updateAdjustmentModel.setId(id);
        updateAdjustmentModel.setCustId(custId);
        updateAdjustmentModel.setAdjustType(adjustType);
        updateAdjustmentModel.setDescription(description);
        updateAdjustmentModel.setAmount(amount);
        updateAdjustmentModel.setAdjustAmount(adjustamount);
        updateAdjustmentModel.setDate(date);
        updateAdjustmentModel.setApplicableType(applicableType);
        updateAdjustmentModel.setApplicableID(refApplicableID);
        updateAdjustmentModel.setChildName(childname);

        Call<UpdateAdjustmentResponse> call=api.update_adjustment(id,"0", updateAdjustmentModel);
        call.enqueue(new Callback<UpdateAdjustmentResponse>() {
            @Override
            public void onResponse(Call<UpdateAdjustmentResponse> call, Response<UpdateAdjustmentResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdateAdjustment.this != null && !UpdateAdjustment.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateAdjustment.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateAdjustment.this, Adjustments.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateAdjustment.this,response.body().getErrorMessage(),false);
                    }
                }
                progressLayout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateAdjustmentResponse> call, Throwable t) {
                progressLayout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateAdjustment.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean Validation() {
        if (!date1()) {
            return false;
        } else if (!type()) {
            return false;
        }else if (!applicationon()) {
            return false;
        }else if (!amount1()) {
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

    private boolean type() {
        if (adjust_spin.getText().toString().isEmpty()) {
            adjust_spin.setError("Adjust type should not be empty");
            adjust_spin.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean applicationon() {
        if (application_spin.getText().toString().isEmpty()) {
            application_spin.setError("Application On should not be empty");
            application_spin.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean amount1() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(UpdateAdjustment.this,Adjustments.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }
}
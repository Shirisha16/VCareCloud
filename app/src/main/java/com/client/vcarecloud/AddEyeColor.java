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
import com.client.vcarecloud.models.AddLookupTypeModel;
import com.client.vcarecloud.models.AddLookupTypeResponse;
import com.client.vcarecloud.models.LookupTypeModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddEyeColor extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText eye;
    AppCompatButton addColor;

    UserDetails userDetails;
    LookupTypeModel lookupTypeModel;

    String custId,lookupId,lookupType="Eye Color",lookupName="",empId="0", createdOn,
            lastChangedOn,message,errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_eye_color);

        back=findViewById(R.id.back);
        eye=findViewById(R.id.eyeColor);
        addColor=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);

        userDetails = new UserDetails(AddEyeColor.this);
        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        lookupTypeModel=new LookupTypeModel();

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEyeColor.this, EyeColor.class);
                startActivity(intent);
                finish();
            }
        });

        addColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupName=eye.getText().toString();

                if (Validate()) {
                    addEyeColor();
                }
            }
        });
    }

    private void addEyeColor() {
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

        VcareApi vcareApi=retrofit.create(VcareApi.class);
        AddLookupTypeModel addLookupTypeModel =new AddLookupTypeModel();
        addLookupTypeModel.setCustId(custId);
        addLookupTypeModel.setLookupsId(lookupId);
        addLookupTypeModel.setLookupType(String.valueOf(lookupType));
        addLookupTypeModel.setLookupName(lookupName);
        addLookupTypeModel.setEmpId(empId);

        Call<AddLookupTypeResponse> call=vcareApi.addLookups("0", addLookupTypeModel);
        call.enqueue(new Callback<AddLookupTypeResponse>() {
            @Override
            public void onResponse(Call<AddLookupTypeResponse> call, Response<AddLookupTypeResponse> response) {
                if(response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddEyeColor.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddEyeColor.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddEyeColor.this, EyeColor.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddEyeColor.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddLookupTypeResponse> call, Throwable t) {
                Toast.makeText(AddEyeColor.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddEyeColor.this, EyeColor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean Validate() {
        if (!lookup_name()) {
            return false;
        }else{
            return  true;
        }
    }

    private boolean lookup_name() {
        String str = eye.getText().toString();
        if (eye.getText().toString().isEmpty()) {
            eye.setError("Eye color should not be empty");
            eye.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            eye.setText(eye.getText().toString().trim());
            eye.setSelection(eye.getText().length());
            return false;
        }
        else {
            return true;
        }
    }
}
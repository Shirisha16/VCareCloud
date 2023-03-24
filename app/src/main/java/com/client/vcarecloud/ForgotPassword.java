package com.client.vcarecloud;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.UserDetails;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ForgotPassword extends AppCompatActivity {
AppCompatButton submit;
    TextInputLayout username;
    String username1,empId,userId,custId,securityid,message,errorMessage;
    RelativeLayout progress;
    UserDetails userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        username=findViewById(R.id.username);
        submit=findViewById(R.id.submit);
        userDetails=new UserDetails(ForgotPassword.this);
        progress=findViewById(R.id.progressLayout);
        username1=userDetails.getEmail();
        empId=userDetails.getEmpID();
        custId=userDetails.getCustId();
        userId=userDetails.getUserID();
        securityid=userDetails.getSecurityid();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username1 =username.getEditText().getText().toString();
                if (Validations()){
                    forgotResponse(username1);
                }

            }
        });

    }

    private void forgotResponse(String username1) {
        progress.setVisibility(View.VISIBLE);
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
        Call<String> call=api.forgot_password(username1);
        String finalUsername = username1;
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(ForgotPassword.this, "yes"+response.body(), Toast.LENGTH_SHORT).show();
                if (response.body()!=null){
                    progress.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject=new JSONObject(response.body());
                        if(jsonObject.optString("message").equalsIgnoreCase("Email Sent")){
                            message=jsonObject.optString("message");
                            errorMessage=jsonObject.optString("errorMessage");
                            Intent intent=new Intent(ForgotPassword.this,Login.class);
                            intent.putExtra("UserName",userDetails.getEmail());
                            intent.putExtra("userID",userDetails.getUserID());
                            intent.putExtra("empID",userDetails.getEmpID());
                            intent.putExtra("custId",userDetails.getCustId());
                            intent.putExtra("securityid",userDetails.getSecurityid());
                            Toast.makeText(ForgotPassword.this,"Email Sent", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            submit.setEnabled(false);
                        } else if (jsonObject.optString("message").equalsIgnoreCase("null")){
                            Toast.makeText(ForgotPassword.this,"No Accounts Found", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ForgotPassword.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private boolean Validations() {
        try {
            if (!ValidateUserName()) {
                System.out.println("Error values");
                return false;
            }
        } catch (Exception e) {

        }
        return true;
    }


    private boolean ValidateUserName() {
        if (username.getEditText().getText().toString().trim().isEmpty()) {
            username.setError("Username should not be empty");
            username.requestFocus();
            return false;
        } else {
            return true;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(ForgotPassword.this,Login.class);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
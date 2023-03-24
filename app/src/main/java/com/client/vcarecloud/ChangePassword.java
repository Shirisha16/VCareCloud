package com.client.vcarecloud;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.client.vcarecloud.Api.RestService;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.ChangePasswordModel;
import com.client.vcarecloud.models.PasswordResponce;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChangePassword extends AppCompatActivity {
    AppCompatButton submit;
    String oldPassword, newPassword, confirmPassword, empId, custId, userId, username, securityId;
    //    TextInputLayout old_password,new_password,confirm_password;
    RelativeLayout progress;
    TextInputEditText old_password, new_password, confirm_password;
    int emp_id, cust_id, user_id;
    TextView changePassword;
    ImageView back;
    ChangePasswordModel changePasswordModel;
    UserDetails userDetails;
    String  message,errorMessage;
    TextView responseTv;
    RestService restService;
    PasswordResponce passwordResponce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        old_password = findViewById(R.id.oldPassword1);
        new_password = findViewById(R.id.newPassword1);
        back = findViewById(R.id.back);
        confirm_password = findViewById(R.id.confirmPassword1);
        submit = findViewById(R.id.submit);
        passwordResponce = new PasswordResponce();
        progress = findViewById(R.id.progressLayout);
        restService = new RestService();
        responseTv = findViewById(R.id.response);
        changePassword = findViewById(R.id.changeText);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(ChangePassword.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(ChangePassword.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        userDetails = new UserDetails(ChangePassword.this);

        username = getIntent().getStringExtra("UserName");
        empId = userDetails.getEmpID();
        custId = userDetails.getCustId();
        userId = userDetails.getUserID();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword = old_password.getText().toString();
                newPassword = new_password.getText().toString();
                confirmPassword = confirm_password.getText().toString();
                if (Validate())
                    changeResponse();
            }
        });
    }

    private void changeResponse() {
        progress.setVisibility(View.VISIBLE);
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
        ChangePasswordModel changePasswordModel = new ChangePasswordModel();
        changePasswordModel.setOld(oldPassword);
        changePasswordModel.setNew(newPassword);
        changePasswordModel.setConfirm(confirmPassword);
        if(empId.equalsIgnoreCase("null")){
            changePasswordModel.setEmpid(null);
        }else {
            changePasswordModel.setEmpid(empId);
        }

        changePasswordModel.setCustId(custId);

        if(userId.equalsIgnoreCase("null")){
            changePasswordModel.setUserId(null);
        }else {
            changePasswordModel.setUserId(userId);
        }

        Call<PasswordResponce> call = api.changePassword(changePasswordModel);
        call.enqueue(new Callback<PasswordResponce>() {
            @Override
            public void onResponse(Call<PasswordResponce> call, Response<PasswordResponce> response) {

//                progress.setVisibility(View.GONE);
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        Utils.showAlertDialog(ChangePassword.this,message, false);
                        Intent intent = new Intent(ChangePassword.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        userDetails.setIsLogged(false);
                        userDetails.clearData();
                        startActivity(intent);
                        finish();
                    } else {
                        Utils.showAlertDialog(ChangePassword.this, "Old Password is wrong", false);
                    }
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PasswordResponce> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ChangePassword.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean Validate() {

        if (!OldPassword()) {
            return false;
        } else if (!NewPassword()) {
            return false;
        } else if (!ConfirmPassword()) {
            return false;
        } else {
            return true;
        }

    }

    private boolean OldPassword() {
        if (old_password.getText().toString().isEmpty()) {
            old_password.setError("field should not be empty");
            old_password.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean ConfirmPassword() {

        if (!confirm_password.getText().toString().equals(new_password.getText().toString())) {
            confirm_password.setError("The new password and confirmation password does not match");
            confirm_password.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean NewPassword() {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");

        if (old_password.getText().toString().equalsIgnoreCase(confirm_password.getText().toString())){
            new_password.setError("Old Password and New Password Can't be same!");
            new_password.requestFocus();
            return false;
        }else if (new_password.getText().toString().trim().isEmpty()) {
            new_password.setError("password should not be empty");
            new_password.requestFocus();
            return false;
        } else if (!UpperCasePatten.matcher(new_password.getText().toString().trim()).find()) {
            new_password.setError("Password must have at least one uppercase character !!");
            new_password.requestFocus();
            return false;
        } else if (!lowerCasePatten.matcher(new_password.getText().toString().trim()).find()) {
            new_password.setError("Password must have at least one lowercase character !!");
            new_password.requestFocus();
            return false;
        } else if (!specailCharPatten.matcher(new_password.getText().toString().trim()).find()) {
            new_password.setError("Password must have at least one special character !!");
            new_password.requestFocus();
            return false;
        } else if (!digitCasePatten.matcher(new_password.getText().toString().trim()).find()) {
            new_password.setError("Password must have at least one digit  !!");
            new_password.requestFocus();
            return false;
        } else if (new_password.getText().toString().trim().length() < 6) {
            new_password.setError("password should have at least 6 characters");
            new_password.requestFocus();
            return false;
        } else {
            return true;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(ChangePassword.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(ChangePassword.this,EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
package com.client.vcarecloud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.Login_model;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.PreferenceConnector;
import com.client.vcarecloud.utils.Utils;
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

public class Login extends AppCompatActivity {
    TextInputLayout username, password;
    AppCompatButton submit;
    TextView forgotPassword, toast;
    String username1, password1, userId, empId, custId, securityId;
    RelativeLayout progress;
    Login_model login_model;
    Boolean logged;
    UserDetails userDetails;
    String message, UserIdResponse, errorMessage, DidError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        username = findViewById(R.id.username);
        userDetails = new UserDetails(Login.this);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        forgotPassword = findViewById(R.id.forgotPassword);
        progress = findViewById(R.id.progressLayout);
        toast = findViewById(R.id.toast);
        userDetails=new UserDetails(Login.this);
        login_model = new Login_model();
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                intent.putExtra("UserName", userDetails.getEmail());
                intent.putExtra("userID", userDetails.getUserID());
                intent.putExtra("empID", userDetails.getEmpID());
                intent.putExtra("custId", userDetails.getCustId());
                intent.putExtra("securityid", userDetails.getSecurityid());
                startActivity(intent);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username1 = username.getEditText().getText().toString();
                password1 = password.getEditText().getText().toString();
                if (Validations()) {
                    LoginResponse(username1, password1);
                }
            }
        });
    }
    private boolean Validations() {
        try {
            if (!ValidateUserName()) {
                return false;
            } else if (!ValidatePassword()) {
                return false; }
        } catch (Exception e) { }
        return true;
    }

    private boolean ValidatePassword() {
        if (password.getEditText().getText().toString().trim().isEmpty()) {
            password.setError("Password should not empty");
            password.requestFocus();
            return false;
        } else { return true; }
    }

    private boolean ValidateUserName() {
        if (username.getEditText().getText().toString().trim().isEmpty()) {
            username.setError("Username should not empty");
            username.requestFocus();
            return false;
        } else { return true; }
    }

    private void LoginResponse(String username1, String password1) {
        progress.setVisibility(View.VISIBLE);
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
        Call<String> call = api.login_page(username1, password1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    progress.setVisibility(View.GONE);
                    try {
                        JSONObject obj = new JSONObject(response.body());
                        JSONObject jsonObject = obj.getJSONObject("model");
                        message = obj.getString("message");
                        errorMessage = obj.getString("errorMessage");
                        progress.setVisibility(View.GONE);
                        userDetails = new UserDetails(Login.this);
                        userDetails.setIsLogged(true);
                        userDetails.setUserID(jsonObject.getString("userID"));
                        userDetails.setEmpID(jsonObject.getString("empID"));
                        userDetails.setCustId(jsonObject.getString("custId"));
                        userDetails.setFirstName(jsonObject.getString("firstName"));
                        userDetails.setLastName(jsonObject.getString("lastName"));
                        userDetails.setEmail(jsonObject.getString("email"));
                        userDetails.setUserType(jsonObject.getString("userType"));
                        userDetails.setDaycareName(jsonObject.getString("daycareName"));
                        userDetails.setSecurityid(jsonObject.getString("securityid"));
                        PreferenceConnector.writeString(getApplicationContext(), "Password", password1);
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                     if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                         userDetails.setIsLogged(true);
                         Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(Login.this, Dashboard.class);
                         startActivity(intent);
                     } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                         userDetails.setIsLogged(true);
                         Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(Login.this, EmployeeDashboard.class);
                         startActivity(intent);
                     }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progress.setVisibility(View.GONE);
                    Utils.showAlertDialog(Login.this, "Invalid Credentials", false);
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
                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       finish();
    }
}
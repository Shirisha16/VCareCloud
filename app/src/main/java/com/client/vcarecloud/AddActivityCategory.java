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
import com.client.vcarecloud.models.ActivityCategoryModel;
import com.client.vcarecloud.models.AddActCategoryModel;
import com.client.vcarecloud.models.AddActCategoryResponse;
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

public class AddActivityCategory extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText activityName;
    AppCompatButton addActivity;

    UserDetails userDetails;
    ActivityCategoryModel activityCategoryModel;

    String id,custId,lookupType,lookupName="",message,errorMessage,createdOn,lastChangedOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        back=findViewById(R.id.back);
        activityName=findViewById(R.id.activity_name);
        addActivity=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);

        userDetails = new UserDetails(AddActivityCategory.this);
        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        activityCategoryModel=new ActivityCategoryModel();

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivityCategory.this, ActivityCategory.class);
                startActivity(intent);
                finish();
            }
        });

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupName=activityName.getText().toString();

                if (Validate()) {
                    addNewActivityCategory();
                }

            }
        });
    }

    private void addNewActivityCategory() {
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
        AddActCategoryModel addActCategoryModel=new AddActCategoryModel();
        addActCategoryModel.setId(id);
        addActCategoryModel.setCustId(custId);
        addActCategoryModel.setLookupType(lookupType);
        addActCategoryModel.setLookupName(lookupName);

        Call<AddActCategoryResponse> call= api.add_activityCategory(custId,addActCategoryModel);
        call.enqueue(new Callback<AddActCategoryResponse>() {
            @Override
            public void onResponse(Call<AddActCategoryResponse> call, Response<AddActCategoryResponse> response) {
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddActivityCategory.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddActivityCategory.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddActivityCategory.this, ActivityCategory.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
//                            Intent intent = new Intent(AddActivityCategory.this, ActivityCategory.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            Toast.makeText(AddActivityCategory.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                            startActivity(intent);
//                            finish();
                        }
                    }else{
                        Utils.showAlertDialog(AddActivityCategory.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddActCategoryResponse> call, Throwable t) {
                Toast.makeText(AddActivityCategory.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddActivityCategory.this, ActivityCategory.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private boolean Validate() {
        if (!activity_name()) {
            return false;
        }else{
            return  true;
        }
    }

    private boolean activity_name() {
        String str = activityName.getText().toString();
        if (activityName.getText().toString().isEmpty()) {
            activityName.setError("Activity name should not be empty");
            activityName.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            activityName.setText(activityName.getText().toString().trim());
            activityName.setSelection(activityName.getText().length());
            return false;
        }else {
            return true;
        }


    }
}
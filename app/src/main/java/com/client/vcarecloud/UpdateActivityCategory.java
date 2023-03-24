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
import com.client.vcarecloud.models.UpdateActivityCategoryModel;
import com.client.vcarecloud.models.UpdateActivityCategoryResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateActivityCategory extends AppCompatActivity {

    String id,custId,lookupType,lookupName,createdOn,lastChangedOn;

    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText activityName;
    AppCompatButton editActivity;

    UserDetails userDetails;
    String message,error;

    ActivityCategory activityCategory;
    ActivityCategoryModel activityCategoryModel;
    UpdateActivityCategoryModel updateActivityCategoryModel;
    UpdateActivityCategoryResponse updateActivityCategoryResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        back=findViewById(R.id.back);
        activityName=findViewById(R.id.activity_name);
        editActivity=findViewById(R.id.update);
        progress_layout=findViewById(R.id.progress);

        activityCategory=new ActivityCategory();
        activityCategoryModel=new ActivityCategoryModel();
        updateActivityCategoryModel=new UpdateActivityCategoryModel();
        updateActivityCategoryResponse=new UpdateActivityCategoryResponse();

        activityCategoryModel=(ActivityCategoryModel)getIntent().getSerializableExtra("list");

        userDetails=new UserDetails(UpdateActivityCategory.this);

        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        id=getIntent().getStringExtra("id");
        custId=getIntent().getStringExtra("custId");
        lookupType=getIntent().getStringExtra("lookupType");
        lookupName=getIntent().getStringExtra("lookupName");

        activityName.setText(lookupName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateActivityCategory.this,ActivityCategory.class);
                intent.putExtra("list",activityCategoryModel);
                startActivity(intent);
                finish();
            }
        });

        editActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupName=activityName.getText().toString();

                if (validate()) {
                    updateActivityDetails();
                }
            }
        });
    }

    private void updateActivityDetails() {
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
        updateActivityCategoryModel.setId(id);
        updateActivityCategoryModel.setCustId(custId);
        updateActivityCategoryModel.setLookupType(lookupType);
        updateActivityCategoryModel.setLookupName(lookupName);

        Call<UpdateActivityCategoryResponse> call=api.update_activityCategory(id,custId,updateActivityCategoryModel);
        call.enqueue(new Callback<UpdateActivityCategoryResponse>() {
            @Override
            public void onResponse(Call<UpdateActivityCategoryResponse> call, Response<UpdateActivityCategoryResponse> response) {
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        error=response.body().getErrorMessage();
                        if (UpdateActivityCategory.this!=null&&!UpdateActivityCategory.this.isFinishing()){
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(UpdateActivityCategory.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateActivityCategory.this, ActivityCategory.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateActivityCategory.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateActivityCategoryResponse> call, Throwable t) {
                Toast.makeText(UpdateActivityCategory.this, "Fail", Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateActivityCategory.this,ActivityCategory.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private boolean validate() {
        if (!activity_name()){
            return  false;
        }else{
            return true;
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
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
import com.client.vcarecloud.models.LookupTypeModel;
import com.client.vcarecloud.models.UpdateLookupTypeModel;
import com.client.vcarecloud.models.UpdateLookupTypeResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdatePaymentType extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText lookup_name;
    AppCompatButton edit;

    UserDetails userDetails;
    String message,error;

    PaymentType paymentType;
    LookupTypeModel lookupTypeModel;
    UpdateLookupTypeModel updateLookupTypeModel;
    UpdateLookupTypeResponse updateLookupTypeResponse;

    String id,empid,custId,lookupname,lookupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_payment_type);

        back=findViewById(R.id.back);
        progress_layout=findViewById(R.id.progress);
        lookup_name=findViewById(R.id.paymentType);
        edit=findViewById(R.id.edit);

        paymentType=new PaymentType();
        lookupTypeModel=new LookupTypeModel();
        updateLookupTypeModel =new UpdateLookupTypeModel();
        updateLookupTypeResponse =new UpdateLookupTypeResponse();

        lookupTypeModel=(LookupTypeModel) getIntent().getSerializableExtra("list");
        userDetails=new UserDetails(UpdatePaymentType.this);

        empid=getIntent().getStringExtra("empId");
        custId=getIntent().getStringExtra("custId");
        id=getIntent().getStringExtra("Id");
        lookupType=getIntent().getStringExtra("lookupType");
        lookupname=getIntent().getStringExtra("lookupName");

        lookup_name.setText(lookupname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdatePaymentType.this,PaymentType.class);
                intent.putExtra("list",lookupTypeModel);
                startActivity(intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupname=lookup_name.getText().toString();

                if (validate()) {
                    updatePaymentType();
                }
            }
        });
    }

    private void updatePaymentType() {
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
        updateLookupTypeModel.setId(id);
        updateLookupTypeModel.setCustId(custId);
        updateLookupTypeModel.setLookupType(String.valueOf(lookupType));
        updateLookupTypeModel.setLookupName(lookupname);
        Call<UpdateLookupTypeResponse> call= api.update_paymentType(id,"0", updateLookupTypeModel);
        call.enqueue(new Callback<UpdateLookupTypeResponse>() {
            @Override
            public void onResponse(Call<UpdateLookupTypeResponse> call, Response<UpdateLookupTypeResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdatePaymentType.this != null && !UpdatePaymentType.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdatePaymentType.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdatePaymentType.this, PaymentType.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdatePaymentType.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateLookupTypeResponse> call, Throwable t) {
                Toast.makeText(UpdatePaymentType.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validate() {
        if (!lookup_name()) {
            return false;
        }else{
            return  true;
        }
    }

    private boolean lookup_name() {
        String str = lookup_name.getText().toString();
        if (lookup_name.getText().toString().isEmpty()) {
            lookup_name.setError("Payment type should not be empty");
            lookup_name.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            lookup_name.setText(lookup_name.getText().toString().trim());
            lookup_name.setSelection(lookup_name.getText().length());
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdatePaymentType.this,PaymentType.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
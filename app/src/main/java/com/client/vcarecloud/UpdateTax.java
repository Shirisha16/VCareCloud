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
import com.client.vcarecloud.models.TaxModel;
import com.client.vcarecloud.models.UpdateTaxModel;
import com.client.vcarecloud.models.UpdateTaxResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
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

public class UpdateTax extends AppCompatActivity {

    TextInputEditText editTaxName,editTaxRate;
    AppCompatButton updateButton;
    ImageView back;
    RelativeLayout progress;
    MaterialCheckBox checkBoxStatus;

    String taxId;
    String custId;
    String taxName;
    String taxRate;
    String taxStatus;
    String createdOn;
    String modifiedOn;
    String status;

    UserDetails userDetails;
    String message,error;

    Taxes taxList;
    TaxModel taxModel;
    UpdateTaxResponse updateTaxResponse;
    UpdateTaxModel updateTaxModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tax);

        editTaxName=findViewById(R.id.taxname);
        editTaxRate=findViewById(R.id.taxRate);
        updateButton=findViewById(R.id.update);
        back=findViewById(R.id.back);
        progress=findViewById(R.id.progress);
        checkBoxStatus=findViewById(R.id.status);

        taxList= new Taxes();
        updateTaxResponse=new UpdateTaxResponse();
        updateTaxModel=new UpdateTaxModel();
        taxModel=(TaxModel) getIntent().getSerializableExtra("list");

        userDetails=new UserDetails(UpdateTax.this);
        updateTaxModel=new UpdateTaxModel();
        taxModel=new TaxModel();

        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        modifiedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        taxId=getIntent().getStringExtra("taxesId");
        custId=getIntent().getStringExtra("custId");
        taxName=getIntent().getStringExtra("taxName");
        taxRate=getIntent().getStringExtra("taxRate");
        taxStatus=getIntent().getStringExtra("taxStatus");
        createdOn=getIntent().getStringExtra("createdOn");
        modifiedOn=getIntent().getStringExtra("modifiedOn");
        status=getIntent().getStringExtra("status");

        editTaxName.setText(taxName);
        editTaxRate.setText(taxRate);

        if(taxStatus.equalsIgnoreCase("true")){
            checkBoxStatus.setChecked(true);
        }else{
            checkBoxStatus.setChecked(false);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateTax.this,Taxes.class);
                intent.putExtra("list",taxModel);
                startActivity(intent);
                finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taxName=editTaxName.getText().toString();
                taxRate=editTaxRate.getText().toString();
                taxStatus=checkBoxStatus.getText().toString();
                if (Validate()) {
                    updateTaxDetails();
                }
            }
        });

    }

    private void updateTaxDetails() {
        progress.setVisibility(View.VISIBLE);

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
        updateTaxModel.setTaxid(taxId);
        updateTaxModel.setCustid(custId);
        updateTaxModel.setTax_name(taxName);
        updateTaxModel.setTax_rate(taxRate);

        if(checkBoxStatus.isChecked()){
            updateTaxModel.setTax_status("true");
        }else{
        updateTaxModel.setTax_status("false");
        }

        updateTaxModel.setCreated_on(createdOn);
        updateTaxModel.setModified_on(modifiedOn);
        updateTaxModel.setStatus(status);

        Call<UpdateTaxResponse> call= api.update_taxes(taxId,"0",updateTaxModel);
        call.enqueue(new Callback<UpdateTaxResponse>() {
            @Override
            public void onResponse(Call<UpdateTaxResponse> call, Response<UpdateTaxResponse> response) {
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        error=response.body().getErrorMessage();
                        if (UpdateTax.this!=null&&!UpdateTax.this.isFinishing()){
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(UpdateTax.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateTax.this, Taxes.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }
                    else{
                        Utils.showAlertDialog(UpdateTax.this,response.body().getErrorMessage(),false);
                    }
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateTaxResponse> call, Throwable t) {
                Toast.makeText(UpdateTax.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateTax.this,Taxes.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private boolean Validate() {
        if (!taxname()) {
            return false;
        } else if (!rateTax()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean taxname() {
        String str = editTaxName.getText().toString();
        if (editTaxName.getText().toString().isEmpty()) {
            editTaxName.setError("Tax name should not be empty");
            editTaxName.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            editTaxName.setText(editTaxName.getText().toString().trim());
            editTaxName.setSelection(editTaxName.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean rateTax() {
        String str = editTaxRate.getText().toString();
        if (editTaxRate.getText().toString().isEmpty()) {
            editTaxRate.setError("Tax rate should not be empty");
            editTaxRate.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            editTaxRate.setText(editTaxRate.getText().toString().trim());
            editTaxRate.setSelection(editTaxRate.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            editTaxRate.setError("Tax rate should not be point");
            editTaxRate.setText(editTaxRate.getText().toString().trim());
            editTaxRate.setSelection(editTaxRate.getText().length());
            return false;
        } else {
            return true;
        }
    }
}
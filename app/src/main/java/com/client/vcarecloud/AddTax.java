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
import com.client.vcarecloud.models.AddTaxModel;
import com.client.vcarecloud.models.AddTaxResponse;
import com.client.vcarecloud.models.TaxModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTax extends AppCompatActivity {
    TextInputEditText taxName, taxRate;
    AppCompatButton addButton;
    ImageView back;
    RelativeLayout progress_layout;

    TaxModel taxModel;
    UserDetails userDetails;

    String empId = "0", taxId, custId, createdOn, modifiedOn, status = "Active";
    Boolean taxStatus;
    String message, errorMessage;

    String tax_name = "";
    String tax_rate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tax);

        taxName = findViewById(R.id.taxname);
        taxRate = findViewById(R.id.taxRate);
        addButton = findViewById(R.id.add);
        back = findViewById(R.id.back);
        progress_layout = findViewById(R.id.progress);

        userDetails = new UserDetails(AddTax.this);
        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        modifiedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        taxModel = new TaxModel();
        tax_name = String.valueOf(taxModel.getName());
        tax_rate = String.valueOf(taxModel.getRate());
        taxStatus = Boolean.valueOf(taxModel.getTaxStatus());

        taxId = userDetails.getTaxId();
        custId = userDetails.getCustId();
//        Toast.makeText(AddTax.this, ""+empId+custId+taxId+tax_name+tax_rate+taxStatus, Toast.LENGTH_SHORT).show();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tax_name = taxName.getText().toString();
                tax_rate = taxRate.getText().toString();

                if (Validate()) {
                    addTaxes();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddTax.this, Taxes.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addTaxes() {
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
        AddTaxModel addTaxModel = new AddTaxModel();
        addTaxModel.setTaxId(taxId);
        addTaxModel.setCustId(custId);
        addTaxModel.setTaxName(tax_name);
        addTaxModel.setTaxRate(tax_rate);
        addTaxModel.setTaxStatus(taxStatus);
        Call<AddTaxResponse> call = api.add_tax("0", addTaxModel);
        call.enqueue(new Callback<AddTaxResponse>() {

            @Override
            public void onResponse(Call<AddTaxResponse> call, Response<AddTaxResponse> response) {
                if(response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddTax.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddTax.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddTax.this, Taxes.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddTax.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddTaxResponse> call, Throwable t) {
                Toast.makeText(AddTax.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), value);
        return body;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddTax.this, Taxes.class);
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
        String str = taxName.getText().toString();
        if (taxName.getText().toString().isEmpty()) {
            taxName.setError("Tax name should not be empty");
            taxName.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            taxName.setText(taxName.getText().toString().trim());
            taxName.setSelection(taxName.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean rateTax() {
        String str = taxRate.getText().toString();
        if (taxRate.getText().toString().isEmpty()) {
            taxRate.setError("Tax rate should not be empty");
            taxRate.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            taxRate.setText(taxRate.getText().toString().trim());
            taxRate.setSelection(taxRate.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            taxRate.setError("Tax rate should not be point");
            taxRate.setText(taxRate.getText().toString().trim());
            taxRate.setSelection(taxRate.getText().length());
            return false;
        }else {
            return true;
        }
    }
}

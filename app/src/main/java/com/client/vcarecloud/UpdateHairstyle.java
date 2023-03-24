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

public class UpdateHairstyle extends AppCompatActivity {
    ImageView back;
    TextInputEditText hair;
    RelativeLayout progress_layout;
    AppCompatButton edit;

    UserDetails userDetails;
    String message,error;

    HairStyle hairStyle;
    LookupTypeModel lookupTypeModel;
    UpdateLookupTypeModel updateLookupTypeModel;
    UpdateLookupTypeResponse updateLookupTypeResponse;

    String id,custId,lookupname,lookupType,createdOn,lastChangedOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hairstyle);

        back=findViewById(R.id.back);
        hair=findViewById(R.id.hair);
        progress_layout=findViewById(R.id.progress);
        edit=findViewById(R.id.edit);

        hairStyle=new HairStyle();
        lookupTypeModel=new LookupTypeModel();
        updateLookupTypeModel =new UpdateLookupTypeModel();
        updateLookupTypeResponse =new UpdateLookupTypeResponse();

        lookupTypeModel=(LookupTypeModel) getIntent().getSerializableExtra("list");

        userDetails=new UserDetails(UpdateHairstyle.this);

        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        id=getIntent().getStringExtra("Id");
        custId=getIntent().getStringExtra("custId");
        lookupType=getIntent().getStringExtra("lookupType");
        lookupname=getIntent().getStringExtra("lookupName");

        hair.setText(lookupname);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateHairstyle.this,HairStyle.class);
                intent.putExtra("list",lookupTypeModel);
                startActivity(intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupname=hair.getText().toString();

                if (validate()) {
                    updateHairstyle();
                }
            }
        });
    }

    private void updateHairstyle() {
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
        Call<UpdateLookupTypeResponse> call= api.updateLookups(id,"0", updateLookupTypeModel);
        call.enqueue(new Callback<UpdateLookupTypeResponse>() {

            @Override
            public void onResponse(Call<UpdateLookupTypeResponse> call, Response<UpdateLookupTypeResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdateHairstyle.this != null && !UpdateHairstyle.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateHairstyle.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateHairstyle.this, HairStyle.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } else{
                        Utils.showAlertDialog(UpdateHairstyle.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<UpdateLookupTypeResponse> call, Throwable t) {
                Toast.makeText(UpdateHairstyle.this, "Fail", Toast.LENGTH_SHORT).show();
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
        String str = hair.getText().toString();
        if (hair.getText().toString().isEmpty()) {
            hair.setError("Hair style should not be empty");
            hair.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            hair.setText(hair.getText().toString().trim());
            hair.setSelection(hair.getText().length());
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateHairstyle.this,HairStyle.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
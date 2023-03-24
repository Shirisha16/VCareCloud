package com.client.vcarecloud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.EventTypeModel;
import com.client.vcarecloud.models.UpdateEventTypeModel;
import com.client.vcarecloud.models.UpdateEventTypeResponse;
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

public class UpdateEventType extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText editeventType;
    AppCompatButton eventTypeButton;

    UserDetails userDetails;
    String message,error;
    String id,custId,typeId,typeName,createdOn,lastChangedOn;

    EventType eventType;
    EventTypeModel eventTypeModel;
    UpdateEventTypeModel updateEventTypeModel;
    UpdateEventTypeResponse updateEventTypeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event_type);

        back=findViewById(R.id.back);
        editeventType=findViewById(R.id.event_type);
        eventTypeButton=findViewById(R.id.edit);
        progress_layout=findViewById(R.id.progress);

        eventType=new EventType();
        eventTypeModel=new EventTypeModel();
        updateEventTypeModel=new UpdateEventTypeModel();
        updateEventTypeResponse=new UpdateEventTypeResponse();

        eventTypeModel=(EventTypeModel)getIntent().getSerializableExtra("list");

        userDetails=new UserDetails(UpdateEventType.this);

        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        id=getIntent().getStringExtra("id");
        custId=getIntent().getStringExtra("custId");
        typeId=getIntent().getStringExtra("typeId");
        typeName=getIntent().getStringExtra("typeName");

        editeventType.setText(typeName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateEventType.this,EventType.class);
                intent.putExtra("list",eventTypeModel);
                startActivity(intent);
                finish();
            }
        });

        eventTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeName=editeventType.getText().toString();

                if (validate()) {
                    updateEventTypeDetails();
                }
            }
        });
    }

    private void updateEventTypeDetails() {
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
        updateEventTypeModel.setId(id);
        updateEventTypeModel.setTypeId(typeId);
        updateEventTypeModel.setCustId(custId);
        updateEventTypeModel.setTypeName(typeName);

        Call<UpdateEventTypeResponse> call=api.update_eventType(id,custId,updateEventTypeModel);
        call.enqueue(new Callback<UpdateEventTypeResponse>() {
            @Override
            public void onResponse(Call<UpdateEventTypeResponse> call, Response<UpdateEventTypeResponse> response) {
                if (response.code()==200){
                    if (response.body().getMessage()!=null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdateEventType.this != null && !UpdateEventType.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateEventType.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateEventType.this, EventType.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    else{
                        Utils.showAlertDialog(UpdateEventType.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateEventTypeResponse> call, Throwable t) {
                Toast.makeText(UpdateEventType.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        if (!type_name()) {
            return false;
        }else{
            return  true;
        }
    }

    private boolean type_name() {
        String str = editeventType.getText().toString();
        if (editeventType.getText().toString().isEmpty()) {
            editeventType.setError("Event type should not be empty");
            editeventType.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            editeventType.setText(editeventType.getText().toString().trim());
            editeventType.setSelection(editeventType.getText().length());
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateEventType.this,EventType.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

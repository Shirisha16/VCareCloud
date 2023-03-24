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
import com.client.vcarecloud.models.AddEventTypeModel;
import com.client.vcarecloud.models.AddEventTypeResponse;
import com.client.vcarecloud.models.EventTypeModel;
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

public class AddEventTypes extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText eventType;
    AppCompatButton addeventType;

    UserDetails userDetails;
    EventTypeModel eventTypeModel;

    String typeId,custId,typeName="",message,errorMessage,createdOn,lastChangedOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event_types);

        back=findViewById(R.id.back);
        eventType=findViewById(R.id.event_type);
        addeventType=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);

        userDetails = new UserDetails(AddEventTypes.this);
        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        eventTypeModel=new EventTypeModel();

        custId=userDetails.getCustId();
        typeId=userDetails.getEmpId();
//        Toast.makeText(AddEventTypes.this, ""+custId+typeId, Toast.LENGTH_SHORT).show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddEventTypes.this, EventType.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        addeventType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeName=eventType.getText().toString();

                if (Validate()) {
                    addNewEventType();
                }
            }
        });
    }

    private void addNewEventType() {
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
        AddEventTypeModel addEventTypeModel=new AddEventTypeModel();
        addEventTypeModel.setTypeId(typeId);
        addEventTypeModel.setCustId(custId);
        addEventTypeModel.setTypeName(typeName);

        Call<AddEventTypeResponse> call=api.addeventType(custId,addEventTypeModel);
        call.enqueue(new Callback<AddEventTypeResponse>() {
            @Override
            public void onResponse(Call<AddEventTypeResponse> call, Response<AddEventTypeResponse> response) {
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddEventTypes.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddEventTypes.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddEventTypes.this, EventType.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else {
                        Utils.showAlertDialog(AddEventTypes.this,response.body().getErrorMessage(),false);
                    }
                    progress_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddEventTypeResponse> call, Throwable t) {
                Toast.makeText(AddEventTypes.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(AddEventTypes.this, EventType.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }

    private boolean Validate() {
        if (!lookup_name()) {
            return false;
        }else{
            return  true;
        }
    }

    private boolean lookup_name() {
        String str = eventType.getText().toString();
        if (eventType.getText().toString().isEmpty()) {
            eventType.setError("Event type should not be empty");
            eventType.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            eventType.setText(eventType.getText().toString().trim());
            eventType.setSelection(eventType.getText().length());
            return false;
        }
        else {
            return true;
        }
    }
}

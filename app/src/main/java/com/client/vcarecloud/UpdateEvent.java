package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddEventTypeModel;
import com.client.vcarecloud.models.EventModel;
import com.client.vcarecloud.models.UpdateEventModel;
import com.client.vcarecloud.models.UpdateEventResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateEvent extends AppCompatActivity {
    ImageView back;
    TextInputEditText editEvent,editLocation,editDescription, textFrom,textTo;
    AutoCompleteTextView eventType_spin;
    AppCompatButton eventTypeButton,edit;
    RelativeLayout progress_layout;

    boolean dateChecked = false;

    String eventId,typeId,custId,eventName,eventLocation,eventDetails,fromDate,
            toDate,eventTypeName, message,errorMessage,eventtypeId,eventTypeName1;

    UserDetails userDetails;

    Events events;
    EventModel eventModel;
    UpdateEventModel updateEventModel;
    UpdateEventResponse updateEventResponse;

    List<AddEventTypeModel> eventTypeModelList;

    ArrayList<String> eventType_Id = new ArrayList<>();
    ArrayList<String> eventType_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        back=findViewById(R.id.back);
        editEvent=findViewById(R.id.event);
        editLocation=findViewById(R.id.locationEvent);
        editDescription=findViewById(R.id.description);
        textFrom=findViewById(R.id.from);
        textTo=findViewById(R.id.to);
        eventType_spin=findViewById(R.id.eventType_spin);
//        eventTypeButton=findViewById(R.id.addEventType);
        edit=findViewById(R.id.edit);
        progress_layout=findViewById(R.id.progress);

        userDetails = new UserDetails(UpdateEvent.this);

        events=new Events();
        eventModel=new EventModel();
        updateEventModel=new UpdateEventModel();
        updateEventResponse=new UpdateEventResponse();

        eventModel=(EventModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

        eventTypeModelList=new ArrayList<>();

        eventId=getIntent().getStringExtra("eventID");
        custId=getIntent().getStringExtra("custId");
        typeId=getIntent().getStringExtra("typeId");
        eventName=getIntent().getStringExtra("eventName");
        eventLocation=getIntent().getStringExtra("eventLocation");
        eventDetails=getIntent().getStringExtra("eventDetails");
        eventTypeName=getIntent().getStringExtra("eventtype");

        fromDate=getIntent().getStringExtra("fromDate");
        if (fromDate!=null){
            fromDate=fromDate.replace("T00:00:00","");
        }

        toDate=getIntent().getStringExtra("toDate");
        if (toDate!=null){
            toDate=toDate.replace("T00:00:00","");
        }

        editEvent.setText(eventName);
        textFrom.setText(fromDate);
        textTo.setText(toDate);
        editLocation.setText(eventLocation);
        eventType_spin.setText(eventTypeName);

        if (eventDetails.equalsIgnoreCase("null")){
            editDescription.setText("");
        }else {
            editDescription.setText(eventDetails);
        }

        getEventTypeList();

//        eventTypeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UpdateEvent.this, EventTypeForUpdate.class);
//                startActivity(intent);
////                finish();
//            }
//        });

        textFrom.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEvent.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                textFrom.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker();
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        });

        textTo.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateEvent.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                String date = day1 + "-" + month1 + "-" + year1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                textTo.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName=editEvent.getText().toString();
                fromDate=textFrom.getText().toString();
                toDate=textTo.getText().toString();
                eventLocation=editLocation.getText().toString();
                eventTypeName=eventType_spin.getText().toString();
                eventDetails=editDescription.getText().toString();

                if (Validate()) {
                    updateEvents();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(UpdateEvent.this,Events.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

    }

    private void updateEvents() {
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
        updateEventModel.setEventId(eventId);
        updateEventModel.setTypeId(typeId);
        updateEventModel.setCustId(custId);
        updateEventModel.setEventName(eventName);
        updateEventModel.setLocation(eventLocation);
        if(eventDetails.isEmpty()){
            updateEventModel.setDetails(null);
        }else {
            updateEventModel.setDetails(eventDetails);
        }
        updateEventModel.setTypeName(eventTypeName);
        updateEventModel.setFromDate(fromDate);
        updateEventModel.setToDate(toDate);

        Call<UpdateEventResponse> call=api.update_events(eventId,"0",updateEventModel);
        call.enqueue(new Callback<UpdateEventResponse>() {
            @Override
            public void onResponse(Call<UpdateEventResponse> call, Response<UpdateEventResponse> response) {
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (UpdateEvent.this!=null&&!UpdateEvent.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(UpdateEvent.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateEvent.this, Events.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateEvent.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateEventResponse> call, Throwable t) {
                Toast.makeText(UpdateEvent.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEventTypeList() {
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
        Call<String> call=api.eventTypeList(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            eventtypeId=jsonObject1.getString("typeId");
                            eventTypeName1=jsonObject1.getString("typeName");
                            eventType_Id.add(eventtypeId);
                            eventType_name.add(eventTypeName1);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(UpdateEvent.this, android.R.layout.simple_dropdown_item_1line, eventType_name);
                        eventType_spin.setAdapter(adapter);

                        eventType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                typeId = eventType_Id.get(i);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateEvent.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean Validate() {
        if (!eventname()) {
            return false;
        } else if (!startDate()) {
            return false;
        } else if (!endDate()) {
            return false;
        }else if (!location()) {
            return false;
        } else if (!eventType1()) {
            return false;
        }else {
            return true;
        }
    }

    private boolean eventname() {
        String str = editEvent.getText().toString();
        if(editEvent.getText().toString().isEmpty()){
            editEvent.setError("Event name should not be empty");
            editEvent.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            editEvent.setText(editEvent.getText().toString().trim());
            editEvent.setSelection(editEvent.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean startDate() {
        if(textFrom.getText().toString().isEmpty()){
            textFrom.setError("From date should not be empty");
            textFrom.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean endDate() {
        if(textTo.getText().toString().isEmpty()){
            textTo.setError("To date should not be empty");
            textTo.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean location() {
        String str = editLocation.getText().toString();
        if(editLocation.getText().toString().isEmpty()){
            editLocation.setError("Location should not be empty");
            editLocation.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            editLocation.setText(editLocation.getText().toString().trim());
            editLocation.setSelection(editLocation.getText().length());
            return false;
        }else {
            return true;
        }
    }

    private boolean eventType1() {
        if(eventType_spin.getText().toString().isEmpty()){
            eventType_spin.setError("Event type should not be empty");
            eventType_spin.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(UpdateEvent.this,Events.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
//        Intent intent=new Intent(UpdateEvent.this,Events.class);
//        startActivity(intent);
//        finish();
    }
}
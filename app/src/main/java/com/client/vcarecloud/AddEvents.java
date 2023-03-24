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
import com.client.vcarecloud.models.AddEventModel;
import com.client.vcarecloud.models.AddEventResponse;
import com.client.vcarecloud.models.AddEventTypeModel;
import com.client.vcarecloud.models.EventModel;
import com.client.vcarecloud.models.EventTypeModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddEvents extends AppCompatActivity {

    ImageView back;
    TextInputEditText editEvent,editLocation,editDescription, textFrom,textTo;
    AutoCompleteTextView eventType_spin;
    AppCompatButton eventTypeButton,addEvent;
    RelativeLayout progress_layout;

    EventModel eventModel;
    EventTypeModel eventTypeModel;
    UserDetails userDetails;

    List<EventTypeModel> eventTypeModelList;

    ArrayList<String> eventType_Id = new ArrayList<>();
    ArrayList<String> eventType_name = new ArrayList<>();

    boolean dateChecked = false;

    String empid,eventId,typeId,custId,eventName="",eventLocation="",eventDetails="",fromDate,
            toDate,eventTypeName, createdOn,lastChangedOn;

    String eventType="";
    String  message,errorMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        back=findViewById(R.id.back);
        editEvent=findViewById(R.id.event);
        editLocation=findViewById(R.id.locationEvent);
        editDescription=findViewById(R.id.description);
        textFrom=findViewById(R.id.from);
        textTo=findViewById(R.id.to);
        eventType_spin=findViewById(R.id.eventType_spin);
//        eventTypeButton=findViewById(R.id.addEventType);
        addEvent=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);

        userDetails = new UserDetails(AddEvents.this);
        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        eventModel=new EventModel();
        eventTypeModel=new EventTypeModel();

        custId=userDetails.getCustId();
        empid=userDetails.getEmpId();
        eventId=userDetails.getEventId();
        typeId=userDetails.getTypeId();

        eventTypeModelList=new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddEvents.this, Events.class);
//                startActivity(intent);
                finish();
//                onBackPressed();
            }
        });

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventName=editEvent.getText().toString();
                fromDate=textFrom.getText().toString();
                toDate=textTo.getText().toString();
                eventLocation=editLocation.getText().toString();
                eventDetails=editDescription.getText().toString();
                eventType=eventType_spin.getText().toString();

                if (Validate()){
                addEvents();
                }
            }
        });

//        eventTypeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AddEvents.this, EventType.class);
//                startActivity(intent);
////                finish();
//            }
//        });

        textFrom.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvents.this, (view, year1, month1, day1) -> {
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
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        textTo.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvents.this, (view, year1, month1, day1) -> {
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

        getEventTypeList();
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

                            typeId=jsonObject1.getString("typeId");
                            eventTypeName=jsonObject1.getString("typeName");
                            eventType_Id.add(typeId);
                            eventType_name.add(eventTypeName);
                        }
                        ArrayAdapter adapter = new ArrayAdapter(AddEvents.this, android.R.layout.simple_dropdown_item_1line, eventType_name);
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
                Toast.makeText(AddEvents.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void addEvents() {
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
        AddEventModel addEventModel = new AddEventModel();
        addEventModel.setCustId(custId);
        addEventModel.setEventName(eventName);
        addEventModel.setLocation(eventLocation);

        if(eventDetails.isEmpty()){
            addEventModel.setDetails(null);
        }else {
            addEventModel.setDetails(eventDetails);
        }

        addEventModel.setTypeId(typeId);
//        addEventModel.setEventtype(eventTypeName);
        addEventModel.setFromDate(fromDate);
        addEventModel.setToDate(toDate);

        Call<AddEventResponse> call = api.addevents("0", addEventModel);
        call.enqueue(new Callback<AddEventResponse>() {
            @Override
            public void onResponse(Call<AddEventResponse> call, Response<AddEventResponse> response) {
                if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        errorMessage = response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddEvents.this.isFinishing()) {

                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddEvents.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddEvents.this, Events.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else {
                        Utils.showAlertDialog(AddEvents.this,response.body().getErrorMessage(),false);
                    }
                    progress_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AddEventResponse> call, Throwable t) {
                Toast.makeText(AddEvents.this, "Fail", Toast.LENGTH_SHORT).show();
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
//        Intent intent =new Intent(AddEvents.this,Events.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        finish();

    }
}
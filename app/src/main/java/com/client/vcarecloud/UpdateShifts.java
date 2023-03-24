package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.ShiftsModel;
import com.client.vcarecloud.models.UpdateShiftModel;
import com.client.vcarecloud.models.UpdateShiftResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateShifts extends AppCompatActivity {
    String custId,empId,id,shiftId,shiftname,starttime,endtime,duration,
            shiftstatus,createdon,lastchangeOn;

    TextInputEditText nameShift,timeStart,timeEnd;
//    MaterialTextView timeStart,timeEnd;
    AppCompatButton updateButton;
    ImageView back;
    int time1_Hour,time1_minute,time2_Hour,time2_minute;
    RelativeLayout progress;

    UserDetails userDetails;
    String message,error;

    Shifts shiftList;
    ShiftsModel shift_model;
    UpdateShiftResponse updateShiftResponse;
    UpdateShiftModel updateShiftsModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shifts);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        nameShift=findViewById(R.id.shift);
//        timeStart=findViewById(R.id.startTimeEdit);
//        timeEnd=findViewById(R.id.endTimeEdit);
        timeStart=findViewById(R.id.startTime1);
        timeEnd=findViewById(R.id.endTime1);
        updateButton=findViewById(R.id.update);
        back=findViewById(R.id.back);
        progress=findViewById(R.id.progress_layout);

        shiftList= new Shifts();
        updateShiftResponse=new UpdateShiftResponse();
        updateShiftsModel=new UpdateShiftModel();
        shift_model=(ShiftsModel)getIntent().getSerializableExtra("list");

        userDetails=new UserDetails(UpdateShifts.this);
        updateShiftsModel=new UpdateShiftModel();
        shift_model=new ShiftsModel();


        createdon = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastchangeOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        id=getIntent().getStringExtra("shiftId");
        empId=getIntent().getStringExtra("EmpId");
        shiftId=getIntent().getStringExtra("shiftId");
        custId=getIntent().getStringExtra("custId");
        shiftname=getIntent().getStringExtra("shiftName");
        starttime=getIntent().getStringExtra("startTime");
        endtime=getIntent().getStringExtra("endTime");
        duration=getIntent().getStringExtra("shiftDuration");
        shiftstatus=getIntent().getStringExtra("shiftStatus");


        nameShift.setText(shiftname);
        timeStart.setText(starttime);
        timeEnd.setText(endtime);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateShifts.this,Shifts.class);
                startActivity(intent);
                finish();
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog=new TimePickerDialog(UpdateShifts.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time1_Hour=hourOfDay;
                        time1_minute=minute;

                        Calendar calendar=Calendar.getInstance();
                        calendar.set(0,0,0,time1_Hour,time1_minute);
                        timeStart.setText(DateFormat.format("HH:mm",calendar));
                    }
                },12,0,false);
                timePickerDialog.updateTime(time1_Hour,time1_minute);
                timePickerDialog.show();
            }
        });

        timeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(UpdateShifts.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time2_Hour=hourOfDay;
                        time2_minute=minute;

                        Calendar calendar=Calendar.getInstance();
                        calendar.set(0,0,0,time2_Hour,time2_minute);
                        timeEnd.setText(DateFormat.format("HH:mm",calendar));
                    }
                },12,0,false);
                timePickerDialog.updateTime(time2_Hour,time2_minute);
                timePickerDialog.show();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shiftname=nameShift.getText().toString();
                starttime=timeStart.getText().toString();
                endtime=timeEnd.getText().toString();

                if (Validate()) {
                    updateShiftDetails();
                }
            }
        });
    }

    private void updateShiftDetails() {
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

        updateShiftsModel.setId(shiftId);
        updateShiftsModel.setEmpid(String.valueOf(0));
        updateShiftsModel.setShift_id(shiftId);
        updateShiftsModel.setCust_id(custId);
        updateShiftsModel.setShift_name(shiftname);
        updateShiftsModel.setStart_time(starttime);
        updateShiftsModel.setEnd_time(endtime);
        updateShiftsModel.setShift_duration(duration);
        updateShiftsModel.setShift_status(shiftstatus);

        Call<UpdateShiftResponse> call=api.update_shifts(shiftId,"0",updateShiftsModel);
        call.enqueue(new Callback<UpdateShiftResponse>() {
            @Override
            public void onResponse(Call<UpdateShiftResponse> call, Response<UpdateShiftResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.code()==200){
                    message=response.body().getMessage();
                    error=response.body().getErrorMessage();
                    if (response.body().getMessage()!=null){
                        if (UpdateShifts.this!=null&&!UpdateShifts.this.isFinishing()){
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(UpdateShifts.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
//                                Toast.makeText(UpdateShifts.this,message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UpdateShifts.this, Shifts.class);
                                startActivity(intent);
                                finish();

                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();

                        }
                    }else{
                        Utils.showAlertDialog(UpdateShifts.this,response.body().getErrorMessage(),false);
                    }
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateShiftResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateShifts.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateShifts.this,Shifts.class);
        startActivity(intent);
        finish();
    }
    private boolean Validate() {

        if (!shiftName()) {
            return false;
        } else if (!startTime()) {
            return false;
        } else if (!endTime()) {
            return false;
        }else if(!difference()){
            return false;
        }
        else {
            return true;
        }
    }

    private boolean difference() {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        try {
            date1 = format.parse(starttime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = format.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long mills = date1.getTime() - date2.getTime();
        int hours = (int) (mills/(1000 * 60 * 60));
        int mins = (int) (mills/(1000*60)) % 60;

        String diff = hours + ":" + mins;
//        Toast.makeText(UpdateShifts.this, ""+diff, Toast.LENGTH_SHORT).show();
        if(diff.equalsIgnoreCase("0:0") || diff.equalsIgnoreCase("-1:0") || diff.equalsIgnoreCase("-2:0")) {
            Utils.showAlertDialog(UpdateShifts.this,"Time difference should be more than 2 hours!",false);
            return false;
        } else if(diff.equalsIgnoreCase("1:0") || diff.equalsIgnoreCase("2:0") || diff.equalsIgnoreCase("3:0") ||diff.equalsIgnoreCase("4:0") ||diff.equalsIgnoreCase("5:0")){
            Utils.showAlertDialog(UpdateShifts.this,"End time should be after Start time",false);
            return false;
        }
        else{
            return true;
        }
    }

    private boolean endTime() {
        if (timeEnd.getText().toString().isEmpty()) {
            timeEnd.setError("End time should not be empty");
            timeEnd.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean startTime() {
        if (timeStart.getText().toString().isEmpty()) {
            timeStart.setError("Start time should not be empty");
            timeStart.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean shiftName() {
        String str = nameShift.getText().toString();
        if (nameShift.getText().toString().isEmpty()) {
            nameShift.setError("Shift name should not be empty");
            nameShift.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            nameShift.setText(nameShift.getText().toString().trim());
            nameShift.setSelection(nameShift.getText().length());
            return false;
        } else {
            return true;
        }
    }
}
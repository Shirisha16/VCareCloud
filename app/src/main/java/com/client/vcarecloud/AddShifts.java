package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddShiftModel;
import com.client.vcarecloud.models.AddShiftResponse;
import com.client.vcarecloud.models.ShiftsModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddShifts extends AppCompatActivity {
    TextInputEditText edit_shift,text_start,text_end;
//    TextView text_start;
//    MaterialTextView text_end;
    int time1_Hour,time1_minute,time2_Hour,time2_minute;
    AppCompatButton submitButton;
    RelativeLayout progress_layout;
    ImageView back;

    ShiftsModel shift_model;
    UserDetails userDetails;

    String shiftId,shiftDuration,shiftStatus,createdOn,lastChangedOn,status;
    String empId=null,custId,message,errorMessage;

    String shift_name = "";
    String start_time = "";
    String end_time = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shifts);

        edit_shift=findViewById(R.id.shiftname1);
        text_start=findViewById(R.id.startTime1);
        text_end=findViewById(R.id.endTime1);
        submitButton=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);
        back = findViewById(R.id.back);

        userDetails = new UserDetails(AddShifts.this);
        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        shift_model=new ShiftsModel();
//        empId=userDetails.getEmp_Id();
        shiftId = userDetails.getShift_Id();
        custId = userDetails.getCustId();
        shift_name=userDetails.getShift_name();
       
        shiftDuration=userDetails.getDuration();
        shiftStatus=userDetails.getShiftstatus();

        text_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog timePickerDialog=new TimePickerDialog(AddShifts.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time1_Hour=hourOfDay;
                        time1_minute=minute;

                        Calendar calendar=Calendar.getInstance();
                        calendar.set(0,0,0,time1_Hour,time1_minute);
                        text_start.setText(DateFormat.format("HH:mm",calendar));
                    }
                },12,0,false);
                timePickerDialog.updateTime(time1_Hour,time1_minute);
                timePickerDialog.show();
            }
        });

        text_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog=new TimePickerDialog(AddShifts.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time2_Hour=hourOfDay;
                        time2_minute=minute;

                        Calendar calendar=Calendar.getInstance();
                        calendar.set(0,0,0,time2_Hour,time2_minute);
                        text_end.setText(DateFormat.format("HH:mm",calendar));
                    }
                },12,0,false);
                timePickerDialog.updateTime(time2_Hour,time2_minute);
                timePickerDialog.show();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shift_name=edit_shift.getText().toString();
                start_time=text_start.getText().toString();
                end_time=text_end.getText().toString();
                if (Validate()){
                    addShift();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(AddShifts.this, Shifts.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });
    }

    private void addShift() {
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
        AddShiftModel addShiftModel=new AddShiftModel();
        addShiftModel.setEmpId(empId);
        addShiftModel.setShiftId(shiftId);
        addShiftModel.setCustId(Integer.parseInt(custId));
        addShiftModel.setShiftName(shift_name);
        addShiftModel.setStartTime(text_start.getText().toString());
        addShiftModel.setEndTime(text_end.getText().toString());
        addShiftModel.setShiftDuration(shiftDuration);
        addShiftModel.setShiftStatus(status);
        addShiftModel.setCreatedOn(createdOn);
        addShiftModel.setLastChangedOn(lastChangedOn);
        addShiftModel.setStatus("Active");

        Call<AddShiftResponse> call=api.add_shifts("0",addShiftModel);
        call.enqueue(new Callback<AddShiftResponse>() {
            @Override
            public void onResponse(Call<AddShiftResponse> call, Response<AddShiftResponse> response) {
           if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddShifts.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddShifts.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddShifts.this, Shifts.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    } else {
                        Utils.showAlertDialog(AddShifts.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddShiftResponse> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(AddShifts.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(AddShifts.this, Shifts.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }

    private boolean Validate() {
        if (!shiftName()) {
            return false;
        } else if (!startTime()) {
            return false;
        } else if (!endTime()) {
            return false;
        } else if(!difference()){
            return false;
        } else{
            return true;
        }
    }

    private boolean difference() {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = null;
        try {
            date1 = format.parse(start_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date2 = null;
        try {
            date2 = format.parse(end_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = date1.getTime() - date2.getTime();

        int hours = (int) (difference/(1000 * 60 * 60));
        int mins = (int) (difference/(1000*60)) % 60;


        String diff = hours + ":" + mins;
//        Toast.makeText(AddShifts.this, ""+diff, Toast.LENGTH_SHORT).show();

//        if(diff.compareTo("-2:0")){
//            Utils.showAlertDialog(AddShifts.this,"Time difference should be more than 2 hours",false);
//            return false;
//        }
//        if(diff.equalsIgnoreCase("0:0") || diff.equalsIgnoreCase("-1:0") || diff.equalsIgnoreCase("-2:0")) {
        if(diff.equalsIgnoreCase("0:0") || diff.equalsIgnoreCase("-1:0") || diff.equalsIgnoreCase("-2:0")) {
            Utils.showAlertDialog(AddShifts.this,"Time difference should be more than 2 hours!",false);
            return false;
        } else if(diff.equalsIgnoreCase("1:0") || diff.equalsIgnoreCase("2:0") || diff.equalsIgnoreCase("3:0") ||diff.equalsIgnoreCase("4:0") ||diff.equalsIgnoreCase("5:0")){
            Utils.showAlertDialog(AddShifts.this,"End time should be after Start time",false);
            return false;
        }
        else{
            return true;
        }
    }

//    private boolean difference() {
//        if(start_time==end_time){
//            Utils.showAlertDialog(AddShifts.this,"The time gap should be minimum of 2 hours",false);
//            return false;
//        } else {
//            return true;
//        }
//        long difference=text_end.getText().toString()-text_start.getText().toString();
//        int days = (int) (difference / (1000*60*60*24));
//        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
//        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
//    }

    private boolean shiftName() {
        String str = edit_shift.getText().toString();
        if (edit_shift.getText().toString().isEmpty()) {
            edit_shift.setError("Shift name should not be empty");
            edit_shift.requestFocus();
            return false;
        }else if(str.length() > 0 && str.startsWith(" ")) {
            edit_shift.setText(edit_shift.getText().toString().trim());
            edit_shift.setSelection(edit_shift.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean startTime() {
        if (start_time.isEmpty()) {
            Utils.showAlertDialog(AddShifts.this,"Start time should not be empty",false);
            return false;
        } else {
            return true;
        }
    }

    private boolean endTime() {
        if (text_end.getText().toString().isEmpty()) {
            Utils.showAlertDialog(AddShifts.this,"End time should not be empty",false);
            return false;
        } else {
            return true;
        }
    }

//    private boolean difference(){
//        boolean result;
//        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//        Date date1 = null;
//        try {
//            date1 = format.parse(text_start.getText().toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date date2 = null;
//        try {
//            date2 = format.parse(text_end.getText().toString());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        long difference=date2.getTime() -date1.getTime();
//        int days = (int) (difference / (1000*60*60*24));
//        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
//        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
//        if (difference >= min) {
//            Utils.showAlertDialog(AddShifts.this, "The time gap should be minimum of 2 hours", false);
//            result = false;
//        } else {
//            result = true;
//        }
//        return result;
//    }

}
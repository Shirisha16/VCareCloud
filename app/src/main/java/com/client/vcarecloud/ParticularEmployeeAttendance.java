package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.ParticularEmployeeAttendanceAdapter;
import com.client.vcarecloud.models.EmpCheckInRequest;
import com.client.vcarecloud.models.EmpCheckInResponse;
import com.client.vcarecloud.models.ParticularEmployeeAttendanceModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ParticularEmployeeAttendance extends AppCompatActivity implements LoadDetails {
    RecyclerView empAttendanceRecycler;
    ArrayList<ParticularEmployeeAttendanceModel> empAttendanceModelArrayList = new ArrayList<>();
    AppCompatButton checkIn;
    ImageView back, noData, cancel;
    RelativeLayout progress;
    UserDetails userDetails;
    EditText search;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipe;
    AlertDialog.Builder builder;
    ParticularEmployeeAttendanceAdapter adapter;
    String custId, message, error, empId, endDate, endTime, employeeId, dates, time, date,
            empMessage,empErrorMessage;

    String endTime1;

    LoadDetails loadDetails;
    ParticularEmployeeAttendanceModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_employee_attendance);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        empAttendanceRecycler = findViewById(R.id.empAttendanceRecycler);
        empAttendanceRecycler.setHasFixedSize(true);
        userDetails = new UserDetails(ParticularEmployeeAttendance.this);
        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        cancel = findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        checkIn = findViewById(R.id.checkin);
        noData = findViewById(R.id.noData_leaveList_admin_approved);
        linearLayout = findViewById(R.id.linear1);
        adapter = new ParticularEmployeeAttendanceAdapter(empAttendanceModelArrayList, ParticularEmployeeAttendance.this, loadDetails);
        progress = findViewById(R.id.progressLayout);
        swipe = findViewById(R.id.swipe);
        empAttendanceResponse();
        endDate = getIntent().getStringExtra("date");
        endTime = getIntent().getStringExtra("time");
        employeeId = userDetails.getEmpID();
        custId = userDetails.getCustId();
        empMessage = userDetails.getMessage();
//        empErrorMessage=getIntent().getStringExtra("errorMessage");

        empErrorMessage=userDetails.getErrorMessage();

        time = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        date = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault()).format(new Date());
        dates = date + time;
        builder = new AlertDialog.Builder(ParticularEmployeeAttendance.this);
        empId = userDetails.getEmpID();

        if (empMessage.equalsIgnoreCase("Checked out Successfully")) {
            checkIn.setText("CheckIn");
            checkIn.setBackgroundResource(R.color.lightGreen);
        } else if (empMessage.equalsIgnoreCase("Checked in Successfully")){
            checkIn.setText("CheckOut");
            checkIn.setBackgroundResource(R.color.lightRed);
        }else{
            checkIn.setText("CheckOut");
            checkIn.setBackgroundResource(R.color.lightRed);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
                    Intent intent = new Intent(ParticularEmployeeAttendance.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
                    Intent intent = new Intent(ParticularEmployeeAttendance.this, EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkIn.setEnabled(false);
                String mess  = userDetails.getMessage();

                if (model.getShiftEndDate().equalsIgnoreCase("Not Checked Out")){
//                    checkIn.setText("CheckIn");
                    userDetails.setMessage("");
                    String mess1  = userDetails.getMessage();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ParticularEmployeeAttendance.this);
                    builder.setMessage("Do you want to check Out?");
                    builder.setPositiveButton("ok", (dialogInterface, i) -> {
                        empCheckInResponse(empId, employeeId, custId, dates);
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ParticularEmployeeAttendance.this, ParticularEmployeeAttendance.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if (userDetails.getMessage().equalsIgnoreCase("Checked in Successfully")) {
//                    checkIn.setText("CheckOut");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ParticularEmployeeAttendance.this);
                    builder.setMessage("Do you want to check Out?");
                    builder.setPositiveButton("ok", (dialogInterface, i) -> {
                        empCheckInResponse(empId, employeeId, custId, dates);
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ParticularEmployeeAttendance.this, ParticularEmployeeAttendance.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(empMessage.equalsIgnoreCase("Checked out Successfully")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ParticularEmployeeAttendance.this);
                    builder.setMessage("Do you want to check In");
                    builder.setPositiveButton("ok", (dialogInterface, i) -> {
                        empCheckInResponse(empId, employeeId, custId, dates);

                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ParticularEmployeeAttendance.this, ParticularEmployeeAttendance.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ParticularEmployeeAttendance.this);
                    builder.setMessage("Do you want to check In?");
                    builder.setPositiveButton("ok", (dialogInterface, i) -> {
                        empCheckInResponse(empId, employeeId, custId, dates);
                    });
                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ParticularEmployeeAttendance.this, ParticularEmployeeAttendance.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
//                Utils.preventTwoClick(view);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipe.setRefreshing(false);
                        empAttendanceModelArrayList.clear();
                        empAttendanceResponse();
                        search.setText("");
                        noData.setVisibility(View.GONE);

                    }
                }, 2000);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                if (search.getText().toString().isEmpty()) {
                    cancel.setVisibility(View.INVISIBLE);
//                    childrenWaitList();
                } else {
                    cancel.setVisibility(View.VISIBLE);

                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                cancel.setVisibility(View.INVISIBLE);
                search.clearFocus();

                empAttendanceRecycler.setVisibility(View.VISIBLE);

            }
        });
    }

    private void empCheckInResponse(String empId, String employeeId, String custId, String dates) {
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
        EmpCheckInRequest body = new EmpCheckInRequest();
        body.setEmpId(empId);
        body.setEmployeeId(employeeId);
        body.setCustId(custId);
        body.setDates(dates);
        Call<EmpCheckInResponse> call = api.empCheckIn(empId, body);
        call.enqueue(new Callback<EmpCheckInResponse>() {
            @Override
            public void onResponse(Call<EmpCheckInResponse> call, Response<EmpCheckInResponse> response) {
                progress.setVisibility(View.GONE);
                if (response.code() == 200) {

                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        userDetails.setMessage(response.body().getMessage());
                        userDetails.setErrorMessage(response.body().getErrorMessage());
                        if (message.equalsIgnoreCase("Checked in Successfully")) {

                            Toast.makeText(ParticularEmployeeAttendance.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ParticularEmployeeAttendance.this, ParticularEmployeeAttendance.class);
                            startActivity(intent);

                        } else if (message.equalsIgnoreCase("Checked out Successfully")) {

                            Toast.makeText(ParticularEmployeeAttendance.this, message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ParticularEmployeeAttendance.this, ParticularEmployeeAttendance.class);
                            startActivity(intent);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EmpCheckInResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ParticularEmployeeAttendance.this, message, Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

    private void filter(String text) {
        ArrayList<ParticularEmployeeAttendanceModel> filteredNames = new ArrayList<>();
        for (ParticularEmployeeAttendanceModel s : empAttendanceModelArrayList) {
            if ((s.getDayStart().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getDayEnd().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }

        if (empAttendanceModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                empAttendanceRecycler.setVisibility(View.VISIBLE);
            }

            adapter.filterList(filteredNames);
        }
    }

    private void empAttendanceResponse() {
        progress.setVisibility(View.VISIBLE);

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
        Call<String> call = api.emp_attendanceList(userDetails.getEmpID());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.setVisibility(View.GONE);
                if (response.body() != null) {
                    checkIn.setEnabled(true);
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                model = new ParticularEmployeeAttendanceModel();
                                model.setEmployeeId(jsonObject1.getString("employeeId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setEmployeeName(jsonObject1.getString("employeeName"));
                                model.setShiftName(jsonObject1.getString("shiftName"));

                                String dayStart = jsonObject1.getString("dayStart");
                                String[] parts = dayStart.split("T");
                                String daystartDate = parts[0];
                                model.setDayStart(daystartDate);

                                String startTime = jsonObject1.getString("shiftStartDate");
                                String[] start = startTime.split("T");
                                String shiftStartTime = start[1];
                                model.setShiftStartDate(shiftStartTime);
//                                        model.setShiftEndDate(shiftStartTime);

                                String dayEnd = jsonObject1.getString("dayEnd");
                                String[] end = dayEnd.split("T");
                                String dayEndDate = end[0];
                                if (dayEndDate.equalsIgnoreCase("null")) {
                                    model.setDayEnd("Not Checked Out");
                                }
                                else {
                                    model.setDayEnd(dayEndDate);
                                }
                                String endTime = jsonObject1.getString("shiftEndDate");
                                String[] endT = endTime.split("T");

                                for (String endTime1 : endT){
                                    if(endTime1.equalsIgnoreCase("null")){
                                        checkIn.setText("CheckOut");
                                        checkIn.setBackgroundResource(R.color.lightRed);
                                        model.setShiftEndDate("Not Checked Out");

                                    }
                                    else {
                                        checkIn.setText("CheckIn");
                                        checkIn.setBackgroundResource(R.color.lightGreen);
                                        model.setShiftEndDate(endTime1);
                                    }
                                }
                                empAttendanceModelArrayList.add(model);

                                message = jsonObject.getString("message");
                                error = jsonObject.getString("errorMessage");
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ParticularEmployeeAttendance.this, RecyclerView.VERTICAL, false);
                            empAttendanceRecycler.setLayoutManager(linearLayoutManager);

                            adapter = new ParticularEmployeeAttendanceAdapter(empAttendanceModelArrayList, ParticularEmployeeAttendance.this, ParticularEmployeeAttendance.this);
                            empAttendanceRecycler.setAdapter(adapter);
                        } else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    progress.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ParticularEmployeeAttendance.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(ParticularEmployeeAttendance.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(ParticularEmployeeAttendance.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onMethodCallback() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }
}
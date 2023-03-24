package com.client.vcarecloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.client.vcarecloud.Adapters.EmployeeAttendanceAdapter;
import com.client.vcarecloud.models.EmpAttendanceModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EmployeeAttendance extends AppCompatActivity implements LoadDetails{
    RecyclerView empAttendanceRecycler;
    ArrayList<EmpAttendanceModel> empAttendanceModelArrayList=new ArrayList<>();

    ImageView back,noData,cancel;
    RelativeLayout progress;
    UserDetails userDetails;
    EditText search;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipe;
    EmployeeAttendanceAdapter adapter;
    String message,error,empId;
    LoadDetails loadDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_attendance);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        empAttendanceRecycler=findViewById(R.id.empAttendanceRecycler);
        empAttendanceRecycler.setHasFixedSize(true);
        userDetails=new UserDetails(EmployeeAttendance.this);
        back=findViewById(R.id.back);
        search=findViewById(R.id.search);
        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        noData=findViewById(R.id.noData_leaveList_admin_approved);
        linearLayout=findViewById(R.id.linear1);
        progress=findViewById(R.id.progressLayout);
        swipe=findViewById(R.id.swipe);

        adapter=new EmployeeAttendanceAdapter(empAttendanceModelArrayList,EmployeeAttendance.this,loadDetails);

        empAttendanceResponse();
        empId= userDetails.getEmpID();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(EmployeeAttendance.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(EmployeeAttendance.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
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
                if (search.getText().toString().isEmpty()){
                    cancel.setVisibility(View.INVISIBLE);
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
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        if(search.getText().toString().length()>0){
                            swipe.setRefreshing(false);

                        }else{
                            swipe.setRefreshing(false);
                            empAttendanceModelArrayList.clear();
                            empAttendanceResponse();
                        }
                    }
                }, 2000);
            }
        });
    }

    private void filter(String text) {
        ArrayList<EmpAttendanceModel> filteredNames=new ArrayList<>();
        for (EmpAttendanceModel s:empAttendanceModelArrayList){
            if ((s.getEmployees().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getDayStart().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getDayEnd().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getShiftStartDate().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getShiftEndDate().toLowerCase().contains(text.toLowerCase())) ||
                 (s.getShifts().toLowerCase().contains(text.toLowerCase()))) {
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
                .connectTimeout(90,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();
        VcareApi api=retrofit.create(VcareApi.class);
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Call<String> call=api.emp_attendance_listAdmin(userDetails.getCustId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progress.setVisibility(View.GONE);
                    if (response.body()!=null){
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("model");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    EmpAttendanceModel model = new EmpAttendanceModel();
                                    model.setEmployees(jsonObject1.getString("employeeName"));
                                    model.setShifts(jsonObject1.getString("shiftName"));
                                    String  dayStart=jsonObject1.getString("dayStart");
                                    String[] parts=dayStart.split("T");
                                    String daystartDate=parts[0];
                                    model.setDayStart(daystartDate);

                                    String startTime=jsonObject1.getString("shiftStartDate");
                                    String[] start=startTime.split("T");
                                    String shiftStartTime=start[1];

                                    model.setShiftStartDate(shiftStartTime);

                                    String dayEnd=jsonObject1.getString("dayEnd");
                                    String[] end=dayEnd.split("T");
                                    String dayEndDate=end[0];
                                    if(dayEndDate.equalsIgnoreCase("null")){
                                        model.setDayEnd("Not Checked out");
                                    }else {
                                        model.setDayEnd(dayEndDate);
                                    }

                                String endTime=jsonObject1.getString("shiftEndDate");
                                String[] endT=endTime.split("T",2);
                                for (String shiftEndTime1 :endT)
                                    if(dayEndDate.equalsIgnoreCase("null")){
                                        model.setShiftEndDate("Not Checked out");
                                    }else {
                                        model.setShiftEndDate(shiftEndTime1);
                                    }
                                    empAttendanceModelArrayList.add(model);

                                    message = jsonObject.getString("message");
                                    error = jsonObject.getString("errorMessage");
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EmployeeAttendance.this, RecyclerView.VERTICAL, false);
                                empAttendanceRecycler.setLayoutManager(linearLayoutManager);

                                adapter = new EmployeeAttendanceAdapter(empAttendanceModelArrayList, EmployeeAttendance.this,EmployeeAttendance.this);
                                empAttendanceRecycler.setAdapter(adapter);
                            }else if (jsonObject.optString("message").equalsIgnoreCase("null")){
                                noData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        Utils.showAlertDialog(EmployeeAttendance.this,"No Data Found",false);
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
                    Toast.makeText(EmployeeAttendance.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Call<String> call=api.emp_attendanceList(userDetails.getEmpId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progress.setVisibility(View.GONE);
                    if (response.body()!=null){
                        try {
                            JSONObject jsonObject=new JSONObject(response.body());
                            if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("model");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    EmpAttendanceModel model = new EmpAttendanceModel();
                                    model.setEmployees(jsonObject1.getString("employeeName"));
                                    model.setShifts(jsonObject1.getString("shiftName"));
                                    String  dayStart=jsonObject1.getString("dayStart");
                                    String[] parts=dayStart.split("T");
                                    String daystartDate=parts[0];
                                    model.setDayStart(daystartDate);

                                    String startTime=jsonObject1.getString("shiftStartDate");
                                    String[] start=startTime.split("T");
                                    String shiftStartTime=start[1];

                                    model.setShiftStartDate(shiftStartTime);

                                    String dayEnd=jsonObject1.getString("dayEnd");
                                    String[] end=dayEnd.split("T");
                                    String dayEndDate=end[0];
                                    model.setDayEnd(dayEndDate);

                                    String endTime=jsonObject1.getString("shiftEndDate");
                                    String[] endT=endTime.split("T",2);
                                    for (String shiftEndTime1 :endT)
                                        if(shiftEndTime1.equalsIgnoreCase("null")){
                                            model.setShiftEndDate("Not Checked Out");
                                        }else {
                                            model.setShiftEndDate(shiftEndTime1);
                                        }

                                    empAttendanceModelArrayList.add(model);

                                    message = jsonObject.getString("message");
                                    error = jsonObject.getString("errorMessage");
                                }
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EmployeeAttendance.this, RecyclerView.VERTICAL, false);
                                empAttendanceRecycler.setLayoutManager(linearLayoutManager);

                                adapter = new EmployeeAttendanceAdapter(empAttendanceModelArrayList, EmployeeAttendance.this,EmployeeAttendance.this);
                                empAttendanceRecycler.setAdapter(adapter);
                            }else if (jsonObject.optString("message").equalsIgnoreCase("null")){
                                noData.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
                    Toast.makeText(EmployeeAttendance.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }
    }

    @Override
    public void onMethodCallback() {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(EmployeeAttendance.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(EmployeeAttendance.this,EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }
}
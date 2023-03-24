package com.client.vcarecloud;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.ChildCheckIn;
import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.ChildrenListAdapter;
import com.client.vcarecloud.models.CheckInRequest;
import com.client.vcarecloud.models.CheckInResponse;
import com.client.vcarecloud.models.ChildCheckInModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.models.childrenListModel;
import com.client.vcarecloud.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class ChildrenList extends AppCompatActivity implements ChildCheckIn, LoadDetails{
    RecyclerView childrenRecycler;
    ArrayList<childrenListModel> listModelArrayList=new ArrayList<>();
    ImageView back,noData,cancel;
    RelativeLayout progress;
    UserDetails userDetails;
    EditText search;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipe;
    ChildrenListAdapter adapter;
    String message,error;
    LoadDetails loadDetails;

    String custID,childID,childName,checkDates,attendance,empid,errorMessage,checkMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_list);
        this.setFinishOnTouchOutside(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        childrenRecycler=findViewById(R.id.childrenListRecycler);
        childrenRecycler.setHasFixedSize(true);
        userDetails=new UserDetails(ChildrenList.this);
        back=findViewById(R.id.back);
        search=findViewById(R.id.search);
        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        // ...but notify us that it happened.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        noData=findViewById(R.id.noData_leaveList_admin_approved);
        linearLayout=findViewById(R.id.linear1);

        progress=findViewById(R.id.progressLayout);
        swipe=findViewById(R.id.swipe);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                            swipe.setRefreshing(false);
                            listModelArrayList.clear();
                            childrenList();
                            search.setText("");
                            noData.setVisibility(View.GONE);

                    }
                }, 2000);
            }
        });

        childrenList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(ChildrenList.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(ChildrenList.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
                if (search.getText().toString().isEmpty()) {
                    cancel.setVisibility(View.INVISIBLE);

                } else {

                    cancel.setVisibility(View.VISIBLE);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                search.setText("");
                cancel.setVisibility(View.INVISIBLE);

                search.clearFocus();
                childrenRecycler.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // If we've received a touch notification that the user has touched
        // outside the app, finish the activity.
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()) {
            finish();
            return true;
        }

        // Delegate everything else to Activity.
        return super.onTouchEvent(event);
    }

    private void filter(String text) {
        ArrayList<childrenListModel> filteredNames=new ArrayList<>();
        for (childrenListModel s:listModelArrayList){
            if ((s.getChildName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getClassname().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getDob().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getParentName().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }

        if (listModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                childrenRecycler.setVisibility(View.VISIBLE);
            }
            adapter.filterList(filteredNames);
        }
    }

    private void childrenList() {
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
        Call<String> call=api.children_list(userDetails.getCustId());
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
                                childrenListModel model = new childrenListModel();
                                model.setChildId(jsonObject1.getString("childId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setChildName(jsonObject1.getString("childName"));
                                model.setDob(jsonObject1.getString("dob"));
                                model.setParentName(jsonObject1.getString("parentName"));
                                model.setClassname(jsonObject1.getString("classname"));
                                listModelArrayList.add(model);

                                message = jsonObject.getString("message");
                                error = jsonObject.getString("errorMessage");
                            }
                            recycler();
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
                Toast.makeText(ChildrenList.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }

       public void recycler(){
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChildrenList.this, RecyclerView.VERTICAL, false);
         childrenRecycler.setLayoutManager(linearLayoutManager);

         adapter=new ChildrenListAdapter(listModelArrayList,ChildrenList.this,loadDetails,this);
         childrenRecycler.setAdapter(adapter);
       }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(ChildrenList.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(ChildrenList.this,EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void childCheckIn(String childId, String name, String dates, String empId, String custId) {
        dates = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        childID=childId;
        childName=name;
        checkDates=dates;
        empid=empId;
//        attendance="0";
        custID=custId;

        OkHttpClient.Builder httpClient=new OkHttpClient.Builder()
                .callTimeout(2,TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        CheckInRequest checkInRequest=new CheckInRequest();
        checkInRequest.setCustId(custID);
        checkInRequest.setChildId(childID);
        checkInRequest.setName(childName);
        checkInRequest.setDates(checkDates);
//        checkInRequest.setChildAttendanceId(attendance);
        checkInRequest.setEmpid(empid);

        Call<ChildCheckInModel> call=api.checkinOut(checkInRequest);
        call.enqueue(new Callback<ChildCheckInModel>() {
            @Override
            public void onResponse(Call<ChildCheckInModel> call, Response<ChildCheckInModel> response) {
                if (response.code() == 200) {

                    checkMessage = response.body().getMessage();
                    errorMessage = response.body().getErrorMessage();

                    if(checkMessage!=null){
                        if(checkMessage.equalsIgnoreCase("Do you want to Check In "+childName+"?") || checkMessage.equalsIgnoreCase("Do you want to Check out "+childName+"?")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ChildrenList.this);
                            builder.setMessage(checkMessage);
                            builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                                checkInResponse();
                            });

                            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ChildrenList.this, ChildrenList.class);
                                    startActivity(intent);
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }else{
                    Utils.showAlertDialog(ChildrenList.this,errorMessage,false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ChildCheckInModel> call, Throwable t) {
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ChildrenList.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMethodCallback() {

    }

    public void checkInResponse(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        CheckInRequest body = new CheckInRequest();
        body.setChildId(childID);
        body.setName(childName);
        body.setDates(checkDates);
        body.setChildAttendanceId("0");
        body.setEmpid(String.valueOf(empid));
        body.setCustId(custID);

        Call<CheckInResponse> call = api.check_in(body);
        call.enqueue(new Callback<CheckInResponse>() {
            @Override
            public void onResponse(Call<CheckInResponse> call, Response<CheckInResponse> response) {
                if (response.code() == 200) {
                    message=response.body().getMessage();
                    error=response.body().getErrorMessage();
                    if(message!=null){
//                        if (message.equalsIgnoreCase(childName+ "Checked in Successfully") || message.equalsIgnoreCase(childName+ "Checked out Successfully")) {
                            Toast.makeText(ChildrenList.this, message, Toast.LENGTH_SHORT).show();
                            if(userDetails.getUserType().equalsIgnoreCase("Admin")) {
                                Intent intent = new Intent(ChildrenList.this, Dashboard.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(ChildrenList.this, EmployeeDashboard.class);
                                startActivity(intent);
                            }
                        }
//                    }
                    else{
                        Utils.showAlertDialog(ChildrenList.this,error,false);

                    }
                }
            }

            @Override
            public void onFailure(Call<CheckInResponse> call, Throwable t) {
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ChildrenList.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
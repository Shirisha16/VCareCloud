package com.client.vcarecloud;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.EmployeeListAdapter;
import com.client.vcarecloud.models.EmployeeListModel;
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

public class EmployeeList extends AppCompatActivity implements LoadDetails{

    RecyclerView empListRecycler;
    ArrayList<EmployeeListModel> employeeListModelArrayList=new ArrayList<>();

    ImageView back,noData,cancel;
    RelativeLayout progress;
    UserDetails userDetails;
    EditText search;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipe;
    EmployeeListAdapter adapter;
    String message,error;
    LoadDetails loadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        empListRecycler=findViewById(R.id.empListRecycler);
        empListRecycler.setHasFixedSize(true);
        userDetails=new UserDetails(EmployeeList.this);
        back=findViewById(R.id.back);
        search=findViewById(R.id.search);
        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        noData=findViewById(R.id.noData_leaveList_admin_approved);
        linearLayout=findViewById(R.id.linear1);
        progress=findViewById(R.id.progressLayout);
        swipe=findViewById(R.id.swipe);

        adapter=new EmployeeListAdapter(employeeListModelArrayList, EmployeeList.this,loadDetails);

        empAttendanceResponse();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(EmployeeList.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(EmployeeList.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
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
                            employeeListModelArrayList.clear();
                            empAttendanceResponse();
                        }
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
                empListRecycler.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filter(String text) {
        ArrayList<EmployeeListModel> filteredNames=new ArrayList<>();
        for (EmployeeListModel s:employeeListModelArrayList){
            if ((s.getFirstName().toLowerCase().contains(text.toLowerCase())) ||
                 (s.getHomePhone().toLowerCase().contains(text.toLowerCase())) ||
                 (s.getSecurityProfile().toLowerCase().contains(text.toLowerCase())) ||
                 (s.getDesignation().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (employeeListModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                empListRecycler.setVisibility(View.VISIBLE);
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
        Call<String> call=api.employees_list(userDetails.getCustId());
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
                                EmployeeListModel model = new EmployeeListModel();
                                userDetails.setEmpId(jsonObject1.getString("employeeId"));
                                userDetails.setShiftName(jsonObject1.getString("shift"));
                                model.setEmployeeId(jsonObject1.getString("employeeId"));
                                model.setFirstName(jsonObject1.getString("firstName"));
                                model.setHomePhone(jsonObject1.getString("homePhone"));
                                model.setDesignation(jsonObject1.getString("designation"));
                                model.setShiftName(jsonObject1.getString("shift"));
                                model.setSecurityProfile(jsonObject1.getString("securityProfile"));

                                employeeListModelArrayList.add(model);

                                message = jsonObject.getString("message");
                                error = jsonObject.getString("errorMessage");
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EmployeeList.this, RecyclerView.VERTICAL, false);
                            empListRecycler.setLayoutManager(linearLayoutManager);

                            adapter = new EmployeeListAdapter(employeeListModelArrayList, EmployeeList.this, EmployeeList.this);
                            empListRecycler.setAdapter(adapter);
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")){
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progress.setVisibility(View.GONE);
                    Utils.showAlertDialog(EmployeeList.this,"No Data Found",false);
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
                Toast.makeText(EmployeeList.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onMethodCallback() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(EmployeeList.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(EmployeeList.this,EmployeeDashboard.class);
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
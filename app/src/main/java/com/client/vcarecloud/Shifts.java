package com.client.vcarecloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
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
import com.client.vcarecloud.Adapters.ShiftsAdapter;
import com.client.vcarecloud.models.ShiftsModel;
import com.client.vcarecloud.models.UserDetails;

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

public class Shifts extends AppCompatActivity implements LoadDetails{
    RecyclerView shiftsRecycler;
    ArrayList<ShiftsModel> shiftsModelArrayList=new ArrayList<>();
    ImageView back,noData,cancel;
    RelativeLayout progress;
    UserDetails userDetails;
    EditText search;
    LinearLayout linearLayout;
    AppCompatButton addNew;
    SwipeRefreshLayout swipe;
    ShiftsAdapter adapter;
    String custId,message,error;
    LoadDetails loadDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        shiftsRecycler=findViewById(R.id.shiftsRecycler);
        shiftsRecycler.setHasFixedSize(true);
        userDetails=new UserDetails(Shifts.this);

        back=findViewById(R.id.back);
        search=findViewById(R.id.search);
        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        addNew=findViewById(R.id.addNew);
        noData=findViewById(R.id.noData_leaveList_admin_approved);
        linearLayout=findViewById(R.id.linear1);
        adapter=new ShiftsAdapter(shiftsModelArrayList,Shifts.this,loadDetails);
        progress=findViewById(R.id.progressLayout);
        swipe=findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
//                        swipe.setRefreshing(false);
//                        shiftsModelArrayList.clear();
//                        shiftsResponse();
//                        search.setText("");
//                        noData.setVisibility(View.GONE);
                        if(search.getText().toString().length()>0){
                            swipe.setRefreshing(false);

                        }else{
                            swipe.setRefreshing(false);
                            shiftsModelArrayList.clear();
                            shiftsResponse();
                        }

                    }
                }, 2000);


            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Shifts.this,AddShifts.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(Shifts.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(Shifts.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        shiftsResponse();
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
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
                shiftsRecycler.setVisibility(View.VISIBLE);
            }
        });
    }

    private void filter(String text) {
        ArrayList<ShiftsModel> filteredNames=new ArrayList<>();
        for (ShiftsModel s:shiftsModelArrayList){
            if ((s.getShift().toLowerCase().contains(text.toLowerCase())) ||
                 (s.getTimeStart().toLowerCase().contains(text.toLowerCase())) ||
                 (s.getEndTime().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (shiftsModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                shiftsRecycler.setVisibility(View.VISIBLE);
//                swipe.setEnabled(false);
            }

            adapter.filterList(filteredNames);
        }
    }

    private void shiftsResponse() {
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
        Call<String> call=api.shifts_list(userDetails.getCustId());
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
                                ShiftsModel model = new ShiftsModel();
                                userDetails.setShift_Id(jsonObject1.getString("shiftId"));
                                userDetails.setDuration(jsonObject1.getString("shiftDuration"));
                                userDetails.setShiftstatus(jsonObject1.getString("shiftStatus"));

                                model.setShift_id(jsonObject1.getString("shiftId"));
                                model.setCust_id(jsonObject1.getString("custId"));
                                model.setShift(jsonObject1.getString("shiftName"));
                                model.setTimeStart(jsonObject1.getString("startTime"));
                                model.setEndTime(jsonObject1.getString("endTime"));
                                model.setShift_duration(jsonObject1.getString("shiftDuration"));
                                model.setShift_status(jsonObject1.getString("shiftStatus"));
                                model.setCreated_by(jsonObject1.getString("createdBy"));
                                model.setCreated_on(jsonObject1.getString("createdOn"));
                                model.setLast_changedBy(jsonObject1.getString("lastChangedBy"));
                                model.setLast_changedOn(jsonObject1.getString("lastChangedOn"));
                                model.setStatus(jsonObject1.getString("status"));
                                model.setCustomer(jsonObject1.getString("customer"));


                                shiftsModelArrayList.add(model);

//                                message = jsonObject.getString("message");
//                                error = jsonObject.getString("errorMessage");
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Shifts.this, RecyclerView.VERTICAL, false);
                            shiftsRecycler.setLayoutManager(linearLayoutManager);

                            adapter = new ShiftsAdapter(shiftsModelArrayList, Shifts.this,Shifts.this);
                            shiftsRecycler.setAdapter(adapter);
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
                Toast.makeText(Shifts.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }
    @Override
    public void onMethodCallback() {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(Shifts.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(Shifts.this,EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
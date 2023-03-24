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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.CampAdapter;
import com.client.vcarecloud.models.CampModel;
import com.client.vcarecloud.models.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Camps extends AppCompatActivity implements LoadDetails{
    RecyclerView campRecycle;
    ImageView back,noData,cancel;
    RelativeLayout progress;
    Button addNewButton;
    SwipeRefreshLayout swipe;
    EditText search;

    String custId,empId,message,error,startDate,enddate;

    ArrayList<CampModel> campModelList=new ArrayList<>();
    UserDetails userDetails;
    CampModel campModel;
    CampAdapter adapter;
    LoadDetails loadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camps);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        campRecycle=findViewById(R.id.campRecycler);
        campRecycle.setHasFixedSize(true);

        back=findViewById(R.id.back);
        noData=findViewById(R.id.noData);
        cancel=findViewById(R.id.cancel);
        progress=findViewById(R.id.progressLayout);
        addNewButton=findViewById(R.id.addNew);
        swipe=findViewById(R.id.swipe);
        search=findViewById(R.id.search);

        campModel=new CampModel();
        campModel = (CampModel) getIntent().getSerializableExtra("list");

        adapter = new CampAdapter(campModelList, Camps.this,loadDetails);

        custId=getIntent().getStringExtra("custId");
        empId=getIntent().getStringExtra("empID");

        userDetails=new UserDetails(Camps.this);
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

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
                            campModelList.clear();
                            campDetails(custId);
                        }
                    }
                }, 2000);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Camps.this,AddCamps.class);
                startActivity(intent);
            }
        });
        campDetails(custId);
    }

    private void campDetails(String custId) {
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
        Call<String> call=api.campData(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.setVisibility(View.GONE);

                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CampModel model = new CampModel();
                                userDetails.setCampId(jsonObject1.getString("campId"));

                                model.setCampId(jsonObject1.getString("campId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setCampName(jsonObject1.getString("campName"));
                                model.setCampDetails(jsonObject1.getString("campDetails"));
                                model.setClassId(jsonObject1.getString("classId"));
                                model.setClassName(jsonObject1.getString("className"));
                                model.setCampStartDate(jsonObject1.getString("campStartDate"));
                                model.setCampEndDate(jsonObject1.getString("campEndDate"));
                                model.setCampCharge(jsonObject1.getString("campCharge"));
                                model.setTaxesId(jsonObject1.getString("taxesId"));
                                model.setTaxname(jsonObject1.getString("taxName"));
                                model.setStatus(jsonObject1.getString("status"));

                                campModelList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Camps.this, RecyclerView.VERTICAL, false);
                            campRecycle.setLayoutManager(linearLayoutManager);
                            adapter = new CampAdapter(campModelList, Camps.this, Camps.this);
                            campRecycle.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    }catch (JSONException e) {
                        Toast.makeText(Camps.this, "No Data Found", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
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
                Toast.makeText(Camps.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<CampModel> filteredNames=new ArrayList<>();
        for (CampModel s:campModelList){
            if ((s.getCampName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getCampStartDate().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getClassName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getCampEndDate().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (campModelList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
            }

            adapter.filterList(filteredNames);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(Camps.this,Dashboard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(Camps.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(Camps.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onMethodCallback() {
    }
}
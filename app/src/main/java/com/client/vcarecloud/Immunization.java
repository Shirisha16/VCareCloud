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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.ImmunizationAdapter;
import com.client.vcarecloud.models.ImmunizationModel;
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

public class Immunization extends AppCompatActivity implements LoadDetails {
    ImageView back,cancel,noDataImage;
    AppCompatButton addButton;
    EditText search;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    RelativeLayout progressLayout;

    UserDetails userDetails;
    ImmunizationModel immunizationModel;
    ImmunizationAdapter adapter;
    LoadDetails loadDetails;
    String custId,message,error;
    ArrayList<ImmunizationModel> immunizationModelArrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immunization);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        noDataImage=findViewById(R.id.noData);
        addButton=findViewById(R.id.addNew);
        search=findViewById(R.id.search);
        swipeRefresh=findViewById(R.id.swipe);
        progressLayout=findViewById(R.id.progressLayout);
        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);

        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        immunizationModel=new ImmunizationModel();
        immunizationModel = (ImmunizationModel) getIntent().getSerializableExtra("list");
        adapter = new ImmunizationAdapter(immunizationModelArrayList, Immunization.this,loadDetails);

        custId=getIntent().getStringExtra("custId");

        userDetails=new UserDetails(Immunization.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Immunization.this,Dashboard.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
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

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        if(search.getText().toString().length()>0){
                            swipeRefresh.setRefreshing(false);

                        }else{
                            swipeRefresh.setRefreshing(false);
                            immunizationModelArrayList.clear();
                            immunizationList();
                        }
                    }
                }, 2000);
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Immunization.this,AddImmunization.class);
                startActivity(intent);
            }
        });
        immunizationList();
    }

    private void immunizationList() {
        progressLayout.setVisibility(View.VISIBLE);

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
        Call<String> call=api.immunizationList(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressLayout.setVisibility(View.GONE);
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ImmunizationModel model = new ImmunizationModel();

                                model.setImmunizationId(jsonObject1.getString("immunizationId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setImmunizationName(jsonObject1.getString("immunizationName"));
                                model.setImmunizationCode(jsonObject1.getString("immunizationCode"));
                                model.setDose1(jsonObject1.getString("dose1"));
                                model.setDose2(jsonObject1.getString("dose2"));
                                model.setDose3(jsonObject1.getString("dose3"));
                                model.setDose4(jsonObject1.getString("dose4"));
                                model.setDose5(jsonObject1.getString("dose5"));
                                model.setDose6(jsonObject1.getString("dose6"));
                                model.setIsOptional(jsonObject1.getString("isOptional"));

                                immunizationModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Immunization.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            adapter=new ImmunizationAdapter(immunizationModelArrayList,Immunization.this,loadDetails);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noDataImage.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    progressLayout.setVisibility(View.GONE);
                    Utils.showAlertDialog(Immunization.this,"No Data Found",false);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressLayout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(Immunization.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<ImmunizationModel> filteredNames=new ArrayList<>();
        for (ImmunizationModel s:immunizationModelArrayList){
            if ((s.getImmunizationName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getImmunizationCode().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getIsOptional().toLowerCase().contains(text.toLowerCase()))){
                filteredNames.add(s);
                noDataImage.setVisibility(View.INVISIBLE);
            }

        }
        if (immunizationModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noDataImage.setVisibility(View.VISIBLE);
            }

            adapter.filterList(filteredNames);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(Immunization.this,Dashboard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(Immunization.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(Immunization.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
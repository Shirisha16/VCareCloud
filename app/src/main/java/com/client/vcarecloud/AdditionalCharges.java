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

import com.client.vcarecloud.Adapters.InvoiceListAdapter;
import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.RestService;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.AdditionalChargesAdapter;
import com.client.vcarecloud.models.AdditionalChargeListModel;
import com.client.vcarecloud.models.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AdditionalCharges extends AppCompatActivity implements LoadDetails{
    ImageView back,cancel,noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    RelativeLayout progress;
    AppCompatButton addButton;

    ArrayList<AdditionalChargeListModel> additionalChargeListModelArrayList=new ArrayList<>();
    LoadDetails loadDetails;
    AdditionalChargesAdapter adapter;
    AdditionalChargeListModel additionalChargeListModel;
    UserDetails userDetails;
    String custId,message,error;

    List<AdditionalChargeListModel.Model> modelList;
    AdditionalChargeListModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_charges);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        cancel=findViewById(R.id.cancel);
        noData=findViewById(R.id.noDataImage);
        search=findViewById(R.id.search);
        swipe=findViewById(R.id.swipe);
        progress=findViewById(R.id.progressLayout);
        addButton=findViewById(R.id.addNew);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        userDetails=new UserDetails(AdditionalCharges.this);

        additionalChargeListModel=new AdditionalChargeListModel();
//        adapter= new AdditionalChargesAdapter(additionalChargeListModelArrayList,AdditionalCharges.this,loadDetails);

        custId=getIntent().getStringExtra("custId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(AdditionalCharges.this,Dashboard.class);
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
                            additionalChargeListModelArrayList.clear();
                            additionalChargesList();
                        }
                    }
                }, 2000);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdditionalCharges.this,AddAdditionalCharges.class);
                startActivity(intent);
            }
        });

        additionalChargesList();
    }

    private void additionalChargesList() {
        VcareApi api = RestService.getClient().create(VcareApi.class);
        progress.setVisibility(View.VISIBLE);
        Call<AdditionalChargeListModel> call=api.additionalCharges(userDetails.getCustId());
        call.enqueue(new Callback<AdditionalChargeListModel>() {
            @Override
            public void onResponse(Call<AdditionalChargeListModel> call, Response<AdditionalChargeListModel> response) {
                progress.setVisibility(View.GONE);
                model = response.body();
                modelList = model.getModel();
                if (!modelList.isEmpty()) {
                    initRecycler();
                }else {
                    noData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<AdditionalChargeListModel> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(AdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initRecycler() { if (modelList!=null){
        noData.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adapter = new AdditionalChargesAdapter((ArrayList<AdditionalChargeListModel.Model>) modelList,this,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
       }else {
        recyclerView.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
      }

    }

//    private void additionalChargesList() {
//        progress.setVisibility(View.VISIBLE);
//
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                .callTimeout(2, TimeUnit.MINUTES)
//                .connectTimeout(90, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(VcareApi.JSONURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//
//        VcareApi api = retrofit.create(VcareApi.class);
//        Call<String> call=api.additionalCharges(userDetails.getCustId());
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                progress.setVisibility(View.GONE);
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body());
//                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
//
//                            JSONArray jsonArray = jsonObject.getJSONArray("model");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                AdditionalChargeListModel model = new AdditionalChargeListModel();
//
//                                model.setChargeId(jsonObject1.getString("additionalChargeId"));
//                                model.setCustId(jsonObject1.getString("custId"));
//                                model.setDate(jsonObject1.getString("chargeDate"));
//                                model.setChargename(jsonObject1.getString("chargeName"));
//                                model.setAmount(jsonObject1.getString("chargeAmount"));
//                                model.setRefApplicableID(jsonObject1.getString("refApplicableID"));
//                                model.setClassName(jsonObject1.getString("className"));
//                                model.setChildName(jsonObject1.getString("childName"));
//                                model.setApplicableType(jsonObject1.getString("applicableType"));
//                                model.setDescription(jsonObject1.getString("chargeDescription"));
//                                model.setTaxesId(jsonObject1.getString("taxesId"));
//                                model.setTaxname(jsonObject1.getString("taxName"));
//
//                                additionalChargeListModelArrayList.add(model);
//                            }
//                            message = jsonObject.getString("message");
//                            error = jsonObject.getString("errorMessage");
//
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdditionalCharges.this, RecyclerView.VERTICAL, false);
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            adapter = new AdditionalChargesAdapter(additionalChargeListModelArrayList, AdditionalCharges.this, AdditionalCharges.this);
//                            recyclerView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
//                            noData.setVisibility(View.VISIBLE);
//                        }
//                    } catch (JSONException e) {
//                        Toast.makeText(AdditionalCharges.this, "No Data Found", Toast.LENGTH_SHORT).show();
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                progress.setVisibility(View.GONE);
//                String message = "";
//                if (t instanceof UnknownHostException) {
//                    message = "No internet connection!";
//                } else {
//                    message = "Something went wrong! try again";
//                }
//                Toast.makeText(AdditionalCharges.this, message, Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//    }

    private void filter(String text) {
        ArrayList<AdditionalChargeListModel.Model> filteredNames=new ArrayList<>();
        for (AdditionalChargeListModel.Model s: modelList) {
            String amt = String.valueOf(s.getChargeAmount());
            String classes=s.getClassName();
            String child=s.getChildName();
            if (s.getClassName() != null || s.getChildName()!=null){
                if ((s.getChargeDate().toLowerCase().contains(text.toLowerCase())) ||
                        (s.getChargeName().toLowerCase().contains(text.toLowerCase())) ||
                        (s.getApplicableType().toLowerCase().contains(text.toLowerCase())) ||
                        (amt.toLowerCase().contains(text.toLowerCase())) /*||
                        (classes.toLowerCase().contains(text.toLowerCase()) ||
                        (child.toLowerCase().contains(text.toLowerCase())))*/) {
                    filteredNames.add(s);
                    noData.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (modelList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
            }
            adapter.filterList(filteredNames);
        }
    }

    @Override
    public void onMethodCallback() {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(AdditionalCharges.this,Dashboard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(AdditionalCharges.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(AdditionalCharges.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
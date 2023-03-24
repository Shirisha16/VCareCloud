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
import com.client.vcarecloud.Adapters.TaxAdapter;
import com.client.vcarecloud.models.TaxModel;
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

public class Taxes extends AppCompatActivity implements LoadDetails {
    RecyclerView taxRecycle;
    ImageView back,noData,cancel;
    RelativeLayout progress;
    Button addNewTax;
    EditText search;
    SwipeRefreshLayout swipe;

    TaxModel taxModel;
    TaxAdapter taxAdapter;
    LoadDetails loadDetails;
    UserDetails userDetails;

    ArrayList<TaxModel> taxModelList=new ArrayList<>();
    String custId,taxId,message,error,taxstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxes);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        taxRecycle=findViewById(R.id.taxRecycler);
        taxRecycle.setHasFixedSize(true);

        back=findViewById(R.id.back);
        progress=findViewById(R.id.progressLayout);
        addNewTax=findViewById(R.id.addNew);
        swipe=findViewById(R.id.swipe);
        search=findViewById(R.id.search);
        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        noData=findViewById(R.id.noData);

        taxAdapter = new TaxAdapter(taxModelList, Taxes.this,loadDetails);

        taxModel=new TaxModel();
        taxModel = (TaxModel) getIntent().getSerializableExtra("list");

        userDetails=new UserDetails(Taxes.this);

        custId=getIntent().getStringExtra("custId");
        taxId=getIntent().getStringExtra("taxesId");

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

//                        swipe.setRefreshing(false);
//                        taxModelList.clear();
//                        taxDetails(custId);
                        if(search.getText().toString().length()>0){
                            swipe.setRefreshing(false);

                        }else{
                            swipe.setRefreshing(false);
                            taxModelList.clear();
                            taxDetails(custId);
                        }
                    }
                }, 2000);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Taxes.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
        });

        addNewTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Taxes.this,AddTax.class);
                startActivity(intent);
            }
        });
        taxDetails(custId);
    }


    private void taxDetails(String custId) {
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
        Call<String> call=api.taxData(userDetails.getCustId());
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
                                TaxModel model = new TaxModel();
                                userDetails.setTaxId(jsonObject1.getString("taxesId"));

                                model.setTaxid(jsonObject1.getString("taxesId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setName(jsonObject1.getString("taxName"));
                                model.setRate(jsonObject1.getString("taxRate"));
                                model.setTaxStatus(jsonObject1.getString("taxStatus"));
                                model.setCreatedBy(jsonObject1.getString("createdBy"));
                                model.setCreatedOn(jsonObject1.getString("createdOn"));
                                model.setModifiedby(jsonObject1.getString("modifiedBy"));
                                model.setModifiedon(jsonObject1.getString("modifiedOn"));
                                model.setStatus(jsonObject1.getString("status"));
                                model.setCustomer(jsonObject1.getString("customer"));

                                taxModelList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Taxes.this, RecyclerView.VERTICAL, false);
                            taxRecycle.setLayoutManager(linearLayoutManager);
                            taxAdapter = new TaxAdapter(taxModelList,Taxes.this,Taxes.this);
                            taxRecycle.setAdapter(taxAdapter);
                            taxAdapter.notifyDataSetChanged();
                        }
                        else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    }  catch (JSONException e) {
                        Toast.makeText(Taxes.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Taxes.this, message, Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(Taxes.this,Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMethodCallback() {
    }

    private void filter(String text) {
        ArrayList<TaxModel> filteredNames=new ArrayList<>();
        for (TaxModel s:taxModelList){
            if ((s.getName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getRate().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getTaxStatus().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (taxModelList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
            }

            taxAdapter.filterList(filteredNames);
        }
    }

}
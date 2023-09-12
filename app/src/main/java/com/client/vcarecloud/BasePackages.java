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
import com.client.vcarecloud.Api.RestService;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.BasePackagesAdapter;
import com.client.vcarecloud.models.BasePackagesModel;
import com.client.vcarecloud.models.InvoiceListModel;
import com.client.vcarecloud.models.UserDetails;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasePackages extends AppCompatActivity implements LoadDetails {
    ImageView back,cancel,noDataImage;
    AppCompatButton addButton;
    EditText search;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerView;
    RelativeLayout progressLayout;

    UserDetails userDetails;
    BasePackagesModel basePackagesModel;
    BasePackagesAdapter adapter;
    LoadDetails loadDetails;
    String custId,message,error;

    ArrayList<BasePackagesModel> basePackagesModelArrayList=new ArrayList<>();
    List<BasePackagesModel.Model> modelList;
    BasePackagesModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_packages);
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

        basePackagesModel=new BasePackagesModel();
//        basePackagesModel = (BasePackagesModel) getIntent().getSerializableExtra("list");
//        adapter = new BasePackagesAdapter(basePackagesModelArrayList, BasePackages.this,loadDetails);

        custId=getIntent().getStringExtra("custId");

        userDetails=new UserDetails(BasePackages.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            basePackagesModelArrayList.clear();
                            basePackageList();
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
                Intent intent=new Intent(BasePackages.this,AddBasePackages.class);
                startActivity(intent);
            }
        });
        basePackageList();
    }

    private void basePackageList() {
        VcareApi api = RestService.getClient().create(VcareApi.class);
        progressLayout.setVisibility(View.VISIBLE);
        Call<BasePackagesModel> call=api.basePackages(userDetails.getCustId());
        call.enqueue(new Callback<BasePackagesModel>() {
            @Override
            public void onResponse(Call<BasePackagesModel> call, Response<BasePackagesModel> response) {
                progressLayout.setVisibility(View.GONE);
                model = response.body();
                modelList = model.getModel();
                if (!modelList.isEmpty()) {
                    noDataImage.setVisibility(View.GONE);
                    initRecycler();
                }else {
                    recyclerView.setVisibility(View.GONE);
                    noDataImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BasePackagesModel> call, Throwable t) {
                progressLayout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(BasePackages.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initRecycler() {
        if (modelList!=null){
            noDataImage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new BasePackagesAdapter(modelList,this,this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
        }
    }

//    private void basePackageList() {
//        progressLayout.setVisibility(View.VISIBLE);
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
//        VcareApi api = retrofit.create(VcareApi.class);
//        Call<String> call=api.basePackages(userDetails.getCustId());
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                progressLayout.setVisibility(View.GONE);
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body());
//                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("model");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                BasePackagesModel model = new BasePackagesModel();
//
//                                model.setPackageId(jsonObject1.getString("packageId"));
//                                model.setCustId(jsonObject1.getString("custId"));
//                                model.setPackageName(jsonObject1.getString("packageName"));
//                                model.setDescription(jsonObject1.getString("packageDescription"));
//                                model.setAmount(jsonObject1.getString("packageAmount"));
//                                model.setTax(jsonObject1.getString("tax"));
//                                model.setTaxid(jsonObject1.getString("taxid"));
//                                model.setClasses(jsonObject1.getString("classes"));
//                                model.setPackageStatus(jsonObject1.getString("packageStatus"));
//                                model.setClassid(jsonObject1.getString("classId"));
//
//                                basePackagesModelArrayList.add(model);
//                            }
//                            message = jsonObject.getString("message");
//                            error = jsonObject.getString("errorMessage");
//
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BasePackages.this, RecyclerView.VERTICAL, false);
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            adapter=new BasePackagesAdapter(basePackagesModelArrayList,BasePackages.this,loadDetails);
//                            recyclerView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();
//                        }
//                        else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
//                            noDataImage.setVisibility(View.VISIBLE);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    progressLayout.setVisibility(View.GONE);
//                    Utils.showAlertDialog(BasePackages.this,"No Data Found",false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                progressLayout.setVisibility(View.GONE);
//                String message = "";
//                if (t instanceof UnknownHostException) {
//                    message = "No internet connection!";
//                } else {
//                    message = "Something went wrong! try again";
//                }
//                Toast.makeText(BasePackages.this, message, Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//    }

    private void filter(String text) {
        ArrayList<BasePackagesModel.Model> filteredNames=new ArrayList<>();
        for (BasePackagesModel.Model s:modelList){
            String amt= String.valueOf(s.getPackageAmount());
            if ((s.getPackageName().toLowerCase().contains(text.toLowerCase())) ||
                    (amt.toLowerCase().contains(text.toLowerCase())) ||
                    (s.getTax().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getPackageStatus().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getClasses().toLowerCase().contains(text.toLowerCase()))){
                filteredNames.add(s);
            }
        }
//        ArrayList<BasePackagesModel.Model> filteredNames=new ArrayList<>();
//        for (BasePackagesModel.Model s:modelList){
//            String amt= String.valueOf(s.getPackageAmount());
//            if ((s.getPackageName().toLowerCase().contains(text.toLowerCase())) ||
//                    (amt.toLowerCase().contains(text.toLowerCase())) ||
//                    (s.getTax().toLowerCase().contains(text.toLowerCase())) ||
//                    (s.getPackageStatus().toLowerCase().contains(text.toLowerCase())) ||
//                    (s.getClasses().toLowerCase().contains(text.toLowerCase()))){
//                filteredNames.add(s);
//                noDataImage.setVisibility(View.INVISIBLE);
//            }
//
//        }
        if (modelList.size() != 0) {
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
//        search.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(BasePackages.this,Dashboard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(BasePackages.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(BasePackages.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
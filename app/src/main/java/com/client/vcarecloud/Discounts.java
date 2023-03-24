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
import com.client.vcarecloud.Adapters.DiscountsAdapter;
import com.client.vcarecloud.models.DiscountListModel;
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

public class Discounts extends AppCompatActivity implements LoadDetails {
    ImageView back,cancel,noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    RelativeLayout progress;
    AppCompatButton addButton;

    ArrayList<DiscountListModel> discountListModelArrayList=new ArrayList<>();
    LoadDetails loadDetails;
    DiscountsAdapter adapter;
    DiscountListModel discountListModel;
    UserDetails userDetails;
    String custId,message,error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discounts);
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

        userDetails=new UserDetails(Discounts.this);

        discountListModel=new DiscountListModel();
        adapter= new DiscountsAdapter(discountListModelArrayList,Discounts.this,loadDetails);

        custId=getIntent().getStringExtra("custId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Discounts.this,Dashboard.class);
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
                            discountListModelArrayList.clear();
                            discountsList();
                        }
                    }
                }, 2000);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Discounts.this,AddDiscounts.class);
                startActivity(intent);
            }
        });

        discountsList();
    }

    private void discountsList() {
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
        Call<String> call=api.discount_list(userDetails.getCustId());
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
                                DiscountListModel model = new DiscountListModel();

                                model.setDiscountId(jsonObject1.getString("discountId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setDiscountName(jsonObject1.getString("discountName"));
                                model.setDescription(jsonObject1.getString("discountDescription"));
                                model.setDiscountType(jsonObject1.getString("discountType"));
                                model.setDiscountValue(jsonObject1.getString("discountValue"));
                                model.setStatus(jsonObject1.getString("discountStatus"));
                                model.setCheckLimitedPeriod(jsonObject1.getString("checkLimitedPeriod"));
                                model.setFromDate(jsonObject1.getString("limitedPeriodFromDate"));
                                model.setToDate(jsonObject1.getString("limitedPeriodToDate"));
                                model.setBasePackage(jsonObject1.getString("applicable_BasePackage"));
                                model.setAdditionalCharge(jsonObject1.getString("applicable_AdditionalCharge"));
                                model.setCamp(jsonObject1.getString("applicable_Camp"));
                                model.setApplicableActivity(jsonObject1.getString("applicable_Activity"));
                                model.setApplicableAll(jsonObject1.getString("applicable_All"));

                                discountListModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Discounts.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            adapter = new DiscountsAdapter(discountListModelArrayList, Discounts.this, Discounts.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Discounts.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Discounts.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void filter(String text) {
        ArrayList<DiscountListModel> filteredNames=new ArrayList<>();
        for (DiscountListModel s: discountListModelArrayList){
            if ((s.getDiscountName().toLowerCase().contains(text.toLowerCase())) ||
                (s.getDiscountValue().toLowerCase().contains(text.toLowerCase())) ||
                 (s.getStatus().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }

        if (discountListModelArrayList.size() != 0) {
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
//        Intent intent = new Intent(Discounts.this, Dashboard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(Discounts.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(Discounts.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
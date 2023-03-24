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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.PaymentsAdapter;
import com.client.vcarecloud.models.PaymentModel;
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

public class Payments extends AppCompatActivity implements LoadDetails {
    ImageView back,cancel,noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    RelativeLayout progress;
//    AppCompatButton add;

    String custId,message,error;

    ArrayList<PaymentModel> paymentModelArrayList=new ArrayList<>();
    UserDetails userDetails;
    PaymentModel paymentModel;
    PaymentsAdapter adapter;
    LoadDetails loadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        cancel=findViewById(R.id.cancel);
        noData=findViewById(R.id.noDataImage);
        search=findViewById(R.id.search);
        swipe=findViewById(R.id.swipe);
//        add=findViewById(R.id.addNew);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        progress=findViewById(R.id.progressLayout);

        paymentModel=new PaymentModel();
        paymentModel = (PaymentModel) getIntent().getSerializableExtra("list");

        adapter = new PaymentsAdapter(paymentModelArrayList, Payments.this,loadDetails);

        custId=getIntent().getStringExtra("custId");
        userDetails=new UserDetails(Payments.this);

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
                            paymentModelArrayList.clear();
                            paymentsDetails();
                        }
                    }
                }, 2000);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(Payments.this,Dashboard.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        paymentsDetails();
    }

    private void paymentsDetails() {
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
        Call<String> call=api.payment(userDetails.getCustId());
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
                                PaymentModel model = new PaymentModel();

                                model.setPaymentId(jsonObject1.getString("paymentId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setPaymentDate(jsonObject1.getString("paymentDate"));
                                model.setPaymentAmount(jsonObject1.getString("paymentAmount"));
                                model.setPaymentType(jsonObject1.getString("paymentType"));
                                model.setInvoice(jsonObject1.getString("invoice"));
                                model.setChild(jsonObject1.getString("child"));
                                model.setPaymentNotes(jsonObject1.getString("paymentNotes"));

                                paymentModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Payments.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            adapter = new PaymentsAdapter(paymentModelArrayList, Payments.this, Payments.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Payments.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Payments.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<PaymentModel> filteredNames=new ArrayList<>();
        for (PaymentModel s:paymentModelArrayList){
            if ((s.getPaymentDate().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getPaymentAmount().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getPaymentType().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getInvoice().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getChild().toLowerCase().contains(text.toLowerCase())) ) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (paymentModelArrayList.size() != 0) {
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
//        Intent intent =new Intent(Payments.this,Dashboard.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(Payments.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(Payments.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
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
import com.client.vcarecloud.Adapters.InvoiceListAdapter;
import com.client.vcarecloud.models.InvoiceListModel;
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

public class InvoiceList extends AppCompatActivity implements LoadDetails {
    ImageView back,cancel,noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    RelativeLayout progress;

    String custId,message,error;

    ArrayList<InvoiceListModel> invoiceListModelArrayList=new ArrayList<>();
    UserDetails userDetails;
    InvoiceListModel invoiceListModel;
    InvoiceListAdapter adapter;
    LoadDetails loadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        cancel=findViewById(R.id.cancel);
        noData=findViewById(R.id.noDataImage);
        search=findViewById(R.id.search);
        swipe=findViewById(R.id.swipe);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        progress=findViewById(R.id.progressLayout);

        invoiceListModel=new InvoiceListModel();
        invoiceListModel = (InvoiceListModel) getIntent().getSerializableExtra("list");

        adapter = new InvoiceListAdapter(invoiceListModelArrayList, InvoiceList.this,loadDetails);

        custId=getIntent().getStringExtra("custId");
        userDetails=new UserDetails(InvoiceList.this);

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
                            invoiceListModelArrayList.clear();
                            invoiceList();
                        }
                    }
                }, 2000);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InvoiceList.this,Dashboard.class);
                startActivity(intent);
                finish();
            }
        });
        invoiceList();
    }

    private void invoiceList() {
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
        Call<String> call=api.invoice_list(userDetails.getCustId());
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
                                InvoiceListModel model = new InvoiceListModel();

                                model.setHeaderId(jsonObject1.getString("headerId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setClassId(jsonObject1.getString("childId"));
                                model.setClassName(jsonObject1.getString("classname"));
                                model.setInvoiceNo(jsonObject1.getString("invoiceNo"));
                                model.setInvoiceMonth(jsonObject1.getString("invoiceMonth"));
                                model.setInvoiceYear(jsonObject1.getString("invoiceYear"));
                                model.setInvoicePeriod_From(jsonObject1.getString("invoicePeriod_From"));
                                model.setInvoicePeriod_To(jsonObject1.getString("invoicePeriod_To"));
                                model.setInvoiceDate(jsonObject1.getString("invoiceDate"));
                                model.setInvoiceDueDate(jsonObject1.getString("invoiceDueDate"));
                                model.setPaymentDate(jsonObject1.getString("paymentDate"));
                                model.setChildName(jsonObject1.getString("childname"));
                                model.setTotalChargeAmount(jsonObject1.getString("totalChargeAmount"));
                                model.setTotalDiscountAmount(jsonObject1.getString("totalDiscountAmount"));
                                model.setTotalTaxAmount(jsonObject1.getString("totalTaxAmount"));
                                model.setTotalAdjustmentAmount(jsonObject1.getString("totalAdjustmentAmount"));
                                model.setInvoiceAmount(jsonObject1.getString("invoiceAmount"));
                                model.setPayAmount(jsonObject1.getString("payAmount"));

                                model.setLatePayment(jsonObject1.getString("latePayment"));
//                                model.setStatus(jsonObject1.getString("status"));
                                invoiceListModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(InvoiceList.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            adapter = new InvoiceListAdapter(invoiceListModelArrayList, InvoiceList.this, InvoiceList.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(InvoiceList.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(InvoiceList.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<InvoiceListModel> filteredNames=new ArrayList<>();
        for (InvoiceListModel s:invoiceListModelArrayList){
            if ((s.getInvoiceNo().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getChildName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getInvoiceAmount().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getInvoicePeriod_From().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getInvoiceYear().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getPayAmount().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (invoiceListModelArrayList.size() != 0) {
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
        Intent intent =new Intent(InvoiceList.this,Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
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
import com.client.vcarecloud.Api.RestService;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.InvoiceListAdapter;
import com.client.vcarecloud.models.InvoiceListModel;
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

    List<InvoiceListModel.Model> modelList;
    InvoiceListModel model;

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

//        adapter = new InvoiceListAdapter(invoiceListModelArrayList, InvoiceList.this,loadDetails);

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
        VcareApi api =RestService.getClient().create(VcareApi.class);
        progress.setVisibility(View.VISIBLE);
        Call<InvoiceListModel> call=api.invoice_list(userDetails.getCustId());
        call.enqueue(new Callback<InvoiceListModel>() {
            @Override
            public void onResponse(Call<InvoiceListModel> call, Response<InvoiceListModel> response) {
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
            public void onFailure(Call<InvoiceListModel> call, Throwable t) {
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

    private void initRecycler() {
        if (modelList!=null){
            noData.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new InvoiceListAdapter(modelList,this,this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }else {
            recyclerView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }

    private void filter(String text) {
        ArrayList<InvoiceListModel.Model> filteredNames=new ArrayList<>();
        for (InvoiceListModel.Model s:modelList){
            String invAmount=String.valueOf(s.getInvoiceAmount());
            String getInvoiceYear=String.valueOf(s.getInvoiceYear());
            String payAmount= String.valueOf(s.getPayAmount());
            if ((s.getInvoiceNo().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getChildname().toLowerCase().contains(text.toLowerCase())) ||
                    (invAmount.toLowerCase().contains(text.toLowerCase())) ||
                    (s.getInvoicePeriodFrom().toLowerCase().contains(text.toLowerCase())) ||
                    (getInvoiceYear.toLowerCase().contains(text.toLowerCase())) ||
                    (payAmount.toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
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
        Intent intent =new Intent(InvoiceList.this,Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
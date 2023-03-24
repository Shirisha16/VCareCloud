package com.client.vcarecloud;

import androidx.appcompat.app.AppCompatActivity;
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

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Adapters.LookupsAdapter;
import com.client.vcarecloud.models.LookupsModel;
import com.client.vcarecloud.models.UserDetails;

import java.util.ArrayList;

public class Lookups extends AppCompatActivity implements LoadDetails {
    ImageView back, cancel, noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView lookUpRecyclerView;
    RelativeLayout progress;

    ArrayList<LookupsModel> lookupsModelArrayList = new ArrayList<>();
    LoadDetails loadDetails;
    LookupsAdapter adapter;
    UserDetails userDetails;
    String custId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookups);

        back = findViewById(R.id.back);
        cancel = findViewById(R.id.cancel);
        noData = findViewById(R.id.noDataImage);
        search = findViewById(R.id.search);
        swipe = findViewById(R.id.swipe);
        progress = findViewById(R.id.progressLayout);

        lookUpRecyclerView = findViewById(R.id.lookupRecycler);
        lookUpRecyclerView.setHasFixedSize(true);

        userDetails = new UserDetails(Lookups.this);

        adapter= new LookupsAdapter(lookupsModelArrayList,Lookups.this,loadDetails);
        custId = getIntent().getStringExtra("custId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Lookups.this, Dashboard.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

//        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
////                        swipe.setRefreshing(false);
////                        lookupsModelArrayList.clear();
////                        lookupList();
//                        if(search.getText().toString().length()>0){
//                         swipe.setRefreshing(false);
//
//                        }else{
//                            swipe.setRefreshing(false);
//                            lookupsModelArrayList.clear();
////                            lookupList();
//                        }
//                    }
//                }, 2000);
//            }
//        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        if(search.getText().toString().length()>0){
                            swipe.setRefreshing(false);

                        }else{
                            swipe.setRefreshing(false);
                            lookupsModelArrayList.clear();
                            lookupList();
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

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList(s.toString());
                if (search.getText().toString().isEmpty()){
                    cancel.setVisibility(View.INVISIBLE);

                } else {
                    cancel.setVisibility(View.VISIBLE);
                }
            }
        });

        lookupList();
    }

    private void lookupList() {
        lookupsModelArrayList.add(new LookupsModel("Allergy"));
        lookupsModelArrayList.add(new LookupsModel("Employee Designation"));
        lookupsModelArrayList.add(new LookupsModel("Event Type"));
        lookupsModelArrayList.add(new LookupsModel("Eye Color"));
        lookupsModelArrayList.add(new LookupsModel("Hair Style"));
        lookupsModelArrayList.add(new LookupsModel("Health Insurance"));
        lookupsModelArrayList.add(new LookupsModel("Medical Condition"));
        lookupsModelArrayList.add(new LookupsModel("Medication Route"));
        lookupsModelArrayList.add(new LookupsModel("Occupation"));
        lookupsModelArrayList.add(new LookupsModel("Relationship"));
        lookupsModelArrayList.add(new LookupsModel("Religion"));
        lookupsModelArrayList.add(new LookupsModel("Special Needs"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Lookups.this, RecyclerView.VERTICAL, false);
        lookUpRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LookupsAdapter(lookupsModelArrayList, Lookups.this, loadDetails);
        lookUpRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void filterList(String text) {
        ArrayList<LookupsModel> filteredNames=new ArrayList<>();
        for (LookupsModel s: lookupsModelArrayList){
            if ((s.getLookupname().toLowerCase().contains(text.toLowerCase()))){
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (lookupsModelArrayList.size() != 0) {
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
//        Intent intent =new Intent(Lookups.this,Dashboard.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(Lookups.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(Lookups.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
}
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

import com.client.vcarecloud.Adapters.BasePackagesAdapter;
import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.RestService;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.EventAdapter;
import com.client.vcarecloud.models.EventModel;
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

public class Events extends AppCompatActivity implements LoadDetails {
    ImageView back,cancel,noDataImage;
    AppCompatButton addEvent;
    EditText search;
    SwipeRefreshLayout swipeRefresh;
    RecyclerView recyclerEvent;
    RelativeLayout progressLayout;

    ArrayList<EventModel> eventModelsList=new ArrayList<>();
    UserDetails userDetails;
    EventModel eventModel;
    EventAdapter adapter;
    LoadDetails loadDetails;

    String custId,empId,typeId,message,error;

    ArrayList<EventModel.Model> modelList=new ArrayList<>();
    EventModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);

        noDataImage=findViewById(R.id.noData);
        addEvent=findViewById(R.id.addNew);
        search=findViewById(R.id.search);
        swipeRefresh=findViewById(R.id.swipe);
        progressLayout=findViewById(R.id.progressLayout);

        recyclerEvent=findViewById(R.id.eventRecycler);
        recyclerEvent.setHasFixedSize(true);

        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);

        eventModel=new EventModel();
        eventModel = (EventModel) getIntent().getSerializableExtra("list");
//        adapter = new EventAdapter(eventModelsList, Events.this,loadDetails);

        custId=getIntent().getStringExtra("custId");
        empId=getIntent().getStringExtra("empID");
        typeId=getIntent().getStringExtra("typeId");

        userDetails=new UserDetails(Events.this);

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

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

//                        swipeRefresh.setRefreshing(false);
//                        eventModelsList.clear();
//                        eventDetails();
                        if(search.getText().toString().length()>0){
                            swipeRefresh.setRefreshing(false);

                        }else{
                            swipeRefresh.setRefreshing(false);
                            eventModelsList.clear();
                            eventDetails();
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

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Events.this,AddEvents.class);
                startActivity(intent);
            }
        });
        eventDetails();

    }

    private void filter(String text) {
        ArrayList<EventModel.Model> filteredNames=new ArrayList<>();
        for (EventModel.Model s:modelList){
            if ((s.getEventName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getFromDate().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getToDate().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getEventLocation().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getEventDetails().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getEventtype().toLowerCase().contains(text.toLowerCase()))){
                filteredNames.add(s);
                noDataImage.setVisibility(View.INVISIBLE);
            }

        }
        if (modelList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noDataImage.setVisibility(View.VISIBLE);
            }

            adapter.filterList(filteredNames);
        }
    }

    private void eventDetails() {
        progressLayout.setVisibility(View.VISIBLE);

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

        VcareApi api = RestService.getClient().create(VcareApi.class);
//        VcareApi api = retrofit.create(VcareApi.class);
        Call<EventModel> call=api.events(userDetails.getCustId());
        call.enqueue(new Callback<EventModel>() {
            @Override
            public void onResponse(Call<EventModel> call, Response<EventModel> response) {
                progressLayout.setVisibility(View.GONE);
                model = response.body();
                modelList = (ArrayList<EventModel.Model>) model.getModel();
                if (!modelList.isEmpty()) {
                    noDataImage.setVisibility(View.GONE);
                    initRecycler();
                }else {
                    recyclerEvent.setVisibility(View.GONE);
                    noDataImage.setVisibility(View.VISIBLE);
                }
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body());
//                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("model");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                EventModel model = new EventModel();
//                                userDetails.setEventId(jsonObject1.getString("eventID"));
//
//                                model.setEventID(jsonObject1.getString("eventID"));
//                                model.setEventsType(jsonObject1.getString("eventtype"));
//                                model.setCustId(jsonObject1.getString("custId"));
//                                model.setEventName(jsonObject1.getString("eventName"));
//                                model.setEventLocation(jsonObject1.getString("eventLocation"));
//                                model.setEventDetails(jsonObject1.getString("eventDetails"));
//                                model.setFromDate(jsonObject1.getString("fromDate"));
//                                model.setToDate(jsonObject1.getString("toDate"));
//                                model.setTypeId(jsonObject1.getString("typeId"));
//
//                                eventModelsList.add(model);
//
//                            }
//                            message = jsonObject.getString("message");
//                            error = jsonObject.getString("errorMessage");
//
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Events.this, RecyclerView.VERTICAL, false);
//                            recyclerEvent.setLayoutManager(linearLayoutManager);
//                            adapter=new EventAdapter(eventModelsList,Events.this,loadDetails);
//                            recyclerEvent.setAdapter(adapter);
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
//                    Utils.showAlertDialog(Events.this,"No Data Found",false);
//                }
            }

            @Override
            public void onFailure(Call<EventModel> call, Throwable t) {
                progressLayout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(Events.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void initRecycler() {
        if (modelList!=null){
            noDataImage.setVisibility(View.GONE);
            recyclerEvent.setVisibility(View.VISIBLE);
            adapter = new EventAdapter(modelList,Events.this,loadDetails);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerEvent.setLayoutManager(layoutManager);
            recyclerEvent.setAdapter(adapter);
        }else {
            recyclerEvent.setVisibility(View.GONE);
            noDataImage.setVisibility(View.VISIBLE);
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
        if (userDetails.getSecurityid().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(Events.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(Events.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onMethodCallback() {
    }
}
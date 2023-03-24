package com.client.vcarecloud;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.EventTypeAdapterForUpdate;
import com.client.vcarecloud.models.EventTypeModel;
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

public class EventTypeForUpdate extends AppCompatActivity implements View.OnClickListener {

    ImageView back, cancel, noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView eventRecyclerView;
    RelativeLayout progress;
    AppCompatButton add;

    ArrayList<EventTypeModel> eventTypeModelArrayList = new ArrayList<>();
    LoadDetails loadDetails;
    EventTypeAdapterForUpdate adapter;
    EventTypeModel eventTypeModel;
    UserDetails userDetails;
    String custId, typeId, message, error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_type);

        back = findViewById(R.id.back);
        cancel = findViewById(R.id.cancel);
        noData = findViewById(R.id.noDataImage);
        search = findViewById(R.id.search);
        swipe = findViewById(R.id.swipe);
        add = findViewById(R.id.addNew);
        eventRecyclerView = findViewById(R.id.eventTypeRecycler);
        eventRecyclerView.setHasFixedSize(true);
        progress = findViewById(R.id.progressLayout);

        eventTypeModel = new EventTypeModel();
        userDetails = new UserDetails(EventTypeForUpdate.this);
        adapter = new EventTypeAdapterForUpdate(eventTypeModelArrayList, EventTypeForUpdate.this, loadDetails);

        custId = getIntent().getStringExtra("custId");
        typeId = getIntent().getStringExtra("typeId");

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(EventTypeForUpdate.this, UpdateEvent.class);
////                startActivity(intent);
////                finish();
//                onBackPressed();
//            }
//        });
        back.setOnClickListener(this);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (search.getText().toString().length() > 0) {
                            swipe.setRefreshing(false);

                        } else {
                            swipe.setRefreshing(false);
                            eventTypeModelArrayList.clear();
                            eventTypeList();
                        }
                    }
                }, 2000);
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
                if (search.getText().toString().isEmpty()) {
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventTypeForUpdate.this, AddEventTypeForUpdate.class);
                startActivity(intent);
            }
        });
        eventTypeList();
    }

    private void eventTypeList() {
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
        Call<String> call=api.eventTypeList(userDetails.getCustId());
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
                                EventTypeModel model = new EventTypeModel();

//                                userDetails.setEmpId(jsonObject1.getString("typeId"));
                                model.setTypeId(jsonObject1.getString("typeId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setTypeName(jsonObject1.getString("typeName"));

                                eventTypeModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EventTypeForUpdate.this, RecyclerView.VERTICAL, false);
                            eventRecyclerView.setLayoutManager(linearLayoutManager);
                            adapter=new EventTypeAdapterForUpdate(eventTypeModelArrayList, EventTypeForUpdate.this,loadDetails);
                            eventRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progress.setVisibility(View.GONE);
                    Utils.showAlertDialog(EventTypeForUpdate.this,"No Data Found",false);
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
                Toast.makeText(EventTypeForUpdate.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent =new Intent(EventTypeForUpdate.this,UpdateEvent.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }

    private void filter(String text) {
        ArrayList<EventTypeModel> filteredNames = new ArrayList<>();
        for (EventTypeModel s : eventTypeModelArrayList) {
            if ((s.getTypeId().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getTypeName().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (eventTypeModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                eventRecyclerView.setVisibility(View.VISIBLE);
                swipe.setEnabled(false);
            }
            adapter.filterList(filteredNames);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                onBackPressed();
                break;
        }
    }
}

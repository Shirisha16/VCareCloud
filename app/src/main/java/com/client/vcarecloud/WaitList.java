package com.client.vcarecloud;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.ChildrenWaitListAdapter;
import com.client.vcarecloud.models.ChildWaitListModel;
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

public class WaitList extends AppCompatActivity implements LoadDetails{
    RecyclerView waitRecycler;
    ArrayList<ChildWaitListModel> childWaitListModelArrayList=new ArrayList<>();
    ImageView back,noData,cancel;
    RelativeLayout progress;
    UserDetails userDetails;
    EditText search;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    SwipeRefreshLayout swipe;
    ChildrenWaitListAdapter adapter;
    String custId,message,error;
    LoadDetails loadDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        waitRecycler = findViewById(R.id.waitListRecycler);
        waitRecycler.setHasFixedSize(true);
        userDetails = new UserDetails(WaitList.this);
        back = findViewById(R.id.back);
        search = findViewById(R.id.search);
        cancel = findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        relativeLayout = findViewById(R.id.recyclerLayout);
        noData = findViewById(R.id.noData_leaveList_admin_approved);
        linearLayout = findViewById(R.id.linear1);
        adapter = new ChildrenWaitListAdapter(childWaitListModelArrayList, WaitList.this, loadDetails);
        progress = findViewById(R.id.progressLayout);
        swipe = findViewById(R.id.swipe);
//        custId=getIntent().getStringExtra("custId");
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipe.setRefreshing(false);
                        childWaitListModelArrayList.clear();
                        childrenWaitList();
                        search.setText("");
                    }
                }, 2000);


            }
        });
        childrenWaitList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(WaitList.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(WaitList.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
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
                filter(s.toString());
                if (search.getText().toString().isEmpty()) {
                    cancel.setVisibility(View.INVISIBLE);
                } else {
                    cancel.setVisibility(View.VISIBLE);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                search.setText("");
                cancel.setVisibility(View.INVISIBLE);
                search.clearFocus();
                waitRecycler.setVisibility(View.VISIBLE);
            }
        });
    }
        @Override
        public void onResume () {
            super.onResume();
            search.setText("");
        }
        //for searching
        private void filter (String text){
            ArrayList<ChildWaitListModel> filteredNames = new ArrayList<>();
            for (ChildWaitListModel s : childWaitListModelArrayList) {
                if ((s.getFirstName().toLowerCase().contains(text.toLowerCase())) ||
                        (s.getDisplayName().toLowerCase().contains(text.toLowerCase())) ||
                        (s.getSex().toLowerCase().contains(text.toLowerCase()))) {
                    filteredNames.add(s);
                    noData.setVisibility(View.INVISIBLE);
                }
            }
            if (childWaitListModelArrayList.size() != 0) {
                if (filteredNames.isEmpty()) {
                    noData.setVisibility(View.VISIBLE);
                    waitRecycler.setVisibility(View.VISIBLE);
//                    swipe.setEnabled(false);
                }

                adapter.filterList(filteredNames);
            }

        }

    @Override
    public void onMethodCallback() {

    }


    private void childrenWaitList() {
        progress.setVisibility(View.VISIBLE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();
        VcareApi api=retrofit.create(VcareApi.class);
        Call<String> call=api.children_wait_list(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(ChildrenList.this, "yes"+response.body(), Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                if (response.body()!=null){
                    try {
                        JSONObject jsonObject=new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ChildWaitListModel model = new ChildWaitListModel();
                                model.setChildId(jsonObject1.getString("childId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setDisplayName(jsonObject1.getString("displayName"));
                                model.setDob(jsonObject1.getString("dob"));
                                model.setSex(jsonObject1.getString("sex"));
                                model.setCity(jsonObject1.getString("city"));
                                model.setFirstName(jsonObject1.getString("firstName"));

                                childWaitListModelArrayList.add(model);

                                message = jsonObject.getString("message");
                                error = jsonObject.getString("errorMessage");
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WaitList.this, RecyclerView.VERTICAL, false);
                            waitRecycler.setLayoutManager(linearLayoutManager);

                            adapter = new ChildrenWaitListAdapter(childWaitListModelArrayList, WaitList.this, WaitList.this);
                            waitRecycler.setAdapter(adapter);
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")){
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progress.setVisibility(View.GONE);
                    Utils.showAlertDialog(WaitList.this,"No Data Found",false);
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
                Toast.makeText(WaitList.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(WaitList.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(WaitList.this,EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }

}
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
import android.widget.SearchView;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.ChildrenAbsentAdapter;
import com.client.vcarecloud.models.ChildAbsentModel;
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

public class ChildrenAbsentList extends AppCompatActivity implements LoadDetails {
    RecyclerView childrenAbsentRecycler;
    ImageView back,noData,cancel;
    RelativeLayout progress;
    AppCompatButton addNew;
    ArrayList<ChildAbsentModel> childAbsentModelArrayList=new ArrayList<>();
    String custId,firstName,userId,securityid,message,error,classID,reason,className;
    ChildAbsentModel absentModel;
    UserDetails userDetails;
    String empId=null;
    EditText search;
    ChildAbsentModel childAbsentModel;
    SwipeRefreshLayout swipe;
    SearchView searchView;
    ChildrenAbsentAdapter adapter;
    LoadDetails loadDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_absent_list);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        childrenAbsentRecycler=findViewById(R.id.absentListRecycler);
        childrenAbsentRecycler.setHasFixedSize(true);
        back=findViewById(R.id.back);
        childAbsentModel=new ChildAbsentModel();

        progress=findViewById(R.id.progressLayout);
        addNew=findViewById(R.id.addNew);
        swipe=findViewById(R.id.swipe);
        adapter = new ChildrenAbsentAdapter(childAbsentModelArrayList, ChildrenAbsentList.this,loadDetails);
        search=findViewById(R.id.search);
        cancel=findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        noData=findViewById(R.id.noData_leaveList_admin_approved);

        custId=getIntent().getStringExtra("custId");
        empId=getIntent().getStringExtra("empID");
        userId=getIntent().getStringExtra("userID");
        reason=getIntent().getStringExtra("reason");
        className=getIntent().getStringExtra("classId");

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
//                    childrenWaitList();
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
                childAbsentModelArrayList.clear();
                childAbsent();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
//                        swipe.setRefreshing(false);
//                        childAbsentModelArrayList.clear();
//                        childAbsent();
//                        search.setText("");
//                        noData.setVisibility(View.GONE);
                        if(search.getText().toString().length()>0){
                            swipe.setRefreshing(false);

                        }else{
                            swipe.setRefreshing(false);
                            childAbsentModelArrayList.clear();
                            childAbsent();
                        }
                    }
                }, 2000);
            }
        });
        userDetails=new UserDetails(ChildrenAbsentList.this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(ChildrenAbsentList.this,Dashboard.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(ChildrenAbsentList.this,EmployeeDashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChildrenAbsentList.this,AddAbsent.class);
                startActivity(intent);
            }
        });

        childAbsent();

    }

    private void filter(String text) {
        ArrayList<ChildAbsentModel> filteredNames=new ArrayList<>();
        for (ChildAbsentModel s:childAbsentModelArrayList){
            if ((s.getChildName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getClassName().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getAbsentFrom().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getAbsentTo().toLowerCase().contains(text.toLowerCase())) ||
                    (s.getAbsentType().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (childAbsentModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
               childrenAbsentRecycler.setVisibility(View.VISIBLE);
//               swipe.setEnabled(false);
            }

            adapter.filterList(filteredNames);
        }
    }

    private void childAbsent() {
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
        Call<String> call=api.child_absent_list(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(ChildrenAbsentList.this,response.body(), Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                if (response.body()!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ChildAbsentModel model = new ChildAbsentModel();
                                userDetails.setChildId(jsonObject1.getString("childId"));
                                userDetails.setClassId(jsonObject1.getString("classId"));

                                model.setAbsentId(jsonObject1.getString("absentId"));
                                model.setChildId(jsonObject1.getString("childId"));
                                model.setClassId(jsonObject1.getString("classId"));
                                model.setAbsentType(jsonObject1.getString("absentType"));
                                model.setAbsentFrom(jsonObject1.getString("absentFrom"));
                                model.setAbsentTo(jsonObject1.getString("absentTo"));
                                model.setAbsentNotes(jsonObject1.getString("absentNotes"));
                                model.setChildName(jsonObject1.getString("childName"));
                                model.setClassName(jsonObject1.getString("className"));

                                childAbsentModelArrayList.add(model);
                            }

                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChildrenAbsentList.this, RecyclerView.VERTICAL, false);
                                childrenAbsentRecycler.setLayoutManager(linearLayoutManager);
                                adapter = new ChildrenAbsentAdapter(childAbsentModelArrayList, ChildrenAbsentList.this,ChildrenAbsentList.this);
                                childrenAbsentRecycler.setAdapter(adapter);

                        } else if (jsonObject.optString("message").equalsIgnoreCase("null")){
                            noData.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
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
                Toast.makeText(ChildrenAbsentList.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(ChildrenAbsentList.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(ChildrenAbsentList.this,EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onMethodCallback() {
    }

}
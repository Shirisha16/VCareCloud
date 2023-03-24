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
import com.client.vcarecloud.Adapters.EyecolorAdapter;
import com.client.vcarecloud.models.LookupTypeModel;
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

public class EyeColor extends AppCompatActivity implements  LoadDetails{
    ImageView back,cancel,noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView eyeRecycler;
    RelativeLayout progress;
    AppCompatButton add;

    ArrayList<LookupTypeModel> lookupTypeModelArrayList=new ArrayList<>();
    LoadDetails loadDetails;
    EyecolorAdapter adapter;
    LookupTypeModel lookupTypeModel;
    UserDetails userDetails;
    String custId,message,error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_color);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        cancel=findViewById(R.id.cancel);
        noData=findViewById(R.id.noDataImage);
        search=findViewById(R.id.search);
        swipe=findViewById(R.id.swipe);
        add=findViewById(R.id.addNew);
        eyeRecycler=findViewById(R.id.eyeRecycler);
        eyeRecycler.setHasFixedSize(true);
        progress=findViewById(R.id.progressLayout);

        lookupTypeModel=new LookupTypeModel();
        userDetails=new UserDetails(EyeColor.this);
        adapter= new EyecolorAdapter(lookupTypeModelArrayList,EyeColor.this,loadDetails);

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EyeColor.this,Lookups.class);
                startActivity(intent);
                finish();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

//                        swipe.setRefreshing(false);
//                        lookupTypeModelArrayList.clear();
//                        eyeColorList();
                        if(search.getText().toString().length()>0){
                            swipe.setRefreshing(false);

                        }else{
                            swipe.setRefreshing(false);
                            lookupTypeModelArrayList.clear();
                            eyeColorList();
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EyeColor.this,AddEyeColor.class);
                startActivity(intent);
            }
        });

        eyeColorList();
    }

    private void filter(String text) {
        ArrayList<LookupTypeModel> filteredNames=new ArrayList<>();
        for (LookupTypeModel s:lookupTypeModelArrayList){
            if ((s.getLookupName().toLowerCase().contains(text.toLowerCase()))){
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (lookupTypeModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);

            }

            adapter.filterList(filteredNames);
        }
    }

    private void eyeColorList() {
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
        Call<String> call=api.lookupType(custId,"Eye Color");
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
                                LookupTypeModel model = new LookupTypeModel();

                                model.setLookupsId(jsonObject1.getString("lookupsId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setLookupType(jsonObject1.getString("lookupType"));
                                model.setLookupName(jsonObject1.getString("lookupName"));
                                model.setLookupCategory(jsonObject1.getString("lookupCategory"));

                                lookupTypeModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EyeColor.this, RecyclerView.VERTICAL, false);
                            eyeRecycler.setLayoutManager(linearLayoutManager);
                            adapter = new EyecolorAdapter(lookupTypeModelArrayList, EyeColor.this, loadDetails);
                            eyeRecycler.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    progress.setVisibility(View.GONE);
                    Utils.showAlertDialog(EyeColor.this,"No Data Found",false);
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
                Toast.makeText(EyeColor.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(EyeColor.this,Lookups.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        search.setText("");
    }

    @Override
    public void onMethodCallback() {
    }
}
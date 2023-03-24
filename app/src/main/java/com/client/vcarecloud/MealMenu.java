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
import com.client.vcarecloud.Adapters.MenuAdapter;
import com.client.vcarecloud.models.MealMenuModel;
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

public class MealMenu extends AppCompatActivity implements LoadDetails{
    ImageView back,cancel,noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    RelativeLayout progress;
    AppCompatButton add;

    String custId,message,error;

    ArrayList<MealMenuModel> mealMenuModelArrayList=new ArrayList<>();
    UserDetails userDetails;
    MealMenuModel mealMenuModel;
    MenuAdapter adapter;
    LoadDetails loadDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        cancel=findViewById(R.id.cancel);
        noData=findViewById(R.id.noDataImage);
        search=findViewById(R.id.search);
        swipe=findViewById(R.id.swipe);
        add=findViewById(R.id.addNew);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        progress=findViewById(R.id.progressLayout);

        mealMenuModel=new MealMenuModel();
        mealMenuModel = (MealMenuModel) getIntent().getSerializableExtra("list");

        adapter = new MenuAdapter(mealMenuModelArrayList, MealMenu.this,loadDetails);

        custId=getIntent().getStringExtra("custId");

        userDetails=new UserDetails(MealMenu.this);

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

//                    mealMenuModelArrayList.clear();
//                    menuDetails();
                } else {
                    cancel.setVisibility(View.VISIBLE);
//                    swipe.setRefreshing(false);
//                    mealMenuModelArrayList.clear();
                }
//                menuDetails();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setText("");
                cancel.setVisibility(View.INVISIBLE);
                search.clearFocus();
                mealMenuModelArrayList.clear();
                menuDetails();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
//                        swipe.setRefreshing(false);
//                        mealMenuModelArrayList.clear();
//                        menuDetails();
                        if(search.getText().toString().length()>0){
                            swipe.setRefreshing(false);
                        }
                        else{
                            swipe.setRefreshing(false);
                            mealMenuModelArrayList.clear();
                            menuDetails();
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MealMenu.this,AddMealMenu.class);
                startActivity(intent);
            }
        });
        menuDetails();
    }

    private void menuDetails() {
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
        Call<String> call=api.menuList(userDetails.getCustId());
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
                                MealMenuModel model = new MealMenuModel();

                                model.setLookupsId(jsonObject1.getString("lookupsId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setLookupType(jsonObject1.getString("lookupType"));
                                model.setLookupName(jsonObject1.getString("lookupName"));
                                model.setLookupCategory(jsonObject1.getString("lookupCategory"));

                                mealMenuModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MealMenu.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            adapter = new MenuAdapter(mealMenuModelArrayList, MealMenu.this, MealMenu.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MealMenu.this, "No Data Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MealMenu.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void filter(String text) {
        ArrayList<MealMenuModel> filteredNames=new ArrayList<>();
        for (MealMenuModel s:mealMenuModelArrayList){
            if ((s.getLookupName().toLowerCase().contains(text.toLowerCase())) ||
                (s.getLookupCategory().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (mealMenuModelArrayList.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
//                adapter.notifyDataSetChanged();
            }

            adapter.filterList(filteredNames);
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
//        Intent intent =new Intent(MealMenu.this,Dashboard.class);
////        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
            Intent intent = new Intent(MealMenu.this, Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
            Intent intent = new Intent(MealMenu.this, EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onMethodCallback() {
    }
}
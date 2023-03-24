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
import com.client.vcarecloud.Adapters.SecurityProfileAdapter;
import com.client.vcarecloud.models.SecurityProfileModel;
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

public class SecurityProfile extends AppCompatActivity implements LoadDetails{
    ImageView back,cancel,noData;
    EditText search;
    SwipeRefreshLayout swipe;
    RecyclerView securityRecyclerView;
    RelativeLayout progress;
    AppCompatButton add;
    ArrayList<SecurityProfileModel> profileModel=new ArrayList<>();
    LoadDetails loadDetails;
    SecurityProfileAdapter adapter;
    SecurityProfileModel securityModel;
    UserDetails userDetails;
    String custId,message,error;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_profile);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        cancel=findViewById(R.id.cancel);
        noData=findViewById(R.id.noData_list);
        search=findViewById(R.id.search);
        swipe=findViewById(R.id.swipe);
        add=findViewById(R.id.addNew);
        securityRecyclerView=findViewById(R.id.recyclerSecurity);
        securityRecyclerView.setHasFixedSize(true);
        progress=findViewById(R.id.progressLayout);

        securityModel=new SecurityProfileModel();
        userDetails=new UserDetails(SecurityProfile.this);
        adapter= new SecurityProfileAdapter(profileModel,SecurityProfile.this,loadDetails);

        custId=getIntent().getStringExtra("custId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(SecurityProfile.this,Dashboard.class);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        swipe.setRefreshing(false);
                        profileModel.clear();
                        profileList();
                        search.setText("");
                        noData.setVisibility(View.GONE);
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
                securityRecyclerView.setVisibility(View.VISIBLE);
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SecurityProfile.this,AddSecurityProfile.class);
                startActivity(intent);
            }
        });

        profileList();
    }

    private void profileList() {
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
        Call<String> call=api.security_profile(userDetails.getCustId());
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
                                SecurityProfileModel model = new SecurityProfileModel();

                                model.setSecurityId(jsonObject1.getString("securityId"));
                                model.setCustId(jsonObject1.getString("custId"));
                                model.setSecurityProfileName(jsonObject1.getString("securityProfileName"));
                                model.setCheckIn(jsonObject1.getString("access_ChildCheckInOut"));
                                model.setAbsent(jsonObject1.getString("access_ChildAbsent"));
                                model.setChildView(jsonObject1.getString("access_ViewChild"));
                                model.setModifyChild(jsonObject1.getString("access_ModifyChild"));
                                model.setViewWaitList(jsonObject1.getString("access_Waitlist"));
                                model.setModifyWait(jsonObject1.getString("access_ModifyWaitlist"));
                                model.setEmployeeView(jsonObject1.getString("access_Employee"));
                                model.setModifyemp(jsonObject1.getString("access_ModifyEmployee"));
                                model.setShiftsView(jsonObject1.getString("access_Shift"));
                                model.setModifyShifts(jsonObject1.getString("access_ModifyShift"));
                                model.setActivityView(jsonObject1.getString("access_ActivityPlanner"));
                                model.setModifyActPlanner(jsonObject1.getString("access_ModifyActivityPlanner"));
                                model.setDailyActView(jsonObject1.getString("access_DailyActivity"));
                                model.setModifyDailyAct(jsonObject1.getString("access_ModifyDailyActivity"));
                                model.setEventsView(jsonObject1.getString("access_ActivityTheme"));
                                model.setEventmodify(jsonObject1.getString("access_ModifyActivityTheme"));
                                model.setActCategories(jsonObject1.getString("access_ActivityCategory"));
                                model.setCampsView(jsonObject1.getString("access_Camp"));
                                model.setCampsModify(jsonObject1.getString("access_ModifyCamp"));
                                model.setMealView(jsonObject1.getString("access_MealPlanner"));
                                model.setModifyMeal(jsonObject1.getString("access_ModifyMealPlanner"));
                                model.setDailyMealsView(jsonObject1.getString("access_DailyMeal"));
                                model.setDailyMealsModify(jsonObject1.getString("access_ModifyDailyMeal"));
                                model.setMenu(jsonObject1.getString("access_Menu"));
                                model.setMealPortion(jsonObject1.getString("access_MealPortion"));
                                model.setPackageView(jsonObject1.getString("access_BasePackages"));
                                model.setModifyPackage(jsonObject1.getString("access_ModifyBasePackages"));
                                model.setAddChargesView(jsonObject1.getString("access_AdditionalCharges"));
                                model.setAddChargesModify(jsonObject1.getString("access_ModifyAdditionalCharges"));
                                model.setDiscountView(jsonObject1.getString("access_Discounts"));
                                model.setModifyDiscount(jsonObject1.getString("access_ModifyDiscounts"));
                                model.setAdjustmentView(jsonObject1.getString("access_Adjustments"));
                                model.setAdjustmentModify(jsonObject1.getString("access_ModifyAdjustments"));
                                model.setGenerateInvoice(jsonObject1.getString("access_GenerateInvoice"));
                                model.setChildCloseout(jsonObject1.getString("access_ChildCloseout"));
                                model.setPaymentType(jsonObject1.getString("access_PaymentTypes"));
                                model.setViewpayments(jsonObject1.getString("access_Payments"));
                                model.setModifyPayments(jsonObject1.getString("access_ModifyPayments"));
                                model.setGeneralSetting(jsonObject1.getString("access_GeneralSetup"));
                                model.setLookup(jsonObject1.getString("access_LookupSetup"));
                                model.setPrograms(jsonObject1.getString("access_ClassSetup"));
                                model.setImmunization(jsonObject1.getString("access_ImmunizationSetup"));
                                model.setSecurityProfile(jsonObject1.getString("access_Security"));
                                model.setViewReport(jsonObject1.getString("access_Report"));
                                model.setViewMsgs(jsonObject1.getString("access_Messaging"));
                                model.setSendMsgs(jsonObject1.getString("access_SendMessages"));
                                profileModel.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SecurityProfile.this, RecyclerView.VERTICAL, false);
                            securityRecyclerView.setLayoutManager(linearLayoutManager);
                            adapter=new SecurityProfileAdapter(profileModel,SecurityProfile.this,SecurityProfile.this);
                            securityRecyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }else if (jsonObject.optString("message").equalsIgnoreCase("null")) {
                            noData.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    progress.setVisibility(View.GONE);
                    Utils.showAlertDialog(SecurityProfile.this,"No Data Found",false);
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
                Toast.makeText(SecurityProfile.this, message, Toast.LENGTH_SHORT).show();
                finish();
//                Toast.makeText(SecurityProfile.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String text) {
        ArrayList<SecurityProfileModel> filteredNames=new ArrayList<>();
        for (SecurityProfileModel s:profileModel){
            if ((s.getSecurityProfileName().toLowerCase().contains(text.toLowerCase()))) {
                filteredNames.add(s);
                noData.setVisibility(View.INVISIBLE);
            }
        }
        if (profileModel.size() != 0) {
            if (filteredNames.isEmpty()) {
                noData.setVisibility(View.VISIBLE);
                securityRecyclerView.setVisibility(View.VISIBLE);
                swipe.setEnabled(false);
            }
            adapter.filterList(filteredNames);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(SecurityProfile.this,Dashboard.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(SecurityProfile.this,EmployeeDashboard.class);
            startActivity(intent);
            finish();
        }

//        Intent intent=new Intent(SecurityProfile.this,Dashboard.class);
//        startActivity(intent);
//        finish();
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
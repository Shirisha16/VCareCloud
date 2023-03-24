package com.client.vcarecloud.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.R;
import com.client.vcarecloud.UpdateSecurityProfile;
import com.client.vcarecloud.models.SecurityProfileModel;
import com.client.vcarecloud.utils.Utils;

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

public class SecurityProfileAdapter extends RecyclerView.Adapter<SecurityProfileAdapter.MyViewHolder> {
    ArrayList<SecurityProfileModel> profileModelList;
    Context context;
    LoadDetails loadDetails;
    RelativeLayout progressRelative;
    String message,error;
    public SecurityProfileAdapter(ArrayList<SecurityProfileModel> profileModelList, Context context, LoadDetails loadDetails) {
        this.profileModelList = profileModelList;
        this.context = context;
        this.loadDetails = loadDetails;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_security_design,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        progressRelative=view.findViewById(R.id.progress);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textProfile.setText(profileModelList.get(position).getSecurityProfileName());

        SecurityProfileModel model = profileModelList.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = model.getSecurityId();
                String custid = model.getCustId();
                String name = model.getSecurityProfileName();
                String checkin = model.getCheckIn();
                String absent = model.getAbsent();
                String viewChild = model.getChildView();
                String modifyChild = model.getModifyChild();
                String waitlist = model.getViewWaitList();
                String modify_waitList = model.getModifyWait();
                String emp = model.getEmployeeView();
                String modifyEmp = model.getModifyemp();
                String shift = model.getShiftsView();
                String modifyShift = model.getModifyShifts();
                String activityPlanner = model.getActivityView();
                String modify_activityPlanner = model.getModifyActPlanner();
                String dailyActivity = model.getDailyActView();
                String modifyDailyActivity = model.getModifyDailyAct();
                String activityTheme = model.getEventsView();
                String modify_actTheme = model.getEventmodify();
                String activityCategory = model.getActCategories();
                String camp = model.getCampsView();
                String modifyCamp = model.getCampsModify();
                String mealPlanner = model.getMealView();
                String modify_mealPlanner = model.getModifyMeal();
                String dailyMeal = model.getDailyMealsView();
                String modify_dailyMeal = model.getDailyMealsModify();
                String menu1 = model.getMenu();
                String mealPortion1 = model.getMealPortion();
                String basePackage = model.getPackageView();
                String modify_basePackage = model.getModifyPackage();
                String addCharges = model.getAddChargesView();
                String modify_addCharges = model.getAddChargesModify();
                String discount = model.getDiscountView();
                String modify_discount = model.getModifyDiscount();
                String adjustment = model.getAdjustmentView();
                String modify_adjustment = model.getAdjustmentModify();
                String generate_Invoice = model.getGenerateInvoice();
                String child_closeOut = model.getChildCloseout();
                String payment_type = model.getPaymentType();
                String payments = model.getViewpayments();
                String modify_payments = model.getModifyPayments();
                String general = model.getGeneralSetting();
                String lookupSetup = model.getLookup();
                String classSetUp = model.getPrograms();
                String immunizationSetup = model.getImmunization();
                String security = model.getSecurityProfile();
                String report = model.getViewReport();
                String messaging = model.getViewMsgs();
                String sendMsg = model.getSendMsgs();


                Intent intent = new Intent(context, UpdateSecurityProfile.class);
                intent.putExtra("securityId", id);
                intent.putExtra("custId", custid);
                intent.putExtra("securityProfileName", name);
                intent.putExtra("access_ChildCheckInOut", checkin);
                intent.putExtra("access_ChildAbsent", absent);
                intent.putExtra("access_ViewChild", viewChild);
                intent.putExtra("access_ModifyChild", modifyChild);
                intent.putExtra("access_Waitlist", waitlist);
                intent.putExtra("access_ModifyWaitlist", modify_waitList);
                intent.putExtra("access_Employee", emp);
                intent.putExtra("access_ModifyEmployee", modifyEmp);
                intent.putExtra("access_Shift", shift);
                intent.putExtra("access_ModifyShift", modifyShift);
                intent.putExtra("access_ActivityPlanner", activityPlanner);
                intent.putExtra("access_ModifyActivityPlanner", modify_activityPlanner);
                intent.putExtra("access_DailyActivity", dailyActivity);
                intent.putExtra("access_ModifyDailyActivity", modifyDailyActivity);
                intent.putExtra("access_ActivityTheme", activityTheme);
                intent.putExtra("access_ModifyActivityTheme", modify_actTheme);
                intent.putExtra("access_ActivityCategory", activityCategory);
                intent.putExtra("access_Camp", camp);
                intent.putExtra("access_ModifyCamp", modifyCamp);
                intent.putExtra("access_MealPlanner", mealPlanner);
                intent.putExtra("access_ModifyMealPlanner", modify_mealPlanner);
                intent.putExtra("access_DailyMeal", dailyMeal);
                intent.putExtra("access_ModifyDailyMeal", modify_dailyMeal);
                intent.putExtra("access_Menu", menu1);
                intent.putExtra("access_MealPortion", mealPortion1);
                intent.putExtra("access_BasePackages", basePackage);
                intent.putExtra("access_ModifyBasePackages", modify_basePackage);
                intent.putExtra("access_AdditionalCharges", addCharges);
                intent.putExtra("access_ModifyAdditionalCharges", modify_addCharges);
                intent.putExtra("access_Discounts", discount);
                intent.putExtra("access_ModifyDiscounts", modify_discount);
                intent.putExtra("access_Adjustments", adjustment);
                intent.putExtra("access_ModifyAdjustments", modify_adjustment);
                intent.putExtra("access_GenerateInvoice", generate_Invoice);
                intent.putExtra("access_ChildCloseout", child_closeOut);
                intent.putExtra("access_PaymentTypes", payment_type);
                intent.putExtra("access_Payments", payments);
                intent.putExtra("access_ModifyPayments", modify_payments);
                intent.putExtra("access_GeneralSetup", general);
                intent.putExtra("access_LookupSetup", lookupSetup);
                intent.putExtra("access_ClassSetup", classSetUp);
                intent.putExtra("access_ImmunizationSetup", immunizationSetup);
                intent.putExtra("access_Security", security);
                intent.putExtra("access_Report", report);
                intent.putExtra("access_Messaging", messaging);
                intent.putExtra("access_SendMessages", sendMsg);
                intent.putExtra("list", model);
                context.startActivity(intent);
            }
        });

//        holder.remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                        .callTimeout(2, TimeUnit.MINUTES)
//                        .connectTimeout(90,TimeUnit.SECONDS)
//                        .readTimeout(30,TimeUnit.SECONDS)
//                        .writeTimeout(30,TimeUnit.SECONDS);
//                Retrofit retrofit=new Retrofit.Builder()
//                        .baseUrl(VcareApi.JSONURL)
//                        .addConverterFactory(ScalarsConverterFactory.create())
//                        .client(httpClient.build())
//                        .build();
//                VcareApi api=retrofit.create(VcareApi.class);
//                Call<String> call=api.delSecurityProfile(model.getSecurityId(),"0");
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null) {
//                            try {
//
//                                JSONObject jsonObject = new JSONObject(response.body());
//                                message = jsonObject.getString("message");
//                                error = jsonObject.getString("errorMessage");
//                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")) {
//                                    Utils.showAlertDialog(context,message,false);
//                                    profileModelList.remove(position);
//                                    notifyItemRemoved(position);
//                                }else{
//                                    Utils.showAlertDialog(context,error,false);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        String message = "";
//                        if (t instanceof UnknownHostException) {
//                            message = "No internet connection!";
//                        } else {
//                            message = "Something went wrong! try again";
//                        }
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want to Delete?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VcareApi vcareApi=retrofit.create(VcareApi.class);
                        Call<String> call=vcareApi.delSecurityProfile(model.getSecurityId(),"0");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body()!=null){
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body());
                                        String message=jsonObject.getString("message");
                                        String  error=jsonObject.getString("errorMessage");
                                        if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")) {
                                            Utils.showAlertDialog(context, message, false);
                                            profileModelList.remove(position);
                                            notifyItemRemoved(position);
                                        }else {
                                            Utils.showAlertDialog(context,error,false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progressRelative.setVisibility(View.GONE);
                                String message="";
                                if (t instanceof UnknownHostException){
                                    message="No Internet Connection!";
                                }else{
                                    message="Something went wrong! try again";
                                }
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();
                Utils.preventTwoClick(v);
            }
        });

    }
    @Override
    public int getItemCount() {
        return profileModelList.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textProfile;
        AppCompatButton edit,remove;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textProfile=itemView.findViewById(R.id.profile);
            edit=itemView.findViewById(R.id.edit);
            remove=itemView.findViewById(R.id.delete);
        }
    }
    public void filterList(ArrayList<SecurityProfileModel> filterdNames) {
        this.profileModelList = filterdNames;
        notifyDataSetChanged();
    }
}

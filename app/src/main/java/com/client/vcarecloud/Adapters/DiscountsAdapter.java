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
import com.client.vcarecloud.UpdateDiscount;
import com.client.vcarecloud.models.DiscountListModel;
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

public class DiscountsAdapter extends RecyclerView.Adapter<DiscountsAdapter.MyViewHolder> {
    ArrayList<DiscountListModel> discountListModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public DiscountsAdapter(ArrayList<DiscountListModel> discountListModelArrayList, Context context, LoadDetails loadDetails) {
        this.discountListModelArrayList = discountListModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public DiscountsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_discounts,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progressLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.discount.setText(discountListModelArrayList.get(position).getDiscountName());
        holder.value.setText(discountListModelArrayList.get(position).getDiscountValue());

        String status1=discountListModelArrayList.get(position).getStatus();
        if ((status1.equalsIgnoreCase("Y"))) {
            holder.status.setText("Active");
        }else{
            holder.status.setText("Inactive");
        }

        DiscountListModel discountModel=discountListModelArrayList.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=discountModel.getDiscountId();
                String empid=discountModel.getEmpId();
                String discountId=discountModel.getDiscountId();
                String custid=discountModel.getCustId();
                String discountName=discountModel.getDiscountName();
                String description=discountModel.getDescription();
                String type=discountModel.getDiscountType();
                String value=discountModel.getDiscountValue();
                String status=discountModel.getStatus();
                String limitedPeriod=discountModel.getCheckLimitedPeriod();
                String from=discountModel.getFromDate();
                String to=discountModel.getToDate();
                String basepackage=discountModel.getBasePackage();
                String additional=discountModel.getAdditionalCharge();
                String camp=discountModel.getCamp();
                String activity=discountModel.getApplicableActivity();
                String all=discountModel.getApplicableAll();

                Intent intent=new Intent(context, UpdateDiscount.class);
                intent.putExtra("Id",id);
                intent.putExtra("empId",empid);
                intent.putExtra("discountId",discountId);
                intent.putExtra("custId",custid);
                intent.putExtra("discountName",discountName);
                intent.putExtra("discountDescription",description);
                intent.putExtra("discountType",type);
                intent.putExtra("discountValue",value);
                intent.putExtra("discountStatus",status);
                intent.putExtra("checkLimitedPeriod",limitedPeriod);
                intent.putExtra("limitedPeriodFromDate",from);
                intent.putExtra("limitedPeriodToDate",to);
                intent.putExtra("applicable_BasePackage",basepackage);
                intent.putExtra("applicable_AdditionalCharge",additional);
                intent.putExtra("applicable_Camp",camp);
                intent.putExtra("applicable_Activity",activity);
                intent.putExtra("applicable_All",all);
                intent.putExtra("list",discountModel);
                context.startActivity(intent);
            }
        });

//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                        .callTimeout(2, TimeUnit.MINUTES)
//                        .connectTimeout(90,TimeUnit.SECONDS)
//                        .readTimeout(30,TimeUnit.SECONDS)
//                        .writeTimeout(30,TimeUnit.SECONDS);
//
//                Retrofit retrofit=new Retrofit.Builder()
//                        .baseUrl(VcareApi.JSONURL)
//                        .addConverterFactory(ScalarsConverterFactory.create())
//                        .client(httpClient.build())
//                        .build();
//
//                VcareApi vcareApi=retrofit.create(VcareApi.class);
//                Call<String> call=vcareApi.delete_discount(discountModel.getDiscountId(),"0");
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null){
//                            try {
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                String message=jsonObject.getString("message");
//                                Utils.showAlertDialog(context,message,false);
//                                discountListModelArrayList.remove(position);
//                                notifyItemRemoved(position);
//                                notifyDataSetChanged();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        progressRelative.setVisibility(View.GONE);
//                        String message="";
//                        if (t instanceof UnknownHostException){
//                            message="No Internet Connection!";
//                        }else{
//                            message="Something went wrong! try again";
//                        }
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
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
                        Call<String> call=vcareApi.delete_discount(discountModel.getDiscountId(),"0");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body()!=null){
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body());
                                        String message=jsonObject.getString("message");
                                        String  error=jsonObject.getString("errorMessage");
                                        if (message.equalsIgnoreCase("Deleted Successfully!")) {
                                            Utils.showAlertDialog(context, message, false);
                                            discountListModelArrayList.remove(position);
                                            notifyItemRemoved(position);
                                        }else{
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
        return discountListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView discount,value,status;
        AppCompatButton edit,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            discount=itemView.findViewById(R.id.discount1);
            value=itemView.findViewById(R.id.value1);
            status=itemView.findViewById(R.id.status1);

            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
        }
    }

    public void filterList(ArrayList<DiscountListModel> filteredNames) {
        this.discountListModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

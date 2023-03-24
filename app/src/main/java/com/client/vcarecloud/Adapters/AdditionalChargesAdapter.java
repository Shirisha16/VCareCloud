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
import com.client.vcarecloud.UpdateAdditionalCharges;
import com.client.vcarecloud.models.AdditionalChargeListModel;
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

public class AdditionalChargesAdapter extends RecyclerView.Adapter<AdditionalChargesAdapter.MyViewHolder>{
    ArrayList<AdditionalChargeListModel> additionalChargeListModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public AdditionalChargesAdapter(ArrayList<AdditionalChargeListModel> additionalChargeListModelArrayList, Context context, LoadDetails loadDetails) {
        this.additionalChargeListModelArrayList = additionalChargeListModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public AdditionalChargesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_additional_charges,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progressLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdditionalChargesAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String fromDate1 = additionalChargeListModelArrayList.get(position).getDate();
        String[] parts = fromDate1.split("T");
        String daystartDate = parts[0];
        if (fromDate1 != null) {
            holder.date.setText(daystartDate);
        }

        holder.chargeName.setText(additionalChargeListModelArrayList.get(position).getChargename());
        holder.amount.setText(additionalChargeListModelArrayList.get(position).getAmount());
        holder.applicationOn.setText(additionalChargeListModelArrayList.get(position).getApplicableType());

        String applicableType = additionalChargeListModelArrayList.get(position).getApplicableType();
        if (applicableType.equalsIgnoreCase("Child")) {
            holder.applicationRef.setText(additionalChargeListModelArrayList.get(position).getChildName());
        }else {
            holder.applicationRef.setText(additionalChargeListModelArrayList.get(position).getClassName());
        }

        AdditionalChargeListModel additionalChargeListModel=additionalChargeListModelArrayList.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=additionalChargeListModel.getChargeId();
                String empid=additionalChargeListModel.getEmpid();
                String custid=additionalChargeListModel.getCustId();
                String chargeId=additionalChargeListModel.getChargeId();
                String chargeName=additionalChargeListModel.getChargename();
                String description=additionalChargeListModel.getDescription();
                String amount=additionalChargeListModel.getAmount();
                String taxesId=additionalChargeListModel.getTaxesId();
                String taxname=additionalChargeListModel.getTaxname();
                String date=additionalChargeListModel.getDate();
                String applicationType=additionalChargeListModel.getApplicableType();
                String classid=additionalChargeListModel.getRefApplicableID();
                String className=additionalChargeListModel.getClassName();
                String childid=additionalChargeListModel.getRefApplicableID();
                String childName=additionalChargeListModel.getChildName();

                Intent intent=new Intent(context, UpdateAdditionalCharges.class);
                intent.putExtra("Id",id);
                intent.putExtra("empid",empid);
                intent.putExtra("custId",custid);
                intent.putExtra("additionalChargeId",chargeId);
                intent.putExtra("chargeName",chargeName);
                intent.putExtra("ChargeDescription",description);
                intent.putExtra("chargeAmount",amount);
                intent.putExtra("taxesId",taxesId);
                intent.putExtra("taxName",taxname);
                intent.putExtra("chargeDate",date);
                intent.putExtra("applicableType",applicationType);
//                intent.putExtra("refApplicableID",childid);
//                intent.putExtra("Applicableonclass",classid);

                if(applicationType.equalsIgnoreCase("Child")){
                    intent.putExtra("Applicableonchild",additionalChargeListModelArrayList.get(position).getRefApplicableID());
                }else{
                    intent.putExtra("Applicableonclass",additionalChargeListModelArrayList.get(position).getRefApplicableID());
                }

                intent.putExtra("className",className);
//                intent.putExtra("Applicableonchild",childid);
                intent.putExtra("childName",childName);

                intent.putExtra("list",additionalChargeListModel);
                context.startActivity(intent);
            }
        });

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
                        Call<String> call=vcareApi.delete_additionalCharge(additionalChargeListModel.getChargeId(),"0");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body()!=null){
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body());
                                        String message=jsonObject.getString("message");
                                        if (message.equalsIgnoreCase("Deleted Successfully!")) {
                                            Utils.showAlertDialog(context, message, false);
                                            additionalChargeListModelArrayList.remove(position);
                                            notifyItemRemoved(position);
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
        return additionalChargeListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,chargeName,amount,applicationOn,applicationRef;
        AppCompatButton edit,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            chargeName=itemView.findViewById(R.id.chargeName);
            amount=itemView.findViewById(R.id.amount);
            applicationOn=itemView.findViewById(R.id.application);
            applicationRef=itemView.findViewById(R.id.ref);

            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
        }
    }
    public void filterList(ArrayList<AdditionalChargeListModel> filteredNames) {
        this.additionalChargeListModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

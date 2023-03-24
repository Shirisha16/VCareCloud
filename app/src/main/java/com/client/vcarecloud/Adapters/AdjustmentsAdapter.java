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
import com.client.vcarecloud.UpdateAdjustment;
import com.client.vcarecloud.models.AdjustmentsModel;
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

public class AdjustmentsAdapter extends RecyclerView.Adapter<AdjustmentsAdapter.MyViewHolder>{
    ArrayList<AdjustmentsModel> adjustmentsModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public AdjustmentsAdapter(ArrayList<AdjustmentsModel> adjustmentsModelArrayList, Context context, LoadDetails loadDetails) {
        this.adjustmentsModelArrayList = adjustmentsModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public AdjustmentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_adjustments,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progressLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdjustmentsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.application.setText(adjustmentsModelArrayList.get(position).getChild());

        String fromDate1=adjustmentsModelArrayList.get(position).getAdjustDate();
        String[] parts = fromDate1.split("T");
        String daystartDate = parts[0];
        if (fromDate1!=null) {
            holder.date.setText(daystartDate);
        }

        holder.adjustType.setText(adjustmentsModelArrayList.get(position).getAdjustType());
        holder.amount.setText(adjustmentsModelArrayList.get(position).getAmount());

        AdjustmentsModel adjustmentsModel=adjustmentsModelArrayList.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = adjustmentsModel.getAdjustmentId();
                String custid = adjustmentsModel.getCustId();
                String adjustType = adjustmentsModel.getAdjustType();
                String description = adjustmentsModel.getDescription();
                String amount = adjustmentsModel.getAmount();
                String adjustAmount=adjustmentsModel.getAdjustAmount();
                String date = adjustmentsModel.getAdjustDate();
                String applicableType = adjustmentsModel.getApplicableType();
                String child=adjustmentsModel.getChild();
                String applicationId = adjustmentsModel.getRefApplicableID();

                Intent intent = new Intent(context, UpdateAdjustment.class);
                intent.putExtra("id", id);
                intent.putExtra("custId", custid);
                intent.putExtra("adjustType", adjustType);
                intent.putExtra("adjustDescription", description);
                intent.putExtra("amount", amount);
                intent.putExtra("adjustAmount", adjustAmount);
                intent.putExtra("adjustDate", date);
                intent.putExtra("applicableType", applicableType);
                intent.putExtra("refApplicableID", applicationId);
                intent.putExtra("child", child);

                intent.putExtra("list",adjustmentsModel);
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
//                VcareApi vcareApi=retrofit.create(VcareApi.class);
//                Call<String> call= vcareApi.delete_adjustment(adjustmentsModel.getAdjustmentId(),adjustmentsModel.getCustId());
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null){
//                            try {
////                                JSONObject jsonObject=new JSONObject(response.body());
////                                String message=jsonObject.getString("message");
////                                String error=jsonObject.getString("errorMessage");
////                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
////                                    Utils.showAlertDialog(context,message,false);
////                                    adjustmentsModelArrayList.remove(position);
////                                    notifyItemRemoved(position);
////                                } else {
////                                    Utils.showAlertDialog(context,error,false);
////                                }
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                String message=jsonObject.getString("message");
//                                Utils.showAlertDialog(context,message,false);
//                                adjustmentsModelArrayList.remove(position);
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
                        Call<String> call=vcareApi.delete_adjustment(adjustmentsModel.getAdjustmentId(),"0");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body()!=null){
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body());
                                        String message=jsonObject.getString("message");
                                        if (message.equalsIgnoreCase("Deleted Successfully!")) {
                                            Utils.showAlertDialog(context, message, false);
                                            adjustmentsModelArrayList.remove(position);
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
        return adjustmentsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView application,date,adjustType,amount;
        AppCompatButton edit,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            application=itemView.findViewById(R.id.application);
            date=itemView.findViewById(R.id.date);
            adjustType=itemView.findViewById(R.id.adjustType);
            amount=itemView.findViewById(R.id.amount);

            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
        }
    }
    public void filterList(ArrayList<AdjustmentsModel> filteredNames) {
        this.adjustmentsModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

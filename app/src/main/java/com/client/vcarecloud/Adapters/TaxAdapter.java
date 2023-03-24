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
import com.client.vcarecloud.UpdateTax;
import com.client.vcarecloud.models.TaxModel;
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

public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.MyViewHolder>{

    ArrayList<TaxModel> taxModelArrayList;
    Context context;
    LoadDetails loadDetails;
    RelativeLayout progressRelative;
    String message,error;

    public TaxAdapter(ArrayList<TaxModel> taxModelArrayList, Context context, LoadDetails loadDetails) {
        this.taxModelArrayList = taxModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public TaxAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_taxes,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progress);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaxAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tax_name.setText(taxModelArrayList.get(position).getName());
        holder.tax_rate.setText(taxModelArrayList.get(position).getRate());
//        holder.tax_status.setText(taxModelArrayList.get(position).getTaxStatus());

        String taxStatus1=taxModelArrayList.get(position).getTaxStatus();
        if (taxStatus1.equalsIgnoreCase("true")) {
            holder.tax_status.setText("Active");
        } else {
            holder.tax_status.setText("Inactive");
        }

        TaxModel taxModel=taxModelArrayList.get(position);

//        holder.delete_but.setOnClickListener(new View.OnClickListener() {
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
//                Call<String> call=vcareApi.delete_tax(taxModel.getTaxid(),taxModel.getCustId());
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null){
//                            try {
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                message=jsonObject.getString("message");
//                                error=jsonObject.getString("errorMessage");
//                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//                                    Utils.showAlertDialog(context,message,false);
//                                    taxModelArrayList.remove(position);
//                                    notifyItemRemoved(position);
//                                    notifyDataSetChanged();
//                                } else {
//                                    Utils.showAlertDialog(context,error,false);
//                                }
////                                JSONObject jsonObject=new JSONObject(response.body());
////                                String message=jsonObject.getString("message");
////                                Utils.showAlertDialog(context,message,false);
////                                taxModelArrayList.remove(position);
////                                notifyItemRemoved(position);
////                                notifyDataSetChanged();
//                            } catch (JSONException e) {
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
//
//            }
//        });
        holder.delete_but.setOnClickListener(new View.OnClickListener() {
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
                        Call<String> call=vcareApi.delete_tax(taxModel.getTaxid(),"0");
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
                                            taxModelArrayList.remove(position);
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

        holder.update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taxid=taxModel.getTaxid();
                String custid=taxModel.getCustId();
                String taxname=taxModel.getName();
                String taxrate=taxModel.getRate();
                String taxStatus=taxModel.getTaxStatus();
                String createdon=taxModel.getCreatedOn();
                String modifiedon=taxModel.getModifiedon();
                String status=taxModel.getStatus();

                Intent intent=new Intent(context, UpdateTax.class);
                intent.putExtra("taxesId",taxid);
                intent.putExtra("custId",custid);
                intent.putExtra("taxName",taxname);
                intent.putExtra("taxRate",taxrate);
                intent.putExtra("taxStatus",taxStatus);
                intent.putExtra("createdOn",createdon);
                intent.putExtra("modifiedOn",modifiedon);
                intent.putExtra("status",status);
                intent.putExtra("list",taxModel);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taxModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tax_name,tax_rate,tax_status;
        AppCompatButton delete_but,update_button;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tax_name=itemView.findViewById(R.id.textTaxName);
            tax_rate=itemView.findViewById(R.id.textRate);
            tax_status=itemView.findViewById(R.id.textStatus);

            update_button=itemView.findViewById(R.id.edit);
            delete_but=itemView.findViewById(R.id.delete);
        }
    }

    public void filterList(ArrayList<TaxModel> filteredNames) {
        this.taxModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}


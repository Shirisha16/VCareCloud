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
import com.client.vcarecloud.UpdateBasePackage;
import com.client.vcarecloud.models.BasePackagesModel;
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

public class BasePackagesAdapter extends RecyclerView.Adapter<BasePackagesAdapter.MyViewHolder> {

    ArrayList<BasePackagesModel> basePackagesModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public BasePackagesAdapter(ArrayList<BasePackagesModel> basePackagesModelArrayList, Context context, LoadDetails loadDetails) {
        this.basePackagesModelArrayList = basePackagesModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_base_package,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.packageName.setText(basePackagesModelArrayList.get(position).getPackageName());
        holder.amount.setText(basePackagesModelArrayList.get(position).getAmount());
        holder.tax.setText(basePackagesModelArrayList.get(position).getTax());
        holder.className.setText(basePackagesModelArrayList.get(position).getClasses());

        String status=basePackagesModelArrayList.get(position).getPackageStatus();
        if (status.equalsIgnoreCase("Y")) {
            holder.status.setText("Active");
        } else {
            holder.status.setText("Inactive");
        }

        BasePackagesModel basePackagesModel=basePackagesModelArrayList.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageId = basePackagesModel.getPackageId();
                String custid = basePackagesModel.getCustId();
                String packageName = basePackagesModel.getPackageName();
                String description = basePackagesModel.getDescription();
                String amount = basePackagesModel.getAmount();
                String taxid = basePackagesModel.getTaxid();
                String taxName = basePackagesModel.getTax();
                String classid = basePackagesModel.getClassid();
                String classname = basePackagesModel.getClasses();
                String status=basePackagesModel.getPackageStatus();

                Intent intent = new Intent(context, UpdateBasePackage.class);
                intent.putExtra("packageId", packageId);
                intent.putExtra("custId", custid);
                intent.putExtra("packageName", packageName);
                intent.putExtra("packageDescription", description);
                intent.putExtra("packageAmount", amount);
                intent.putExtra("taxid", taxid);
                intent.putExtra("tax",taxName);
                intent.putExtra("classId", classid);
                intent.putExtra("className", classname);
                intent.putExtra("packageStatus", status);
                intent.putExtra("list",basePackagesModel);

                context.startActivity(intent);
            }
        });

//        holder.remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                            .callTimeout(2, TimeUnit.MINUTES)
//                            .connectTimeout(90,TimeUnit.SECONDS)
//                            .readTimeout(30,TimeUnit.SECONDS)
//                            .writeTimeout(30,TimeUnit.SECONDS);
//
//                    Retrofit retrofit=new Retrofit.Builder()
//                            .baseUrl(VcareApi.JSONURL)
//                            .addConverterFactory(ScalarsConverterFactory.create())
//                            .client(httpClient.build())
//                            .build();
//
//                    VcareApi vcareApi=retrofit.create(VcareApi.class);
//                    Call<String> call=vcareApi.delete_package(basePackagesModel.getPackageId(),"0");
//                    call.enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            if (response.body()!=null){
//                                try {
//                                    JSONObject jsonObject=new JSONObject(response.body());
//                                    String message=jsonObject.getString("message");
//                                    String  error=jsonObject.getString("errorMessage");
//                                    if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//                                        Utils.showAlertDialog(context,message,false);
//                                        basePackagesModelArrayList.remove(position);
//                                        notifyItemRemoved(position);
//                                    } else {
//                                        Utils.showAlertDialog(context,error,false);
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }else{
//                                progressRelative.setVisibility(View.GONE);
//                                Utils.showAlertDialog(context,error,false);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<String> call, Throwable t) {
//                            progressRelative.setVisibility(View.GONE);
//                            String message="";
//                            if (t instanceof UnknownHostException){
//                                message="No Internet Connection!";
//                            }else{
//                                message="Something went wrong! try again";
//                            }
//                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//                        }
//                    });
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
                        Call<String> call=vcareApi.delete_package(basePackagesModel.getPackageId(),"0");
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
                                            basePackagesModelArrayList.remove(position);
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
        return basePackagesModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView packageName,amount,tax,className,status;
        AppCompatButton edit,remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            packageName=itemView.findViewById(R.id.textPackage);
            amount=itemView.findViewById(R.id.textamount);
            tax=itemView.findViewById(R.id.textTax);
            className=itemView.findViewById(R.id.textclass);
            status=itemView.findViewById(R.id.textStatus);

            edit=itemView.findViewById(R.id.edit);
            remove=itemView.findViewById(R.id.delete);

        }
    }

    public void filterList(ArrayList<BasePackagesModel> filteredNames) {
        this.basePackagesModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

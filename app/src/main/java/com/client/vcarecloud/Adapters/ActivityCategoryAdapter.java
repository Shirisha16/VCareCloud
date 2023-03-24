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
import com.client.vcarecloud.UpdateActivityCategory;
import com.client.vcarecloud.models.ActivityCategoryModel;
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

public class ActivityCategoryAdapter extends RecyclerView.Adapter<ActivityCategoryAdapter.MyViewHolder> {

    ArrayList<ActivityCategoryModel> activityCategoryModelArrayList;
    Context context;
    LoadDetails loadDetails;
    RelativeLayout progressRelative;
    String message,error;

    public ActivityCategoryAdapter(ArrayList<ActivityCategoryModel> activityCategoryModelArrayList, Context context, LoadDetails loadDetails) {
        this.activityCategoryModelArrayList = activityCategoryModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public ActivityCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_activity_category,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityCategoryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.activityCategory.setText(activityCategoryModelArrayList.get(position).getLookupName());

        ActivityCategoryModel activityCategoryModel=activityCategoryModelArrayList.get(position);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=activityCategoryModel.getLookupId();
                String custId=activityCategoryModel.getCustId();
                String lookup_type=activityCategoryModel.getLookupType();
                String lookup_name=activityCategoryModel.getLookupName();

                Intent intent=new Intent(context, UpdateActivityCategory.class);
                intent.putExtra("id",id);
                intent.putExtra("custId",custId);
                intent.putExtra("lookupType",lookup_type);
                intent.putExtra("lookupName",lookup_name);
                context.startActivity(intent);
            }
        });

//        holder.removeButton.setOnClickListener(new View.OnClickListener() {
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
//                Call<String> call= vcareApi.delete_activity(activityCategoryModel.getLookupId(),activityCategoryModel.getCustId());
//
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null){
//                            try {
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                message=jsonObject.getString("message");
//                                error=jsonObject.getString("errorMessage");
//                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//
//                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                                    builder1.setMessage("Do you want to delete?");
//                                    builder1.setCancelable(false);
//                                    builder1.setPositiveButton(
//                                            "Yes",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    Utils.showAlertDialog(context,message,false);
//                                                    activityCategoryModelArrayList.remove(position);
//                                                    notifyItemRemoved(position);
////                                                    dialog.dismiss();
//                                                }
//                                            });
//                                    builder1.setNegativeButton(
//                                            "No",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    dialog.dismiss();
//                                                    dialog.cancel();
//                                                }
//                                            });
//
//                                    AlertDialog alert11 = builder1.create();
//                                    alert11.show();
//
//                                } else {
//                                    Utils.showAlertDialog(context,error,false);
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
////                        else{
////                            progressRelative.setVisibility(View.GONE);
////                            Utils.showAlertDialog(context,error,false);
////                        }
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
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
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
                        Call<String> call=vcareApi.delete_activity(activityCategoryModel.getLookupId(),"0");
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
                                            activityCategoryModelArrayList.remove(position);
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
        return activityCategoryModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView activityCategory;
        AppCompatButton editButton,removeButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            activityCategory=itemView.findViewById(R.id.textActivity);
            editButton=itemView.findViewById(R.id.edit);
            removeButton=itemView.findViewById(R.id.delete);
        }
    }

    public void filterList(ArrayList<ActivityCategoryModel> filteredNames) {
        this.activityCategoryModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}


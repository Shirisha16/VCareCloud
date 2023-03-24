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
import com.client.vcarecloud.UpdateShifts;
import com.client.vcarecloud.models.ShiftsModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONObject;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ShiftsAdapter extends RecyclerView.Adapter<ShiftsAdapter.MyViewHolder>{
    ArrayList<ShiftsModel> shiftsModelArrayList;
    Context context;
    RelativeLayout progress;
    UserDetails userDetails;
    LoadDetails loadDetails;
    String message,error;

    public ShiftsAdapter(ArrayList<ShiftsModel> shiftsModelArrayList, Context context, LoadDetails loadDetails) {
        this.shiftsModelArrayList = shiftsModelArrayList;
        this.context = context;
        this.loadDetails=loadDetails;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_shifts_list,parent,false);
       MyViewHolder myViewHolder=new MyViewHolder(view);
        progress=view.findViewById(R.id.progressLayout);
        userDetails=new UserDetails(context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.shiftName.setText(shiftsModelArrayList.get(position).getShift());

        holder.shiftStartTime.setText(convertTime(shiftsModelArrayList.get(position).getTimeStart()));
        holder.shiftEndTime.setText(convertTime(shiftsModelArrayList.get(position).getEndTime()));

        ShiftsModel shiftModel= shiftsModelArrayList.get(position);

//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progress.setVisibility(View.VISIBLE);
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
////                holder.delete.setVisibility(View.INVISIBLE);
//                Call<String> call=api.delete_shifts(shiftId,"0");
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        progress.setVisibility(View.GONE);
//                        if (response.body()!=null){
//                            try {
//
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                message=jsonObject.getString("message");
//                                error=jsonObject.getString("errorMessage");
//                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//                                    Utils.showAlertDialog(context,message,false);
//                                    shiftsModelArrayList.remove(position);
//                                    notifyItemRemoved(position);
//                                } else {
//                                    Utils.showAlertDialog(context,error,false);
////                                    holder.delete.setVisibility(View.VISIBLE);
//                                }
////                               message=jsonObject.getString("message");
////                                error=jsonObject.getString("errorMessage");
////
////                                    Utils.showAlertDialog(context,message,false);
////                                    shiftsModelArrayList.remove(position);
////                                    notifyItemRemoved(position);
////                                }
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        progress.setVisibility(View.GONE);
//                        String message = "";
//                        if (t instanceof UnknownHostException) {
//                            message = "No internet connection!";
//                        } else {
//                            message = "Something went wrong! try again";
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
                        Call<String> call=vcareApi.delete_shifts(shiftModel.getShift_id(),"0");
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
                                            shiftsModelArrayList.remove(position);
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
                                progress.setVisibility(View.GONE);
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

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=shiftModel.getId();
                String empId=shiftModel.getEmpId();
                String custId=shiftModel.getCust_id();
                String shiftId=shiftModel.getShift_id();
                String shiftName=shiftModel.getShift();
                String starttime=shiftModel.getTimeStart();
                String endtime=shiftModel.getEndTime();
                String duration=shiftModel.getShift_duration();
                String shiftStatus=shiftModel.getShift_status();
                String createdOn=shiftModel.getCreated_on();
                String createdBy=shiftModel.getCreated_by();
                String lastChangedon=shiftModel.getLast_changedOn();
                String lastChangedby=shiftModel.getLast_changedBy();
                String status=shiftModel.getStatus();
                String customer=shiftModel.getCustomer();

                Intent intent=new Intent(context, UpdateShifts.class);
                intent.putExtra("id",id);
                intent.putExtra("shiftId",shiftId);
                intent.putExtra("EmpId",empId);
                intent.putExtra("custId",custId);
                intent.putExtra("shiftName",shiftName);
                intent.putExtra("startTime",starttime);
                intent.putExtra("endTime",endtime);
                intent.putExtra("shiftDuration",duration);
                intent.putExtra("shiftStatus",shiftStatus);
                intent.putExtra("createdBy",createdBy);
                intent.putExtra("createdOn",createdOn);
                intent.putExtra("lastChangedBy",lastChangedby);
                intent.putExtra("lastChangedOn",lastChangedon);
                intent.putExtra("status",status);
                intent.putExtra("customer",customer);
//               Toast.makeText(context, ""+shiftId+empId+custId+shiftName+starttime+endtime+duration+shiftStatus+createdBy+createdOn+lastChangedby+lastChangedon+status+customer, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shiftsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shiftName,shiftStartTime,shiftEndTime;
        AppCompatButton edit,delete;
        MaterialCardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftName=itemView.findViewById(R.id.shiftName1);
            shiftStartTime=itemView.findViewById(R.id.shiftStartTime1);
            shiftEndTime=itemView.findViewById(R.id.shiftEndTime1);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    private String convertTime(String time) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format1 = new SimpleDateFormat("hh:mm aa");
        Date date = new Date();

        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String convertedDate = format1.format(date);
        return convertedDate;
    }

    public void filterList(ArrayList<ShiftsModel> filterdNames) {
        this.shiftsModelArrayList = filterdNames;
        notifyDataSetChanged();
    }
}

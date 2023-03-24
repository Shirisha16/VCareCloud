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
import com.client.vcarecloud.UpdateAbsentList;
import com.client.vcarecloud.models.ChildAbsentModel;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.card.MaterialCardView;

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

public class ChildrenAbsentAdapter extends RecyclerView.Adapter<ChildrenAbsentAdapter.MyViewHolder>{

    ArrayList<ChildAbsentModel> childAbsentModelArrayList;

    Context context;
    RelativeLayout progress;
    LoadDetails loadDetails;

    public ChildrenAbsentAdapter(ArrayList<ChildAbsentModel> childAbsentModelArrayList, Context context,LoadDetails loadDetails) {
        this.childAbsentModelArrayList = childAbsentModelArrayList;
        this.context = context;
        this.loadDetails=loadDetails;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_children_absentlist,parent,false);
       MyViewHolder myViewHolder=new MyViewHolder(view);
        progress=view.findViewById(R.id.progressLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.child.setText(childAbsentModelArrayList.get(position).getChildName());
        holder.class1.setText(childAbsentModelArrayList.get(position).getClassName());

        String fromDate1=childAbsentModelArrayList.get(position).getAbsentFrom();
        if (fromDate1!=null){
            fromDate1=fromDate1.replace("T00:00:00","");
        }
        holder.fromDate.setText(fromDate1);

//        if (fromDate1.contains("0000-00-00")){
//
//        }

        String toDate1=childAbsentModelArrayList.get(position).getAbsentTo();
        if (toDate1!=null){
            toDate1=toDate1.replace("T00:00:00","");
        }
        holder.toDate.setText(toDate1);

        holder.absentType.setText(childAbsentModelArrayList.get(position).getAbsentType());
        String id=childAbsentModelArrayList.get(position).getAbsentId();
        String empid=childAbsentModelArrayList.get(position).getChildId();

        ChildAbsentModel updateAbsentModel=childAbsentModelArrayList.get(position);

//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Utils.showProgressDialog(context);
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
////                holder.delete.setVisibility(View.INVISIBLE);
//                VcareApi api=retrofit.create(VcareApi.class);
//                Call<String> call=api.delete_absentList(updateAbsentModel.getAbsentId(),updateAbsentModel.getChildId());
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
////                        Toast.makeText(context, "yes"+response, Toast.LENGTH_SHORT).show();
//
//                        Utils.dismissProgressDialog();
//                        if (response.body()!=null){
//                            try {
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                String message=jsonObject.getString("message");
//                                Utils.showAlertDialog(context,message,false);
//                                childAbsentModelArrayList.remove(position);
//                                notifyItemRemoved(position);
//                                notifyDataSetChanged();
//                                loadDetails.onMethodCallback();
////                        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
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
                        Call<String> call=vcareApi.delete_absentList(updateAbsentModel.getAbsentId(),"0");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body()!=null){
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body());
                                        String message=jsonObject.getString("message");
                                        if (message.equalsIgnoreCase("Record Deleted Successfully")) {
                                            Utils.showAlertDialog(context, message, false);
                                            childAbsentModelArrayList.remove(position);
                                            notifyItemRemoved(position);
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
                String id=updateAbsentModel.getAbsentId();
                String childId=updateAbsentModel.getChildId();
                String classId=updateAbsentModel.getClassId();
                String custId=updateAbsentModel.getCustId();
                String fromDate=updateAbsentModel.getAbsentFrom();
                String toDate=updateAbsentModel.getAbsentTo();
                String absentType=updateAbsentModel.getAbsentType();
                String absentNotes=updateAbsentModel.getAbsentNotes();
                String createdBy=updateAbsentModel.getCreatedBy();
                String createdOn=updateAbsentModel.getCreatedOn();
                String lastChangedBy=updateAbsentModel.getLastChangedBy();
                String lastChangedOn=updateAbsentModel.getLastChangedOn();
                String status=updateAbsentModel.getStatus();
                String child_name= updateAbsentModel.getChildName();
//                String class_name=updateAbsentModel.getClassName();

                Intent intent=new Intent(context, UpdateAbsentList.class);
                intent.putExtra("id",id);
                intent.putExtra("ChildId",childId);
                intent.putExtra("ClassId",childAbsentModelArrayList.get(position).getClassId());
                intent.putExtra("CustId",custId);
                intent.putExtra("child_name",child_name);
                intent.putExtra("class_name",childAbsentModelArrayList.get(position).getClassName());
                intent.putExtra("AbsentFrom",fromDate);
                intent.putExtra("AbsentTo",toDate);
                intent.putExtra("AbsentType",absentType);
                intent.putExtra("absentNotes",absentNotes);
                intent.putExtra("CreatedBy",createdBy);
                intent.putExtra("CreatedOn",createdOn);
                intent.putExtra("LastChangedBy",lastChangedBy);
                intent.putExtra("LastChangedOn",lastChangedOn);
                intent.putExtra("Status",status);
//                Toast.makeText(context, ""+absentType, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);

//                Toast.makeText(context, ""+class_name+classId+absentType, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return childAbsentModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView child,class1,fromDate,toDate,absentType;
        AppCompatButton edit,delete;
        MaterialCardView cardView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            child=itemView.findViewById(R.id.child1);
            class1=itemView.findViewById(R.id.class1);
            fromDate=itemView.findViewById(R.id.fromDate1);
            toDate=itemView.findViewById(R.id.toDate1);
            absentType=itemView.findViewById(R.id.absentType1);
            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }

    public void filterList(ArrayList<ChildAbsentModel> filterdNames) {
        this.childAbsentModelArrayList = filterdNames;
        notifyDataSetChanged();
    }
}

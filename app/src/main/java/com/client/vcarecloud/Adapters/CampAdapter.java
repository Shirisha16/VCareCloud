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
import com.client.vcarecloud.UpdateCamps;
import com.client.vcarecloud.models.CampModel;
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

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.MyViewHolder>{

    ArrayList<CampModel> campModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public CampAdapter(ArrayList<CampModel> campModelArrayList, Context context, LoadDetails loadDetails) {
        this.campModelArrayList = campModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public CampAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.camps_design,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progress);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CampAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.camp.setText(campModelArrayList.get(position).getCampName());
//        holder.startDate.setText(campModelArrayList.get(position).getCampStartDate());

        String fromDate1=campModelArrayList.get(position).getCampStartDate();
        if (fromDate1!=null){
            fromDate1=fromDate1.replace("T00:00:00","");
        }

        holder.startDate.setText(fromDate1);

        String toDate1=campModelArrayList.get(position).getCampEndDate();
        if (toDate1!=null){
            toDate1=toDate1.replace("T00:00:00","");
        }
        holder.endDate.setText(toDate1);

//        holder.endDate.setText(campModelArrayList.get(position).getCampEndDate());
        holder.childClass.setText(campModelArrayList.get(position).getClassName());

        CampModel campModel=campModelArrayList.get(position);

        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=campModel.getCampId();
                String custid=campModel.getCustId();
                String campname=campModel.getCampName();
                String campDetails=campModel.getCampDetails();
                String className=campModel.getClassName();
                String fromDate=campModel.getCampStartDate();
                String toDate=campModel.getCampEndDate();
                String classId=campModel.getClassId();
                String campCharges=campModel.getCampCharge();
                String taxid=campModel.getTaxesId();
                String taxName=campModel.getTaxname();

                Intent intent=new Intent(context, UpdateCamps.class);
                intent.putExtra("campId",id);
                intent.putExtra("custId",custid);
                intent.putExtra("campName",campname);
                intent.putExtra("campDetails",campDetails);
                intent.putExtra("className",className);
                intent.putExtra("campStartDate",fromDate);
                intent.putExtra("campEndDate",toDate);
                intent.putExtra("classId",classId);
                intent.putExtra("campCharge",campCharges);
                intent.putExtra("taxesId",taxid);
                intent.putExtra("taxName",taxName);
                intent.putExtra("list",campModel);

                context.startActivity(intent);

            }
        });

//      holder.deleteButton.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                      .callTimeout(2, TimeUnit.MINUTES)
//                      .connectTimeout(90,TimeUnit.SECONDS)
//                      .readTimeout(30,TimeUnit.SECONDS)
//                      .writeTimeout(30,TimeUnit.SECONDS);
//
//              Retrofit retrofit=new Retrofit.Builder()
//                      .baseUrl(VcareApi.JSONURL)
//                      .addConverterFactory(ScalarsConverterFactory.create())
//                      .client(httpClient.build())
//                      .build();
//
//              VcareApi vcareApi=retrofit.create(VcareApi.class);
//              Call<String> call=vcareApi.delete_camp(campModel.getCampId(),"0");
//              call.enqueue(new Callback<String>() {
//                  @Override
//                  public void onResponse(Call<String> call, Response<String> response) {
//                      if (response.body()!=null){
//                          try {
//                              JSONObject jsonObject=new JSONObject(response.body());
//                              String message=jsonObject.getString("message");
//                              String  error=jsonObject.getString("errorMessage");
//                              if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")) {
//                                  Utils.showAlertDialog(context,message,false);
//                                  campModelArrayList.remove(position);
//                                  notifyItemRemoved(position);
//                              } else {
//                                  Utils.showAlertDialog(context,error,false);
//                              }
//                              } catch (Exception e) {
//                              e.printStackTrace();
//                          }
//                      }
//                  }
//
//                  @Override
//                  public void onFailure(Call<String> call, Throwable t) {
//
//                  }
//              });
//          }
//      });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
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
                        Call<String> call=vcareApi.delete_camp(campModel.getCampId(),"0");
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
                                            campModelArrayList.remove(position);
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
        return campModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView camp,startDate,endDate,childClass;
        AppCompatButton deleteButton,updateButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            camp=itemView.findViewById(R.id.camps);
            startDate=itemView.findViewById(R.id.startDate);
            endDate=itemView.findViewById(R.id.endDate);
            childClass=itemView.findViewById(R.id.className);

            deleteButton=itemView.findViewById(R.id.delete);
            updateButton=itemView.findViewById(R.id.edit);
        }
    }

    public void filterList(ArrayList<CampModel> filterdNames) {
        this.campModelArrayList = filterdNames;
        notifyDataSetChanged();
    }
}

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
import com.client.vcarecloud.UpdateAllergy;
import com.client.vcarecloud.models.LookupTypeModel;
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

public class LookupTypeAdapter extends RecyclerView.Adapter<LookupTypeAdapter.MyViewHolder> {
    ArrayList<LookupTypeModel> allergyModelArrayList;
    Context context;
    LoadDetails loadDetails;
    RelativeLayout progressRelative;

    public LookupTypeAdapter(ArrayList<LookupTypeModel> allergyModelArrayList, Context context, LoadDetails loadDetails) {
        this.allergyModelArrayList = allergyModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public LookupTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_allergy,parent,false);
      MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.allergy.setText(allergyModelArrayList.get(position).getLookupName());

        LookupTypeModel allergyModel=allergyModelArrayList.get(position);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=allergyModel.getLookupsId();
                String custId=allergyModel.getCustId();
                String lookuptype=allergyModel.getLookupType();
                String lookupName=allergyModel.getLookupName();

                Intent intent = new Intent(context, UpdateAllergy.class);
                    intent.putExtra("Id", id);
                    intent.putExtra("custId", custId);
                    intent.putExtra("lookupType", lookuptype);
                    intent.putExtra("lookupName", lookupName);
                    context.startActivity(intent);

//                if(position==0) {
//                    Intent intent = new Intent(context, UpdateAllergy.class);
//                    intent.putExtra("Id", id);
//                    intent.putExtra("custId", custId);
//                    intent.putExtra("lookupType", lookuptype);
//                    intent.putExtra("lookupName", lookupName);
//                    context.startActivity(intent);
//                }
//                else if(position==1) {
//                    Intent intent = new Intent(context, UpdateEmpDesignation.class);
//                    intent.putExtra("Id", id);
//                    intent.putExtra("custId", custId);
//                    intent.putExtra("lookupType", lookuptype);
//                    intent.putExtra("lookupName", lookupName);
//                    context.startActivity(intent);
//                }
//
//                else if(position==2) {
//                    Intent intent = new Intent(context, UpdateEventType.class);
//                    intent.putExtra("Id", id);
//                    intent.putExtra("custId", custId);
//                    intent.putExtra("lookupType", lookuptype);
//                    intent.putExtra("lookupName", lookupName);
//                    context.startActivity(intent);
//                }
//
//                else if(position==3) {
//                    Intent intent = new Intent(context, UpdateEyeColor.class);
//                    intent.putExtra("Id", id);
//                    intent.putExtra("custId", custId);
//                    intent.putExtra("lookupType", lookuptype);
//                    intent.putExtra("lookupName", lookupName);
//                    context.startActivity(intent);
//                }
//                else if(position==4) {
//                    Intent intent = new Intent(context, UpdateHairstyle.class);
//                    intent.putExtra("Id", id);
//                    intent.putExtra("custId", custId);
//                    intent.putExtra("lookupType", lookuptype);
//                    intent.putExtra("lookupName", lookupName);
//                    context.startActivity(intent);
//                }
//                switch (position) {
//                    case 0:
//                        Intent intent = new Intent(context, UpdateAllergy.class);
//                        intent.putExtra("Id", id);
//                        intent.putExtra("custId", custId);
//                        intent.putExtra("lookupType", lookuptype);
//                        intent.putExtra("lookupName", lookupName);
//                        context.startActivity(intent);
//                        break;
//
//                    case 1:
//                        Intent intent1 = new Intent(context, UpdateEmpDesignation.class);
//                        intent1.putExtra("Id", id);
//                        intent1.putExtra("custId", custId);
//                        intent1.putExtra("lookupType", lookuptype);
//                        intent1.putExtra("lookupName", lookupName);
//                    context.startActivity(intent1);
//                    break;
//
//                    case 2:
//                        Intent intent2 = new Intent(context, UpdateEventType.class);
//                        intent2.putExtra("Id", id);
//                        intent2.putExtra("custId", custId);
//                        intent2.putExtra("lookupType", lookuptype);
//                        intent2.putExtra("lookupName", lookupName);
//                        context.startActivity(intent2);
//                        break;
//
//                    case 3:
//                        Intent intent3 = new Intent(context, UpdateEyeColor.class);
//                        intent3.putExtra("Id", id);
//                        intent3.putExtra("custId", custId);
//                        intent3.putExtra("lookupType", lookuptype);
//                        intent3.putExtra("lookupName", lookupName);
//                        context.startActivity(intent3);
//                        break;
//                }
            }

        });

//        holder.removeButton.setOnClickListener(new View.OnClickListener() {
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
//                    Call<String> call=vcareApi.deleteLookup(allergyModel.getLookupsId(),allergyModel.getCustId());
//                    call.enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            if (response.body()!=null){
//                                try {
//
//                                    JSONObject jsonObject=new JSONObject(response.body());
//                                    message=jsonObject.getString("message");
//                                    error=jsonObject.getString("errorMessage");
//                                    if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//                                        Utils.showAlertDialog(context,message,false);
//                                        allergyModelArrayList.remove(position);
//                                        notifyItemRemoved(position);
//                                    } else {
//                                        Utils.showAlertDialog(context,error,false);
////                                        holder.delete.setVisibility(View.VISIBLE);
//                                    }
////                                    JSONObject jsonObject=new JSONObject(response.body());
////                                    String message=jsonObject.getString("message");
////                                    Utils.showAlertDialog(context,message,false);
////                                    allergyModelArrayList.remove(position);
////                                    notifyItemRemoved(position);
////                                    notifyDataSetChanged();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
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
                        Call<String> call=vcareApi.deleteLookup(allergyModel.getLookupsId(),"0");
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
                                            allergyModelArrayList.remove(position);
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
        return allergyModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView allergy;
        AppCompatButton editButton,removeButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            allergy=itemView.findViewById(R.id.textAllergy);
            editButton=itemView.findViewById(R.id.edit);
            removeButton=itemView.findViewById(R.id.delete);
        }
    }

    public void filterList(ArrayList<LookupTypeModel> filteredNames) {
        this.allergyModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

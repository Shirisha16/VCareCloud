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
import com.client.vcarecloud.UpdateReligion;
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

public class ReligionAdapter extends RecyclerView.Adapter<ReligionAdapter.MyViewHolder>{
    ArrayList<LookupTypeModel> lookupTypeModelArrayList;
    Context context;
    LoadDetails loadDetails;
    RelativeLayout progressRelative;

    public ReligionAdapter(ArrayList<LookupTypeModel> lookupTypeModelArrayList, Context context, LoadDetails loadDetails) {
        this.lookupTypeModelArrayList = lookupTypeModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public ReligionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_allergy,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReligionAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.allergy.setText(lookupTypeModelArrayList.get(position).getLookupName());

        LookupTypeModel lookupTypeModel=lookupTypeModelArrayList.get(position);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=lookupTypeModel.getLookupsId();
                String custId=lookupTypeModel.getCustId();
                String lookuptype=lookupTypeModel.getLookupType();
                String lookupName=lookupTypeModel.getLookupName();

                Intent intent = new Intent(context, UpdateReligion.class);
                intent.putExtra("Id", id);
                intent.putExtra("custId", custId);
                intent.putExtra("lookupType", lookuptype);
                intent.putExtra("lookupName", lookupName);
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
//                Call<String> call=vcareApi.deleteLookup(lookupTypeModel.getLookupsId(),lookupTypeModel.getCustId());
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null){
//                            try {
////                                JSONObject jsonObject=new JSONObject(response.body());
////                                String message=jsonObject.getString("message");
////                                Utils.showAlertDialog(context,message,false);
////                                lookupTypeModelArrayList.remove(position);
////                                notifyItemRemoved(position);
////                                notifyDataSetChanged();
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                String message=jsonObject.getString("message");
//                                String  error=jsonObject.getString("errorMessage");
//                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//                                    Utils.showAlertDialog(context,message,false);
//                                    lookupTypeModelArrayList.remove(position);
//                                    notifyItemRemoved(position);
//                                } else {
//                                    Utils.showAlertDialog(context,error,false);
//                                }
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
                        Call<String> call=vcareApi.deleteLookup(lookupTypeModel.getLookupsId(),lookupTypeModel.getCustId());
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
                                            lookupTypeModelArrayList.remove(position);
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
        return lookupTypeModelArrayList.size();
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
        this.lookupTypeModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

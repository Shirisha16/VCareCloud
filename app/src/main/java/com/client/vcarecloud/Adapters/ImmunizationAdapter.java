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
import com.client.vcarecloud.UpdateImmunization;
import com.client.vcarecloud.models.ImmunizationModel;
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

public class ImmunizationAdapter extends RecyclerView.Adapter<ImmunizationAdapter.MyViewHolder>{

    ArrayList<ImmunizationModel> immunizationModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public ImmunizationAdapter(ArrayList<ImmunizationModel> immunizationModelArrayList, Context context, LoadDetails loadDetails) {
        this.immunizationModelArrayList = immunizationModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public ImmunizationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_immunization,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImmunizationAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImmunizationModel immunizationModel=immunizationModelArrayList.get(position);

        holder.immunizationName.setText(immunizationModelArrayList.get(position).getImmunizationName());
        holder.code.setText(immunizationModelArrayList.get(position).getImmunizationCode());

        String opt=immunizationModelArrayList.get(position).getIsOptional();
        if(opt.equalsIgnoreCase("N")){
            holder.optional.setText("No");
        }else {
            holder.optional.setText("Yes");
        }

//        String dose1=immunizationModel.getDose1();
//        String[] parts = dose1.split(" ");
//        String dose1a = parts[0];
//        if(dose1!=null) {
//
//        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=immunizationModel.getImmunizationId();
                String custid=immunizationModel.getCustId();
                String name=immunizationModel.getImmunizationName();
                String code=immunizationModel.getImmunizationCode();
                String dose1a= immunizationModel.getDose1();
                String dose1b=immunizationModel.getDose1();
                String dose1c=immunizationModel.getDose1();
                String dose1d=immunizationModel.getDose1();
                String dose2a=immunizationModel.getDose2();
                String dose2b=immunizationModel.getDose2();
                String dose2c=immunizationModel.getDose2();
                String dose2d=immunizationModel.getDose2();
                String dose3a=immunizationModel.getDose3();
                String dose3b=immunizationModel.getDose3();
                String dose3c=immunizationModel.getDose3();
                String dose3d=immunizationModel.getDose3();
                String dose4a=immunizationModel.getDose4();
                String dose4b=immunizationModel.getDose4();
                String dose4c=immunizationModel.getDose4();
                String dose4d=immunizationModel.getDose4();
                String dose5a=immunizationModel.getDose5();
                String dose5b=immunizationModel.getDose5();
                String dose5c=immunizationModel.getDose5();
                String dose5d=immunizationModel.getDose5();
                String dose6a=immunizationModel.getDose6();
                String dose6b=immunizationModel.getDose6();
                String dose6c=immunizationModel.getDose6();
                String dose6d=immunizationModel.getDose6();
                String optional=immunizationModel.getIsOptional();

                Intent intent=new Intent(context, UpdateImmunization.class);
                intent.putExtra("id",id);
                intent.putExtra("custId",custid);
                intent.putExtra("immunizationName",name);
                intent.putExtra("immunizationCode",code);
                intent.putExtra("dose1a",dose1a);
                intent.putExtra("dose1b",dose1b);
                intent.putExtra("dose1c",dose1c);
                intent.putExtra("dose1d",dose1d);
                intent.putExtra("dose2a",dose2a);
                intent.putExtra("dose2b",dose2b);
                intent.putExtra("dose2c",dose2c);
                intent.putExtra("dose2d",dose2d);
                intent.putExtra("dose3a",dose3a);
                intent.putExtra("dose3b",dose3b);
                intent.putExtra("dose3c",dose3c);
                intent.putExtra("dose3d",dose3d);
                intent.putExtra("dose4a",dose4a);
                intent.putExtra("dose4b",dose4b);
                intent.putExtra("dose4c",dose4c);
                intent.putExtra("dose4d",dose4d);
                intent.putExtra("dose5a",dose5a);
                intent.putExtra("dose5b",dose5b);
                intent.putExtra("dose5c",dose5c);
                intent.putExtra("dose5d",dose5d);
                intent.putExtra("dose6a",dose6a);
                intent.putExtra("dose6b",dose6b);
                intent.putExtra("dose6c",dose6c);
                intent.putExtra("dose6d",dose6d);
                intent.putExtra("isOptional",optional);
                intent.putExtra("list",immunizationModel);
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
//                    Call<String> call=vcareApi.delete_immunization(immunizationModel.getImmunizationId(),"0");
//                    call.enqueue(new Callback<String>() {
//                        @Override
//                        public void onResponse(Call<String> call, Response<String> response) {
//                            if (response.body()!=null){
//                                try {
//                                    JSONObject jsonObject=new JSONObject(response.body());
//                                    String message=jsonObject.getString("message");
//                                    Utils.showAlertDialog(context,message,false);
//                                    immunizationModelArrayList.remove(position);
//                                    notifyItemRemoved(position);
//                                    notifyDataSetChanged();
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
                        Call<String> call=vcareApi.delete_immunization(immunizationModel.getImmunizationId(),"0");
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
                                            immunizationModelArrayList.remove(position);
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
            }
        });
    }

    @Override
    public int getItemCount() {
        return immunizationModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView immunizationName,code,optional;
        AppCompatButton view,edit,remove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            immunizationName=itemView.findViewById(R.id.textImmunization);
            code=itemView.findViewById(R.id.textCode);
            optional=itemView.findViewById(R.id.textOptional);

            view=itemView.findViewById(R.id.view);
            edit=itemView.findViewById(R.id.edit);
            remove=itemView.findViewById(R.id.delete);
        }
    }

    public void filterList(ArrayList<ImmunizationModel> filteredNames) {
        this.immunizationModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

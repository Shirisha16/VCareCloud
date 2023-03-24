package com.client.vcarecloud.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

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

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.R;
import com.client.vcarecloud.UpdateMealMenu;
import com.client.vcarecloud.models.MealMenuModel;
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

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    ArrayList<MealMenuModel> mealMenuModels;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public MenuAdapter(ArrayList<MealMenuModel> mealMenuModels, Context context, LoadDetails loadDetails) {
        this.mealMenuModels = mealMenuModels;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public MenuAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_menu_adapter,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progressLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.menu.setText(mealMenuModels.get(position).getLookupName());
        holder.mealtype.setText(mealMenuModels.get(position).getLookupCategory());

        MealMenuModel mealMenuModel=mealMenuModels.get(position);

        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=mealMenuModel.getLookupsId();
                String empid=mealMenuModel.getEmpid();
                String custId=mealMenuModel.getCustId();
                String lookupId=mealMenuModel.getLookupsId();
                String lookupName=mealMenuModel.getLookupName();
                String lookuptype=mealMenuModel.getLookupType();
                String lookupCategory=mealMenuModel.getLookupCategory();

                Intent intent = new Intent(context, UpdateMealMenu.class);
                intent.putExtra("id", id);
                intent.putExtra("EmpId", empid);
                intent.putExtra("custId", custId);
                intent.putExtra("lookupsId", lookupId);
                intent.putExtra("lookupName", lookupName);
                intent.putExtra("lookupType", lookuptype);
                intent.putExtra("lookupCategory", lookupCategory);

                context.startActivity(intent);
            }
        });

//        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
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
//                Call<String> call=vcareApi.deleteMenu(mealMenuModel.getLookupsId(),mealMenuModel.getCustId());
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null){
//                            try {
////                                JSONObject jsonObject=new JSONObject(response.body());
////                                String message=jsonObject.getString("message");
////                                Utils.showAlertDialog(context,message,false);
////                                mealMenuModels.remove(position);
////                                notifyItemRemoved(position);
////                                notifyDataSetChanged();
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                message=jsonObject.getString("message");
//                                error=jsonObject.getString("errorMessage");
//                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//
//                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                                    builder1.setMessage("Do you want to delete?");
////                                    builder1.setCancelable(true);
//
//                                    builder1.setPositiveButton(
//                                            "Yes",
//                                            new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    Utils.showAlertDialog(context, message, false);
//                                                    mealMenuModels.remove(position);
//                                                    notifyItemRemoved(position);
//                                                }
//                                            });
//                                    builder1.setNegativeButton(
//                                            "No",null);
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
//                                    builder1.setCancelable(true);

                builder1.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                VcareApi vcareApi=retrofit.create(VcareApi.class);
                Call<String> call=vcareApi.deleteMenu(mealMenuModel.getLookupsId(),"0");
                call.enqueue(new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body()!=null){
                            try {
                                JSONObject jsonObject=new JSONObject(response.body());
                                String message=jsonObject.getString("message");
                                String error=jsonObject.getString("errorMessage");
                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")) {
                                    Utils.showAlertDialog(context, message, false);
                                    mealMenuModels.remove(position);
                                    notifyItemRemoved(position);
                                }else{
                                    Utils.showAlertDialog(context,error,false);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
//                        notifyDataSetChanged();
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
                } );
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

//                holder.deleteButton.setEnabled(false);
                Utils.preventTwoClick(v);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mealMenuModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView menu,mealtype;
        AppCompatButton deleteButton,updateButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            menu=itemView.findViewById(R.id.menu);
            mealtype=itemView.findViewById(R.id.mealType);
            deleteButton=itemView.findViewById(R.id.delete);
            updateButton=itemView.findViewById(R.id.edit);

        }
    }

    public void filterList(ArrayList<MealMenuModel> filteredNames) {
        this.mealMenuModels = filteredNames;
        notifyDataSetChanged();
    }
}
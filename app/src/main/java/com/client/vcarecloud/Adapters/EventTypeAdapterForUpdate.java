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
import com.client.vcarecloud.UpdateEventTypeForUpdate;
import com.client.vcarecloud.models.EventTypeModel;
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

public class EventTypeAdapterForUpdate extends RecyclerView.Adapter<EventTypeAdapterForUpdate.MyViewHolder>{
    ArrayList<EventTypeModel> eventTypeModelArrayList;
    Context context;
    LoadDetails loadDetails;
    RelativeLayout progressRelative;

    public EventTypeAdapterForUpdate(ArrayList<EventTypeModel> eventTypeModelArrayList, Context context, LoadDetails loadDetails) {
        this.eventTypeModelArrayList = eventTypeModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }


    @NonNull
    @Override
    public EventTypeAdapterForUpdate.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_event_type,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeAdapterForUpdate.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.eventType.setText(eventTypeModelArrayList.get(position).getTypeName());

        EventTypeModel eventTypeModel=eventTypeModelArrayList.get(position);

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=eventTypeModel.getTypeId();
                String typeId=eventTypeModel.getTypeId();
                String custId=eventTypeModel.getCustId();
                String typeName=eventTypeModel.getTypeName();

                Intent intent=new Intent(context, UpdateEventTypeForUpdate.class);
                intent.putExtra("id",id);
                intent.putExtra("typeId",typeId);
                intent.putExtra("custId",custId);
                intent.putExtra("typeName",typeName);
                context.startActivity(intent);
            }
        });

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
                        Call<String> call=vcareApi.delete_eventsType(eventTypeModel.getTypeId(),"0");
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
                                            eventTypeModelArrayList.remove(position);
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
        return eventTypeModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventType;
        AppCompatButton editButton,removeButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eventType=itemView.findViewById(R.id.texteventtype);
            editButton=itemView.findViewById(R.id.edit);
            removeButton=itemView.findViewById(R.id.delete);
    }
}

    public void filterList(ArrayList<EventTypeModel> filteredNames) {
        this.eventTypeModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

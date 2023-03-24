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
import com.client.vcarecloud.UpdateEvent;
import com.client.vcarecloud.models.EventModel;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder>{

    ArrayList<EventModel> eventsModelArrayList;
    Context context;
    LoadDetails loadDetails;
    RelativeLayout progressRelative;

    public EventAdapter(ArrayList<EventModel> eventsModelArrayList, Context context, LoadDetails loadDetails) {
        this.eventsModelArrayList = eventsModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.eventName.setText(eventsModelArrayList.get(position).getEventName());
        holder.eventType.setText(eventsModelArrayList.get(position).getEventsType());
        holder.location.setText(eventsModelArrayList.get(position).getEventLocation());

//        String eventDetails=eventsModelArrayList.get(position).getEventDetails();
//        if (eventDetails.equalsIgnoreCase("null") || eventDetails.equalsIgnoreCase("")) {
//            holder.details.setText("N/A");
//        } else {
//            holder.details.setText(eventDetails);
//        }

        String fromDate1=eventsModelArrayList.get(position).getFromDate();
        if (fromDate1!=null){
            fromDate1=fromDate1.replace("T00:00:00","");
        }
        holder.fromDate.setText(fromDate1);

        String toDate1=eventsModelArrayList.get(position).getToDate();
        if (toDate1!=null){
            toDate1=toDate1.replace("T00:00:00","");
        }
        holder.toDate.setText(toDate1);

        EventModel eventModel=eventsModelArrayList.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventid = eventModel.getEventID();
                String custid = eventModel.getCustId();
                String typeId = eventModel.getTypeId();
                String eventType=eventModel.getEventsType();
                String eventname = eventModel.getEventName();
                String location = eventModel.getEventLocation();
                String details = eventModel.getEventDetails();
                String fromDate = eventModel.getFromDate();
                String toDate = eventModel.getToDate();

                Intent intent = new Intent(context, UpdateEvent.class);
                intent.putExtra("eventID", eventid);
                intent.putExtra("custId", custid);
                intent.putExtra("typeId", typeId);
                intent.putExtra("eventtype",eventType);
                intent.putExtra("eventName", eventname);
                intent.putExtra("eventLocation", location);
                intent.putExtra("eventDetails", details);
                intent.putExtra("fromDate", fromDate);
                intent.putExtra("toDate", toDate);
                intent.putExtra("list",eventModel);

                context.startActivity(intent);

//                Toast.makeText(context, ""+typeId+eventType, Toast.LENGTH_SHORT).show();
            }
        });

//        holder.remove.setOnClickListener(new View.OnClickListener() {
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
//                Call<String> call=vcareApi.delete_events(eventModel.getEventID(),"0");
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.body()!=null){
//                            try {
////                                JSONObject jsonObject=new JSONObject(response.body());
////                                String message=jsonObject.getString("message");
////                                Utils.showAlertDialog(context,message,false);
////                                eventsModelArrayList.remove(position);
////                                notifyItemRemoved(position);
////                                notifyDataSetChanged();
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                String message=jsonObject.getString("message");
//                                String  error=jsonObject.getString("errorMessage");
//                                if (jsonObject.getString("message").equalsIgnoreCase("Deleted Successfully")){
//                                    Utils.showAlertDialog(context,message,false);
//                                    eventsModelArrayList.remove(position);
//                                    notifyItemRemoved(position);
//                                } else {
//                                    Utils.showAlertDialog(context,error,false);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }else{
//                            progressRelative.setVisibility(View.GONE);
//                            Utils.showAlertDialog(context,error,false);
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
                        Call<String> call=vcareApi.delete_events(eventModel.getEventID(),"0");
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
                                            eventsModelArrayList.remove(position);
                                            notifyItemRemoved(position);
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
        return eventsModelArrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventName,eventType,location,details,fromDate,toDate;
        AppCompatButton edit,remove;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            eventName=itemView.findViewById(R.id.event);
            eventType=itemView.findViewById(R.id.eventType);
            location=itemView.findViewById(R.id.location);
//            details=itemView.findViewById(R.id.details);
            fromDate=itemView.findViewById(R.id.startDate);
            toDate=itemView.findViewById(R.id.endDate);

            edit=itemView.findViewById(R.id.edit);
            remove=itemView.findViewById(R.id.delete);
        }
    }
    public void filterList(ArrayList<EventModel> filteredNames) {
        this.eventsModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

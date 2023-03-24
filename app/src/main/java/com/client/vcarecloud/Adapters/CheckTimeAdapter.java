package com.client.vcarecloud.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.R;

import com.client.vcarecloud.models.CheckInModel;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CheckTimeAdapter extends RecyclerView.Adapter<CheckTimeAdapter.MyViewHolder> {
   ArrayList<CheckInModel> modelArrayList;
   Context context;
   Calendar calendar;
   String currentTime;
   Date date=null;

    public CheckTimeAdapter(ArrayList<CheckInModel> modelArrayList, Context context) {
        this.modelArrayList = modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_checkchild_design,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.name.setText(modelArrayList.get(position).getName());
            String time = modelArrayList.get(position).getCheckIn();
            holder.time.setText(convertTime(time));
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView name,time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.eventsCard);
            name=itemView.findViewById(R.id.name1);
            time=itemView.findViewById(R.id.checkTime);

        }
    }
    private String convertTime(String time) {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
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

}

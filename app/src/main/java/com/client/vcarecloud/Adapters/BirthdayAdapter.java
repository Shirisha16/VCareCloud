package com.client.vcarecloud.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.client.vcarecloud.R;
import com.client.vcarecloud.models.upcomingModel;

import java.util.ArrayList;

public class BirthdayAdapter extends RecyclerView.Adapter<BirthdayAdapter.MyViewHolder> {
    ArrayList<upcomingModel> upcomingModelArrayList;
    Context context;
    String dob;

    public BirthdayAdapter(ArrayList<upcomingModel> upcomingModelArrayList, Context context) {
        this.upcomingModelArrayList = upcomingModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_birthdaylist_recyclerview,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(upcomingModelArrayList.get(position).getName());

        dob=upcomingModelArrayList.get(position).getDob();
        if (dob!=null){
            dob=dob.replace("T00:00:00","");

        }
        holder.birthday.setText(dob);
//        else{
//            holder.birthday.setText("NO UPCOMING BIRTHDAYS");
//        }

    }

    @Override
    public int getItemCount() {
        return upcomingModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,birthday;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name1);
            birthday=itemView.findViewById(R.id.date1);
        }
    }
}

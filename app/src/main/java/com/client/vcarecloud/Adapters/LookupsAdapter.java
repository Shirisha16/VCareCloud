package com.client.vcarecloud.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Allergy;
import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.EmpDesignation;
import com.client.vcarecloud.EyeColor;
import com.client.vcarecloud.HairStyle;
import com.client.vcarecloud.HealthInsurance;
import com.client.vcarecloud.EventTypeLookUp;
import com.client.vcarecloud.MedicalCondition;
import com.client.vcarecloud.MedicationRoute;
import com.client.vcarecloud.Occupation;
import com.client.vcarecloud.R;
import com.client.vcarecloud.Relationship;
import com.client.vcarecloud.Religion;
import com.client.vcarecloud.SpecialNeeds;
import com.client.vcarecloud.models.LookupsModel;

import java.util.ArrayList;

public class LookupsAdapter extends RecyclerView.Adapter<LookupsAdapter.MyViewHolder> {

    ArrayList<LookupsModel> lookupsModelArrayList;
    Context context;
    LoadDetails loadDetails;

    public LookupsAdapter(ArrayList<LookupsModel> lookupsModelArrayList, Context context, LoadDetails loadDetails) {
        this.lookupsModelArrayList = lookupsModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public LookupsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_lookups,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull LookupsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LookupsModel model=lookupsModelArrayList.get(position);
        holder.lookup.setText(model.getLookupname());

        holder.actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Allergy")){
//                if(position==0){
                    Intent intent = new Intent(context, Allergy.class);
                    context.startActivity(intent);

                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Employee Designation")){
//                if(position==1){
                    Intent intent = new Intent(context, EmpDesignation.class);
                    context.startActivity(intent);

                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Event Type")){
//                if(position==2){
                    Intent intent = new Intent(context, EventTypeLookUp.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Eye Color")){
//                if(position==3){
                    Intent intent = new Intent(context, EyeColor.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Hair Style")){
//                if(position==4){
                    Intent intent = new Intent(context, HairStyle.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Health Insurance")){
//                if(position==5){
                    Intent intent = new Intent(context, HealthInsurance.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Medical Condition")){
//                if(position==6){
                    Intent intent = new Intent(context, MedicalCondition.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Medication Route")){
//                if(position==7){
                    Intent intent = new Intent(context, MedicationRoute.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Occupation")){
//                if(position==8){
                    Intent intent = new Intent(context, Occupation.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Relationship")){
//                if(position==9){
                    Intent intent = new Intent(context, Relationship.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Religion")){
//                if(position==10){
                    Intent intent = new Intent(context, Religion.class);
                    context.startActivity(intent);
                }
                if(lookupsModelArrayList.get(position).getLookupname().equalsIgnoreCase("Special Needs")){
//                if(position==11){
                    Intent intent = new Intent(context, SpecialNeeds.class);
                    context.startActivity(intent);
                }
        }
    });

    }

    @Override
    public int getItemCount() {
        return lookupsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView lookup;
        AppCompatButton actionButton;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            lookup=itemView.findViewById(R.id.textLook);
            actionButton=itemView.findViewById(R.id.button);
        }
    }

    public void filterList(ArrayList<LookupsModel> filteredNames) {
        this.lookupsModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

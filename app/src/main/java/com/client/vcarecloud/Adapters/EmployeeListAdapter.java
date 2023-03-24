package com.client.vcarecloud.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.EmployeeInfo;
import com.client.vcarecloud.R;
import com.client.vcarecloud.models.EmployeeListModel;
import com.client.vcarecloud.models.UserDetails;

import java.util.ArrayList;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {

    ArrayList<EmployeeListModel> employeeListModelArrayList;
    Context context;
    LoadDetails loadDetails;
    String employeeId,custId,firstname;
    UserDetails userDetails;


    public EmployeeListAdapter(ArrayList<EmployeeListModel> employeeListModelArrayList, Context context, LoadDetails loadDetails) {
        this.employeeListModelArrayList = employeeListModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_employees_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        userDetails=new UserDetails(context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.employeeName.setText(employeeListModelArrayList.get(position).getFirstName());
        holder.contact.setText(employeeListModelArrayList.get(position).getHomePhone());
        holder.designation.setText(employeeListModelArrayList.get(position).getDesignation());
        holder.security.setText(employeeListModelArrayList.get(position).getSecurityProfile());

        EmployeeListModel model = employeeListModelArrayList.get(position);

        employeeId= model.getEmployeeId();
        custId = userDetails.getCustId();
        firstname = model.getFirstName();

        holder.empInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EmployeeInfo.class);
                intent.putExtra("employeeId", model.getEmployeeId());
                intent.putExtra("custId", userDetails.getCustId());
                intent.putExtra("firstName", model.getFirstName());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return employeeListModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName, contact, designation, security;
        AppCompatButton empInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName = itemView.findViewById(R.id.empName1);
            contact = itemView.findViewById(R.id.contact1);
            designation = itemView.findViewById(R.id.designation1);
            security = itemView.findViewById(R.id.security1);
            empInfo=itemView.findViewById(R.id.empInfo);

        }
    }

    public void filterList(ArrayList<EmployeeListModel> filterdNames) {
        this.employeeListModelArrayList = filterdNames;
        notifyDataSetChanged();
    }

}

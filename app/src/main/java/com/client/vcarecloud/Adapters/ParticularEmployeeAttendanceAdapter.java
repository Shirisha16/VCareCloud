package com.client.vcarecloud.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.R;
import com.client.vcarecloud.models.ParticularEmployeeAttendanceModel;
import com.client.vcarecloud.models.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class  ParticularEmployeeAttendanceAdapter extends RecyclerView.Adapter<ParticularEmployeeAttendanceAdapter.MyViewHolder> {

    ArrayList<ParticularEmployeeAttendanceModel> empAttendanceModelArrayList;
    Context context;
    UserDetails userDetails;
    String startDate,startTime,endDate,endTime,dates,empId,custId,employeeId;
    LoadDetails loadDetails;

    public ParticularEmployeeAttendanceAdapter(ArrayList<ParticularEmployeeAttendanceModel> empAttendanceModelArrayList, Context context, LoadDetails loadDetails) {
        this.empAttendanceModelArrayList = empAttendanceModelArrayList;
        this.loadDetails=loadDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public ParticularEmployeeAttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_particular_emp_attendnace,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        userDetails=new UserDetails(context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticularEmployeeAttendanceAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.employeeName.setText(empAttendanceModelArrayList.get(position).getEmployeeName());
        holder.shift.setText(empAttendanceModelArrayList.get(position).getShiftName());
        startDate = empAttendanceModelArrayList.get(position).getDayStart();

        holder.startDate.setText(startDate);

        startTime=empAttendanceModelArrayList.get(position).getShiftStartDate();
        if (startTime!=null){
            startTime=startTime.replace("0001-01-01T00:00:00","");
        }
        holder.startTime.setText(startTime);

        endDate=empAttendanceModelArrayList.get(position).getDayEnd();
        if (endDate!=null){
            endDate=endDate.replace("T00:00:00","");
        }

        holder.endDate.setText(endDate);

        endTime=empAttendanceModelArrayList.get(position).getShiftEndDate();
        holder.endTime.setText(endTime);
//        holder.endTime.setText(convertTime(endTime));

        ParticularEmployeeAttendanceModel model=empAttendanceModelArrayList.get(position);
        empId=model.getEmployeeId();
        custId=model.getCustId();
        employeeId=model.getEmployeeId();
    }

    @Override
    public int getItemCount() {
        return empAttendanceModelArrayList.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName,shift,startDate,endDate,startTime,endTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            employeeName=itemView.findViewById(R.id.empName1);
            shift=itemView.findViewById(R.id.empShift1);
            startDate=itemView.findViewById(R.id.startDate1);
            startTime=itemView.findViewById(R.id.startTime1);
            endDate=itemView.findViewById(R.id.endDate1);
            endTime=itemView.findViewById(R.id.endTime1);
        }
    }

    public void filterList(ArrayList<ParticularEmployeeAttendanceModel> filterdNames) {
        this.empAttendanceModelArrayList = filterdNames;
        notifyDataSetChanged();
    }
}

package com.client.vcarecloud.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.R;
import com.client.vcarecloud.models.EmpAttendanceModel;
import com.client.vcarecloud.models.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmployeeAttendanceAdapter extends RecyclerView.Adapter<EmployeeAttendanceAdapter.MyViewHolder> {

    ArrayList<EmpAttendanceModel> empAttendanceModelArrayList;
    Context context;
    UserDetails userDetails;
    String startDate,startTime,endDate,endTime;
    LoadDetails loadDetails;

    public EmployeeAttendanceAdapter(ArrayList<EmpAttendanceModel> empAttendanceModelArrayList, Context context,LoadDetails loadDetails) {
        this.empAttendanceModelArrayList = empAttendanceModelArrayList;
        this.loadDetails=loadDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeAttendanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        userDetails=new UserDetails(context);
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_emp_attendnace,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAttendanceAdapter.MyViewHolder holder, int position) {
        holder.employeeName.setText(empAttendanceModelArrayList.get(position).getEmployees());
        holder.shift.setText(empAttendanceModelArrayList.get(position).getShifts());
        holder.startDate.setText(empAttendanceModelArrayList.get(position).getDayStart());

        startTime=empAttendanceModelArrayList.get(position).getShiftStartDate();
        if (startTime!=null){
            startTime=startTime.replace("0001-01-01T00:00:00","");
        }
//        holder.startTime.setText(convertTime(startTime));
        holder.startTime.setText(startTime);

        endDate=empAttendanceModelArrayList.get(position).getDayEnd();
        if (endDate!=null){
            endDate=endDate.replace("T00:00:00","");
        }
        holder.endDate.setText(endDate);

        endTime=empAttendanceModelArrayList.get(position).getShiftEndDate();
        if (endTime!=null){
            endTime=endTime.replace("0001-01-01T00:00:00","");
        }

//        holder.endTime.setText(convertTime(endTime));
        holder.endTime.setText(endTime);
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
    public void filterList(ArrayList<EmpAttendanceModel> filterdNames) {
        this.empAttendanceModelArrayList = filterdNames;
        notifyDataSetChanged();
    }

}

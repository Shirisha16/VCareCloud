package com.client.vcarecloud.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.RestService;
import com.client.vcarecloud.Api.ChildCheckIn;
import com.client.vcarecloud.ChildInfo;
import com.client.vcarecloud.R;
import com.client.vcarecloud.models.CheckInRequest;
import com.client.vcarecloud.models.CheckInResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.models.childrenListModel;
import com.client.vcarecloud.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChildrenListAdapter extends RecyclerView.Adapter<ChildrenListAdapter.MyViewHolder> {

    int EmpId = 0,childAttendance=0;
    ArrayList<childrenListModel> childrenListModelArrayList;
    CheckInRequest checkInRequests;
    String message, error, childId, custId, name, dates, errorMessage;
    Context context;
    RelativeLayout progress;
    LoadDetails loadDetails;
//    CheckInResponse checkInResponse;
    RestService restService;
    UserDetails userDetails;
    ChildCheckIn childCheckIn;
    ImageView noData;

    public ChildrenListAdapter(ArrayList<childrenListModel> childrenListModelArrayList, Context context, LoadDetails loadDetails,ChildCheckIn childCheckIn) {
        this.childrenListModelArrayList = childrenListModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
        this.childCheckIn=childCheckIn;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_childrenlist_design, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        progress = view.findViewById(R.id.progressLayout);
        restService = new RestService();

        checkInRequests = new CheckInRequest();
        noData = view.findViewById(R.id.noData_leaveList_admin_approved);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.childName.setText(childrenListModelArrayList.get(position).getChildName());
        holder.className.setText(childrenListModelArrayList.get(position).getClassname());
        String dateOfBirth = childrenListModelArrayList.get(position).getDob();
        if (dateOfBirth != null) {
            dateOfBirth = dateOfBirth.replace("T00:00:00", "");
        }

        holder.dob.setText(dateOfBirth);
        holder.guardianName.setText(childrenListModelArrayList.get(position).getParentName());

        childrenListModel model = childrenListModelArrayList.get(position);

        childId = model.getChildId();
        name = model.getChildName();
        custId = model.getCustId();

        userDetails = new UserDetails(context);

        message=String.valueOf(userDetails.getMessage());
//        errorMessage=userDetails.getErrorMessage();
        CheckInResponse checkInResponse=new CheckInResponse();

        errorMessage=checkInResponse.getErrorMessage();

        dates = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        holder.childInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChildInfo.class);
                intent.putExtra("childId", model.getChildId());
                intent.putExtra("childName", model.getChildName());
                intent.putExtra("custId", model.getCustId());
                context.startActivity(intent);
            }
        });

        holder.checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childCheckIn.childCheckIn(childrenListModelArrayList.get(position).getChildId(),childrenListModelArrayList.get(position).getChildName(),childrenListModelArrayList.get(position).getDates(),"0",childrenListModelArrayList.get(position).getCustId());
//                message=userDetails.getMessage();
//                errorMessage=String.valueOf(userDetails.getErrorMessage());
////                Toast.makeText(context,errorMessage, Toast.LENGTH_SHORT).show();
////                Utils.showProgressDialog(context);
////                if (message!=null){
//                if (message.equals("Checked in  Successfully")) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
////                    builder.setMessage("Do you want to checkOut  " + childrenListModelArrayList.get(position).getChildName());
//                    builder.setMessage("Do you want to checkOut ");
//                    builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
//                        checkInResponse();
//                    });
//
//                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(context, ChildrenList.class);
//                            context.startActivity(intent);
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
//                else if (message.equalsIgnoreCase("Checked out Successfully")){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage("Do you want to checkIn ");
//                    builder.setPositiveButton("ok", (dialogInterface, i) -> {
//                        checkInResponse();
//
//                    });
//                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(context, ChildrenList.class);
//                            context.startActivity(intent);
//                        }
//                    });
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//                }
//                else  {
//                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
//                    builder.setMessage("Already attendance record exists for the day");
//                    builder.setPositiveButton(R.string.ok, (dialogInterface, i) ->{
//                        checkInResponse();
//                    });
//                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent=new Intent(context,ChildrenList.class);
//                            context.startActivity(intent);
//                        }
//                    });
//                    AlertDialog dialog= builder.create();
//                    dialog.show();
////                    Toast.makeText(context, "Already attendance record exists for the day", Toast.LENGTH_SHORT).show();
//                }
//                holder.checkIn.setEnabled(false);
                Utils.preventClick(view);
            }
        });

    }

    @Override
    public int getItemCount() {
        return childrenListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView childName, className, dob, guardianName;
        AppCompatButton checkIn, childInfo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            childName = itemView.findViewById(R.id.name1);
            className = itemView.findViewById(R.id.className1);
            dob = itemView.findViewById(R.id.dob1);
            guardianName = itemView.findViewById(R.id.guardianName1);
            checkIn = itemView.findViewById(R.id.checkin);
            childInfo = itemView.findViewById(R.id.childInfo);
        }
    }


    public void filterList(ArrayList<childrenListModel> filterdNames) {
        this.childrenListModelArrayList = filterdNames;
        notifyDataSetChanged();
    }

}

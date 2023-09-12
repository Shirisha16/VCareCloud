package com.client.vcarecloud.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.InvoiceDetails;
import com.client.vcarecloud.R;
import com.client.vcarecloud.models.InvoiceListModel;

import java.util.ArrayList;
import java.util.List;

public class InvoiceListAdapter extends RecyclerView.Adapter<InvoiceListAdapter.MyViewHolder>{
    List<InvoiceListModel.Model> invoiceListModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;

    public InvoiceListAdapter(List<InvoiceListModel.Model> invoiceListModelArrayList, Context context, LoadDetails loadDetails) {
        this.invoiceListModelArrayList = invoiceListModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public InvoiceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_invoice_list,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progress);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceListAdapter.MyViewHolder holder, int position) {

        InvoiceListModel.Model model=invoiceListModelArrayList.get(position);

        holder.invoice_no.setText(model.getInvoiceNo());
        holder.child.setText(model.getChildname());
        holder.amount.setText(String.valueOf(model.getInvoiceAmount()));
        String invoiceMonth= String.valueOf(model.getInvoiceMonth());
        String invoiceYear= String.valueOf(model.getInvoiceYear());
        String invoiceFor= invoiceMonth + "-" + invoiceYear;
        if (invoiceFor!=null){
            holder.invoice_for.setText(invoiceFor);
        }

        String status1=String.valueOf(model.getPayAmount());
        if(status1.equals("0.0")){
            holder.status.setText("Pending");
        }else{
            holder.status.setText("Paid");
        }

//        String childName=invoiceListModelArrayList.get(position).getChildName();
//        holder.invoice_no.setText(invoiceListModelArrayList.get(position).getInvoiceNo());
//        holder.child.setText(childName);
//        holder.amount.setText(invoiceListModelArrayList.get(position).getInvoiceAmount());
//
//        String invoiceMonth=invoiceListModelArrayList.get(position).getInvoiceMonth();
//        String invoiceYear=invoiceListModelArrayList.get(position).getInvoiceYear();
//        String invoiceFor= invoiceMonth + "-" + invoiceYear;
//        if (invoiceFor!=null){
//            holder.invoice_for.setText(invoiceFor);
//        }
//
//        String status1=invoiceListModelArrayList.get(position).getPayAmount();
//        if(status1.equals("0.0")){
//            holder.status.setText("Pending");
//        }else{
//            holder.status.setText("Paid");
//        }
////        holder.status.setText(invoiceListModelArrayList.get(position).getStatus());
//
//        InvoiceListModel model=invoiceListModelArrayList.get(position);
//        InvoiceDetailsModel model=invoiceDetailsModelArrayList.get(position);
//        invoiceDetailsModelArrayList=new ArrayList<>();
//        InvoiceDetailsModel model=invoiceDetailsModelArrayList.get(0);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String headerId=String.valueOf(model.getHeaderId());
                String custId=String.valueOf(model.getCustId());
                String classname=model.getClassname();
                String invoiceNo=model.getInvoiceNo();
                String month=String.valueOf(model.getInvoiceMonth());
                String year=String.valueOf(model.getInvoiceYear());
                String period_from=model.getInvoicePeriodFrom();
                String period_to=model.getInvoicePeriodTo();
                String date=model.getInvoiceDate();
                String dueDate=model.getInvoiceDueDate();
                String paymentDate=model.getPaymentDate();
                String childId=String.valueOf(model.getChildId());
                String childname=model.getChildname();
                String charge=String.valueOf(model.getTotalChargeAmount());
                String discount=String.valueOf(model.getTotalDiscountAmount());
                String tax=String.valueOf(model.getTotalTaxAmount());
                String adjustment=String.valueOf(model.getTotalAdjustmentAmount());
                String invoiceAmount=String.valueOf(model.getInvoiceAmount());
                String payamount=String.valueOf(model.getPayAmount());
                String latePayment=String.valueOf(model.getLatePayment());

                Intent intent = new Intent(context, InvoiceDetails.class);
                intent.putExtra("headerId", headerId);
                intent.putExtra("custId", custId);
                intent.putExtra("classname", classname);
                intent.putExtra("invoiceNo", invoiceNo);
                intent.putExtra("invoiceMonth", month);
                intent.putExtra("invoiceYear", year);
                intent.putExtra("invoicePeriod_From", period_from);
                intent.putExtra("invoicePeriod_To", period_to);
                intent.putExtra("invoiceDate", date);
                intent.putExtra("invoiceDueDate", dueDate);
                intent.putExtra("paymentDate", paymentDate);
                intent.putExtra("childId",childId);
                intent.putExtra("childname", childname);
                intent.putExtra("totalChargeAmount", charge);
                intent.putExtra("totalDiscountAmount", discount);
                intent.putExtra("totalTaxAmount", tax);
                intent.putExtra("totalAdjustmentAmount", adjustment);
                intent.putExtra("invoiceAmount", invoiceAmount);
                intent.putExtra("payAmount", payamount);
                intent.putExtra("latePayment", latePayment);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invoiceListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView invoice_no,child,amount,invoice_for,status;
        AppCompatButton view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            invoice_no=itemView.findViewById(R.id.invoice1);
            child=itemView.findViewById(R.id.child);
            amount=itemView.findViewById(R.id.amount);
            invoice_for=itemView.findViewById(R.id.invoiceFor);
            status=itemView.findViewById(R.id.status);

            view=itemView.findViewById(R.id.view);
        }
    }

    public void filterList(ArrayList<InvoiceListModel.Model> filteredNames) {
        this.invoiceListModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

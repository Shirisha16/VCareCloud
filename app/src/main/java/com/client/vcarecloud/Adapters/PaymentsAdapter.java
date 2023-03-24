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
import com.client.vcarecloud.models.PaymentModel;

import java.util.ArrayList;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.MyViewHolder>{
    ArrayList<PaymentModel> paymentModelArrayList;
    Context context;
    LoadDetails loadDetails;

    public PaymentsAdapter(ArrayList<PaymentModel> paymentModelArrayList, Context context, LoadDetails loadDetails) {
        this.paymentModelArrayList = paymentModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public PaymentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_payments,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentsAdapter.MyViewHolder holder, int position) {

//        holder.date.setText(paymentModelArrayList.get(position).getPaymentDate());
        String fromDate1=paymentModelArrayList.get(position).getPaymentDate();
        String[] parts = fromDate1.split("T");
        String daystartDate = parts[0];
        if (fromDate1!=null) {
            holder.date.setText(daystartDate);
        }

        holder.amount.setText(paymentModelArrayList.get(position).getPaymentAmount());
        holder.paymentMode.setText(paymentModelArrayList.get(position).getPaymentType());
        holder.invoice.setText(paymentModelArrayList.get(position).getInvoice());
        holder.child.setText(paymentModelArrayList.get(position).getChild());

        PaymentModel paymentModel=paymentModelArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return paymentModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,amount,paymentMode,invoice,child;
//        AppCompatButton edit,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            amount=itemView.findViewById(R.id.amount);
            paymentMode=itemView.findViewById(R.id.mode);
            invoice=itemView.findViewById(R.id.invoice);
            child=itemView.findViewById(R.id.child);

//            edit=itemView.findViewById(R.id.edit);
//            delete=itemView.findViewById(R.id.delete);

        }
    }
    public void filterList(ArrayList<PaymentModel> filteredNames) {
        this.paymentModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

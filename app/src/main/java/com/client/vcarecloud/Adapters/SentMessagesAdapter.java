package com.client.vcarecloud.Adapters;

import static com.client.vcarecloud.Adapters.ProgramsAdapter.saveImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.R;
import com.client.vcarecloud.ViewMessages;
import com.client.vcarecloud.models.SentMsgModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SentMessagesAdapter extends RecyclerView.Adapter<SentMessagesAdapter.MyViewHolder> {
    ArrayList<SentMsgModel> sentMsgModelArrayList;
    Context context;
    LoadDetails loadDetails;

    File img;

    RelativeLayout progressRelative;

    public SentMessagesAdapter(ArrayList<SentMsgModel> sentMsgModelArrayList, Context context, LoadDetails loadDetails) {
        this.sentMsgModelArrayList = sentMsgModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_sent_messages,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progress);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        String fromDate1=sentMsgModelArrayList.get(position).getSendOn();
        String[] parts = fromDate1.split("T");
        String daystartDate = parts[0];
        if (fromDate1!=null){
//            fromDate1=fromDate1.replace("T00:00:00","");
            holder.date.setText(daystartDate);

            holder.sentTo.setText(sentMsgModelArrayList.get(position).getSendTo());
            holder.subject.setText(sentMsgModelArrayList.get(position).getMessageSubject());

            SentMsgModel sentMsgModel=sentMsgModelArrayList.get(position);

            holder.ViewMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id=sentMsgModel.getCommunicationId();
                    String custId=sentMsgModel.getCustId();
                    String sendOn=sentMsgModel.getSendOn();
                    String category=sentMsgModel.getSendToCategory();
                    String sendTo=sentMsgModel.getSendTo();
                    String subject=sentMsgModel.getMessageSubject();
                    String description=sentMsgModel.getMessageDescription();
                    String attachments=sentMsgModel.getAttachments();

                    Intent intent = new Intent(context, ViewMessages.class);
                    intent.putExtra("communicationId", id);
                    intent.putExtra("custId", custId);
                    intent.putExtra("sendOn", sendOn);
                    intent.putExtra("sendToCategory", category);
                    intent.putExtra("sendTo", sendTo);
                    intent.putExtra("messageSubject", subject);
                    intent.putExtra("messageDescription", description);

                     if(attachments.equalsIgnoreCase("null")) {
                         intent.putExtra("attachments", attachments);
                     }else {
                         try {
                             img = saveImage(context, sentMsgModelArrayList.get(position).getAttachments());
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                         String path = String.valueOf(img);
                         intent.putExtra("attachments", path);

                     }
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return sentMsgModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,sentTo,subject;
        AppCompatButton ViewMail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            date=itemView.findViewById(R.id.date);
            sentTo=itemView.findViewById(R.id.sent);
            subject=itemView.findViewById(R.id.subject);

            ViewMail=itemView.findViewById(R.id.view);
//            delete=itemView.findViewById(R.id.delete);
        }
    }

    public void filterList(ArrayList<SentMsgModel> filteredNames) {
        this.sentMsgModelArrayList = filteredNames;
        notifyDataSetChanged();
    }
}

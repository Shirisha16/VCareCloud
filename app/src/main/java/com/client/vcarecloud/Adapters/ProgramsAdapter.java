package com.client.vcarecloud.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.R;
import com.client.vcarecloud.UpdatePrograms;
import com.client.vcarecloud.models.ProgramModel;
import com.client.vcarecloud.utils.Utils;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.MyViewHolder>{
    ArrayList<ProgramModel> programModelArrayList;
    Context context;
    LoadDetails loadDetails;

    RelativeLayout progressRelative;
    File img;
    public ProgramsAdapter(ArrayList<ProgramModel> programModelArrayList, Context context, LoadDetails loadDetails) {
        this.programModelArrayList = programModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }

    @NonNull
    @Override
    public ProgramsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_programs,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);

        progressRelative=view.findViewById(R.id.progressLayout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.program.setText(programModelArrayList.get(position).getClassName());

        String minAge=programModelArrayList.get(position).getMinAge();
        String maxAge=programModelArrayList.get(position).getMaxAge();
        String age1=minAge +"m" + "-" + maxAge +"m";
        if(age1 != null){
            holder.age.setText(age1);
        }
        holder.capacity.setText(programModelArrayList.get(position).getCapacity());

        ProgramModel programModel=programModelArrayList.get(position);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String photo=programModelArrayList.get(position).getPhoto();

                Intent intent=new Intent(context, UpdatePrograms.class);
                intent.putExtra("id",programModelArrayList.get(position).getClassId());
                intent.putExtra("classId",programModelArrayList.get(position).getClassId());
                intent.putExtra("custId",programModelArrayList.get(position).getCustId());
                intent.putExtra("className",programModelArrayList.get(position).getClassName());
                intent.putExtra("minAge",programModelArrayList.get(position).getMinAge());
                intent.putExtra("maxAge",programModelArrayList.get(position).getMaxAge());
                intent.putExtra("capacity",programModelArrayList.get(position).getCapacity());
//                intent.putExtra("photo",photo);
                    try {
                        if(photo.equalsIgnoreCase("null")) {

                            intent.putExtra("photo",photo);
                        }else{
                            img = saveImage(context, photo);
                            String path = String.valueOf(img);
                            intent.putExtra("photo",path);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                context.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                        .callTimeout(2, TimeUnit.MINUTES)
                        .connectTimeout(90,TimeUnit.SECONDS)
                        .readTimeout(30,TimeUnit.SECONDS)
                        .writeTimeout(30,TimeUnit.SECONDS);

                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl(VcareApi.JSONURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want to Delete?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        VcareApi vcareApi=retrofit.create(VcareApi.class);
                        Call<String> call=vcareApi.delete_program(programModel.getClassId(),"0");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body()!=null){
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body());
                                        String message=jsonObject.getString("message");
                                        String  error=jsonObject.getString("errorMessage");
                                        if (message.equalsIgnoreCase("Deleted Successfully!")) {
                                            Utils.showAlertDialog(context, message, false);
                                            programModelArrayList.remove(position);
                                            notifyItemRemoved(position);
                                        }else{
                                            Utils.showAlertDialog(context,error,false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progressRelative.setVisibility(View.GONE);
                                String message="";
                                if (t instanceof UnknownHostException){
                                    message="No Internet Connection!";
                                }else{
                                    message="Something went wrong! try again";
                                }
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert11 = builder1.create();
                alert11.show();
                Utils.preventTwoClick(v);
            }
        });
    }


    @Override
    public int getItemCount() {
        return programModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView program,age,capacity;
        AppCompatButton edit,delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            program=itemView.findViewById(R.id.program1);
            age=itemView.findViewById(R.id.age1);
            capacity=itemView.findViewById(R.id.capacity1);

            edit=itemView.findViewById(R.id.edit);
            delete=itemView.findViewById(R.id.delete);
        }
    }
    public void filterList(ArrayList<ProgramModel> filteredNames) {
        this.programModelArrayList = filteredNames;
        notifyDataSetChanged();
    }

    public static File saveImage(final Context context, final String imageData) throws IOException {
        final byte[] imgBytesData = android.util.Base64.decode(imageData,
                android.util.Base64.DEFAULT);

        final File file = File.createTempFile("image", null, context.getCacheDir());
        final FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        try {
            bufferedOutputStream.write(imgBytesData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}

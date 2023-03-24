package com.client.vcarecloud.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.R;
import com.client.vcarecloud.models.ChildWaitListModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChildrenWaitListAdapter extends RecyclerView.Adapter<ChildrenWaitListAdapter.MyViewHolder>  {

    RelativeLayout progress;

    ArrayList<ChildWaitListModel> childWaitListModelArrayList;
    Context context;
    LoadDetails loadDetails;
    String message,error;
    UserDetails userDetails;

    public ChildrenWaitListAdapter( ArrayList<ChildWaitListModel> childWaitListModelArrayList,Context context,LoadDetails loadDetails) {
        this.childWaitListModelArrayList = childWaitListModelArrayList;
        this.context = context;
        this.loadDetails = loadDetails;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.for_childrenwait_list,parent,false);
       MyViewHolder myViewHolder=new MyViewHolder(view);
//       childWaitListModel=new ChildWaitListModel();
//       sharedPreferenceModel=
        userDetails=new UserDetails(context);
       progress=view.findViewById(R.id.progressLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

//        Login_model model1=new Login_model();
        holder.name.setText(childWaitListModelArrayList.get(position).getDisplayName());
        holder.gender.setText(childWaitListModelArrayList.get(position).getSex());
        String dateOfBirth=childWaitListModelArrayList.get(position).getDob();
        if (dateOfBirth!=null){
            dateOfBirth=dateOfBirth.replace("T00:00:00","");
        }
        holder.dob.setText(dateOfBirth);
        holder.address.setText(childWaitListModelArrayList.get(position).getCity());
        holder.guardian.setText(childWaitListModelArrayList.get(position).getFirstName());
        ChildWaitListModel model=childWaitListModelArrayList.get(position);
//        String id=model.getChildId();
//        String empId= model.getCustId();

//        Toast.makeText(context,id+empId, Toast.LENGTH_SHORT).show();

        holder.enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);
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
                VcareApi api=retrofit.create(VcareApi.class);
                Call<String> call=api.child_enroll(model.getChildId(), model.getCustId());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        progress.setVisibility(View.GONE);
                        if (response.body()!=null){
                            try {
                                JSONObject jsonObject=new JSONObject(response.body());
                                String message= jsonObject.getString("message");
                                String error= jsonObject.getString("errorMessage");
                                if(error.equalsIgnoreCase("Sorry Capacity of Program is full")){
                                    AlertDialog.Builder builder= new AlertDialog.Builder(context);
                                    builder.setMessage("sorry capacity of class is full");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                        loadDetails.onMethodCallback();
                                    });
                                    AlertDialog dialog= builder.create();
                                    dialog.show();
                                }else
//                                    if(error.equalsIgnoreCase("Child has been already enrolled"))
                                    {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage("child is enrolled successfully");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                            loadDetails.onMethodCallback();
                                            childWaitListModelArrayList.remove(position);
                                            notifyItemRemoved(position);
                                            notifyDataSetChanged();
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                        String message = "";
                        if (t instanceof UnknownHostException) {
                            message = "No internet connection!";
                        } else {
                            message = "Something went wrong! try again";
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

//        holder.remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progress.setVisibility(View.VISIBLE);
//                OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                        .callTimeout(2, TimeUnit.MINUTES)
//                        .connectTimeout(90,TimeUnit.SECONDS)
//                        .readTimeout(30,TimeUnit.SECONDS)
//                        .writeTimeout(30,TimeUnit.SECONDS);
//                Retrofit retrofit=new Retrofit.Builder()
//                        .baseUrl(VcareApi.JSONURL)
//                        .addConverterFactory(ScalarsConverterFactory.create())
//                        .client(httpClient.build())
//                        .build();
//                holder.remove.setVisibility(View.INVISIBLE);
//                VcareApi api=retrofit.create(VcareApi.class);
//                Call<String> call=api.child_remove(model.getChildId(), model.getCustId());
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        progress.setVisibility(View.GONE);
//                        if (response.body()!=null){
//                            try {
//                                JSONObject jsonObject=new JSONObject(response.body());
//                                String message=jsonObject.getString("message");
//                                AlertDialog.Builder builder= new AlertDialog.Builder(context);
//                                builder.setMessage("Deleted Successfully");
//                                builder.setCancelable(false);
//                                childWaitListModelArrayList.remove(position);
//                                notifyItemRemoved(position);
//                                builder.setPositiveButton("ok",(dialogInterface, i) -> {
////                                    loadDetails.onMethodCallback();
//                                });
//                                AlertDialog dialog= builder.create();
//                                dialog.show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        progress.setVisibility(View.GONE);
//                        String message = "";
//                        if (t instanceof UnknownHostException) {
//                            message = "No internet connection!";
//                        } else {
//                            message = "Something went wrong! try again";
//                        }
//                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//            }
//        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
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

                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                builder1.setMessage("Do you want to Delete?");
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VcareApi vcareApi=retrofit.create(VcareApi.class);
                        Call<String> call=vcareApi.child_remove(model.getChildId(),"0");
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if (response.body()!=null){
                                    try {
                                        JSONObject jsonObject=new JSONObject(response.body());
                                        String message=jsonObject.getString("message");
                                        String  error=jsonObject.getString("errorMessage");
                                        if (message.equalsIgnoreCase("Child Removed Successfully")) {
                                            Utils.showAlertDialog(context, message, false);
                                            childWaitListModelArrayList.remove(position);
                                            notifyItemRemoved(position);
                                        }else {
                                            Utils.showAlertDialog(context,error,false);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                progress.setVisibility(View.GONE);
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
                android.app.AlertDialog alert11 = builder1.create();
                alert11.show();
                Utils.preventTwoClick(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return childWaitListModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,gender,dob,address,guardian;
        ImageView profile;
        AppCompatButton enroll,remove;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.childName1);
            gender=itemView.findViewById(R.id.gender1);
            dob=itemView.findViewById(R.id.dob1);
            address=itemView.findViewById(R.id.address1);
            guardian=itemView.findViewById(R.id.guardian1);
            profile=itemView.findViewById(R.id.profile);
            enroll=itemView.findViewById(R.id.enroll);
            remove=itemView.findViewById(R.id.remove);
        }
    }


    public void filterList(ArrayList<ChildWaitListModel> filterdNames) {
        this.childWaitListModelArrayList = filterdNames;
        notifyDataSetChanged();
    }

}

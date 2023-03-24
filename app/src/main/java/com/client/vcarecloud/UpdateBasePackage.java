package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddBasePackageModel;
import com.client.vcarecloud.models.AddBasePackageResponse;
import com.client.vcarecloud.models.AddTaxModel;
import com.client.vcarecloud.models.BasePackagesModel;
import com.client.vcarecloud.models.EventModel;
import com.client.vcarecloud.models.GetClassList;
import com.client.vcarecloud.models.UpdateBasePackageModel;
import com.client.vcarecloud.models.UpdateBasePackageResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateBasePackage extends AppCompatActivity {
    ImageView back;
    TextInputEditText packageText,amountText,descriptionText;
    MaterialCheckBox checkBoxStatus;
    AutoCompleteTextView classType_spin,tax_spin;
    AppCompatButton editButton;
    RelativeLayout progress_layout;

    UserDetails userDetails;
    String message,error;

    String id,empId,custId,packId,packageName,description,amount,taxid,taxname,classId,
            className,status,taxStatus,classID,classNameSpin,taxName,taxID;

    BasePackages basePackages;
    BasePackagesModel basePackagesModel;
    UpdateBasePackageModel updateBasePackageModel;
    UpdateBasePackageResponse updateBasePackageResponse;

    ArrayList<String> class_Name = new ArrayList<>();
    ArrayList<String> class_Id = new ArrayList<>();

    ArrayList<String> tax_id = new ArrayList<>();
    ArrayList<String> tax_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_base_package);

        back=findViewById(R.id.back);
        packageText=findViewById(R.id.textPackage);
        checkBoxStatus=findViewById(R.id.status);
        classType_spin=findViewById(R.id.class_spin);
        tax_spin=findViewById(R.id.tax_spin);
        amountText=findViewById(R.id.amount);
        descriptionText=findViewById(R.id.description);
        editButton=findViewById(R.id.edit);
        progress_layout=findViewById(R.id.progress);

        basePackages=new BasePackages();
        basePackagesModel=new BasePackagesModel();
        updateBasePackageModel=new UpdateBasePackageModel();
        updateBasePackageResponse=new UpdateBasePackageResponse();

        userDetails=new UserDetails(UpdateBasePackage.this);

        basePackagesModel=(BasePackagesModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

//        classList = new ArrayList<>();
//        taxModelList = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateBasePackage.this,BasePackages.class);
                intent.putExtra("list",basePackagesModel);
                startActivity(intent);
                finish();
            }
        });

        id=getIntent().getStringExtra("id");
        empId=getIntent().getStringExtra("empid");
        packId=getIntent().getStringExtra("packageId");
        custId=getIntent().getStringExtra("custId");
        packageName=getIntent().getStringExtra("packageName");
        description=getIntent().getStringExtra("packageDescription");
        amount=getIntent().getStringExtra("packageAmount");
        taxid=getIntent().getStringExtra("taxid");
        taxname=getIntent().getStringExtra("tax");
        classId=getIntent().getStringExtra("classId");
        className=getIntent().getStringExtra("className");
        status=getIntent().getStringExtra("packageStatus");

        packageText.setText(packageName);
        amountText.setText(amount);
        tax_spin.setText(taxname);
        classType_spin.setText(className);

        if(description.equalsIgnoreCase("null")){
            descriptionText.setText("");
        }else {
            descriptionText.setText(description);
        }

        if(status.equalsIgnoreCase("y")){
            checkBoxStatus.setChecked(true);
        }else{
            checkBoxStatus.setChecked(false);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packageName=packageText.getText().toString();
                amount=amountText.getText().toString();
                taxname=tax_spin.getText().toString();
                className=classType_spin.getText().toString();

                updateBasePackageModel.setId(id);
                updateBasePackageModel.setEmpid("0");
                updateBasePackageModel.setId(packId);
                updateBasePackageModel.setCustId(custId);
                updateBasePackageModel.setPackageName(packageName);
                updateBasePackageModel.setDescription(description);
                updateBasePackageModel.setAmount(amount);
                updateBasePackageModel.setTaxid(taxid);
                updateBasePackageModel.setTaxName(taxname);
                updateBasePackageModel.setClassId(classId);
//                updateBasePackageModel.setClassName(className);
//                Toast.makeText(UpdateBasePackage.this, ""+classId+className, Toast.LENGTH_SHORT).show();
                if(checkBoxStatus.isChecked()){
                    updateBasePackageModel.setStatus("Y");
                }else{
                    updateBasePackageModel.setStatus("N");
                }
                if (Validate()) {
                    updateBasePackage();
                }
            }
        });

        getTaxList();
        getClassList();
    }

    private void updateBasePackage() {
        progress_layout.setVisibility(View.VISIBLE);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

       VcareApi api = retrofit.create(VcareApi.class);
       Call<UpdateBasePackageResponse> call=api.update_package(packId,"0",updateBasePackageModel);
       call.enqueue(new Callback<UpdateBasePackageResponse>() {
           @Override
           public void onResponse(Call<UpdateBasePackageResponse> call, Response<UpdateBasePackageResponse> response) {
               if (response.code() == 200) {
                   if (response.body().getMessage() != null) {
                       message = response.body().getMessage();
                       error = response.body().getErrorMessage();
                       if (UpdateBasePackage.this != null && !UpdateBasePackage.this.isFinishing()) {
                           androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateBasePackage.this);
                           builder.setMessage(response.body().getMessage());
                           builder.setCancelable(false);
                           builder.setPositiveButton("ok", (dialogInterface, i) -> {
                               Intent intent = new Intent(UpdateBasePackage.this, BasePackages.class);
                               startActivity(intent);
                               finish();
                           });
                           AlertDialog dialog = builder.create();
                           dialog.show();
                       }
               }else{
                       Utils.showAlertDialog(UpdateBasePackage.this,response.body().getErrorMessage(),false);
                   }
               }
               progress_layout.setVisibility(View.GONE);
           }

           @Override
           public void onFailure(Call<UpdateBasePackageResponse> call, Throwable t) {
               Toast.makeText(UpdateBasePackage.this, "Fail", Toast.LENGTH_SHORT).show();
           }
       });

    }

//    private void getTaxList() {
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
//                .callTimeout(2, TimeUnit.MINUTES)
//                .connectTimeout(90, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(VcareApi.JSONURL)
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .client(httpClient.build())
//                .build();
//        VcareApi api = retrofit.create(VcareApi.class);
//        Call<String> call=api.taxData(userDetails.getCustId());
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//                if (response.body() != null) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body());
//                        JSONArray jsonArray = jsonObject.getJSONArray("model");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                            taxID=jsonObject1.getString("taxesId");
//                            taxName=jsonObject1.getString("taxName");
//                            taxStatus=jsonObject1.getString("taxStatus");
//                            if(taxStatus.equalsIgnoreCase("true")) {
//                                tax_id.add(taxid);
//                                tax_name.add(taxname);
//                            }
//                        }
//
//                        ArrayAdapter adapter = new ArrayAdapter(UpdateBasePackage.this, android.R.layout.simple_dropdown_item_1line, tax_name);
//                        tax_spin.setAdapter(adapter);
//
//                        tax_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                String selected = (String) adapterView.getItemAtPosition(i);
//                                taxid = tax_id.get(i);
////                                taxname=tax_name.get(i);
//                            }
//                        });
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//                progress_layout.setVisibility(View.GONE);
//                String message = "";
//                if (t instanceof UnknownHostException) {
//                    message = "No internet connection!";
//                } else {
//                    message = "Something went wrong! try again";
//                }
//                Toast.makeText(UpdateBasePackage.this, message, Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//    }

    private void getTaxList() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();
        VcareApi api = retrofit.create(VcareApi.class);
        Call<String> call=api.taxData(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            taxID=jsonObject1.getString("taxesId");
                            taxName=jsonObject1.getString("taxName");
                            taxStatus=jsonObject1.getString("taxStatus");
                            if(taxStatus.equalsIgnoreCase("true")) {
                                tax_id.add(taxID);
                                tax_name.add(taxName);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateBasePackage.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(UpdateBasePackage.this, android.R.layout.simple_dropdown_item_1line, tax_name);
        tax_spin.setAdapter(adapter);

        tax_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                taxid = tax_id.get(i);
            }
        });
    }

    private void getClassList() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        Call<String> call = api.class_dropdown(custId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            classID = jsonObject1.getString("classId");
                            classNameSpin = jsonObject1.getString("className");
                            class_Id.add(classID);
                            class_Name.add(classNameSpin);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(UpdateBasePackage.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                        classType_spin.setAdapter(adapter);
                        classType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                classId = class_Id.get(i);

                            }
                        });
//                        Utils.dismissProgressDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateBasePackage.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateBasePackage.this,BasePackages.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean Validate() {
        if (!package_name()) {
            return false;
        } else if (!amount1()) {
            return false;
        }else if (!tax()) {
            return false;
        }else if (!class1()) {
            return false;
        } else {
            return true;
        }
    }

    private boolean package_name() {
        String str = packageText.getText().toString();
        if (packageText.getText().toString().isEmpty()) {
            packageText.setError("Package Name should not be empty");
            packageText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            packageText.setText(packageText.getText().toString().trim());
            packageText.setSelection(packageText.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean amount1() {
        String str = amountText.getText().toString();
        if (amountText.getText().toString().isEmpty()) {
            amountText.setError("Amount should not be empty");
            amountText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            amountText.setText(amountText.getText().toString().trim());
            amountText.setSelection(amountText.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            amountText.setError("Amount should not be point");
            amountText.setText(amountText.getText().toString().trim());
            amountText.setSelection(amountText.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean tax() {
        String str = tax_spin.getText().toString();
        if (tax_spin.getText().toString().isEmpty()) {
            tax_spin.setError("Tax should not be empty");
            tax_spin.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            tax_spin.setText(tax_spin.getText().toString().trim());
            tax_spin.setSelection(tax_spin.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean class1() {
        String str = classType_spin.getText().toString();
        if (classType_spin.getText().toString().isEmpty()) {
            classType_spin.setError("Class should not be empty");
            classType_spin.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            classType_spin.setText(classType_spin.getText().toString().trim());
            classType_spin.setSelection(classType_spin.getText().length());
            return false;
        } else {
            return true;
        }
    }
}
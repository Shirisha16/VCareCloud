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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddBasePackageModel;
import com.client.vcarecloud.models.AddBasePackageResponse;
import com.client.vcarecloud.models.AddTaxModel;
import com.client.vcarecloud.models.BasePackagesModel;
import com.client.vcarecloud.models.GetClassList;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddBasePackages extends AppCompatActivity {
    ImageView back;
    TextInputEditText packageText,amountText,descriptionText;
    MaterialCheckBox checkBoxStatus;
    AutoCompleteTextView classType_spin,tax_spin;
    AppCompatButton addButton;
    RelativeLayout progress_layout;

    BasePackagesModel basePackagesModel;
    UserDetails userDetails;
    AddBasePackageModel addBasePackageModel;

    String custId,packageName,description,amount,taxesId,taxName,taxStatus,classId,className="";
    String message,errorMessage;

    List<GetClassList> classList;
    List<AddTaxModel> taxModelList;

    ArrayList<String> class_Name = new ArrayList<>();
    ArrayList<String> class_Id = new ArrayList<>();

    ArrayList<String> tax_id = new ArrayList<>();
    ArrayList<String> tax_name = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_base_packages);

        back=findViewById(R.id.back);
        packageText=findViewById(R.id.textPackage);
        checkBoxStatus=findViewById(R.id.status);
        classType_spin=findViewById(R.id.class_spin);
        tax_spin=findViewById(R.id.tax_spin);
        amountText=findViewById(R.id.amount);
        descriptionText=findViewById(R.id.description);
        addButton=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);

        classList = new ArrayList<>();
        taxModelList = new ArrayList<>();

        basePackagesModel=new BasePackagesModel();
        addBasePackageModel=new AddBasePackageModel();

        userDetails = new UserDetails(AddBasePackages.this);

        custId=userDetails.getCustId();
        taxesId=userDetails.getTaxId();
        classId=userDetails.getClassId();

        getTaxList();
        getClassList();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBasePackages.this, BasePackages.class);
                startActivity(intent);
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packageName=packageText.getText().toString();
                amount=amountText.getText().toString();
                taxName=tax_spin.getText().toString();
                className=classType_spin.getText().toString();
                description=descriptionText.getText().toString();
//                status=checkBoxStatus.getText().toString();

                if (Validate()) {
                addBasePackage();
                }
            }
        });

        if(checkBoxStatus.isChecked()){
            checkBoxStatus.setChecked(true);
        }
        else{
            checkBoxStatus.setChecked(false);
        }
    }

    private void addBasePackage() {
        progress_layout.setVisibility(View.VISIBLE);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        AddBasePackageModel addBasePackageModel=new AddBasePackageModel();
        addBasePackageModel.setEmpid(String.valueOf(0));
        addBasePackageModel.setCustId(custId);
        addBasePackageModel.setPackageName(packageName);
        addBasePackageModel.setDescription(description);
        addBasePackageModel.setAmount(amount);
        addBasePackageModel.setTaxid(taxesId);
        addBasePackageModel.setClassId(classId);

        if(checkBoxStatus.isChecked()==true){
            addBasePackageModel.setStatus("Y");
        }else {
            addBasePackageModel.setStatus("N");
        }
//        Toast.makeText(AddBasePackages.this, ""+classId, Toast.LENGTH_SHORT).show();
        Call<AddBasePackageResponse> call=api.add_package("0",addBasePackageModel);
        call.enqueue(new Callback<AddBasePackageResponse>() {
            @Override
            public void onResponse(Call<AddBasePackageResponse> call, Response<AddBasePackageResponse> response) {
             if (response.code()==200) {
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddBasePackages.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddBasePackages.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddBasePackages.this, BasePackages.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddBasePackages.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddBasePackageResponse> call, Throwable t) {
                Toast.makeText(AddBasePackages.this, "Fail", Toast.LENGTH_SHORT).show();
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
        Call<String> call = api.class_dropdown(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            classId = jsonObject1.getString("classId");
                            className = jsonObject1.getString("className");
                            class_Id.add(classId);
                            class_Name.add(className);
                        }

                        ArrayAdapter adapter = new ArrayAdapter(AddBasePackages.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                        classType_spin.setAdapter(adapter);
                        classType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                classId = class_Id.get(i);
                            }
                        });
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
                Toast.makeText(AddBasePackages.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

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

                            taxesId=jsonObject1.getString("taxesId");
                            taxName=jsonObject1.getString("taxName");
                            taxStatus=jsonObject1.getString("taxStatus");
                            if(taxStatus.equalsIgnoreCase("true")) {
                                tax_id.add(taxesId);
                                tax_name.add(taxName);
                            }
                        }

                        ArrayAdapter adapter = new ArrayAdapter(AddBasePackages.this, android.R.layout.simple_dropdown_item_1line, tax_name);
                        tax_spin.setAdapter(adapter);

                        tax_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                taxesId = tax_id.get(i);
                            }
                        });
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
                Toast.makeText(AddBasePackages.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
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
        } else if(str.length() > 0 && str.startsWith(".")) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(AddBasePackages.this,BasePackages.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}

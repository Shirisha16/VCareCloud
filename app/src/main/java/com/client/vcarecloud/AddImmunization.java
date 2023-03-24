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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddDiscountResponse;
import com.client.vcarecloud.models.AddImmunizationModel;
import com.client.vcarecloud.models.AddImmunizationResponse;
import com.client.vcarecloud.models.ImmunizationModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddImmunization extends AppCompatActivity {
    ImageView back;
    TextInputEditText nameText,codeText,dose1Text,dose2Text,dose3Text,dose4Text,dose5Text,
            dose6Text,to1,to2,to3,to4,to5,to6;

    AutoCompleteTextView month_spin1,month_spin2,month_spin3,month_spin4,month_spin5,month_spin6,
                          year_spin1,year_spin2,year_spin3,year_spin4,year_spin5,year_spin6;

    MaterialCheckBox optionalBox;
    AppCompatButton addButton;
    RelativeLayout progress_layout;

    String custId,name,code,dose1a,dose1b,dose1c,dose1d,dose2a,dose2b,dose2c,dose2d,
           dose3a,dose3b,dose3c,dose3d,dose4a,dose4b,dose4c,dose4d,dose5a,dose5b,dose5c,dose5d,
           dose6a,dose6b,dose6c,dose6d,optional;
    String message,errorMessage,selected;

    String[] monthSpins = {"--Select--", "Month(s)", "Year(s)"};

    UserDetails userDetails;
    AddImmunizationModel addImmunizationModel;
    ImmunizationModel immunizationModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_immunization);

        back=findViewById(R.id.back);
        nameText=findViewById(R.id.textName);
        codeText=findViewById(R.id.textCode);
        optionalBox=findViewById(R.id.optional);
        dose1Text=findViewById(R.id.textDose1);
        month_spin1=findViewById(R.id.month_spin);
        to1=findViewById(R.id.textto1);
        year_spin1=findViewById(R.id.year_spin);
        dose2Text=findViewById(R.id.textDose2);
        month_spin2=findViewById(R.id.month_spin_dose2);
        to2=findViewById(R.id.to_dose2);
        year_spin2=findViewById(R.id.year_spin_dose2);
        dose3Text=findViewById(R.id.textDose3);
        month_spin3=findViewById(R.id.month_spin_dose3);
        to3=findViewById(R.id.to_dose3);
        year_spin3=findViewById(R.id.year_spin_dose3);
        dose4Text=findViewById(R.id.textDose4);
        month_spin4=findViewById(R.id.month_spin_dose4);
        to4=findViewById(R.id.to_dose4);
        year_spin4=findViewById(R.id.year_spin_dose4);
        dose5Text=findViewById(R.id.textDose5);
        month_spin5=findViewById(R.id.month_spin_dose5);
        to5=findViewById(R.id.to_dose5);
        year_spin5=findViewById(R.id.year_spin_dose5);
        dose6Text=findViewById(R.id.textDose6);
        month_spin6=findViewById(R.id.month_spin_dose6);
        to6=findViewById(R.id.to_dose6);
        year_spin6=findViewById(R.id.year_spin_dose6);

        addButton=findViewById(R.id.add);
        progress_layout=findViewById(R.id.progress);

        userDetails = new UserDetails(AddImmunization.this);

        custId = userDetails.getCustId();
        immunizationModel=new ImmunizationModel();
        addImmunizationModel=new AddImmunizationModel();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddImmunization.this, Immunization.class);
                startActivity(intent);
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=nameText.getText().toString();
                code=codeText.getText().toString();
                optional=optionalBox.getText().toString();
                dose1a=dose1Text.getText().toString();
                dose1b=month_spin1.getText().toString();
                dose1c=to1.getText().toString();
                dose1d=year_spin1.getText().toString();
                dose2a=dose2Text.getText().toString();
                dose2b=month_spin2.getText().toString();
                dose2c=to2.getText().toString();
                dose2d=year_spin2.getText().toString();
                dose3a=dose3Text.getText().toString();
                dose3b=month_spin3.getText().toString();
                dose3c=to3.getText().toString();
                dose3d=year_spin3.getText().toString();
                dose4a=dose4Text.getText().toString();
                dose4b=month_spin4.getText().toString();
                dose4c=to4.getText().toString();
                dose4d=year_spin4.getText().toString();
                dose5a=dose5Text.getText().toString();
                dose5b=month_spin5.getText().toString();
                dose5c=to5.getText().toString();
                dose5d=year_spin5.getText().toString();
                dose6a=dose6Text.getText().toString();
                dose6b=month_spin6.getText().toString();
                dose6c=to6.getText().toString();
                dose6d=year_spin6.getText().toString();

                  if (Validation()) {
                    addImmunization();
                }
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(AddImmunization.this, android.R.layout.simple_list_item_1, monthSpins);

        month_spin1.setAdapter(adapter);
        month_spin1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        month_spin2.setAdapter(adapter);
        month_spin2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        month_spin3.setAdapter(adapter);
        month_spin3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        month_spin4.setAdapter(adapter);
        month_spin4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        month_spin5.setAdapter(adapter);
        month_spin5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        month_spin6.setAdapter(adapter);
        month_spin6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        year_spin1.setAdapter(adapter);
        year_spin1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        year_spin2.setAdapter(adapter);
        year_spin2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        year_spin3.setAdapter(adapter);
        year_spin3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        year_spin4.setAdapter(adapter);
        year_spin4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        year_spin5.setAdapter(adapter);
        year_spin5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });

        year_spin6.setAdapter(adapter);
        year_spin6.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
            }
        });
    }

    private void addImmunization() {
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
        AddImmunizationModel addImmunizationModel=new AddImmunizationModel();

        addImmunizationModel.setCustId(custId);
        addImmunizationModel.setName(name);
        addImmunizationModel.setCode(code);
        addImmunizationModel.setDose1a(dose1a);

        if(dose1b.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose1b("m");
        }else if(dose1b.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose1b("y");
        }

//        if(dose1c.equals("") || dose1c.equalsIgnoreCase("--Select--")){
//            addImmunizationModel.setDose1c(null);
//        }else {
//            addImmunizationModel.setDose1c(dose1c);
//        }

        if(dose1c.equals("")){
            addImmunizationModel.setDose1c(null);
        }else
        addImmunizationModel.setDose1c(dose1c);

        if(dose1d.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose1d("m");
        }else if(dose1d.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose1d("y");
        }

        if(dose2a.equals("")){
            addImmunizationModel.setDose2a(null);
        }else {
        addImmunizationModel.setDose2a(dose2a);}

        if(dose2b.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose2b("m");
        }else if(dose2b.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose2b("y");
        }

        if(dose2c.equals("")){
            addImmunizationModel.setDose2c(null);
        }else{
            addImmunizationModel.setDose2c(dose2c);
        }

        if(dose2d.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose2d("m");
        }else if(dose2d.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose2d("y");
        }

        if(dose3a.equals("")){
            addImmunizationModel.setDose3a(null);
        }else{
        addImmunizationModel.setDose3a(dose3a);}

        if(dose3b.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose3b("m");
        }else if(dose3b.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose3b("y");
        }

        if(dose3c.equals("")){
            addImmunizationModel.setDose3c(null);
        }else{
            addImmunizationModel.setDose3c(dose3c);
        }


        if(dose3d.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose3d("m");
        }else if(dose3d.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose3d("y");
        }

        if(dose4a.equals("")){
            addImmunizationModel.setDose4a(null);
        }else{
            addImmunizationModel.setDose4a(dose4a);
        }

        if(dose4b.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose4b("m");
        }else if(dose4b.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose4b("y");
        }

        if(dose4c.equals("")){
            addImmunizationModel.setDose4c(null);
        }else{
            addImmunizationModel.setDose4c(dose4c);
        }

        if(dose4d.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose4d("m");
        }else if(dose4d.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose4d("y");
        }

        if(dose5a.equals("")){
            addImmunizationModel.setDose5a(null);
        }else{
            addImmunizationModel.setDose5a(dose5a);
        }

        if(dose5b.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose5b("m");
        }else if(dose5b.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose5b("y");
        }

        if(dose5c.equals("")){
            addImmunizationModel.setDose5c(null);
        }else{
            addImmunizationModel.setDose5c(dose5c);
        }

        if(dose5d.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose5d("m");
        }else if(dose5d.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose5d("y");
        }

        if(dose6a.equals("")){
            addImmunizationModel.setDose6a(null);
        }else{
            addImmunizationModel.setDose6a(dose6a);
        }

        if(dose6b.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose6b("m");
        }else if(dose6b.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose6b("y");
        }

        if(dose6c.equals("")){
            addImmunizationModel.setDose6c(null);
        }else{
            addImmunizationModel.setDose6c(dose6c);
        }

        if(dose6d.equalsIgnoreCase("Month(s)")){
            addImmunizationModel.setDose6d("m");
        }else if(dose6d.equalsIgnoreCase("Year(s)")){
            addImmunizationModel.setDose6d("y");
        }

        if(optionalBox.isChecked()==true){
            addImmunizationModel.setOptional("Y");
        }else{
            addImmunizationModel.setOptional("N");
        }

        Call<AddImmunizationResponse> call= api.add_immunization("0",addImmunizationModel);
        call.enqueue(new Callback<AddImmunizationResponse>() {
            @Override
            public void onResponse(Call<AddImmunizationResponse> call, Response<AddImmunizationResponse> response) {
               if (response.code()==200) {
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddImmunization.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddImmunization.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddImmunization.this, Immunization.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddImmunization.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddImmunizationResponse> call, Throwable t) {
                Toast.makeText(AddImmunization.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean Validation() {
        if(!name1()){
            return false;
        }else if(!code1()){
            return false;
        }else if(!dose1Valid()){
            return false;
        }else if(!month1Valid()){
            return false;
        } else if(!toValid()){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean name1() {
        String str = nameText.getText().toString();
        if (nameText.getText().toString().isEmpty()) {
            nameText.setError("Immunization Name should not be empty");
            nameText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            nameText.setText(nameText.getText().toString().trim());
            nameText.setSelection(nameText.getText().length());
            return false;
        }else {
            return true;
        }
    }

    private boolean code1() {
        String str = codeText.getText().toString();
        if (codeText.getText().toString().isEmpty()) {
            codeText.setError("Code should not be empty");
            codeText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            codeText.setText(codeText.getText().toString().trim());
            codeText.setSelection(codeText.getText().length());
            return false;
        }else {
            return true;
        }
    }

    private boolean toValid() {
        if(to1.getText().toString().length()>0 && (year_spin1.getText().toString().equalsIgnoreCase("--Select--")|| year_spin1.getText().toString().isEmpty())){
            Utils.showAlertDialog(AddImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }else if(to2.getText().toString().length()>0 && (year_spin2.getText().toString().equalsIgnoreCase("--Select--") || year_spin2.getText().toString().isEmpty())){
            Utils.showAlertDialog(AddImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }
        if(to3.getText().toString().length()>0 && (year_spin3.getText().toString().equalsIgnoreCase("--Select--") || year_spin3.getText().toString().isEmpty())){
            Utils.showAlertDialog(AddImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }
        if(to4.getText().toString().length()>0 && (year_spin4.getText().toString().equalsIgnoreCase("--Select--") || year_spin4.getText().toString().isEmpty())){
            Utils.showAlertDialog(AddImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }
        if(to5.getText().toString().length()>0 && (year_spin5.getText().toString().equalsIgnoreCase("--Select--") || year_spin5.getText().toString().isEmpty())){
            Utils.showAlertDialog(AddImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }
        if(to6.getText().toString().length()>0 && (year_spin6.getText().toString().equalsIgnoreCase("--Select--") || year_spin6.getText().toString().isEmpty())){
            Utils.showAlertDialog(AddImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }
        else {
            return true;
        }
    }

    private boolean month1Valid() {
        if (month_spin1.getText().toString().isEmpty() || month_spin1.getText().toString().equalsIgnoreCase("--Select--")) {
            Utils.showAlertDialog(AddImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose2Text.getText().toString().length() > 0 && (month_spin2.getText().toString().equalsIgnoreCase("--Select--") || month_spin2.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(AddImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose3Text.getText().toString().length() > 0 && (month_spin3.getText().toString().equalsIgnoreCase("--Select--") || month_spin3.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(AddImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose4Text.getText().toString().length() > 0 && (month_spin4.getText().toString().equalsIgnoreCase("--Select--") || month_spin4.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(AddImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose5Text.getText().toString().length() > 0 && (month_spin5.getText().toString().equalsIgnoreCase("--Select--") || month_spin5.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(AddImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose6Text.getText().toString().length() > 0 && (month_spin6.getText().toString().equalsIgnoreCase("--Select--") || month_spin6.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(AddImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else{
            return true;
        }
    }

    private boolean dose1Valid() {
        if (dose1Text.getText().toString().isEmpty()) {
            dose1Text.setError("Dose 1 should not be empty");
            dose1Text.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(AddImmunization.this,Immunization.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
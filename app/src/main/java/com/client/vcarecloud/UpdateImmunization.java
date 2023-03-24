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
import com.client.vcarecloud.models.DiscountListModel;
import com.client.vcarecloud.models.ImmunizationModel;
import com.client.vcarecloud.models.UpdateDiscountModel;
import com.client.vcarecloud.models.UpdateImmunizationModel;
import com.client.vcarecloud.models.UpdateImmunizationResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateImmunization extends AppCompatActivity {
    ImageView back;
    TextInputEditText nameText,codeText,dose1Text,dose2Text,dose3Text,dose4Text,dose5Text,
                      dose6Text,to1,to2,to3,to4,to5,to6;
    AutoCompleteTextView month_spin1,month_spin2,month_spin3,month_spin4,month_spin5,month_spin6,
                         year_spin1,year_spin2,year_spin3,year_spin4,year_spin5,year_spin6;

    MaterialCheckBox optionalBox;
    AppCompatButton editButton;
    RelativeLayout progress_layout;

    String  id,custId,name,code,dose1a,dose1b,dose1c,dose1d,dose2a,dose2b,dose2c,dose2d,
            dose3a,dose3b,dose3c,dose3d,dose4a,dose4b,dose4c,dose4d,dose5a,dose5b,dose5c,dose5d,
            dose6a,dose6b,dose6c,dose6d,optional;

    UserDetails userDetails;
    String message,error,selected;

    ArrayList<String> monthList = new ArrayList<>();

    ImmunizationModel immunizationModel;
    UpdateImmunizationModel updateImmunizationModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_immunization);

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

        editButton=findViewById(R.id.edit);
        progress_layout=findViewById(R.id.progress);

        monthList.add("--Select--");
        monthList.add("Month(s)");
        monthList.add("Year(s)");

        immunizationModel=new ImmunizationModel();
        updateImmunizationModel=new UpdateImmunizationModel();

        userDetails=new UserDetails(UpdateImmunization.this);

        immunizationModel=(ImmunizationModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateImmunization.this,Immunization.class);
                intent.putExtra("list",immunizationModel);
                startActivity(intent);
                finish();
            }
        });

        id=getIntent().getStringExtra("id");
        custId=getIntent().getStringExtra("custId");
        name=getIntent().getStringExtra("immunizationName");
        code=getIntent().getStringExtra("immunizationCode");
        dose1a=getIntent().getStringExtra("dose1a");
        dose1b=getIntent().getStringExtra("dose1b");
        dose1c=getIntent().getStringExtra("dose1c");
        dose1d=getIntent().getStringExtra("dose1d");
        dose2a=getIntent().getStringExtra("dose2a");
        dose2b=getIntent().getStringExtra("dose2b");
        dose2c=getIntent().getStringExtra("dose2c");
        dose2d=getIntent().getStringExtra("dose2d");
        dose3a=getIntent().getStringExtra("dose3a");
        dose3b=getIntent().getStringExtra("dose3b");
        dose3c=getIntent().getStringExtra("dose3c");
        dose3d=getIntent().getStringExtra("dose3d");
        dose4a=getIntent().getStringExtra("dose4a");
        dose4b=getIntent().getStringExtra("dose4b");
        dose4c=getIntent().getStringExtra("dose4c");
        dose4d=getIntent().getStringExtra("dose4d");
        dose5a=getIntent().getStringExtra("dose5a");
        dose5b=getIntent().getStringExtra("dose5b");
        dose5c=getIntent().getStringExtra("dose5c");
        dose5d=getIntent().getStringExtra("dose5d");
        dose6a=getIntent().getStringExtra("dose6a");
        dose6b=getIntent().getStringExtra("dose6b");
        dose6c=getIntent().getStringExtra("dose6c");
        dose6d=getIntent().getStringExtra("dose6d");
        optional=getIntent().getStringExtra("isOptional");

        nameText.setText(name);
        codeText.setText(code);

//        for Dose 1
        if (dose1a.contains("-")){
            String[] separated = dose1a.split("-");
            if (separated[0].endsWith("y ")){
                month_spin1.setText("Year(s)");
            }else {
                month_spin1.setText("Month(s)");
            }
                dose1Text.setText(removeLastCharacter2(separated[0]));

            if (separated[1].length()>0){
                if(!separated[1].isEmpty()){
                    if (separated[1].endsWith("y")){
                        year_spin1.setText("Year(s)");
                    }else {
                        year_spin1.setText("Month(s)");
                    }
                    to1.setText(removeLastCharacter1(separated[1]));
                }
            }else {
                dose1Text.setText(removeLastCharacter2(separated[0]));
            }
        }
        else {
            if (dose1a.endsWith("y")){
                month_spin1.setText("Year(s)");
            }else {
                month_spin1.setText("Month(s)");
            }
            dose1Text.setText(removeLastCharacter1(dose1a));
        }

//        For dose 2
        if(dose2a.equalsIgnoreCase("null")) {
            dose2Text.setText("");
        }else {
            if (dose2a.contains("-")) {
                String[] separated = dose2a.split("-");
                if (separated[0].endsWith("y ")) {
                    month_spin2.setText("Year(s)");
                } else {
                    month_spin2.setText("Month(s)");
                }
                dose2Text.setText(removeLastCharacter2(separated[0]));

                if (separated[1].length() > 0) {
                    if (!separated[1].isEmpty()) {
                        if (separated[1].endsWith("y")) {
                            year_spin2.setText("Year(s)");
                        } else {
                            year_spin2.setText("Month(s)");
                        }
                        to2.setText(removeLastCharacter1(separated[1]));
                    }
                } else {
                    dose2Text.setText(removeLastCharacter2(separated[0]));
                }
            } else {
                if (dose2a.endsWith("y")) {
                    month_spin2.setText("Year(s)");
                } else {
                    month_spin2.setText("Month(s)");
                }
                dose2Text.setText(removeLastCharacter1(dose2a));
            }
        }

//        For dose 3
        if(dose3a.equalsIgnoreCase("null")) {
            dose3Text.setText("");
        }else {
            if (dose3a.contains("-")) {
                String[] separated = dose3a.split("-");
                if (separated[0].endsWith("y ")) {
                    month_spin3.setText("Year(s)");
                } else {
                    month_spin3.setText("Month(s)");
                }
                dose3Text.setText(removeLastCharacter2(separated[0]));

                if (separated[1].length() > 0) {
                    if (!separated[1].isEmpty()) {
                        if (separated[1].endsWith("y")) {
                            year_spin3.setText("Year(s)");
                        } else {
                            year_spin3.setText("Month(s)");
                        }
                        to3.setText(removeLastCharacter1(separated[1]));
                    }
                } else {
                    dose3Text.setText(removeLastCharacter2(separated[0]));
                }
            } else {
                if (dose3a.endsWith("y")) {
                    month_spin3.setText("Year(s)");
                } else {
                    month_spin3.setText("Month(s)");
                }
                dose3Text.setText(removeLastCharacter1(dose3a));
            }
        }

//        For Dose 4
        if(dose4a.equalsIgnoreCase("null")) {
            dose4Text.setText("");
        }else {
            if (dose4a.contains("-")) {
                String[] separated = dose4a.split("-");
                if (separated[0].endsWith("y ")) {
                    month_spin4.setText("Year(s)");
                } else {
                    month_spin4.setText("Month(s)");
                }
                dose4Text.setText(removeLastCharacter2(separated[0]));

                if (separated[1].length() > 0) {
                    if (!separated[1].isEmpty()) {
                        if (separated[1].endsWith("y")) {
                            year_spin4.setText("Year(s)");
                        } else {
                            year_spin4.setText("Month(s)");
                        }
                        to4.setText(removeLastCharacter1(separated[1]));
                    }
                } else {
                    dose4Text.setText(removeLastCharacter2(separated[0]));
                }
            } else {
                if (dose4a.endsWith("y")) {
                    month_spin4.setText("Year(s)");
                } else {
                    month_spin4.setText("Month(s)");
                }
                dose4Text.setText(removeLastCharacter1(dose4a));
            }
        }

//        For Dose 5
        if(dose5a.equalsIgnoreCase("null")) {
            dose5Text.setText("");
        }else {
            if (dose5a.contains("-")) {
                String[] separated = dose5a.split("-");
                if (separated[0].endsWith("y ")) {
                    month_spin5.setText("Year(s)");
                } else {
                    month_spin5.setText("Month(s)");
                }
                dose5Text.setText(removeLastCharacter2(separated[0]));

                if (separated[1].length() > 0) {
                    if (!separated[1].isEmpty()) {
                        if (separated[1].endsWith("y")) {
                            year_spin5.setText("Year(s)");
                        } else {
                            year_spin5.setText("Month(s)");
                        }
                        to5.setText(removeLastCharacter1(separated[1]));
                    }
                } else {
                    dose5Text.setText(removeLastCharacter2(separated[0]));
                }
            } else {
                if (dose5a.endsWith("y")) {
                    month_spin5.setText("Year(s)");
                } else {
                    month_spin5.setText("Month(s)");
                }
                dose5Text.setText(removeLastCharacter1(dose5a));
            }
        }

//        For Dose 6
        if(dose6a.equalsIgnoreCase("null")) {
            dose6Text.setText("");
        }else {
            if (dose6a.contains("-")) {
                String[] separated = dose6a.split("-");
                if (separated[0].endsWith("y ")) {
                    month_spin6.setText("Year(s)");
                } else {
                    month_spin6.setText("Month(s)");
                }
                dose6Text.setText(removeLastCharacter2(separated[0]));

                if (separated[1].length() > 0) {
                    if (!separated[1].isEmpty()) {
                        if (separated[1].endsWith("y")) {
                            year_spin6.setText("Year(s)");
                        } else {
                            year_spin6.setText("Month(s)");
                        }
                        to6.setText(removeLastCharacter1(separated[1]));
                    }
                } else {
                    dose6Text.setText(removeLastCharacter2(separated[0]));
                }
            } else {
                if (dose6a.endsWith("y")) {
                    month_spin6.setText("Year(s)");
                } else {
                    month_spin6.setText("Month(s)");
                }
                dose6Text.setText(removeLastCharacter1(dose6a));
            }
        }

        if(optional.equalsIgnoreCase("y")){
            optionalBox.setChecked(true);
        }else{
            optionalBox.setChecked(false);
        }

        editButton.setOnClickListener(new View.OnClickListener() {
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
                    editImmunization();
                }
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(UpdateImmunization.this, android.R.layout.simple_list_item_1, monthList);

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

    public String removeLastCharacter2(String str){
        if ((str != null) && (str.length() > 0)) {
            return str.substring(0, str.length() - 3);
        }
        else{
            return "";
        }
    }

    public String removeLastCharacter1(String str){
        if ((str != null) && (str.length() > 0)) {
            if(str.startsWith(" ")){
                return str.substring(1, str.length() - 2);
            }else {
                return str.substring(0, str.length() - 2);
            }
        }
        else{
            return "";
        }
    }

    private void editImmunization() {
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
        updateImmunizationModel.setId(id);
        updateImmunizationModel.setCustId(custId);
        updateImmunizationModel.setName(name);
        updateImmunizationModel.setCode(code);
        updateImmunizationModel.setDose1a(dose1a);

        if(dose1b.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose1b("m");
        }else if(dose1b.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose1b("y");
        }

        if(dose1c.equals("") || dose1d.equalsIgnoreCase("--Select--")){
            updateImmunizationModel.setDose1c(null);
        }else {
            updateImmunizationModel.setDose1c(dose1c);
        }

        if(dose1d.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose1d("m");
        }else if(dose1d.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose1d("y");
        }

        if(dose2a.equals("")){
            updateImmunizationModel.setDose2a(null);
        }else {
            updateImmunizationModel.setDose2a(dose2a);}

        if(dose2b.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose2b("m");
        }else if(dose2b.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose2b("y");
        }

        if(dose2c.equals("")){
            updateImmunizationModel.setDose2c(null);
        }else{
            updateImmunizationModel.setDose2c(dose2c);
        }

        if(dose2d.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose2d("m");
        }else if(dose2d.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose2d("y");
        }

        if(dose3a.equals("")){
            updateImmunizationModel.setDose3a(null);
        }else{
            updateImmunizationModel.setDose3a(dose3a);}

        if(dose3b.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose3b("m");
        }else if(dose3b.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose3b("y");
        }

        if(dose3c.equals("")){
            updateImmunizationModel.setDose3c(null);
        }else{
            updateImmunizationModel.setDose3c(dose3c);
        }


        if(dose3d.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose3d("m");
        }else if(dose3d.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose3d("y");
        }

        if(dose4a.equals("")){
            updateImmunizationModel.setDose4a(null);
        }else{
            updateImmunizationModel.setDose4a(dose4a);
        }

        if(dose4b.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose4b("m");
        }else if(dose4b.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose4b("y");
        }

        if(dose4c.equals("")){
            updateImmunizationModel.setDose4c(null);
        }else{
            updateImmunizationModel.setDose4c(dose4c);
        }

        if(dose4d.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose4d("m");
        }else if(dose4d.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose4d("y");
        }

        if(dose5a.equals("")){
            updateImmunizationModel.setDose5a(null);
        }else{
            updateImmunizationModel.setDose5a(dose5a);
        }

        if(dose5b.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose5b("m");
        }else if(dose5b.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose5b("y");
        }

        if(dose5c.equals("")){
            updateImmunizationModel.setDose5c(null);
        }else{
            updateImmunizationModel.setDose5c(dose5c);
        }

        if(dose5d.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose5d("m");
        }else if(dose5d.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose5d("y");
        }

        if(dose6a.equals("")){
            updateImmunizationModel.setDose6a(null);
        }else{
            updateImmunizationModel.setDose6a(dose6a);
        }

        if(dose6b.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose6b("m");
        }else if(dose6b.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose6b("y");
        }

        if(dose6c.equals("")){
            updateImmunizationModel.setDose6c(null);
        }else{
            updateImmunizationModel.setDose6c(dose6c);
        }

        if(dose6d.equalsIgnoreCase("Month(s)")){
            updateImmunizationModel.setDose6d("m");
        }else if(dose6d.equalsIgnoreCase("Year(s)")){
            updateImmunizationModel.setDose6d("y");
        }

        if(optionalBox.isChecked()){
            updateImmunizationModel.setOptional("Y");
        }else{
            updateImmunizationModel.setOptional("N");
        }

        Call<UpdateImmunizationResponse> call=api.update_immunization(id,"0",updateImmunizationModel);
        call.enqueue(new Callback<UpdateImmunizationResponse>() {
            @Override
            public void onResponse(Call<UpdateImmunizationResponse> call, Response<UpdateImmunizationResponse> response) {
               if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdateImmunization.this != null && !UpdateImmunization.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UpdateImmunization.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateImmunization.this, Immunization.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateImmunization.this,response.body().getErrorMessage(),false);
                    }
               }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateImmunizationResponse> call, Throwable t) {
                Toast.makeText(UpdateImmunization.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean Validation() {
        if(!name1()){
            return false;
        }else if(!code1()){
            return false;
        }else if(!month1Valid()){
            return false;
        }else if(!toValid()){
            return false;
        } else if(!dose1Valid()){
            return false;
        } else{
            return true;
        }
    }

    private boolean toValid() {
        if(to1.getText().toString().length()>0 && year_spin1.getText().toString().equalsIgnoreCase("--Select--")){
            Utils.showAlertDialog(UpdateImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }else if(to2.getText().toString().length()>0 && year_spin2.getText().toString().equalsIgnoreCase("--Select--")){
            Utils.showAlertDialog(UpdateImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }if(to3.getText().toString().length()>0 && year_spin3.getText().toString().equalsIgnoreCase("--Select--")){
            Utils.showAlertDialog(UpdateImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }if(to4.getText().toString().length()>0 && year_spin4.getText().toString().equalsIgnoreCase("--Select--")){
            Utils.showAlertDialog(UpdateImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }if(to5.getText().toString().length()>0 && year_spin5.getText().toString().equalsIgnoreCase("--Select--")){
            Utils.showAlertDialog(UpdateImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        }if(to6.getText().toString().length()>0 && year_spin6.getText().toString().equalsIgnoreCase("--Select--")){
            Utils.showAlertDialog(UpdateImmunization.this,"Please Select Month(s) or Year(s)", false);
            return false;
        } else {
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

    private boolean month1Valid() {
        if (month_spin1.getText().toString().isEmpty() || month_spin1.getText().toString().equalsIgnoreCase("--Select--")) {
            Utils.showAlertDialog(UpdateImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose2Text.getText().toString().length() > 0 && (month_spin2.getText().toString().equalsIgnoreCase("--Select--") || month_spin2.getText().toString().isEmpty())  ) {
                Utils.showAlertDialog(UpdateImmunization.this, "Please Select Month(s) or Year(s)", false);
                return false;
        }else if(dose3Text.getText().toString().length() > 0 && (month_spin3.getText().toString().equalsIgnoreCase("--Select--") || month_spin3.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(UpdateImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose4Text.getText().toString().length() > 0 && (month_spin4.getText().toString().equalsIgnoreCase("--Select--") || month_spin4.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(UpdateImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose5Text.getText().toString().length() > 0 && (month_spin5.getText().toString().equalsIgnoreCase("--Select--") || month_spin5.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(UpdateImmunization.this, "Please Select Month(s) or Year(s)", false);
            return false;
        }else if(dose6Text.getText().toString().length() > 0 && (month_spin6.getText().toString().equalsIgnoreCase("--Select--") || month_spin6.getText().toString().isEmpty())  ) {
            Utils.showAlertDialog(UpdateImmunization.this, "Please Select Month(s) or Year(s)", false);
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
        Intent intent =new Intent(UpdateImmunization.this,Immunization.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
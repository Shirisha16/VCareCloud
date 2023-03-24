package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.ChildAbsentModel;
import com.client.vcarecloud.models.UpdateAbsentModel;
import com.client.vcarecloud.models.UpdateAbsentResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UpdateAbsentList extends AppCompatActivity {
    String className,childName,fromDate,toDate,absentType,id,childId,classId,classID,classNameSpin,
            custId,createdBy,createdOn,lastChangedBy,lastChangedOn,status="Active";

    AutoCompleteTextView className_spin, child_spin, absentType_spin;
    TextInputEditText from, to,reason;
    ImageView back;
    AppCompatButton update;
    RelativeLayout progress;
    UserDetails userDetails;
    Dialog dialog;

    boolean dateChecked = false;

    ArrayList<String> class_Name=new ArrayList<>();
    ArrayList<String> class_Id=new ArrayList<>();

    ArrayList<String> display_Name=new ArrayList<>();
    ArrayList<String> child_Id=new ArrayList<>();

    String[] absentTypeList = {"Sick", "Vacation", "Other"};

    String empId=null,absentNotes=null,reason1,selected;
    ChildAbsentModel childAbsentModel;
    UpdateAbsentResponse updateAbsentResponse;
    UpdateAbsentModel updateAbsentModel;
    ChildrenAbsentList childrenAbsentList;
    JSONObject jsonObject;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_absent_list);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        className_spin = findViewById(R.id.class_spin);
        child_spin = findViewById(R.id.child_spin);
        absentType_spin = findViewById(R.id.absent_spin);
        userDetails=new UserDetails(UpdateAbsentList.this);
        from = findViewById(R.id.from1);
        to = findViewById(R.id.to1);
        reason=findViewById(R.id.reason1);
        back=findViewById(R.id.back);
        progress=findViewById(R.id.progress_layout);

        childrenAbsentList=new ChildrenAbsentList();
        updateAbsentResponse=new UpdateAbsentResponse();
        childAbsentModel = (ChildAbsentModel)getIntent().getSerializableExtra("list");

        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        update=findViewById(R.id.update);

        updateAbsentModel=new UpdateAbsentModel();
        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(UpdateAbsentList.this,ChildrenAbsentList.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(UpdateAbsentList.this,ChildrenAbsentList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        childAbsentModel=new ChildAbsentModel();

        className=getIntent().getStringExtra("class_name");
        childName=getIntent().getStringExtra("child_name");
        id=getIntent().getStringExtra("id");
        empId=getIntent().getStringExtra("EmpId");
        childId=getIntent().getStringExtra("ChildId");
        classId=getIntent().getStringExtra("ClassId");

        fromDate=getIntent().getStringExtra("AbsentFrom");
        if (fromDate!=null){
            fromDate=fromDate.replace("T00:00:00","");
        }

        toDate=getIntent().getStringExtra("AbsentTo");
        if (toDate!=null){
            toDate=toDate.replace("T00:00:00","");
        }

        absentNotes=getIntent().getStringExtra("absentNotes");
        createdBy=getIntent().getStringExtra("CreatedBy");
        createdOn=getIntent().getStringExtra("CreatedOn");
        lastChangedBy=getIntent().getStringExtra("LastChangedBy");
        lastChangedOn=getIntent().getStringExtra("LastChangedOn");
        absentType=getIntent().getStringExtra("AbsentType");
        status=getIntent().getStringExtra("Status");

        className_spin.setText(className);
        child_spin.setText(childName);
        absentType_spin.setText(absentType);
        from.setText(fromDate);
        to.setText(toDate);

        if(absentNotes.equalsIgnoreCase("null")){
            reason.setText("");
        }else {
            reason.setText(absentNotes);
        }

        ArrayAdapter adapter = new ArrayAdapter(UpdateAbsentList.this, android.R.layout.simple_list_item_1, absentTypeList);
        absentType_spin.setAdapter(adapter);

        absentType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);
//                if(absentType_spin.getText().toString().equalsIgnoreCase("Sick")){
//                    maxDates();
//                    from.getText().clear();
//                    to.getText().clear();
//                }else if(absentType_spin.getText().toString().equalsIgnoreCase("Other")){
//                   allDates();
//                    from.getText().clear();
//                    to.getText().clear();
//                }
//                else{
//                    minDates();
//                    from.getText().clear();
//                    to.getText().clear();
//                }
            }
        });
        dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.spinner_search_item_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        getClassList();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                className = className_spin.getText().toString();
                childName = child_spin.getText().toString();
                fromDate=from.getText().toString();
                toDate=to.getText().toString();
                absentType = absentType_spin.getText().toString();
                reason1=reason.getText().toString();

                if (Validate()) {
                    updateAbsentResponse();
                }
            }
        });

//        if(absentType_spin.getText().toString().equals("Sick")){
//            maxDates();
//        }else{
//            minDates();
//        }
        allDates();
    }

    private void allDates() {
        from.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAbsentList.this, (view, year1, month1, day1) -> {

                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;

                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                from.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);

                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        to.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAbsentList.this, (view,year1, month1, day1) -> {

                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                String date = day1 + "-" + month1 + "-" + year1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }
                to.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });
    }

    private void minDates() {

        from.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.MONTH,6);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAbsentList.this, (view, year1, month1, day1) -> {

                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;

                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                from.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);

                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker().setMaxDate(sixMonths.getTimeInMillis());
            datePickerDialog.show();
        });

        to.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.MONTH,6);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAbsentList.this, (view,year1, month1, day1) -> {

                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                String date = day1 + "-" + month1 + "-" + year1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }
                to.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker().setMaxDate(sixMonths.getTimeInMillis());
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });
    }

    private void maxDates() {
//        from.getText().clear();
//        to.getText().clear();

        from.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.DATE,-10);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAbsentList.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                from.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() +7 * 24 * 60 * 60 * 1000);
            datePickerDialog.getDatePicker().setMinDate(sixMonths.getTimeInMillis());
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        to.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.DATE,-10);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateAbsentList.this, (view, year1, month1, day1) -> {
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                String date = day1 + "-" + month1 + "-" + year1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                to.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() +7 * 24 * 60 * 60 * 1000);
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });
    }

    private void getChildList(String custId, String classId) {

        Utils.showProgressDialog(UpdateAbsentList.this);

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
        Call<String> call=api.childClass_dropdown(custId, classId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
               Utils.dismissProgressDialog();
                if (response.body()!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray=jsonObject.getJSONArray("model");
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            childId=jsonObject1.getString("childId");
                            childName=jsonObject1.getString("displayName");
                            child_Id.add(childId);
                            display_Name.add(childName);
//                            child_spin.setText("");
//                            getChildList(custId,classId);
                        }
//                        class_Name.add(0,"select Type");

                        ArrayAdapter adapter = new ArrayAdapter(UpdateAbsentList.this,android.R.layout.simple_dropdown_item_1line,display_Name);
                        child_spin.setAdapter(adapter);
                        child_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                childId=child_Id.get(i);

//                                classNameID=child_Id.get(i);
//                                if (classId==class_Id.get(i)){
//                                display_Name.clear();
//                                child_spin.setText("select child");
//                                getChildList(custId,classId);
//                                }
                          }
                        });

                        Utils.dismissProgressDialog();
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
                Toast.makeText(UpdateAbsentList.this, message, Toast.LENGTH_SHORT).show();
                finish();
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
            progress.setVisibility(View.GONE);

            if (response.body()!=null){
                try {
                    jsonObject  = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("model");
                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        classID=jsonObject1.getString("classId");
                        classNameSpin = jsonObject1.getString("className");
                        class_Id.add(classID);
                        class_Name.add(classNameSpin);
                    }
                    ArrayAdapter adapter = new ArrayAdapter(UpdateAbsentList.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                    className_spin.setAdapter(adapter);

                    className_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int v, long l) {
                            String selected = (String) adapterView.getItemAtPosition(v);

                            classId=class_Id.get(v);
                            display_Name.clear();
                            child_Id.clear();
                            child_spin.setText("");
                            getChildList(custId,classId);
                        }
                    });

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
            Toast.makeText(UpdateAbsentList.this, message, Toast.LENGTH_SHORT).show();
            finish();
        }
    });

}

    private void updateAbsentResponse() {
        progress.setVisibility(View.VISIBLE);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        VcareApi api = retrofit.create(VcareApi.class);
        updateAbsentModel.setId(id);
        updateAbsentModel.setEmpId(empId);
        updateAbsentModel.setChildId(childId);
        updateAbsentModel.setClassId(classId);
        updateAbsentModel.setCustId(custId);
        updateAbsentModel.setFrom(fromDate);
        updateAbsentModel.setTo(toDate);
        updateAbsentModel.setAbsentType(absentType);
        updateAbsentModel.setAbsentNotes(reason1);
        updateAbsentModel.setCreatedOn(createdOn);
        updateAbsentModel.setLastChangedOn(lastChangedOn);
        updateAbsentModel.setStatus(status);
//        Toast.makeText(UpdateAbsentList.this, ""+classId+absentType+childId, Toast.LENGTH_SHORT).show();
        Call<UpdateAbsentResponse> call= api.update_absent(id,"0",updateAbsentModel);
        call.enqueue(new Callback<UpdateAbsentResponse>() {
            @Override
            public void onResponse(Call<UpdateAbsentResponse> call, Response<UpdateAbsentResponse> response) {
                progress.setVisibility(View.VISIBLE);
                if (response.code()==200){
                    if (response.body().getMessage()!=null){
                        if (UpdateAbsentList.this!=null&&!UpdateAbsentList.this.isFinishing()){
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(UpdateAbsentList.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                            Intent intent = new Intent(UpdateAbsentList.this, ChildrenAbsentList.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();

                        }
                    }else{
                        Utils.showAlertDialog(UpdateAbsentList.this,response.body().getErrorMessage(),false);
                    }
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateAbsentResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(UpdateAbsentList.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(UpdateAbsentList.this,ChildrenAbsentList.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(UpdateAbsentList.this,ChildrenAbsentList.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean Validate() {

        if (!className()) {
            return false;
        } else if (!childName()) {
            return false;
        } else if (!from()) {
            return false;
        } else if (!to()){
            return false;
        } else if (!absentType()){
            return false;
        } else {
            return true;
        }

    }

    private boolean childName() {
        if (child_spin.getText().toString().isEmpty()) {
            child_spin.setError("Child name should not be empty");
            child_spin.requestFocus();
            return false;
        } else {
            return true;
        }
    }
    private boolean from() {
        if (from.getText().toString().isEmpty()) {
            from.setError("From date should not be empty");
            from.requestFocus();
            return false;
        } else {
            return true;
        }
    }
    private boolean to() {
        if (to.getText().toString().isEmpty()) {
            to.setError("To date should not be empty");
            to.requestFocus();
            return false;
        } else {
            return true;
        }
    }
    private boolean absentType() {
        if (absentType_spin.getText().toString().isEmpty()) {
            absentType_spin.setError("Absent type should not be empty");
            absentType_spin.requestFocus();
            return false;
        }
//        else if (absentType_spin.getText().toString().equalsIgnoreCase("Sick")) {
//            futureDates();
////            absentType_spin.setError("Absent type should not be empty");
////            absentType_spin.requestFocus();
////            Utils.showAlertDialog(UpdateAbsentList.this,"Please enter valid dates",false);
//            return false;
//        }
        else {
            return true;
        }
    }

    private boolean className() {
        if (className_spin.getText().toString().isEmpty()) {
            className_spin.setError("Class name should not be empty");
            className_spin.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}
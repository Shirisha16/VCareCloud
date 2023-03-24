package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.client.vcarecloud.Adapters.SpinnerAdapterForChildList;
import com.client.vcarecloud.Adapters.SpinnerAdapterForClassList;
import com.client.vcarecloud.models.AddAbsentModel;
import com.client.vcarecloud.models.AddAbsentResponse;
import com.client.vcarecloud.models.ChildAbsentModel;
import com.client.vcarecloud.models.ChildNameResponse;
import com.client.vcarecloud.models.GetChildList;
import com.client.vcarecloud.models.GetClassList;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddAbsent extends AppCompatActivity {
    AutoCompleteTextView className_spin, child_spin, absentType_spin;
//    MaterialTextView fromDate, toDate;
    boolean dateChecked = false;
    ArrayList<String> absentList = new ArrayList<>();
    String[] absentTypeNames = {"Sick", "Vacation", "other"};

    String className = "";
    String childName = "";
    String absentType = "";

    Dialog dialog;
    List<GetChildList> childList;
    List<GetClassList> classList;

    String from;
    String to;
    String childId, classId, custId;
    String status = "Active";
    AppCompatButton submit;
    RelativeLayout progress;
    UserDetails userDetails;
    TextInputEditText reason,fromDate, toDate;
    ImageView back;

    ArrayList<String> class_Name = new ArrayList<>();
    ArrayList<String> class_Id = new ArrayList<>();

    ArrayList<String> display_Name = new ArrayList<>();
    ArrayList<String> child_Id = new ArrayList<>();

    String absentNotes, EmpId = null, message, errorMessage;
    ChildAbsentModel childAbsentModel;

    String selected, createdOn, lastChangedOn,reason1;
    SpinnerAdapterForChildList adapterForChildList;
    SpinnerAdapterForClassList adapterForClassList;
    String classNameID = "", childNameId = "";
    ChildNameResponse childNameResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_absent);

        className_spin = findViewById(R.id.class_spin);
        child_spin = findViewById(R.id.child_spin);
        absentType_spin = findViewById(R.id.absent_spin);
        fromDate = findViewById(R.id.from1);
        reason=findViewById(R.id.reason1);
        toDate = findViewById(R.id.to1);
        back = findViewById(R.id.back);
        submit = findViewById(R.id.add);
        progress = findViewById(R.id.progress);

        userDetails = new UserDetails(AddAbsent.this);

        childNameResponse = new ChildNameResponse();

        createdOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        lastChangedOn = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        classList = new ArrayList<>();
        childList = new ArrayList<>();
        getClassList();

        childAbsentModel = new ChildAbsentModel();

        EmpId = userDetails.getEmpID();
        custId = userDetails.getCustId();
        classId = userDetails.getClassId();
        childId = userDetails.getChildId();
        absentNotes=userDetails.getAbsentNotes();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                className = className_spin.getText().toString();
                childName = child_spin.getText().toString();
                absentType = absentType_spin.getText().toString();
                from = fromDate.getText().toString();
                to = toDate.getText().toString();
                reason1=reason.getText().toString();
//                Toast.makeText(AddAbsent.this, ""+absentType_spin.getText().toString(), Toast.LENGTH_SHORT).show();

              if (Validate()){
                  addAbsentResponse();
              }
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(AddAbsent.this, android.R.layout.simple_list_item_1, absentTypeNames);
        absentType_spin.setAdapter(adapter);

        absentType_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selected = (String) adapterView.getItemAtPosition(i);

//                if(absentType_spin.getText().toString().equalsIgnoreCase("Sick")){
//                    maxDates();
//
//                    fromDate.getText().clear();
//                    toDate.getText().clear();
//                }else if(absentType_spin.getText().toString().equalsIgnoreCase("Other")){
//                    allDates();
//                    fromDate.getText().clear();
//                    toDate.getText().clear();
//                }else{
//                    minDates();
//                    fromDate.getText().clear();
//                    toDate.getText().clear();
//                }
         }
        });

        dialog = new Dialog(getApplicationContext());
        dialog.setContentView(R.layout.spinner_search_item_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")){
                    Intent intent =new Intent(AddAbsent.this,ChildrenAbsentList.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")){
                    Intent intent =new Intent(AddAbsent.this,ChildrenAbsentList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

//        if(absentType_spin.getText().toString().equalsIgnoreCase("Sick")){
//            futureDates();
//        }else{
            allDates();
//        }
    }
    private void allDates() {
        fromDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAbsent.this, (view, year1, month1, day1) -> {

                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;

                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                fromDate.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);

                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        toDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAbsent.this, (view,year1, month1, day1) -> {

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
                toDate.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
//            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });
    }

    private void minDates() {

        fromDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.MONTH,6);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAbsent.this, (view, year1, month1, day1) -> {

                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;

                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                fromDate.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);

                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker().setMaxDate(sixMonths.getTimeInMillis());
            datePickerDialog.show();
        });

        toDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.MONTH,6);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAbsent.this, (view,year1, month1, day1) -> {

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
                toDate.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.getDatePicker().setMaxDate(sixMonths.getTimeInMillis());
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });
    }

    private void maxDates() {

        fromDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.DATE,-10);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAbsent.this, (view, year1, month1, day1) -> {
//                datePickerDialog.add(calendar.DAY)
                month1 = month1 + 1;
                String formattedMonth = "" + month1;
                String formattedDayOfMonth = "" + day1;
                if (month1 < 10) {
                    formattedMonth = "0" + month1;
                }
                if (day1 < 10) {
                    formattedDayOfMonth = "0" + day1;
                }

                fromDate.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() +7 * 24 * 60 * 60 * 1000);
            datePickerDialog.getDatePicker().setMinDate(sixMonths.getTimeInMillis());
            //            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });

        toDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Calendar sixMonths=(Calendar) calendar.clone();
            sixMonths.add(Calendar.DATE,-10);

            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            final int month = calendar.get(Calendar.MONTH);
            final int year = calendar.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddAbsent.this, (view, year1, month1, day1) -> {
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

                toDate.setText(year1 + "-" + formattedMonth + "-" + formattedDayOfMonth);
                dateChecked = true;
            }, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() +7 * 24 * 60 * 60 * 1000);
            datePickerDialog.getDatePicker().setMinDate(sixMonths.getTimeInMillis());
//            datePickerDialog.getDatePicker();
            datePickerDialog.show();
        });
    }

    private void getChildList(String custId, String classId) {
        Utils.showProgressDialog(AddAbsent.this);
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
        Call<String> call = api.childClass_dropdown(custId, classId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Utils.dismissProgressDialog();
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            childId = jsonObject1.getString("childId");
                            childName = jsonObject1.getString("displayName");
                            child_Id.add(childId);
                            display_Name.add(childName);
//                            child_spin.setText("");
//                            getChildList(custId,classId);
                        }
//                        class_Name.add(0,"select Type");

                        ArrayAdapter adapter = new ArrayAdapter(AddAbsent.this, android.R.layout.simple_dropdown_item_1line, display_Name);
                        child_spin.setAdapter(adapter);
                        child_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                childId = child_Id.get(i);
//                                classNameID=child_Id.get(i);
//                                if (classId==class_Id.get(i)){
//                                display_Name.clear();
//                                child_spin.setText("select child");
//                                getChildList(custId,classId);
//                                }
//                                Toast.makeText(AddAbsent.this, "yes"+childId, Toast.LENGTH_SHORT).show();

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
                Toast.makeText(AddAbsent.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void getClassList() {
        Utils.showProgressDialog(AddAbsent.this);
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
                Utils.dismissProgressDialog();
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

                        ArrayAdapter adapter = new ArrayAdapter(AddAbsent.this, android.R.layout.simple_dropdown_item_1line, class_Name);
                        className_spin.setAdapter(adapter);
                        className_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String selected = (String) adapterView.getItemAtPosition(i);
                                classId = class_Id.get(i);
                                if (classId==class_Id.get(i)){
                                display_Name.clear();
                                child_Id.clear();
                                child_spin.setText("");
                                getChildList(custId, classId);
                                }
                           }
                        });

//                        HintSpinner<String> hintSpinner = new HintSpinner<>(
//                                sp_employee,
//                                // Default layout - You don't need to pass in any layout id, just your hint text and
//                                // your list data
//                                new HintAdapter<>(this, R.string.sp_emp_hint, Arrays.asList(class_Name)),
//                                new HintSpinner.Callback<String>() {
//                                    @Override
//                                    public void onItemSelected(int position, String itemAtPosition) {
//                                        // Here you handle the on item selected event (this skips the hint selected event)
//                                        empNames = empList.get(position).getFirstName()+ " "+empList.get(position).getLastName();
//                                        empRef = empList.get(position).getId();
//                                        //Toast.makeText(getContext(), "empName " + empNames + " empId " + empRef, Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                        hintSpinner.init();

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
                Toast.makeText(AddAbsent.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void addAbsentResponse() {
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

        AddAbsentModel addAbsentModel=new AddAbsentModel();
        addAbsentModel.setEmpId(String.valueOf(0));
        addAbsentModel.setChildId(childId);
        addAbsentModel.setClassId(classId);
        addAbsentModel.setCustId(custId);
        addAbsentModel.setAbsentType(absentType);
        addAbsentModel.setAbsentFrom(fromDate.getText().toString());
        addAbsentModel.setAbsentTo(toDate.getText().toString());
        addAbsentModel.setAbsentNotes(reason.getText().toString());
        addAbsentModel.setCreatedOn(createdOn);
        addAbsentModel.setLastChangedOn(lastChangedOn);
        addAbsentModel.setStatus(status);
//        Toast.makeText(AddAbsent.this, ""+absentType, Toast.LENGTH_SHORT).show();
        Call<AddAbsentResponse> call = api.add_absent("0", addAbsentModel);
        call.enqueue(new Callback<AddAbsentResponse>() {
            @Override
            public void onResponse(Call<AddAbsentResponse> call, Response<AddAbsentResponse> response) {
                if (response.code()==200){
                   if (response.body().getMessage()!=null){
                       message=response.body().getMessage();
                       errorMessage=response.body().getErrorMessage();
                       if (getApplicationContext() != null && !AddAbsent.this.isFinishing()) {
                           androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddAbsent.this);
                           builder.setMessage(response.body().getMessage());
                           builder.setCancelable(false);
                           builder.setPositiveButton("ok",(dialogInterface, i) -> {
                           Intent intent = new Intent(AddAbsent.this, ChildrenAbsentList.class);
                           startActivity(intent);
                           finish();

                           });
                           AlertDialog dialog= builder.create();
                           dialog.show();
                       }
                   } else {
                       Utils.showAlertDialog(AddAbsent.this,response.body().getErrorMessage(),false);
                   }
               }
               progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddAbsentResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(AddAbsent.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public static RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), value);
        return body;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(AddAbsent.this,ChildrenAbsentList.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(AddAbsent.this,ChildrenAbsentList.class);
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
    private boolean className() {
        if (className_spin.getText().toString().isEmpty()) {
            className_spin.setError("Class name should not be empty");
            className_spin.requestFocus();
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
        if (fromDate.getText().toString().isEmpty()) {
            fromDate.setError("From date should not be empty");
            fromDate.requestFocus();

            return false;
        } else {
            return true;
        }
    }
    private boolean to() {
        if (toDate.getText().toString().isEmpty()) {
            toDate.setError("To date should not be empty");
            toDate.requestFocus();

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
//        else if(absentType_spin.getText().toString().equalsIgnoreCase("--Select absent Type--")) {
//            Utils.showAlertDialog(AddAbsent.this, "Please select absent type", false);
//            return false;
//        } else if (absentType_spin.getText().toString().equalsIgnoreCase("Sick")){
//            futureDates();
//            return false;
//        }
        else {
            return true;
        }
    }
}



package com.client.vcarecloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.LoadDetails;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.InvoiceDetailsModel;
import com.client.vcarecloud.models.UserDetails;

import org.json.JSONArray;
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

public class InvoiceDetails extends AppCompatActivity implements LoadDetails {
    ImageView back;
    TextView chargeText,taxText,adjustmentText,discountText,paymentText,
              amountText;
    AppCompatTextView header;

    ArrayList<InvoiceDetailsModel> invoiceDetailsModelArrayList;
    String message,error;
    UserDetails userDetails;
    InvoiceDetailsModel invoiceDetailsModel;

    String headerid,custId,classname,invoiceNo,invoiceMonth,invoiceYear,childname,chargeAmount,
            discount,tax,adjustment,invoiceAmount,payAmount,latePayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back=findViewById(R.id.back);
        chargeText=findViewById(R.id.chargeable);
        taxText=findViewById(R.id.tax);
        adjustmentText=findViewById(R.id.adjustment);
        discountText=findViewById(R.id.discount);
        paymentText=findViewById(R.id.late);
        amountText=findViewById(R.id.amount);
        header=findViewById(R.id.header_title);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userDetails=new UserDetails(InvoiceDetails.this);

        custId=userDetails.getCustId();

        invoiceDetailsModel=new InvoiceDetailsModel();

        headerid=getIntent().getStringExtra("headerId");
        custId=getIntent().getStringExtra("custId");
        classname=getIntent().getStringExtra("classname");
        invoiceNo=getIntent().getStringExtra("invoiceNo");
        invoiceMonth=getIntent().getStringExtra("invoiceMonth");
        invoiceYear=getIntent().getStringExtra("invoiceYear");
        childname=getIntent().getStringExtra("childname");

        if(invoiceMonth.equals("1")){
            invoiceDetailsModel.setInvoiceMonth("Jan");
        }if(invoiceMonth.equals("2")){
            invoiceDetailsModel.setInvoiceMonth("Feb");
        }if(invoiceMonth.equals("3")){
            invoiceDetailsModel.setInvoiceMonth("Mar");
        }if(invoiceMonth.equals("4")){
            invoiceDetailsModel.setInvoiceMonth("April");
        }if(invoiceMonth.equals("5")){
            invoiceDetailsModel.setInvoiceMonth("May");
        }if(invoiceMonth.equals("6")){
            invoiceDetailsModel.setInvoiceMonth("Jun");
        }if(invoiceMonth.equals("7")){
            invoiceDetailsModel.setInvoiceMonth("Jul");
        }if(invoiceMonth.equals("8")){
            invoiceDetailsModel.setInvoiceMonth("Aug");
        }if(invoiceMonth.equals("9")){
            invoiceDetailsModel.setInvoiceMonth("Sep");
        }if(invoiceMonth.equals("10")){
            invoiceDetailsModel.setInvoiceMonth("Oct");
        }if(invoiceMonth.equals("11")){
            invoiceDetailsModel.setInvoiceMonth("Nov");
        }if(invoiceMonth.equals("12")){
            invoiceDetailsModel.setInvoiceMonth("Dec");
        }
        header.setText("Detailed Invoice of " +childname +" for " +invoiceDetailsModel.getInvoiceMonth() +"," +invoiceYear);

        chargeAmount=getIntent().getStringExtra("totalChargeAmount");
        discount=getIntent().getStringExtra("totalDiscountAmount");
        tax=getIntent().getStringExtra("totalTaxAmount");
        adjustment=getIntent().getStringExtra("totalAdjustmentAmount");
        invoiceAmount=getIntent().getStringExtra("invoiceAmount");
        payAmount=getIntent().getStringExtra("payAmount");
        latePayment=getIntent().getStringExtra("latePayment");

        chargeText.setText(chargeAmount);
        taxText.setText(tax);
        adjustmentText.setText(adjustment);
        discountText.setText(discount);
        paymentText.setText(latePayment);
        amountText.setText(invoiceAmount);

        invoiceDetailsModel = (InvoiceDetailsModel) getIntent().getSerializableExtra("list");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(InvoiceDetails.this,InvoiceList.class);
                startActivity(intent);
                finish();
            }
        });

        invoiceDetails();

    }

    private void invoiceDetails() {
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
        Call<String> call=api.invoice_details(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                InvoiceDetailsModel model = new InvoiceDetailsModel();

                                model.setHeaderId(jsonObject1.getString("headerId"));
                                model.setCustId(jsonObject1.getString("custId"));
//                                model.setClassname(jsonObject1.getString("classId"));
                                model.setClassname(jsonObject1.getString("classname"));
                                model.setInvoiceNo(jsonObject1.getString("invoiceNo"));
                                model.setInvoiceMonth(jsonObject1.getString("invoiceMonth"));
                                model.setInvoiceYear(jsonObject1.getString("invoiceYear"));
                                model.setInvoicePeriod_From(jsonObject1.getString("invoicePeriod_From"));
                                model.setInvoicePeriod_To(jsonObject1.getString("invoicePeriod_To"));

                                model.setInvoiceDate(jsonObject1.getString("invoiceDate"));
                                model.setInvoiceDueDate(jsonObject1.getString("invoiceDueDate"));
                                model.setPaymentDate(jsonObject1.getString("paymentDate"));
//                                model.setChildId(jsonObject1.getString("childId"));
                                model.setChildname(jsonObject1.getString("childname"));
                                model.setTotalChargeAmount(jsonObject1.getString("totalChargeAmount"));
                                model.setTotalDiscountAmount(jsonObject1.getString("totalDiscountAmount"));
                                model.setTotalTaxAmount(jsonObject1.getString("totalTaxAmount"));
                                model.setTotalAdjustmentAmount(jsonObject1.getString("totalAdjustmentAmount"));
                                model.setInvoiceAmount(jsonObject1.getString("invoiceAmount"));
                                model.setPayAmount(jsonObject1.getString("payAmount"));
                                model.setLatePayment(jsonObject1.getString("latePayment"));
//                                model.setStatus(jsonObject1.getString("status"));
//                                model.setChildId(jsonObject1.getString("child"));

                                invoiceDetailsModelArrayList.add(model);
                            }
                            message = jsonObject.getString("message");
                            error = jsonObject.getString("errorMessage");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(InvoiceDetails.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(InvoiceDetails.this,InvoiceList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMethodCallback() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logout) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(InvoiceDetails.this);
            builder1.setMessage("Do you want to Logout");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(InvoiceDetails.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            userDetails.setIsLogged(false);
                            userDetails.clearData();
                            startActivity(intent);

                            finish();
                        }
                    });
            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
        if (id == R.id.changePwd) {
            Intent intent = new Intent(InvoiceDetails.this, ChangePassword.class);
            intent.putExtra("userID", userDetails.getUserID());
            intent.putExtra("empID", userDetails.getEmpID());
            intent.putExtra("custId", userDetails.getCustId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
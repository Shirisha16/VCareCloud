package com.client.vcarecloud;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.UserDetails;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EmployeeInfo extends AppCompatActivity {
    ImageView back;
    CircularImageView customer_profilepic;
    RelativeLayout progress;
    AppCompatTextView header,firstName,lastName,gender,dob,designation,securityProfile,
            shift,dateOfJoining,dateOfLeaving;

    String firstName1,lastName1,gender1,dob1,designation1,securityProfile1,shift1,
            dateOfJoining1,dateOfLeaving1,employeeId,val_img,date1,custId;

    UserDetails userDetails;
//    ArrayList<EmpInfoModel> empInfoModelArrayList = new ArrayList<>();
//    EmpInfoModel empInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_info);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        back = findViewById(R.id.back);
        progress = findViewById(R.id.progressLayout);
        customer_profilepic=findViewById(R.id.customer_profilepic);

        header=findViewById(R.id.header_title);
        firstName=findViewById(R.id.firstName1);
        lastName=findViewById(R.id.lastName1);
        gender=findViewById(R.id.gender1);
        dob=findViewById(R.id.dob1);
        designation=findViewById(R.id.designation1);
        securityProfile=findViewById(R.id.security1);
        shift=findViewById(R.id.shift1);
        dateOfJoining=findViewById(R.id.date_of_joining1);
        dateOfLeaving=findViewById(R.id.date_of_leaving1);

        userDetails = new UserDetails(EmployeeInfo.this);

//        Date date = Calendar.getInstance().getTime();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        date1 = dateFormat.format(date);
        employeeId = getIntent().getStringExtra("employeeId");
        custId = getIntent().getStringExtra("custId");
        firstName1 = getIntent().getStringExtra("firstName");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeInfo.this, EmployeeList.class);
                startActivity(intent);
                finish();
            }
        });
        employeeDetails();
    }

    private void employeeDetails() {
        progress.setVisibility(View.VISIBLE);
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

        Call<String> call=api.employeeInfo(employeeId);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.setVisibility(View.GONE);
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        JSONArray jsonArray = jsonObject.getJSONArray("model");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        firstName1 = jsonObject1.getString("firstName");
                        lastName1=jsonObject1.getString("lastName");

                        gender1=jsonObject1.getString("sex");
                        if (gender1.equalsIgnoreCase("null")) {
                            gender.setText("N/A");
                        } else {
                            gender.setText(gender1);
                        }

                        val_img=jsonObject1.getString("photo");
                        if(!val_img.equalsIgnoreCase("null")) {
                            byte[] decodeString = Base64.decode(val_img, Base64.NO_PADDING);
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(decodeString)
                                    .error(R.drawable.person)
                                    .into(customer_profilepic);
                        }

                        dob1=jsonObject1.getString("dob");
                        if (dob1 != null) {
                            dob1 = dob1.replace("T00:00:00", "");
                        }

                        designation1=jsonObject1.getString("designation");
                        securityProfile1=jsonObject1.getString("securityProfile");
                        shift1=jsonObject1.getString("shift");

                        dateOfJoining1=jsonObject1.getString("joiningDate");
                        if (dateOfJoining1 != null) {
                            dateOfJoining1 = dateOfJoining1.replace("T00:00:00", "");
                        }

                        dateOfLeaving1=jsonObject1.getString("leavingDate");
                        if (dateOfLeaving1.equalsIgnoreCase("null")) {
                            dateOfLeaving.setText("N/A");
                        } else {
                            dateOfLeaving1 = dateOfLeaving1.replace("T00:00:00", "");
                            dateOfLeaving.setText(dateOfLeaving1);
                        }

                        header.setText(firstName1);
                        firstName.setText(firstName1);
                        lastName.setText(lastName1);
                        dob.setText(dob1);
                        designation.setText(designation1);
                        securityProfile.setText(securityProfile1);
                        shift.setText(shift1);
                        dateOfJoining.setText(dateOfJoining1);

                        String message = jsonObject1.getString("message");

                    } catch (Exception e) {
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
                Toast.makeText(EmployeeInfo.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            try {
                final Uri imageUri = data.getData();
                String imagepath = imageUri.getPath().toString();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(imageUri,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagepath = cursor.getString(columnIndex);
                cursor.close();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                customer_profilepic.setImageBitmap(selectedImage);

                val_img = BitMapToString(selectedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
            Intent intent =new Intent(EmployeeInfo.this,EmployeeList.class);
            startActivity(intent);
            finish();
        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
            Intent intent =new Intent(EmployeeInfo.this,EmployeeList.class);
            startActivity(intent);
            finish();
        }
    }
}
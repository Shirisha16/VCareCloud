package com.client.vcarecloud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.ChildInfoModel;
//import com.github.dhaval2404.imagepicker.ImagePicker;
//import com.google.android.gms.cast.framework.media.ImagePicker;
import com.client.vcarecloud.models.UserDetails;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChildInfo extends AppCompatActivity {
    ImageView back, profilePic;
    CircularImageView customer_profilepic;
    RelativeLayout progress;
    AppCompatTextView firstName, header, lastName, displayName, dob, gender, height,
            weight, eyeColor, hairStyle, religion;

    String firstName1, lastName1, displayNam1e, dob1, gender1, height1, weight1, eyeColor1,
            hairStyle1, religion1, childId, val_img, dates,childCode,message,error;

    UserDetails userDetails;
    ArrayList<ChildInfoModel> childInfoModelArrayList = new ArrayList<>();
    String custId;
    int RC=123;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_info);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        firstName = findViewById(R.id.firstName1);
        lastName = findViewById(R.id.lastName1);
        displayName = findViewById(R.id.displayName1);
        dob = findViewById(R.id.dob1);
        gender = findViewById(R.id.gender1);
        header = findViewById(R.id.header_title);
        height = findViewById(R.id.height1);
        weight = findViewById(R.id.weight1);
        eyeColor = findViewById(R.id.eyeColor1);
        hairStyle = findViewById(R.id.hairStyle1);
        religion = findViewById(R.id.religion1);
        back = findViewById(R.id.back);
        progress = findViewById(R.id.progressLayout);
        userDetails = new UserDetails(ChildInfo.this);
        customer_profilepic = findViewById(R.id.customer_profilepic);

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        dates = dateFormat.format(date);
        childId = getIntent().getStringExtra("childId");
        custId = getIntent().getStringExtra("custId");
        displayNam1e = getIntent().getStringExtra("childName");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDetails.getUserType().equalsIgnoreCase("Admin")) {
                    Intent intent = new Intent(ChildInfo.this, ChildrenList.class);
                    startActivity(intent);
                    finish();
                } else if (userDetails.getUserType().equalsIgnoreCase("User")) {
                    Intent intent = new Intent(ChildInfo.this, ChildrenList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        profileResponse();
    }

    private void profileResponse() {
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

        Call<String> call = api.child_info(childId);
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
                        firstName.setText(firstName1);

                        lastName1=jsonObject1.getString("lastName");
                        lastName.setText(lastName1);

                        displayNam1e=jsonObject1.getString("displayName");
                        header.setText(displayNam1e);
                        displayName.setText(displayNam1e);

                        gender1=jsonObject1.getString("sex");
                        if (gender1.equalsIgnoreCase("null")) {
                            gender.setText("N/A");
                        } else {
                            gender.setText(gender1);
                        }

                        dob1=jsonObject1.getString("dob");
                        if (dob1 != null) {
                            dob1 = dob1.replace("T00:00:00", "");
                            dob.setText(dob1);
                        }

                        height1=jsonObject1.getString("c_Height");
                        if(height1.equalsIgnoreCase("null")){
                            height.setText("N/A");
                        }else{
                            height.setText(height1);
                        }

                        weight1=jsonObject1.getString("c_Weight");
                        if(weight1.equalsIgnoreCase("null")){
                            weight.setText("N/A");
                        }else {
                            weight.setText(weight1);
                        }

                        eyeColor1=jsonObject1.getString("eyeColor");
                        if (eyeColor1.equalsIgnoreCase("null")){
                            eyeColor.setText("N/A");
                        }else{
                            eyeColor.setText(eyeColor1);
                        }

                        hairStyle1=jsonObject1.getString("hairStyle");
                        if(hairStyle1.equalsIgnoreCase("null")){
                            hairStyle.setText("N/A");
                        }else{
                            hairStyle.setText(hairStyle1);
                        }

                        religion1=jsonObject1.getString("religion");
                        if(religion1.equalsIgnoreCase("NA")){
                            religion.setText("N/A");
                        }else{
                            religion.setText(religion1);
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
                Toast.makeText(ChildInfo.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
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


        } catch (Exception e) {
            e.printStackTrace();

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
//        if (userDetails.getUserType().equalsIgnoreCase("Admin")){
//            Intent intent =new Intent(ChildInfo.this,ChildrenList.class);
//            startActivity(intent);
//            finish();
//        } else if (userDetails.getUserType().equalsIgnoreCase("User")){
//            Intent intent =new Intent(ChildInfo.this,ChildrenList.class);
//            startActivity(intent);
//            finish();
//        }
    }

}
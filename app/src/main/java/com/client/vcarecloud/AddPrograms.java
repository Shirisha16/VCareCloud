package com.client.vcarecloud;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.AddProgramsModel;
import com.client.vcarecloud.models.AddProgramsResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddPrograms extends AppCompatActivity {
    ImageView back;
    TextInputEditText programText, minAgeText, maxAgeText, capacityText;
    TextView photoFile;
    AppCompatButton addButton;
    TextView textPath;
    RelativeLayout progress_layout;

    String empid = "0", classid, custId, program, minAge, maxAge, capacity, photo;

    String message, errorMessage;
    UserDetails userDetails;
    String filePath="";
    int file_size;
    File file;
    RequestBody filename;
    MultipartBody.Part fileToUpload;

    private final static int SELECT_PHOTO = 12345;
    private final int EXTERNAL_STORAGE_PERMISSION_CODE=1587;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_programs);

        back = findViewById(R.id.back);
        programText = findViewById(R.id.program);
        minAgeText = findViewById(R.id.minAge);
        maxAgeText = findViewById(R.id.maxAge);
        capacityText = findViewById(R.id.capacity);
        photoFile = findViewById(R.id.photo);
        addButton = findViewById(R.id.add);
        textPath=findViewById(R.id.path);
        progress_layout = findViewById(R.id.progress);
        userDetails = new UserDetails(AddPrograms.this);

        custId = userDetails.getCustId();

        ActivityCompat.requestPermissions(AddPrograms.this, new String[]{READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        photoFile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, SELECT_PHOTO); }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                program = programText.getText().toString();
                minAge = minAgeText.getText().toString();
                maxAge = maxAgeText.getText().toString();
                capacity = capacityText.getText().toString();
//                photo = photoFile.getText().toString();

                if (Validate()) {
                    addPrograms();
                }
            }
        });
    }

    private void addPrograms() {
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
//        AddProgramsModel addProgramsModel=new AddProgramsModel();
//        addProgramsModel.setEmpid(empid);
//        addProgramsModel.setCustId(custId);
//        addProgramsModel.setClassName(program);
//        addProgramsModel.setMinAge(minAge);
//        addProgramsModel.setMaxAge(maxAge);
//        addProgramsModel.setCapacity(capacity);

        if (file != null) {
            if (!filePath.isEmpty()){

                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//                extension = filePath.substring(filePath.lastIndexOf("."));
            }
        }

        Map<String, RequestBody> map = new HashMap<>();
        map.put("EmpId", toRequestBody(empid));
//        map.put("classId", UserDetails.toRequestBody("11"));
        map.put("custId", toRequestBody(custId));
        map.put("ClassName",toRequestBody(program));
        map.put("MinAgeLimit", toRequestBody(minAge));
        map.put("MaxAgeLimit", toRequestBody(maxAge));
        map.put("Capacity",toRequestBody(capacity));

        Call<AddProgramsResponse> call = api.add_programs("0", fileToUpload, filename, map);
        call.enqueue(new Callback<AddProgramsResponse>() {
            @Override
            public void onResponse(Call<AddProgramsResponse> call, Response<AddProgramsResponse> response) {
             if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        errorMessage = response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddPrograms.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddPrograms.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(AddPrograms.this, Programs.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } else {
                        Utils.showAlertDialog(AddPrograms.this, response.body().getErrorMessage(), false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AddProgramsResponse> call, Throwable t) {
                Toast.makeText(AddPrograms.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream inputStream=null;
        try {
            if (resultCode == RESULT_OK && data != null) {
                Uri path = data.getData();
                inputStream = AddPrograms.this.getContentResolver().openInputStream(path);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);

                String encodedCode = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
                filePath = PathUtil.getPathFromUri(getApplicationContext(), path);
                if (filePath == null) {
                    Toast.makeText(AddPrograms.this, "null", Toast.LENGTH_SHORT).show();
                } else {

                    file = new File(filePath);
                    file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                }
                photoFile.setText("Change File");
                textPath.setVisibility(View.VISIBLE);
                textPath.setText(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
        return body;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean Validate() {
        if (!program_name()) {
            return false;
        } else if (!min_age()) {
            return false;
        } else if (!max_age()) {
            return false;
        } else if (!capacity1()) {
            return false;
        } else if(!fileSize()){
            return false;
        } else {
            return true;
        }
    }

    private boolean fileSize() {
        if(file_size > 2000){
            Utils.showAlertDialog(AddPrograms.this,"File size should not be more then 2MB",false);
            return false;
        }else {
            return true;
        }
    }

    private boolean program_name() {
        String str = programText.getText().toString();
        if (programText.getText().toString().isEmpty()) {
            programText.setError("Class Name should not be empty");
            programText.requestFocus();
            return false;
        } else if (str.length() > 0 && str.startsWith(" ")) {
            programText.setText(programText.getText().toString().trim());
            programText.setSelection(programText.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean min_age() {
        String str = minAgeText.getText().toString();
        if (minAgeText.getText().toString().isEmpty()) {
            minAgeText.setError("Min-age should not be empty");
            minAgeText.requestFocus();
            return false;
        } else if (str.length() > 0 && str.startsWith(" ")) {
            minAgeText.setText(minAgeText.getText().toString().trim());
            minAgeText.setSelection(minAgeText.getText().length());
            return false;
        } else if (str.length() > 0 && str.startsWith(".")) {
            minAgeText.setError("Min-age should not be point");
            minAgeText.setText(minAgeText.getText().toString().trim());
            minAgeText.setSelection(minAgeText.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean max_age() {
        String str = maxAgeText.getText().toString();
        if (maxAgeText.getText().toString().isEmpty()) {
            maxAgeText.setError("Max-age should not be empty");
            maxAgeText.requestFocus();
            return false;
        } else if (str.length() > 0 && str.startsWith(" ")) {
            maxAgeText.setText(maxAgeText.getText().toString().trim());
            maxAgeText.setSelection(maxAgeText.getText().length());
            return false;
        } else if (str.length() > 0 && str.startsWith(".")) {
            maxAgeText.setError("Max-age should not be point");
            maxAgeText.setText(maxAgeText.getText().toString().trim());
            maxAgeText.setSelection(maxAgeText.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean capacity1() {
        String str = capacityText.getText().toString();
        if (capacityText.getText().toString().isEmpty()) {
            capacityText.setError("Capacity should not be empty");
            capacityText.requestFocus();
            return false;
        } else if (str.length() > 0 && str.startsWith(" ")) {
            capacityText.setText(capacityText.getText().toString().trim());
            capacityText.setSelection(capacityText.getText().length());
            return false;
        } else if (str.length() > 0 && str.startsWith(".")) {
            capacityText.setError("Please enter a number");
            capacityText.setText(capacityText.getText().toString().trim());
            capacityText.setSelection(capacityText.getText().length());
            return false;
        } else {
            return true;
        }
    }
}
package com.client.vcarecloud;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.client.vcarecloud.Adapters.ProgramsAdapter;
import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.DiscountListModel;
import com.client.vcarecloud.models.ProgramModel;
import com.client.vcarecloud.models.UpdateDiscountModel;
import com.client.vcarecloud.models.UpdateDiscountResponse;
import com.client.vcarecloud.models.UpdateProgramModel;
import com.client.vcarecloud.models.UpdateProgramResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdatePrograms extends AppCompatActivity {
    ImageView back;
    TextInputEditText programText,minAgeText,maxText,capacityText;
    TextView photo,textPath;
    AppCompatButton editButton;
    RelativeLayout progress_layout;

    UserDetails userDetails;
    String message,error;

    String id,classId,custId,className,minAge,maxAge,capacity,photoFile,img;
    String filePath="";
    int file_size;
    private String encodedCode;
    File file;
    RequestBody filename;
    MultipartBody.Part fileToUpload;

    private final static int SELECT_PHOTO = 12345;
    private final int EXTERNAL_STORAGE_PERMISSION_CODE=1587;

    ProgramModel programModel;
    UpdateProgramModel updateProgramModel;
    UpdateProgramResponse updateProgramResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_programs);

        back=findViewById(R.id.back);
        programText=findViewById(R.id.program);
        minAgeText=findViewById(R.id.minAge);
        maxText=findViewById(R.id.maxAge);
        capacityText=findViewById(R.id.capacity);
        photo=findViewById(R.id.photo);
        editButton=findViewById(R.id.edit);
        textPath=findViewById(R.id.path);
        progress_layout=findViewById(R.id.progress);

        programModel=new ProgramModel();
        updateProgramModel=new UpdateProgramModel();
        updateProgramResponse=new UpdateProgramResponse();

        userDetails=new UserDetails(UpdatePrograms.this);

        programModel=(ProgramModel) getIntent().getSerializableExtra("list");

        custId=userDetails.getCustId();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(UpdatePrograms.this,Programs.class);
//                intent.putExtra("list",programModel);
//                startActivity(intent);
//                finish();
                onBackPressed();
            }
        });

        ActivityCompat.requestPermissions(UpdatePrograms.this, new String[]{READ_EXTERNAL_STORAGE},
                EXTERNAL_STORAGE_PERMISSION_CODE);
//        img=getIntent().getStringExtra("photo");

//        if (getIntent().getExtras() != null){
            id=getIntent().getStringExtra("id");
            classId=getIntent().getStringExtra("classId");
            custId=getIntent().getStringExtra("custId");
            className=getIntent().getStringExtra("className");
            minAge=getIntent().getStringExtra("minAge");
            maxAge=getIntent().getStringExtra("maxAge");
            capacity=getIntent().getStringExtra("capacity");
            photoFile=getIntent().getStringExtra("photo");
//            String s = getIntent().getStringExtra("photo");
//            Uri path = Uri.parse(s);
//            if (path!=null)
//                filePath = PathUtil.getPathFromUri(getApplicationContext(), path);
//            if (filePath == null) {
//                Toast.makeText(UpdatePrograms.this, "null", Toast.LENGTH_SHORT).show();
//            } else {
//
//                file = new File(filePath);
//                file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
//            }
            programText.setText(className);
            minAgeText.setText(minAge);
            maxText.setText(maxAge);
            capacityText.setText(capacity);
            if(photoFile.equalsIgnoreCase("null")){
                photo.setText("Select File");
            }else {
                photo.setText("Change File");
                Toast.makeText(UpdatePrograms.this, "File already exist...", Toast.LENGTH_SHORT).show();
            }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                className=programText.getText().toString();
                minAge=minAgeText.getText().toString();
                maxAge=maxText.getText().toString();
                capacity=capacityText.getText().toString();
                photoFile=photo.getText().toString();

                if(Validate()) {
                    updateProgram();
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, SELECT_PHOTO);
            }
        });
    }

    private void updateProgram() {
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

        if (file != null) {
            if (!filePath.isEmpty()){

                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
//                extension = filePath.substring(filePath.lastIndexOf("."));
            }
        }
//        updateProgramModel.setPhoto(photoFile);

       Map<String, RequestBody> map = new HashMap<>();
//        map.put("id", UserDetails.toRequestBody(id));
        map.put("EmpId", UserDetails.toRequestBody("0"));
        map.put("classId", UserDetails.toRequestBody(classId));
        map.put("custId", UserDetails.toRequestBody(custId));
        map.put("ClassName",UserDetails.toRequestBody(className));
        map.put("MinAgeLimit", UserDetails.toRequestBody(minAge));
        map.put("MaxAgeLimit", UserDetails.toRequestBody(maxAge));
        map.put("Capacity",UserDetails.toRequestBody(capacity));

        Call<UpdateProgramResponse> call=api.update_program(classId,"0",fileToUpload,filename,map);
        call.enqueue(new Callback<UpdateProgramResponse>() {
            @Override
            public void onResponse(Call<UpdateProgramResponse> call, Response<UpdateProgramResponse> response) {
              if (response.code() == 200) {
                    if (response.body().getMessage() != null) {
                        message = response.body().getMessage();
                        error = response.body().getErrorMessage();
                        if (UpdatePrograms.this != null && !UpdatePrograms.this.isFinishing()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UpdatePrograms.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok", (dialogInterface, i) -> {
                                Intent intent = new Intent(UpdatePrograms.this, Programs.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdatePrograms.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UpdateProgramResponse> call, Throwable t) {
                Toast.makeText(UpdatePrograms.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream inputStream=null;
        try {
            if (resultCode != Activity.RESULT_CANCELED) {
                Uri path = data.getData();
                inputStream = UpdatePrograms.this.getContentResolver().openInputStream(path);
                byte[] pdfInBytes = new byte[inputStream.available()];
                inputStream.read(pdfInBytes);

                encodedCode = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);
                if (path!=null)
                    filePath = PathUtil.getPathFromUri(getApplicationContext(), path);
                if (filePath == null) {
                    Toast.makeText(UpdatePrograms.this, "null", Toast.LENGTH_SHORT).show();
                } else {

                    file = new File(filePath);
                    file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                }
                photo.setText("Change File");
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
        }else if (!max_age()) {
            return false;
        }else if (!capacity1()) {
            return false;
        } else if(!fileSize()){
            return false;
        }else {
            return true;
        }
    }

    private boolean fileSize() {
        if(file_size > 2000){
            Utils.showAlertDialog(UpdatePrograms.this,"File size should not be more then 2MB",false);
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
        } else if(str.length() > 0 && str.startsWith(" ")) {
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
        } else if(str.length() > 0 && str.startsWith(" ")) {
            minAgeText.setText(minAgeText.getText().toString().trim());
            minAgeText.setSelection(minAgeText.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            minAgeText.setError("Min-age should not be point");
            minAgeText.setText(minAgeText.getText().toString().trim());
            minAgeText.setSelection(minAgeText.getText().length());
            return false;
        } else {
            return true;
        }
    }

    private boolean max_age() {
        String str = maxText.getText().toString();
        if (maxText.getText().toString().isEmpty()) {
            maxText.setError("Max-age should not be empty");
            maxText.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            maxText.setText(maxText.getText().toString().trim());
            maxText.setSelection(maxText.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            maxText.setError("Max-age should not be point");
            maxText.setText(maxText.getText().toString().trim());
            maxText.setSelection(maxText.getText().length());
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
        } else if(str.length() > 0 && str.startsWith(" ")) {
            capacityText.setText(capacityText.getText().toString().trim());
            capacityText.setSelection(capacityText.getText().length());
            return false;
        }else if(str.length() > 0 && str.startsWith(".")) {
            capacityText.setError("Please enter a number");
            capacityText.setText(capacityText.getText().toString().trim());
            capacityText.setSelection(capacityText.getText().length());
            return false;
        } else {
            return true;
        }
    }
}
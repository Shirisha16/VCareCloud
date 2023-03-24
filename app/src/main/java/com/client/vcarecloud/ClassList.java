package com.client.vcarecloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.GetClassList;
import com.client.vcarecloud.models.UserDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ClassList extends AppCompatActivity {
    ImageView back;
    AutoCompleteTextView classType_spin;
    AppCompatButton submit;

    String classId,className,custId;

    UserDetails userDetails;

    ArrayList<String> class_Name = new ArrayList<>();
    ArrayList<String> class_Id = new ArrayList<>();

    List<GetClassList> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        back=findViewById(R.id.back);
        classType_spin=findViewById(R.id.class_spin);
        submit=findViewById(R.id.submit);

        userDetails=new UserDetails(ClassList.this);

        custId=userDetails.getCustId();
        classId=userDetails.getClassId();

        classList = new ArrayList<>();

        custId=getIntent().getStringExtra("custId");
        classId=getIntent().getStringExtra("classId");

        getClassList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassList.this, AddAdditionalCharges.class);
                startActivity(intent);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                className=classType_spin.getText().toString();

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

                        ArrayAdapter adapter = new ArrayAdapter(ClassList.this, android.R.layout.simple_dropdown_item_1line, class_Name);
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
//                progress_layout.setVisibility(View.GONE);
                String message = "";
                if (t instanceof UnknownHostException) {
                    message = "No internet connection!";
                } else {
                    message = "Something went wrong! try again";
                }
                Toast.makeText(ClassList.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

//    private boolean Validate() {
//        if (!class_name()) {
//            return false;
//        }
//        else {
//            return true;
//        }
//    }
//
//    private boolean class_name() {
//    }
}
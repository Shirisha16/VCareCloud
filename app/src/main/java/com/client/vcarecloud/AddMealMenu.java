package com.client.vcarecloud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.MenuModelMeal;
import com.client.vcarecloud.models.MealMenuResponse;
import com.client.vcarecloud.models.MealMenuModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddMealMenu extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText food;
    RadioButton veg,nonVeg;
    AppCompatButton add;

    UserDetails userDetails;
    MealMenuModel mealMenuModel;

    String empId,custId,lookupId,lookupName,lookupType,lookupCategory;
    String message, errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal_menu);

        back=findViewById(R.id.back);
        progress_layout=findViewById(R.id.progress);
        food=findViewById(R.id.menu);
        veg=findViewById(R.id.veg);
        nonVeg=findViewById(R.id.nonveg);
        add=findViewById(R.id.add);

        userDetails = new UserDetails(AddMealMenu.this);
        mealMenuModel=new MealMenuModel();

        custId=userDetails.getCustId();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupName = food.getText().toString();

                if(veg.isChecked()){
                    lookupCategory = veg.getText().toString();
                }else {
                    lookupCategory = nonVeg.getText().toString();
                }
                if (Validate()) {
                    addMenu();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMealMenu.this, MealMenu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addMenu() {
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
        MenuModelMeal addMenuModel=new MenuModelMeal();
        addMenuModel.setEmpid(empId);
        addMenuModel.setCustId(custId);
        addMenuModel.setLookupId(lookupId);
        addMenuModel.setLookupName(lookupName);
        addMenuModel.setLookupType(lookupType);
        addMenuModel.setLookupCategory(lookupCategory);

        Call<MealMenuResponse> call=api.addMenu("0",addMenuModel);
        call.enqueue(new Callback<MealMenuResponse>() {
            @Override
            public void onResponse(Call<MealMenuResponse> call, Response<MealMenuResponse> response) {
                if(response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        errorMessage=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !AddMealMenu.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(AddMealMenu.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(AddMealMenu.this, MealMenu.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(AddMealMenu.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MealMenuResponse> call, Throwable t) {
                Toast.makeText(AddMealMenu.this, "Fail", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(AddMealMenu.this, MealMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private boolean Validate() {
        if (!lookup_name()) {
            return false;
        }
        else if(!menu_type()){
            return  false;
        } else {
            return true;
        }
    }

    private boolean lookup_name() {
        String str = food.getText().toString();
        if (food.getText().toString().isEmpty()) {
            food.setError("Food Name should not be empty");
            food.requestFocus();
            return false;
        } else if(str.length() > 0 && str.startsWith(" ")) {
            food.setText(food.getText().toString().trim());
            food.setSelection(food.getText().length());
            return false;
        }
        else {
            return true;
        }
    }

    private boolean menu_type() {
        if(veg.isChecked()== false && nonVeg.isChecked()==false){
//            mealtype.setError("Please select one of these options");
//            mealtype.requestFocus();
//            Toast.makeText(AddMealMenu.this, "Please select one of these options", Toast.LENGTH_SHORT).show();
            Utils.showAlertDialog(AddMealMenu.this,"Please select Meal Type",false);
            return false;
        }else{
            return true;
        }
    }
}
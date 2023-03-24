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
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.models.LookupTypeModel;
import com.client.vcarecloud.models.MenuModelMeal;
import com.client.vcarecloud.models.MealMenuResponse;
import com.client.vcarecloud.models.MealMenuModel;
import com.client.vcarecloud.models.UpdateLookupTypeModel;
import com.client.vcarecloud.models.UpdateLookupTypeResponse;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateMealMenu extends AppCompatActivity {
    ImageView back;
    RelativeLayout progress_layout;
    TextInputEditText food;
    RadioButton veg,nonVeg;
    AppCompatButton edit;

    UserDetails userDetails;
    String message,error;

    String id,empId,custId,lookupId,lookupName,lookupType,lookupCategory;

    MealMenu mealMenu;
    MealMenuModel mealMenuModel;
    MealMenuResponse mealMenuResponse;
    MenuModelMeal menuModelMeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_meal_menu);

        back=findViewById(R.id.back);
        progress_layout=findViewById(R.id.progress);
        food=findViewById(R.id.menu);
        veg=findViewById(R.id.veg);
        nonVeg=findViewById(R.id.nonveg);
        edit=findViewById(R.id.edit);

        mealMenu=new MealMenu();
        mealMenuModel=new MealMenuModel();
        mealMenuResponse =new MealMenuResponse();
        menuModelMeal =new MenuModelMeal();

        mealMenuModel=(MealMenuModel) getIntent().getSerializableExtra("list");

        userDetails=new UserDetails(UpdateMealMenu.this);

        id=getIntent().getStringExtra("id");
        empId=getIntent().getStringExtra("EmpId");
        custId=getIntent().getStringExtra("custId");
        lookupId=getIntent().getStringExtra("lookupsId");
        lookupName=getIntent().getStringExtra("lookupName");
        lookupType=getIntent().getStringExtra("lookupType");
        lookupCategory=getIntent().getStringExtra("lookupCategory");

        food.setText(lookupName);
        if(lookupCategory.equalsIgnoreCase("veg")){
            veg.setChecked(true);
        }else{
            nonVeg.setChecked(true);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateMealMenu.this,MealMenu.class);
                intent.putExtra("list",mealMenuModel);
                startActivity(intent);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lookupName=food.getText().toString();
                if(veg.isChecked()){
                    lookupCategory = veg.getText().toString();
                }else {
                    lookupCategory = nonVeg.getText().toString();
                }
                if (validate()) {
                    updateMenu();
                }
            }
        });
    }

    private void updateMenu() {
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
        menuModelMeal.setId(id);
        menuModelMeal.setEmpid(empId);
        menuModelMeal.setCustId(custId);
        menuModelMeal.setLookupId(lookupId);
        menuModelMeal.setLookupName(lookupName);
        menuModelMeal.setLookupType(lookupType);
        menuModelMeal.setLookupCategory(lookupCategory);

        Call<MealMenuResponse> call= api.update_Menu(id,"0", menuModelMeal);
        call.enqueue(new Callback<MealMenuResponse>() {
            @Override
            public void onResponse(Call<MealMenuResponse> call, Response<MealMenuResponse> response) {
                if(response.code()==200){
                    if (response.body().getMessage()!=null){
                        message=response.body().getMessage();
                        error=response.body().getErrorMessage();
                        if (getApplicationContext() != null && !UpdateMealMenu.this.isFinishing()) {
                            androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(UpdateMealMenu.this);
                            builder.setMessage(response.body().getMessage());
                            builder.setCancelable(false);
                            builder.setPositiveButton("ok",(dialogInterface, i) -> {
                                Intent intent = new Intent(UpdateMealMenu.this, MealMenu.class);
                                startActivity(intent);
                                finish();
                            });
                            AlertDialog dialog= builder.create();
                            dialog.show();
                        }
                    }else{
                        Utils.showAlertDialog(UpdateMealMenu.this,response.body().getErrorMessage(),false);
                    }
                }
                progress_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<MealMenuResponse> call, Throwable t) {
                Toast.makeText(UpdateMealMenu.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        if (!lookup_name()) {
            return false;
        }
        else if(!menu_type()){
            return  false;
        }
        else {
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
          Utils.showAlertDialog(UpdateMealMenu.this,"Please select Meal Type",false);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent =new Intent(UpdateMealMenu.this,MealMenu.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
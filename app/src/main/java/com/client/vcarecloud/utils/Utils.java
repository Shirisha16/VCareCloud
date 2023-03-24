package com.client.vcarecloud.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String ALERT_NETWORK_NOT_FOUND = "Please check your connection and try again.";
    public static Bitmap BitMap;
    public static String userProfile="";
    public static String userName="";
    public static List<Integer> skillId=new ArrayList<>();
    public static String UserRole;
    public static String SelectedEmplyeId,LoginType;
    public static String AddChildMedicationResult="";
    public static String AddImmunization="";
    public static String SuccessMessage="";
    public static ProgressDialog progressDialog;

    @SuppressWarnings("deprecation")
    public static void showAlertDialog(Context context, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dl, int which) {
                dl.dismiss();

            }
        });
        alertDialog.show();
    }

    public static void showProgressDialog(Context context){

        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog(){
        progressDialog.dismiss();
    }

    public static void preventTwoClick(final View view){
        view.setEnabled(false);
        view.postDelayed(
                ()-> view.setEnabled(true),
                600
        );
    }

    public static void preventClick(final View view){
        view.setEnabled(false);
        view.postDelayed(
                ()-> view.setEnabled(true),
                1600
        );
    }
}

package com.client.vcarecloud.models;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class UserDetails {
    String name1, number1, wallet1;
    Boolean isLoged;
    Context context;
    String user_ID = "userID",emp_ID="empID",cust_Id="custId",first_Name="firstName",
            last_Name = "lastName", Email = "email", user_Type = "userType",
            security_id = "securityid", daycare_Name = "daycareName",Photo="photo";

    public static final String KEY_Message = "message";
    private static final String KEY_LAST_ACTIVITY = "last_activity";

    private static SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String Message="message";

    String isLogged = "islogged",Password="Password",AbsentId="absentId",ChildId="childId",
            ClassId="classId",AbsentNotes="absentNotes",EmpId="employeeId",
            ErrorMessage="errorMessage",Shift_Id="ShiftId",Shift_name="ShiftName",
            Duration="ShiftDuration",Shiftstatus="ShiftStatus",ShiftName="shift";

    String taxId="taxesId",eventId="eventID",typeId="typeId",campId="campId",
            lookupId="lookupsId", lookupType="lookupType",securityProfileName="securityProfileName";

    String shiftEndDate="shiftEndDate";

    public UserDetails(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static RequestBody toRequestBody(String value) {
        RequestBody body = RequestBody.create(MediaType.parse("text/plain"), value);
//        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), value);
        return body;
    }

    public String getTaxId() {
        return sharedPreferences.getString(taxId,"");
    }

    public void setTaxId(String tax_id) {
        editor.putString(taxId, tax_id);
        editor.commit();
    }

    public String getEventId() {
        return sharedPreferences.getString(eventId,"");
    }

    public void setEventId(String eventid) {
        editor.putString(eventId,eventid);
        editor.commit();
    }

    public String getTypeId() {
        return sharedPreferences.getString(typeId,"");
    }

    public void setTypeId(String typeid) {
        editor.putString(typeId,typeid);
        editor.commit();
    }

    public String getCampId() {
        return sharedPreferences.getString(campId,"");
    }

    public void setCampId(String camp_id) {
        editor.putString(campId, camp_id);
        editor.commit();
    }

    public String getLookupId() {
        return sharedPreferences.getString(lookupId,"");
    }

    public void setLookupId(String lookupid) {
        editor.putString(lookupId,lookupid);
        editor.commit();
    }

    public String getSecurityProfileName() {
        return sharedPreferences.getString(securityProfileName,"");
    }

    public void setSecurityProfileName(String profileName) {
        editor.putString(securityProfileName,profileName);
        editor.commit();
    }

    public String getLookupType() {
        return sharedPreferences.getString(lookupType,"");
    }

    public void setLookupType(String lookuptype) {
        editor.putString(lookupType,lookuptype);
        editor.commit();
    }

    public String getShiftName() {
        return sharedPreferences.getString(ShiftName,"");
    }

    public void setShiftName(String shiftName) {
        editor.putString(ShiftName,shiftName);
        editor.commit();
    }

    public String getDuration() {
        return sharedPreferences.getString(Duration,"");
    }

    public void setDuration(String duration) {
        editor.putString(Duration,duration);
        editor.commit();
    }
    public String getShiftstatus() {
        return sharedPreferences.getString(Shiftstatus,"");
    }

    public void setShiftstatus(String shiftstatus) {
        editor.putString(Shiftstatus,shiftstatus);
        editor.commit();
    }
    public String getShift_Id() {
        return sharedPreferences.getString(Shift_Id, "");
    }

    public void setShift_Id(String shift_Id) {
        editor.putString(Shift_Id, shift_Id);
        editor.commit();
    }
    public String getShift_name() {
        return sharedPreferences.getString(Shift_name,"");
    }

    public void setShift_name(String shift_name) {
        editor.putString(Shift_name,shift_name);
        editor.commit();
    }
    public String getMessage() {
        return sharedPreferences.getString(Message, "");
    }

    public void setMessage(String message) {
        editor.putString(Message, message);
        editor.commit();
    }

    public String getErrorMessage() {
        return sharedPreferences.getString(ErrorMessage, "");
    }

    public void setErrorMessage(String errorMessage) {
        editor.putString(ErrorMessage, errorMessage);
        editor.commit();
    }

    public String getShiftEndDate() {
        return sharedPreferences.getString(shiftEndDate,"");
    }

    public void setShiftEndDate(String shiftendDate) {
        editor.putString(shiftEndDate, shiftendDate);
        editor.commit();
    }

    public String getEmpId() {
        return sharedPreferences.getString(EmpId, "");
    }

    public void setEmpId(String empId) {
        editor.putString(EmpId, empId);
        editor.commit();
    }

    public String getAbsentNotes() {
        return sharedPreferences.getString(AbsentNotes, "");
    }

    public void setAbsentNotes(String absentNotes) {
        editor.putString(AbsentNotes, absentNotes);
        editor.commit();
    }

    public String getAbsentId() {
        return sharedPreferences.getString(AbsentId, "");
    }

    public void setAbsentId(String absentId) {
        editor.putString(AbsentId, absentId);
        editor.commit();
    }

    public String getChildId() {
        return sharedPreferences.getString(ChildId, "");
    }

    public void setChildId(String childId) {
        editor.putString(ChildId, childId);
        editor.commit();
    }

    public String getClassId() {
        return sharedPreferences.getString(ClassId, "");
    }

    public void setClassId(String classId) {
        editor.putString(ClassId, classId);
        editor.commit();
    }

    public String getPassword() {
        return sharedPreferences.getString(Password, "");
    }

    public void setPassword(String password) {
        editor.putString(Password, password);
        editor.commit();
    }

    public String getPhoto() {
        return sharedPreferences.getString(Photo, "");
    }

    public void setPhoto(String photo) {
        editor.putString(Photo, photo);
        editor.commit();
    }





    public String getUserID() {
        return sharedPreferences.getString(user_ID, "");
    }

    public void setUserID(String userID) {
        editor.putString(user_ID, userID);
        editor.commit();
    }

    public String getEmpID() {
        return sharedPreferences.getString(emp_ID, "");
    }

    public void setEmpID(String empID) {
        editor.putString(emp_ID, empID);
        editor.commit();
    }

    public String getCustId() {
        return sharedPreferences.getString(cust_Id, "");
    }

    public void setCustId(String custId) {
        editor.putString(cust_Id, custId);
        editor.commit();
    }

    public String getFirstName() {
        return sharedPreferences.getString(first_Name, "");
    }

    public void setFirstName(String firstName) {
        editor.putString(first_Name, firstName);
        editor.commit();
    }

    public String getLastName() {
        return sharedPreferences.getString(last_Name, "");
    }

    public void setLastName(String lastName) {
        editor.putString(last_Name, lastName);
        editor.commit();
    }

    public String getEmail() {
        return sharedPreferences.getString(Email, "");
    }

    public void setEmail(String email) {
        editor.putString(Email, email);
        editor.commit();
    }

    public String getUserType() {
        return sharedPreferences.getString(user_Type, "");
    }

    public void setUserType(String userType) {
        editor.putString(user_Type, userType);
        editor.commit();
    }

    public String getSecurityid() {
        return sharedPreferences.getString(security_id, "");
    }

    public void setSecurityid(String securityid) {
        editor.putString(security_id, securityid);
        editor.commit();
    }

    public String getDaycareName() {
        return sharedPreferences.getString(daycare_Name, "");
    }

    public void setDaycareName(String daycareName) {
        editor.putString(daycare_Name, daycareName);
        editor.commit();
    }



    public boolean getIsLogged() {
        return sharedPreferences.getBoolean(isLogged,false);
    }

    public void setIsLogged(boolean isLogged1) {
        editor.putBoolean(isLogged,isLogged1);
        editor.apply();
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void clearData() {
        editor.clear();
        editor.apply();
    }

    public boolean isNetworkConnected(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if((wifi !=null && wifi.isConnected()) ||(mobile !=null && mobile.isConnected())){
            return true;
        }else {
            return false;
        }
    }

    public static void showInerntetDialog(Context context, String title){
        // android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        android.app.AlertDialog.Builder builder=new  android.app.AlertDialog.Builder(context);
        builder.setMessage(title);
        builder.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        android.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
      /*  alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });*/
    }
    public static void showAlertDialog(Context context,String title, String message,boolean status) {
   /*     android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();*/
        android.app.AlertDialog.Builder builder=new  android.app.AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(status);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
    public  String getCheckInOutMessage() {
        String email=sharedPreferences.getString(KEY_Message,"");
        return email;
    }

    public  void createCheckInOutMessage(String email_id ) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Message,email_id);
        editor.commit();

    }
    public static void errorMessage() {
        String message=sharedPreferences.getString(KEY_Message,"");
        sharedPreferences.edit().clear().commit();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_Message,message);
        editor.commit();
    }


    public void setlastActivity(String lastActivity) {
        editor.putString(KEY_LAST_ACTIVITY, lastActivity);
        editor.commit();
    }

    public String getLastActivity() {
        String profile = sharedPreferences.getString(KEY_LAST_ACTIVITY, "");
        return profile;
    }
}

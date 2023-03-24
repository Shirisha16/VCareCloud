package com.client.vcarecloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.client.vcarecloud.Api.VcareApi;
import com.client.vcarecloud.Adapters.BirthdayAdapter;
import com.client.vcarecloud.Adapters.CheckTimeAdapter;
import com.client.vcarecloud.Adapters.ExpandableListAdapter;
import com.client.vcarecloud.models.CheckInModel;
import com.client.vcarecloud.models.MenuModel;
import com.client.vcarecloud.models.SecurityProfileModel;
import com.client.vcarecloud.models.UserDetails;
import com.client.vcarecloud.models.upcomingModel;
import com.client.vcarecloud.utils.ExpandableLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EmployeeDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    RecyclerView eventsRecycler, birthdayRecycler;

    AppCompatTextView username,employeeCount,childCount,waitCount,programCount;

//    MaterialCardView waitCard, employeesCard, childrenCard, programsCard;
    CardView waitCard, employeesCard, childrenCard, programsCard;
    DrawerLayout drawer;
    TextView noCheckins,noBirthdays;

    TextView dashboard_tv,children_tv,childrenList_tv,absentList_tv,waitList_tv,emp_tv,empList_tv,
            empAttendance_tv,shifts_tv, childActivity_tv,activityPlanner_tv,dailyActivity_tv,
            actcategory_tv,camp_tv,event_tv,meal_tv,mealPlanner_tv,dailyMeal_tv,menu_tv,mealportion_tv,
            billing_tv,package_tv,charges_tv,adjustments_tv,discount_tv,generateInvoice_tv,
            closeOut_tv,paymentGroup_tv,paymentChild_tv,paymentType_tv,setup_tv,general_tv,profile_tv,
            lookups_tv,programs_tv,immunization_tv,msg_tv,sentMsg_tv,compose_tv,report_tv;

    TextView addSymbol,emp_add_tv,childActivity_add_tv,meal_add_tv,bill_add_tv,payment_add_tv,
             setup_add_tv,msg_add_tv;

    ExpandableLayout expandableLayout_child,expandableLayout_emp,expandableLayout_childActivity,
            expandableLayout_meal,expandableLayout_billing,expandableLayout_payment,expandableLayout_setup,
            expandableLayout_msg;

    RelativeLayout rl_child,rl_emp,rl_childActivity,rl_meal,rl_billing,rl_payment,rl_setup,rl_msg;

    View v_child,v_emp,v_childActivity,v_meal,v_billing,v_payment,v_setup,v_msg;

    //    private NavigationListView expandableListView;

    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();
    String  empId, custId, userId, securityid, firstName,password,message,error;
    RelativeLayout progress;

    ArrayList<CheckInModel> checkInModelArrayList=new ArrayList<>();
    ArrayList<upcomingModel> upcomingArrayList=new ArrayList<>();

    UserDetails userDetails;

    String securityId,securityProfileName,checkIn,absent,childView,modifyChild,viewWaitList,
            modifyWait, employeeView, modifyemp,shiftsView,modifyShifts,activityView,eventsView,
            dailyActView, campsView,modifyActPlanner, eventmodify,modifyDailyAct,campsModify,
            actCategories, mealView,dailyMealsView,menu,mealPortion, modifyMeal,dailyMealsModify,
            packageView, addChargesView,adjustmentView,discountView, modifyPackage,addChargesModify,
            adjustmentModify,modifyDiscount,childCloseout,generateInvoice, viewpayments,
            modifyPayments,paymentType,generalSetting,securityProfile,lookup,programs,
            immunization,viewMsgs,sendMsgs,viewReport;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        username=findViewById(R.id.username);

        setSupportActionBar(toolbar);
        eventsRecycler = findViewById(R.id.checkRecycler);

        birthdayRecycler = findViewById(R.id.birthdayRecycler);
        waitCard = findViewById(R.id.waitCountCard);
        childrenCard=findViewById(R.id.childrenCountCard);
        employeesCard=findViewById(R.id.empCountCard);
        programsCard=findViewById(R.id.programsCountCard);
        expandableListView = findViewById(R.id.expandableListView);
        waitCount=findViewById(R.id.waitListCount);
        employeeCount=findViewById(R.id.employeesCount);
        programCount=findViewById(R.id.programCount);
        childCount=findViewById(R.id.childCount);
        progress=findViewById(R.id.progressLayout);
        noBirthdays=findViewById(R.id.text);
        noCheckins=findViewById(R.id.text2);

        userDetails=new UserDetails(EmployeeDashboard.this);

        String custId = userDetails.getCustId();
        String userId = userDetails.getUserID();
        String daycare = userDetails.getDaycareName();
        securityid = userDetails.getSecurityid();
        password=getIntent().getStringExtra("password");

        dashboardCountResponse();

        waitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeDashboard.this, WaitList.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("userID", userId);
                intent.putExtra("empID", "0");
                intent.putExtra("custId", custId);
                intent.putExtra("securityid", securityid);
                startActivity(intent);
            }
        });

        childrenCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeDashboard.this, ChildrenList.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("userID", userId);
                intent.putExtra("empID", "0");
                intent.putExtra("custId", custId);
                intent.putExtra("securityid", securityid);
                startActivity(intent);
            }
        });

        employeesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeDashboard.this, EmployeeList.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("userID", userId);
                intent.putExtra("empID", "0");
                intent.putExtra("custId", custId);
                intent.putExtra("securityid", securityid);
                startActivity(intent);
            }
        });

        programsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeDashboard.this, Programs.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("userID", userId);
                intent.putExtra("empID", "0");
                intent.putExtra("custId", custId);
                intent.putExtra("securityid", securityid);
                startActivity(intent);
            }
        });

        dashboardChecklistResponse();
        dashboardUpcomingResponse();

//        prepareMenuData();
//        populateExpandableList();
//        expandableList();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        username=header.findViewById(R.id.username);
        username.setText(daycare);

        dashboard_tv=header.findViewById(R.id.dashboard);
        children_tv=header.findViewById(R.id.children);
        expandableLayout_child=header.findViewById(R.id.expandable_children);

        childrenList_tv=header.findViewById(R.id.childrenList);
        absentList_tv=header.findViewById(R.id.absent);
        waitList_tv=header.findViewById(R.id.wait);
        emp_tv=header.findViewById(R.id.employee);
        expandableLayout_emp=header.findViewById(R.id.expandable_emp);

        empList_tv=header.findViewById(R.id.empList);
        empAttendance_tv=header.findViewById(R.id.empAttendance);
        shifts_tv=header.findViewById(R.id.shift);

        expandableLayout_childActivity=header.findViewById(R.id.expLayout_childActivity);
        childActivity_tv=header.findViewById(R.id.childActivity);
//        activityPlanner_tv=header.findViewById(R.id.activityplanner);
//        dailyActivity_tv=header.findViewById(R.id.daily);
        actcategory_tv=header.findViewById(R.id.category);
        camp_tv=header.findViewById(R.id.camps);
        event_tv=header.findViewById(R.id.events);

        expandableLayout_meal=header.findViewById(R.id.expLayout_mealPlanner);
        meal_tv=header.findViewById(R.id.meal);
//        mealPlanner_tv=header.findViewById(R.id.mealPlanner);
        dailyMeal_tv=header.findViewById(R.id.dailyMeals);
        menu_tv=header.findViewById(R.id.menu);
        mealportion_tv=header.findViewById(R.id.mealportion);

        expandableLayout_billing=header.findViewById(R.id.expLayout_billing);
        billing_tv=header.findViewById(R.id.billing);
        package_tv=header.findViewById(R.id.package1);
        charges_tv=header.findViewById(R.id.charges);
        adjustments_tv=header.findViewById(R.id.adjustment);
        discount_tv=header.findViewById(R.id.discount);
//        generateInvoice_tv=header.findViewById(R.id.generate);
//        closeOut_tv=header.findViewById(R.id.closeOut);

        expandableLayout_payment=header.findViewById(R.id.expLayout_payment);
        paymentGroup_tv=header.findViewById(R.id.payment);
        paymentChild_tv=header.findViewById(R.id.payments);
        paymentType_tv=header.findViewById(R.id.paymentType);

        expandableLayout_setup=header.findViewById(R.id.expLayout_setup);
        setup_tv=header.findViewById(R.id.setUp);
        general_tv=header.findViewById(R.id.generalSetup);
        profile_tv=header.findViewById(R.id.profile);
        lookups_tv=header.findViewById(R.id.lookup);
        programs_tv=header.findViewById(R.id.program);
//        taxes_tv=header.findViewById(R.id.tax);
        immunization_tv=header.findViewById(R.id.immunization);

        expandableLayout_msg=header.findViewById(R.id.expLayout_msg);
        msg_tv=header.findViewById(R.id.message);
//        compose_tv=header.findViewById(R.id.compose);
        sentMsg_tv=header.findViewById(R.id.sentMsg);

//        report_tv=header.findViewById(R.id.reports);

        addSymbol= header.findViewById(R.id.add1);
        emp_add_tv=header.findViewById(R.id.add2);
        childActivity_add_tv=header.findViewById(R.id.add3);
        meal_add_tv=header.findViewById(R.id.add4);
        bill_add_tv=header.findViewById(R.id.add5);
        payment_add_tv=header.findViewById(R.id.add6);
        setup_add_tv=header.findViewById(R.id.addBut);
        msg_add_tv=header.findViewById(R.id.add8);

        rl_child=header.findViewById(R.id.rl_child);
        v_child=header.findViewById(R.id.v1);

        rl_emp=header.findViewById(R.id.rl_emp);
        v_emp=header.findViewById(R.id.v2);

        rl_childActivity=header.findViewById(R.id.rl_childActivity);
        v_childActivity=header.findViewById(R.id.v3);

        rl_meal=header.findViewById(R.id.rl_meal);
        v_meal=header.findViewById(R.id.v4);

        rl_billing=header.findViewById(R.id.rl_billing);
        v_billing=header.findViewById(R.id.v5);

        rl_payment=header.findViewById(R.id.rl_payment);
        v_payment=header.findViewById(R.id.v6);

        rl_setup=header.findViewById(R.id.rl_setup);
        v_setup=header.findViewById(R.id.v7);

        rl_msg=header.findViewById(R.id.rl_msg);
        v_msg=header.findViewById(R.id.v8);

        listeners();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        profileList();
        expandableList();
    }

    private void expandableList() {
        rl_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childrenExpand();
//                hideFeatures();
            }
        });

        rl_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                empExpand();
            }
        });

        rl_childActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childExpand();
            }
        });

        rl_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealExpand();
            }
        });

        rl_billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billExpand();
            }
        });

        rl_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentExpand();
            }
        });

        rl_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupExpand();
            }
        });

        rl_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgExpand();
            }
        });

    }

    private void msgExpand() {
        if (expandableLayout_msg.isExpanded()) {
            expandableLayout_msg.collapse();
            msg_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        }else {
            expandableLayout_msg.expand();
            msg_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

//        if(viewMsgs.equalsIgnoreCase("N"))
//            compose_tv.setVisibility(View.GONE);

        if(sendMsgs.equalsIgnoreCase("N")){
            sentMsg_tv.setVisibility(View.GONE);
        }
//        if(viewMsgs.equalsIgnoreCase("N") && sendMsgs.equalsIgnoreCase("N")){
//            msg_tv.setVisibility(View.GONE);
//        }
//        if(viewMsgs.equalsIgnoreCase("N") && sendMsgs.equalsIgnoreCase("Y")){
//            msg_tv.setVisibility(View.GONE);
//            sentMsg_tv.setVisibility(View.GONE);
//            compose_tv.setVisibility(View.GONE);
//        }
        if(viewMsgs.equalsIgnoreCase("Y") && sendMsgs.equalsIgnoreCase("N")){
            sentMsg_tv.setVisibility(View.GONE);
        }
        if(viewMsgs.equalsIgnoreCase("Y") && sendMsgs.equalsIgnoreCase("Y")){
            sentMsg_tv.setVisibility(View.VISIBLE);
//            compose_tv.setVisibility(View.VISIBLE);
        }
    }

    private void setupExpand() {
        if (expandableLayout_setup.isExpanded()) {
            expandableLayout_setup.collapse();
            setup_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        }else {
            expandableLayout_setup.expand();
            setup_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

//        if(generalSetting.equalsIgnoreCase("N"))
//            general_tv.setVisibility(View.GONE);

        if(lookup.equalsIgnoreCase("N")){
            lookups_tv.setVisibility(View.GONE);
        }
        if(programs.equalsIgnoreCase("N")){
            programs_tv.setVisibility(View.GONE);
        }
        if(immunization.equalsIgnoreCase("N")){
            immunization_tv.setVisibility(View.GONE);
        }
        if(securityProfile.equalsIgnoreCase("N")){
            profile_tv.setVisibility(View.GONE);
        }
//        if(generalSetting.equalsIgnoreCase("N") && lookup.equalsIgnoreCase("N") &&
//                programs.equalsIgnoreCase("N") && immunization.equalsIgnoreCase("N") &&
//                securityProfile.equalsIgnoreCase("N")){
//            setup_tv.setVisibility(View.GONE);
//        }
    }

    private void paymentExpand() {
        if (expandableLayout_payment.isExpanded()) {
            expandableLayout_payment.collapse();
            payment_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        }else {
            expandableLayout_payment.expand();
            payment_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

        if (viewpayments.equalsIgnoreCase("N") && modifyPayments.equalsIgnoreCase("N"))
            paymentChild_tv.setVisibility(View.GONE);

        if(paymentType.equalsIgnoreCase("N")){
            paymentType_tv.setVisibility(View.GONE);
        }

//        if(viewpayments.equalsIgnoreCase("N")&& modifyPayments.equalsIgnoreCase("N") &&
//                paymentType.equalsIgnoreCase("N")){
//            paymentGroup_tv.setVisibility(View.GONE);
//        }
    }

    private void billExpand() {
        if (expandableLayout_billing.isExpanded()) {
            expandableLayout_billing.collapse();
            bill_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        } else {
            expandableLayout_billing.expand();
            bill_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

        if(packageView.equalsIgnoreCase("N")&& modifyPackage.equalsIgnoreCase("N"))
            package_tv.setVisibility(View.GONE);

        if(addChargesView.equalsIgnoreCase("N")&& addChargesModify.equalsIgnoreCase("N")){
            charges_tv.setVisibility(View.GONE);
        }
        if(discountView.equalsIgnoreCase("N")&& modifyDiscount.equalsIgnoreCase("N")){
            discount_tv.setVisibility(View.GONE);
        }
        if(adjustmentView.equalsIgnoreCase("N")&& adjustmentModify.equalsIgnoreCase("N")){
            adjustments_tv.setVisibility(View.GONE);
        }
//        if(generateInvoice.equalsIgnoreCase("N")){
//            generateInvoice_tv.setVisibility(View.GONE);
//        }
//        if(childCloseout.equalsIgnoreCase("N")){
//            closeOut_tv.setVisibility(View.GONE);
//        }

//        if(packageView.equalsIgnoreCase("N")&& modifyPackage.equalsIgnoreCase("N")
//                && addChargesView.equalsIgnoreCase("N")&& addChargesModify.equalsIgnoreCase("N")
//                && discountView.equalsIgnoreCase("N")&& modifyDiscount.equalsIgnoreCase("N")
//                && adjustmentView.equalsIgnoreCase("N")&& adjustmentModify.equalsIgnoreCase("N")
//                && generateInvoice.equalsIgnoreCase("N") && childCloseout.equalsIgnoreCase("N")){
//            billing_tv.setVisibility(View.GONE);
//        }
    }

    private void mealExpand() {
        if (expandableLayout_meal.isExpanded()) {
            expandableLayout_meal.collapse();
            meal_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        } else {
            expandableLayout_meal.expand();
            meal_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

//        if(mealView.equalsIgnoreCase("N")&& modifyMeal.equalsIgnoreCase("N"))
//            mealPlanner_tv.setVisibility(View.GONE);
//
//        if(dailyMealsView.equalsIgnoreCase("N")&& dailyMealsModify.equalsIgnoreCase("N")){
//            dailyMeal_tv.setVisibility(View.GONE);
//        }

        if(menu.equalsIgnoreCase("N")){
            menu_tv.setVisibility(View.GONE);
        }
        if(mealPortion.equalsIgnoreCase("N")){
            mealportion_tv.setVisibility(View.GONE);
        }

//        if(mealView.equalsIgnoreCase("N")&& modifyMeal.equalsIgnoreCase("N") &&
//                dailyMealsView.equalsIgnoreCase("N")&& dailyMealsModify.equalsIgnoreCase("N") &&
//                menu.equalsIgnoreCase("N") && mealPortion.equalsIgnoreCase("N")){
//            meal_tv.setVisibility(View.GONE);
//        }
    }

    private void childExpand() {
        if (expandableLayout_childActivity.isExpanded()) {
            expandableLayout_childActivity.collapse();
            childActivity_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        } else {
            expandableLayout_childActivity.expand();
            childActivity_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

//        if (activityView.equalsIgnoreCase("N") && modifyActPlanner.equalsIgnoreCase("N"))
//            activityPlanner_tv.setVisibility(View.GONE);
//
//        if (dailyActView.equalsIgnoreCase("N") && modifyDailyAct.equalsIgnoreCase("N")) {
//            dailyActivity_tv.setVisibility(View.GONE);
//        }
        if (eventsView.equalsIgnoreCase("N") && eventmodify.equalsIgnoreCase("N")) {
            event_tv.setVisibility(View.GONE);
        }
        if (campsView.equalsIgnoreCase("N") && campsModify.equalsIgnoreCase("N")) {
            camp_tv.setVisibility(View.GONE);
        }
        if(actCategories.equalsIgnoreCase("N")){
            actcategory_tv.setVisibility(View.GONE);
        }
//        if(activityView.equalsIgnoreCase("N") && modifyActPlanner.equalsIgnoreCase("N") &&
//                dailyActView.equalsIgnoreCase("N") && modifyDailyAct.equalsIgnoreCase("N") &&
//                eventsView.equalsIgnoreCase("N") && eventmodify.equalsIgnoreCase("N") &&
//                campsView.equalsIgnoreCase("N") && campsModify.equalsIgnoreCase("N") &&
//                actCategories.equalsIgnoreCase("N")){
//            childActivity_tv.setVisibility(View.GONE);
//        }
    }

    private void empExpand() {
        if (expandableLayout_emp.isExpanded()){
            expandableLayout_emp.collapse();
            emp_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);
        }
        else {
            expandableLayout_emp.expand();
            emp_add_tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

        if (employeeView.equalsIgnoreCase("N")){
            empList_tv.setVisibility(View.GONE);
           }
        if (shiftsView.equalsIgnoreCase("N") && shiftsView.equalsIgnoreCase("N")) {
            shifts_tv.setVisibility(View.GONE);
        }
//        if(employeeView.equalsIgnoreCase("N") && shiftsView.equalsIgnoreCase("N") && shiftsView.equalsIgnoreCase("N")){
//            emp_tv.setVisibility(View.GONE);
//        }
    }

    private void childrenExpand() {
        if (expandableLayout_child.isExpanded()) {
            expandableLayout_child.collapse();
            addSymbol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add, 0, 0, 0);

        } else {
            expandableLayout_child.expand();
            addSymbol.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove, 0, 0, 0);
        }

        if (childView.equalsIgnoreCase("N") && checkIn.equalsIgnoreCase("N")){
            childrenList_tv.setVisibility(View.GONE);
        }

        if (absent.equalsIgnoreCase("N")) {
            absentList_tv.setVisibility(View.GONE);
        }
        if (viewWaitList.equalsIgnoreCase("N") && modifyWait.equalsIgnoreCase("N")) {
            waitList_tv.setVisibility(View.GONE);
        }
//        if(childView.equalsIgnoreCase("N") && checkIn.equalsIgnoreCase("N")
//                && absent.equalsIgnoreCase("N") && viewWaitList.equalsIgnoreCase("N")
//                && modifyWait.equalsIgnoreCase("N")){
//            children_tv.setVisibility(View.GONE);
//        }
    }

    private void listeners() {
        dashboard_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this, EmployeeDashboard.class);
                startActivity(intent);
            }
        });

        childrenList_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashboard.this, ChildrenList.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("userID", userId);
                intent.putExtra("empID", empId);
                intent.putExtra("custId", custId);
                intent.putExtra("securityid", securityid);
                startActivity(intent);
            }
        });

        absentList_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,ChildrenAbsentList.class);
                startActivity(intent);
            }
        });

        waitList_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,WaitList.class);
                startActivity(intent);
            }
        });

        empList_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,EmployeeList.class);
                startActivity(intent);
            }
        });

        empAttendance_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,ParticularEmployeeAttendance.class);
                startActivity(intent);
            }
        });

        shifts_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Shifts.class);
                startActivity(intent);
            }
        });

        actcategory_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,ActivityCategory.class);
                startActivity(intent);
            }
        });

        camp_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Camps.class);
                startActivity(intent);
            }
        });

        event_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeDashboard.this, Events.class);
                intent.putExtra("firstName", firstName);
                intent.putExtra("userID", userId);
                intent.putExtra("empID", empId);
                intent.putExtra("custId", custId);
                intent.putExtra("securityid", securityid);
                startActivity(intent);
            }
        });

        menu_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,MealMenu.class);
                startActivity(intent);
            }
        });

        mealportion_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,MealPortion.class);
                startActivity(intent);
            }
        });

        package_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,BasePackages.class);
                startActivity(intent);
            }
        });

        charges_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,AdditionalCharges.class);
                startActivity(intent);
            }
        });

        discount_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Discounts.class);
                startActivity(intent);
            }
        });

        adjustments_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Adjustments.class);
                startActivity(intent);
            }
        });

        paymentChild_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Payments.class);
                startActivity(intent);
            }
        });

        paymentType_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,PaymentType.class);
                startActivity(intent);
            }
        });

        profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,SecurityProfile.class);
                startActivity(intent);
            }
        });

        lookups_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Lookups.class);
                startActivity(intent);
            }
        });

        programs_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Programs.class);
                startActivity(intent);
            }
        });

//        taxes_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(EmployeeDashboard.this,Taxes.class);
//                startActivity(intent);
//            }
//        });

        immunization_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,Immunization.class);
                startActivity(intent);
            }
        });

        sentMsg_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmployeeDashboard.this,SentMessages.class);
                startActivity(intent);
            }
        });
    }

    private void dashboardUpcomingResponse() {
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
        Call<String> call = api.dashboard_upcomingList(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.setVisibility(View.GONE);
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                upcomingModel model = new upcomingModel();
                                model.setName(jsonObject1.getString("name"));
                                model.setDob(jsonObject1.getString("dob"));
                                upcomingArrayList.add(model);
                            }
                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(EmployeeDashboard.this, RecyclerView.VERTICAL, false);
                            birthdayRecycler.setLayoutManager(linearLayoutManager1);
                            birthdayRecycler.setNestedScrollingEnabled(false);
                            linearLayoutManager1.setAutoMeasureEnabled(true);
                            birthdayRecycler.setHasFixedSize(false);
                            BirthdayAdapter adapter1 = new BirthdayAdapter(upcomingArrayList, EmployeeDashboard.this);
                            birthdayRecycler.setAdapter(adapter1);
                        }else{
                            noBirthdays.setText("No upcoming Birthday's");
                        }
                    } catch (JSONException e) {
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
                Toast.makeText(EmployeeDashboard.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void dashboardChecklistResponse() {
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
        Call<String> call = api.dashboard_checkIn(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.setVisibility(View.GONE);
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {

                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CheckInModel model = new CheckInModel();
                                model.setName(jsonObject1.getString("name"));
                                String check = jsonObject1.getString("checkin");
                                String[] checkArray = check.split("T", 2);
                                for (String shiftEndTime1 : checkArray)
                                    model.setCheckIn(shiftEndTime1);
                                checkInModelArrayList.add(model);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(EmployeeDashboard.this, RecyclerView.VERTICAL, false);
                            eventsRecycler.setLayoutManager(linearLayoutManager);

                            CheckTimeAdapter adapter = new CheckTimeAdapter(checkInModelArrayList, EmployeeDashboard.this);
                            eventsRecycler.setAdapter(adapter);
                        }else{
                            noCheckins.setText("No Check-in's");
                        }
                    } catch (JSONException e) {
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
                Toast.makeText(EmployeeDashboard.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void dashboardCountResponse() {
        progress.setVisibility(View.VISIBLE);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(90,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(VcareApi.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(httpClient.build())
                .build();
        VcareApi api=retrofit.create(VcareApi.class);
        Call<String> call=api.dashboard(userDetails.getCustId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.setVisibility(View.GONE);
                if (response.body()!=null){
                    try {
                        JSONObject jsonObject=new JSONObject(response.body());
                        JSONObject jsonObject1=jsonObject.getJSONObject("model");
                        employeeCount.setText(jsonObject1.getString("empCount"));
                        childCount.setText(jsonObject1.getString("childCount"));
                        programCount.setText(jsonObject1.getString("programsCount"));
                        waitCount.setText(jsonObject1.getString("waitlistCount"));
                        String custId=jsonObject1.getString("custId");

                    } catch (JSONException e) {
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
                Toast.makeText(EmployeeDashboard.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void profileList() {
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
        Call<String> call=api.security_profile_for_specific(securityid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progress.setVisibility(View.GONE);
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.optString("message").equalsIgnoreCase("Success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("model");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SecurityProfileModel securityProfileModel = new SecurityProfileModel();
                                securityid=jsonObject1.getString("securityId");
                                custId = jsonObject1.getString("custId");
                                securityProfileName = jsonObject1.getString("securityProfileName");
                                checkIn = jsonObject1.getString("access_ChildCheckInOut");
                                absent = jsonObject1.getString("access_ChildAbsent");
                                childView = jsonObject1.getString("access_ViewChild");
                                modifyChild = jsonObject1.getString("access_ModifyChild");
                                viewWaitList = jsonObject1.getString("access_Waitlist");
                                modifyWait=jsonObject1.getString("access_ModifyWaitlist");

                                if(childView.equalsIgnoreCase("N") && checkIn.equalsIgnoreCase("N")
                                        && absent.equalsIgnoreCase("N") && viewWaitList.equalsIgnoreCase("N")
                                        && modifyWait.equalsIgnoreCase("N")){
                                    rl_child.setVisibility(View.GONE);
                                    v_child.setVisibility(View.GONE);
                                }

                                employeeView=jsonObject1.getString("access_Employee");
                                modifyemp=jsonObject1.getString("access_ModifyEmployee");
                                shiftsView=jsonObject1.getString("access_Shift");
                                modifyShifts=jsonObject1.getString("access_ModifyShift");

                                if(employeeView.equalsIgnoreCase("N") && shiftsView.equalsIgnoreCase("N") && shiftsView.equalsIgnoreCase("N")){
//                                    rl_emp.setVisibility(View.GONE);
//                                    v_emp.setVisibility(View.GONE);
                                    empAttendance_tv.setVisibility(View.VISIBLE);
                                    empList_tv.setVisibility(View.GONE);
                                    shifts_tv.setVisibility(View.GONE);
                                }

                                activityView=jsonObject1.getString("access_ActivityPlanner");
                                eventsView=jsonObject1.getString("access_ActivityTheme");
                                dailyActView=jsonObject1.getString("access_DailyActivity");
                                campsView=jsonObject1.getString("access_Camp");
                                modifyActPlanner=jsonObject1.getString("access_ModifyActivityPlanner");
                                eventmodify=jsonObject1.getString("access_ModifyActivityTheme");
                                modifyDailyAct=jsonObject1.getString("access_ModifyDailyActivity");
                                campsModify=jsonObject1.getString("access_ModifyCamp");
                                actCategories=jsonObject1.getString("access_ActivityCategory");

                                if(activityView.equalsIgnoreCase("N") && modifyActPlanner.equalsIgnoreCase("N") &&
                                        dailyActView.equalsIgnoreCase("N") && modifyDailyAct.equalsIgnoreCase("N") &&
                                        eventsView.equalsIgnoreCase("N") && eventmodify.equalsIgnoreCase("N") &&
                                        campsView.equalsIgnoreCase("N") && campsModify.equalsIgnoreCase("N") &&
                                        actCategories.equalsIgnoreCase("N")){
                                    v_childActivity.setVisibility(View.GONE);
                                    rl_childActivity.setVisibility(View.GONE);
                                }

                                mealView=jsonObject1.getString("access_MealPlanner");
                                dailyMealsView=jsonObject1.getString("access_DailyMeal");
                                menu=jsonObject1.getString("access_Menu");
                                mealPortion=jsonObject1.getString("access_MealPortion");
                                modifyMeal=jsonObject1.getString("access_ModifyMealPlanner");
                                dailyMealsModify=jsonObject1.getString("access_ModifyDailyMeal");

                                if(mealView.equalsIgnoreCase("N")&& modifyMeal.equalsIgnoreCase("N") &&
                                        dailyMealsView.equalsIgnoreCase("N")&& dailyMealsModify.equalsIgnoreCase("N") &&
                                        menu.equalsIgnoreCase("N") && mealPortion.equalsIgnoreCase("N")){
                                    rl_meal.setVisibility(View.GONE);
                                    v_meal.setVisibility(View.GONE);
                                }

                                packageView=jsonObject1.getString("access_BasePackages");
                                addChargesView=jsonObject1.getString("access_AdditionalCharges");
                                adjustmentView=jsonObject1.getString("access_Adjustments");
                                discountView=jsonObject1.getString("access_Discounts");
                                modifyPackage=jsonObject1.getString("access_ModifyBasePackages");
                                addChargesModify=jsonObject1.getString("access_ModifyAdditionalCharges");
                                adjustmentModify=jsonObject1.getString("access_ModifyAdjustments");
                                modifyDiscount=jsonObject1.getString("access_ModifyDiscounts");
                                childCloseout=jsonObject1.getString("access_ChildCloseout");
                                generateInvoice=jsonObject1.getString("access_GenerateInvoice");

                                if(packageView.equalsIgnoreCase("N")&& modifyPackage.equalsIgnoreCase("N")
                                        && addChargesView.equalsIgnoreCase("N")&& addChargesModify.equalsIgnoreCase("N")
                                        && discountView.equalsIgnoreCase("N")&& modifyDiscount.equalsIgnoreCase("N")
                                        && adjustmentView.equalsIgnoreCase("N")&& adjustmentModify.equalsIgnoreCase("N")
                                        && generateInvoice.equalsIgnoreCase("N") && childCloseout.equalsIgnoreCase("N")){
                                    rl_billing.setVisibility(View.GONE);
                                    v_billing.setVisibility(View.GONE);
                                }

                                viewpayments=jsonObject1.getString("access_Payments");
                                modifyPayments=jsonObject1.getString("access_ModifyPayments");
                                paymentType=jsonObject1.getString("access_PaymentTypes");

                                if(viewpayments.equalsIgnoreCase("N")&& modifyPayments.equalsIgnoreCase("N") &&
                                   paymentType.equalsIgnoreCase("N")){
                                    rl_payment.setVisibility(View.GONE);
                                    v_payment.setVisibility(View.GONE);
                                }

                                generalSetting=jsonObject1.getString("access_GeneralSetup");
                                securityProfile=jsonObject1.getString("access_Security");
                                lookup=jsonObject1.getString("access_LookupSetup");
                                programs=jsonObject1.getString("access_ClassSetup");
                                immunization=jsonObject1.getString("access_ImmunizationSetup");

                                if(generalSetting.equalsIgnoreCase("N") && lookup.equalsIgnoreCase("N") &&
                                        programs.equalsIgnoreCase("N") && immunization.equalsIgnoreCase("N") &&
                                        securityProfile.equalsIgnoreCase("N")){
                                    rl_setup.setVisibility(View.GONE);
                                    v_setup.setVisibility(View.GONE);
                                }

                                viewMsgs=jsonObject1.getString("access_Messaging");
                                sendMsgs=jsonObject1.getString("access_SendMessages");

                                if(viewMsgs.equalsIgnoreCase("N") && sendMsgs.equalsIgnoreCase("N")){
                                    rl_msg.setVisibility(View.GONE);
                                    v_msg.setVisibility(View.GONE);
                                }
                                if(viewMsgs.equalsIgnoreCase("N") && sendMsgs.equalsIgnoreCase("Y")){
                                    rl_msg.setVisibility(View.GONE);
                                    v_msg.setVisibility(View.GONE);
                                    sentMsg_tv.setVisibility(View.GONE);
                                    compose_tv.setVisibility(View.GONE);
                                }

                                viewReport=jsonObject1.getString("access_Report");
//                                if(viewReport.equalsIgnoreCase("N")){
//                                    report_tv.setVisibility(View.GONE);
//                                }
                            }
                        }
                    } catch (JSONException e) {
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
                Toast.makeText(EmployeeDashboard.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateExpandableList() {
        expandableListAdapter = new ExpandableListAdapter(EmployeeDashboard.this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//            expandableListView.setSelected(groupPosition);
                if (headerList.get(groupPosition).isGroup) {
                    if (id == 0) {
                        // dashboard
                        drawer.openDrawer(GravityCompat.START);

                    } else if (id == 1) {
                        drawer.openDrawer(GravityCompat.START);

                    } else if (id == 2) {
                        drawer.openDrawer(GravityCompat.START);
                    } else if (id == 3) {
                        drawer.openDrawer(GravityCompat.START);
                    } else if (id == 4) {
                        drawer.openDrawer(GravityCompat.START);
                    } else if (id == 5) {
                        drawer.openDrawer(GravityCompat.START);
                    } else if (id == 6) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }

//                int previousItem = -1;
//                if (groupPosition != previousItem) {
//                    expandableListView.collapseGroup(previousItem);
//                    previousItem = groupPosition;
//                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (groupPosition==1) {
//                    if(checkIn.equalsIgnoreCase("Y")) {
                    if (childPosition == 0) {
                        Intent intent = new Intent(EmployeeDashboard.this, ChildrenList.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("userID", userId);
                        intent.putExtra("empID", empId);
                        intent.putExtra("custId", custId);
                        intent.putExtra("securityid", securityid);
                        startActivity(intent);
                    }
//                    }else{
//                    }

                    if (childPosition==1){
                        Intent intent = new Intent(EmployeeDashboard.this, ChildrenAbsentList.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("userID", userId);
                        intent.putExtra("empID", empId);
                        intent.putExtra("custId", custId);
                        intent.putExtra("securityid", securityid);
                        startActivity(intent);
                    }
                    if (childPosition==2){
                        Intent intent = new Intent(EmployeeDashboard.this, WaitList.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("userID", userId);
                        intent.putExtra("empID", empId);
                        intent.putExtra("custId", custId);
                        intent.putExtra("securityid", securityid);
                        startActivity(intent);
                    }
                }
                if (groupPosition==2){
                    if (childPosition==0){
                        Intent intent = new Intent(EmployeeDashboard.this, EmployeeList.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("userID", userId);
                        intent.putExtra("empID", empId);
                        intent.putExtra("custId", custId);
                        intent.putExtra("securityid", securityid);
                        startActivity(intent);
                    }

                    if (childPosition==1){
                        Intent intent = new Intent(EmployeeDashboard.this, ParticularEmployeeAttendance.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("userID", userId);
                        intent.putExtra("empID", empId);
                        intent.putExtra("custId", custId);
                        intent.putExtra("securityid", securityid);
                        startActivity(intent);
                    }
                    if (childPosition==2){
                        Intent intent = new Intent(EmployeeDashboard.this, Shifts.class);
                        intent.putExtra("firstName", firstName);
                        intent.putExtra("userID", userId);
                        intent.putExtra("empID", empId);
                        intent.putExtra("custId", custId);
                        intent.putExtra("securityid", securityid);
                        startActivity(intent);
                    }
                }

//                if (groupPosition==3){
//                    if (childPosition==0){
//                        Intent intent = new Intent(EmployeeDashboard.this, ActivityCategory.class);
//                        intent.putExtra("firstName", firstName);
//                        intent.putExtra("userID", userId);
//                        intent.putExtra("empID", empId);
//                        intent.putExtra("custId", custId);
//                        intent.putExtra("securityid", securityid);
//                        startActivity(intent);
//                    }
//
//                    if (childPosition==1){
//                        Intent intent = new Intent(EmployeeDashboard.this, Camps.class);
//                        intent.putExtra("firstName", firstName);
//                        intent.putExtra("userID", userId);
//                        intent.putExtra("empID", empId);
//                        intent.putExtra("custId", custId);
//                        intent.putExtra("securityid", securityid);
//                        startActivity(intent);
//                    }
//                    if (childPosition==2){
//                        Intent intent = new Intent(EmployeeDashboard.this, Events.class);
//                        intent.putExtra("firstName", firstName);
//                        intent.putExtra("userID", userId);
//                        intent.putExtra("empID", empId);
//                        intent.putExtra("custId", custId);
//                        intent.putExtra("securityid", securityid);
//                        startActivity(intent);
//                    }
//                }

                onBackPressed();
                return false;
            }
        });
    }

    private void prepareMenuData() {

        MenuModel menuModel = new MenuModel("Dashboard", R.drawable.dashboard); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

//        if (checkIn.equalsIgnoreCase("Y") || childView.equalsIgnoreCase("Y")){
//            menuModel = new MenuModel("Children", R.drawable.children, true, true, true, false); //Menu of Java Tutorials
//            headerList.add(menuModel);
//        }else {
//            menuModel.isHasChild();
//        }

        menuModel = new MenuModel("Children", R.drawable.children, true, true, true, false); //Menu of Java Tutorials
        headerList.add(menuModel);
        List<MenuModel> childModelsList = new ArrayList<>();

        MenuModel childModel = new MenuModel("Children List", R.drawable.ic_baseline_arrow_forward_ios_24);
        childModelsList.add(childModel);

//        if(checkIn.equalsIgnoreCase("Y")){
////            MenuModel childModel = new MenuModel("Children List", R.drawable.ic_baseline_arrow_forward_ios_24);
//            childModelsList.add(childModel);
//        }else{
//
//        }

        childModel = new MenuModel("Absent List", R.drawable.ic_baseline_arrow_forward_ios_24);
        childModelsList.add(childModel);

        childModel = new MenuModel("Wait List", R.drawable.ic_baseline_arrow_forward_ios_24);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            Log.d("API123", "here");
            childList.put(menuModel, childModelsList);
        }

        childModelsList = new ArrayList<>();
        menuModel = new MenuModel("Employees", R.drawable.employees, true, true, true, false); //Menu of Python Tutorials
        headerList.add(menuModel);
        childModel = new MenuModel("Employees List", R.drawable.ic_baseline_arrow_forward_ios_24);
        childModelsList.add(childModel);

        childModel = new MenuModel("Employee Attendance", R.drawable.ic_baseline_arrow_forward_ios_24);
        childModelsList.add(childModel);
        childModel = new MenuModel("Shifts", R.drawable.ic_baseline_arrow_forward_ios_24);
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

//        menuModel = new MenuModel("Child Activity", R.drawable.child_activity, true, true, true, false); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        childModelsList = new ArrayList<>();
//        childModel = new MenuModel("Activity Planner", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Daily Activities", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Activity Category", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Camps", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("Events", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        if (menuModel.hasChildren) {
//            Log.d("API123", "here");
//            childList.put(menuModel, childModelsList);
//        }
//        menuModel = new MenuModel("Meal", R.drawable.meal, true, true, true, false); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        childModelsList = new ArrayList<>();
//        childModel = new MenuModel("Meal Planner", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Daily Meals", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Menu", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Meal Portion Taken", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//
//        if (menuModel.hasChildren) {
//            Log.d("API123", "here");
//            childList.put(menuModel, childModelsList);
//        }
//        menuModel = new MenuModel("Billing", R.drawable.billing, true, true, true, false); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        childModelsList = new ArrayList<>();
//        childModel = new MenuModel("Base Packages", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Additional Charges", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Discounts", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Adjustments", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("Generate Invoice", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("Invoice List", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("Child Closeout", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        if (menuModel.hasChildren) {
//            Log.d("API123", "here");
//            childList.put(menuModel, childModelsList);
//        }
//        menuModel = new MenuModel("Payments", R.drawable.payments, true, true, true, false); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        childModelsList = new ArrayList<>();
//        childModel = new MenuModel("Payments", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Payments Type", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//        if (menuModel.hasChildren) {
//            Log.d("API123", "here");
//            childList.put(menuModel, childModelsList);
//        }
//        menuModel = new MenuModel("Setup", R.drawable.setup, true, true, true, false); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        childModelsList = new ArrayList<>();
//        childModel = new MenuModel("General Settings", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Security Profiles", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Lookups", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Programs", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("Taxes", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//        childModel = new MenuModel("Immunizations", R.drawable.ic_baseline_arrow_forward_ios_24);
//        childModelsList.add(childModel);
//
//        if (menuModel.hasChildren) {
//            Log.d("API123", "here");
//            childList.put(menuModel, childModelsList);
//        }
//        menuModel = new MenuModel("Messaging", R.drawable.message, true, true, true, false); //Menu of Java Tutorials
//        headerList.add(menuModel);
//        childModelsList = new ArrayList<>();
//        childModel = new MenuModel("Compose Message", false, false, "");
//        childModelsList.add(childModel);
//
//        childModel = new MenuModel("Sent Messages", false, false, "");
//        childModelsList.add(childModel);
//        if (menuModel.hasChildren) {
//            Log.d("API123", "here");
//            childList.put(menuModel, childModelsList);
//        }
//        menuModel = new MenuModel("Reports", R.drawable.report); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (id==R.id.)
        return false;
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
            Intent intent = new Intent(EmployeeDashboard.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            userDetails.setIsLogged(false);
            startActivity(intent);
            finish();

        }
        if (id == R.id.changePwd) {
            Intent intent = new Intent(EmployeeDashboard.this, ChangePassword.class);
            intent.putExtra("userID",userDetails.getUserID());
            intent.putExtra("empID",userDetails.getEmpID());
            intent.putExtra("custId",userDetails.getCustId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    protected void onResume() {
//        super.onResume();
////        dashboardCountResponse();
//
//    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(EmployeeDashboard.this);
            builder1.setMessage("Do you want to exit ");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        reload();
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}


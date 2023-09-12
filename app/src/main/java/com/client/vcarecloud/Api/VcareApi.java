package com.client.vcarecloud.Api;

import com.client.vcarecloud.models.AddAbsentModel;
import com.client.vcarecloud.models.AddAbsentResponse;
import com.client.vcarecloud.models.AddActCategoryModel;
import com.client.vcarecloud.models.AddActCategoryResponse;
import com.client.vcarecloud.models.AddAdjustmentModel;
import com.client.vcarecloud.models.AddAdjustmentResponse;
import com.client.vcarecloud.models.AddBasePackageModel;
import com.client.vcarecloud.models.AddBasePackageResponse;
import com.client.vcarecloud.models.AddDiscountModel;
import com.client.vcarecloud.models.AddDiscountResponse;
import com.client.vcarecloud.models.AddEventTypeModelLookup;
import com.client.vcarecloud.models.AddImmunizationModel;
import com.client.vcarecloud.models.AddImmunizationResponse;
import com.client.vcarecloud.models.AddLookupTypeModel;
import com.client.vcarecloud.models.AddLookupTypeResponse;
import com.client.vcarecloud.models.AddCampModel;
import com.client.vcarecloud.models.AddEventModel;
import com.client.vcarecloud.models.AddEventResponse;
import com.client.vcarecloud.models.AddEventTypeModel;
import com.client.vcarecloud.models.AddEventTypeResponse;
import com.client.vcarecloud.models.AddPaymentModel;
import com.client.vcarecloud.models.AddProgramsModel;
import com.client.vcarecloud.models.AddProgramsResponse;
import com.client.vcarecloud.models.AddSecurityProfileModel;
import com.client.vcarecloud.models.AddSecurityProfileResponse;
import com.client.vcarecloud.models.AdditionalChargeListModel;
import com.client.vcarecloud.models.AdditionalChargeResponse;
import com.client.vcarecloud.models.AdditionalChargesModel;
import com.client.vcarecloud.models.BasePackagesModel;
import com.client.vcarecloud.models.ChildCheckInModel;
import com.client.vcarecloud.models.EventModel;
import com.client.vcarecloud.models.GetChildListResponseModel;
import com.client.vcarecloud.models.GetClassList;
import com.client.vcarecloud.models.GetClassListResponseModel;
import com.client.vcarecloud.models.InvoiceListModel;
import com.client.vcarecloud.models.MenuModelMeal;
import com.client.vcarecloud.models.MealMenuResponse;
import com.client.vcarecloud.models.AddShiftModel;
import com.client.vcarecloud.models.AddShiftResponse;
import com.client.vcarecloud.models.AddTaxModel;
import com.client.vcarecloud.models.AddTaxResponse;
import com.client.vcarecloud.models.AddcampResponse;
import com.client.vcarecloud.models.ChangePasswordModel;
import com.client.vcarecloud.models.CheckInRequest;
import com.client.vcarecloud.models.CheckInResponse;
import com.client.vcarecloud.models.EmpCheckInRequest;
import com.client.vcarecloud.models.EmpCheckInResponse;
import com.client.vcarecloud.models.PasswordResponce;
import com.client.vcarecloud.models.PaymentModel;
import com.client.vcarecloud.models.PaymentResponse;
import com.client.vcarecloud.models.UpdateAbsentModel;
import com.client.vcarecloud.models.UpdateAbsentResponse;
import com.client.vcarecloud.models.UpdateActivityCategoryModel;
import com.client.vcarecloud.models.UpdateActivityCategoryResponse;
import com.client.vcarecloud.models.UpdateAdditionalChargesModel;
import com.client.vcarecloud.models.UpdateAdditionalChargesResponse;
import com.client.vcarecloud.models.UpdateAdjustmentModel;
import com.client.vcarecloud.models.UpdateAdjustmentResponse;
import com.client.vcarecloud.models.UpdateBasePackageModel;
import com.client.vcarecloud.models.UpdateBasePackageResponse;
import com.client.vcarecloud.models.UpdateDiscountModel;
import com.client.vcarecloud.models.UpdateDiscountResponse;
import com.client.vcarecloud.models.UpdateImmunizationModel;
import com.client.vcarecloud.models.UpdateImmunizationResponse;
import com.client.vcarecloud.models.UpdateLookupTypeModel;
import com.client.vcarecloud.models.UpdateLookupTypeResponse;
import com.client.vcarecloud.models.UpdateCampModel;
import com.client.vcarecloud.models.UpdateCampResponse;
import com.client.vcarecloud.models.UpdateEventModel;
import com.client.vcarecloud.models.UpdateEventResponse;
import com.client.vcarecloud.models.UpdateEventTypeModel;
import com.client.vcarecloud.models.UpdateEventTypeResponse;
import com.client.vcarecloud.models.UpdateProgramModel;
import com.client.vcarecloud.models.UpdateProgramResponse;
import com.client.vcarecloud.models.UpdateSecurityProfileModel;
import com.client.vcarecloud.models.UpdateSecurityProfileResponse;
import com.client.vcarecloud.models.UpdateShiftModel;
import com.client.vcarecloud.models.UpdateShiftResponse;
import com.client.vcarecloud.models.UpdateTaxModel;
import com.client.vcarecloud.models.UpdateTaxResponse;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.http.FormUrlEncoded;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface VcareApi {
//    String JSONURL = "https://testdaycareonlinewebapi.azurewebsites.net/"; //  TESTING URL
      String JSONURL = "https://testvcarewebapi.azurewebsites.net/"; //  TESTING URL


//    String JSONURL = "https://proddaycareonlinewebapi.azurewebsites.net/"; //Production Testing URL

    @GET("api/Account/Login/{UserName}/{Password}")
    Call<String> login_page(@Path(value = "UserName", encoded = true) String username,
                            @Path(value = "Password", encoded = true) String password);

    @GET("api/Account/ForgotPassword/{UserName}")
    Call<String> forgot_password(@Path(value = "UserName",encoded = true) String username);

    @POST("api/Account/ChangePassword")
    Call<PasswordResponce> changePassword(@Body ChangePasswordModel changePasswordModel);

    @GET("api/Child/ChildList/{CustId}")
    Call<String> children_list(@Path(value = "CustId",encoded = true) String custId);

    @GET("api/Child/ChildWaitList/{CustId}")
    Call<String> children_wait_list(@Path(value = "CustId",encoded = true) String custId);

    @GET("api/Employees/EmpList/{CustId}")
    Call<String> employees_list(@Path(value = "CustId",encoded = true) String custId);

    @GET("api/Employees/EmpAttendList/{CustId}")
    Call<String> emp_attendance_listAdmin(@Path(value = "CustId",encoded = true) String custId);

    @GET("api/Employees/EmpAttend/{EmpId}")
    Call<String> emp_attendanceList(@Path("EmpId") String EmpId);


    @GET("api/Child/getChildAbsent/{CustId}")
    Call<String> child_absent_list(@Path(value = "CustId",encoded = true) String custId);

    @PUT("api/Child/enroll/{id}/{empid}")
    Call<String> child_enroll(@Path("id") String id,@Path("empid") String empid);

    @PUT("api/Child/Remove/{id}/{empid}")
    Call<String> child_remove(@Path("id") String id, @Path("empid") String empid);

    @GET("api/Child/Childinfo/{ChildId}")
    Call<String> child_info(@Path(value = "ChildId",encoded = true) String custId);

    @POST("api/Child/AddChildabsent/{EmpId}")
    Call<AddAbsentResponse> add_absent(@Path("EmpId") String EmpId, @Body AddAbsentModel addAbsentModel);

    @GET("api/Account/getdashboardcount/{CustId}")
    Call<String> dashboard(@Path(value = "CustId",encoded = true) String Custid);

    @GET("api/Child/getCheckinList/{Custid}")
    Call<String> dashboard_checkIn(@Path(value = "Custid",encoded = true) String Custid);

    @GET("api/Child/getupcomingbirthday/{Custid}")
    Call<String> dashboard_upcomingList(@Path(value = "Custid",encoded = true) String Custid);

    @PUT("api/Child/DeleteChildabsent/{id}/{empid}")
    Call<String> delete_absentList(@Path("id") String id,@Path("empid") String empid);

    @PUT("api/Child/UpdateChildabsent/{id}/{EmpId}")
    Call<UpdateAbsentResponse> update_absent(@Path(value = "id",encoded = true) String id, @Path(value = "EmpId",encoded = true) String EmpId, @Body UpdateAbsentModel updateAbsentModel);

    @GET("api/Classes/getclassdropdown/{Custid}")
    Call<String> class_dropdown(@Path(value = "Custid",encoded = true) String Custid);

    @GET("api/Child/getchilddropdown/{Custid}")
    Call<String> child_dropdown(@Path(value = "Custid",encoded = true) String Custid);

    @GET("api/Child/getchildclassdropdown/{Custid}/{classid}")
    Call<String> childClass_dropdown(@Path(value = "Custid",encoded = true) String Custid,
                                     @Path(value = "classid",encoded = true) String classid);

    @PUT("api/Child/ChildCheck")
    Call<CheckInResponse> check_in( @Body CheckInRequest body);

    @PUT("api/Child/childcheckstatus")
    Call<ChildCheckInModel> checkinOut(@Body CheckInRequest body);

    @PUT("api/Employees/EmpCheck/{EmpId}")
    Call<EmpCheckInResponse> empCheckIn(@Path(value = "EmpId",encoded = true) String empId,
                                        @Body EmpCheckInRequest body);

    @GET("api/Shifts/getshifts/{CustId}")
    Call<String> shifts_list(@Path(value = "CustId",encoded = true) String custId);

    @PUT("api/Shifts/DeleteShift/{id}/{Empid}")
    Call<String> delete_shifts(@Path(value = "id",encoded = true) String id,@Path(value = "Empid",encoded = true) String empid);

    @PUT("api/Shifts/UpdateShifts/{id}/{EmpId}")
    Call<UpdateShiftResponse> update_shifts(@Path(value = "id",encoded = true) String id,
                                            @Path(value = "EmpId",encoded = true) String EmpId,
                                            @Body UpdateShiftModel updateShiftModel);

    @POST("api/Shifts/AddShifts/{EmpId}")
    Call<AddShiftResponse> add_shifts(@Path("EmpId") String empId,
                                      @Body AddShiftModel addShiftModel);

    @GET("api/Employees/Empinfo/{EmpId}")
    Call<String> employeeInfo(@Path(value = "EmpId",encoded = true) String empId);

    @GET("api/SecurityProfiles/getsecurityprofiles/{Custid}")
    Call<String> security_profile(@Path(value = "Custid",encoded = true) String custId);

    @GET("api/SecurityProfiles/getsecurityprofileinfo/{id}")
    Call<String> security_profile_for_specific(@Path(value = "id",encoded = true) String id);

    @POST("api/SecurityProfiles/AddSecurityProfiles/{empid}")
    Call<AddSecurityProfileResponse> add_security_profile(@Path("empid") String empId,
                                                          @Body AddSecurityProfileModel addSecurityProfileModel);

    @PUT("api/SecurityProfiles/UpdateSecurityProfile/{id}/{empid}")
    Call<UpdateSecurityProfileResponse> update_security_profile(@Path(value = "id",encoded = true) String id,
                                                                @Path(value = "empid",encoded = true) String EmpId,
                                                                @Body UpdateSecurityProfileModel updateSecurityProfileModel);

    @PUT("api/SecurityProfiles/DeleteSecurityProfile/{id}/{empid}")
    Call<String> delSecurityProfile(@Path("id") String id,@Path("empid") String empid);

    @GET("api/Tax/gettaxes/{Custid}")
    Call<String> taxData(@Path(value = "Custid",encoded = true) String custId);

    @PUT("api/Tax/DeleteTax/{id}/{EmpId}")
    Call<String> delete_tax(@Path("id") String id,@Path("EmpId") String empid);

    @POST("api/Tax/AddTaxes/{EmpId}")
    Call<AddTaxResponse> add_tax(@Path("EmpId") String empId,
                                 @Body AddTaxModel addTaxModel);

    @PUT("api/Tax/UpdateTaxes/{id}/{EmpId}")
    Call<UpdateTaxResponse> update_taxes(@Path(value = "id",encoded = true) String id,
                                         @Path(value = "EmpId",encoded = true) String EmpId,
                                         @Body UpdateTaxModel updateTaxModel);

    @GET("api/Camps/getcamps/{Custid}")
    Call<String> campData(@Path(value = "Custid",encoded = true) String custId);

    @POST("api/Camps/AddCamps/{empid}")
    Call<AddcampResponse> add_camps(@Path("empid") String empId,
                                    @Body AddCampModel addCampModel);

    @PUT("api/Camps/UpdateCamps/{id}/{empid}")
    Call<UpdateCampResponse> update_camps(@Path(value = "id",encoded = true) String id,
                                          @Path(value = "empid",encoded = true) String EmpId,
                                          @Body UpdateCampModel updateCampModel);

    @PUT("api/Camps/DeleteCamp/{id}/{empid}")
    Call<String> delete_camp(@Path("id") String id,@Path("empid") String empid);

    @GET("api/ActivityCategory/getActivityCategory/{Custid}")
    Call<String> activityCategoryList(@Path (value ="Custid", encoded = true) String custId);

    @POST("api/ActivityCategory/AddActivityCategory/{EmpId}")
    Call<AddActCategoryResponse> add_activityCategory(@Path("EmpId") String empid,
                                                      @Body AddActCategoryModel addActCategoryModel);

    @PUT("api/ActivityCategory/UpdateActivityCategory/{id}/{EmpId}")
    Call<UpdateActivityCategoryResponse> update_activityCategory(@Path(value = "id",encoded = true)String id,
                                                                 @Path(value ="EmpId",encoded = true)String empid,
                                                                 @Body UpdateActivityCategoryModel updateActivityCategoryModel);

    @PUT("api/ActivityCategory/DeleteActivityCategory/{id}/{EmpId}")
    Call<String> delete_activity(@Path("id") String id,@Path("EmpId")String empid);

    @GET("api/Events/getevents/{Custid}")
    Call<EventModel> events(@Path(value = "Custid",encoded = true)String custId);

    @POST("api/Events/AddEvents/{EmpId}")
    Call<AddEventResponse> addevents(@Path("EmpId")String empid,
                                     @Body AddEventModel addEventModel);

    @GET("api/Events/geteventtypes/{Custid}")
    Call<String> eventTypeList(@Path(value = "Custid",encoded = true)String custId);

    @POST("api/Events/AddEventsType/{EmpId}")
    Call<AddEventTypeResponse> addeventType(@Path("EmpId")String empid,
                                            @Body AddEventTypeModel addEventTypeModel);
    @POST("api/Events/AddEventsType/{EmpId}")
    Call<AddEventTypeResponse> addeventTypeLookup(@Path("EmpId")String empid,
                                            @Body AddEventTypeModelLookup addEventTypeModelLookup);

    @PUT("api/Events/UpdateEventsType/{id}/{EmpId}")
    Call<UpdateEventTypeResponse> update_eventType(@Path(value = "id",encoded = true)String id,
                                                   @Path(value ="EmpId",encoded = true)String empid,
                                                   @Body UpdateEventTypeModel updateEventTypeModel);

    @PUT("api/Events/DeleteEventsType/{id}/{EmpId}")
    Call<String> delete_eventsType(@Path("id") String id,@Path("EmpId")String empid);

    @PUT("api/Events/UpdateEvents/{id}/{EmpId}")
    Call<UpdateEventResponse> update_events(@Path(value = "id",encoded = true)String id,
                                            @Path(value = "EmpId",encoded = true)String empid,
                                            @Body UpdateEventModel updateEventModel);

    @PUT("api/Events/DeleteEvents/{id}/{EmpId}")
    Call<String> delete_events(@Path("id") String id,@Path("EmpId")String empid);

    @GET("api/Lookup/getlookup/{Custid}/{Lookuptype}")
    Call<String> lookupType(@Path(value ="Custid", encoded = true)String custid,
                            @Path(value = "Lookuptype", encoded = true)String lookupType);

    @POST("api/Lookup/AddLookup/{EmpId}")
    Call<AddLookupTypeResponse> addLookups(@Path("EmpId")String empid,
                                           @Body AddLookupTypeModel addLookupTypeModel);

    @PUT("api/Lookup/UpdateLookup/{id}/{EmpId}")
    Call<UpdateLookupTypeResponse> updateLookups(@Path(value = "id",encoded = true)String id,
                                                 @Path(value = "EmpId",encoded = true)String empId,
                                                 @Body UpdateLookupTypeModel updateLookupTypeModel);

    @PUT("api/Lookup/DeleteLookup/{id}/{EmpId}")
    Call<String> deleteLookup(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/Menu/getMenu/{Custid}")
    Call<String> menuList(@Path(value = "Custid",encoded = true)String custId);

    @POST("api/Menu/AddMenu/{EmpId}")
    Call<MealMenuResponse> addMenu(@Path("EmpId")String empid,
                                   @Body MenuModelMeal menuModelMeal);

    @PUT("api/Menu/UpdateMenu/{id}/{EmpId}")
    Call<MealMenuResponse> update_Menu(@Path(value = "id",encoded = true)String id,
                                       @Path(value ="EmpId",encoded = true)String empid,
                                       @Body MenuModelMeal menuModelMeal);

    @PUT("api/Menu/DeleteMenu/{id}/{EmpId}")
    Call<String> deleteMenu(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/MealPortion/getMealPortion/{Custid}")
    Call<String> meal_portion(@Path(value = "Custid",encoded = true)String custId);

    @POST("api/MealPortion/AddMealPortion/{EmpId}")
    Call<AddLookupTypeResponse> addMeal_portion(@Path("EmpId")String empid,
                                           @Body AddLookupTypeModel addLookupTypeModel);

    @PUT("api/MealPortion/UpdateMealPortion/{id}/{EmpId}")
    Call<UpdateLookupTypeResponse> update_mealPortion(@Path(value = "id",encoded = true)String id,
                                                 @Path(value = "EmpId",encoded = true)String empId,
                                                 @Body UpdateLookupTypeModel updateLookupTypeModel);

    @PUT("api/MealPortion/DeleteMealPortion/{id}/{EmpId}")
    Call<String> delete_mealPortion(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/Message/getMails/{Custid}")
    Call<String> sentMsg(@Path(value = "Custid",encoded = true)String custId);

    @GET("api/Message/ViewMail/{id}")
    Call<String> view_mail(@Path(value = "id",encoded = true)String custId);

    @GET("api/Payment/getpayments/{Custid}")
    Call<String> payment(@Path(value = "Custid",encoded = true)String custId);

    @POST("api/Payment/AddPayments/{EmpId}")
    Call<PaymentResponse> addpayment(@Path("EmpId")String empid,
                                     @Body AddPaymentModel addPaymentModel);

    @GET("api/Paymenttype/getPaymenttype/{Custid}")
    Call<String> paymentType(@Path(value = "Custid",encoded = true)String custId);

    @POST("api/Paymenttype/AddPaymenttype/{EmpId}")
    Call<AddLookupTypeResponse> addpaymentType(@Path("EmpId")String empid,
                                           @Body AddLookupTypeModel addLookupTypeModel);

    @PUT("api/Paymenttype/DeletePaymenttype/{id}/{EmpId}")
    Call<String> delete_paymentType(@Path("id")String id,@Path("EmpId")String empid);

    @PUT("api/Paymenttype/UpdatePaymenttype/{id}/{EmpId}")
    Call<UpdateLookupTypeResponse> update_paymentType(@Path(value = "id",encoded = true)String id,
                                                 @Path(value = "EmpId",encoded = true)String empId,
                                                 @Body UpdateLookupTypeModel updateLookupTypeModel);
    @GET("api/Invoice/getInvoices/{Custid}")
    Call<InvoiceListModel> invoice_list(@Path(value = "Custid",encoded = true)String custId);

    @GET("api/Invoice/getInvoiceDetail/{id}")
    Call<String> invoice_details(@Path(value = "id",encoded = true)String custId);

    @GET("api/Adjustments/getadjustments/{Custid}")
    Call<String> adjustment(@Path(value = "Custid",encoded = true)String custId);

    @POST("api/Adjustments/AddAdjustments/{EmpId}")
    Call<AddAdjustmentResponse> add_adjustment(@Path("EmpId")String empid,
                                               @Body AddAdjustmentModel addAdjustmentModel);

    @PUT("api/Adjustments/UpdateAdjustments/{id}/{EmpId}")
    Call<UpdateAdjustmentResponse> update_adjustment(@Path(value = "id",encoded = true)String id,
                                                     @Path(value = "EmpId",encoded = true)String empId,
                                                     @Body UpdateAdjustmentModel updateAdjustmentModel);

    @PUT("api/Adjustments/DeleteAdjustments/{id}/{EmpId}")
    Call<String> delete_adjustment(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/Packages/getpackages/{Custid}")
    Call<BasePackagesModel> basePackages(@Path(value = "Custid", encoded = true)String custid);

    @POST("api/Packages/AddPackage/{EmpId}")
    Call<AddBasePackageResponse> add_package(@Path("EmpId")String empid,
                                             @Body AddBasePackageModel addBasePackageModel);

    @PUT("api/Packages/UpdatePackage/{id}/{EmpId}")
    Call<UpdateBasePackageResponse> update_package(@Path(value ="id",encoded = true)String id,
                                                   @Path(value = "EmpId", encoded = true)String empid,
                                                   @Body UpdateBasePackageModel updateBasePackageModel);

    @PUT("api/Packages/DeletePackage/{id}/{EmpId}")
    Call<String> delete_package(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/AdditionalCharges/getaddcharges/{Custid}")
    Call<AdditionalChargeListModel> additionalCharges(@Path(value = "Custid", encoded = true)String custid);

    @POST("api/AdditionalCharges/AddAdditionalCharges/{EmpId}")
    Call<AdditionalChargeResponse> add_additionalCharge(@Path("EmpId")String empid,
                                                        @Body AdditionalChargesModel additionalChargesModel);

    @PUT("api/AdditionalCharges/UpdateAdditionalCharges/{id}/{EmpId}")
    Call<UpdateAdditionalChargesResponse> update_additionalCharge(@Path(value ="id",encoded = true)String id,
                                                                  @Path(value = "EmpId", encoded = true)String empid,
                                                                  @Body UpdateAdditionalChargesModel updateAdditionalChargesModel);

    @PUT("api/AdditionalCharges/DeleteAdditionalCharges/{id}/{EmpId}")
    Call<String> delete_additionalCharge(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/Discounts/getdiscounts/{Custid}")
    Call<String> discount_list(@Path(value = "Custid", encoded = true)String custid);

    @POST("api/Discounts/AddDiscounts/{EmpId}")
    Call<AddDiscountResponse> add_discount(@Path("EmpId")String empid,
                                           @Body AddDiscountModel addDiscountModel);

    @PUT("api/Discounts/UpdateDiscounts/{id}/{EmpId}")
    Call<UpdateDiscountResponse> update_discount(@Path(value ="id",encoded = true)String id,
                                                 @Path(value = "EmpId", encoded = true)String empid,
                                                 @Body UpdateDiscountModel updateDiscountModel);

    @PUT("api/Discounts/DeleteDiscount/{id}/{EmpId}")
    Call<String> delete_discount(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/Classes/getclasses/{Custid}")
    Call<String> programs_list(@Path(value = "Custid", encoded = true)String custid);


//    @POST("api/Classes/AddClass/{EmpId}")
//    Call<AddProgramsResponse> add_programs(@Part("EmpId") String empid,
//                                           @Body AddProgramsModel addProgramsModel);

//    @Multipart
//    @POST("api/Classes/AddClass/{EmpId}")
//    Call<AddProgramsResponse> add_programs(@Path("EmpId")String empid,
//                                           @PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("api/Classes/AddClass/{EmpId}")
    Call<AddProgramsResponse> add_programs(@Path("EmpId") String empid,
                                                            @Part MultipartBody.Part file,
                                                            @Part("File") RequestBody name,
                                                            @PartMap Map<String, RequestBody> params);

    @Multipart
    @PUT("api/Classes/UpdateClass/{id}/{EmpId}")
    Call<UpdateProgramResponse> update_program(@Path("id") String id,
                                               @Path("EmpId") String empid,
                                               @Part MultipartBody.Part file,
                                               @Part("File") RequestBody name,
                                               @PartMap Map<String, RequestBody> params);

//    @Multipart
//    @POST("api/Classes/UpdateClass/{id}/{EmpId}")
//    Call<UpdateProgramResponse> update_program(@Path("id") String id,
//                                             @Path("EmpId") String empid,
//                                           @Part MultipartBody.Part file,
//                                           @Part("File") RequestBody name,
//                                           @PartMap Map<String, RequestBody> params);

//    @Multipart
//    @PUT("api/Classes/UpdateClass/{id}/{EmpId}")
//    Call<UpdateProgramResponse> update_program(@Path(value ="id",encoded = true)String id,
//                                               @Path(value = "EmpId", encoded = true)String empid,
//                                               @PartMap Map<String, RequestBody> params);

//    @PUT("api/Classes/UpdateClass/{id}/{EmpId}")
//    Call<UpdateProgramResponse> update_program(@Path(value ="id",encoded = true)String id,
//                                               @Path(value = "EmpId", encoded = true)String empid,
//                                               @Body UpdateProgramModel updateProgramModel);

    @PUT("api/Classes/DeleteClass/{id}/{EmpId}")
    Call<String> delete_program(@Path("id")String id,@Path("EmpId")String empid);

    @GET("api/Immunizations/getImmunization/{Custid}")
    Call<String> immunizationList(@Path(value = "Custid", encoded = true)String custid);

    @POST("api/Immunizations/AddImmunization/{EmpId}")
    Call<AddImmunizationResponse> add_immunization(@Path("EmpId")String empid,
                                                   @Body AddImmunizationModel addImmunizationModel);

    @PUT("api/Immunizations/UpdateImmunization/{id}/{EmpId}")
    Call<UpdateImmunizationResponse> update_immunization(@Path(value ="id",encoded = true)String id,
                                                         @Path(value = "EmpId", encoded = true)String empid,
                                                         @Body UpdateImmunizationModel updateImmunizationModel);

    @PUT("api/Immunizations/DeleteImmunization/{id}/{EmpId}")
    Call<String> delete_immunization(@Path("id")String id,@Path("EmpId")String empid);


    @GET("api/Classes/getclassdropdown/{Custid}")
    Call<GetClassListResponseModel> classDropdown(@Path(value = "Custid",encoded = true)
                                                          String Custid);
}
package com.rc.ac.retrofit;


import com.rc.ac.model.Appointment;
import com.rc.ac.model.ParamsAddAppointment;
import com.rc.ac.model.ParamsAppUser;
import com.rc.ac.model.ParamsAppointment;
import com.rc.ac.model.RespAppointment;
import com.rc.ac.model.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public interface APIInterface {

    @POST("bepza-122/CmApps/login")
    Call<User> apiUserLogin(@Query("username") String username,
                           @Query("password") String password);

    @POST("bepza-122/CmApps/index")
    Call<RespAppointment> apiGetAppointmentList(@Query("token") String token,
                                                @Query("fromDate") String fromDate);

    @POST("bepza-122/CmApps/saveData")
    Call<APIResponse> apiAddAppointment(@Query("token") String token,
                                @Query("aDate") String appDate,
                                @Query("aSlNo") String appSl,
                                @Query("aTime") String appTime,
                                @Query("aWith") String appDes,
                                @Query("aRemarks") String appRemark);

    @POST("bepza-122/CmApps/updateData")
    Call<APIResponse> apiUpdateAppointment(@Query("token") String token,
                                   @Query("aId") String appId,
                                   @Query("aDate") String appDate,
                                   @Query("aSlNo") String appSl,
                                   @Query("aTime") String appTime,
                                   @Query("aWith") String appDes,
                                   @Query("aRemarks") String appRemark);

    @POST("bepza-122/CmApps/deleteAppointmentInstance")
    Call<APIResponse> apiDeleteAppointment(@Query("token") String token,
                                           @Query("aId") String appId);
}
package com.hjq.demo.service;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface DeviceService {

    /**
     * 获取token
     */
    @GET("api/device-auth/{sn}")
    Observable<Map<String, Object>> getToken(@Path("sn") String sn);

    /**
     * 上传deviceInfo
     */
    @POST("api/device-info")
    Observable<Map<String, Object>> putDevice(
            @Query("device_sn") String device_sn,
            @Query("content") String content);
}

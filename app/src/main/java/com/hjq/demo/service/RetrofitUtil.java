package com.hjq.demo.service;

import com.hjq.demo.utils.GsonUtil;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {

    public static final int TIMEOUT = 60;
    private static OkHttpClient mOkHttpClient;

    /**
     * 超时时间
     */
    private static volatile RetrofitUtil mInstance;
    private DeviceService deviceService;
    /**
     * 单例封装
     *
     * @return
     */
    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                }
            }
        }
        return mInstance;
    }

    public RetrofitUtil() {
        this.deviceService = initRetrofit();
    }

    /**
     * 初始化Retrofit
     */
    public DeviceService initRetrofit() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(initOKHttp())
                // 设置请求的域名
                .baseUrl("http://genstar.bctcn.net")
                // 设置解析转换工厂
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return mRetrofit.create(DeviceService.class);
    }

    /**
     * 全局httpclient
     *
     * @return
     */
    public static OkHttpClient initOKHttp() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)//设置写入超时时间
//                    .addInterceptor(InterceptorUtil.LogInterceptor())//添加日志拦截器
                    //cookie
//                    .addInterceptor(new CookieReadInterceptor())
//                    .addInterceptor(new CookiesSaveInterceptor())
                    .build();
        }
        return mOkHttpClient;
    }

    public Observable<Map<String, Object>> getToken(String sn) {
        return deviceService.getToken(sn);
    }

    public Observable<Map<String, Object>> putDevice(String sn, Map<String, Object> deviceInfo) {
        return deviceService.putDevice(sn, GsonUtil.getGson().toJson(deviceInfo));
    }
}

package com.hjq.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.hjq.demo.R;
import com.hjq.demo.mananger.CameraManager;
import com.hjq.demo.mananger.MachineManager;
import com.hjq.demo.mananger.NetworkManager;
import com.hjq.demo.model.BasicModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DeviceUploadService extends Service {

    protected Handler mHandler;
    private long mInterval = TimeUnit.MINUTES.toMillis(5);
    private Runnable mAction = new Runnable() {

        @Override
        public void run() {
            Timber.d("tid:%d \"%s\" is running", Thread.currentThread().getId(), Thread.currentThread().getName());
            getToken();
            mHandler.postDelayed(mAction, mInterval);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HandlerThread thread = new HandlerThread(getClass().getSimpleName() + " work thread");
        thread.start();
        mHandler = new Handler(thread.getLooper());
        mHandler.post(mAction);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private void getToken() {
        RetrofitUtil.getInstance().getToken(MachineManager.getInstance().getSn())
                .map(new Function<Map<String, Object>, String>() {
                    @Override
                    public String apply(Map<String, Object> response) throws Exception {
                        Timber.d("");
                        Boolean isSuccess = (boolean)response.get("success");
                        if(isSuccess) {
                            String token = (String)response.get("token");
                            return token;
                        } else {
                            throw new RuntimeException("get token failed.");
                        }
                    }
                })
                .flatMap(new Function<String, ObservableSource<Map<String, Object>>>() {
                    @Override
                    public ObservableSource<Map<String, Object>> apply(String token) throws Exception {
                        Map<String, Object> params = new HashMap<>();
                        params.put("token", token);
                        params.put("brand", Build.BRAND);
                        params.put("model", Build.MODEL);
                        params.put("deviceSn", MachineManager.getInstance().getSn());
                        params.put("wifiMac", NetworkManager.getInstance().getWifiMac());
                        params.put("bluetoothMac", NetworkManager.getInstance().getBluetoothMac());
                        params.put("imei", NetworkManager.getInstance().getImei());
                        long timer = SystemClock.elapsedRealtime();
                        params.put("workingTime", timer);

                        params.put("androidVersion", MachineManager.getInstance().getAndroidVersion());
                        params.put("firmwareVersion", MachineManager.getInstance().getFirmwareVersion());
                        params.put("workingTime", timer);

                        params.put("cameraSn", CameraManager.getInstance().getCameraSn());
                        return RetrofitUtil.getInstance().putDevice(MachineManager.getInstance().getSn(), params);
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Map<String, Object>>() {
                    @Override
                    public void accept(Map<String, Object> stringObjectMap) throws Exception {
                        Timber.d("");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.d("");
                    }
                });
    }
}

package com.hjq.demo.service;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.hjq.demo.R;
import com.hjq.demo.mananger.CameraManager;
import com.hjq.demo.mananger.HardwareManager;
import com.hjq.demo.mananger.MachineManager;
import com.hjq.demo.mananger.NetworkManager;
import com.hjq.demo.model.BasicModel;
import com.hjq.demo.utils.CommonUtils;

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
    private long mInterval = TimeUnit.MINUTES.toMillis(1);
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
                        params.put("device_sn", MachineManager.getInstance().getSn());
                        params.put("camera_sn", CameraManager.getInstance().getCameraSn());

                        params.put("wireless_mac", NetworkManager.getInstance().getWifiMac());
                        params.put("wire_mac", NetworkManager.getInstance().getEthernetMac());
                        params.put("wire_ip", NetworkManager.getInstance().getEhernetIp());
                        params.put("bluetooth_mac", NetworkManager.getInstance().getBluetoothMac());
                        params.put("4G_imei", NetworkManager.getInstance().getImei());

                        params.put("real_longitude", "");
                        params.put("real_latitude", "");
                        params.put("reg_time", "");
                        params.put("reg_statue", "");
                        params.put("leave_time", "");
                        params.put("belong_custom", "");
                        params.put("hwmanager_version", MachineManager.getInstance().getVersionName());
                        params.put("android_version", MachineManager.getInstance().getAndroidVersion());
                        params.put("rom_version", MachineManager.getInstance().getFirmwareVersion());

                        params.put("wireless_statue", NetworkManager.getInstance().isWifiConnected()?"已连接":"未连接");
                        WifiInfo wifiInfo = NetworkManager.getInstance().getWifiInfo();
                        params.put("wireless_ssid", wifiInfo.getSSID());
                        params.put("wireless_ip", CommonUtils.int2ip(wifiInfo.getIpAddress()));
                        params.put("signal_strength", wifiInfo.getRssi() + "db");

                        params.put("wire_status", NetworkManager.getInstance().getNetworkState().name());
                        params.put("wire_ip", NetworkManager.getInstance().getEhernetIp());

                        params.put("4G_statue", "");
                        params.put("4G_ip", "");
                        params.put("4G_strength", "");



                        params.put("android_ver", MachineManager.getInstance().getAndroidVersion());
                        params.put("firmware_ver", MachineManager.getInstance().getFirmwareVersion());

                        params.put("panel_size", HardwareManager.getInstance().getScreenSize());
                        params.put("panel_resolution", HardwareManager.getInstance().getScreenResolve());
                        params.put("CPU_model", HardwareManager.getInstance().getCpu());
                        params.put("ram_size", HardwareManager.getInstance().getMemory());
                        params.put("rom_size", HardwareManager.getInstance().getFlash());
                        params.put("speaker_power", HardwareManager.getInstance().getSpeakerPower());
                        params.put("wifi_version", HardwareManager.getInstance().getWifiVer());
                        params.put("bluetooth_version", HardwareManager.getInstance().getBluetoothVer());


                        params.put("app_name", MachineManager.getInstance().getAppName());
                        params.put("package_name", MachineManager.getInstance().getPackageName());
                        params.put("app_version", MachineManager.getInstance().getVersionName());

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

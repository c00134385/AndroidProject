package com.hjq.demo.mananger;

import android.content.Context;
import android.os.Build;

import com.hjq.demo.utils.CommonUtils;

public class MachineManager {
    private static MachineManager ourInstance;
    private Context context;

    private String model;
    private String brand;
    private String sn;
    private int versionCode;
    private String versionName;
    private String imei;
    private long powerOnTime;
    private String extra;
    private String androidVersion;
    private String firmwareVersion;

    public static MachineManager getInstance() {
        if(null == ourInstance) {
            throw new RuntimeException("MachineManager not intialized.");
        }
        return ourInstance;
    }

    private MachineManager(Context context) {
        this.context = context;
        model = Build.MODEL;
        brand = Build.BRAND;
        sn = Build.SERIAL;
        versionCode = CommonUtils.packageCode(context);
        versionName = CommonUtils.packageVersionName(context);
        androidVersion = Build.VERSION.RELEASE;
        firmwareVersion = Build.DISPLAY;
    }

    public static void init(Context context) {
        ourInstance = new MachineManager(context);
    }

    public static MachineManager getOurInstance() {
        return ourInstance;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getSn() {
        return sn;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getImei() {
        return imei;
    }

    public long getPowerOnTime() {
        return powerOnTime;
    }

    public String getExtra() {
        return extra;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }
}

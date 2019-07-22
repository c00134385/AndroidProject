package com.hjq.demo.mananger;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import com.hjq.demo.utils.CpuUtils;
import com.hjq.demo.utils.Tools;

public class HardwareManager {
    private static HardwareManager ourInstance;
    private Context context;

    private String screenResolve;
    private String screenSize;
    private String cameraFront;
    private String cameraBack;
    private String memory;
    private String flash;
    private String cpu;
    private String cpuHz;

    public static HardwareManager getInstance() {
        if(null == ourInstance) {
            throw new RuntimeException("HardwareManager not intialized.");
        }
        return ourInstance;
    }

    private HardwareManager(Context context) {
        this.context = context;

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        screenResolve = screenWidth + " x " + screenHeight;
        screenSize = "" + getScreenSize(context);

        memory = Tools.getTotalMemory(context) + " " + Tools.getMemoryFree(context);
        flash = Tools.getRomTotalSize(context) + " " + Tools.getRomAvailableSize(context) + " " + Tools.readSystem(context);

        Tools.getDetailInfo(context);
        cpu = Build.CPU_ABI;
        cpuHz = CpuUtils.getMinCPU() + "-" + CpuUtils.getMaxCPU();
//        cameraFront = CameraUtils.getCameraPixels(CameraUtils.hasFrontCamera());
//        cameraBack = CameraUtils.getCameraPixels(CameraUtils.hasBackCamera());
    }

    public static void init(Context context) {
        ourInstance = new HardwareManager(context);
    }

    public static HardwareManager getOurInstance() {
        return ourInstance;
    }

    public Context getContext() {
        return context;
    }

    public String getScreenResolve() {
        return screenResolve;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public String getCameraFront() {
        return cameraFront;
    }

    public String getCameraBack() {
        return cameraBack;
    }

    public String getMemory() {
        return memory;
    }

    public String getFlash() {
        return flash;
    }

    public String getCpu() {
        return cpu;
    }

    public String getCpuHz() {
        return cpuHz;
    }

    public static float getScreenSize(Context mContext) {
        int densityDpi = mContext.getResources().getDisplayMetrics().densityDpi;
        float scaledDensity = mContext.getResources().getDisplayMetrics().scaledDensity;
        float density = mContext.getResources().getDisplayMetrics().density;
        float xdpi = mContext.getResources().getDisplayMetrics().xdpi;
        float ydpi = mContext.getResources().getDisplayMetrics().ydpi;
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int height = mContext.getResources().getDisplayMetrics().heightPixels;

        // 这样可以计算屏幕的物理尺寸
        float width2 = (width / densityDpi)*(width / densityDpi);
        float height2 = (height / densityDpi)*(width / densityDpi);

        return (float) Math.sqrt(width2+height2);
    }

}

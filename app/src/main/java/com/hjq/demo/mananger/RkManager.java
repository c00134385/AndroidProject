package com.hjq.demo.mananger;

import android.content.Context;

import com.ys.rkapi.MyManager;

public class RkManager {
    private static RkManager ourInstance;

    public static RkManager getInstance() {
        if(null == ourInstance) {
            throw new RuntimeException("RkManager not instanced.");
        }
        return ourInstance;
    }

    private RkManager(Context context) {
        myManager = MyManager.getInstance(context);
        myManager.bindAIDLService(context);
    }

    public static void init(Context context) {
        ourInstance = new RkManager(context);
    }

    private MyManager myManager;

    public String getMac() {
        return myManager.getEthMacAddress();
    }

    public int open() {
        try {
            myManager.ethEnabled(true);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public int close() {
        try {
            myManager.ethEnabled(false);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public boolean getEthStatus() {
//        return myManager.getEthStatus();
        return true;
    }

    public String getIp() {
        if(myManager.getEthMode().equalsIgnoreCase("StaticIp")) {
            return myManager.getStaticEthIPAddress();
        } else {
            return myManager.getDhcpIpAddress();
        }
    }

    public String getGateway() {
        try {
            return myManager.getEthMode();
        } catch (Exception e) {
            return null;
        }
    }

    public String getNetmask() {
        return "";
    }


}

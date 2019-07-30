package com.hjq.demo.mananger;

import android.app.ZysjSystemManager;
import android.content.Context;

import timber.log.Timber;

public class EthernetManager {

    private static EthernetManager ourInstance;

    public static EthernetManager getInstance() {
        if(null == ourInstance) {
            throw new RuntimeException("EthernetManager not instanced.");
        }
        if(null == ourInstance.manager) {
            throw new RuntimeException("ZysjSystemManager not instanced.");
        }
        return ourInstance;
    }

    private Context context;

    public EthernetManager(Context context) {
        this.context = context;
        manager=(ZysjSystemManager)context.getSystemService("zysj");
        Timber.d("");
    }

    public static void init(Context context) {
        ourInstance = new EthernetManager(context);
    }

    private ZysjSystemManager manager;

    public int open() {
        try {
            return manager.ZYsetEthTurnOn();
        } catch (Exception e) {
            return -1;
        }
    }

    public int close() {
        try {
            return manager.ZYsetEthTurnOff();
        } catch (Exception e) {
            return -1;
        }
    }

    //获取以太网 IP 地址
    public String getIp() {
        try {
            return manager.ZYgetEthIp();
        } catch (Exception e) {
            return null;
        }
    }

    public String getMac() {
        try {
            return manager.ZYgetEthMacAddress();
        } catch (Exception e) {
            return null;
        }
    }

    public String getGateway() {
        try {
            return manager.ZYgetEthGatWay();
        } catch (Exception e) {
            return null;
        }
    }

    public String getNetmask() {
        try {
            return manager.ZYgetEthNetMask();
        } catch (Exception e) {
            return null;
        }
    }

    public String getDns1() {
        try {
            return manager.ZYgetEthDns1();
        } catch (Exception e) {
            return null;
        }
    }

    public String getDns2() {
        try {
            return manager.ZYgetEthDns2();
        } catch (Exception e) {
            return null;
        }
    }
}

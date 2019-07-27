package com.hjq.demo.mananger;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.hjq.demo.utils.CommonUtils;

import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import timber.log.Timber;

/**
 * Created by elegant on 16/3/6.
 */
public class NetworkManager {
    private static NetworkManager instance;
    private static final String TAG = NetworkManager.class.getSimpleName();
    public static final String ETHERNET_SERVICE_NAME = "ethernet";
    public static final String WIFI_INTERFACE = "wlan0";
    public static final String ETHERNET_INTERFACE = "eth0";
    public static int ETHERNET_ENABLED = 2;

    private Context context;
    private String wifiMac;
    private String ethernetMac;
    private String bluetoothMac;
    private String ipAddress;
    private String userMac;

    private String wifiIp;
    private String ethernetIp;

    private ConnectivityManager connectivityManager;
    private WifiManager wifiManager;
    //    private EthernetManager ethernetManager;
    private TelephonyManager telephonyManager;
    private BluetoothAdapter ba;

    private volatile ENetworkState networkState;
    private volatile EConnectionState connectionState;

    private NetworkManager(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        /*TODO:初始化有线网络管理类
          在android中类名为android.net.EthernetManager，在mele中是android.net.ethernet.EthernetManager
          本次使用mele固件中的类，后续确定具体合作商后再修改
         */

        ba = BluetoothAdapter.getDefaultAdapter();
//        try {
//            ethernetManager = (EthernetManager) context.getSystemService(ETHERNET_SERVICE_NAME);
//        } catch (Exception e) {
//        }
        refreshNetworkInfo();
        networkState = getNetworkState();
        connectionState = EConnectionState.NORMAL;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new NetworkManager(context);
        }
        instance.refreshNetworkInfo();
    }

    public static NetworkManager getInstance() {
        if (instance == null) {
            throw new RuntimeException(NetworkManager.class.getSimpleName() + " not intialized.");
        }
        return instance;
    }

    public void AsyncRefreshNetworkInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                refreshNetworkInfo();
            }
        }).start();
    }

    //刷新网络信息
    public void refreshNetworkInfo() {
        //初始化mac
        byte[] mac = null;
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (WIFI_INTERFACE.equals(networkInterface.getDisplayName())) {
                    wifiMac = parseMac(networkInterface.getHardwareAddress());
                    wifiIp = validIpOnNetworkInterface(networkInterface);
                    continue;
                }
                if (ETHERNET_INTERFACE.equals(networkInterface.getDisplayName())) {
                    ethernetMac = parseMac(networkInterface.getHardwareAddress());
                    ethernetIp = validIpOnNetworkInterface(networkInterface);
                    continue;
                }
            }
            if (TextUtils.isEmpty(wifiMac)) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String tmpWifiMac = wifiInfo.getMacAddress(); //如果wifi未打开会返回 020000000000
                if (!TextUtils.isEmpty(tmpWifiMac) && !TextUtils.equals("020000000000", tmpWifiMac) && !TextUtils.equals("02:00:00:00:00:00", tmpWifiMac)) {
                    wifiMac = tmpWifiMac;
                    wifiIp = CommonUtils.int2ip(wifiInfo.getIpAddress());
                }
            }
            if (TextUtils.isEmpty(ethernetMac)) {
                ethernetMac = getMacFromFile(); // 从文件中获取
            }

            if (ba != null) {
//                bluetoothMac = ba.getAddress();
                bluetoothMac = getBluetoothAddress(ba);
                if(null != bluetoothMac) {
                    bluetoothMac = bluetoothMac.toLowerCase();
                }
            }
        } catch (Exception e) {
            Timber.e(e, "refreshNetworkInfo failed");
        }
    }

    //解析mac
    private String parseMac(byte[] mac) {
        StringBuffer sb = new StringBuffer();
        if (mac != null) {
            sb.delete(0, sb.length());
            for (int i = 0; i < mac.length; i++) {
                sb.append(parseByte(mac[i]));
            }
            return sb.substring(0, sb.length() - 1);
        }
        return null;
    }

    //获取有效的IP
    private String validIpOnNetworkInterface(NetworkInterface networkInterface) {
        String ip = null;
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address.isAnyLocalAddress() || address.isLoopbackAddress() || !(address instanceof Inet4Address) || !address.isSiteLocalAddress()) {
                continue;
            }
            ip = address.toString().substring(1);
        }
        return ip;
    }

    //字节转字符
    private String parseByte(byte b) {
        String s = "00" + Integer.toHexString(b) + ":";
        return s.substring(s.length() - 3);
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public String getEthernetMac() {
        return ethernetMac;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }

    private String getBluetoothAddress(BluetoothAdapter adapter) {
        if (adapter == null) {
            return null;
        }

        Class<? extends BluetoothAdapter> btAdapterClass = adapter.getClass();
        try {
            Field mServiceField = adapter.getClass().getDeclaredField("mService");
            mServiceField.setAccessible(true);
            Object btManagerService = mServiceField.get(adapter);
            if (btManagerService != null) {
                return (String) btManagerService.
                        getClass().getMethod("getAddress").invoke(btManagerService);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Timber.e(e, e.getMessage());
            return null;
        }
    }

    public String getIpAddress() {
        return (wifiIp == null || wifiIp.equals("0.0.0.0")) ? ethernetIp : wifiIp;
    }

    public String getEhernetIp() {
        return ethernetIp;
    }

    public String getWifiIp() {
        return wifiIp;
    }

    public String getUserMac() {
        String mac = null;
        if ((mac = StringUtils.trimToNull(ethernetMac)) == null) {
            mac = StringUtils.trimToNull(wifiMac);
        }
        if (mac == null) return "";
        return StringUtils.replace(mac, ":", "").toLowerCase();
    }

    /**
     * Wifi是否连接
     */
    public boolean isWifiConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() != ConnectivityManager.TYPE_ETHERNET;
    }

    /**
     * Ethernet是否连接
     */
    public boolean isEthernetConnected() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET;
    }

//    @SuppressLint("WrongConstant")
//    public boolean initEthernetManager(Context context) {
//        if (ethernetManager != null) {
//            return false;
//        }
//        try {
//            ethernetManager = (EthernetManager) context.getSystemService(ETHERNET_SERVICE_NAME);
//        } catch (Exception e) {
//            Toast.makeText(context, "No ethernetmanager found!", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//            return false;
//        }
//        return ethernetManager != null;
//    }

//    public int getEthState() {
//        if (ethernetManager == null) {
//            return 1;
//        }
//        return ethernetManager.getEthState();
//    }
//
//    public DhcpInfo getEthernetDhcpInfo() {
//        if(null != ethernetManager) {
//            return ethernetManager.getDhcpInfo();
//        }
//        return null;
//    }
//
//    public boolean existsEthernetManager() {
//        return ethernetManager != null;
//    }
//
//    public boolean isEthDeviceUp() {
//        return ethernetManager.isEthDeviceUp();
//    }
//
//    public void setEthEnabled(boolean flag) {
//        ethernetManager.setEthEnabled(flag);
//    }

    public ENetworkState getNetworkState() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            switch (networkInfo.getType()) {
                case ConnectivityManager.TYPE_ETHERNET:
                    return ENetworkState.ETHERNET_CONNECTED;
                case ConnectivityManager.TYPE_WIFI:
                    return ENetworkState.WIFI_CONNECTED;
                // 目前缺省都归成WiFi
                default:
                    return ENetworkState.WIFI_CONNECTED;
            }
        } else if (wifiManager.isWifiEnabled()) {
            return ENetworkState.WIFI_DISCONNECTED;
        } else {
            return ENetworkState.ETHERNET_DISCONNECTED;
        }
    }

    public boolean isNetworkConnected() {
//        return isEthernetConnected() || isWifiConnected();
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public String getImei() {
        String imei = "";
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 没有权限
                imei = "";
            } else {
                imei = telephonyManager.getDeviceId();
            }
        } else {
            imei = telephonyManager.getDeviceId();
        }

        return imei;
    }

    public String getAndroidId() {
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidId;
    }

    public static String getMacFromFile() {

        FileInputStream localFileInputStream;
        String mac = "";
        try {
            localFileInputStream = new FileInputStream(
                    "/sys/class/net/eth0/address");
            byte[] arrayOfByte = new byte[17];
            localFileInputStream.read(arrayOfByte, 0, 17);
            mac = new String(arrayOfByte);
            localFileInputStream.close();
            if (mac.contains("-"))
                mac = mac.replace("-", ":").trim();
        } catch (Exception e) {
            Timber.e(e, "getMac by file error!");
            return "";
        }
        if (TextUtils.isEmpty(mac)) {
            Timber.e("getMac by file return empty!");
            return "";
        }

        return mac.toLowerCase();
    }

    public static class ConnectionEvent {
        String message;
        EConnectionState state;

        public ConnectionEvent(String msg, EConnectionState pState) {
            message = msg;
            state = pState;
        }

        public String getMessage() {
            return message;
        }

        public EConnectionState getState() {
            return state;
        }

    }

    public enum EConnectionState {
        NORMAL,
        DISCONNECTED,
        ERROR
    }

    public enum ENetworkState {
        WIFI_CONNECTED,
        ETHERNET_CONNECTED,
        WIFI_DISCONNECTED,
        ETHERNET_DISCONNECTED,
        SERVER_DISCONNECTED
    }

    public EConnectionState getConnectionState() {
        return connectionState;
    }


}

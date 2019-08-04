package com.hjq.demo.ui.service;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.chice.scangun.ScanGun;
import com.chice.scangun.ScanGun.ScanGunCallBack;

public class DetectionService extends AccessibilityService {

    final static String TAG = "DetectionService";
    private ScanGun mScanGun = null;

    public class LocalBinder extends Binder {
        DetectionService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DetectionService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInterrupt() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            if (keyCode <= 6) {
                return false;
            }
            if (mScanGun.isMaybeScanning(keyCode, event)) {
                return true;
            }
        }

        return super.onKeyEvent(event);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        mScanGun = new ScanGun(new ScanGunCallBack() {

            @Override
            public void onScanFinish(String scanResult) {
                if (!TextUtils.isEmpty(scanResult)) {
                    Toast.makeText(DetectionService.this.getBaseContext(),
                            "监听扫描枪数据:" + scanResult, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mScanGun.setMaxKeysInterval(50);
        super.onCreate();
    }

    /**
     * 检测输入设备是否是扫码器
     *
     * @param context
     * @return 是的话返回true，否则返回false
     */
    public boolean isInputFromScanner(Context context, KeyEvent event) {
        if (event.getDevice() == null) {
            return false;
        }
//        event.getDevice().getControllerNumber();
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            //实体按键，若按键为返回、音量加减、返回false
            return false;
        }
        if (event.getDevice().getSources() == (InputDevice.SOURCE_KEYBOARD | InputDevice.SOURCE_DPAD | InputDevice.SOURCE_CLASS_BUTTON)) {
            //虚拟按键返回false
            return false;
        }
        Configuration cfg = context.getResources().getConfiguration();
        return cfg.keyboard != Configuration.KEYBOARD_UNDEFINED;
    }

}

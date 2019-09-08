package com.hjq.demo.mananger;

import android.content.Context;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;

import timber.log.Timber;

public class CameraManager extends BaseManager {

    private static CameraManager ourInstance;
    private String cameraSn = "";

    private MainListener mainlistener;
    private ImiDevice mDevice;
    private ImiDeviceAttribute mDeviceAttribute = null;

    private static final int DEVICE_OPEN_SUCCESS = 0;
    private static final int DEVICE_OPEN_FALIED = 1;
    private static final int DEVICE_DISCONNECT = 2;
    private static final int MSG_EXIT = 5;

    public static CameraManager getInstance() {
        if(null == ourInstance) {
            throw new RuntimeException("CameraManager not intialized.");
        }
        return ourInstance;
    }

    protected CameraManager(Context context) {
        super(context);
    }

    public static void init(Context context) {
        ourInstance = new CameraManager(context);
    }

    private Runnable mAction = new Runnable() {

        @Override
        public void run() {
            Timber.d("tid:%d \"%s\" is running", Thread.currentThread().getId(), Thread.currentThread().getName());
            int ret = ImiNect.initialize();
            Timber.d("ret:%d", ret);
            mDevice = ImiDevice.getInstance();
            mainlistener = new MainListener();
            mDevice.addDeviceStateListener(mainlistener);

            ImiFrameMode colorMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_IMAGE_RGB24, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.COLOR, colorMode);

            ImiFrameMode depthMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_DEP_16BIT, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.DEPTH, depthMode);

            mDevice.open(mContext, ImiDevice.ImiStreamType.COLOR.toNative() | ImiDevice.ImiStreamType.DEPTH.toNative(), mainlistener);
        }
    };

    private Runnable mCloseAction = new Runnable() {

        @Override
        public void run() {
            Timber.d("tid:%d \"%s\" is running", Thread.currentThread().getId(), Thread.currentThread().getName());
            if(mDevice != null) {
                mDevice.close();
                mDevice = null;
                ImiDevice.destroy();
            }
            ImiNect.destroy();
        }
    };

    public void start() {
        mHandler.removeCallbacks(mAction);
        mHandler.post(mAction);
    }

    private class MainListener implements ImiDevice.OpenDeviceListener, ImiDevice.DeviceStateListener {

        @Override
        public void onOpenDeviceSuccess() {
            //open device success.
            Timber.d("onOpenDeviceSuccess");
            mDeviceAttribute = mDevice.getAttribute();
            cameraSn = mDeviceAttribute.getSerialNumber();
            mHandler.post(mCloseAction);
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            //open device falied.
            cameraSn = "invalid sn";
            Timber.d("onOpenDeviceFailed");
        }

        @Override
        public void onDeviceStateChanged(String deviceInfo, ImiDeviceState state) {
            Timber.d("onDeviceStateChanged");
        }
    }

    public String getCameraSn() {
        return cameraSn;
    }
}

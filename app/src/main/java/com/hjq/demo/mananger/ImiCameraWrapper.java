package com.hjq.demo.mananger;

import android.content.Context;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;

import timber.log.Timber;

public class ImiCameraWrapper {

    Context context;
    ImiCallback callback;

    private MainListener mainlistener;
    private ImiDevice mDevice;
    private ImiDeviceAttribute mDeviceAttribute = null;
    private String cameraSn;
    private String cameraName;

    public ImiCameraWrapper(Context context, ImiCallback callback) {
        this.context = context;
        this.callback = callback;
        new Thread(new OpenDeviceRunnable()).start();
    }

    private class OpenDeviceRunnable implements Runnable {

        @Override
        public void run() {
            //get iminect instance.
            ImiNect.get_DriverInfo();

            int ret = ImiNect.initialize();
            Timber.d("ret:%d", ret);
            mDevice = ImiDevice.getInstance();
            mainlistener = new MainListener();
            mDevice.addDeviceStateListener(mainlistener);

            ImiFrameMode colorMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_IMAGE_RGB24, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.COLOR, colorMode);

            ImiFrameMode depthMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_DEP_16BIT, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.DEPTH, depthMode);

            mDevice.open(context, ImiDevice.ImiStreamType.COLOR.toNative() | ImiDevice.ImiStreamType.DEPTH.toNative(), mainlistener);
        }
    }

    private class MainListener implements ImiDevice.OpenDeviceListener, ImiDevice.DeviceStateListener {

        @Override
        public void onOpenDeviceSuccess() {
            //open device success.
            mDeviceAttribute = mDevice.getAttribute();
            cameraSn = mDeviceAttribute.getSerialNumber();
            cameraName = mDeviceAttribute.getDeviceDescribeName();
            if(null != callback) {
                callback.onImiInfo(cameraName, cameraSn);
            }
            closeCamera();
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            //open device falied.
            if(null != callback) {
                callback.onError(errorMsg);
            }
            closeCamera();
        }

        @Override
        public void onDeviceStateChanged(String deviceInfo, ImiDeviceState state) {
        }
    }

    private void closeCamera() {
        if(mDevice != null) {
            mDevice.close();
            mDevice = null;
            ImiDevice.destroy();
        }
        ImiNect.destroy();
    }

    public String getCameraSn() {
        return cameraSn;
    }

    public String getCameraName() {
        return cameraName;
    }

    public interface ImiCallback {
        void onImiInfo(String name, String sn);

        void onError(String errMsg);
    }
}

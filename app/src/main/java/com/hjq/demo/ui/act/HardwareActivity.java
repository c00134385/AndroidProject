package com.hjq.demo.ui.act;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;
import com.hjq.demo.R;
import com.hjq.demo.adapter.ListAdapter;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.HardwareManager;
import com.hjq.demo.mananger.ImiCameraManager;
import com.hjq.demo.mananger.ImiCameraWrapper;
import com.hjq.demo.model.BasicModel;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class HardwareActivity extends MyActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private List<BasicModel> basicModels;
    private ListAdapter listAdapter;
    private BasicModel cameraSnModel;
    private BasicModel cameraNameModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hardware;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        basicModels = new ArrayList<>();
        basicModels.add(new BasicModel(getString(R.string.screen_resolve), HardwareManager.getInstance().getScreenResolve()));
        basicModels.add(new BasicModel(getString(R.string.screen_size), HardwareManager.getInstance().getScreenSize()));
        cameraNameModel = new BasicModel("摄像头型号", "");
        basicModels.add(cameraNameModel);
        cameraSnModel = new BasicModel("摄像头SN", "");
        basicModels.add(cameraSnModel);

//        basicModels.add(new BasicModel(getString(R.string.camera_back), HardwareManager.getInstance().getCameraBack()));
        basicModels.add(new BasicModel(getString(R.string.memory), HardwareManager.getInstance().getMemory()));
        basicModels.add(new BasicModel(getString(R.string.flash), HardwareManager.getInstance().getFlash()));
        basicModels.add(new BasicModel(getString(R.string.cpu), HardwareManager.getInstance().getCpu()));
        basicModels.add(new BasicModel("喇叭功率", HardwareManager.getInstance().getSpeakerPower()));
        basicModels.add(new BasicModel("WIFI版本", HardwareManager.getInstance().getWifiVer()));
        basicModels.add(new BasicModel("蓝牙版本", HardwareManager.getInstance().getBluetoothVer()));
//        basicModels.add(new BasicModel(getString(R.string.cpu_hz), HardwareManager.getInstance().getCpuHz()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        listAdapter = new ListAdapter(basicModels);
        recyclerView.setAdapter(listAdapter);
//        new Thread(new OpenDeviceRunnable()).start();
        new ImiCameraWrapper(this, new ImiCameraWrapper.ImiCallback() {
            @Override
            public void onImiInfo(String name, String sn) {
                cameraSnModel.setValue(sn);
                cameraNameModel.setValue(name);
                HardwareActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onError(String errMsg) {
                ToastUtils.show(errMsg);
            }
        });
    }

    private MainListener mainlistener;
    private ImiDevice mDevice;
    private ImiDeviceAttribute mDeviceAttribute = null;
    private String cameraSn;
    private String cameraType;

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

            mDevice.open(HardwareActivity.this, ImiDevice.ImiStreamType.COLOR.toNative() | ImiDevice.ImiStreamType.DEPTH.toNative(), mainlistener);
        }
    }

    private class MainListener implements ImiDevice.OpenDeviceListener, ImiDevice.DeviceStateListener {

        @Override
        public void onOpenDeviceSuccess() {
            //open device success.
            mDeviceAttribute = mDevice.getAttribute();
            cameraSn = mDeviceAttribute.getSerialNumber();
            cameraType = mDeviceAttribute.getDeviceDescribeName();
            cameraSnModel.setValue(cameraSn);
            cameraNameModel.setValue(cameraType);
            HardwareActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listAdapter.notifyDataSetChanged();
                }
            });
            closeCamera();
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            //open device falied.
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
}

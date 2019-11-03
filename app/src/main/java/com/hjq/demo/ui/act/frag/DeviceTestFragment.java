package com.hjq.demo.ui.act.frag;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;
import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mananger.ImiCameraWrapper;
import com.hjq.demo.mananger.MachineManager;
import com.hjq.demo.ui.act.t.BaseTestActivity;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class DeviceTestFragment extends MyLazyFragment<BaseTestActivity> {

    @BindView(R.id.tv_device_sn)
    TextView deviceSn;

    @BindView(R.id.img_device_qr)
    ImageView deviceQr;

    @BindView(R.id.tv_camera_sn)
    TextView cameraSn;

    @BindView(R.id.img_camera_qr)
    ImageView cameraQr;

    private MainListener mainlistener;
    private ImiDevice mDevice;
    private ImiDeviceAttribute mDeviceAttribute = null;

    private static final int DEVICE_OPEN_SUCCESS = 0;
    private static final int DEVICE_OPEN_FALIED = 1;
    private static final int DEVICE_DISCONNECT = 2;
    private static final int MSG_EXIT = 5;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_device;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        deviceSn.setText("Device SN: " + MachineManager.getInstance().getSn());
        Observable.just(QRCodeEncoder.syncEncodeQRCode(MachineManager.getInstance().getSn(), 300))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        deviceQr.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show(throwable.getMessage());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(TextUtils.isEmpty(ImiCameraManager.getInstance().getCameraSn())) {
//            new Thread(new OpenDeviceRunnable()).start();
//        } else {
//            updateCameraSn(ImiCameraManager.getInstance().getCameraSn());
//        }

        new ImiCameraWrapper(getContext(), new ImiCameraWrapper.ImiCallback() {
            @Override
            public void onImiInfo(String name, String sn) {
                updateCameraSn(sn);
            }

            @Override
            public void onError(String errMsg) {
                ToastUtils.show(errMsg);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(mDevice != null) {
//            mDevice.close();
//            mDevice = null;
//            ImiDevice.destroy();
//        }
//        ImiNect.destroy();
    }

    private void updateCameraSn(final String sn) {
        Observable.just(QRCodeEncoder.syncEncodeQRCode(sn, 300))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        cameraSn.setText("Camera SN: " + sn);
                        cameraQr.setImageBitmap(bitmap);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show(throwable.getMessage());
                    }
                });
    }

    private Handler MainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case DEVICE_OPEN_FALIED:
                case DEVICE_DISCONNECT:
//                    showMessageDialog((String) msg.obj);
                    ToastUtils.show((String) msg.obj);
                    break;
                case DEVICE_OPEN_SUCCESS:
//                    runViewer();
                    updateCameraSn(mDeviceAttribute.getSerialNumber());
                    break;
                case MSG_EXIT:
//                    Exit();
                    break;
            }
        }
    };

    private class OpenDeviceRunnable implements Runnable {

        @Override
        public void run() {
            //get iminect instance.

            int ret = ImiNect.initialize();
            Timber.d("ret:%d", ret);
            mDevice = ImiDevice.getInstance();
            mainlistener = new MainListener();
            mDevice.addDeviceStateListener(mainlistener);

            ImiFrameMode colorMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_IMAGE_RGB24, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.COLOR, colorMode);

            ImiFrameMode depthMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_DEP_16BIT, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.DEPTH, depthMode);

            mDevice.open(getContext(), ImiDevice.ImiStreamType.COLOR.toNative() | ImiDevice.ImiStreamType.DEPTH.toNative(), mainlistener);
        }
    }

    private class MainListener implements ImiDevice.OpenDeviceListener, ImiDevice.DeviceStateListener {

        @Override
        public void onOpenDeviceSuccess() {
            //open device success.
            mDeviceAttribute = mDevice.getAttribute();

            MainHandler.sendEmptyMessage(DEVICE_OPEN_SUCCESS);
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            //open device falied.
            MainHandler.sendMessage(MainHandler.obtainMessage(DEVICE_OPEN_FALIED, errorMsg));
        }

        @Override
        public void onDeviceStateChanged(String deviceInfo, ImiDeviceState state) {
            if(state == ImiDeviceState.IMI_DEVICE_STATE_CONNECT) {
                Toast.makeText(getContext(), deviceInfo + " CONNECT", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getContext(), deviceInfo + " DISCONNECT", Toast.LENGTH_LONG).show();
            }
        }
    }
}

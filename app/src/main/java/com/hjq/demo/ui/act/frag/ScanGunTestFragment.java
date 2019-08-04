package com.hjq.demo.ui.act.frag;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.utils.Tools;
import com.hjq.toast.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ScanGunTestFragment extends MyLazyFragment {

//    private ScanGun mScanGun = null;

    @BindView(R.id.btn_start)
    Button btnStart;

    @BindView(R.id.btn_stop)
    Button btnStop;

    @BindView(R.id.tv_scanresult)
    TextView tvScanResult;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_scangun;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.btn_start, R.id.btn_stop})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                startService();
                break;

            case R.id.btn_stop:
                stopService();
                break;
        }
    }

    @Override
    protected void initData() {
// TODO Auto-generated method stub
//        mScanGun = new ScanGun(new ScanGun.ScanGunCallBack() {
//
//            @Override
//            public void onScanFinish(String scanResult) {
//                Timber.d("scan result:%s", scanResult);
//                if (!TextUtils.isEmpty(scanResult)) {
//                    Toast.makeText(getContext(),
//                            "无界面监听扫描枪数据:" + scanResult, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        ScanGun.setMaxKeysInterval(50);
//        Timber.d("ScanGun is ready.....");
    }

    private void startService() {
        Timber.d("startService");

        if(!Tools.isAccessibilitySettingsOn(getContext())) {
            Tools.openAccessibilitySetting(getContext());
        } else {
            ToastUtils.show("扫描枪已打开");
        }
//        Intent intent = new Intent();
//        intent.setClass(getContext(), DetectionService.class);
//        getContext().startService(intent);
    }

    private void stopService() {
        Timber.d("stopService");
//        Intent intent = new Intent();
//        intent.setClass(getContext(), DetectionService.class);
//        getContext().stopService(intent);
    }
}
